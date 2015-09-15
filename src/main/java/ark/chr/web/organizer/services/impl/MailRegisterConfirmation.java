package ark.chr.web.organizer.services.impl;

import ark.chr.web.organizer.dao.IOrganizerUserDao;
import ark.chr.web.organizer.domain.OrganizerUser;
import ark.chr.web.organizer.services.api.IMailRegisterConfirmation;
import ark.chr.web.organizer.services.api.exceptions.RegisterConfirmationExpiredException;
import ark.chr.web.organizer.services.api.exceptions.RegisterConfirmationFailedException;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import org.apache.commons.codec.digest.DigestUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Arek on 2015-07-15.
 */
@Service
@Transactional
@PropertySource("classpath:mailRegisterConfirmation.properties")
public class MailRegisterConfirmation implements IMailRegisterConfirmation {

    private static final Logger logger = LoggerFactory.getLogger(MailRegisterConfirmation.class);
    private static final String REGISTER_CONFIRMATION_PATH = "registerConfirmation.vm";
    private static final long ONE_DAY_IN_MS = 24 * 60 * 60 * 1000;

    @Autowired
    private IOrganizerUserDao userDao;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private VelocityEngine velocityEngine;
    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Value("${noReplyEmailAddress}")
    private String noReplyEmailAddress;
    @Value("${confirmRegisterUrl}")
    private String confirmRegisterUrl;
    @Value("${confirmationKey}")
    private String confirmationKey;

    @Override
    @Async
    public void sendConfirmRegistrationEmail(OrganizerUser userToRegister, Locale locale) {
        isTrue(userToRegister != null);
        isTrue(!userToRegister.isActive(), "user account must be inactive");
        notNull(userToRegister.getDateCreated(), "userToRegister.dateCreated required");
        logger.info("Sending register confirmation e-mail to: " + userToRegister.getLogin());

        String digest = generateRegisterDigest(userToRegister);
        String url = confirmRegisterUrl + "?s="
                + userToRegister.getId() + "&amp;d=" + digest;

        Map<String, Object> model = new HashMap<>();
        model.put("registerConfirmationBody", messageSource);
        model.put("locale", locale);
        model.put("userToRegister", userToRegister);
        model.put("url", url);

        String emailBody = VelocityEngineUtils
                .mergeTemplateIntoString(velocityEngine,
                        REGISTER_CONFIRMATION_PATH, "UTF-8", model);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setSubject(messageSource.getMessage("email.subject", null, locale));
            helper.setTo(userToRegister.getLogin());
            helper.setFrom(noReplyEmailAddress);
            helper.setSentDate(userToRegister.getDateCreated());
            helper.setText(emailBody, true);
        } catch(MessagingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        mailSender.send(message);
    }

    @Override
    public void confirmUserRegistration(Long userId, String digest) throws RegisterConfirmationFailedException {
        isTrue(userId > 0L);
        notNull(digest);
        logger.info("Confirming registration to user with Id: " + userId);
        OrganizerUser userToConfirm = userDao.findOne(userId);
        checkTimeNotExpired(userToConfirm.getDateCreated().getTime());

        String expectedDigest = generateRegisterDigest(userToConfirm);
        if(!digest.equals(expectedDigest)) {
            logger.warn("Wrong digest for user: " + userToConfirm.getLogin());
            throw new RegisterConfirmationFailedException("Wrong digest");
        }

        userToConfirm.setActive(true);
        userDao.save(userToConfirm);
    }

    private String generateRegisterDigest(OrganizerUser userToRegister) {
        return DigestUtils
                .shaHex(userToRegister.getId() + ":" + confirmationKey);
    }

    private void checkTimeNotExpired(long time) throws RegisterConfirmationExpiredException {
        long now = System.currentTimeMillis();
        if((now - time) > ONE_DAY_IN_MS) {
            logger.info("Register confirmation time expired");
            throw new RegisterConfirmationExpiredException();
        }
    }

}

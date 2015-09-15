package ark.chr.web.organizer.services.impl;

import ark.chr.web.organizer.dao.IOrganizerRoleDao;
import ark.chr.web.organizer.dao.IOrganizerUserDao;
import ark.chr.web.organizer.domain.OrganizerRole;
import ark.chr.web.organizer.domain.OrganizerUser;
import ark.chr.web.organizer.services.api.IRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Arek on 2015-07-15.
 */
@Service
@Transactional
public class RegisterService implements IRegisterService {

    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IOrganizerRoleDao roleDao;

    @Autowired
    private IOrganizerUserDao userDao;

    @Override
    public boolean registerUser(OrganizerUser user) {
        if(userDao.findByLogin(user.getLogin()) != null) {
            logger.info("Registration failed. User with login "
                    + user.getLogin() + " already exists.");
            return false;
        }

        user.setActive(false);
        user.setDateCreated(new Date());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        OrganizerRole role = roleDao.findByName("ROLE_USER");
        if (role != null) {
            user.getRoles().add(role);
        }

        userDao.save(user);
        return true;
    }
}

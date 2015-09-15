package ark.chr.web.organizer.services.security;

import ark.chr.web.organizer.dao.IOrganizerUserDao;
import ark.chr.web.organizer.domain.OrganizerUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Arek on 2015-06-28.
 */
@Service("userDetailsService")
@Transactional(readOnly = true)
public class UserDetailsServiceAdapter implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceAdapter.class);

    @Autowired
    private IOrganizerUserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        OrganizerUser user = userDao.findByLogin(login);

        if (user == null) {
            logger.warn("No such user: " + login);
            throw new UsernameNotFoundException("No such user: " + login);
        } else if (user.getRoles().isEmpty()) {
            logger.warn("User " + login + " has no authorities");
            throw new UsernameNotFoundException("User " + login + " has no authorities");
        }

        return new UserDetailsAdapter(user);
    }
}

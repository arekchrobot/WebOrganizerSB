package ark.chr.web.organizer.utils;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Arek on 2015-09-10.
 */
public class CookiesUtil {

    private static final Logger logger = LoggerFactory.getLogger(CookiesUtil.class);
    private static final int EXPIRY = 86400;

    public static void setCookie(String name, String value) {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Cookie cookie = null;

        Cookie[] userCookies = request.getCookies();
        if (userCookies != null && userCookies.length > 0) {
            for (Cookie userCookie : userCookies) {
                if (userCookie.getName().equals(name)) {
                    cookie = userCookie;
                    break;
                }
            }
        }

        if (cookie != null) {
            logger.info("Updating value of existing cookie.");
            cookie.setValue(value);
        } else {
            logger.info("Setting new cookie.");
            cookie = new Cookie(name, value);
            cookie.setPath(request.getContextPath());
        }

        cookie.setMaxAge(EXPIRY);

        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.addCookie(cookie);
    }

    public static Cookie getCookie(String name) {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Cookie cookie;

        Cookie[] userCookies = request.getCookies();
        if (userCookies != null && userCookies.length > 0) {
            for (Cookie userCookie : userCookies) {
                if (userCookie.getName().equals(name)) {
                    logger.info("Returning cookie: " + name);
                    cookie = userCookie;
                    return cookie;
                }
            }
        }
        logger.warn("Cookie not found: " + name);
        return null;
    }
}

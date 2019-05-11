package filecenter.utils;


import login.model.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


public class Utils {


    public static void saveUser(User vu) {

        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        httpRequest.getSession().setAttribute("user", vu);

    }


    public static User getUser() {
        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (User) httpRequest.getSession().getAttribute("user");
    }

}

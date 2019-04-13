package login.service;

import login.model.User;
import org.springframework.stereotype.Service;


public interface LoginService {
    public User getUserinfo(String username, String password);
}

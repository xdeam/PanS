package login.service;

import login.mapper.UserMapper;
import login.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LoginServiceImp implements LoginService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserinfo(String username, String password) {
        User u = userMapper.getUser(username);
        if (u != null && password.equals(u.getPasswd())) {
            return u;
        }
        return null;
    }
}

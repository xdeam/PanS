package filecenter.service;

import filecenter.dao.DeptDao;
import filecenter.dao.UserDao;
import login.model.Dept;
import login.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    DeptDao deptDao;

    public User getUserWithDept(int uid){
        User user=userDao.selectByPrimaryKey(uid);
        if (user!=null){
            Dept dept= deptDao.selectByPrimaryKey(user.getDeptid());
            user.setDept(dept);
        }
        return user;
    }
}

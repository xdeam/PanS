package login.mapper;

import login.model.Dept;
import login.model.User;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
    User getUser(String uname);

    Dept getDept(int deptid);
}

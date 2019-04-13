package login;

import login.mapper.UserMapper;
import login.model.Dept;
import login.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class MapperTest {
    private ClassPathXmlApplicationContext context;

    @Test
    public void test1() {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserMapper userMapper = context.getBean(UserMapper.class);
        //获取全部信息
     /*   List<User> users =userMapper.selectAll();
        Assert.assertNotNull(users);
        System.out.println(users);*/
     /*   User user=userMapper.getUser("abc");
        Assert.assertNotNull(user);
        System.out.println(user);*/
        Dept dept = userMapper.getDept(1);
        System.out.println(dept);
    }
}

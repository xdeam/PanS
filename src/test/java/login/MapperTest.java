package login;

import filecenter.dao.CategoryDetilDao;
import filecenter.model.CategoryDetil;
import filecenter.model.CategoryInfo;
import filecenter.service.CategoryDetilService;
import filecenter.service.CategoryService;
import login.mapper.UserMapper;
import login.model.Dept;
import login.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
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
    @Test
    public void test2() {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");

        CategoryDetilDao categoryDetilDao=context.getBean(CategoryDetilDao.class);
        CategoryDetil categoryDetil=new CategoryDetil();
        categoryDetil.setCategrayId(3);
        categoryDetil.setFileId(2);
        int id1=categoryDetilDao.insert(categoryDetil);
        int id2=categoryDetil.getId();
       // int id2=categoryDetilDao.insertAndGetId(categoryDetil);
        System.out.println("id1"+id1+" id2"+id2);

    }
    @Test
    public void t3(){
        context = new ClassPathXmlApplicationContext("applicationContext.xml");


        CategoryService service=context.getBean(CategoryService.class);

        CategoryInfo categoryInfo=service.select(15);
        categoryInfo.setCreateTime(new Date());
        categoryInfo.setParentId(0);
        categoryInfo.setCategoryName("123");
        service.update(categoryInfo);
    }
}

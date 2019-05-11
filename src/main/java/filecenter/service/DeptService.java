package filecenter.service;

import filecenter.dao.DeptDao;
import filecenter.dao.UserDao;
import login.model.Dept;
import login.model.DeptExample;
import login.model.User;
import login.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeptService {
    @Autowired
    private DeptDao deptDao;
    @Autowired
    private UserDao userDao;

    public List getDeptService(){
        List<Dept> deptList=deptDao.selectByExample(new DeptExample());
        List list=new ArrayList();
        for (Dept dept:deptList){
            String deptName=dept.getDeptName();
          //  List alist=
            Map map=new HashMap();
            map.put("name",deptName);
            List list1=new ArrayList();
            UserExample userExample=new UserExample();
            userExample.createCriteria().andDeptidEqualTo(dept.getDeptid());
            List<User> userList=userDao.selectByExample(userExample);
            for (User user:userList){
                Map map1=new HashMap();
                map1.put("uid",user.getUid());
                map1.put("name",user.getUname());
                map1.put("child",new ArrayList());
                list1.add(map1);
            }
            map.put("child",list1);

            list.add(map);
        }
        List rootList=new ArrayList();
        Map mapx=new HashMap();
        mapx.put("name","所有部门");
        mapx.put("child",list);
        rootList.add(mapx);
        return list;
    }

    public Dept getDept(int deptId){
        return deptDao.selectByPrimaryKey(deptId);
    }
}

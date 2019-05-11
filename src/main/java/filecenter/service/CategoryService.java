package filecenter.service;

import filecenter.dao.CategoryDetilDao;
import filecenter.dao.CategoryInfoDao;
import filecenter.model.CategoryDetil;
import filecenter.model.CategoryInfo;
import filecenter.model.CategoryInfoExample;
import filecenter.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryInfoDao cDao;
    public List  getFCategory(){ //获得0层分类

        CategoryInfoExample categoryInfoExample=new CategoryInfoExample();
        categoryInfoExample.createCriteria().andParentIdEqualTo(0);
        List list=cDao.selectByExample(categoryInfoExample);
        return list;
    }
    public List <CategoryInfo> getSubCategory(int parentId){ //获得子分类

        CategoryInfoExample categoryInfoExample=new CategoryInfoExample();
        categoryInfoExample.createCriteria().andParentIdEqualTo(parentId);
        List list=cDao.selectByExample(categoryInfoExample);
        return list;
    }


    public List <CategoryInfo> getMyCategory(int uid){ //获得我的分类

        CategoryInfoExample categoryInfoExample=new CategoryInfoExample();
        categoryInfoExample.createCriteria().andAutherUidEqualTo(uid);
        List list=cDao.selectByExample(categoryInfoExample);
        return list;
    }


    public List <FileInfo> getCategoryDatils(int cid){ //文件信息

        List list=cDao.findFilesOfCategory(cid);
        return list;
    }

    public void addCategory(CategoryInfo categoryInfo){ //添加分类
        cDao.insert(categoryInfo);
    }
    public void delCategory(int id){ //删除分类
        cDao.deleteByPrimaryKey(id);
    }
    public void  update(CategoryInfo categoryInfo)//修改
    {
        cDao.updateByPrimaryKeySelective(categoryInfo);
    }
    public CategoryInfo select(int cid)//查询
    {

        return cDao.selectByPrimaryKey(cid);
    }

}

package filecenter.service;


import filecenter.dao.CategoryDetilDao;
import filecenter.dao.CategoryInfoDao;
import filecenter.model.CategoryDetil;
import filecenter.model.CategoryDetilExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryDetilService {
    @Autowired
    CategoryDetilDao categoryDetilDao;

    //增加记录
    public void add(CategoryDetil categoryDetil){
        categoryDetilDao.insert(categoryDetil);
    }


    //查询文件是否已存在文件夹
    public Boolean isFileExistInCategory(int categoryId,int fileId){
        CategoryDetilExample categoryDetilExample=new CategoryDetilExample();
        categoryDetilExample.createCriteria().andCategrayIdEqualTo(categoryId).andFileIdEqualTo(fileId);
        List list=categoryDetilDao.selectByExample(categoryDetilExample);
        if (list!=null&&list.size()>0){
            return  true;
        }
        else return false;
    }
    //查询文件是否已存在
    public Boolean isFileExist(int fileId){
        CategoryDetilExample categoryDetilExample=new CategoryDetilExample();
        categoryDetilExample.createCriteria().andFileIdEqualTo(fileId);
        List list=categoryDetilDao.selectByExample(categoryDetilExample);
        if (list!=null&&list.size()>0){
            return  true;
        }
        else return false;
    }

    //查询文件是否已存在
    public List<CategoryDetil> selecCateDtils(int cid){
        CategoryDetilExample categoryDetilExample=new CategoryDetilExample();
        categoryDetilExample.createCriteria().andCategrayIdEqualTo(cid);
        List list=categoryDetilDao.selectByExample(categoryDetilExample);
        return  list;
    }
    //删除
    public void delByCateIdAndFileId (int categoryId,int fileId){
        CategoryDetilExample categoryDetilExample=new CategoryDetilExample();
        categoryDetilExample.createCriteria().andCategrayIdEqualTo(categoryId).andFileIdEqualTo(fileId);
        //categoryDetilExample.setLimit(1);
        categoryDetilDao.deleteByExample(categoryDetilExample);
    }

    //更新
    public void updateByCateIdAndFileId (int categoryId,int fileId,int newCategoryId){
        CategoryDetilExample categoryDetilExample=new CategoryDetilExample();
        categoryDetilExample.createCriteria().andCategrayIdEqualTo(categoryId).andFileIdEqualTo(fileId);
        //categoryDetilExample.setLimit(1);
        CategoryDetil categoryDetil=new CategoryDetil();
        categoryDetil.setCategrayId(newCategoryId);
        categoryDetilDao.updateByExampleSelective(categoryDetil,categoryDetilExample);
    }

}

package filecenter.service;

import filecenter.dao.AccessInfoDao;
import filecenter.model.AccessInfo;
import filecenter.model.AccessInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessService {
    @Autowired
    private AccessInfoDao accessInfoDao;

    //获取单个文件权限列表
    public List getFileAccess(int categoryId, int fileId){
        AccessInfoExample accessInfoExample=new AccessInfoExample();
        accessInfoExample.createCriteria().andFileIdEqualTo(fileId).andCategoryIdEqualTo(categoryId);
        List accessList=accessInfoDao.selectByExample(accessInfoExample);
        return accessList;
    }

    //获取用户文件权限列表
    public List getUserFileAccess(int uid, int fileId){
        AccessInfoExample accessInfoExample=new AccessInfoExample();
        accessInfoExample.createCriteria().andFileIdEqualTo(fileId).andUserIdEqualTo(uid);
        List accessList=accessInfoDao.selectByExample(accessInfoExample);
        return accessList;
    }
    //获取用户分类文件权限列表
    public List getUserCateFileAccess(int uid,int cate, int fileId){
        AccessInfoExample accessInfoExample=new AccessInfoExample();
        accessInfoExample.createCriteria().andCategoryIdEqualTo(cate).andFileIdEqualTo(fileId).andUserIdEqualTo(uid);
        List accessList=accessInfoDao.selectByExample(accessInfoExample);
        return accessList;
    }

    //获取文件分类权限列表
    public List getCateAccess(int categoryId){
        AccessInfoExample accessInfoExample=new AccessInfoExample();
        accessInfoExample.createCriteria().andCategoryIdEqualTo(categoryId);
        List accessList=accessInfoDao.selectByExample(accessInfoExample);
        return accessList;
    }
    //更新文件分类权限列表
    public void updateCateAccess(int uid,int cateId,AccessInfo accessInfo){
        AccessInfoExample accessInfoExample=new AccessInfoExample();
        accessInfoExample.createCriteria().andUserIdEqualTo(uid)
                .andCategoryIdEqualTo(cateId);
       accessInfoDao.updateByExample(accessInfo,accessInfoExample);
    }
    //更新文件权限列表
    public void updateFileAccess(AccessInfo accessInfo){
        AccessInfoExample example=new AccessInfoExample();
        example.createCriteria().andUserIdEqualTo(accessInfo.getUserId()).andFileIdEqualTo(accessInfo.getFileId()).andCategoryIdEqualTo(accessInfo.getCategoryId());
        accessInfoDao.updateByExample(accessInfo,example);
    }
    //保存文件权限列表
    public void save(AccessInfo accessInfo){
      accessInfoDao.insert(accessInfo);
    }

    //取消文件权限列表
    public void del(int accessId){
        accessInfoDao.deleteByPrimaryKey(accessId);
    }

}

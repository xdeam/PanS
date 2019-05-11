package filecenter.service;

import filecenter.dao.FileInfoDao;
import filecenter.model.FileInfo;
import filecenter.model.FileInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileInfoDao fileInfoDao;

    public int addFile(FileInfo fileInfo){
       return fileInfoDao.insert(fileInfo);
    }
    public void delFile(int id){
        fileInfoDao.deleteByPrimaryKey(id);
    }

    public FileInfo selectFile(int id){
        return fileInfoDao.selectByPrimaryKey(id);
    }

    public void update(FileInfo fileInfo){
        fileInfoDao.updateByPrimaryKey(fileInfo);
    }
    public List searchFile(String strLike){
        FileInfoExample example=new FileInfoExample();
        example.createCriteria().andFileNameLike("%"+strLike+"%");
        List fileList=fileInfoDao.selectByExample(example);
        return fileList;
    }
}

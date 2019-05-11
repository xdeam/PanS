package filecenter.controller;

import filecenter.model.AccessInfo;
import filecenter.model.CategoryDetil;
import filecenter.model.CategoryInfo;
import filecenter.model.FileInfo;
import filecenter.service.AccessService;
import filecenter.service.CategoryDetilService;
import filecenter.service.CategoryService;
import filecenter.service.FileService;
import filecenter.utils.PanResult;
import filecenter.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

@RestController
@RequestMapping("pan/file")
public class FileManageController {
    @Autowired
    FileService fileService;
    @Autowired
    CategoryDetilService categoryDetilService;
    @Autowired
    AccessService accessService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    HttpServletRequest request;

    //复制文件
       @RequestMapping(value = "copy/{id}/{toId}", method = {RequestMethod.PUT})
    public PanResult<String> copyFile(@PathVariable("id") Integer id, @PathVariable("id")Integer toId) {
        FileInfo fileInfo=fileService.selectFile(id);

        if (categoryDetilService.isFileExistInCategory(toId,id)){
            fileInfo.setFileName(fileInfo.getFileName()+"copy");
            fileInfo.setUploadTime(new Date());
            fileService.addFile(fileInfo);
        }
        int fileId=fileInfo.getFileId();
        CategoryDetil c=new CategoryDetil();
        c.setFileId(fileId);
        c.setCategrayId(toId);
        categoryDetilService.add(c);


        PanResult<String> result=new PanResult<>();
        result.success("复制成功");


        return result;
    }
    //移动文件
    @RequestMapping(value = "move/{fromId}/{id}/{toId}", method = {RequestMethod.PUT})
    public PanResult<String> moveFile(@PathVariable("id") Integer id,@PathVariable("fromId")Integer fromId,@PathVariable("id")Integer toId) {
        FileInfo fileInfo=fileService.selectFile(id);
        if (fromId!=toId){
            CategoryDetil categoryDetil=new CategoryDetil();
            categoryDetil.setFileId(id);
            categoryDetil.setCategrayId(toId);
            categoryDetilService.updateByCateIdAndFileId(fromId,id,toId);
        }

        PanResult<String> result=new PanResult<>();
        result.success("移动成功");


        return result;
    }
    //删除文件
    @RequestMapping(value = "del/{cateId}/{fileId}", method = {RequestMethod.DELETE})
    public PanResult<String> delFile(@PathVariable("cateId") Integer cateId,@PathVariable("fileId")Integer fileId) {
        FileInfo fileInfo= fileService.selectFile(fileId);
        String path=fileInfo.getFileLocation();
        if (Utils.getUser().getUid()==1||Utils.getUser().getUid()==fileInfo.getUploaderId()){
            categoryDetilService.delByCateIdAndFileId(cateId,fileId);
            //  System.out.println(categoryDetilService.isFileExist(fileId));
            if (!categoryDetilService.isFileExist(fileId)){
                String filePath =request.getSession().getServletContext().getRealPath(path);
                File file=new File(filePath);
                file.delete();
                fileService.delFile(fileId);
            }

            List<AccessInfo> list=accessService.getFileAccess(cateId,fileId);
            if (list!=null&&list.size()>0){
                accessService.del(list.get(0).getAccessId());

            }
        }else {
            List<AccessInfo> list=accessService.getUserCateFileAccess(Utils.getUser().getUid(),cateId,fileId);
            if (list!=null&&list.size()>0){
                accessService.del(list.get(0).getAccessId());
            }
        }

        PanResult<String> result=new PanResult<>();
        result.success("删除成功");


        return result;
    }
    //搜索文件
    @RequestMapping(value="search")
    public PanResult<List> searchFile(String fileName){
          List <FileInfo>list= fileService.searchFile(fileName);
          List accessAbleFiles=new ArrayList();
          PanResult panResult=new PanResult();
        int uid=Utils.getUser().getUid();
        if (uid!=1){
            for (FileInfo fileInfo:list){
                if (fileInfo.getUploaderId()==uid)
                {
                    accessAbleFiles.add(fileInfo);
                }else {
                    List<AccessInfo> accessInfos=accessService.getUserFileAccess(uid,fileInfo.getFileId());
                    if (accessInfos!=null&&accessInfos.size()>0)
                        accessAbleFiles.add(fileInfo);
                }
            }
        }else {
            accessAbleFiles=list;
        }

          panResult.success(accessAbleFiles);
          return panResult;
    }

    @RequestMapping(value="mine")
    public PanResult<Map> loadMine(){

        PanResult<Map> result=new PanResult<>();
        List parentCates=new ArrayList();


        Collections.reverse(parentCates);

        List myCategory=categoryService.getMyCategory(Utils.getUser().getUid());
        //List <FileInfo> filesOfCategory=new ArrayList<>();
        List <FileInfo>accessAbleFiles=new ArrayList();
   /*     int uid=Utils.getUser().getUid();
        if (uid!=1){
            for (FileInfo fileInfo:filesOfCategory){
                if (fileInfo.getUploaderId()==uid)
                {
                    accessAbleFiles.add(fileInfo);
                }else {
                    List<AccessInfo> accessInfos=accessService.getUserCateFileAccess(uid,id,fileInfo.getFileId());
                    if (accessInfos!=null&&accessInfos.size()>0)
                        accessAbleFiles.add(fileInfo);
                }
            }
        }else {
            accessAbleFiles=filesOfCategory;
        }*/

        Map resultMap=new HashMap();
        resultMap.put("subCate",myCategory);
        resultMap.put("files",accessAbleFiles);
        resultMap.put("urls",parentCates);
        result.success(resultMap);
        return result;
    }

}

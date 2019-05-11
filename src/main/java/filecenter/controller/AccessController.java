package filecenter.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import filecenter.model.Access;
import filecenter.model.AccessInfo;
import filecenter.model.CategoryInfo;
import filecenter.model.FileInfo;
import filecenter.service.AccessService;
import filecenter.service.CategoryService;
import filecenter.service.FileService;
import filecenter.service.UserService;
import filecenter.utils.PanResult;
import filecenter.utils.Utils;
import login.model.User;
import login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("pan/access")
public class AccessController {
    @Autowired
    private AccessService accessService;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Autowired
    CategoryService categoryService;
    //int uid= Utils.getUser().getUid();
    //授权
    @RequestMapping(value = "/manage/{categoryId}/{fileId}", method = {RequestMethod.POST})
    public PanResult manageAccess(@PathVariable("categoryId") Integer categoryId,@PathVariable("fileId")Integer fileId,@RequestBody List<Integer> userIds){
  /*      List <Integer>fileIds=new ArrayList();
        if (fileId==0) {
            List<FileInfo> fileInfos = categoryService.getCategoryDatils(categoryId);
            for (FileInfo fileInfo:fileInfos)
                fileIds.add(fileInfo.getFileId());
        }else {
            fileIds.add(fileId);
        }*/

        for (int uid:userIds){

           // for (int fid:fileIds){
                List accessInfos=accessService.getUserCateFileAccess(uid,categoryId,fileId);
                AccessInfo accessInfo=new AccessInfo();
                accessInfo.setAccessType("1");
                accessInfo.setFileId(fileId);
                accessInfo.setCategoryId(categoryId);
                accessInfo.setUserId(uid);
                if (accessInfos==null||accessInfos.size()==0){
                    List list=accessService.getUserCateFileAccess(uid,categoryId,0);
                    if (list==null||list.size()==0)
                    accessService.save(accessInfo);
                }else {
                    //accessService.updateFileAccess(accessInfo);
                }
          //  }

        }
        PanResult result=new PanResult();
        result.success("成功");
        return result;
    }




    //查看文件授权列表
    @RequestMapping(value = "/viewFile/{categoryId}/{fileId}", method = {RequestMethod.GET})
    public PanResult<List> viewAccess(@PathVariable("categoryId") Integer categoryId, @PathVariable("fileId")Integer fileId){
        FileInfo fileInfo=fileService.selectFile(fileId);
        if (Utils.getUser().getUid()!=1&&fileInfo.getUploaderId()!=Utils.getUser().getUid()){
            PanResult panResult=new PanResult();
            panResult.error("你不是管理员或者上传者");
            return panResult;
        }
        List <Access>accessList=new ArrayList();


        List <AccessInfo>list=accessService.getFileAccess(categoryId,fileId);
        for (AccessInfo accessInfo:list){
            User user=userService.getUserWithDept(accessInfo.getUserId());
            Access access=new Access();
            access.setUser(user);
            access.setAccessId(accessInfo.getAccessId());
            access.setFileId(accessInfo.getFileId());
            access.setCateId(accessInfo.getCategoryId());
            access.setType("file");
            accessList.add(access);
        }

        PanResult<List> listPanResult=new PanResult<>();
        listPanResult.success(accessList);

        listPanResult.setResultMsg(fileInfo.getFileName());
        return listPanResult;
    }

    //查看分类授权列表
    @RequestMapping(value = "/viewCate/{categoryId}/", method = {RequestMethod.GET})
    public PanResult<List> viewCateAccess(@PathVariable("categoryId") Integer categoryId){
    CategoryInfo categoryInfo=categoryService.select(categoryId);
     if (Utils.getUser().getUid()!=1&&categoryInfo.getAutherUid()!=Utils.getUser().getUid()){
         PanResult panResult=new PanResult();
         panResult.error("你不是管理员或者上传者");
         return panResult;
     }
        List <Access>accessList=new ArrayList();


        List <AccessInfo>list=accessService.getFileAccess(categoryId,0);
        for (AccessInfo accessInfo:list){
            User user=userService.getUserWithDept(accessInfo.getUserId());
            Access access=new Access();
            access.setUser(user);
            access.setAccessId(accessInfo.getAccessId());
            access.setFileId(accessInfo.getFileId());
            access.setType("cate");
            access.setCateId(accessInfo.getCategoryId());
            accessList.add(access);
        }


        PanResult<List> listPanResult=new PanResult<>();
        listPanResult.success(accessList);
        listPanResult.setResultMsg(categoryInfo.getCategoryName());
        return listPanResult;
    }

    //删除文件授权列表
    @RequestMapping(value = "/cancelAccess/{accessId}/", method = {RequestMethod.GET})
    public PanResult cancelAccess(@PathVariable("accessId") Integer accessId){
        accessService.del(accessId);
       PanResult panResult=new PanResult();
        panResult.success("删除成功");
        return panResult;
    }


    //修改文件授权列表
    @RequestMapping(value = "/{categoryId}/{fileId}", method = {RequestMethod.PUT})
    public PanResult<String> updateAccess(@PathVariable("categoryId") Integer categoryId,
                                        @PathVariable("fileId")Integer fileId,
                                        @RequestParam("accessMap") String accessMapJson){

        Map<Integer,Integer> accessMap=new Gson().fromJson(accessMapJson, new TypeToken<Map<Integer, String>>() { }.getType());
        for(Map.Entry<Integer,Integer> entry:accessMap.entrySet()){
            AccessInfo accessInfo=new AccessInfo();
            accessInfo.setAccessType(entry.getValue()+"");
            accessService.updateFileAccess(accessInfo);
        }

        PanResult<String> panResult=new PanResult<>();
        panResult.success("修改成功");
        return panResult;
    }


    //修改分类授权列表
    @RequestMapping(value = "/{categoryId}/", method = {RequestMethod.PUT})
    public PanResult<String> updateAccess(@PathVariable("categoryId") Integer categoryId,

                                          @RequestParam("accessMap") String accessMapJson){

        Map<Integer,Integer> accessMap=new Gson().fromJson(accessMapJson, new TypeToken<Map<Integer, String>>() { }.getType());
        for(Map.Entry<Integer,Integer> entry:accessMap.entrySet()){
            AccessInfo accessInfo=new AccessInfo();
            accessInfo.setAccessType(entry.getValue()+"");
            accessService.updateCateAccess(entry.getKey(),categoryId,accessInfo);
        }

        PanResult<String> panResult=new PanResult<>();
        panResult.success("修改成功");
        return panResult;
    }
    //TODO 查看被授权的文件夹
}

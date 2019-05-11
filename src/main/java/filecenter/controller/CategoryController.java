package filecenter.controller;

import filecenter.model.*;
import filecenter.service.AccessService;
import filecenter.service.CategoryDetilService;
import filecenter.service.CategoryService;
import filecenter.service.FileService;
import filecenter.utils.PanResult;
import filecenter.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("pan/category")
public class CategoryController
{
    @Autowired
    CategoryService service;
    @Autowired
    CategoryDetilService categoryDetilService;
    @Autowired
    FileService fileService;
    @Autowired
    AccessService accessService;

    //全部0层分类
    @RequestMapping(value = "/", method ={ RequestMethod.GET})
    public PanResult<List> viewFCategory() {
        PanResult<List> result=new PanResult<>();
        List resultList=service.getFCategory();
        result.success(resultList);
        return result;
    }

    //查看分类详情 包括子分类和文件
    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    public PanResult<Map> viewCategory(@PathVariable("id") Integer id) {
        boolean flag=false;

        PanResult<Map> result=new PanResult<>();
        List parentCates=new ArrayList();
        CategoryInfo categoryInfo=service.select(id);
        if (categoryInfo==null){
            result.error("未找到");
            return result;
        }
        int pid=categoryInfo.getCategoryId();//.getParentId();
        CategoryInfo pCategory=categoryInfo;//service.select(pid);
        while (pid!=0&&pCategory!=null){
            Map map=new HashMap();
            map.put("pname",pCategory.getCategoryName());
            map.put("pid",pCategory.getCategoryId());
            parentCates.add(map);
            pid=pCategory.getParentId();
            pCategory=service.select(pid);
        }
        Collections.reverse(parentCates);

        List subCategory=service.getSubCategory(id);
        List <FileInfo> filesOfCategory=service.getCategoryDatils(id);
        List <FileInfo>accessAbleFiles=new ArrayList();
        int uid=Utils.getUser().getUid();
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
        }

        Map resultMap=new HashMap();
        resultMap.put("subCate",subCategory);
        resultMap.put("files",accessAbleFiles);
        resultMap.put("urls",parentCates);
        result.success(resultMap);
        return result;
    }



    //增加分类
    @RequestMapping(value = "/addCate", method = {RequestMethod.POST})
    public PanResult<String> addCategory(int parentCateId,String cateName) {

        CategoryInfo categoryInfo=new CategoryInfo();
        //TODO 配置拥有者id
        categoryInfo.setAutherUid(1);//配置作者id

        categoryInfo.setCreateTime(new Date());
        categoryInfo.setParentId(parentCateId);
        categoryInfo.setCategoryName(cateName);
        service.addCategory(categoryInfo);

        PanResult<String> result=new PanResult<>();
        result.success("添加成功");
        return result;
    }



    //复制分类
    @RequestMapping(value = "copy/{id}/{toId}", method = {RequestMethod.PUT})
    public PanResult<String> copyCategory(@PathVariable("id") Integer id,@PathVariable("id")Integer toId) {
        CategoryInfo categoryInfo=service.select(id);

     //   categoryInfo.setCategoryId(id);
        categoryInfo.setCreateTime(new Date());
        if (toId==categoryInfo.getParentId()){
            categoryInfo.setCategoryName(categoryInfo.getCategoryName()+"copy");
        }else
        categoryInfo.setParentId(toId);
        service.addCategory(categoryInfo);

        PanResult<String> result=new PanResult<>();
        result.success("复制成功");


        return result;
    }
    //移动分类
    @RequestMapping(value = "move/{id}/{toId}", method = {RequestMethod.PUT})
    public PanResult<String> moveCategory(@PathVariable("id") Integer id,@PathVariable("id")Integer toId) {
        CategoryInfo categoryInfo=service.select(id);
        categoryInfo.setParentId(toId);
        service.update(categoryInfo);
        PanResult<String> result=new PanResult<>();
        result.success("移动成功");


        return result;
    }
    //更新分类
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT})
    public PanResult<String> updateCategory(@PathVariable("id") Integer id,
                                            @RequestBody CategoryInfo categoryInfo) {
       // System.out.println(id+" pid"+parentCateId+" n:"+cateName);
       // System.out.println(categoryInfo);
   /*  CategoryInfo categoryInfo=service.select(id);
        categoryInfo.setCreateTime(new Date());
        categoryInfo.setParentId(parentCateId);
        categoryInfo.setCategoryName(cateName);*/
        categoryInfo.setCreateTime(new Date());
        service.update(categoryInfo);

        PanResult<String> result=new PanResult<>();
        result.success("修改成功");
         //return result;


       return new PanResult<>();
    }

    //删除分类和分类中的文件
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    public PanResult<String> delCategory(@PathVariable("id") Integer id) {




        List <CategoryDetil> categoryDetils=new ArrayList();
        List <CategoryDetil> filx=categoryDetilService.selecCateDtils(id);
        if (filx!=null&&filx.size()!=0)
            categoryDetils.addAll(filx);


        List<CategoryInfo> subCateList=service.getSubCategory(id);
        int i=0;
       // System.out.println(subCateList!=null&&subCateList.size()!=i);
        while (subCateList!=null&&subCateList.size()!=i){
            List subList1=service.getSubCategory(subCateList.get(i).getCategoryId());
            int cid= subCateList.get(i).getCategoryId();
            List<CategoryDetil> fileList1= categoryDetilService.selecCateDtils(cid);
            if (fileList1!=null&&fileList1.size()!=0)
                categoryDetils.addAll(fileList1);
            if (subList1!=null&&subList1.size()!=0)
                subCateList.addAll(subList1);
            i++;
        }

        //删除文件夹
        for (int j=0;j<subCateList.size();j++){
            CategoryInfo categoryInfo=subCateList.get(j);
            int cid=categoryInfo.getCategoryId();
             service.delCategory(cid);
        }

        //删除文件
        System.out.println(categoryDetils);
        for (int j=0;j<categoryDetils.size();j++){

            CategoryDetil categoryDetil=categoryDetils.get(j);

            int cateId=categoryDetil.getCategrayId();
            int fileId=categoryDetil.getFileId();
            categoryDetilService.delByCateIdAndFileId(cateId,fileId);
            System.out.println(categoryDetilService.isFileExist(fileId));
            if (!categoryDetilService.isFileExist(fileId)){
                //TODO 从硬盘删除文件
                fileService.delFile(fileId);
            }
        }

        service.delCategory(id);

        PanResult<String> result=new PanResult<>();
        result.success("删除成功");
        return result;
    }
}

package filecenter.controller;

import filecenter.model.CategoryDetil;
import filecenter.model.FileInfo;
import filecenter.service.CategoryDetilService;
import filecenter.service.FileService;
import filecenter.utils.PanResult;
import filecenter.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

@Controller

public class FileUploadController {
    /*
     *采用spring提供的上传文件的方法
     */
    @Autowired
    FileService fileService;
    @Autowired
    CategoryDetilService categoryDetilService;

    @ResponseBody
    @RequestMapping("fileUpload")
    public PanResult<String> springUpload(HttpServletRequest request, @RequestParam String cate) throws IllegalStateException, IOException
    {
        long  startTime=System.currentTimeMillis();
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request))
        {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;

           // String cate= multiRequest.getParameter("cateId");
           // System.out.println(cate);
            //获取multiRequest 中所有的文件名
            Iterator iter=multiRequest.getFileNames();

            while(iter.hasNext())
            {
                //一次遍历所有文件
                MultipartFile multipartFile=multiRequest.getFile(iter.next().toString());
                if(multipartFile!=null)
                {
                    String path = request.getSession().getServletContext().getRealPath("/upload/");
                    String dif= UUID.randomUUID().toString().substring(0,4).replaceAll("-","");
                    String fileName=multipartFile.getOriginalFilename();
                    String fns[]=fileName.split("\\.");
                    String newfileName=fns[0]+dif+"."+fns[1];
                    File file = new File(path +newfileName);
                    FileInfo fileInfo=new FileInfo();
                    fileInfo.setUploadTime(new Date());
                    fileInfo.setFileName(fileName);
                    fileInfo.setFileLocation("/upload/"+newfileName);
                    fileInfo.setFileType(fns[1]);

                    fileInfo.setUploaderId(Utils.getUser().getUid());
                    CategoryDetil categoryDetil=new CategoryDetil();
                    fileService.addFile(fileInfo);
                    categoryDetil.setCategrayId(Integer.parseInt(cate));
                    categoryDetil.setFileId(fileInfo.getFileId());
                    categoryDetilService.add(categoryDetil);

                    if (!file.getParentFile().exists()) {

                        file.getParentFile().mkdirs();
                    }
                    //上传
                    multipartFile.transferTo(file);
                }

            }

        }
        long  endTime=System.currentTimeMillis();
        System.out.println("运行时间："+String.valueOf(endTime-startTime)+"ms");

       return new PanResult<>();
    }
}


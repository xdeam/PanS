package filecenter.controller;

import com.alibaba.fastjson.JSON;
import filecenter.model.FileInfo;
import filecenter.service.CategoryDetilService;
import filecenter.service.CategoryService;
import filecenter.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class FileDownloadController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    FileService fileService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryDetilService categoryDetilService;


    @RequestMapping(value = "/download/{fileId}", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<byte[]> download(@PathVariable(name = "fileId") String fileId)
    {

        FileInfo fileInfo=fileService.selectFile(Integer.parseInt(fileId));
        String fileName=fileInfo.getFileName();
        String path=fileInfo.getFileLocation();

        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            // 获取物理路径
            String filePath =request.getSession().getServletContext().getRealPath(path);

            File pic = new File(filePath);
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(pic), headers, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/downloadFolder/{folderId}")
    public void downloadFolder(@PathVariable String folderId,HttpServletRequest req, HttpServletResponse resp)
    {
        Map<String, byte[]> files=new HashMap<>();
        List<FileInfo> fileInfos=categoryService.getCategoryDatils(Integer.parseInt(folderId));
        String categoryName=categoryService.select(Integer.parseInt(folderId)).getCategoryName();
        files.putAll(getFileNameIOMapInFolder(fileInfos,categoryName));
        if (files!=null)
            downloadBatchByFile(resp,files,categoryName);
    }

    @RequestMapping("/mutiDownload")
    @ResponseBody
    public void mutiDownload(String json,String folders,HttpServletRequest req, HttpServletResponse resp){

        Map<String, byte[]> files=new HashMap<>();

        List<String> fileIds=JSON.parseArray(json,String.class);
        files.putAll(getFileNameIOMap(fileIds));


       if (folders!=null&&!folders.equals("")){
           List<String> foldersIds=JSON.parseArray(folders,String.class);
           for(String id:foldersIds){
              List<FileInfo> fileInfos=categoryService.getCategoryDatils(Integer.parseInt(id));
              String categoryName=categoryService.select(Integer.parseInt(id)).getCategoryName();
              files.putAll(getFileNameIOMapInFolder(fileInfos,categoryName));
           }
       }


        if (files!=null)
          downloadBatchByFile(resp,files,files.entrySet().iterator().next().getKey()+"等");
    }


    /**
     * 根据文件，进行压缩，批量下载
     * @param response
     * @param files 文件转换成的byte字节流
     * @throws Exception
     */


    public void downloadBatchByFile(HttpServletResponse response, Map<String, byte[]> files, String zipName){
        try{
            response.reset();
            zipName = java.net.URLEncoder.encode(zipName, "UTF-8");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + zipName + ".zip");

            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            BufferedOutputStream bos = new BufferedOutputStream(zos);

            for(Map.Entry<String, byte[]> entry : files.entrySet()){
                String fileName = entry.getKey();            //每个zip文件名
                byte[]    file = entry.getValue();            //这个zip文件的字节

                BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(file));
                zos.putNextEntry(new ZipEntry(fileName));

                int len = 0;
                byte[] buf = new byte[10 * 1024];
                while( (len=bis.read(buf, 0, buf.length)) != -1){
                    bos.write(buf, 0, len);
                }
                bis.close();
                bos.flush();
            }
            bos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Map<String, byte[]> getFileNameIOMap(List<String> fileIds){
        Map<String, byte[]> files=new HashMap<>();
        for (String fileId:fileIds){


            FileInfo fileInfo=fileService.selectFile(Integer.parseInt(fileId));
            String path=fileInfo.getFileLocation();
            String fileName=fileInfo.getFileName();
            String filePath =request.getSession().getServletContext().getRealPath(path);

            File file=new File(filePath);

            try {
                files.put(fileName,FileUtils.readFileToByteArray(file));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return files;
    }
    public Map<String, byte[]> getFileNameIOMapInFolder(List<FileInfo> fileInfos,String folderName){
        Map<String, byte[]> files=new HashMap<>();
        for (FileInfo fileInfo:fileInfos){


            //FileInfo fileInfo=fileService.selectFile(Integer.parseInt(fileId));
            String path=fileInfo.getFileLocation();
            String fileName=fileInfo.getFileName();
            String filePath =request.getSession().getServletContext().getRealPath(path);

            File file=new File(filePath);

            try {
                files.put(folderName+"/"+fileName,FileUtils.readFileToByteArray(file));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return files;
    }

}

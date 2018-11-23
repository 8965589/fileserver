package com.ander.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/ue")
public class UEController {
    Logger logger = Logger.getLogger("UEController");

    @RequestMapping(value = "/uploadFileForm", method = RequestMethod.POST)
    public ResponseEntity uploadFileForm(@RequestParam("file") MultipartFile file, @RequestParam String appName) {
        try {
            if (!file.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String date = sdf.format(new Date());
                WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
                ServletContext servletContext = webApplicationContext.getServletContext();
                String filesPath = servletContext.getRealPath("/files");
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String savePath = filesPath + "\\" + appName + "\\" + date + "\\";
                logger.info(savePath);
                File filepath = new File(savePath, fileName);
                if (!filepath.getParentFile().exists()) {
                    filepath.getParentFile().mkdirs();//如果目录不存在，创建目录
                }
                file.transferTo(new File(savePath + File.separator + fileName));//把文件写入目标文件地址
                return ResponseEntity.ok().build();
                //String servletPath = request.getServletPath();
                // String scheme = request.getScheme();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/uploadFileRestTem", method = RequestMethod.POST)
    public ResponseEntity uploadFileRestTem(@RequestParam Map<String, Object> map) {
        try {
            String appName = String.valueOf(map.get("appName"));
            MultipartFile file = (MultipartFile) map.get("file");
            if (!file.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String date = sdf.format(new Date());
                WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
                ServletContext servletContext = webApplicationContext.getServletContext();
                String filesPath = servletContext.getRealPath("/files");
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String savePath = filesPath + "\\" + appName + "\\" + date + "\\";
                logger.info(savePath);
                File filepath = new File(savePath, fileName);
                if (!filepath.getParentFile().exists()) {
                    filepath.getParentFile().mkdirs();
                }
                file.transferTo(new File(savePath + File.separator + fileName));//把文件写入目标文件地址
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception exception) {
            logger.warning("文件上传失败" + exception.toString());
        }
        return null;
    }
}
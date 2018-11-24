package com.ander.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

//抛弃不用了
public class UEController {
    Logger logger = Logger.getLogger("UEController");

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
                    filepath.getParentFile().mkdirs();
                }
                file.transferTo(new File(savePath + File.separator + fileName));
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

    public Object conf(HttpServletRequest request, @RequestParam(required = false) String action) throws Exception {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        if (StringUtils.equals(action, "config")) {
            String uirPre = "http://localhost:8080/fileSystem/weba/";
            ObjectMapper mapper = new ObjectMapper();

            String filesPath = servletContext.getRealPath("/files");
            File file = ResourceUtils.getFile(filesPath + "/config/config.json");
            ObjectNode objectNode = mapper.readValue(file, ObjectNode.class);
            objectNode.put("imageUrlPrefix", uirPre);
            objectNode.put("fileUrlPrefix", uirPre);
            objectNode.put("scrawlUrlPrefix", uirPre);
            objectNode.put("snapscreenUrlPrefix", uirPre);
            objectNode.put("catcherUrlPrefix", uirPre);
            objectNode.put("videoUrlPrefix", uirPre);
            objectNode.put("imageManagerUrlPrefix", uirPre);
            objectNode.put("fileManagerUrlPrefix", uirPre);
            return objectNode;
        } else if ("uploadimage".equals(action) || "uploadvideo".equals(action) || "uploadfile".equals(action)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            String filesPath = servletContext.getRealPath("/files");
            String date = sdf.format(new Date());
            String path = filesPath + "\\" + date;


            File temFile = new File(path);

            if (!temFile.exists()) {
                temFile.mkdir();
            }

            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;

            MultipartFile upfile = mRequest.getFile("upfile");
            if (upfile.isEmpty()) {
                return "上传错误";
            }
            String localFileName = System.currentTimeMillis() + "_" + upfile.getOriginalFilename();

            try {
                String filePath = path + "\\" + localFileName;
                upfile.transferTo(new File(filePath));
            } catch (Exception e) {
            }

            String imageUrl = date + "\\" + localFileName;

            String prefix = upfile.getOriginalFilename().substring(upfile.getOriginalFilename().lastIndexOf(".") + 1);

            Map<String, Object> result = new HashMap();
            result.put("original", upfile.getOriginalFilename());

            result.put("name", upfile.getOriginalFilename());
            result.put("url", imageUrl);
            result.put("size", upfile.getSize());
            result.put("type", "." + prefix);
            result.put("state", "SUCCESS");


            return result;
        } else {
            return null;
        }

    }
}
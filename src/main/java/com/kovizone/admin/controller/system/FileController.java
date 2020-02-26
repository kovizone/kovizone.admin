package com.kovizone.admin.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.kovizone.admin.anno.PermissionScanIgnore;
import com.kovizone.admin.util.DateUtil;
import com.kovizone.admin.util.FileUtils;
import com.kovizone.admin.util.GeneralUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.Objects;

/**
 * @author KoviChen
 */
@PermissionScanIgnore
@RequestMapping("/file")
@Controller
public class FileController {

    private Logger logger = LoggerFactory.getLogger(FileController.class);

    public static final String IMG_TYPE_PNG = "PNG";
    public static final String IMG_TYPE_JPG = "JPG";
    public static final String IMG_TYPE_JPEG = "JPEG";
    public static final String IMG_TYPE_DMG = "BMP";
    public static final String IMG_TYPE_GIF = "GIF";
    public static final String IMG_TYPE_SVG = "SVG";

    @RequestMapping(value = "/image/view.do")
    public ModelAndView view() {
        ModelAndView mv = new ModelAndView("system/imgUploader");
        mv.addObject("now", new Date());
        return mv;
    }

    private String imagePath = "D://kovi/test/";

    @RequestMapping("/image/upload.do")
    @ResponseBody
    public JSONObject fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        logger.info("正在做上传操作，上传文件为：{}，文件类型：{}", file.getOriginalFilename(), suffix);
        String fileName = String.format("%s.%s", DateUtil.dateFormat(new Date(), "yyyyMMddHHmmssSSS"), suffix);
        String path = imagePath + fileName;
        FileUtils.generateFile(file.getBytes(), path);
        return new JSONObject() {{
            put("fileName", fileName);
            put("url", imagePath + fileName);
        }};
    }

    @RequestMapping("/image/{name}.{type}")
    public void show(@PathVariable("name") String name,
                     @PathVariable("type") String type,
                     HttpServletResponse response) {
        FileInputStream fileInputStream = null;
        OutputStream outputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(String.format("%s/%s.%s", imagePath, name, type)));
            int i = fileInputStream.available();
            byte[] data = new byte[i];
            fileInputStream.read(data);
            fileInputStream.close();
            response.setContentType("image/" + type);
            outputStream = response.getOutputStream();
            outputStream.write(data);
            outputStream.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            GeneralUtils.close(outputStream, fileInputStream);
        }
    }
}


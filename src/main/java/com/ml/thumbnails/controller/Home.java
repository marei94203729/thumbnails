package com.ml.thumbnails.controller;

import com.ml.thumbnails.service.ExcelUtil;
import lombok.Cleanup;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/*
 * @description home
 * @author hjj
 * @createTime 2020/9/14
 */
@RestController
public class Home {
    @GetMapping("home")
    public String getPath(HttpServletResponse response)throws Exception{
        @Cleanup OutputStream os=response.getOutputStream();
        //给文件命名。随机命名
        String fileName = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xlsx";
        //告诉浏览器数据格式，将头和数据传到前台
        String headStr = "attachment; filename=\"" + fileName + "\"";
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", headStr);
        @Cleanup  Workbook workbook= ExcelUtil.createWorkbook(ExcelUtil.ExcelFileType.Excel2007);
        workbook.write(os);
        return "34";
    }
}

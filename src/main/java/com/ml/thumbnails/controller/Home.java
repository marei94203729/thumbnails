package com.ml.thumbnails.controller;

import com.ml.thumbnails.service.ExcelUtil;
import lombok.Cleanup;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/*
 * @description home
 * @author hjj
 * @createTime 2020/9/14
 */
@Controller
public class Home {
    @GetMapping("home")
    public String getPath(HttpServletResponse response)throws Exception{
      //  @Cleanup OutputStream os=response.getOutputStream();
        //给文件命名。随机命名
       // String fileName = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xlsx";
        //告诉浏览器数据格式，将头和数据传到前台
     //   String headStr = "attachment; filename=\"" + fileName + "\"";
     //   response.setContentType("APPLICATION/OCTET-STREAM");
     //   response.setHeader("Content-Disposition", headStr);
      //  @Cleanup  Workbook workbook= ExcelUtil.createWorkbook(ExcelUtil.ExcelFileType.Excel2007);
      //  workbook.write(os);
        return "home";
    }
    @GetMapping("download")
    public String getDownloadPath(HttpServletResponse response)throws Exception{
          @Cleanup OutputStream os=response.getOutputStream();
        //给文件命名。随机命名
           String fileName = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xlsx";
        //告诉浏览器数据格式，将头和数据传到前台
           //String headStr = "attachment; filename=\"" + fileName + "\"";
            // 解决导出文件名中文乱码
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", String.format("attachment; filename=%s", new String(fileName.getBytes("UTF-8"), "ISO-8859-1")));
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
          @Cleanup Workbook workbook= ExcelUtil.createWorkbook(ExcelUtil.ExcelFileType.Excel2007);
          workbook.write(os);
        return "home";
    }
}

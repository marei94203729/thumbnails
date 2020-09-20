package com.ml.thumbnails.service;

import lombok.Cleanup;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/*
 * @description excel操作类
 * <dl>
 *   <p>ExcelDe操作类，使用org.apache.poi的poi及poi-ooxml实现</p>
 * </dl>
 * <dt>Workbook excel的文档对象</dt>
 * <dd>Sheet excel的sheet表
 * Row excel的行
 * Cell excel的格子单元
 * Font excel字体
 * DataFormat 日期格式
 * Header sheet头
 * Footer sheet尾（只有打印的时候才能看到效果）
 * CellStyle cell样式
 * 辅助操作包括
 * DateUtil 日期
 * PrintSetup 打印
 * ErrorConstants 错误信息表</dd>
 * 以上都是接口类型，如果是excel2003的实现类则是HSSF开头
 * 如果是excel2010或2007的实现类则是XSSF开头
 * 方法调用的大致流程:创建Workbook=》使用Workbook创建Sheet=》使用Sheet创建指定行号的Row引用(行号从0开始索引)
 * =》使用Row创建指定列的Cell(列号从0开始索引)=》设置单元格样式或直接设置单元格的值
 * 设置样式的方式请参看设置样式的方法{@link Thumbnails#of(BufferedImage...)}
 * @author hjj
 * @createTime 2020/9/14
 */
@Slf4j
public class ExcelUtil {

    /**
     * 创建指定类型的Excel的Workbook
     * @param excelFileType ExcelFileType枚举类型指定是2003的xls还是2010的xlsx
     * @return 返回一个Workbook实例，该实例使用后必须手动close，可以使用@Cleanup进行关闭
     */
    public static Workbook createWorkbook(ExcelFileType excelFileType){
        Workbook workBook=null;
        if(excelFileType==ExcelFileType.Excel2003){//生成2003工作部
            workBook=new HSSFWorkbook();
        }else{//生成2007或2010的工作簿
            workBook = new XSSFWorkbook();
        }
        Sheet sheet = workBook.createSheet("sheet1");
        //自动调整第三列列宽
        sheet.autoSizeColumn(2);
        // 创建Excel标题行，第一行
        Row headRow = sheet.createRow(0);
        //隐藏行
        headRow.setZeroHeight(true);
        //设置行高为两个默认行高
        headRow.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
        headRow.createCell(0).setCellValue("姓名");
        headRow.createCell(1).setCellValue("分数");
        return workBook;
        //try {
            //File xlsFile = new File("D:/直播订单."+excelType);
            //@Cleanup FileOutputStream fos=new FileOutputStream(xlsFile );
            // 或者以流的形式写入文件 workbook.write(new FileOutputStream(xlsFile));
            //workBook.write(fos);
        ////} catch (IOException e) {
            // TODO
       // } finally {//
           // try {
              //  workBook.close();
           // } catch (IOException e) {
                // TODO
           // }
       //}
    }

    /**
     * 在指定的workBook中创建默认名称为sheet1的Sheet
     * @param workBook 一个2003 HSSFWorkbook或2007以上 XSSFWorkbook类型的实例
     * @return Sheet类型(HSSFWorkbook类型Workbook创建的就是HSSFSheet,XSSFWorkbook类型创建的就是XSSFSheet)
     */
    public static Sheet createDefaultSheet(Workbook workBook){
        Sheet sheet=createSheet(workBook,"sheet1");
        //设置默认行宽
        sheet.setDefaultColumnWidth(30);//设置默认行宽
        //设置第三列根据内容自动调整列宽
        sheet.autoSizeColumn(2);
        //合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,0));
        return  sheet;
    }

    /**
     * 在指定的workBook中创建指定名称为sheetName的Sheet
     * @param workBook 一个2003 HSSFWorkbook或2007以上 XSSFWorkbook类型的实例
     * @param sheetName Sheet名称
     * @return Sheet类型(HSSFWorkbook类型Workbook创建的就是HSSFSheet,XSSFWorkbook类型创建的就是XSSFSheet)
     */
    public static Sheet createSheet(Workbook workBook,String sheetName){
        return workBook.createSheet(sheetName);
    }

    /**
     * 在指定的sheet中创建指定rowIndex(rowIndex从0开始，0对应第一行)的Row的引用
     * @param sheet Sheet类型(HSSFWorkbook类型Workbook创建的就是HSSFSheet,XSSFWorkbook类型创建的就是XSSFSheet)
     * @param rowIndex rowIndex从0开始，0对应第一行
     * @return 具体行索引的Row的引用(根据sheet实例类型创建HSSFRow或XSSFRow)
     */
    public static Row createRow(Sheet sheet,int rowIndex){
        return sheet.createRow(rowIndex);
    }

    /**
     * 在指定的row中创建指定colIndex(colIndex从0开始，0对应第一行列)的Cell的引用
     * @param row 具体行号的Row的引用(根据sheet实例类型创建HSSFRow或XSSFRow)
     * @param colIndex colIndex从0开始，0对应第一行列
     * @return 具体列索引的Cell的引用(根据Row实例类型创建HSSFCell或XSSFCell)
     */
    public static Cell createCell(Row row,int colIndex){
        return row.createCell(colIndex);
    }

    /**
     * 为指定的workBook创建字体接口实例(Font)
     * 创建的字体接口实例(Font)最总被设置到单元格样式CellStyle中，格式参照: style.setFont(font);
     * @param workBook
     * @return Font接口实例
     */
    public static Font createFont(Workbook workBook){
        return workBook.createFont();
    }

    /**
     * 为指定的workBook创建默认字体接口实例(Font)
     * @param workBook
     * @return
     */
    public static Font createDefaultFont(Workbook workBook){
        Font font=createFont(workBook);
        // 设置字体大小
        font.setFontHeightInPoints((short) 12);
        // 字体加粗
        font.setBold(false);
        // 设置字体名字
        font.setFontName("宋体");
        font.setItalic(true);
        font.setStrikeout(true);

        //设置字体颜色IndexedColors枚举中预定义了常用颜色
        font.setColor(IndexedColors.BLACK.index);
        //设置是否有下划线，FontUnderline枚举中预定义了常用下划线模式
        font.setUnderline(FontUnderline.NONE.getByteValue());
        return font;
    }

    /**
     * 使用指定workBook创建一个CellStyle单元格样式
     * 创建的CellStyle单元格样式，最终被设置到cell中，格式参照： cell.setCellStyle(style);
     * @param workBook
     * @return
     */
    public static CellStyle createCellStyle(Workbook workBook){
        return workBook.createCellStyle();
    }

    /**
     * 设置默认单元格样式
     * @param workBook
     * @return 一个具有默认设置的CellStyle
     */
    public static CellStyle createDefaultCellStyle(Workbook workBook){
        CellStyle style=createCellStyle(workBook);
        //设置底边框的样式，使用BorderStyle枚举进行设置
        style.setBorderBottom(BorderStyle.NONE);
        //设置字体颜色IndexedColors枚举中预定义了常用颜色(IndexedColors.BLACK.index)
        style.setBottomBorderColor(IndexedColors.BLACK.index);
        // 设置左边框;
        style.setBorderLeft(BorderStyle.NONE);
        // 设置左边框颜色;
        style.setLeftBorderColor(IndexedColors.BLACK.index);
        // 设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(IndexedColors.BLACK.index);
        // 设置顶边框;
        style.setBorderTop(BorderStyle.NONE);
        // 设置顶边框颜色;
        style.setTopBorderColor(IndexedColors.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(createDefaultFont(workBook));
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //背景色
        style.setFillBackgroundColor(IndexedColors.TURQUOISE.index);
        style.setFillPattern(FillPatternType.BIG_SPOTS);
        //设置前景颜色(非字体颜色)
        style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //设置单元格格式
        DataFormat format=workBook.createDataFormat();
        //设置单元格为文本格式
        style.setDataFormat(format.getFormat("@"));
        //设置单元格为日期格式
        style.setDataFormat(format.getFormat("yyyy年m月d日"));
        //设置单元格为保留两位小数格式
        style.setDataFormat(format.getFormat("0.00"));
        //设置单元格为货币(0.00%为百分比 [DbNum2][$-804]0中文大写 0.00E+00科学计数法)
        style.setDataFormat(format.getFormat("0¥#,##0"));
        style.setDataFormat(format.getFormat("#,##0.0000"));
        return style;
    }

    /**
     * excel读取器内部静态类
     * 实现读取指定路径的excel文件,生成WorkBook
     */
    public static class Reader implements Closeable {
        private Workbook _workbook;
        /**
         * 从给定的文件地址读取excel
         * @param filePath 文件地址，如果是类路径下可以使用类加载器进行加载
         * @return
         * @throws IOException
         */
        public  Reader readWorkbookByPath(String filePath)throws IOException {
            Workbook wb = WorkbookFactory.create(new File(filePath));
            this._workbook=wb;
            return this;
        }

        /**
         * 从给定的文件地址读取excel
         * @param filePath 文件地址，如果是类路径下可以使用类加载器进行加载
         * @param password excel密码
         * @return
         * @throws IOException
         */
        public  Reader readWorkbookByPath(String filePath,String password)throws IOException {
            Workbook wb = WorkbookFactory.create(new File(filePath),password,true);
            this._workbook=wb;
            return this;
        }

        /**
         * 从给定的地址读取excel
         * @param file 加载了excel地址的文件
         * @return
         * @throws IOException
         */
        public  Reader readWorkbookByFile(File file)throws IOException {
            Workbook wb = WorkbookFactory.create(file);
            this._workbook=wb;
            return this;
        }

        /**
         * 从给定的地址读取excel
         * @param file 加载了excel地址的文件
         * @param password excel密码
         * @return
         * @throws IOException
         */
        public  Reader readWorkbookByFile(File file,String password)throws IOException {
            Workbook wb = WorkbookFactory.create(file,password,true);
            this._workbook=wb;
            return this;
        }

        /**
         * 通过指定url读取excel文件，注意使用 InputStream读取会消耗更多的内存
         * @param url excel的URL路径
         * @return
         * @throws IOException
         */
        public  Reader readWorkbookByURL(URL url)throws IOException {
            @Cleanup InputStream inputStream= url.openStream();
            Workbook wb = WorkbookFactory.create(inputStream);
            this._workbook=wb;
            return this;
        }

        /**
         * 通过指定url读取excel文件，注意使用 InputStream读取会消耗更多的内存
         * @param url excel的URL路径
         * @param password excel密码
         * @return
         * @throws IOException
         */
        public  Reader readWorkbookByURL(URL url,String password)throws IOException {
            @Cleanup InputStream inputStream= url.openStream();
            Workbook wb = WorkbookFactory.create(inputStream,password);
            this._workbook=wb;
            return this;
        }

        /**
         * 通过给定的输入流读取excel文件
         * @param inputStream 已经加载了excel的输入流
         * @return
         * @throws IOException
         */
        public  Reader readWorkbookByInputStream(InputStream inputStream)throws IOException {
            @Cleanup InputStream in=inputStream;
            Workbook wb = WorkbookFactory.create(in);
            this._workbook=wb;
            return this;
        }

        /**
         * 通过给定的输入流读取excel文件
         * @param inputStream 已经加载了excel的输入流
         * @param password 读物excel的密码
         * @return
         * @throws IOException
         */
        public  Reader readWorkbookByInputStream(InputStream inputStream,String password)throws IOException {
            @Cleanup InputStream in=inputStream;
            Workbook wb = WorkbookFactory.create(in,password);
            this._workbook=wb;
            return this;
        }

        /**
         * 读取数据
         * @return
         */
        public Reader readData(){
            DataFormatter formatter = new DataFormatter();
            Sheet sheet1 = this._workbook.getSheetAt(0);
            for (Row row : sheet1) {
                for (Cell cell : row) {
                    CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
                    //log.info(cellRef.formatAsString()+" - "+formatter.formatCellValue(cell));
                    //log.info(" - ");
                    // get the text that appears in the cell by getting the cell value and applying any data formats (Date, 0.00, 1.23e9, $1.23, etc)
                    //String text = formatter.formatCellValue(cell);
                    //log.info(text);
                    // Alternatively, get the value and format it yourself
                    switch (cell.getCellType()) {
                        case STRING:
                         //   log.info(cell.getRichStringCellValue().getString());
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                              //  log.info(cell.getDateCellValue().toString());
                            } else {
                                //log.info(String.valueOf(cell.getNumericCellValue()));
                            }
                            break;
                        case BOOLEAN:
                           // log.info(String.valueOf(cell.getBooleanCellValue()));
                            break;
                        case FORMULA:
                          //  log.info(cell.getCellFormula());
                            break;
                        case BLANK:
                            System.out.println(cellRef.formatAsString()+" - ");
                            break;
                        default:
                            System.out.println();
                    }
                }
            }
            return this;
        }
        @Override
        public void close() throws IOException {
            if(this._workbook!=null){
                this._workbook.close();
            }
        }
    }
    /**
     * 内部静态枚举
     * 标识Excel类型
     */
    public static enum ExcelFileType{
        Excel2003("xls"),
        Excel2007("xlsx");
        ExcelFileType(String name){
            this.name=name;
        }
        @Getter
        private String name;
    }
}

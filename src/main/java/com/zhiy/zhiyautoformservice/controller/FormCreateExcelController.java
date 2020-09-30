package com.zhiy.zhiyautoformservice.controller;

import com.zhiy.zhiyautoformservice.utils.Zhiy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: liukun
 * @create: 2020-08-19 16:27
 */
@RestController
@RequestMapping("/excel")
public class FormCreateExcelController {

    @GetMapping("test")
    public Zhiy test(){
        return Zhiy.ok().put("status","success");
    }

    /**
     * 导入Excel数据，解析表单映射，本地测试
     *
     * @param file 文件
     * @param sheetName sheet名称
     * @throws Exception
     */
    @PostMapping("/resexc")
    public Zhiy resolvingExcel(@RequestParam("file") MultipartFile file, @RequestParam("sheetName") String sheetName) throws Exception {
        XSSFWorkbook wookbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = wookbook.getSheet(sheetName);
        //获取到Excel文件中的所有行数
        int rows = sheet.getPhysicalNumberOfRows();
        //遍历行
        Map<String, Object> list = new HashMap<String, Object>();
        List<Map<String, Object>> listParam = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < rows; i++) {
            // 读取左上端单元格
            XSSFRow row = sheet.getRow(i);
            // 行不为空
            if (row != null) {
                Map<String, Object> mapSub = new HashMap<String, Object>();
                XSSFCell mark1Cell = row.getCell(0);
                String mark1 = getValue(mark1Cell);
                if(!mark1.equals("begin")){
                    continue;
                }else{
                    row = sheet.getRow(++i);
                    XSSFCell tableCell = row.getCell(0);
                    String tableName = getValue(tableCell);
                    row = sheet.getRow(++i);
                    mapSub.put("begin", ++i );
                    List<String> columnsList=new ArrayList<>();
                    for(int j=0;j<100;j++){
                        XSSFCell columnCell = row.getCell(j);
                        String columnName = getValue(columnCell);
                        if(!columnName.equals("")&& columnName!=""){
                            columnsList.add(columnName);
                        }else {
                            break;
                        }
                    }
                    mapSub.put("columns", columnsList );
                    mapSub.put("tableName", tableName );
                }
                listParam.add(mapSub);
            }
        }

        for (int i = 0; i < listParam.size(); i++){
            List<Map<String, Object>> listSub = new ArrayList<Map<String, Object>>();
            for (int j = Integer.parseInt(listParam.get(i).get("begin").toString()); j < rows; j++) {
                XSSFRow row = sheet.getRow(j);
                // 行不为空
                if (row != null) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    //标志位
                    XSSFCell markCell1 = row.getCell(0);
                    String markValue1 = getValue(markCell1);
                    if(markValue1.equals("end")){
                        break;
                    }
                    for(int k = 0; k < castList(listParam.get(i).get("columns"),String.class).size(); k++){
                        XSSFCell cell = row.getCell(k);
                        String value = getValue(cell);
                        map.put(castList(listParam.get(i).get("columns"),String.class).get(k), value);
                    }
                    listSub.add(map);
                }
            }
            //list.put("2",listParam.get(i).get("columns").toString(),listSub);
            list.put(listParam.get(i).get("tableName").toString(),listSub);
        }
        return Zhiy.ok().put("list", list);
    }


    private String getValue(XSSFCell xSSFCell) {
        if (null == xSSFCell) {
            return "";
        }
        if (xSSFCell.getCellType() == xSSFCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(xSSFCell.getBooleanCellValue());
        } else if (xSSFCell.getCellType() == xSSFCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(xSSFCell.getNumericCellValue());
        } else {
            // 返回字符串类型的值
            return String.valueOf(xSSFCell.getStringCellValue());
        }
    }

    private static <T> List<T> castList(Object obj, Class<T> clazz)
    {
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }



}
package com.lww.sandwich.utils.easyExcelUtils;

import cn.hutool.core.convert.Convert;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.IoUtils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author lww
 * @since 2024/3/6 17:14
 */
public class MyStringImageConverterUtil implements Converter<List<String>> {
    @Override
    public Class supportJavaTypeKey() {
        return List.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        /**
         *这里记得枚举类型为IMAGE
         */
        return CellDataTypeEnum.IMAGE;
    }

    @Override
    public List convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        //从excel中读数据时被EasyExcel调用
        String stringValue = cellData.getStringValue();
        //用json转换工具将excel单元格中数据转换为java List<String>对象
        List<String> list = Convert.toList(String.class,stringValue);
        return list;
    }

    @Override
    public CellData convertToExcelData(List<String> stringUrl, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // 这里进行对数据实体类URL集合处理
        List<CellData> data = new ArrayList<>();
        // for 循环一次读取
        for (String url : stringUrl) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(url);
                byte[] bytes = IoUtils.toByteArray(inputStream);
                data.add(new CellData(bytes));
            } catch (Exception e) {
                //图片异常展示的图片
                data.add(new CellData(IoUtils.toByteArray(new FileInputStream("D:\\easyexcel\\err.png"))));
                continue;
            } finally {
                if (inputStream != null){
                    inputStream.close();
                }
            }
        }

        // 这种方式并不能返回一个List,所以只好通过CellData cellData = new CellData(data);将这个list对象塞到返回值CellData对象的data属性中；
        CellData cellData = new CellData(data);
        cellData.setType(CellDataTypeEnum.IMAGE);
        return cellData;
    }
}

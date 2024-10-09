package com.lww.sandwich.utils.easyexcel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.IoUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  
 * @author lww
 * @since 2024/3/6 17:12
 */
public class MyUrlConverterUtil implements Converter<List<URL>> {
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
        return Collections.emptyList();
    }

    @Override
    public CellData convertToExcelData(List<URL> value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // 这里进行对数据实体类URL集合处理
        List<CellData<byte[]>> data = new ArrayList<>();
        // for 循环一次读取
        for (URL url : value) {
            InputStream inputStream = null;
            try {
                inputStream = url.openStream();
                byte[] bytes = IoUtils.toByteArray(inputStream);
                data.add(new CellData(bytes));
            } catch (Exception e) {
                //图片异常展示的图片
                data.add(new CellData(IoUtils.toByteArray(new FileInputStream("C:\\Users\\Administrator\\Desktop\\测试ds\\1.jpg"))));
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

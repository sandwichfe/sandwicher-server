package com.lww.sandwich.utils.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.net.URL;
import java.util.List;

/**
 *  
 * @author lww
 * @since 2024/3/6 17:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@HeadStyle(horizontalAlignment = HorizontalAlignment.CENTER)
@ContentStyle(horizontalAlignment = HorizontalAlignment.CENTER)
public class MyExcel {

    @ExcelProperty(value = "姓名")
    private String name;

    @ExcelProperty(value = "年龄")
    private String age;

    @ExcelProperty(value = "URL图片", converter = MyUrlConverterUtil.class)
    private List<URL> imageUrls;

    @ExcelProperty(value = "String图片", converter = MyStringImageConverterUtil.class)
    private List<String> imageStrings;
}

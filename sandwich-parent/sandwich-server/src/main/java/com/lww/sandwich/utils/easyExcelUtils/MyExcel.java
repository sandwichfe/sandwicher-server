package com.lww.sandwich.utils.easyExcelUtils;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.lww.sandwich.utils.easyExcelUtils.MyStringImageConverterUtil;
import com.lww.sandwich.utils.easyExcelUtils.MyUrlConverterUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.net.URL;
import java.util.List;

/**
 * @description:
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

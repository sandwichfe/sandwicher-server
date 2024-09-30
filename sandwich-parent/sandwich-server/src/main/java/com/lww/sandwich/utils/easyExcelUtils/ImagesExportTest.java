package com.lww.sandwich.utils.easyExcelUtils;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 *  
 * @author lww
 * @since 2024/3/6 17:17
 */
public class ImagesExportTest {

    @Test
    public void test() throws Exception {
        String filename = "C:\\Users\\Administrator\\Desktop\\测试ds\\easyExcelImages-"+new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date()) +".xlsx";
        // 图片位置
        String imagePath = "C:\\Users\\Administrator\\Desktop\\测试ds\\1.jpg";
        // 网络图片
        URL url = new URL("https://cdn.wwads.cn/creatives/abIZSqJjzHmoDUraPG0V9JEqF4ShrHkXmtM32ev4.png");

        ArrayList<String> list = new ArrayList<>();
        ArrayList<URL> urls = new ArrayList<>();
        ArrayList<MyExcel> excelList = new ArrayList<>();



            for (int i = 0; i < 3; i++) {
                list.add(imagePath);
                if (i==2){
                    //异常String类型图片地址
                    list.add("C:\\Users\\Administrator\\Desktop\\测试ds\\1.jpg");
                    //异常url类型图片地址
                    urls.add(new URL("https://cdn.wwads.cn/creatives/abIZSqJjzHmoDUraPG0V9JEqF4ShrHkXmtM32ev4.png"));
                }
                urls.add(url);
            }

        for (int z = 0; z <1 ; z++) {
            excelList.add(new MyExcel().setImageStrings(list.subList(0, 2)).setImageUrls(urls.subList(0, 1)).setName("赵六").setAge("12"));
            excelList.add(new MyExcel().setImageStrings(list.subList(0, 1)).setImageUrls(urls.subList(0, 2)).setName("纳兹").setAge("12"));
            //异常string和url类型图片地址
            excelList.add(new MyExcel().setImageStrings(Arrays.asList("C:\\Users\\Administrator\\Desktop\\测试ds\\1.jpg")).setImageUrls(Arrays.asList(new URL("https://cn.bing.com/th?id=OHR.TanzaniaBeeEater_ZH-CN324665733_1920x1080.jpg"))).setName("纳兹").setAge("12"));
            //无图片
            excelList.add(new MyExcel().setImageStrings(list.subList(0, 0)).setImageUrls(urls.subList(0, 0)).setName("纳兹").setAge("12"));
        }

        long l = System.currentTimeMillis();
        //该文件导出到本地
        EasyExcel.write(filename, MyExcel.class)
                .registerWriteHandler(new CustomImageModifyHandler())
                .sheet("多图片")
                .doWrite(excelList);
        System.out.println("构造图片花费："+(System.currentTimeMillis()-l));

        // 导出到接口
        //EasyExcel.write(outputStream, Excel数据实体类.class)
        //        .registerWriteHandler(new CustomImageModifyHandler()).sheet().doWrite(查询出来并且完成数据转换的Excel数据集合);
    }


}
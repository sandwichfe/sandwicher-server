package com.lww.sandwich;

import cn.idev.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ImportTest {


    @Test
    public void testImport(){
        System.out.println("111");
        String fileName = "C:\\Users\\sandw\\Desktop\\10000.xlsx"; // 输出文件名
        List<UserInfo> dataList = generateData();  // 生成数据
        EasyExcel.write(fileName, UserInfo.class)
                .sheet("Sheet1")
                .doWrite(dataList);  // 将数据写入 Excel 文件

        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入

        // 写法1 JDK8+
        // since: 3.0.0-beta1
        // String fileName = "C:\\Users\\sandw\\Desktop\\1514.xlsx"; // 输出文件名
        //
        // // 如果这里想使用03 则 传入excelType参数即可
        // EasyExcel.write(fileName, DemoData.class)
        //         .sheet("模板")
        //         .doWrite(generateData());

    }



    // 生成数据
    private static List<UserInfo> generateData() {
        List<UserInfo> userInfoList = new ArrayList<>();

        for (int i = 0; i <= 1; i++) {
            userInfoList.add(new UserInfo());  // 将数据添加到列表
        }
        return userInfoList;
    }

}

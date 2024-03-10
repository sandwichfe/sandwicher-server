package com.lww.sandwich;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

public class Activiti7Test01 {


    /**
     * 获取processEngine对象的第一种方式
     */
    @Test
    public void test1(){
            // 这个方法会默认去读取 resource目录下的activiti.cfg.xml 对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(defaultProcessEngine);
    }


}

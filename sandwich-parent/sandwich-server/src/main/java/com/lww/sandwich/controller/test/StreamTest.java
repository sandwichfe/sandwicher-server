package com.lww.sandwich.controller.test;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author lww
 * @since 2022/9/20 14:36
 */
public class StreamTest {

    static List<Person> personList = new ArrayList<Person>();
    private static void initPerson() {
        personList.add(new Person("张三", 8, 3000));
        personList.add(new Person("李四", 18, 5000));
        personList.add(new Person("王五", 28, 7000));
        personList.add(new Person("孙六", 38, 9000));
    }


    public static void main(String[] args) {
        //initPerson();
        // 从员工集合中筛选出salary大于8000的员工，并放置到新的集合里。
        //Stream<Person> personStream = personList.stream().filter(person -> person.getAge() > 8000);
        //List<Person> collect = personStream.collect(Collectors.toList());
        //System.out.println(collect);
        // 统计员工的最高薪资、平均薪资、薪资之和。

        List<Integer> list = Arrays.asList(7, 6, 9, 3, 8, 2, 1);
        list.stream().filter(x->x>3).forEach(System.out::println);
        //
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5);





    }

    @Test
    public void test(){
        

    }


    @Test
    public void test1() {
        // stream
        // 从员工集合中筛选出salary大于8000的员工，并放置到新的集合里。


    }



}

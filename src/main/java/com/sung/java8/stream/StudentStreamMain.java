package com.sung.java8.stream;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by sungang on 2017/8/15.
 */
public class StudentStreamMain {


    // 初始化
    List<Student> students = new ArrayList<Student>() {
        {
            add(new Student(20160001, "孔明", 20, 1, "土木工程", "武汉大学"));
            add(new Student(20160002, "伯约", 21, 2, "信息安全", "武汉大学"));
            add(new Student(20160003, "玄德", 22, 3, "经济管理", "武汉大学"));
            add(new Student(20160004, "云长", 21, 2, "信息安全", "武汉大学"));
            add(new Student(20161001, "翼德", 21, 2, "机械与自动化", "华中科技大学"));
            add(new Student(20161002, "元直", 23, 4, "土木工程", "华中科技大学"));
            add(new Student(20161003, "奉孝", 23, 4, "计算机科学", "华中科技大学"));
            add(new Student(20162001, "仲谋", 22, 3, "土木工程", "浙江大学"));
            add(new Student(20162002, "鲁肃", 23, 4, "计算机科学", "浙江大学"));
            add(new Student(20163001, "丁奉", 24, 5, "土木工程", "南京大学"));
        }
    };


    /**
     *
     */
    @Test
    public void filterTest() {


        List<Student> whuStudents = students.stream()
                .filter(student -> "武汉大学".equals(student.getSchool()))
                .collect(Collectors.toList());


        System.out.println(JSON.toJSONString(whuStudents));
    }


    /**
     * distinct操作类似于我们在写SQL语句时，添加的DISTINCT关键字，用于去重处理
     */
    @Test
    public void distinctTest() {
        List<Integer> nums = Lists.newArrayList();
        nums.add(1);
        nums.add(2);
        nums.add(3);
        nums.add(4);
        nums.add(5);
        nums.add(6);
        nums.add(7);
        nums.add(8);
        nums.add(8);
        List<Integer> evens = nums.stream()
                .filter(num -> num % 2 == 0).distinct()
                .collect(Collectors.toList());

        System.out.println(JSON.toJSONString(evens));
    }


    /**
     * limit操作也类似于SQL语句中的LIMIT关键字，不过相对功能较弱，limit返回包含前n个元素的流，当集合大小小于n时，则返回实际长度
     * 如下面的例子返回前两个专业为土木工程专业的学生
     */
    @Test
    public void limitTest() {
        List<Student> civilStudents = students.stream().filter(student -> "土木工程".equals(student.getMajor())).limit(2).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(civilStudents));
    }

    /**
     * 说到limit，不得不提及一下另外一个流操作：sorted。该操作用于对流中元素进行排序，
     * sorted要求待比较的元素必须实现Comparable接口，如果没有实现也不要紧，我们可以将比较器作为参数传递给sorted(Comparator
     */
    @Test
    public void limitTest2() {

        List<Student> civilStudents = students.stream().filter(student -> "土木工程".equals(student.getMajor())).sorted((s1, s2) -> s1.getAge() - s2.getAge()).limit(2).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(civilStudents));

    }

    /**
     * skip操作与limit操作相反，如同其字面意思一样，是跳过前n个元素
     * 比如我们希望找出排序在2之后的土木工程专业的学生，那么可以实现为:
     */
    @Test
    public void skipTest() {
        List<Student> civilStudents = students.stream().filter(student -> "土木工程".equals(student.getMajor())).skip(2).collect(Collectors.toList());
        System.out.println(civilStudents);
    }

    /**
     * 假设我们希望筛选出所有专业为计算机科学的学生姓名，
     * 那么我们可以在filter筛选的基础之上，通过map将学生实体映射成为学生姓名字符串，具体实现如下：
     */
    @Test
    public void mapTest() {
        List<String> names = students.stream()
                .filter(student -> "计算机科学".equals(student.getMajor()))
                .map(Student::getName).collect(Collectors.toList());
        System.out.println(names);
    }


    /**
     *
     */
    @Test
    public void mapToIntTest() {
        int totalAge = students.stream().filter(student -> "计算机科学".equals(student.getMajor()))
                .mapToInt(Student::getAge).sum();
        System.out.println(totalAge);
    }


    /**
     * flatMap与map的区别在于* flatMap是将一个流中的每个值都转成一个个流，然后再将这些流扁平化成为一个流 。
     * 举例说明，假设我们有一个字符串数组String[] strs = {“java8”, “is”, “easy”, “to”, “use”};，我们希望输出构成这一数组的所有非重复字符，那么我们可能首先会想到如下实现
     */
    @Test
    public void mapTes2t() {
        String[] strs = {"java8", "is", "easy", "to", "use"};
        List<String[]> distinctStrs = Arrays.stream(strs)
                .map(str -> str.split(""))  // // 映射成为Stream<String[]>
                .distinct()
                .collect(Collectors.toList());
        System.out.println(JSON.toJSONString(distinctStrs));
    }


    /**
     *
     */
    @Test
    public void flatMapTest(){
        String[] strs = {"java8", "is", "easy", "to", "use"};
        List<String> distinctStrs = Arrays.stream(strs)
                .map(str -> str.split(""))  // 映射成为Stream<String[]>
                .flatMap(Arrays::stream)  // 扁平化为Stream<String>
                .distinct()
                .collect(Collectors.toList());
        System.out.println(JSON.toJSONString(distinctStrs));
    }


    /**
     * allMatch用于检测是否全部都满足指定的参数行为，
     * 如果全部满足则返回true，例如我们希望检测是否所有的学生都已满18周岁，那么可以实现为：
     */
    @Test
    public void allMatchTest(){
        boolean noneCs = students.stream().allMatch(student -> student.getAge()>= 18);
        System.out.println(noneCs);
    }

    /**
     * findFirst用于返回满足条件的第一个元素，比如我们希望选出专业为土木工程的排在第一个学生，那么可以实现如下：
     */
    @Test
    public void findFirstTest(){
        Optional<Student> optStu = students.stream().filter(student -> "土木工程".equals(student.getMajor())).findFirst();
        System.out.println(JSON.toJSONString(optStu));
    }

    /**
     * findAny相对于findFirst的区别在于，findAny不一定返回第一个，
     * 而是返回任意一个，比如我们希望返回任意一个专业为土木工程的学生，可以实现如下：
     */
    @Test
    public void findAnyTest(){
        Optional<Student> optStu = students.stream().filter(student -> "土木工程".equals(student.getMajor())).findAny();
        System.out.println(JSON.toJSONString(optStu));
    }
}

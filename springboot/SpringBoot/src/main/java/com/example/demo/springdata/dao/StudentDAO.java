package com.example.demo.springdata.dao;

import com.example.demo.springdata.domain.Student;

import java.util.List;

/**
 * Created by hunter on 2018/4/3.
 */
public interface StudentDAO {
    /**
     * 查询所有学生
     * @return 所有学生
     */
    public List<Student> query();

    /**
     * 添加一个学生
     * @param student 待添加的学生
     */
    public void save(Student student);
}

package com.example.demo.springdata.dao;

import com.example.demo.springdata.domain.Student;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by hunter on 2018/4/3.
 */
public class StudentDAOImplTest {

    @Test
    public void testQuery() throws Exception {
        StudentDAO studentDAO = new StudentDAOImpl();
        List<Student> students = studentDAO.query();

        for (Student student : students) {
            System.out.println("id:" + student.getId()
                    + " , name:" + student.getName()
                    + ", age:" + student.getAge());
        }
    }

    @Test
    public void testSave() throws Exception {
        StudentDAO studentDAO = new StudentDAOImpl();
        Student student = new Student();
        student.setName("test");
        student.setAge(30);

        studentDAO.save(student);
    }
}
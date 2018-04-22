package com.example.demo.springdata.dao;

import com.example.demo.springdata.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter on 2018/4/3.
 */
@Repository(value = "studentDAOSpringJdbc")
public class StudentDAOSpringJdbcImpl implements  StudentDAO{
    //这里还没用到hibernate
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Student> query() {
        final List<Student> students = new ArrayList<Student>();
        String sql = "select id, name , age from student";

        jdbcTemplate.query(sql, rs -> {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");

            Student student = new Student();
            student.setId(id);
            student.setName(name);
            student.setAge(age);

            students.add(student);
        });

        return students;
    }

    @Override
    public void save(Student student) {
        String sql = "insert into student(name, age) values(?,?)";
        jdbcTemplate.update(sql, new Object[]{student.getName(), student.getAge()});
    }

}

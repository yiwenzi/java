package com.example.demo.repository;

import com.example.demo.domain.Girl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by hunter on 2018/3/28.
 */
public interface GirlRepository extends JpaRepository<Girl,Integer>{
    //通过年龄来查询
    List<Girl> findByAge(Integer age);

    @Query("select max(g.id) from Girl g")
    Integer getGirlId();

    @Query("select g from Girl g")
    List<Girl> queryParams1(String name,Integer age);
}

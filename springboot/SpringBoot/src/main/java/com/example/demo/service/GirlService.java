package com.example.demo.service;

import com.example.demo.domain.Girl;
import com.example.demo.enums.ResultEnum;
import com.example.demo.exception.GirlException;
import com.example.demo.repository.GirlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by hunter on 2018/3/28.
 */
@Service
public class GirlService {
    @Autowired
    private GirlRepository girlRepository;

    @Transactional
    public void insertTwo() {
        Girl girl = new Girl();
        girl.setCupSize("A");
        girl.setAge(18);

        girlRepository.save(girl);

        Girl girl2 = new Girl();
        girl2.setCupSize("B");
        girl2.setAge(19);
        girlRepository.save(girl2);
    }

    public void getAge(Integer id) throws Exception{
        Girl girl = girlRepository.findOne(id);
        Integer age = girl.getAge();
        if(age < 10) {
            throw new GirlException(ResultEnum.PRIMARY_SCHOOL);
        } else if (age > 10 && age < 16) {
            throw new GirlException(ResultEnum.MIDDLE_SCHOOL);
        }
    }

    /** for test **/
    public Girl findOne(Integer id) {
        System.err.println("Repository: " + girlRepository);
        return girlRepository.findOne(id);
    }
}

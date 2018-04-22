package com.example.demo.springdata.dao;

import com.example.demo.utils.SpringUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

/**
 * Created by hunter on 2018/4/3.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataSourceTest {
    private ApplicationContext ctx = null;

    @Before
    public void setUp() {
        ctx = SpringUtil.getApplicationContext();
        //ctx = ContextLoader.getCurrentWebApplicationContext();
    }

    @After
    public void tearDown() {
        ctx = null;
    }
    @Test
    public void testDataSource() {
        DataSource dataSource = (DataSource) ctx.getBean("dataSource");
        String[] beans = ctx.getBeanDefinitionNames();
        for (String bean : beans) {
            System.out.println(bean);
        }
        Assert.assertNotNull(dataSource);
    }
}

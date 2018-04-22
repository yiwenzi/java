package com.joker.concurrency.example.singleton;

import com.joker.concurrency.annotaions.NotThreadSafe;

/**
 * Created by hunter on 2018/4/8.
 * 懒汉模式： 单例的实例在第一次使用时创建
 */
@NotThreadSafe
public class SingletonExample1 {
    //私有构造函数
    private SingletonExample1() {
        //这里可能会有复杂的逻辑
    }
    //单例对象
    private static SingletonExample1 instance = null;

    //静态的工厂方法
    public static SingletonExample1 getInstance() {
        if(instance == null) {
            instance = new SingletonExample1();
        }
        return instance;
    }
}

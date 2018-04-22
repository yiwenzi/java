package com.joker.concurrency.example.singleton;

import com.joker.concurrency.annotaions.ThreadSafe;

/**
 * Created by hunter on 2018/4/8.
 * 饿汉模式： 单例的实例在类装载时创建
 */
@ThreadSafe
public class SingletonExample2 {
    //私有构造函数
    private SingletonExample2() {
        //这里可能会有复杂的逻辑，会导致加载很慢
        //若加载后没有使用，则造成资源的浪费
    }
    //单例对象
    private static SingletonExample2 instance = new SingletonExample2();

    //静态的工厂方法
    public static SingletonExample2 getInstance() {
        return instance;
    }
}

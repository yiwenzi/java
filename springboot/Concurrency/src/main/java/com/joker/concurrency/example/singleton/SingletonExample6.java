package com.joker.concurrency.example.singleton;

import com.joker.concurrency.annotaions.ThreadSafe;

/**
 * Created by hunter on 2018/4/8.
 * 饿汉模式： 单例的实例在类装载时创建
 */
@ThreadSafe
public class SingletonExample6 {
    //私有构造函数
    private SingletonExample6() {
        //这里可能会有复杂的逻辑，会导致加载很慢
        //若加载后没有使用，则造成资源的浪费
    }
    //单例对象
    private static SingletonExample6 instance = null;
    //静态域和静态块的执行顺序不能乱
    static {
        instance = new SingletonExample6();
    }

    //静态的工厂方法
    public static SingletonExample6 getInstance() {
        return instance;
    }
}

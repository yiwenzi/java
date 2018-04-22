package com.joker.concurrency.example.singleton;

import com.joker.concurrency.annotaions.ThreadSafe;

/**
 * Created by hunter on 2018/4/8.
 * 懒汉模式： 单例的实例在第一次使用时创建
 * 但是不推荐，锁的开销大
 */
@ThreadSafe
public class SingletonExample3 {
    //私有构造函数
    private SingletonExample3() {
        //这里可能会有复杂的逻辑
    }
    //单例对象
    private static SingletonExample3 instance = null;

    //静态的工厂方法
    public static synchronized SingletonExample3 getInstance() {
        if(instance == null) {
            instance = new SingletonExample3();
        }
        return instance;
    }
}

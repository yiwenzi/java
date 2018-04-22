package com.joker.concurrency.example.singleton;

import com.joker.concurrency.annotaions.NotThreadSafe;

/**
 * Created by hunter on 2018/4/8.
 * 懒汉模式： 单例的实例在第一次使用时创建
 * 双重同步锁单例模式
 */
@NotThreadSafe
public class SingletonExample4 {
    //私有构造函数
    private SingletonExample4() {
        //这里可能会有复杂的逻辑
    }
    //单例对象
    private static SingletonExample4 instance = null;

    /**
     * 1. memory = allocate() 分配对象的内存空间
     * 2. ctorInstance() 初始化对象
     * 3. instance = memory 设置instance指向刚分配的内存
     */

    //静态的工厂方法
    public static SingletonExample4 getInstance() {
        if(instance == null) {
            synchronized (SingletonExample4.class) {
                if(instance == null) {
                    instance = new SingletonExample4();
                }
            }
        }
        return instance;
    }
}

package com.joker.concurrency.example.singleton;

import com.joker.concurrency.annotaions.ThreadSafe;

/**
 * Created by hunter on 2018/4/8.
 * 枚举模式：最安全
 */
@ThreadSafe
public class SingletonExample7 {
    //私有构造函数
    private SingletonExample7() {

    }

    //静态的工厂方法
    public static SingletonExample7 getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;

        private SingletonExample7 singleton;
        //jvm保证这个方法绝对之调用一次
        Singleton() {
            singleton = new SingletonExample7();
        }
        public SingletonExample7 getInstance() {
            return singleton;
        }
    }

}

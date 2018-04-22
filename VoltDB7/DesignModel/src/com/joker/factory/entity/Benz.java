package com.joker.factory.entity;

/**
 * Created by hunter on 2017/10/18.
 */
public class Benz implements Car {

    @Override
    public void driver() {
        System.out.println("Driving Benz");
    }
}
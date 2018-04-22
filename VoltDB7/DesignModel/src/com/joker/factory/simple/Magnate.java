package com.joker.factory.simple;

import com.joker.factory.entity.Car;

/**
 * Created by hunter on 2017/10/18.
 */
public class Magnate {
    public static void main(String[] args) {
        Car car = Driver.driverCar("benz");
        car.driver();
    }
}

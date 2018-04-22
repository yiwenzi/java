package com.joker.factory.method;

import com.joker.factory.entity.Car;

/**
 * Created by hunter on 2017/10/18.
 */
public class Magnate {
    public static void main(String[] args) {
        DriverMethod driver = new BenzDriver();
        Car car = driver.driverCar();
        car.driver();
    }
}

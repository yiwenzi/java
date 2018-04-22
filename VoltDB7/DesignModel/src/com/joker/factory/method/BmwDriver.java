package com.joker.factory.method;

import com.joker.factory.entity.Bmw;
import com.joker.factory.entity.Car;

/**
 * Created by hunter on 2017/10/18.
 */
public class BmwDriver implements DriverMethod {
    @Override
    public Car driverCar() {
        return new Bmw();
    }
}

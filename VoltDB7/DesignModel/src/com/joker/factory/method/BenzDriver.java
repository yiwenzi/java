package com.joker.factory.method;

import com.joker.factory.entity.Benz;
import com.joker.factory.entity.Car;

/**
 * Created by hunter on 2017/10/18.
 */
public class BenzDriver implements DriverMethod {
    @Override
    public Car driverCar() {
        return new Benz();
    }
}

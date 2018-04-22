package com.joker.factory.method;

import com.joker.factory.entity.Audi;
import com.joker.factory.entity.Car;

/**
 * Created by hunter on 2017/10/18.
 */
public class AudiDriver implements DriverMethod {
    @Override
    public Car driverCar() {
        return new Audi();
    }
}

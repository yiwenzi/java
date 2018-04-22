package com.joker.factory.simple;

import com.joker.factory.entity.Audi;
import com.joker.factory.entity.Benz;
import com.joker.factory.entity.Bmw;
import com.joker.factory.entity.Car;

/**
 * Created by hunter on 2017/10/18.
 */
public class Driver {
    public static Car driverCar(String s){
        if (s.equalsIgnoreCase("Benz")) {
            return new Benz();
        } else if (s.equalsIgnoreCase("Bmw")){
            return new Bmw();
        } else {
            return new Audi();
        }
    }
}

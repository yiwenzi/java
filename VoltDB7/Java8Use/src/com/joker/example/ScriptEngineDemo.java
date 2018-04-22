package com.joker.example;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by hunter on 2018/3/13.
 */
public class ScriptEngineDemo {
    public static void main(String[] args) throws ScriptException {
        System.out.println(calc("1+2-5"));
    }

    private static int calc(String exp) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        return (int) engine.eval(exp);
    }
}

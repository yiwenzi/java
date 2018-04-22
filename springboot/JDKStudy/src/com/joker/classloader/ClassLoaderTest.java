package com.joker.classloader;

/**
 * Created by hunter on 2018/4/8.
 */
public class ClassLoaderTest {
    public static void main(String[] args) {
        //findPath();
        testClassLoader();
    }

    public static void testClassLoader() {
        ClassLoader cl = ClassLoaderTest.class.getClassLoader();
        System.out.println("ClassLoaderTest: " + cl);
        ClassLoader cl2 = int.class.getClassLoader();
        System.out.println("int: " + cl2);
        System.out.println("每一个类加载器都有一个父加载器");

        System.out.println("ClassLoaderTest's parent: " + cl.getParent());
        System.out.println("ClassLoaderTest's parent's parent: " + cl.getParent().getParent());
    }
    public static void findPath() {
        String bootStrapPath = System.getProperty("sun.boot.class.path");
        String extPath = System.getProperty("java.ext.dirs");
        String appPath = System.getProperty("java.class.path");
        System.out.println("bootStrapPath: " + bootStrapPath);
        System.out.println("extPath: " + extPath);
        System.out.println("appPath: " + appPath);
    }
}

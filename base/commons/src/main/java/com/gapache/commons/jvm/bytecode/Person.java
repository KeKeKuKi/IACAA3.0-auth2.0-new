package com.gapache.commons.jvm.bytecode;

/**
 * @author HuSen
 * create on 2020/3/20 2:25 下午
 */
public class Person {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getInfo() {
        return getName() + "\t" + getAge();
    }
}

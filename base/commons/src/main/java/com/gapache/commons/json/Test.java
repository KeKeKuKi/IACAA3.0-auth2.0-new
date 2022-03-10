package com.gapache.commons.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author HuSen
 * create on 2020/3/16 6:14 下午
 */
public class Test {
    public static void main(String[] args) {

        B b = new B();
        b.setA("a");
        b.setB("b");
        String jsonString = JSON.toJSONString(b);
        System.out.println(jsonString);

        B b1 = JSONObject.parseObject(jsonString, B.class);
        System.out.println(JSON.toJSONString(b1));
    }
}

@Data
class A {
    private String a;
}

@EqualsAndHashCode(callSuper = true)
@Data
class B extends A {
    private String b;
}

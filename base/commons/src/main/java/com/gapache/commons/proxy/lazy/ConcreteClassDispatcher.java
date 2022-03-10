package com.gapache.commons.proxy.lazy;

import net.sf.cglib.proxy.Dispatcher;

/**
 * 每次都懒加载
 *
 * @author HuSen
 * @since 2020/5/27 2:06 下午
 */
public class ConcreteClassDispatcher implements Dispatcher {

    @Override
    public Object loadObject() {
        System.out.println("before Dispatcher...");
        PropertyBean propertyBean = new PropertyBean();
        propertyBean.setKey("xxx");
        propertyBean.setValue(new Object());
        System.out.println("after Dispatcher...");
        return propertyBean;
    }
}

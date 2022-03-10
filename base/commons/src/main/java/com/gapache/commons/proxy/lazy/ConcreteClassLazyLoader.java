package com.gapache.commons.proxy.lazy;

import net.sf.cglib.proxy.LazyLoader;

/**
 * 只有第一次懒加载
 *
 * @author HuSen
 * @since 2020/5/27 2:02 下午
 */
public class ConcreteClassLazyLoader implements LazyLoader {

    /**
     * 对需要延迟加载的对象添加代理，在获取该对象属性时先通过代理类回调方法进行对象初始化。
     * 在不需要加载该对象时，只要不去获取该对象内属性，该对象就不会被初始化了
     * （在CGLib的实现中只要去访问该对象内属性的getter方法，就会自动触发代理类回调）。
     *
     * @return PropertyBean
     */
    @Override
    public Object loadObject() {
        System.out.println("before lazyLoader ...");
        PropertyBean propertyBean = new PropertyBean();
        propertyBean.setKey("key");
        propertyBean.setValue(new Object());
        System.out.println("after lazyLoader ...");
        return propertyBean;
    }
}

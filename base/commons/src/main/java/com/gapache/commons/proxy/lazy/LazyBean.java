package com.gapache.commons.proxy.lazy;

import lombok.Getter;
import lombok.Setter;
import net.sf.cglib.proxy.Enhancer;

/**
 * @author HuSen
 * @since 2020/5/27 1:59 下午
 */
@Setter
@Getter
public class LazyBean {
    private String name;
    private Integer age;
    private PropertyBean propertyBean;
    private PropertyBean propertyBeanDispatcher;

    public LazyBean(String name, Integer age) {
        System.out.println("lazy bean init");
        this.name = name;
        this.age = age;
        this.propertyBean = createPropertyBean();
        this.propertyBeanDispatcher = createPropertyBeanDispatcher();
    }

    /**
     * 每次都懒加载
     *
     * @return PropertyBean
     */
    private PropertyBean createPropertyBeanDispatcher() {
        return (PropertyBean) Enhancer.create(PropertyBean.class, new ConcreteClassDispatcher());
    }

    /**
     * 只第一次懒加载
     *
     * @return PropertyBean
     */
    private PropertyBean createPropertyBean() {
        // 使用cglib进行懒加载 对需要延迟加载的对象添加代理，在获取该对象属性时先通过代理类回调方法进行对象初始化。
        // 在不需要加载该对象时，只要不去获取该对象内属性，该对象就不会被初始化了（在CGLib的实现中只要去访问该对象内属性的getter方法，就会自动触发代理类回调）。
        return (PropertyBean) Enhancer.create(PropertyBean.class, new ConcreteClassLazyLoader());
    }

    @Override
    public String toString() {
        return "LazyBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", propertyBean=" + propertyBean +
                '}';
    }
}

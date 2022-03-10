package com.gapache.commons.proxy;

import com.gapache.commons.proxy.lazy.LazyBean;
import net.sf.cglib.proxy.*;

import java.lang.reflect.Method;

/**
 * JDK中的动态代理是通过反射类Proxy以及InvocationHandler回调接口实现的，
 * 但是，JDK中所要进行动态代理的类必须要实现一个接口，也就是说只能对该类所实现接口中定义的方法进行代理，
 * 这在实际编程中具有一定的局限性，而且使用反射的效率也并不是很高。
 * <p>
 * 使用CGLib实现动态代理，完全不受代理类必须实现接口的限制，
 * 而且CGLib底层采用ASM字节码生成框架，使用字节码技术生成代理类，
 * 比使用Java反射效率要高。
 * 唯一需要注意的是，CGLib不能对声明为final的方法进行代理，因为CGLib原理是动态生成被代理类的子类。
 *
 * @author HuSen
 * @since 2020/5/27 11:27 上午
 */
public class ProxyDemo {

    public static void main(String[] args) {
        testInterfaceMaker();
    }

    private static void testInterfaceMaker() {
        try {
            InterfaceMaker interfaceMaker = new InterfaceMaker();
            // 抽取某个类的方法生成接口方法
            interfaceMaker.add(TargetObject.class);
            Class<?> targetInterface = interfaceMaker.create();
            for (Method method : targetInterface.getMethods()) {
                System.out.println(method.getName());
            }
            // 接口代理并设置代理接口方法拦截
            Object object = Enhancer.create(Object.class, new Class[]{targetInterface}, (MethodInterceptor) (obj, method, args1, proxy) -> {
                if (method.getName().equals("method1")) {
                    System.out.println("filter method1 ");
                    return "mmmmmmmmm";
                }
                if (method.getName().equals("method2")) {
                    System.out.println("filter method2 ");
                    return 1111111;
                }
                if (method.getName().equals("method3")) {
                    System.out.println("filter method3 ");
                    return 3333;
                }
                return "default";
            });
            Method method3 = object.getClass().getMethod("method3", int.class);
            int i = (int) method3.invoke(object, 33);
            System.out.println(i);
            Method method1 = object.getClass().getMethod("method1", String.class);
            System.out.println(method1.invoke(object, "sdfs"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testLoadLazy() {
        LazyBean lazyBean = new LazyBean("sensen", 25);
        // 只有第一次懒加载
        lazyBean.getPropertyBean().getKey();
        System.out.println("=============");
        lazyBean.getPropertyBean().getKey();
        System.out.println("=============");
        // 每次都懒加载
        lazyBean.getPropertyBeanDispatcher().getKey();
        System.out.println("=============");
        lazyBean.getPropertyBeanDispatcher().getKey();
        System.out.println("=============");
    }

    private static void testCallbackFilter() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TargetObject.class);

        CallbackFilter callbackFilter = new TargetMethodCallbackFilter();
        Callback noopCb = NoOp.INSTANCE;
        Callback callback1 = new TargetInterceptor();
        Callback fixedValue = new TargetResultFixed();
        Callback[] cbArray = new Callback[]{callback1, noopCb, fixedValue};
        enhancer.setCallbacks(cbArray);
        enhancer.setCallbackFilter(callbackFilter);

        TargetObject targetObject2=(TargetObject)enhancer.create();
        System.out.println(targetObject2);
        System.out.println(targetObject2.method1("sensen"));
        System.out.println(targetObject2.method2(100));
        System.out.println(targetObject2.method3(100));
        System.out.println(targetObject2.method3(200));
    }

    private static void testCallbackMethodInterceptor() {
        // 这里Enhancer类是CGLib中的一个字节码增强器，它可以方便的对你想要处理的类进行扩展，以后会经常看到它。
        // 首先将被代理类TargetObject设置成父类，然后设置拦截器TargetInterceptor，
        // 最后执行enhancer.create()动态生成一个代理类，并从Object强制转型成父类型TargetObject。
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TargetObject.class);
        enhancer.setCallback(new TargetInterceptor());

        TargetObject targetObject = (TargetObject) enhancer.create();
        System.out.println(targetObject);
        System.out.println(targetObject.method1("sensen"));
        System.out.println(targetObject.method2(2));
        System.out.println(targetObject.method3(3));
    }
}

/**
 * 表示锁定方法返回值，无论被代理类的方法返回什么值，回调方法都返回固定值。
 */
class TargetResultFixed implements FixedValue {
    @Override
    public Object loadObject() {
        System.out.println("锁定结果");
        return 999;
    }
}

/**
 * 回调方法过滤器
 * 在CGLib回调时可以设置对不同方法执行不同的回调逻辑，或者根本不执行回调。
 * 在JDK动态代理中并没有类似的功能，对InvocationHandler接口方法的调用对代理类内的所以方法都有效。
 */
class TargetMethodCallbackFilter implements CallbackFilter {

    /**
     * 过滤的方法
     *
     * @param method 要回调额的方法
     * @return 返回的值为数字，代表了Callback数组中的索引位置，要到用的Callback
     */
    @Override
    public int accept(Method method) {
        if (method.getName().equals("method1")) {
            System.out.println("filter method1 == 0");
            return 0;
        }
        if (method.getName().equals("method2")) {
            System.out.println("filter method2 == 1");
            return 1;
        }
        if (method.getName().equals("method3")) {
            System.out.println("filter method3 == 2");
            return 2;
        }
        return 0;
    }
}

/**
 * 目标对象拦截器，实现MethodInterceptor
 */
class TargetInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("调用前");
        Object result = proxy.invokeSuper(obj, args);
        System.out.println("调用后" + result);
        return result;
    }
}

/**
 * 被代理类
 */
class TargetObject {

    public String method1(String paramName) {
        return paramName;
    }

    public int method2(int count) {
        return count;
    }

    public int method3(int count) {
        return count;
    }

    @Override
    public String toString() {
        return "TargetObject []" + getClass();
    }
}

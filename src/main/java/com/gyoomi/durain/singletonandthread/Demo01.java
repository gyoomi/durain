/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.singletonandthread;

/**
 * 6.单例模式和多线程
 *   说明：本章主要考虑的内容，就是如何使单例模式在多线程环境下是安全的、正确的。
 *
 * 6.1 立即加载/饿汉模式
 *
 * @author Leon
 * @version 2018/8/20 16:44
 */
public class Demo01 {
    public static void main(String[] args) {
        Runnable runnable = () -> {
            System.out.println("threadName：" + Thread.currentThread().getName() + "  " + MyObject0101.getInstance().hashCode());
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);
        t1.start();
        t2.start();
        t3.start();
    }
}
class MyObject0101 {
    private static MyObject0101 obj = new MyObject0101();

    private MyObject0101() {}

    public static MyObject0101 getInstance() {
        // 方法没有加同步，容易出现线程安全问题
        // 只能由一个实例变量，不能有其他的实例变量
        return obj;
    }
}
// ----------------------------------------------------
/**
 * 6.2 延迟加载/懒汉模式
 *     1.延迟加载解析
 *     2.懒汉模式的缺点
 *       说明：多个线程会得到不同的对象。破坏了单例模式。
 *     3.上面等我问题解决
 *       1)synchronized关键字，实现get方法同步
 *       2）效率低
 *     4.使用同步代码块，效果也是一样的。效率一样低下。
 *     5.使用DCL双重检查机制解决单例和效率问题
 *
 */
class Run0101 {
    public static void main(String[] args) {
        Runnable runnable = () -> {
            System.out.println(MyObject0102.getInstance().hashCode());
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);
        t1.start();
        t2.start();
        t3.start();
    }
}
class MyObject0102 {
    private static MyObject0102 obj;
    private MyObject0102() {}
    public synchronized static MyObject0102 getInstance() {
        try {
            if (obj == null) {
                // 模拟一些业务代码
                Thread.sleep(2000);
                synchronized (MyObject0102.class) {
                    if (obj == null) {
                        obj = new MyObject0102();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
// ---------------------------------------------------
/**
 * 6.3 使用静态内置类来实现单例模式
 *     说明：dcl双重检查机制可以解决多线程下单例模式的问题，同样使用静态内置类也可以解决。
 *
 *
 */
class Run0102 {
    public static void main(String[] args) {
        Runnable runnable = () -> System.out.println(MyObject0103.getInstance().hashCode());
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);
        t1.start();
        t2.start();
        t3.start();
    }
}
class MyObject0103 {
    private MyObject0103() {}

    public static MyObject0103 getInstance() {
        return MyObjectHandler.obj;
    }

    public static class MyObjectHandler {
        private static MyObject0103 obj = new MyObject0103();
    }
}
// ---------------------------------------------------
/**
 * 6.4 序列化和反序列化单例模式的实现
 *     说明：
 *
 *
 */
class Run0103 {
    public static void main(String[] args) {

    }
}






















/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.communicate;

/**
 * 4.InheritableThreadLocal类的使用
 *   使用InheritableThreadLocal类可以在子线程中获取父线程中继承下来的值。
 *
 *
 * 4.1 值继承
 *     说明：使用InheritableThreadLocal类可以使子线程从父线程中获取值。
 *     实例：
 *         如下。
 *
 * @author Leon
 * @version 2018/8/16 11:09
 */
public class ThreadDemo04 {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            System.out.println(" Main get value = " + Tools.tl.get());
            Thread.sleep(200);
        }
        Thread0401 t1 = new Thread0401();
        t1.start();
    }
}
class InheritableThreadLocalExt extends InheritableThreadLocal {
    @Override
    protected Object initialValue() {
        return System.currentTimeMillis();
    }
}
class Tools {
    public static InheritableThreadLocalExt tl = new InheritableThreadLocalExt();
}
class Thread0401 extends Thread {
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("Thread A get value = " + Tools.tl.get());
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
// ----------------------------------------

/**
 * 4.2 值继承与再修改
 *     说明：如果在继承的时候，可以对值进行进一步的处理就好了。
 *     实例：
 *     结果：
 *          Main get value = 1534390078232
 *          Thread A get value = 1534390078232~我是在子线程添加的
 *          Thread A get value = 1534390078232~我是在子线程添加的
 *     注意：
 *         如果子线程在取得值得同时，主线程将InheritableThreadLocal的值进行修改，那么子线程获取的值还是旧值。
 *
 */
class Run0402 {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            System.out.println(" Main get value = " + Tools2.tl.get());
            Thread.sleep(200);
        }
        Thread0402 t = new Thread0402();
        t.start();
    }
}
class InheritableThreadLocalExt2 extends InheritableThreadLocal {
    @Override
    protected Object initialValue() {
        return System.currentTimeMillis();
    }

    @Override
    protected Object childValue(Object parentValue) {
        return parentValue + "~我是在子线程添加的";
    }
}
class Tools2 {
    public static InheritableThreadLocalExt2 tl = new InheritableThreadLocalExt2();
}
class Thread0402 extends Thread {
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("Thread A get value = " + Tools2.tl.get());
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}





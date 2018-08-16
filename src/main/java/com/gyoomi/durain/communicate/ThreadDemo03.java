/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.communicate;

/**
 * 3.类ThreadLocal的使用
 *   说明：
 *       变量值的共享可以使用public static的形式，所有的线程使用同一个public static变量。
 *       如果想要实现每个线程都有自己的共享变量该怎么解决？可以通过ThreadLocal来解决。
 *   解释：
 *       主要解决每个线程绑定自己的值。可以将ThreadLocal比喻成全局存放数据的盒子，盒子中可以存储每个线程的私有数据。
 *
 *
 *
 *
 * @author Leon
 * @version 2018/8/15 17:50
 */
public class ThreadDemo03 {
}
// ---------------------------------------
/**
 * 3.1 方法get()与null
 *     说明：
 *         类ThreadLocal解决的是变量在不同线程间的隔离性。也就是不同线程拥有不同的变量值。
 *
 */
class Run0301 extends Thread {
    public static ThreadLocal threadLocal = new ThreadLocal();
    public static void main(String[] args) {
        if (threadLocal.get() == null) {
            System.out.println("从来没有放过值");
            threadLocal.set("我的值");
        }
        System.out.println(threadLocal.get());
        System.out.println(threadLocal.get());
    }
}
// ---------------------------------------
/**
 * 3.2 验证线程变量的隔离性
 *     实例：
 *         可见每个线程都有自己的变量区间
 *     问题：
 *         但是第一次get()的时候，如果没有放值。返回null.怎么解决不让他返回null,也就是默认值。
 *
 *
 */
class Run0302 {
    public static void main(String[] args) {
        Thread0302 t1 = new Thread0302();
        Thread0303 t2 = new Thread0303();
        t1.start();
        t2.start();
    }
}
class Tool0302 {
    public static ThreadLocal tl = new ThreadLocal();
}
class Thread0302 extends Thread {
    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                Tool0302.tl.set("Thread A" + (i + 1));
                System.out.println("Thread A getValue = " + Tool0302.tl.get());
                Thread.sleep(400);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Thread0303 extends Thread {
    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                Tool0302.tl.set("Thread B" + (i + 1));
                System.out.println("Thread B getValue = " + Tool0302.tl.get());
                Thread.sleep(400);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
// ---------------------------------------
/**
 * 3.3 解决get()返回null问题
 *     实例：
 *         此方法只证明了main方法尤其的值，不知道其他线程是否尤其默认值。
 *         见下
 *
 */
class Run0303 {
    public static ThreadLocalExt tlx = new ThreadLocalExt();
    public static void main(String[] args) {
        if (tlx.get() == null) {
            System.out.println("从来没有放过值");
            tlx.set("我的值");
        }
        System.out.println(tlx.get());
        System.out.println(tlx.get());
        System.out.println(tlx.get());
    }
}
class ThreadLocalExt extends ThreadLocal{
    @Override
    protected Object initialValue() {
        return System.currentTimeMillis();
    }
}
// ---------------------------------------

/**
 * 3.4 再次验证数据的隔离性
 *     结论;
 *         从打印结果呢再次验证了数据的隔离性。
 */
class Run0304 {
    public static void main(String[] args) {
        try {
            Thread0304 t = new Thread0304();
            t.start();
            Thread.sleep(5000);
            for (int i = 0; i < 10; i++) {
                System.out.println("Main get value = " + Tool0304.tl.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Tool0304 extends ThreadLocal {
    public static ThreadLocalExt tl = new ThreadLocalExt();
}
class Thread0304 extends Thread {
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("Thread A get value = " + Tool0304.tl.get());
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}












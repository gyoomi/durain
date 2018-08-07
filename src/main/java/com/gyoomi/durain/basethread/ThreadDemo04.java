/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * 自定义线程类中：实例变量针对其他线程有共享和非共享之分，这是多个线程之间进行交互的重要技术点
 *
 * @author Leon
 * @version 2018/8/7 21:37
 */
public class ThreadDemo04 {

    public static void main(String[] args) {
        MyThread05 my = new MyThread05();
        Thread t1 = new Thread(my, "A");
        Thread t2 = new Thread(my, "B");
        Thread t3 = new Thread(my, "C");
        Thread t4 = new Thread(my, "D");
        Thread t5 = new Thread(my, "E");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }

    public static void test01() {
        MyThread04 m1 = new MyThread04("A");
        MyThread04 m2 = new MyThread04("B");
        MyThread04 m3 = new MyThread04("C");
        m1.start();
        m2.start();
        m3.start();
    }
}

/**
 * 不共享
 */
class MyThread04 extends Thread {
    private int count = 5;

    public MyThread04(String name) {
        super();
        // 设置名称
        this.setName(name);
    }

    @Override
    public void run() {
        super.run();
        while (count > 0) {
            count--;
            System.out.println("由" + this.currentThread().getName() + "计算count = " + count);
        }
    }
}

/**
 *
 * 共享数据
 * i--不是线程安全的
 * 过程如下：
 *   1.取到i的值
 *   2.-1
 *   3.把新值赋值回去
 *
 *   由此通过synchronized关键字来解决
 *   实现多个线程对同一对象的同一变量的值进行操作时，进行同步
 */
class MyThread05 extends Thread {
    private int count = 5;

    @Override
    public void run() {
        super.run();
        count--;
        System.out.println("由" + this.currentThread().getName() + "计算count = " + count);
    }
}

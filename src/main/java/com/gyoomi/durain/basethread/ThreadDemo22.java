/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * 线程的优先级
 *    在系统中，线程可以划分优先级。高的可以获取更多的cpu资源.
 * 设置线程优先级：可以有助于“线程规划器”，来确定选择下一个线程哪个优先执行。
 *
 * java中的线程优先级的范围是1-10，不在范围都会报错
 *
 * @author Leon
 * @version 2018/8/8 22:24
 */
public class ThreadDemo22 {
}


// -----------------------------------

/**
 * 线程优先级的继承特性
 *
 * 如：利用a线程启动b线程，那么b线程优先级将继承a线程。
 */
class Run2201 {

    public static void main(String[] args) {
        System.out.println("before Main thread the priority = "  + Thread.currentThread().getPriority());
        Thread.currentThread().setPriority(6);
        System.out.println("after Main thread the priority = "  + Thread.currentThread().getPriority());
        MyThread2201 thread = new MyThread2201();
        thread.start();
    }
}

class MyThread2201 extends Thread {

    @Override
    public void run() {
        System.out.println("MyThread2201 run the priority = " + this.getPriority());
        MyThread2203 thread2203 = new MyThread2203();
        thread2203.start();
    }
}

class MyThread2203 extends Thread {

    @Override
    public void run() {
        System.out.println("MyThread2203 run the priority = " + this.getPriority());
    }
}

// ---------------------------------------------------------------

/**
 * 优先级具有规则性
 * 虽然可以调用setPriority(int)设置线程的优先级，但是还没有看到设置优先级所带来的效果
 *
 * 测试结果：
 *     我们发现，优先级高的代码总会先执行完，但是不带表优先级的线程代码会被全部执行完。
 *     谁先执行无代码顺序无关。
 *
 *  总结：
 *       可以看出优先级高具有一定的规则性，也就是cpu会尽量将执行的资源让给优先级高的线程
 */
class Run2203 {

    public static void main(String[] args) {
        MyThread2204 thread1 = new MyThread2204();
        MyThread2205 thread2 = new MyThread2205();
        thread1.setPriority(10);
        thread2.setPriority(1);
        thread1.start();
        thread2.start();
    }
}

class MyThread2204 extends Thread {
    @Override
    public void run() {
        long start = System.currentTimeMillis();
        int addResult = 0;
        for (int i = 0; i < 500000000; i++) {
            addResult = addResult + i;
        }
        long end = System.currentTimeMillis();
        System.out.println("*****Thread 1 use time = " + (end - start));
    }
}

class MyThread2205 extends Thread {
    @Override
    public void run() {
        long start = System.currentTimeMillis();
        int addResult = 0;
        for (int i = 0; i < 500000000; i++) {
            addResult = addResult + i;
        }
        long end = System.currentTimeMillis();
        System.out.println("☆☆☆☆☆Thread 2 use time = " + (end - start));
    }
}

// --------------------------------------------

/**
 * 优先级具有随机性
 * 说明；
 *     前面介绍线程优先级高的线程则限制性run方法中代码，其实，这是不能太肯定，
 *     因为线程的优先级还具有一定随机性。优先级高的不一定每次都执行完。
 *
 */
class Run2204 {

}



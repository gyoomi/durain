/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * 在沉睡中停止
 * 结论：
 *      在sleep的状态下停止某一线程，会进入其catch块；并且还会清除停止状态值，使其变成false
 *      无论是先sleep后interrupt还是先interrupt后sleep结果都是一样的。
 * @author Leon
 * @version 2018/8/8 17:30
 */
public class ThreadDemo14 {

    public static void main(String[] args) throws Exception {
        // 先interrupt 再sleep
        MyThread1502 thread = new MyThread1502();
        thread.start();
        System.out.println("Main begin");
        thread.interrupt();
        System.out.println("Main end");
    }

    public static void test01() throws Exception {
        // 先sleep,再interrupt
        MyThread15 thread = new MyThread15();
        thread.start();
        System.out.println("Main begin");
        Thread.sleep(1000);
        thread.interrupt();
        System.out.println("Main end");
    }
}

class MyThread15 extends Thread {
    @Override
    public void run() {
        try {
            System.out.println("run begin");
            Thread.sleep(2000000);
            System.out.println("run end");
        } catch (InterruptedException e) {
            System.out.println("进入了MyThread15类的run方法的catch块。" + this.isInterrupted());
            e.printStackTrace();
        }
    }
}

class MyThread1502 extends Thread {

    @Override
    public void run() {
        try {
            for (int i = 0; i < 500000; i++) {
                System.out.println("i=" + (i + 1));
            }
            System.out.println("run begin");
            Thread.sleep(2000000);
            System.out.println("run end");
        } catch (InterruptedException e) {
            System.out.println("进入了MyThread1502类的run方法的catch块。" + this.isInterrupted());
            e.printStackTrace();
        }
    }
}

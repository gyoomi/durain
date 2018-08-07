/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * Thread.currentThread()和this的区别
 *
 * Thread.currentThread()：正在执行代码的线程
 * this至当前这个类的线程对象
 *
 * @author Leon
 * @version 2018/8/7 22:37
 */
public class ThreadDemo08 {

    public static void main(String[] args) {
        MyThread09 my = new MyThread09();
        Thread t = new Thread(my);
        System.out.println("Main start t isAlive = " + t.isAlive());
        t.setName("A");
        t.start();
        System.out.println("Main end t isAlive = " + t.isAlive());
    }
}

class MyThread09 extends Thread {

    public MyThread09() {
        System.out.println("MyThread09 begin--->");
        System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
        System.out.println("Thread.currentThread().isAlive() = " + Thread.currentThread().isAlive());
        System.out.println("this.getName() = " + this.getName());
        System.out.println("this.isAlive() = " + this.isAlive());
        System.out.println("MyThread09 end--->");
    }

    @Override
    public void run() {
        System.out.println("run begin--->");
        System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
        System.out.println("Thread.currentThread().isAlive() = " + Thread.currentThread().isAlive());
        System.out.println("this.getName() = " + this.getName());
        System.out.println("this.isAlive() = " + this.isAlive());
        System.out.println("run end--->");
    }
}

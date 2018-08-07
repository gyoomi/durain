/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * 留意i--和System.out.println()的异常
 *
 * 虽然println是线程安全的，但是i--是非线程安全的。
 * 而i--是在进如println方法前执行，所以还是有概率执行的
 *
 *
 *
 * @author Leon
 * @version 2018/8/7 22:04
 */
public class ThreadDemo05 {
    public static void main(String[] args) {
        MyThread06 run = new MyThread06();
        Thread t1 = new Thread(run);
        Thread t2 = new Thread(run);
        Thread t3 = new Thread(run);
        Thread t4 = new Thread(run);
        Thread t5 = new Thread(run);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}

class MyThread06 extends Thread {
    private int count = 5;

    @Override
    public void run() {
        System.out.println("count=" + (count--) + this.currentThread().getName());
    }
}

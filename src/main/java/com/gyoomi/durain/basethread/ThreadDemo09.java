/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * sleep()方法
 * 作用：在指定毫秒数内让“当前正在执行的线程”休眠，也就是暂停执行
 * 这个正在执行的线程指的是this.currentThread()
 *
 * @author Leon
 * @version 2018/8/8 9:33
 */
public class ThreadDemo09 {

    public static void main(String[] args) {
        MyThread10 my = new MyThread10();
        System.out.println("start=" + System.currentTimeMillis());
        my.start();
        System.out.println("end=" + System.currentTimeMillis());
    }
}

class MyThread10 extends Thread {
    @Override
    public void run() {
        try {
            System.out.println("run threadName=" + this.currentThread().getName() + " begin");
            Thread.sleep(2000);
            System.out.println("run threadName=" + this.currentThread().getName() + " end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

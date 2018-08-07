/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * 1.线程执行的随机性
 * 2.start不代表启动顺序，只是表示该线程可以被系统调用的准备
 *
 *
 */
class MyThread extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " Run...");
            // 加入此段代码，将代码的执行过程放大，看到线程执行的随机
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
/**
 * 类功能描述
 *
 * @author Leon
 * @version 2018/8/7 21:01
 */
public class ThreadDemo02 {

    public static void main(String[] args) {
        MyThread my = new MyThread();
        my.start();
        for (int i = 0; i < 10; i++) {
            System.out.println("Main run...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

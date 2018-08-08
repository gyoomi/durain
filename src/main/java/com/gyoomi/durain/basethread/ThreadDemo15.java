/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * 能停止的线程---暴力停止
 *     stop()
 * stop执行后线程直接退出
 *
 * @author Leon
 * @version 2018/8/8 20:32
 */
public class ThreadDemo15 {

    public static void main(String[] args) {
        try {
            MyThread16 thread = new MyThread16();
            thread.start();
            Thread.sleep(8000);
            thread.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyThread16 extends Thread {

    private int i = 0;
    @Override
    public void run() {
        try {
            while (true) {
                i++;
                System.out.println("i = " + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

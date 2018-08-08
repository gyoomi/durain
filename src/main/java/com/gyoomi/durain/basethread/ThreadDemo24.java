/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * 守护线程
 *
 * java中有用户线程和守护线程之分。
 * 守护线程是一种特殊的线程。，当当前线程不存在非守护线程时，守护线程则会自动销毁。
 * 最典型的例子就是：垃圾回收线程
 *
 * @author Leon
 * @version 2018/8/8 22:56
 */
public class ThreadDemo24 {

    public static void main(String[] args) {
        try {
            MyThread24 thread = new MyThread24();
            thread.setDaemon(true);
            thread.start();
            Thread.sleep(5000);
            System.out.println("离开了thread对象，也就不在打印了，也就是停止了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyThread24 extends Thread {
    private int i = 0;

    @Override
    public void run() {
        try {
            while (true) {
                i++;
                System.out.println("i = " + (i + 1));
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

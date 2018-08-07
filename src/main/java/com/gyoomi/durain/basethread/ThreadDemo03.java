/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

class MyThread02 extends Thread {
    private int i;

    public MyThread02(int i) {
        super();
        this.i = i;
    }

    @Override
    public void run() {
        System.out.println(i);
    }
}
/**
 * 重点：start不代表启动顺序，只是表示该线程可以被系统调用的准备
 *
 * @author Leon
 * @version 2018/8/7 21:29
 */
public class ThreadDemo03 {

    public static void main(String[] args) {
        MyThread02 t1 = new MyThread02(1);
        MyThread02 t2 = new MyThread02(2);
        MyThread02 t3 = new MyThread02(3);
        MyThread02 t4 = new MyThread02(4);
        MyThread02 t5 = new MyThread02(5);
        MyThread02 t6 = new MyThread02(6);
        MyThread02 t7 = new MyThread02(7);
        MyThread02 t8 = new MyThread02(8);
        MyThread02 t9 = new MyThread02(9);
        MyThread02 t10 = new MyThread02(10);
        MyThread02 t11 = new MyThread02(11);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        t9.start();
        t10.start();
        t11.start();
    }
}

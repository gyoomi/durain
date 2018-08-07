/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * isAlive()方法
 *   判断当前线程是否处于活动状态
 * 活动状态：
 *     就是线程已经启动，但是并未终止。就是活动状态。（处于运行或准备运行的都是alive的）
 *
 * @author Leon
 * @version 2018/8/7 22:28
 */
public class ThreadDemo07 {

    public static void main(String[] args) throws Exception {
        MyThread08 my = new MyThread08();
        System.out.println("begin=" + my.isAlive());
        my.start();
        Thread.sleep(1000);
        System.out.println("begin=" + my.isAlive());
    }
}

class MyThread08 extends Thread {

    @Override
    public void run() {
        System.out.println("run=" + this.isAlive());
    }
}

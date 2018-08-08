/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * 能停止的线程---异常法
 *
 * 出现的问题：
 *    for循环break之后，for之后的代码又继续执行了
 *
 * 如何解决：利用try catch解决
 *
 * @author Leon
 * @version 2018/8/8 13:56
 */
public class ThreadDemo13 {

    public static void main(String[] args) throws Exception {
        MyThread14 thread = new MyThread14();
        thread.start();
        Thread.sleep(2000);
        thread.interrupt();
        System.out.println("end");
    }
}

class MyThread14 extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < 600000; i++) {
            if (this.interrupted()) {
                System.out.println("已经是停止状态了，我要退出了");
                break;
            }
            System.out.println("i-" + (i + 1));
        }
        System.out.println("我又被输出了，此代码是for又继续运行了，线程并未停止");
    }
}

class Run2 {

    public static void main(String[] args) {
        try {
            MyThread1402 thread = new MyThread1402();
            thread.start();
            Thread.sleep(1000);
            thread.interrupt();
            System.out.println("end");
        } catch (InterruptedException e) {
            System.err.println("进入了Main方法的catch:::块中");
            e.printStackTrace();
        }
    }
}

class MyThread1402 extends Thread {
    @Override
    public void run() {
        super.run();
        try {
            for (int i = 0; i < 600000; i++) {
                if (this.interrupted()) {
                    System.out.println("已经是停止状态了，我要退出了");
                    throw new InterruptedException();
                }
                System.out.println("i-" + (i + 1));
            }
            System.out.println("我在for下面");
        } catch (Exception e) {
            System.err.println("进入了MyThread1402类的run方法的catch:::块中");
            e.printStackTrace();
        }
    }
}

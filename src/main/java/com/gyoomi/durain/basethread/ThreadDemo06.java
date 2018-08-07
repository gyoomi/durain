/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * currentThread()方法
 * 返回代码块正在被哪个线程的调用信息
 *
 * 测试结果：
 *        构造方法的打印：main
 *        run方法的打印：Thread-0
 * 说明：
 *      构造器被main调用，run被线程调用，还是自动调用
 *
 *  Thread.currentThread().getName()得到的正在执行代码的线程的名字
 *  getName得到的线程的属性名
 *
 * @author Leon
 * @version 2018/8/7 22:13
 */
public class ThreadDemo06 {

    public static void main(String[] args) {
        MyThread07 my07 = new MyThread07();
        my07.start();
    }
}

class MyThread07 extends Thread {

    public MyThread07() {
        System.out.println("构造方法的打印：" + Thread.currentThread().getName());
    }

    @Override
    public void run() {
        System.out.println("run方法的打印：" + Thread.currentThread().getName());
    }
}

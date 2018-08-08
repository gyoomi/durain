/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * 停止线程
 * 含义：停止一个线程意味着在线程处理完任务之前停掉正在做的操作，也就是放弃当前操作。
 *     1.Thread.stop()线程不安全的，已废弃的方法
 *     2.interrupt(),需要加一些判断才能终止线程
 * Java中三种方式停止线程
 *     1.使用退出标志，使线程正常退出。也就是run方法完成后结束
 *     2.使用stop()方法强行终止线程，但是不推荐。和suspend,resume方法一样，作废方法，会产生不可预料的后果
 *     3.使用interrupt()中断线程
 *
 * @author Leon
 * @version 2018/8/8 11:42
 */
public class ThreadDemo11 {

    public static void main(String[] args) throws Exception {
        MyThread12 my = new MyThread12();
        my.start();
        Thread.sleep(2000);
        my.interrupt();
        // 调用interrupt方法，仅仅是在当前线程打了一个停止的标记，并不是真的停止线程
    }


}

class MyThread12 extends Thread {
    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 50000; i++) {
            System.out.println("i=" + (i + 1));
        }
    }
}

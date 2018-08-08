/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * 暂停线程
 *    暂停线程就意味着此线程还可以恢复执行。java中可以suspend()暂停线程，使用resume恢复线程。
 *
 *
 * 测试结果：
 *   A=1533735773198 i = 2324226853
 *   A=1533735778199 i = 2324226853
 *   B=1533735783207 i = 4634337122
 *   B=1533735788207 i = 6920402436
 * @author Leon
 * @version 2018/8/8 21:34
 */
public class ThreadDemo19 {

    public static void main(String[] args) {
        try {
            Thread20 thread = new Thread20();
            thread.start();
            Thread.sleep(5000);
            // A段
            thread.suspend();
            System.out.println("A=" + System.currentTimeMillis() + " i = " + thread.getI());
            Thread.sleep(5000);
            System.out.println("A=" + System.currentTimeMillis() + " i = " + thread.getI());
            // B段
            thread.resume();
            Thread.sleep(5000);
            // C段
            System.out.println("B=" + System.currentTimeMillis() + " i = " + thread.getI());
            Thread.sleep(5000);
            System.out.println("B=" + System.currentTimeMillis() + " i = " + thread.getI());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Thread20 extends Thread {

    private long i;

    public long getI() {
        return i;
    }

    public void setI(long i) {
        this.i = i;
    }

    @Override
    public void run() {
        while (true) {
            i++;
        }
    }
}

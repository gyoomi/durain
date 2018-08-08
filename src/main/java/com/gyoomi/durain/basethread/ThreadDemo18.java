/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * 使用return停止线程
 *
 *    不过还是建议抛异常的方式来实现线程的终止，这样异常可以在向上跑的过程中得到传播
 *
 * @author Leon
 * @version 2018/8/8 21:04
 */
public class ThreadDemo18 {

    public static void main(String[] args) {
        try {
            Thread19 thread = new Thread19();
            thread.start();
            Thread.sleep(3000);
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Thread19 extends Thread {

    @Override
    public void run() {
        while (true) {
            if (this.isInterrupted()) {
                System.out.println("停止了");
                return;
            }
            System.out.println("Timer = " + System.currentTimeMillis());
        }
    }
}

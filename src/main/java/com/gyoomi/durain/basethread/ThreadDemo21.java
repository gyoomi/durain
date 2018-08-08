/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * yield()方法
 *
 * 作用：放弃当前cpu资源，将它让给其他任务取占用CPU执行资源。
 *       但是放弃时间不确定，有可能刚刚放弃，但是又马上获得了cpu的时间片轮
 *
 *  本实例中：可以获取运行时间，来作为结果进行比较
 *    根据执行实现的长短可以看出yield的作用
 *
 * @author Leon
 * @version 2018/8/8 22:16
 */
public class ThreadDemo21 {

    public static void main(String[] args) {
        MyThread22 thread = new MyThread22();
        thread.start();
    }
}

class MyThread22 extends Thread {

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        int count = 0;
        for (int i = 0; i < 50000000; i++) {
            // this.yield();
            count = count + (i + 1);
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start));
    }
}

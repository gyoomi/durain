/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * stop()方法与java.lang.ThreadDeath异常
 *
 * 调用stop方法，会抛出java.lang.ThreadDeath异常，但是通常情况下此异常不需要显示捕捉。
 *
 * 测试结论 ：
 *     1.stop()方法已经作废，如果强制使用，可能会导致一些请理性工作得不到完成
 *     2.另外，就是对锁定的对象进行“解锁”，导致数据得不到同步处理，会出现数据不一致的现象
 * @author Leon
 * @version 2018/8/8 20:38
 */
public class ThreadDemo16 {

    public static void main(String[] args) {
        MyThread17 thread = new MyThread17();
        thread.start();
    }
}

class MyThread17 extends Thread {

    @Override
    public void run() {
        try {
            this.stop();
            // 必须是ThreadDeath，否则捕捉不到异常
        } catch (ThreadDeath e) {
            System.out.println("进入了MyThread17类的catch中");
            e.printStackTrace();
        }
    }

}

/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * 判断线程是否停止状态
 *   1.this.interrupted()  测试当前线程是否已经中断
 *   2.this.isInterrupted()  测试线程是否已经中断
 *
 *
 * interrupted()和isInterrupted()总结：
 *    1.this.interrupted()测试当前线程是否是中断状态，执行后并将状态置为false的功能
 *    2.isInterrupted()测试线程Thread对象是否是中断状态，但是不清楚状态
 * @author Leon
 * @version 2018/8/8 12:40
 */
public class ThreadDemo12 {

    public static void main(String[] args) throws Exception {

    }

    /**
     * 1.interrupted()测试当前线程是否终止过。由于当前线程是main,所以从来没有终止过
     * 2.interrupted()具备清楚状态的功能，多次调用，后面的结果会返回false
     *
     * @throws Exception
     */
    public static void test01() throws Exception {
        MyThread13 my = new MyThread13();
        my.start();
        Thread.sleep(1000);
        my.interrupt();
        System.out.println("是否停止 1? = " + my.interrupted());
        System.out.println("是否停止 2? = " + my.interrupted());

        System.out.println("end");
    }
}

class MyThread13 extends Thread {
    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 500000; i++) {
            System.out.println("i=" + (i + 1));
        }
    }
}


class Run3 {

    /**
     * isInterrupted()测试线程Thread对象是否是中断状态，并且不具备清楚状态的功能
     * 所以看到两个true
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        MyThread13 run3 = new MyThread13();
        run3.start();
        Thread.sleep(1000);
        run3.interrupt();
        System.out.println("是否停止 1? = " + run3.isInterrupted());
        System.out.println("是否停止 2? = " + run3.isInterrupted());

        System.out.println("end");
    }
}
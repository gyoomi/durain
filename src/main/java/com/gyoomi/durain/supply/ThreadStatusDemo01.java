/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.supply;

/**
 * 7. 查缺补漏
 * 7.1 线程的状态
 *     说明：
 *         线程对象在不同的运行时期有不同的运行状态，状态信息就保存在State枚举类中.
 *         调用的与线程有关的方法造成线程改变的主要原因。
 *    1.验证new、runnable、terminated
 *    2.验证timed_waiting
 *      说明：
 *          线程状态timed_waiting代表线程执行了Thread.sleep()方法，呈等待状态，等待时间到达，继续向下运行
 *    3.验证blocked
 *      说明：
 *          blocked状态出现在某一个线程正在等待锁的时候
 *    4.验证waiting
 *      说明：
 *          此状态是线程执行了Object.wait()方法后所处的状态。
 *
 *
 *
 * @author Leon
 * @version 2018/8/20 21:45
 */
public class ThreadStatusDemo01 {
    public static void main(String[] args) throws InterruptedException {
        MyThread0101 t = new MyThread0101();
        System.out.println("main方法中状态1：" + t.getState());
        Thread.sleep(1000);
        t.start();
        Thread.sleep(1000);
        System.out.println("main方法中状态2：" + t.getState());
    }
}
class MyThread0101 extends Thread {
    public MyThread0101() {
        System.out.println("构造方法中的状态：" + Thread.currentThread().getState());
    }

    @Override
    public void run() {
        System.out.println("run方法中：" + Thread.currentThread().getState());
    }
}
class Run0102 {
    public static void main(String[] args) throws InterruptedException {
        MyThread0102 t = new MyThread0102();
        t.start();
        Thread.sleep(1000);
        // 验证TIMED_WAITING状态
        System.out.println("Main方法中的状态：" + t.getState());
    }
}
class MyThread0102 extends Thread {
    @Override
    public void run() {
        System.out.println("begin sleep");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end   sleep");
    }
}
class Run0103 {
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> MyService0103.testService();
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        Thread.sleep(1000);
        System.out.println("Main方法中t2的线程：" + t2.getState());
    }
}
class MyService0103 {
    public synchronized static void testService() {
        try {
            System.out.println(Thread.currentThread().getName() + "进入了业务方法了");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Run0104 {
    public static void main(String[] args) throws InterruptedException {
        MyThread0105 t = new MyThread0105(new Object());
        t.start();
        Thread.sleep(1000);
        System.out.println("Main方法中的t线程的状态：" + t.getState());
    }
}
class MyThread0105 extends Thread {
    private Object object;
    public MyThread0105(Object object) {
        this.object = object;
    }

    @Override
    public void run() {
        try {
            synchronized (object) {
                object.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

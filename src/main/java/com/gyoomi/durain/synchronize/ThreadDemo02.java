/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.synchronize;

/**
 * Synchronized语句块
 *
 * synchronized方法的弊端
 *     说明：利用sync来声明方法在某些情况下是十分糟糕的。例如线程a调用同步方法来执行一个长时间的任务，
 *           那么线程b必须进行长时间的等待。在这样的情况下，可以使用sync语句块来解决
 *
 *  以下例子很明显：说明用来至少6秒，才执行完这写代码。
 *
 * @author Leon
 * @version 2018/8/9 21:29
 */
public class ThreadDemo02 {
    public static void main(String[] args) throws Exception {
        Task task = new Task();
        MyThread0201 t1 = new MyThread0201(task);
        MyThread0202 t2 = new MyThread0202(task);
        t1.start();
        t2.start();

        Thread.sleep(7000);
        long begin = CommonUtils.beginTime1;
        if (CommonUtils.beginTime2 < CommonUtils.beginTime1) {
            begin = CommonUtils.beginTime2;
        }
        long end = CommonUtils.endTime1;
        if (CommonUtils.endTime2 > CommonUtils.endTime1) {
            end = CommonUtils.endTime2;
        }
        System.out.println("耗时：" + (end - begin));
    }
}

class Task {
    private String getData1;
    private String getData2;
    public synchronized void doLongTask() {
        try {
            System.out.println("task begin");
            Thread.sleep(3000);
            getData1 = "长时间处理任务 返回值 1 。thread = " + Thread.currentThread().getName();
            getData2 = "长时间处理任务 返回值 2 。thread = " + Thread.currentThread().getName();
            System.out.println(getData1);
            System.out.println(getData2);
            System.out.println("task end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class CommonUtils {
    public static long beginTime1;
    public static long endTime1;
    public static long beginTime2;
    public static long endTime2;
}
class MyThread0201 extends Thread {
    private Task task;
    public MyThread0201(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        super.run();
        CommonUtils.beginTime1 = System.currentTimeMillis();
        task.doLongTask();
        CommonUtils.endTime1 = System.currentTimeMillis();
    }
}
class MyThread0202 extends Thread {
    private Task task;
    public MyThread0202(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        super.run();
        CommonUtils.beginTime2 = System.currentTimeMillis();
        task.doLongTask();
        CommonUtils.endTime2 = System.currentTimeMillis();
    }
}
// -------------------------------------------------------------------------------

/**
 * 同步代码块的使用
 *
 * 下面的例子展示如何使用同步代码块
 * 但是代码执行的效率 并没有得到的 提升，怎么处理？
 *
 */
class Run0202 {
    public static void main(String[] args) {
        ObjectService02 service = new ObjectService02();
        MyThread0203 t1 = new MyThread0203(service);
        MyThread0204 t2 = new MyThread0204(service);
        t1.setName("a");
        t2.setName("b");
        t1.start();
        t2.start();
    }
}
class ObjectService02 {
    public void serviceMethod() {
        try {
            synchronized (this) {
                System.out.println("begin time = " + System.currentTimeMillis());
                Thread.sleep(2000);
                System.out.println("end time = " + System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class MyThread0203 extends Thread {
    private ObjectService02 service;
    public MyThread0203(ObjectService02 service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.serviceMethod();
    }
}
class MyThread0204 extends Thread {
    private ObjectService02 service;
    public MyThread0204(ObjectService02 service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.serviceMethod();
    }
}
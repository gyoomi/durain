/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * suspend()和resume()的缺点---独占
 * 如果以上两者使用不当，极易造成公共的对象被独占，造成其他线程的无法访问。
 *
 * 结果如下：
 *     begin
 *     a线程永远suspend了
 *     thread2启动了，但是object的toString方法()
 *
 * @author Leon
 * @version 2018/8/8 21:44
 */
public class ThreadDemo20 {

    public static void main(String[] args) throws InterruptedException {
        final SynchronizedObject02 object = new SynchronizedObject02();
        Thread t1 = new Thread() {
            @Override
            public void run() {
                object.printString();
            }
        };
        t1.setName("a");
        t1.start();
        Thread.sleep(2000);

        Thread t2 = new Thread() {
            @Override
            public void run() {
                System.out.println("thread2启动了，但是object的toString方法()");
                object.printString();
            }
        };
        t2.start();
    }
}

class SynchronizedObject02 {

    public synchronized void printString() {
        System.out.println("begin");
        if (Thread.currentThread().getName().equals("a")) {
            System.out.println("a线程永远suspend了");
            Thread.currentThread().suspend();
        }
        System.out.println("end");
    }
}

// ----------------------------------------------------------
/**
 *
 */
class Run20 {

    public static void main(String[] args) {
        try {
            MyThread21 thread = new MyThread21();
            thread.start();
            Thread.sleep(1000);
            thread.suspend();
            System.out.println("Main end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyThread21 extends Thread {

    private long i = 0;

    @Override
    public void run() {
        while (true) {
            i++;
            // println方法是线程安全的，注意区分
            // 线程独占，并且锁并未释放，从而导致main方法中的打印也迟迟不能执行
            System.out.println(i);
        }
    }
}

// ---------------------------------------------------------

/**
 * suspend()和resume()的方法的缺点---不同步
 * 在使用以上两者也很重导致以上两者因为线程的暂停而导致数据的不同步
 * 解决此问题：在后面的章节将会介绍
 *
 */
class Run2001 {

    public static void main(String[] args) throws Exception {
        final MyObject object = new MyObject();
        Thread t1 = new Thread() {
            @Override
            public void run() {
                object.setValue("1", "11");
            }
        };
        t1.setName("a");
        t1.start();
        Thread.sleep(1000);

        Thread t2 = new Thread() {
            @Override
            public void run() {
                object.printMyObject();
            }
        };
        t2.start();
    }
}

class MyObject {
    private String username = "a";
    private String password = "aa";

    public void setValue(String u, String p) {
        this.username = u;
        if (Thread.currentThread().getName().equals("a")) {
            System.out.println("停止了a线程");
            Thread.currentThread().suspend();
        }
        this.password = p;
    }

    public void printMyObject() {
        System.out.println(username + ":" + password);
    }
}

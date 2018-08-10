/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.synchronize;

/**
 * 细化验证3个结论
 *
 * "synchronized(非this对象x)"格式的写法，是将x本身作为对象监视器，这样就得出三个结论：
 *     1.当多个线程执行synchronized(x)时，同步代码块呈现同步效果；
 *     2.当其他线程执行x对象的中的synchronized方法时，呈现同步效果；
 *     3.当其他线程执行x对象方法里面的synchronized(this)代码块时也呈现同步效果。
 *     注意：
 *         如果其他线程调用不加synchronized关键字的方法时，还是会异步调用。
 *
 *  验证第一个结论：
 *
 * @author Leon
 * @version 2018/8/10 17:05
 */
public class ThreadDemo03 {
    public static void main(String[] args) {
        MyObject03 object = new MyObject03();
        // MyObject03 object2 = new MyObject03();
        MyService0301 service = new MyService0301();
        MyThread0301 t1 = new MyThread0301(object, service);
        MyThread0302 t2 = new MyThread0302(object, service);
        t1.setName("AAA");
        t2.setName("BBB");
        t1.start();
        t2.start();
    }
}
class MyObject03 {
}
class MyService0301 {
    public void testMethod1(MyObject03 object) {
        synchronized (object) {
            try {
                System.out.println("testMethod1---> getLock time = " + System.currentTimeMillis() + " run...thread = " + Thread.currentThread().getName());
                Thread.sleep(3000);
                System.out.println("testMethod1---> releaseLock time = " + System.currentTimeMillis() + " run...thread = " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class MyThread0301 extends Thread {
    private MyObject03 object;
    private MyService0301 service;
    public MyThread0301(MyObject03 object, MyService0301 service) {
        this.object = object;
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.testMethod1(object);
    }
}
class MyThread0302 extends Thread {
    private MyObject03 object;
    private MyService0301 service;
    public MyThread0302(MyObject03 object, MyService0301 service) {
        this.object = object;
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.testMethod1(object);
    }
}
// --------------------------------------------------------------

/**
 * 验证第二个结论：
 *     当其他线程执行x对象的中的synchronized方法时，呈现同步效果；
 */
class Run0301 {
    public static void main(String[] args) {
        MyObject0302 object = new MyObject0302();
        MyService0303 service = new MyService0303();
        MyThread0303 t1 = new MyThread0303(object, service);
        MyThread0304 t2 = new MyThread0304(object);
        t1.setName("AAAA");
        t2.setName("BBBB");
        t2.start();
        t1.start();
    }
}
class MyObject0302 {
    public synchronized void speedPrintString() {
        System.out.println("speedPrintString  getLock time = " + System.currentTimeMillis() + " thread = " + Thread.currentThread().getName());
        System.out.println("===================================");
        System.out.println("speedPrintString  releaseLock time = " + System.currentTimeMillis() + " thread = " + Thread.currentThread().getName());
    }
}
class MyService0303 {
    public void testMethod1(MyObject0302 object) {
        try {
            synchronized (object) {
                System.out.println("testMethod1  getLock time = " + System.currentTimeMillis() + " thread = " + Thread.currentThread().getName());
                Thread.sleep(3500);
                System.out.println("testMethod1  releaseLock time = " + System.currentTimeMillis() + " thread = " + Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class MyThread0303 extends Thread {
    private MyObject0302 object;
    private MyService0303 service;
    public MyThread0303(MyObject0302 object, MyService0303 service) {
        this.object = object;
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.testMethod1(object);
    }
}
class MyThread0304 extends Thread {
    private MyObject0302 object;

    public MyThread0304(MyObject0302 object) {
        this.object = object;
    }

    @Override
    public void run() {
        super.run();
        object.speedPrintString();
    }
}
// --------------------------------------------------------
/**
 * 验证第三个结论：
 *
 */
class Run0302 {
    public static void main(String[] args) {
        // 将上述MyObject0302中的同步方法改成sync(this)  结果是代码同样第同步执行的
    }
}
// ------------------------------------------------------------
/**
 * 静态同步synchronized方法与synchronized(class)代码块
 *
 */
class Run0303 {
    public static void main(String[] args) {

    }
}

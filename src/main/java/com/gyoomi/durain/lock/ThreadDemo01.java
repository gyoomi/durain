/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1. ReentrantLock的使用
 *    说明：
 *        1.5之前可以通过synchronized实现同步。1.5后添加了ReentrantLock也可以实现同步，而且功能更强大，
 *        支持嗅探锁定、多路分支通知等，使用起来更加灵活。
 * 1.1 使用ReentrantLock实现同步：测试1
 *     示例：
 *         如下。
 *
 *
 * @author Leon
 * @version 2018/8/16 11:37
 */
public class ThreadDemo01 {
    public static void main(String[] args) {
        MyService0101 service = new MyService0101();
        Thread0101 t1 = new Thread0101(service);
        Thread0101 t2 = new Thread0101(service);
        Thread0101 t3 = new Thread0101(service);
        Thread0101 t4 = new Thread0101(service);
        Thread0101 t5 = new Thread0101(service);
        t1.start();
        t2.start();
        t4.start();
        t3.start();
        t5.start();
    }
}
class MyService0101 {
    private ReentrantLock lock = new ReentrantLock();
    public void testMethod() {
        lock.lock();
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println("ThreadName = " + Thread.currentThread().getName() + " " + (i + 1));
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();
    }
}
class Thread0101 extends Thread {
    private MyService0101 service;
    public Thread0101(MyService0101 service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.testMethod();
    }
}
// ------------------------------------------
/**
 * 1.2 使用ReentrantLock实现同步：测试2
 *     示例：
 *         如下。
 *     结论：
 *         证明了使用lock.lock()方法，就等于持有对象监视器，其他对象就只有等待线程锁被释放后再次抢夺。
 *         顺序执行。
 */
class Run0101 {
    public static void main(String[] args) {
        MyService0102 service = new MyService0102();
        Thread0102A t1 = new Thread0102A(service);
        Thread0103AA t2 = new Thread0103AA(service);
        Thread0103B t3 = new Thread0103B(service);
        Thread0103BB t4 = new Thread0103BB(service);
        t1.setName("A");
        t2.setName("AA");
        t3.setName("B");
        t4.setName("BB");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
class MyService0102 {
    private ReentrantLock lock = new ReentrantLock();
    public void methodA() {
        try {
            lock.lock();
            System.out.println("ThreadName = " + Thread.currentThread().getName() + " start methodA time = " + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println("ThreadName = " + Thread.currentThread().getName() + " end   methodA time = " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void methodB() {
        try {
            lock.lock();
            System.out.println("ThreadName = " + Thread.currentThread().getName() + " start methodB time = " + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println("ThreadName = " + Thread.currentThread().getName() + " end   methodB time = " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
class Thread0102A extends Thread {
    private MyService0102 service;
    public Thread0102A(MyService0102 service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.methodA();
    }
}
class Thread0103AA extends Thread {
    private MyService0102 service;
    public Thread0103AA(MyService0102 service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.methodA();
    }
}
class Thread0103B extends Thread {
    private MyService0102 service;
    public Thread0103B(MyService0102 service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.methodB();
    }
}
class Thread0103BB extends Thread {
    private MyService0102 service;
    public Thread0103BB(MyService0102 service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.methodB();
    }
}
// -----------------------------------------
/**
 * 1.3 使用Condition实现等待/通知：错误用法和解决
 *     说明：
 *         1）Condition可以实现多路通知。也就是在一个Lock对象里面可以创建多个Condition(即对象监视器)的实例，线程对象可以注册到指定的Condition中，
 *         从而可以有选择性的进行线程通知，在线程的调度上更加灵活。
 *         2）在使用notify或notifyAll进行通知时，被通知的线程是由jvm随机选择决定的。但是使用ReentrantLock配合Condition类可以实现由选择性的通知。这也是其最重要的功能。
 *         3）synchronized类似于全局就只有一个单一的condition对象，所有的线程都注册在它身上。
 *     示例：
 *         现象：抛出IllegalMonitorStateException异常
 *     解决：
 *         在使用await()之前必须调用lock()方法，获取对象锁。
 *         修改后的代码如下。
 */
class Run0102 {
    public static void main(String[] args) {
        MyService0103 service = new MyService0103();
        Thread0104 t = new Thread0104(service);
        t.start();
    }
}
class MyService0103 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void testAwait() {
        try {
            lock.lock();
            System.out.println("A");
            condition.await();
            System.out.println("B");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println("锁释放了");
        }
    }
}
class Thread0104 extends Thread {
    private MyService0103 service;
    public Thread0104(MyService0103 service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.testAwait();
    }
}
// ------------------------------------------
/**
 * 1.4 正确使用Condition实现等待/通知
 *     示例：
 *         如下。
 *
 *
 *
 *
 */
class Run0103 {
    public static void main(String[] args) throws InterruptedException {
        MyService0104 service = new MyService0104();
        Thread0105 t = new Thread0105(service);
        t.start();
        Thread.sleep(3000);
        service.testSignal();
    }
}
class MyService0104 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    public void testAwait() {
        try {
            lock.lock();
            System.out.println("await的时间为：" + System.currentTimeMillis());
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void testSignal() {
        try {
            lock.lock();
            System.out.println("signal的时间为：" + System.currentTimeMillis());
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
class Thread0105 extends Thread {
    private MyService0104 service;
    public Thread0105(MyService0104 service) {
        this.service = service;
    }
    @Override
    public void run() {
        service.testAwait();
    }
}
// -----------------------------------------
/**
 * 1.5 使用多个Condition实现通知部门线程：错误用法
 *
 */
class Run0104 {
    public static void main(String[] args) {

    }
}



























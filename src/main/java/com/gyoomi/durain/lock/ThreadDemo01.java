/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.lock;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
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
 *     一个condition就类似于sync
 *     那多个condition怎么处理
 */
class Run0104 {
    public static void main(String[] args) throws InterruptedException {
        MyService0105 service = new MyService0105();
        Thread0106 t1 = new Thread0106(service);
        Thread0107 t2 = new Thread0107(service);
        t1.start();
        t2.start();
        Thread.sleep(3000);
        service.signAll();
    }
}
class MyService0105 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    public void awaitA() {
        try {
            lock.lock();
            System.out.println("begin awaitA at = " + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
            condition.await();
            System.out.println("end   awaitA at = " + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void awaitB() {
        try {
            lock.lock();
            System.out.println("begin awaitB at = " + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
            condition.await();
            System.out.println("end   awaitB at = " + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void signAll() {
        try {
            lock.lock();
            System.out.println("signAll all at = " + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
class Thread0106 extends Thread {
    private MyService0105 service;
    public Thread0106(MyService0105 service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.awaitA();
    }
}
class Thread0107 extends Thread {
    private MyService0105 service;
    public Thread0107(MyService0105 service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.awaitB();
    }
}
// -----------------------------------------
/**
 *  1.6 使用多个condition实现通知部分线程：正确用法
 *      结论：
 *          使用ReentrantLock可以根据不同的种类，唤醒不同类的线程。
 *
 */
class Run0105 {
    public static void main(String[] args) throws InterruptedException {
        MyService0106 service = new MyService0106();
        Thread0108 t1 = new Thread0108(service);
        Thread0109 t2 = new Thread0109(service);
        t1.start();
        t2.start();
        Thread.sleep(3000);
        service.signall_B();
    }
}
class MyService0106 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();
    public void awaitA() {
        try {
            lock.lock();
            System.out.println("begin awaitA time = " + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
            conditionA.await();
            System.out.println("end   awaitA time = " + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void awaitB() {
        try {
            lock.lock();
            System.out.println("begin awaitB time = " + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
            conditionB.await();
            System.out.println("end   awaitB time = " + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void signall_A() {
        try {
            lock.lock();
            System.out.println("signall_A time = " + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
            conditionA.signalAll();
        } finally {
            lock.unlock();
        }
    }
    public void signall_B() {
        try {
            lock.lock();
            System.out.println("signall_B time = " + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
            conditionB.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
class Thread0108 extends Thread {
    private MyService0106 service;
    public Thread0108(MyService0106 service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.awaitA();
    }
}
class Thread0109 extends Thread {
    private MyService0106 service;
    public Thread0109(MyService0106 service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.awaitB();
    }
}
// ----------------------------------------
/**
 * 1.7 实现生产者/消费者：一对一交替打印
 *
 *
 */
class Run0106 {
    public static void main(String[] args) {
        MyService0107 service = new MyService0107();
        Thread0110 t1 = new Thread0110(service);
        Thread0111 t2 = new Thread0111(service);
        t1.start();
        t2.start();
    }
}
class MyService0107 extends Thread {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private boolean hasSetValue = false;
    public void set() {
        try {
            lock.lock();
            if (hasSetValue == true) {
                condition.await();
            }
            System.out.println("set★");
            hasSetValue = true;
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void get() {
        try {
            lock.lock();
            if (hasSetValue == false) {
                condition.await();
            }
            System.out.println("get☆");
            hasSetValue = false;
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
class Thread0110 extends Thread {
    private MyService0107 service;
    public Thread0110(MyService0107 service) {
        this.service = service;
    }

    @Override
    public void run() {
        while (true) {
            service.set();
        }
    }
}
class Thread0111 extends Thread {
    private MyService0107 service;
    public Thread0111(MyService0107 service) {
        this.service = service;
    }

    @Override
    public void run() {
        while (true) {
            service.get();
        }
    }
}
// ----------------------------------------
/**
 * 1.8 实现生产者/消费者：多对多交替打印
 *     注意if和while的区别
 *     注意理解MyService0108中的代码
 *
 */
class Run0107 {
    public static void main(String[] args) {
        MyService0108 service = new MyService0108();
        Thread0112[] setArr = new Thread0112[10];
        Thread0113[] getArr = new Thread0113[10];
        for (int i = 0; i < 10; i++) {
            setArr[i] = new Thread0112(service);
            setArr[i].start();
            getArr[i] = new Thread0113(service);
            getArr[i].start();
        }
    }
}
class MyService0108 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private boolean hasSetValue = false;

    public void set() {
        try {
            lock.lock();
            while (hasSetValue) {
                System.out.println("有可能连续 ★★");
                condition.await();
            }
            System.out.println("set ★");
            hasSetValue = true;
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void get() {
        try {
            lock.lock();
            while (!hasSetValue) {
                System.out.println("有可能连续 ☆☆");
                condition.await();
            }
            System.out.println("get ☆");
            hasSetValue = false;
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
class Thread0112 extends Thread {
    private MyService0108 service;
    public Thread0112(MyService0108 service) {
        this.service = service;
    }

    @Override
    public void run() {
        while (true) {
            service.set();
        }
    }
}
class Thread0113 extends Thread {
    private MyService0108 service;
    public Thread0113(MyService0108 service) {
        this.service = service;
    }

    @Override
    public void run() {
        while (true) {
            service.get();
        }
    }
}
// --------------------------------------
/**
 * 1.9 公平锁和非公平锁
 *     说明：
 *         锁Lock分为公平锁和非公平锁。所谓公平锁，就是表示线程获取锁的顺序按照线程加锁的顺序来分配的。即先来先得的FIFO的先进先出的顺序来的。
 *         而非公平锁是一种获取锁的抢占机制，是随机获取锁的，这一点和公平锁不一样。
 *
 *     示例：
 *          可以看出公平锁的启动时间和获取锁的时间基本上一致的。基本上有序的。
 *     示例2：基本乱序。可以看出。
 */
class Run0108 {
    public static void main(String[] args) {
        final MyService0109 service = new MyService0109(true);
        Runnable runnable = new Runnable(){
            public void run() {
                System.out.println("ThreadName = " + Thread.currentThread().getName() + " 运行了");
                service.seviceMethod();
            }
        };
        Thread[] arr = new Thread[10];
        for (int i = 0; i < 10; i++) {
            arr[i] = new Thread(runnable);
            arr[i].start();
        }
    }
}
class Run0109 {
    public static void main(String[] args) {
        final MyService0109 service = new MyService0109(false);
        Runnable runnable = new Runnable(){
            public void run() {
                System.out.println("ThreadName = " + Thread.currentThread().getName() + " 运行了");
                service.seviceMethod();
            }
        };
        Thread[] arr = new Thread[10];
        for (int i = 0; i < 10; i++) {
            arr[i] = new Thread(runnable);
            arr[i].start();
        }
    }
}
class MyService0109 {
    private ReentrantLock lock;
    public MyService0109(boolean isFair) {
        lock = new ReentrantLock(isFair);
    }
    public void seviceMethod() {
        try {
            lock.lock();
            System.out.println("ThreadName = " + Thread.currentThread().getName() + " 获得了锁定");
        } finally {
            lock.unlock();
        }
    }
}
// -------------------------------------
/**
 * 1.10 方法getHoldCount()、getQueueLength()、getWaitQueueLength()方法测试
 *      1）getHoldCount()作用是查询当前线程保持此锁定的个数，也就是调用lock()方法的次数
 *      2）getQueueLength()作用是返回正等待获取此锁定线程的估计数，比如说有5个线程，一个线程首先执行await(),
 *         那么调用getQueueLength就会返回4.说明有四个线程正在同时等待lock锁的释放
 *      3)int getWaitQueueLength(Condition)作用是返回等待和此锁定相关给定条件Condition的线程估计数。
 *         比如说有5个线程，每个线程都执行了同一个condition对象的await()方法，此时调用此方法的返回值将是5.
 *
 *
 */
class Run0110 {
    public static void main(String[] args) {
        MyService0110 service = new MyService0110();
        service.serviceMethod2();
    }
}
class MyService0110 {
    private ReentrantLock lock = new ReentrantLock();
    public void serviceMethod1() {
        try {
            lock.lock();
            System.out.println("serviceMethod1 getHoldCount = " + lock.getHoldCount());
            serviceMethod2();
        } finally {
            lock.unlock();
        }
    }
    public void serviceMethod2() {
        try {
            lock.lock();
            System.out.println("serviceMethod2 getHoldCount = " + lock.getHoldCount());
        } finally {
            lock.unlock();
        }
    }
}
class Run0111 {
    public static void main(String[] args) throws InterruptedException {
        final MyService0111 service = new MyService0111();
        Runnable runnable = new Runnable(){
            public void run() {
                service.serviceMethod1();
            }
        };
        Thread[] arr = new Thread[10];
        for (int i = 0; i < 10; i++) {
            arr[i] = new Thread(runnable);
        }
        for (int i = 0; i < 10; i++) {
            arr[i].start();
        }
        Thread.sleep(2000);
        System.out.println("等待获取锁的线程数是： " + service.lock.getQueueLength());
    }
}
class MyService0111 {
    public ReentrantLock lock = new ReentrantLock();
    public void serviceMethod1() {
        try {
            lock.lock();
            System.out.println("ThreadName = " + Thread.currentThread().getName() + " 进入了方法！");
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
class Run0112 {
    public static void main(String[] args) throws InterruptedException {
        final MyService0112 service = new MyService0112();
        Runnable runnable = () -> service.awaitMethod();
        Thread[] threadArr = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threadArr[i] = new Thread(runnable);
        }
        for (int i = 0; i < 10; i++) {
            threadArr[i].start();
        }
        Thread.sleep(2000);
        service.signalMethod();
        service.signalMethod();
    }
}
class MyService0112 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    public void awaitMethod() {
        try {
            lock.lock();
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void signalMethod() {
        try {
            lock.lock();
            System.out.println("有" + lock.getWaitQueueLength(condition) + "个线程正在等待condition对象");
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
// ------------------------------------
/**
 * 1.11 hasQueuedThread()、hasQueuedThreads()和hasWaiters()测试
 *      说明：
 *          1）boolean hasQueuedThread(Thread)查询指定线程是否在等待获取此锁定？
 *          2）boolean hasQueuedThreads()查询是否有线程等待获取此锁定
 *      结果：
 *          false
 *          true
 *          true
 *
 *          3）boolean hasWaiters(Condition)作用是查询是否有线程正在等待与此锁定有关的condition条件
 */
class Run0113 {
    public static void main(String[] args) throws InterruptedException {
        final MyService0113 service = new MyService0113();
        Runnable runnable = () -> service.waitMethod();
        Thread a = new Thread(runnable);
        a.start();
        Thread.sleep(500);
        Thread b = new Thread(runnable);
        b.start();
        Thread.sleep(500);
        System.out.println(service.lock.hasQueuedThread(a));
        System.out.println(service.lock.hasQueuedThread(b));
        System.out.println(service.lock.hasQueuedThreads());
    }
}
class MyService0113 {
    public ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    public void waitMethod() {
        try {
            lock.lock();
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
class Run0114 {
    public static void main(String[] args) throws InterruptedException {
        final MyService0114 service = new MyService0114();
        Runnable runnable = () -> service.waitMethod();
        Thread[] arr = new Thread[10];
        for (int i = 0; i < 10; i++) {
            arr[i] = new Thread(runnable);
        }
        for (int i = 0; i < 10; i++) {
            arr[i].start();
        }
        Thread.sleep(2000);
        service.signalMethod();
    }
}
class MyService0114 {
    public ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    public void waitMethod() {
        try {
            lock.lock();
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void signalMethod() {
        try {
            lock.lock();
            System.out.println("有没有线程在等待condition？" + lock.hasWaiters(condition) + " 线城数是多少" + lock.getWaitQueueLength(condition));
            condition.signal();
        } finally {
            lock.unlock();
        }

    }
}
// ----------------------------------
/**
 * 1.12 方法isFair()、isHeldByCurrentThread()和isLocked测试
 *      1）isFair()判断是不是公平锁
 *         默认情况下：ReentrantLock是非公平锁
 *      2)boolean isHeldByCurrentThread()作用查询当前线程是否保持锁定。
 *      3)boolean isLocked()查询此锁定是否被任意线程所保持
 *
 */
class Run0115 {
    public static void main(String[] args) {
        final MyService0115 service1 = new MyService0115(true);
        Runnable runnable = () -> service1.serviceMethod();
        Thread a = new Thread(runnable);
        a.start();
        final MyService0115 service2 = new MyService0115(false);
        Runnable runnable2 = () -> service2.serviceMethod();
        Thread b = new Thread(runnable2);
        b.start();
    }
}
class MyService0115 {
    private ReentrantLock lock;
    public MyService0115(boolean isFair) {
        lock = new ReentrantLock(isFair);
    }
    public void serviceMethod() {
        try {
            lock.lock();
            System.out.println("公平锁情况：" + lock.isFair());
        } finally {
            lock.unlock();
        }
    }
}
class Run0116 {
    public static void main(String[] args) {
        MyService0116 service = new MyService0116(false);
        Runnable runnable = () -> service.serviceMethod();
        Thread t = new Thread(runnable);
        t.start();
    }
}
class MyService0116 {
    private ReentrantLock lock;
    public MyService0116(boolean fair) {
        lock = new ReentrantLock(fair);
    }
    public void serviceMethod() {
        try {
            System.out.println(lock.isLocked());
            lock.lock();
            System.out.println(lock.isLocked());
        } finally {
            lock.unlock();
        }
    }
}
// ---------------------------------
/**
 * 1.13 方法lockInterruptibly()、tryLock()和tryLock(long timeout, TimeUnit unit)的测试
 *      1)lockInterruptibly()的作用是：如果当前线程未被中断，则获取锁定；如果已被中断，则抛出异常。
 *      2)boolean tryLock():仅在调用时锁定未被另一个线程保持的情况下，才获取该锁定。
 *        Acquires the lock only if it is not held by another thread at the time of invocation
 *      3）tryLock(long timeout, TimeUnit unit)
 *         如果锁定在给定时间内，没有被另外的线程保持，且当年线程并未中断，则获取该锁定。
 *
 */
class Run0117 {
    public static void main(String[] args) throws InterruptedException {
        MyService0117 service = new MyService0117();
        Runnable runnable = () -> service.serviceMethod();
        Thread t1 = new Thread(runnable);
        t1.setName("A");
        t1.start();
        Thread.sleep(500);
        Thread t2 = new Thread(runnable);
        t2.setName("B");
        t2.start();
        // t2.interrupt(); // 打标记
        t2.interrupt(); // 此时便会抛出异常
        System.out.println("main end");
    }
}
class MyService0117 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    public void serviceMethod() {
        try {
            lock.lockInterruptibly();
            System.out.println("lock begin threadName " + Thread.currentThread().getName());
            for (int i = 0; i < Integer.MAX_VALUE / 10; i++) {
                Math.random();
            }
            System.out.println("lock end   threadName " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
class Run0118 {
    public static void main(String[] args) {
        final MyService0118 service = new MyService0118();
        Runnable runnable = () -> service.testMethod();
        Thread t1 = new Thread(runnable);
        t1.setName("AAA");
        t1.start();
        Thread t2 = new Thread(runnable);
        t2.setName("BBB");
        t2.start();
    }
}
class MyService0118 {
    private ReentrantLock lock = new ReentrantLock();
    public void testMethod() {
        if (lock.tryLock()) {
            System.out.println(Thread.currentThread().getName() + "获得锁");
        } else {
            System.out.println(Thread.currentThread().getName() + "没有获得锁");
        }
    }
}
class Run0119 {
    public static void main(String[] args) {
        final MyService0119 service = new MyService0119();
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName() + "调用了testMethod方法 time = " + System.currentTimeMillis());
            service.testMethod();
        };
        Thread t1 = new Thread(runnable);
        t1.setName("AAA");
        t1.start();
        Thread t2 = new Thread(runnable);
        t2.setName("BBB");
        t2.start();
    }
}
class MyService0119 {
    private ReentrantLock lock = new ReentrantLock();
    public void testMethod() {
        try {
            if (lock.tryLock(3, TimeUnit.SECONDS)) {
                System.out.println("time = " + System.currentTimeMillis() + " " + Thread.currentThread().getName() + "获取了锁");
            } else  {
                System.out.println("time = " + System.currentTimeMillis() + " " + Thread.currentThread().getName() + "没有获取了锁");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
// ---------------------------------
/**
 * 1.14 方法awaitUninterruptibly()的使用
 *      现象描述：
 *      使用condition.await(),如果线程在执行的过程的被interrupt的话，会抛出异常
 *      而使用condition.awaitUninterruptibly()则会正常运行，不会抛出异常。
 */
class Run0120 {
    public static void main(String[] args) throws InterruptedException {
        final MyService0120 service = new MyService0120();
        Runnable runnable = () -> service.testMethod();
        Thread t = new Thread(runnable);
        t.start();
        Thread.sleep(2_000);
        t.interrupt();
    }
}
class MyService0120 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    public void testMethod() {
        try {
            lock.lock();
            System.out.println("wait begin");
            // condition.awaitUninterruptibly();
            condition.await();
            System.out.println("wait  end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
// --------------------------------
/**
 * 1.15 方法awaitUntil()方法的使用
 *      awaitUntil(Date) 到时间会自动醒来。
 *      如果在此时间之前被signAll.线程也会醒来。
 *
 */
class Run0121 {
    public static void main(String[] args) throws InterruptedException {
        MyService0121 service = new MyService0121();
        Runnable runnable = () -> service.waitMethod();
        Thread t1 = new Thread(runnable);
        t1.start();
        Runnable runnable2 = () -> service.notifyMethod();
        Thread t2 = new Thread(runnable2);
        Thread.sleep(2000);
        t2.start();
    }
}
class MyService0121 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    public void waitMethod() {
        try {
            Calendar now = Calendar.getInstance();
            now.add(Calendar.SECOND, 10);
            lock.lock();
            System.out.println("wait begin time = " + System.currentTimeMillis());
            condition.awaitUntil(now.getTime());
            System.out.println("wait end   time = " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void notifyMethod() {
        try {
            lock.lock();
            System.out.println("notify begin time = " + System.currentTimeMillis());
            condition.signalAll();
            System.out.println("notify   end time = " + System.currentTimeMillis());
        } finally {
            lock.unlock();
        }
    }
}






























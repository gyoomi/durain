/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.communicate;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 线程间通信
 *
 * 1.等待/通知机制
 *
 * 1.1不使用等待/通知机制实现线程间的通信
 *    说明：
 *        使用sleep()和while(true)不断轮训来实现两个线程间的通信
 * @author Leon
 * @version 2018/8/13 10:08
 */
public class ThreadDemo01 {
    public static void main(String[] args) {
        MyList01 list = new MyList01();
        Thread0101 t1 = new Thread0101(list);
        Thread0102 t2 = new Thread0102(list);
        t1.setName("AAA");
        t2.setName("BBB");
        t1.start();
        t2.start();
    }
}
class MyList01 {
    private List list = new ArrayList();

    public void add() {
        list.add("gyoomi");
    }
    public int size() {
        return this.list.size();
    }
}
class Thread0101 extends Thread {
    private MyList01 list;
    public Thread0101(MyList01 list) {
        this.list = list;
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                list.add();
                System.out.println("list添加了" + (i + 1) + "个元素");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Thread0102 extends Thread {
    private MyList01 list;
    public Thread0102(MyList01 list) {
        this.list = list;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // System.out.println("b-> size = " + list.size());
                if (list.size() == 5) {
                    System.out.println("==5了，线程b要退出了");
                    throw new InterruptedException();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
// ------------------------------------------------------

/**
 * 1.2什么是等待/通知
 *    说明：
 *        厨师和服务员的关系
 */
class Run0101 {
}

// -----------------------------------------------------

/**
 * 1.3等待/通知机制的实现
 *    说明：
 *        wait():
 *              1）将执行当前代码的线程进行等待。将当前线程置入“预执行队列之中”，并在wait（）所在代码处停止，直到接到通知或停止。
 *              2）调用该方法必须获得锁，即只能在sync方法或块中执行。如果没有锁调则会抛出异常
 *              3）在执行wait()方法后，在从wait()返回前，线程和其他线程竞争重新获得锁。
 *        notify():
 *             1）：同上面2点。
 *             2）：如果有多个线程处于wait状态，则由线程规划器随机挑选出一个wait状态的线程，对其发出notify通知，并使他等待获取该对象的对象锁。
 *                  需要说明的是：执行了notify()之后，当前线程不会立刻释放锁，wait的线程也不会立马获得锁；而是等notify()方法的线程执行完代码（即退出sync块），当前线程才会释放锁。
 *             3）：当第一个获取该对象锁的wait线程运行完毕后，如果没有调用notify()方法，其他处理的wait状态的线程，仍然会一直处理阻塞在wait处于等待状态，但是当前该对象锁却是空闲状态。
 *    总结：
 *        wait使线程停止运行，notify使停止的线程继续运行。
 *
 *    实例：notify和wait的基本用法
 *    结果：
 *        wait start time = 1534139676822
 *        notify start time = 1534139676872
 *        notify end   time = 1534139676872
 *        wait end   time = 1534139676872
 *
 */
class Run0102 {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        MyThread0103 t1 = new MyThread0103(lock);
        MyThread0104 t2 = new MyThread0104(lock);
        t1.start();
        Thread.sleep(50);
        t2.start();
    }
}
class MyThread0103 extends Thread {
    private Object lock;
    public MyThread0103(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                System.out.println("wait start time = " + System.currentTimeMillis());
                lock.wait();
                System.out.println("wait end   time = " + System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class MyThread0104 extends Thread {
    private Object lock;
    public MyThread0104(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            System.out.println("notify start time = " + System.currentTimeMillis());
            lock.notify();
            System.out.println("notify end   time = " + System.currentTimeMillis());
        }
    }
}
// --------------------------------------

/**
 * 实例：
 *    利用wait和notify来解决上面的问题
 *    代码如下。
 *
 *    结果：
 *        Thread A wait start at 1534140480578
 *        Thread B 添加了1个元素
 *        Thread B 添加了2个元素
 *        Thread B 添加了3个元素
 *        Thread B 添加了4个元素
 *        Thread B 发出通知了
 *        Thread B 添加了5个元素
 *        Thread B 添加了6个元素
 *        Thread B 添加了7个元素
 *        Thread B 添加了8个元素
 *        Thread B 添加了9个元素
 *        Thread B 添加了10个元素
 *        Thread A wait end   at 1534140491079
 *   结论：
 *       1）notify不会立刻释放锁，而是等代码执行完后才会释放；
 *       2）如果发出notify(),没有处于wait状态的线程，该命令将会被忽略
 *       3）notify()是唤醒一个，notifyAll()是唤醒所有
 *
 *   线程的几大阶段：
 *      1）new
 *      2) runnable 和 running (调用sleep超过指定时间、阻塞io调用完毕，方法返回、成功获得对象监视器、正在等待通知，而其他线程通知了、调用了resume)
 *      3) block (sleep方法、阻塞io、等待锁、被wait、suspend)
 *      4) dead
 *
 *      每个锁对象都有两个队列：
 *         1.阻塞队列（只能被唤醒，放入到就绪队列）
 *         2.就绪丢了
 *
 */
class Run0103 {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        MyThread0105 t1 = new MyThread0105(lock);
        MyThread0106 t2 = new MyThread0106(lock);
        t1.start();
        Thread.sleep(500);
        t2.start();
    }
}
class MyList02 {
    private static List list = new ArrayList();
    public static void add() {
        list.add("one element");
    }
    public static int size() {
        return list.size();
    }

}
class MyThread0105 extends Thread {
    private Object lock;
    public MyThread0105(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                System.out.println("Thread A wait start at " + System.currentTimeMillis());
                lock.wait();
                System.out.println("Thread A wait end   at " + System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class MyThread0106 extends Thread {
    private Object lock;
    public MyThread0106(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    MyList02.add();
                    if (MyList02.size() == 5) {
                        lock.notify();
                        System.out.println("Thread B 发出通知了");
                    }
                    System.out.println("Thread B 添加了" + (i + 1) + "个元素");
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
// --------------------------------------

/**
 * 1.4方法wait()释放锁notify()锁不释放
 *
 * 说明：当执行wait()方法时后，锁会被自动释放，而执行notify()方法，锁则不自动释放
 *      该wait为sleep，则结果为顺序执行（同步效果）。
 *
 *      由此可以得出：
 *      wait释放锁，sleep不释放锁。
 *
 *
 */
class Run0104 {
    public static void main(String[] args) {
        Object lock = new Object();
        MyThread0107 t1 = new MyThread0107(lock);
        MyThread0108 t2 = new MyThread0108(lock);
        t1.start();
        t2.start();
    }
}
class MyService01 {
    public void testMethod(Object lock) {
        try {
            synchronized (lock) {
                System.out.println("begin wait");
                // lock.wait();
                Thread.sleep(4000);
                System.out.println("end wait");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class MyThread0107 extends Thread {
    private Object lock;
    public MyThread0107(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        MyService01 service = new MyService01();
        service.testMethod(lock);
    }
}
class MyThread0108 extends Thread {
    private Object lock;
    public MyThread0108(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        MyService01 service = new MyService01();
        service.testMethod(lock);
    }
}
// ----------------------------------------

/**
 * 1.5 当interrupt方法遇到wait方法
 *
 *     说明:
 *         当线程处于wait状态，调用interrupt方法则会抛出InterruptException异常。
 *     实例：
 *
 *     总结：
 *         1.执行完同步代码块就会释放锁
 *         2.在执行同步代码，如噶发生异常，锁也是会 被释放的
 *         3.在执行同步代码块的过程中，如果执行了锁所属对象wait方法，则这个线程则会释放锁，进入阻塞队列等待被唤醒。
 *
 */
class Run0105 {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        MyThread0109  t1 = new MyThread0109(lock);
        t1.start();
        Thread.sleep(4000);
        t1.interrupt();
    }
}
class MyService02 {
    public void testMethod(Object lock) {
        try {
            synchronized (lock) {
                System.out.println("begin wait");
                lock.wait();
                System.out.println("end wait");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("出现异常了，因为线程在wait状态下呗interrupt了");
        }
    }
}
class MyThread0109 extends Thread {
    private Object lock;
    public MyThread0109(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        MyService02 service = new MyService02();
        service.testMethod(lock);
    }
}
// ----------------------------------------------------

/**
 * 1.6 只通知一个线程
 *
 *     说明：调用notify()方法，一次只随机通知一个线程。
 *     补充：如果多次调用notify().则会随机唤醒wait中的线程
 *     实例：
 *         结果：
 *         thread = Thread-0 begin wait
 *         thread = Thread-1 begin wait
 *         thread = Thread-2 begin wait
 *         thread = Thread-0 end   wait
 *
 */
class Run0106 {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        MyThread0110 t1 = new MyThread0110(lock);
        MyThread0110 t2 = new MyThread0110(lock);
        MyThread0110 t3 = new MyThread0110(lock);
        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(2000);
        MyThread0111 t = new MyThread0111(lock);
        t.start();
    }
}
class Service03 {
    public void testMethod(Object lock) {
        try {
            synchronized (lock) {
                System.out.println("thread = " + Thread.currentThread().getName() + " begin wait");
                lock.wait();
                System.out.println("thread = " + Thread.currentThread().getName() + " end   wait");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class MyThread0110 extends Thread {
    private Object lock;
    public MyThread0110(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        Service03 service = new Service03();
        service.testMethod(lock);
    }
}
class MyThread0111 extends Thread {
    private Object lock;
    public MyThread0111(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            lock.notifyAll();
        }
    }
}
// ---------------------------------------------------

/**
 * 1.7 唤醒所有线程
 *     说明：为了唤醒所有线程，可以使用notifyAll().
 */
class Run0107 {
    public static void main(String[] args) {

    }
}
// ---------------------------------------------------

/**
 * 1.9 方法wait(long)使用
 *     说明：等待某一个时间内是否有线程对锁进行唤醒，如果超过时间自动唤醒
 *
 */
class Run0108 {
    public static void main(String[] args) throws InterruptedException {
        final Object lock = new Object();
        Runnable runnable1 = new Runnable() {
            public void run() {
                try {
                    synchronized (lock) {
                        System.out.println("wait begin at " + System.currentTimeMillis());
                        lock.wait(5000);
                        System.out.println("wait begin at " + System.currentTimeMillis());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable runnable2 = new Runnable() {
            public void run() {
                synchronized (lock) {
                    lock.notify();
                }
            }
        };
        Thread t1 = new Thread(runnable1);
        t1.start();
        Thread.sleep(3000);
        Thread t2 = new Thread(runnable2);
        t2.start();
    }
}
// --------------------------------------------------

/**
 * 1.10 通知过早
 *      说明：
 *          如果通知过早，则会打乱程序的运行逻辑。
 *      出现：wait状态的线程则会会一直处于等待中
 *
 */
class Run0109 {
    public static void main(String[] args) throws InterruptedException {
        final Object lock = new Object();
        Runnable r1 = new Runnable(){
            public void run() {
                try {
                    synchronized (lock) {
                        System.out.println("begin wait");
                        lock.wait();
                        System.out.println("end   wait");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable r2 = new Runnable(){
            public void run() {
                synchronized (lock) {
                    System.out.println("notify wait");
                    lock.notify();
                    System.out.println("notify wait");
                }
            }
        };
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        t1.start();
        Thread.sleep(1000);
        t2.start();

    }
}
// -------------------------------------------------

/**
 * 1.11 等待wait的条件发生变化
 *      说明：
 *          使用wait/notify模式时，还需要注意另外一种情况，那就是wait等待的条件发生了变化，也会造成程序逻辑的混乱。
 *      实例：
 *          发生indexOutOfBoundException
 *      解决：把if改成while即可。
 *
 */
class Run0110 {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        Add add = new Add(lock);
        Subtract subtract = new Subtract(lock);
        MyThread0113 t1 = new MyThread0113(subtract);
        MyThread0113 t2 = new MyThread0113(subtract);
        t1.start();
        t2.start();
        Thread.sleep(1000);
        MyThread0112 t = new MyThread0112(add);
        t.start();
    }
}
class ObjectValue {
    public static List list = new ArrayList();
}
class Add {
    private Object lock;
    public Add(Object lock) {
        this.lock = lock;
    }
    public void add() {
        synchronized (lock) {
            ObjectValue.list.add("anything");
            lock.notifyAll();
        }
    }
}
class Subtract {
    private Object lock;
    public Subtract(Object lock) {
        this.lock = lock;
    }
    public void sbutract() {
        try {
            synchronized (lock) {
                while (ObjectValue.list.size() == 0) {
                    System.out.println("thread = " + Thread.currentThread().getName() + " begin wait");
                    lock.wait();
                    System.out.println("thread = " + Thread.currentThread().getName() + " end   wait");
                }
                ObjectValue.list.remove(0);
                System.out.println("list size = " + ObjectValue.list.size());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class MyThread0112 extends Thread {
    private Add add;
    public MyThread0112(Add add) {
        this.add = add;
    }

    @Override
    public void run() {
        add.add();
    }
}
class MyThread0113 extends Thread {
    private Subtract subtract;
    public MyThread0113(Subtract subtract) {
        this.subtract = subtract;
    }

    @Override
    public void run() {
        subtract.sbutract();
    }
}
// ------------------------------------------------

/**
 * 1.12 生产者和消费者模式实现
 *      基于wait/notify原理实现的
 *
 */
class Run0111 {
    public static void main(String[] args) {

    }
}
// ---------------------------------------------

/**
 * 一生产者一消费者：操作者
 *     代码如下:
 *     测试结果：
 *         交替打印
 *
 *
 *
 */
class Run0112 {
    public static void main(String[] args) {
        Object lock = new Object();
        P11 p = new P11(lock);
        C11 c = new C11(lock);
        Thread0114 t1 = new Thread0114(p);
        Thread0115 t2 = new Thread0115(c);
        t1.start();
        t2.start();
    }
}
class ObjectValue11 {
    public static String value = "";
}
class P11 {
    private Object lock;
    public P11(Object lock) {
        this.lock = lock;
    }
    public void setValue() {
        try {
            synchronized (lock) {
                if (!ObjectValue11.value.equals("")) {
                    lock.wait();
                }
                String value = System.currentTimeMillis() + "_" + System.nanoTime();
                System.out.println("setValue = " + value);
                ObjectValue11.value = value;
                lock.notify();

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class C11 {
    private Object lock;
    public C11(Object lock) {
        this.lock = lock;
    }
    public void getValue() {
        try {
            synchronized (lock) {
                if (ObjectValue11.value.equals("")) {
                    lock.wait();
                }
                System.out.println("getValue = " + ObjectValue11.value);
                ObjectValue11.value = "";
                lock.notify();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Thread0114 extends Thread {
    private P11 p;
    public Thread0114(P11 p) {
        super();
        this.p = p;
    }

    @Override
    public void run() {
        while (true) {
            p.setValue();
        }
    }
}
class Thread0115 extends Thread {
    private C11 c;
    public Thread0115(C11 c) {
        super();
        this.c = c;
    }

    @Override
    public void run() {
        while (true) {
            c.getValue();
        }
    }
}
// ----------------------------------------------

/**
 * 多生产者多消费者：操作值 - 假死
 *
 *     说明：
 *         所谓“假死”现象就是线程进入了wait状态。如果所有的线程进入了wait状态，则整个项目则处于停滞。
 *    分析：
 *        出现假死主要使用为出现同类唤醒同类。
 *
 *    解决：
 *        不光唤醒同类，将异类也一起唤醒。
 *  多生产者多消费者：操作值
 *    修改：
 *    将notify()改为notifyAll()即可。
 *
 *
 */
class Run0113 {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        P12 p = new P12(lock);
        C12 c = new C12(lock);
        Thread0116[] pArr = new Thread0116[2];
        Thread0117[] cArr = new Thread0117[2];
        for (int i = 0; i < 2; i++) {
            pArr[i] = new Thread0116(p);
            cArr[i] = new Thread0117(c);
            pArr[i].setName("生产者" + (i + 1));
            cArr[i].setName("消费者" + (i + 1));
            pArr[i].start();
            cArr[i].start();
        }
        Thread.sleep(5000);
        System.out.println("---------------------------------------------------------");
        Thread[] ts = new Thread[Thread.currentThread().getThreadGroup().activeCount()];
        Thread.currentThread().getThreadGroup().enumerate(ts);
        for (int i = 0; i < ts.length; i++) {
            System.out.println(ts[i].getName() + ":" + ts[i].getState());
        }
    }
}
class ObjectValue12 {
    public static String value = "";
}
class P12 {
    private Object lock;
    public P12(Object lock) {
        this.lock = lock;
    }
    public void setValue() {
        try {
            synchronized (lock) {
                while (!ObjectValue12.value.equals("")) {
                    System.out.println("生产者" + Thread.currentThread().getName() + "wait...");
                    lock.wait();
                }
                System.out.println("生产者" + Thread.currentThread().getName() + "running...");
                String value = System.currentTimeMillis() + "_" + System.nanoTime();
                ObjectValue12.value = value;
                lock.notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class C12 {
    private Object lock;
    public C12(Object lock) {
        this.lock = lock;
    }
    public void getValue() {
        try {
            synchronized (lock) {
                while (ObjectValue12.value.equals("")) {
                    System.out.println("消费者" + Thread.currentThread().getName() + "wait...");
                    lock.wait();
                }
                System.out.println("消费者" + Thread.currentThread().getName() + "running...");
                ObjectValue12.value = "";
                lock.notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Thread0116 extends Thread {
    private P12 p;
    public Thread0116(P12 p) {
        this.p = p;
    }

    @Override
    public void run() {
        while (true) {
            p.setValue();
        }
    }
}
class Thread0117 extends Thread {
    private C12 c;
    public Thread0117(C12 c) {
        this.c = c;
    }

    @Override
    public void run() {
        while (true) {
            c.getValue();
        }
    }
}
// ------------------------------------------
/**
 * 一生产者与一消费者：操作栈
 *     说明：
 *         使用生产者想堆栈List放入元素，使用消费者从List中取出元素。List的容量只有1。一个生产者一个消费者。
 *
 */
class Run0114 {
    public static void main(String[] args) {
        MyStack stack = new MyStack();
        P13 p = new P13(stack);
        C13 c = new C13(stack);
        Thread0118 t1 = new Thread0118(p);
        Thread0119 t2 = new Thread0119(c);
        t1.start();
        t2.start();
    }
}
class MyStack {
    private List list = new ArrayList();
    public synchronized void push() {
        try {
            while (list.size() == 1) {
                this.wait();
            }
            System.out.println("thread = " + Thread.currentThread().getName() + "生产。。。");
            list.add("any" + Math.random());
            this.notifyAll();
            System.out.println("push = " + list.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public synchronized String pop() {
        String value = "";
        try {
            while (list.size() == 0) {
                this.wait();
            }
            value = "" + list.get(0);
            list.remove(0);
            this.notifyAll();
            System.out.println("thread = " + Thread.currentThread().getName() + "消费。。。");
            System.out.println("pop = " + list.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return value;
    }
}
class P13 {
    private MyStack stack;
    public P13(MyStack stack) {
        this.stack = stack;
    }

    public void pushService() {
        stack.push();
    }

}
class C13 {
    private MyStack stack;
    public C13(MyStack stack) {
        this.stack = stack;
    }
    public void popService() {
        System.out.println("pop = " + stack.pop());
    }
}
class Thread0118 extends Thread {
    private P13 p;
    public Thread0118(P13 p) {
        this.p = p;
    }

    @Override
    public void run() {
        while (true) {
            p.pushService();
        }
    }
}
class Thread0119 extends Thread {
    private C13 c;
    public Thread0119(C13 c) {
        this.c = c;
    }

    @Override
    public void run() {
        while (true) {
            c.popService();
        }
    }
}
// ------------------------------------------

/**
 * 一生产者与多消费者：操作栈  解决wait条件改变与假死
 * 问题：
 *     抛出异常：将if换成while
 *     假死： notify()换成notifyAll()
 *
 *
 */
class Run0115 {
    public static void main(String[] args) {
        MyStack stack = new MyStack();
        P13 p = new P13(stack);
        C13 c = new C13(stack);
        C13 c2 = new C13(stack);
        C13 c3 = new C13(stack);
        C13 c4 = new C13(stack);
        C13 c5 = new C13(stack);
        Thread0118 t1 = new Thread0118(p);
        Thread0119 t2 = new Thread0119(c);
        Thread0119 t3 = new Thread0119(c2);
        Thread0119 t4 = new Thread0119(c3);
        Thread0119 t5 = new Thread0119(c4);
        Thread0119 t6 = new Thread0119(c5);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
    }
}
// -----------------------------------------
/**
 * 多生产者一消费者：操作栈
 *
 */
class Run0116 {
    public static void main(String[] args) {
        MyStack stack = new MyStack();
        P13 p = new P13(stack);
        P13 p2 = new P13(stack);
        P13 p3 = new P13(stack);
        P13 p4 = new P13(stack);
        P13 p5 = new P13(stack);
        C13 c = new C13(stack);
        Thread0118 t1 = new Thread0118(p);
        Thread0118 t3 = new Thread0118(p2);
        Thread0118 t4 = new Thread0118(p3);
        Thread0118 t5 = new Thread0118(p4);
        Thread0118 t6 = new Thread0118(p5);
        Thread0119 t2 = new Thread0119(c);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
    }
}
// ----------------------------------------
/**
 * 多生产者多消费者：操作栈
 *
 */
class Run0117 {
    public static void main(String[] args) {
        MyStack stack = new MyStack();
        P13 p1 = new P13(stack);
        P13 p2 = new P13(stack);
        P13 p3 = new P13(stack);
        P13 p4 = new P13(stack);
        C13 c1 = new C13(stack);
        C13 c2 = new C13(stack);
        C13 c3 = new C13(stack);
        C13 c4 = new C13(stack);
        C13 c5 = new C13(stack);
        Thread0118 pt1 = new Thread0118(p1);
        Thread0118 pt2 = new Thread0118(p2);
        Thread0118 pt3 = new Thread0118(p3);
        Thread0118 pt4 = new Thread0118(p4);
        Thread0119 ct1 = new Thread0119(c1);
        Thread0119 ct2 = new Thread0119(c2);
        Thread0119 ct3 = new Thread0119(c3);
        Thread0119 ct4 = new Thread0119(c4);
        Thread0119 ct5 = new Thread0119(c5);
        pt1.start();
        pt2.start();
        pt3.start();
        pt4.start();
        ct1.start();
        ct2.start();
        ct3.start();
        ct4.start();
        ct5.start();
    }
}
// -----------------------------------------

/**
 * 1.13 通过管道进行线程间通信：字节流
 *      java中的管道流是一种特殊的流，用于在不同线程之间知己恩发送数据
 *      说明：
 *          一个线程发送数据到输出管道，另外一个线程从输入管道读取数据。通过管道实现线程间的通讯，无需借助其他临时性的文件。
 *      java中有四个流是关于其的：
 *          PipeInputStream PipeOutputStream
 *          PipeWriter PipeReader
 *      实例：
 *          首先会会阻塞在读的方法，直到有数据，读取结束，退出线程。
 *
 */
class Run0118 {
    public static void main(String[] args) throws IOException, InterruptedException {
        WriteData writeData = new WriteData();
        ReadData readData = new ReadData();
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream();
        in.connect(out);
        WriteThread wt = new WriteThread(writeData, out);
        ReadThread rt = new ReadThread(readData, in);
        rt.start();
        Thread.sleep(2000);
        wt.start();
    }
}
class WriteData {
    public void writeData(PipedOutputStream out) {
        try {
            System.out.println("write：");
            for (int i = 0; i < 300; i++) {
                String data = "" + (i + 1);
                out.write(data.getBytes());
                System.out.print(data);
            }
            System.out.println();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class ReadData {
    public void readData(PipedInputStream in) {
        try {
            System.out.println("read：");
            byte[] readArr = new byte[20];
            int readLength = in.read(readArr);
            while (readLength != -1) {
                String newData = new String(readArr, 0, readLength);
                System.out.print(newData);
                readLength = in.read(readArr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class WriteThread extends Thread {
    private WriteData writeData;
    private PipedOutputStream out;
    public WriteThread(WriteData writeData, PipedOutputStream out) {
        this.out = out;
        this.writeData = writeData;
    }

    @Override
    public void run() {
        writeData.writeData(out);
    }
}
class ReadThread extends Thread {
    private ReadData readData;
    private PipedInputStream in;
    public ReadThread(ReadData readData, PipedInputStream in) {
        this.readData = readData;
        this.in = in;
    }

    @Override
    public void run() {
        readData.readData(in);
    }
}
// ---------------------------------------

/**
 * 1.14 通过管道进行线程间的通信：字符流
 *      实例：
 *
 *
 */
class Run0119 {
    public static void main(String[] args) throws IOException, InterruptedException {
        WriteData02 writeData = new WriteData02();
        ReadData02 readData = new ReadData02();
        PipedReader reader = new PipedReader();
        PipedWriter writer = new PipedWriter();
        reader.connect(writer);
        CharWriteThread wt = new CharWriteThread(writeData, writer);
        CharReadThread rt = new CharReadThread(readData, reader);
        rt.start();
        Thread.sleep(2000);
        wt.start();
    }
}
class WriteData02 {
    public void writeChar(PipedWriter writer) {
        try {
            System.out.println("write：");
            for (int i = 0; i < 300; i++) {
                String data = "\r\n" + (i + 1);
                writer.write(data);
                System.out.print(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class ReadData02 {
    public void readChar(PipedReader reader) {
        try {
            System.out.println("read：");
            char[] charArr = new char[20];
            int readLength = reader.read(charArr);
            while (readLength != -1) {
                String data = new String(charArr, 0, readLength);
                System.out.println(data);
                readLength = reader.read(charArr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class CharWriteThread extends Thread {
    private WriteData02 writeData;
    private PipedWriter writer;
    public CharWriteThread(WriteData02 writeData, PipedWriter writer) {
        this.writeData = writeData;
        this.writer = writer;
    }

    @Override
    public void run() {
        writeData.writeChar(writer);
    }
}
class CharReadThread extends Thread {
    private ReadData02 readData;
    private PipedReader reader;
    public CharReadThread(ReadData02 readData, PipedReader reader) {
        this.readData = readData;
        this.reader = reader;
    }

    @Override
    public void run() {
        readData.readChar(reader);
    }
}
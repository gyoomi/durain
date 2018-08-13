/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.communicate;

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
 *       1）nofify不会立刻释放锁，而是等代码执行完后才会释放；
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
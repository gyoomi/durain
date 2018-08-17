/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 2.ReentrantReadWriteLock类
 *   说明：
 *       1）类ReentrantLock具有完全互斥排他的效果，即同一时间只有一个线程执行Reentrant.lock()
 *          方法后面的任务。这样做虽然保证了实例变量的线程安全性，但是效率低下，所以jdk中提供了一种读写锁
 *          ReentrantReadWriteLock类。使用它可以加快读写速度，在某些不需要操作实例变量的方法中，完全
 *          可以使用读写锁来提高效率。
 *       2）读写锁表示分为两个锁，一个读相关操作的锁，也叫共享锁。另一个是写相关操作的锁，也叫排它锁。
 *          也就是多个读锁之间不互斥，读锁和写锁互斥，写锁和读锁互斥。
 *       3）在没有线程Thread进行写入操作时，进行读取操作的多个Thread都可以获取读锁。而进行写入操作的Thread
 *          只有在获取写锁后才能进行写入操作。即多个Thread可以同时读取；但是同一时刻只允许一个Thread进行写入操作。
 *
 *  2.1 类ReentrantReadWriteLock的使用：读读共享
 *
 *
 * @author Leon
 * @version 2018/8/17 21:11
 */
public class ThreadDemo02 {
    public static void main(String[] args) {
       final MyService0201 service = new MyService0201();
       Runnable runnable1 = () -> service.read();
       Thread t1 = new Thread(runnable1);
       Thread t2 = new Thread(runnable1);
       t1.setName("A");
       t2.setName("B");
       t1.start();
       t2.start();
    }
}
class MyService0201 {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public void read() {
        try {
            try {
                lock.readLock().lock();
                System.out.println("获取读锁：" + Thread.currentThread().getName() + " " + System.currentTimeMillis());
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}
// ------------------------------------------------------
/**
 * 2.2 类ReentrantReadWriteLock的使用：写写互斥
 *
 *
 */
class Run0202 {
    public static void main(String[] args) {
       final MyService0202 service = new MyService0202();
       Runnable runnable = () -> service.testWrite();
       Thread t1 = new Thread(runnable);
       Thread t2 = new Thread(runnable);
       t1.setName("AA");
       t2.setName("BB");
       t1.start();
       t2.start();
    }
}
class MyService0202 {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public void testWrite() {
        try {
            lock.writeLock().lock();
            System.out.println("获取写锁： " + Thread.currentThread().getName() + " " + System.currentTimeMillis());
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
// --------------------------------------------------

/**
 * 2.3 类ReentrantReadWriteLock的使用:读写互斥
 * 2.4 类ReentrantReadWriteLock的使用:写读互斥
 *
 *     总结：
 *          读读异步，不互斥。其余写写，读写，写读都是互斥的。同步的。
 *
 */
class Run0203 {
    public static void main(String[] args) {
        final MyService0203 service = new MyService0203();
        Runnable runnable1 = () -> service.testRead();
        Runnable runnable2 = () -> service.testWrite();
        Thread t1 = new Thread(runnable1);
        t1.setName("A");
        Thread t2 = new Thread(runnable2);
        t2.setName("B");
        t1.start();
        t2.start();
    }
}
class MyService0203 {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public void testRead() {
        try {
            lock.readLock().lock();
            System.out.println("获取读锁： " + Thread.currentThread().getName() + " " + System.currentTimeMillis());
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }
    public void testWrite() {
        try {
            lock.writeLock().lock();
            System.out.println("获取读锁： " + Thread.currentThread().getName() + " " + System.currentTimeMillis());
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }
}



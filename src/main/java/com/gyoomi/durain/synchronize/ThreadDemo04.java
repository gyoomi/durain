/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.synchronize;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * volatile关键字
 *     作用：
 *         使变量在多个线程可见。
 *
 *  1、关键字volatile与死循环
 *     如下：程序运行之后，根本无法停下来。原因是main线程一直在处理while循环。
 *          导致程序执行不到后面的程序。解决办法就是使用多线程。
 *
 * @author Leon
 * @version 2018/8/11 17:55
 */
public class ThreadDemo04 {
    public static void main(String[] args) {
        PrintString print = new PrintString();
        print.printStringMethod();
        System.out.println("我要停止它：thread = " + Thread.currentThread().getName());
        print.setContinuePrint(false);
    }
}
class PrintString {
    private boolean isContinuePrint = true;
    public boolean isContinuePrint() {
        return isContinuePrint;
    }
    public void setContinuePrint(boolean continuePrint) {
        isContinuePrint = continuePrint;
    }
    public void printStringMethod() {
        try {
            while (isContinuePrint == true) {
                System.out.println("run printStringMethod threadName = " + Thread.currentThread().getName());
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
// ----------------------------------------------

/**
 * 2.解决同步死循环
 *   但是：下面代码在64位的服务器上，会出现死循环。解决就是通过volatile关键字来解决。
 *   解决：
 *       volatile的作用就是强制从公共堆栈区域中获取变量值，而不是通过线程的私有栈中获取。
 *
 */
class Run0401 {
    public static void main(String[] args) {
        PrintString01 print = new PrintString01();
        new Thread(print).start();
        System.out.println("我要停止它：thread = " + Thread.currentThread().getName());
        print.setContinuePrint(false);
    }
}

class PrintString01 implements Runnable {
    private boolean isContinuePrint = true;
    public boolean isContinuePrint() {
        return isContinuePrint;
    }
    public void setContinuePrint(boolean continuePrint) {
        isContinuePrint = continuePrint;
    }
    public void printStringMethod() {
        try {
            while (isContinuePrint == true) {
                System.out.println("run02 printStringMethod threadName = " + Thread.currentThread().getName());
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        printStringMethod();
    }
}
// ----------------------------------------------------------

/**
 * 3.解决异步死循环
 *
 * 使用volatile强制从公共内存中读取变量的值。
 *
 * 使用volatile增加了实例变量在多个线程间的可见性。但是它最致命的就是不支持原子性。
 *
 * synchronized和volatile对比
 * 1）volatile是线程同步的轻量级实现，所以volatile的性能要比sync好，而且volatile只能修饰变量
 *    而sync可以修改方法及代码块。但是随着jdk版本的提升，sync的效率得到极大的提升，所以在开发中
 *    使用sync还是比较多的。
 * 2）多线程访问volatile不会发生阻塞，而sync则会
 * 3）volatile可以保证数据的可见性，但是不能保证原子性。而sync可以保证原子性，也可以间接保证可见性，
 *    它是他会将私有内存和公共内存中的数据做同步。
 * 4）再次重申一下，volatile解决的是变量在多个线程之间的可见性；而sync解决的多个线程之间
 *    资源访问的同步性。
 *    线程安全包含原子性和可见性，java的同步机制都会围绕这个方面来的
 *
 */
class Run0402 {
    public static void main(String[] args) throws InterruptedException {
        RunThread02 t = new RunThread02();
        t.start();
        Thread.sleep(1000);
        t.setRunning(false);
        System.out.println("已经被赋值为false");
    }
}
class RunThread02 extends Thread {
    private volatile boolean isRunning = true;
    public boolean isRunning() {
        return isRunning;
    }
    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void run() {
        System.out.println("进入了run方法");
        while (isRunning == true) {

        }
        System.out.println("线程被停止了");
    }
}
// -------------------------------------------------

/**
 * 4.volatile非原子特性
 * 说明：
 *     volatile关键字虽然增加了实例变量在多个线程之间的可见性，但是它确不具备同步性那么也就不具备原子性。
 * 实例：
 *     如下。
 *     运行结果并不是10000.
 *     修改：
 *     在addCount()加上synchronized static实现了原子性和可见性
 *     结果是10000.
 * 实例分析：
 *     volatile的适用场合：在多个线程中可以感知实例变量被修改了，并且可以获得最新的值进行使用。
 *     但是如果修改实例变量：如count++; 即i=i+1;这样的表达并不是一个原子性的操作，也就是非线程安全的。
 *     i++可以分解成以下3个步骤：
 *     1）从内存内取出i的值；
 *     2）计算i的值；
 *     3）将i的值写入到内存中。
 *     假如说，在计算第2步的时候，另外一个线程也修改了i的值，那么这个时候就会出现脏数据。
 *     解决的办法就是使用sync关键字。
 *     所以说volatile关键字本身不处理数据的原子性，而是强制对数据的读写操作及时的影响到主内存中。
 *
 *
 *     具体分析的过程的：详见书籍第126页。分析的很好！！！
 *
 */
class Run03 {
    public static void main(String[] args) {
        MyThread0403[] arr = new MyThread0403[100];
        for (int i = 0; i < 100; i++) {
            arr[i] = new MyThread0403();
        }
        for (int i = 0; i < 100; i++) {
            arr[i].start();
        }
    }
}
class MyThread0403 extends Thread {
    public static volatile int count;
    // static 方法必须要加，保证锁的对象是MyThread0403.class
    public synchronized static void addCount() {
        for (int i = 0; i < 100; i++) {
            count++;
        }
        System.out.println("count = " + count);
    }

    @Override
    public void run() {
        super.run();
        addCount();
    }
}
// -----------------------------------------------------------------------

/**
 * 5.使用原子类进行i++操作
 *   要想实现i++的原子性，除了使用synchronized关键字实现同步外，还可以使用原子类来实现。
 *
 *   实例：
 *       如下。
 *       成功加到50000.
 *
 *       AtomicInteger
 *       对应的Boolean，long都有原子操作类
 *
 */
class Run05 {
    public static void main(String[] args) {
        AddCountThread thread = new AddCountThread();
        Thread t1 = new Thread(thread);
        Thread t2 = new Thread(thread);
        Thread t3 = new Thread(thread);
        Thread t4 = new Thread(thread);
        Thread t5 = new Thread(thread);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}
class AddCountThread extends Thread {
    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            System.out.println(count.incrementAndGet());
        }
    }
}
// ---------------------------------------------------

/**
 * 6.原子类也不完全安全
 *
 * 实例：
 *     不安全的情况。
 *     如下。
 *     打印顺序出错。
 *     原因是每个方法是原子性的，但是方法和方法之间并不是原子性的。
 *     错误结果是：
 *     Thread = Thread-1 加了100后的值是:100
 *     Thread = Thread-4 加了100后的值是:500
 *     Thread = Thread-3 加了100后的值是:400
 *     Thread = Thread-0 加了100后的值是:300
 *     Thread = Thread-2 加了100后的值是:200
 *     结果是：505
 *     虽然最后的结果是正确的，但是中间打印的顺序是乱的，而且结果还是错误的。
 *
 *
 *     给addNum()方法加上同步sync,打印的顺序是正确的。
 *     结果如下：
 *     Thread = Thread-1 加了100后的值是:100
 *     Thread = Thread-4 加了100后的值是:201
 *     Thread = Thread-3 加了100后的值是:302
 *     Thread = Thread-2 加了100后的值是:403
 *     Thread = Thread-0 加了100后的值是:504
 *     结果是：505
 *
 */
class Run06 {
    public static void main(String[] args) throws InterruptedException {
        MyService06 service = new MyService06();
        MyThread0406[] arr = new MyThread0406[5];
        for (int i = 0; i < 5; i++) {
            arr[i] = new MyThread0406(service);
        }
        for (int i = 0; i < 5; i++) {
            arr[i].start();
        }
        Thread.sleep(1000);
        System.out.println("结果是：" + service.varRef.get());
    }
}
class MyService06 {
    public AtomicLong varRef = new AtomicLong();
    public  void addNum() {
        System.out.println("Thread = " + Thread.currentThread().getName() + " 加了100后的值是:" + varRef.addAndGet(100));
        varRef.addAndGet(1);
    }
}
class MyThread0406 extends Thread {
    private MyService06 service;
    public MyThread0406(MyService06 service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.addNum();
    }
}
// ----------------------------------------------

/**
 * 7.synchronized代码块有volatile同步功能
 *   关键字synchronized可以使多个线程访问同一个资源具有同步性，而且他还具有将线程工作内存中的私有变量和公共内存中公有变量同步的功能。
 *
 *   证明上述结论的实例：
 *   如下：
 *       代码已经出现了死循环。及时发了停止命令，也停不下来。
 *   解决上述问题：
 *       利用synchronized关键字。
 *        synchronized关键字可以保证在同一时刻，只有一个线程某一个方法或代码块。
 *        1.互斥性
 *        2.可见性
 *        同步的synchronized不仅可以解决一个线程看到的对象处于不一致的状态。
 *        还可以保证进入同步方法或块每个线程，都可以看到由同一个锁保护之前的所有修改效果。
 *
 *
 *      =========================
 *      2. synchronized实现可见性
 *
 *      synchronized除了常见的原子性，还实现了可见性。这是因为：
 *     1) 线程解锁前，必须把共享变量的最新值刷新到主内存中去；
 *     2) 线程加锁时，将清空工作内存中的共享变量的值，使用到共享变量时，从主内存中获取最新的共享变量值（加锁和解锁需要同一把锁
 *
 *     3. volatile实现可见性
 *     通过内存屏障和禁止重排序优化来实现可见性。
 *    1) 对共享变量进行写操作后，加入一条store屏障指令，强制将共享变量的值刷新到主内存；
 *    2) 对共享变量进行读操作前，加入一条load屏障指令，强制从主内存中将最新值刷新到工作内存；
 *
 *    4.volatile不能保证原子性
 *
 *     一个比较典型的例子是++运算符。
 *     在下面的代码中，一共创建了1000个线程，预期应该是加了1000次，那么number的值应该是1000，实际上有可能并不是。
 *     这是因为，++运算符并不是一次操作。以number++为例，可以看作是，先从主内存中取出number的值，然后将其加1，刷新工作内存，刷新主内存，这么几个步骤。
 *     而volatile并不能保证原子性，这就意味着，有可能出现这种情况：
 *     1)线程A获取到主内存的number的值（假设为10）到工作内存
 *     2)此时CPU调度，A暂停，线程B开始执行，同样从主内存中获取到number为10，number++后，number为11，刷新到主内存
 *     3)线程A继续执行number++，它的工作内存中number为10，执行完毕刷新到主内存，此时，number的值为11. 也就是说，AB两个线程同时进行了+1操作，但最终的结果，只加了1
 *
 */
class Run07 {
    public static void main(String[] args) throws InterruptedException {
        MyService0407 service = new MyService0407();
        MyThread0407 t1 = new MyThread0407(service);
        t1.start();
        Thread.sleep(1000);
        MyThread0408 t2 = new MyThread0408(service);
        t2.start();
        System.out.println("已经发起了停止命令");
    }
}
class MyService0407 {
    private boolean isContinue = true;
    public void runMethod() {
        String anyThing = new String();
        while (isContinue == true) {
            synchronized (anyThing) {}
        }
        System.out.println("停下来了。。。");
    }
    public void stopService(boolean isContinue) {
        this.isContinue = isContinue;
    }
}
class MyThread0407 extends Thread {
    private MyService0407 service;
    public MyThread0407(MyService0407 service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.runMethod();
    }
}
class MyThread0408 extends Thread {
    private MyService0407 service;
    public MyThread0408(MyService0407 service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.stopService(false);
    }
}
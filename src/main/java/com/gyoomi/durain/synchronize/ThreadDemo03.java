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
 * 说明：
 *     关键字synchronized还可以应用在static静态方法上，如果你这样的写的话，
 *     就是对*.java对应的.class类进行加锁
 *
 * 结论：
 *     synchronized关键字加到static方法上给Class类上锁，而synchronized关键字加到非static关键字上给对象上锁，
 *     这是二者本质的区别，下面也将进行举例证明。
 *
 * 其他：
 *     synchronized(class) {}代码块其实和synchronized static方法效果是一样的。
 */
class Run0303 {
    public static void main(String[] args) {
        MyService0304 service = new MyService0304();
        MyThread0305 t1 = new MyThread0305(service);
        MyThread0306 t2 = new MyThread0306(service);
        MyThread0307 t3 = new MyThread0307(service);
        t1.setName("AAAA");
        t2.setName("BBBB");
        t3.setName("CCCC");
        t1.start();
        t2.start();
        t3.start();
    }
}
class MyService0304 {
    public synchronized static void printA() {
        try {
            System.out.println("thread " + Thread.currentThread().getName() + "在 " + System.currentTimeMillis() + " 进入了 printA");
            Thread.sleep(3000);
            System.out.println("thread " + Thread.currentThread().getName() + "在 " + System.currentTimeMillis() + " 离开了 printA");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public synchronized static void printB() {
        System.out.println("thread " + Thread.currentThread().getName() + "在 " + System.currentTimeMillis() + " 进入了 printB");
        System.out.println("thread " + Thread.currentThread().getName() + "在 " + System.currentTimeMillis() + " 离开了 printB");
    }

    public synchronized void printC() {
        System.out.println("thread " + Thread.currentThread().getName() + "在 " + System.currentTimeMillis() + " 进入了 printC");
        System.out.println("thread " + Thread.currentThread().getName() + "在 " + System.currentTimeMillis() + " 离开了 printC");
    }
}
class MyThread0305 extends Thread {
    private MyService0304 service;
    public MyThread0305(MyService0304 service) {
        this.service = service;
    }
    @Override
    public void run() {
        service.printA();
    }
}
class MyThread0306 extends Thread {
    private MyService0304 service;
    public MyThread0306(MyService0304 service) {
        this.service = service;
    }
    @Override
    public void run() {
        service.printB();
    }
}
class MyThread0307 extends Thread {
    private MyService0304 service;
    public MyThread0307(MyService0304 service) {
        this.service = service;
    }
    @Override
    public void run() {
        service.printC();
    }
}
// ----------------------------------------------------------------------------

/**
 * 数据类型String的常量池特性
 *
 * 在jvm中具有string常量池缓存的功能。
 *
 * 所以在使用synchronized(string)同步快与String联合使用时，要注意常量池带来的一些例外。
 *
 * 结论:
 *     开发中药避免使用string类型作为锁，这是避免string常量池的作用，导致多个线程都是一把锁。
 *     进行new Object()方式，并且不把他放入到缓存中。
 */
class Run0304 {
    public static void main(String[] args) {
        /*
        String a = new String();
        String b = new String("");
        System.out.println(a.equals(b));
        // true
        */
        MyService0305 service = new MyService0305();
        MyThread0308 t1 = new MyThread0308(service);
        MyThread0309 t2 = new MyThread0309(service);
        t1.setName("A");
        t2.setName("B");
        t1.start();
        t2.start();
    }
}
class MyService0305 {
    public static void print(String stringParam) {
        try {
            synchronized (stringParam) {
                while (true) {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class MyThread0308 extends Thread {
    private MyService0305 service;
    public MyThread0308(MyService0305 service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.print("AA");
    }
}
class MyThread0309 extends Thread {
    private MyService0305 service;
    public MyThread0309(MyService0305 service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.print("AA");
    }
}
// -------------------------------------------------------

/**
 * 同步synchronized方法无线等待和解决
 *
 * 问题：
 *     同步方法容易造成死循环。
 * 演示：
 *     如下。
 *        线程b永远得不到运行机会，线程锁死了。可以使用同步块来解决。
 *
 */
class Run0306 {
    public static void main(String[] args) {
        MyService0306 service = new MyService0306();
        MyThreadA t1 = new MyThreadA(service);
        MyThreadB t2 = new MyThreadB(service);
        t1.start();
        t2.start();
    }
}
class MyService0306 {
    private Object o1 = new Object();
    public void methodA() {
        synchronized (o1) {
            System.out.println("methodA begin");
            boolean flag = true;
            while (flag) {

            }
            System.out.println("methodA end");
        }
    }
    private Object o2 = new Object();
    public void methodB() {
        synchronized (o2) {
            System.out.println("methodB begin");
            System.out.println("methodB end");
        }

    }
}
class MyThreadA extends Thread {
    private MyService0306 service;
    public MyThreadA(MyService0306 service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.methodA();
    }
}
class MyThreadB extends Thread {
    private MyService0306 service;
    public MyThreadB(MyService0306 service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.methodB();
    }
}
// -----------------------------------------------------------------

/**
 * 多线程的死锁
 *
 * 一句话来概括：不同线程都在等待不可能释放的锁，从而导致所有的任务都无法完成。在多线程编程中，
 * 死锁是必须要避免的。
 *
 * 下面实例使用嵌套来模拟死锁，但是死锁，并不都是嵌套形式的，非嵌套形式也会出现。
 * 只要是相互等待不可能释放的锁都是死锁。
 *
 */
class Run0307 {
    public static void main(String[] args) throws InterruptedException {
        DealThread t1 = new DealThread();
        t1.setUsername("a");
        Thread thread1 = new Thread(t1);
        thread1.start();
        Thread.sleep(1000);
        t1.setUsername("b");
        Thread thread2 = new Thread(t1);
        thread2.start();
    }
}
class DealThread implements Runnable {
    private String username;
    private Object o1 = new Object();
    private Object o2 = new Object();

    public void setUsername(String username) {
        this.username = username;
    }

    public void run() {
        if  (username.equals("a")) {
            synchronized (o1) {
                try {
                    System.out.println("username = " + username);
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o2) {
                    System.out.println("按照o1 -> o2 的顺序执行");
                }
            }
        } else {
            synchronized (o2) {
                try {
                    System.out.println("username = " + username);
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o1) {
                    System.out.println("按照o2 -> o1 的顺序执行");
                }
            }
        }
    }
}
// ---------------------------------------------------------------------

/**
 * 内置类和静态内置类
 * synchronized关键字还设计在内置类中使用。
 *
 * 内部类分为：
 *     静态内部类  可以直接new
 *     非静态内部（又分pubic修饰的类）  通过  外部类实例进行new
 *
 *  内置类与同步：实验1
 *     如下：
 *         因为持有不用的对象监视器，所以打印结果就是乱的。
 *
 *    如下：
 */
class Run0308 {
    public static void main(String[] args) {
        final OutClass.InnerClass in = new OutClass.InnerClass();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                in.method1();
            }
        }, "AAA");
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                in.method2();
            }
        }, "BBB");
        t1.start();
        t2.start();
    }
}
class OutClass {
    static class InnerClass {
        public void method1() {
            synchronized ("其他锁") {
                for (int i = 0; i < 10; i++) {
                    try {
                        System.out.println("Thread = " + Thread.currentThread().getName() + " i = " + (i + 1));
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public synchronized void method2() {
            for (int i = 0; i < 20; i++) {
                try {
                    System.out.println("Thread = " + Thread.currentThread().getName() + " i = " + (i + 1));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
// ---------------------------------------------------------------------------------------

/**
 * 内置类与同步：实验2
 *     如下：
 *     测试sync(class2)对class2上锁后，其他线程只能以同步的方式调用class2中的静态同步方法
 *     结论：
 *         class1中的method1方法和method2方法异步执行，class2中的方法和class1中的method1方法保持同步
 *
 *
 */
class Run0309 {
    public static void main(String[] args) {
        final OutClass2.InnerClass1 innerClass1 = new OutClass2.InnerClass1();
        final OutClass2.InnerClass2 innerClass2 = new OutClass2.InnerClass2();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                innerClass1.method1(innerClass2);
            }
        }, "AAA");

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                innerClass1.method2();
            }
        }, "BBB");

        Thread t3 = new Thread(new Runnable() {
            public void run() {
                innerClass2.method1();
            }
        }, "CCC");
        t1.start();
        t2.start();
        t3.start();
    }
}
class OutClass2 {
    static class InnerClass1 {
        public void method1(InnerClass2 class2) {
            String threadName = Thread.currentThread().getName();
            synchronized (class2) {
                System.out.println(threadName + "进入了 InnerClass1 method1方法");
                for (int i = 0; i < 10; i++) {
                    System.out.println("i = " + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(threadName + "离开了 InnerClass1 method1方法");
            }
        }

        public synchronized void method2() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + "进入了 InnerClass1 method2方法");
            for (int j = 0; j < 10; j++) {
                System.out.println("j = " + j);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(threadName + "离开了 InnerClass1 method2方法");
        }
    }

    static class InnerClass2 {
        public synchronized void method1() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + "进入了 InnerClass2 method1方法");
            for (int k = 0; k < 10; k++) {
                System.out.println("k = " + k);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(threadName + "离开了 InnerClass2 method1方法");
        }
    }
}
// --------------------------------------------

/**
 * 锁对象的改变
 *
 * 在将任何数据作为同步锁时，需要注意的是：
 * 结论：
 *     1.是否有多个线程同时持有这些锁，如果同时持有这些相同的锁，则这些线程之间是同步，。如果分别获得这些锁对象的话，
 *     这些线程之间则是异步的。
 *     2.注意：只要是对象不变，只是对象的属性发生变化。代表对象还是没有变。结果还是同步的。
 */
class Run0310 {
    public static void main(String[] args) throws InterruptedException {
        MyService0310 service = new MyService0310();
        MyThread0311 t1 = new MyThread0311(service);
        MyThread0312 t2 = new MyThread0312(service);
        t1.setName("AAA");
        t1.start();
        t2.setName("BBB");
        // Thread.sleep(50);
        t2.start();
    }
}
class MyService0310 {
    private String lock = "123";
    public void testMethod() {
        try {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + " 在 " + System.currentTimeMillis() + "begin");
                lock = "456";
                Thread.sleep(3000);
                System.out.println(Thread.currentThread().getName() + " 在 " + System.currentTimeMillis() + "end");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class MyThread0311 extends Thread {
    private MyService0310 service;
    public MyThread0311(MyService0310 service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.testMethod();
    }
}
class MyThread0312 extends Thread {
    private MyService0310 service;
    public MyThread0312(MyService0310 service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.testMethod();
    }
}


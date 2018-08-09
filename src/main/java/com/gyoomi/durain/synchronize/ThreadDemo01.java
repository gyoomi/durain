/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.synchronize;

/**
 * synchronized同步方法
 *     线程安全和非线程安全 约等于 数据是否脏读。
 *
 *
 * @author Leon
 * @version 2018/8/9 9:27
 */
public class ThreadDemo01 {
}

// ------------------------------------------------------------------------------------------------

/**
 * 方法内的变量为线程安全的。
 *     说明：线程安全问题存在于实例变量。如果是方法内的私有变量，则不存在非线程安全的问题。
 *     用以下例子来证明：
 * 结论：
 *     可见，方法内的变量不存在线程安全问题，永远都是线程安全的。这是由于方法内部的变量的私有特性造成。
 */
class Run01 {
    public static void main(String[] args) {
        HasSelfPrivateNum num = new HasSelfPrivateNum();
        Thread0101 thread01 = new Thread0101(num);
        Thread0102 thread02 = new Thread0102(num);
        thread01.start();
        thread02.start();
    }
}

class HasSelfPrivateNum {
    public void addI(String username) {
        try {
            int num = 0;
            if ("a".equals(username)) {
                num = 100;
                System.out.println("a set over");
                Thread.sleep(2000);
            } else {
                num = 200;
                System.out.println("b set over");
            }
            System.out.println(username + " num = " + num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Thread0101 extends Thread {
    private HasSelfPrivateNum numRef;

    public Thread0101(HasSelfPrivateNum numRef) {
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("a");
    }
}

class Thread0102 extends Thread {
    private HasSelfPrivateNum numRef;

    public Thread0102(HasSelfPrivateNum numRef) {
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("b");
    }
}

// -----------------------------------------------------------------------------------------------------------------

/**
 * 实例变量的非线程安全
 * 说明：
 *     如果多个线程访问一个对象的实例变量，则会出现非线程安全问题。
 * 测试结果：
 *     b set over
 *     a set over
 *     b num = 200
 *     a num = 200
 *
 * 本实验的是两个线程同时访问一个没有同步的方法，如果两个线程同时操作同一个变量，则会出现“非线程安全”的问题。
 *
 * 但是如果加上synchronized则数据是正常。
 */
class Run0102 {
    public static void main(String[] args) {
        HasSelfPrivateNum02 numRef = new HasSelfPrivateNum02();
        Thread0103 thread01 = new Thread0103(numRef);
        Thread0104 thread02 = new Thread0104(numRef);
        thread01.start();
        thread02.start();
    }
}

class HasSelfPrivateNum02 {
    private int num = 0;
    public void addI(String username) {
        try {
            if ("a".equals(username)) {
                num = 100;
                System.out.println("a set over");
                Thread.sleep(3000);
            } else {
                num = 200;
                System.out.println("b set over");
            }
            System.out.println(username + " num = " + num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Thread0103 extends Thread {
    private HasSelfPrivateNum02 numRef;

    public Thread0103(HasSelfPrivateNum02 numRef) {
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("a");
    }
}

class Thread0104 extends Thread {
    private HasSelfPrivateNum02 numRef;

    public Thread0104(HasSelfPrivateNum02 numRef) {
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("b");
    }
}

//------------------------------------------------------------------------------------

/**
 * 多个对象多个锁
 *
 * 测试结果：
 *     多个线程访问多个对象，jvm会创建多个锁。以下测试结果说明创建了两个锁
 *
 */
class Run0103 {
    public static void main(String[] args) {
        HasSelfPrivateNum03 num1 = new HasSelfPrivateNum03();
        HasSelfPrivateNum03 num2 = new HasSelfPrivateNum03();
        Thread0105 thread01 = new Thread0105(num1);
        Thread0106 thread02 = new Thread0106(num2);
        thread01.start();
        thread02.start();
    }
}
class HasSelfPrivateNum03 {
    private int num = 0;
    public synchronized void addI(String username) {
        try {
            if ("a".equals(username)) {
                num = 100;
                System.out.println("a set over");
                Thread.sleep(2500);
            } else {
                num = 200;
                System.out.println("b set over");
            }
            System.out.println(username + " num = " + num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Thread0105 extends Thread {
    private HasSelfPrivateNum03 numRef;
    public Thread0105(HasSelfPrivateNum03 numRef) {
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("a");
    }
}

class Thread0106 extends Thread {
    private HasSelfPrivateNum03 numRef;
    public Thread0106(HasSelfPrivateNum03 numRef) {
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("b");
    }
}

// ---------------------------------------------------------------------------------------------

/**
 * synchronized方法和锁对象
 * 说明：
 *     synchronized方法锁的是对象
 *
 *     1:同一个同步方法会排队执行
 *     2:一个同步，一个非同步方法，会异步执行
 *     3:同一类中两个同步方法，也会排队等待
 */
class Run0104 {
    public static void main(String[] args) {
        MyObject object = new MyObject();
        MyThread0106 thread01 = new MyThread0106(object);
        MyThread0107 thread02 = new MyThread0107(object);
        thread01.setName("A");
        thread02.setName("B");
        thread01.start();
        thread02.start();
    }
}

class MyObject {
    public synchronized void methodA() {
        try {
            System.out.println("begin methodA thread = " + Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("end endTime = " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void methodB() {
        try {
            System.out.println("begin methodB thread = " + Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("end ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyThread0106 extends Thread {
    private MyObject object;
    public MyThread0106(MyObject object) {
        this.object = object;
    }

    @Override
    public void run() {
        super.run();
        object.methodA();
    }
}
class MyThread0107 extends Thread {
    private MyObject object;
    public MyThread0107(MyObject object) {
        this.object = object;
    }

    @Override
    public void run() {
        super.run();
        object.methodB();
    }
}
// --------------------------------------------------------------------

/**
 * 脏读
 * 分析：
 *     因为getValue()方法不是同步的。需要解决脏数据的问题加上sync即可
 *
 *
 */
class Run0105 {
    public static void main(String[] args) throws Exception {

        PublicVar varRef = new PublicVar();
        MyThread0108 thread = new MyThread0108(varRef);
        thread.start();
        // 是否出现脏数据，依赖此数据的大小
        Thread.sleep(200);
        varRef.getValue();
    }
}

class PublicVar {
    private String username = "a";
    private String password = "aa";

    public synchronized void setValue(String username, String password) {
        try {
            this.username = username;
            Thread.sleep(5000);
            this.password = password;
            System.out.println("set value thread name = " + Thread.currentThread().getName() + " " + username + ":" + password);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getValue() {
        System.out.println("get value thread name = " + Thread.currentThread().getName() + " " + username + ":" + password);
    }
}

class MyThread0108 extends Thread {
    private PublicVar varRef;

    public MyThread0108(PublicVar varRef) {
        this.varRef = varRef;
    }

    @Override
    public void run() {
        super.run();
        varRef.setValue("b", "bb");
    }
}
// ------------------------------------------------------------------------------

/**
 * synchronized锁重入
 *     synchronized拥有可重入的功能，也就是在使用synchronized时，当一个线程得到一个对象锁后，再次请求该对象锁时是可以再次获得该对象锁的。
 *     这也证明了在一个sync方法或块内部时，调用本类其他的sync方法或块时，是永远可以得到锁的。
 *
 * 概念：自己可以再次获取自己内部的锁。
 *      另外，可重入性支持在父子继承环境中。
 */
class Run0106 {
    public static void main(String[] args) {
        Thread0109 thread = new Thread0109();
        thread.start();
    }
}
class Service {
    public synchronized void service1() {
        System.out.println("service1");
        service2();
    }
    public synchronized void service2() {
        System.out.println("service2");
        service3();
    }
    public synchronized void service3() {
        System.out.println("service3");
    }
}

class Thread0109 extends Thread {
    @Override
    public void run() {
        Service s = new Service();
        s.service1();
    }
}

// --------------------------------------------------------------------------------------------------------------
class Run0109 {
    public static void main(String[] args) {

    }
}

class Main {
    private int i = 10;
    public synchronized void operateMainMethod() {
        try {
            i--;
            System.out.println("main print: i = " + i);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Sub extends Main {
    public synchronized void operateSubMethod() {

    }
}





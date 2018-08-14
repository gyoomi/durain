/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.communicate;

/**
 * 2.方法join的使用
 *   说明：
 *        很多情况下，主线程创建并启动了子线程，如果子线程中要进行大量的运算，主线程
 *        往往早于子线程结束。这时，如果主线程想等待子线程执行完再结束，比如需要说主
 *        线程需要子线程处理的一个数据，主线程想要取得这个数据的值，就要用到join方法。
 *   作用：等待线程对象销毁。
 *
 * 2.1 学习join前的铺垫
 *     实例：
 *         如下。
 *         出现问题。
 *
 * @author Leon
 * @version 2018/8/14 21:51
 */
public class ThreadDemo02 {
    public static void main(String[] args) {
        Thread0201 t = new Thread0201();
        t.start();
        // Thread.sleep(?);
        System.out.println("我想等t线程执行完毕再执行。。。但是不知道？填多少啊");
    }
}
class Thread0201 extends Thread {
    @Override
    public void run() {
        try {
            int seconds = (int) (Math.random() * 1000);
            System.out.println(seconds);
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
// ----------------------------------
/**
 * 2.2 用join()方法来解决
 *     实例:
 *         如下，实现想要的效果。
 *      总结：
 *          1）使所属线程对象x正常执行run方法中的任务，而使当前线程z无限期阻塞，等待线程x销毁后在执行z后面的代码。
 *          2）方法join具有使线程排队运行的作用，有些类型同步的效果。
 *             join和sync的区别是：join在内部使用wait方法进行等待，而sync则使用对象监视器原理作为同步。
 *
 */
class Run0201 {
    public static void main(String[] args) throws InterruptedException {
        Thread0202 t = new Thread0202();
        t.start();
        t.join();
        System.out.println("我想在t线程执行完毕后执行，获得t线程执行的结果");
        System.out.println("哈哈，我是不是在t线程后成功的执行了哈哈哈");
    }
}
class Thread0202 extends Thread {
    @Override
    public void run() {
        try {
            int seconds = (int) (Math.random() * 1000);
            System.out.println(seconds);
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
// ----------------------------------
/**
 * 2.3 方法join与异常
 *     结论：
 *         在join的过程中，如果当前线程被中断，则当前线程出现异常。
 *     实例：
 *         join和interrupt如果彼此遇到则会发生异常。但是线程按钮还是红色，只是a线程还在运行。
 *
 */
class Run0202 {
    public static void main(String[] args) throws InterruptedException {
        Thread0204 t1 = new Thread0204();
        t1.start();
        Thread.sleep(500);
        Thread0205 t2 = new Thread0205(t1);
        t2.start();
    }
}
class Thread0203 extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String s = new String();
            Math.random();
        }
    }
}
class Thread0204 extends Thread {
    @Override
    public void run() {
        try {
            Thread0203 t = new Thread0203();
            t.start();
            t.join();
            System.out.println("线程b end处打印了");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("线程b catch处打印了");
        }

    }
}
class Thread0205 extends Thread {
    private Thread0204 t;
    public Thread0205(Thread0204 t) {
        this.t = t;
    }

    @Override
    public void run() {
        t.interrupt();
    }
}


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
// ---------------------------------
/**
 * 2.4 方法join(long)的使用
 *     设置一定时长的等待
 *
 *     实例：
 *         主线程只等待子线程2秒。
 *     并且此时t.join(2000);和Thread.sleep(2000);等价。
 *     但是其实二者还是有区别的。具体区别看下节。
 */
class Run0203 {
    public static void main(String[] args) throws InterruptedException {
        Thread0206 t = new Thread0206();
        t.start();
        // t.join(2000);
        Thread.sleep(2000);
        System.out.println(" end time at " + System.currentTimeMillis());
    }
}
class Thread0206 extends Thread {
    @Override
    public void run() {
        try {
            System.out.println(" begin start at " + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println("end end end at" + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
// --------------------------------

/**
 * 2.5 方法join(long)和sleep(long)的区别
 *     join(long)：其实底层使用的是wait(long)实现的。
 *     sleep(long)：sleep不会释放锁。
 *     实例：
 *         测试sleep()不会释放锁的实验。
 *     打印：
 *         run begin start = 1534322854747
 *         >分析：在start之后，有两秒的时间b对象锁是被线程Thread0208持有的，并且是sleep，锁不能释放
 *         bService run = 1534322856748
 *         run begin end   = 1534322857748
 *     结论：Thread.sleep()不释放锁。
 *     实例：
 *         使用join()
 *     结论：
 *         join()会立刻释放锁
 *
 */
class Run0204 {
    public static void main(String[] args) {
        try {
            Thread0207 b = new Thread0207();
            Thread0208 t1 = new Thread0208(b);
            Thread0209 t2 = new Thread0209(b);
            t1.start();
            Thread.sleep(1000);
            t2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Thread0207 extends Thread {
    @Override
    public void run() {
        try {
            System.out.println("run begin start = " + System.currentTimeMillis());
            Thread.sleep(3000);
            System.out.println("run begin end   = " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void bService() {
        System.out.println("bService run = " + System.currentTimeMillis());
    }
}
class Thread0208 extends Thread {
    private Thread0207 b;
    public Thread0208(Thread0207 b) {
        this.b = b;
    }

    @Override
    public void run() {
        try {
            synchronized (b) {
                b.start();
                // Thread.sleep(2000);
                b.join(); // 如果改成join(),则会立刻释放锁。
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    Math.random();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Thread0209 extends Thread {
    private Thread0207 b;
    public Thread0209(Thread0207 b) {
        this.b = b;
    }

    @Override
    public void run() {
        b.bService();
    }
}
// ----------------------------------

/**
 * 2.6 - 2.7 方法join()后面的代码提前运行：出现意外/解释意外
 *
 *
 */
class Run0205 {
    public static void main(String[] args) {
        try {
            Thread0210 b = new Thread0210();
            Thread0211 a = new Thread0211(b);
            a.start();
            b.start();
            b.join(2000);
            System.out.println("main end time = " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Thread0210 extends Thread {
    @Override
    public synchronized void run() {
        try {
            System.out.println(" B begin thread = " + Thread.currentThread().getName() + " time = " + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println(" B end   thread = " + Thread.currentThread().getName() + " time = " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Thread0211 extends Thread {
    private Thread0210 b;
    public Thread0211(Thread0210 b) {
        this.b = b;
    }

    @Override
    public void run() {
        try {
            synchronized (b) {
                System.out.println("A begin thread = " + Thread.currentThread().getName() + " time = " + System.currentTimeMillis());
                Thread.sleep(5000);
                System.out.println("A end   thread = " + Thread.currentThread().getName() + " time = " + System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}






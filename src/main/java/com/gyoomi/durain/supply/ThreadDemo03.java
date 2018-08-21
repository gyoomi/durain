/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.supply;

/**
 * 7.4 SimpleDateFormat非线程安全
 *     说明：
 *         SimpleDateFormat类主要是负责日期转换和格式化，但是在多线程环境下，此类存在线程安全问题。
 *     解决：
 *         1）使用的多例的SimpleDateFormat对象，每在一个线程中进行日期转换的时候都重新new一个SimpleDateFormat。
 *         2）使用ThreadLocal<SimpleDateFormat>来解决，
 *         3）转换方法加锁
 *
 * @author Leon
 * @version 2018/8/21 11:51
 */
public class ThreadDemo03 {
    public static void main(String[] args) {

    }
}
// -------------------------------------------
/**
 * 7.5 线程中出现异常的处理
 *     1）使用UncaughtExceptionHandler类，可以对发生的异常进行有效的处理；
 *        setUncaughtExceptionHandler()是给指定的线程对象设置异常处理器；
 *        也可以使用MyThread0301.setDefaultUncaughtExceptionHandler()方法给所有的线程对象设置异常处理器
 *     2）
 *
 */
class Run0301 {
    public static void main(String[] args) {
        MyThread0301.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("发生异常了");
            }
        });
        MyThread0301 t = new MyThread0301();
        ///
        /*t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("线程：" + t.getName() + "出现异常了");
                e.printStackTrace();
            }
        });*/
        t.start();
    }
}
class MyThread0301 extends Thread {

    @Override
    public void run() {
        String str = null;
        System.out.println(str.hashCode());
    }
}
// ------------------------------------------
/**
 * 7.6 线程组内处理异常
 *     说明：
 *         线程组一个线程出现异常不会影响其他线程
 *     如何实现一个线程出现异常，其他线程都要停止运行？
 *     实现：
 *         使用自定义的线程组对象，然后重写UncaughtException()方法
 *
 */
class Run0302 {
    public static void main(String[] args) {

    }
}
// -----------------------------------------
/**
 * 7.7 线程异常处理的传递
 *     说明：
 *
 *
 */
class Run0303 {
    public static void main(String[] args) {

    }
}

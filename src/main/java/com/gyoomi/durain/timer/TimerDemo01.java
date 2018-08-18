/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 5. 定时器Timer
 *    说明：
 *        在jdk中,Timer主要负责计划任务的功能。也就是在指定的时间内开始执行某一任务。
 *        它内部是使用多线程的技术进行处理。所以我们很有必要去学习。
 *    1）Timer类的主要作用是设置计划任务
 *    2）但是封装任务的类却是TimerTask。把执行计划任务的代码放入TimerTask的子类中，因为其是个
 *       抽象类。
 *
 *  5.1 方法schedule(TimerTask, Date)的测试
 *      作用：在制定时间内执行一次任务。
 *      (1)执行任务时间晚于当前时间：在未来执行的效果。
 *      (2)计划时间早于当前时间：提前运行的效果。
 *         结论：如果执行任务的时间早于当前时间，则会立即执行任务。
 *               如果是守护线程，并且执行任务的时间早于当前时间，则任务不会执行，只会执行main方法里面的代码。
 *      (3)多个task任务及延时的测试
 *         结论 ：
 *             会按照顺序进行执行。
 *             如果前面的任务占用的时间过长，则后面的任务队列将会被推迟执行。
 *
 *
 * @author Leon
 * @version 2018/8/18 16:33
 */
public class TimerDemo01 {
    private static Timer timer = new Timer(true);
    static class MyTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("task run... time = " + new Date());
        }
    }

    public static void main(String[] args) throws ParseException {
        MyTask task = new MyTask();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date scheduleDate = sdf.parse("2018-08-18 17:02:00");
        System.out.println("任务时间为： " + scheduleDate.toLocaleString() + "当前时间为： " + new Date().toLocaleString());
        timer.schedule(task, scheduleDate);
    }
}
class Run0101 {
    private static Timer timer = new Timer();
    static class MyTask01 extends TimerTask {
        @Override
        public void run() {
            System.out.println("MyTask01 执行了。时间为： " + new Date().toLocaleString());
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("MyTask01 end");
        }
    }
    static class MyTask02 extends TimerTask {
        @Override
        public void run() {
            System.out.println("MyTask02 执行了。时间为： " + new Date().toLocaleString());
        }
    }

    public static void main(String[] args) throws ParseException {
        MyTask01 task01 = new MyTask01();
        MyTask02 task02 = new MyTask02();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date schedule01 = sdf1.parse("2018-08-18 17:29:00");
        Date schedule02 = sdf1.parse("2018-08-18 17:29:20");
        System.out.println("当前时间为：" + new Date().toLocaleString());
        timer.schedule(task01, schedule01);
        timer.schedule(task02, schedule02);
    }
}
// -------------------------------------------
/**
 * 5.2 方法schedule(TimerTask task, Date firstDate, long period)的测试
 *     作用：
 *         在指定的日期后，按照指定的时间间隔无限循环地执行某一个任务。
 *     (1)计划时间晚于当前时间：在未来执行的效果
 *        结论：按期执行
 *     (2)计划时间早于当前时间：提前运行的效果
 *     (3)任务执行时间被延迟
 *        结论：任务会顺序执行，而且会按照任务实际执行的时间进行执行.
 *     (4)TimerTask类的cancel()方法
 *        作用：将自身从任务队列中清除。并且其他任务不受影响。
 *     (5)Timer类的cancel()方法
 *        作用：与上面那个不同，它会清空属于这个timer下的所有任务队列。
 *     (6)Timer类cancel()方法的注意事项
 *        cancel()有时不一定停止计划的任务，而是正常执行。
 *        原因是cancel方法没有抢到queue锁，所以 任务才会继续进行下去。
 *
 *
 */
class Run0102 {
    static public class MyTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("task run..." + new Date().toLocaleString());
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("task end..." + new Date().toLocaleString());
        }
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date suhedule = sdf.parse("2018-08-18 17:37:00");
        MyTask task = new MyTask();
        System.out.println("当前时间为：" + new Date().toLocaleString());
        new Timer().schedule(task, suhedule, 4000);
    }
}
class Run0103 {
    static public class MyTaskA extends TimerTask {
        @Override
        public void run() {
            System.out.println("taskA run..." + new Date().toLocaleString());
            timer.cancel();
        }
    }
    static public class MyTaskB extends TimerTask {
        @Override
        public void run() {
            System.out.println("taskB run..." + new Date().toLocaleString());
        }
    }
    private static Timer timer = new Timer();

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date suhedule = sdf.parse("2018-08-18 17:47:00");
        MyTaskA a = new MyTaskA();
        MyTaskB b = new MyTaskB();
        System.out.println("当前时间为：" + new Date().toLocaleString());
        timer.schedule(a, suhedule, 4000);
        timer.schedule(b, suhedule, 4000);
    }
}
// -------------------------------------------
/**
 * 5.3 方法schedule(TimerTask task,long delay)的测试
 *     作用：以执行此方法的当前时间为参考，在此基础上延迟指定的毫秒数来执行下一次任务。
 *
 */
class Run0104 {
    static public class MyTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("task run..." + new Date().toLocaleString());
        }
    }

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Timer timer = new Timer();
        System.out.println("当前时间为：" + new Date().toLocaleString());
        timer.schedule(task, 3000);
    }
}
// ------------------------------------------
/**
 * 5.4 方法schedule(TimerTask task,long delay,long period)的测试
 *     作用：
 *         凡是带period参数的，都是无限循环执行TimerTask中任务。
 *
 */
class Run0105 {
    static public class MyTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("task run..." + new Date().toLocaleString());
        }
    }

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Timer timer = new Timer();
        System.out.println("当前时间为：" + new Date().toLocaleString());
        timer.schedule(task, 3000, 5000);
    }
}
// ------------------------------------------
/**
 * 5.5 方法scheduleAtFixedRate(TimerTask task,Date firstDate,long period)的使用
 *     1）方法schedule和方法scheduleAtFixedRate都会按照顺序执行，所以请不要考虑非线程安全的情况。
 *     2）区别主要在于不延时的情况。
 *     3）使用schedule方法，如果执行任务的时间没有被延时，那么下一次任务的执行时间参考的是上一次任务开始的时间来计算的
 *     4）使用scheduleAtFixedRate方法，如果任务执行的时间没有被延迟，那么下一次任务执行的时间参考的是上一次任务结束的时间来计算的。
 *     5）延时的情况则没有区别，不管是schedule还是scheduleAtFixedRate如果执行的任务都被延时的话，那么下一次任务执行的时间是参考上一次任务结束的时间
 *        来计算的。
 *
 *     1.测试schedule方法任务不延时
 *       示例：Run0106
 *     2.测试schedule延时的情况
 *       示例：Run0107
 *     3.测试scheduleAtFixedRate不延时情况
 *       示例：Run0108
 *     4.测试scheduleAtFixedRate延时情况
 *       示例：Run0109
 *     5.验证schedule方法不具有追赶执行性
 *       说明：如果执行计划时间早于当前时间，那么计划时间到当前时间之间的任务则会被取消
 *     6.验证scheduleAtFixedRate方法具有追赶执行性
 *       说明：拉下的任务会被补充性的执行完毕
 *       如下：示例。
 *
 */
class Run0106 {
    private static Timer timer = new Timer();
    private static int runCount = 0;

    static public class MyTask extends TimerTask {
        @Override
        public void run() {
            try {
                System.out.println("1 begin 运行了！时间为：" + new Date().toLocaleString());
                Thread.sleep(1000);
                System.out.println("1 end   运行了！时间为：" + new Date().toLocaleString());
                runCount++;
                if (runCount == 5) {
                    timer.cancel();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws ParseException {
        MyTask task = new MyTask();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date schedule = sdf.parse("2018-08-18 21:16:20");
        System.out.println("开始了：" + new Date().toLocaleString());
        timer.schedule(task, schedule, 3000);
        // 可见其下次任务执行的时候，参考的上一次任务开始的时间
    }
}
class Run0107 {
    private static Timer timer = new Timer();
    private static int runCount = 0;

    static public class MyTask extends TimerTask {
        @Override
        public void run() {
            try {
                System.out.println("1 begin 运行了！时间为：" + new Date().toLocaleString());
                Thread.sleep(5000);
                System.out.println("1 end   运行了！时间为：" + new Date().toLocaleString());
                runCount++;
                if (runCount == 5) {
                    timer.cancel();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws ParseException {
        MyTask task = new MyTask();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date schedule = sdf.parse("2018-08-18 21:17:20");
        System.out.println("开始了：" + new Date().toLocaleString());
        timer.schedule(task, schedule, 3000);
        // 延时的情况的参考的上次任务结束的时间计算的
    }
}
class Run0108 {
    private static Timer timer = new Timer();
    private static int runCount = 0;

    static public class MyTask extends TimerTask {
        @Override
        public void run() {
            try {
                System.out.println("3 begin 运行了！时间为：" + new Date().toLocaleString());
                Thread.sleep(1000);
                System.out.println("3 end   运行了！时间为：" + new Date().toLocaleString());
                runCount++;
                if (runCount == 5) {
                    timer.cancel();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws ParseException {
        MyTask task = new MyTask();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date schedule = sdf.parse("2018-08-18 21:19:00");
        System.out.println("开始了：" + new Date().toLocaleString());
        timer.scheduleAtFixedRate(task, schedule, 3000);
        // 参考的是上次任务结束的时间计算
    }
}
class Run0109 {
    private static Timer timer = new Timer();
    private static int runCount = 0;

    static public class MyTask extends TimerTask {
        @Override
        public void run() {
            try {
                System.out.println("4 begin 运行了！时间为：" + new Date().toLocaleString());
                Thread.sleep(5000);
                System.out.println("4 end   运行了！时间为：" + new Date().toLocaleString());
                runCount++;
                if (runCount == 5) {
                    timer.cancel();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws ParseException {
        MyTask task = new MyTask();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date schedule = sdf.parse("2018-08-18 21:22:00");
        System.out.println("开始了：" + new Date().toLocaleString());
        timer.scheduleAtFixedRate(task, schedule, 3000);
        // 延时的情况的参考的上次任务结束的时间计算的
    }
}
class Run0110 {
    private static Timer timer = new Timer();

    static public class MyTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("1 begin 运行了！时间为：" + new Date().toLocaleString());
            System.out.println("1 end   运行了！时间为：" + new Date().toLocaleString());
        }
    }

    public static void main(String[] args) throws ParseException {
        MyTask task = new MyTask();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date schedule = sdf.parse("2018-08-18 21:25:00");
        System.out.println("开始了：" + new Date().toLocaleString());
        timer.schedule(task, schedule, 5000);
        // 2018-08-18 21:25:00 早于当前时间，中间的任务则取消了
    }
}
class Run0111 {
    private static Timer timer = new Timer();

    static public class MyTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("7 begin 运行了！时间为：" + new Date().toLocaleString());
            System.out.println("7 end   运行了！时间为：" + new Date().toLocaleString());
        }
    }

    public static void main(String[] args) throws ParseException {
        MyTask task = new MyTask();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date schedule = sdf.parse("2018-08-18 21:30:00");
        System.out.println("开始了：" + new Date().toLocaleString());
        timer.scheduleAtFixedRate(task, schedule, 5000);
        // 2018-08-18 21:25:00 早于当前时间，中间的任务则被补充性的执行完毕了
    }
}
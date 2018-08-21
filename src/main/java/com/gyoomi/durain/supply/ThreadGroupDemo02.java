/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.supply;

/**
 * 7.2 线程组
 *     说明：
 *         可以把线程归属到某一个线程组中，线程组中可以有线程对象和线程组。类似于树形结构。
 *     作用；
 *         可以批量管理线程或线程组对象，有效地对象线程或线程组对象进行组织。
 * 7.2.1 线程对象关联线程组：1级关联
 *       说明：
 *           所谓1级关联就是父对象有子对象，但是子对象中不再创建对象，也不包含子孙对象。这样可以提高对象线程的管理。
 *
 * @author Leon
 * @version 2018/8/21 9:13
 */
public class ThreadGroupDemo02 {

    public static void main(String[] args) {
        Runnable runnable = () -> {
            while (true) {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        ThreadGroup group = new ThreadGroup("我的线程组");
        Thread t1 = new Thread(group, runnable);
        Thread t2 = new Thread(group, runnable);
        t1.setName("aaa");
        t2.setName("bbb");
        t1.start();
        t2.start();
        System.out.println("活动的线程组名称：" + group.getName());
        System.out.println("活动的线程数：" + group.activeCount());
    }
}
// ---------------------------------------------
/**
 * 7.2.2 线程对象关联线程组：多级关联
 *       说明：
 *           支持父子孙等无限的嵌套，但是开发中不常用，但是jdk是支持的。
 *       实例：
 *           如下。
 *
 *
 */
class Run0201 {
    public static void main(String[] args) {
        ThreadGroup mainGroup = new ThreadGroup("MainGroup");
        ThreadGroup group = new ThreadGroup(mainGroup, "group");
        Runnable runnable = () -> {
            System.out.println("runMethod!!!");
            try {
                // 线程必须在运行状态下才可以受组管理
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread t1 = new Thread(group, runnable);
        t1.setName("AAA");
        t1.start();
        ThreadGroup[] listGroup = new ThreadGroup[Thread.currentThread().getThreadGroup().activeGroupCount()];
        Thread.currentThread().getThreadGroup().enumerate(listGroup);
        System.out.println("Main线程中共有多少个子线程组：" + listGroup.length + " 名字为：" + listGroup[0].getName());
        Thread[] listThread = new Thread[listGroup[0].activeCount()];
        listGroup[0].enumerate(listThread);
        System.out.println(listThread[0].getName());
    }
}
// --------------------------------------------
/**
 * 7.2.3 线程组自动归属特性
 *       说明：
 *           自动归属就是自动归属到当前线程组中去。
 *       示例：
 *           方法activeGroupCount()是获取当前线程组中子线程组中的数量。
 *       结论：
 *           当创建一个线程组x，如果不指定所属的线程组，则线程组x则自动归属到当前线程对象所属的线程组中。
 *
 */
class Run0202 {
    public static void main(String[] args) {
        System.out.println("A处线程：" + Thread.currentThread().getName() + " 所属线程组名为：" + Thread.currentThread().getThreadGroup().getName() + " 其中线程组中数量是：" + Thread.currentThread().getThreadGroup().activeGroupCount());
        ThreadGroup group = new ThreadGroup("新的组");
        System.out.println("B处线程：" + Thread.currentThread().getName() + " 所属线程组名为：" + Thread.currentThread().getThreadGroup().getName() + " 其中线程组中数量是：" + Thread.currentThread().getThreadGroup().activeGroupCount());
        ThreadGroup[] tgs = new ThreadGroup[Thread.currentThread().getThreadGroup().activeGroupCount()];
        Thread.currentThread().getThreadGroup().enumerate(tgs);
        for (int i = 0; i < tgs.length; i++) {
            System.out.println("第一个线程组的名称是：" + tgs[i].getName());
        }
    }
}
// --------------------------------------------
/**
 * 7.2.4 获取根线程组
 *       说明：
 *           线程main所在线程组名称是：main
 *           main线程main所在线程组的父线程的名称是：system
 *           如果再调，就会抛出异常。NPE
 */
class Run0203 {
    public static void main(String[] args) {
        System.out.println("线程" + Thread.currentThread().getName() + "所在线程组名称是：" + Thread.currentThread().getThreadGroup().getName());
        System.out.println("main线程" + Thread.currentThread().getName() + "所在线程组的父线程的名称是：" + Thread.currentThread().getThreadGroup().getParent().getName());
        // System.out.println("main线程" + Thread.currentThread().getName() + "所在线程组的父线程的父线程组名称是：" + Thread.currentThread().getThreadGroup().getParent().getParent().getName());

    }
}
// --------------------------------------------
/**
 * 7.2.5 线程组里加线程组
 *       说明：
 *           略。
 *
 */
class Run0204 {
    public static void main(String[] args) {

    }
}
// -------------------------------------------
/**
 * 7.2.6 组内线批量停止
 *      说明：
 *          调用ThreadGroup对象的interrupt方法
 *
 */
class Run0205 {
    public static void main(String[] args) throws InterruptedException {
        ThreadGroup group = new ThreadGroup("myGroup");
        for (int i = 0; i < 5; i++) {
            MyThread0205 thread = new MyThread0205(group, "线程" + (i + 1));
            thread.start();
        }
        Thread.sleep(3000);
        group.interrupt();
        System.out.println("调用了group的interrupt方法");
    }
}
class MyThread0205 extends Thread {
    public MyThread0205(ThreadGroup group,String name) {
        super(group, name);
    }
    @Override
    public void run() {
        System.out.println("ThreadName = " + Thread.currentThread().getName() + " 开始死循环了");
        while (!this.isInterrupted()) {}
        System.out.println("结束了ThreadName = " + Thread.currentThread().getName());
    }
}
// -------------------------------------------
/**
 * 7.2.7 递归和非递归取组内对象
 *       使用线程组的enumerate()方法，通过true或false参数来控制
 * 7.2.8 使线程具有有序性
 *       说明：
 *           正常情况下，线程运行在多个任务之前，执行任务的顺序是无序的，但是我们可以通过控制代码的方式来实现其有序性。
 *
 */
class Run0206 {
    public static void main(String[] args) {

    }
}









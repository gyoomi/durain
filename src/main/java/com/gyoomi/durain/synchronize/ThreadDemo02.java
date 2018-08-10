/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.synchronize;

/**
 * Synchronized语句块
 *
 * synchronized方法的弊端
 *     说明：利用sync来声明方法在某些情况下是十分糟糕的。例如线程a调用同步方法来执行一个长时间的任务，
 *           那么线程b必须进行长时间的等待。在这样的情况下，可以使用sync语句块来解决
 *
 *  以下例子很明显：说明用来至少6秒，才执行完这写代码。
 *
 * @author Leon
 * @version 2018/8/9 21:29
 */
public class ThreadDemo02 {
    public static void main(String[] args) throws Exception {
        Task task = new Task();
        MyThread0201 t1 = new MyThread0201(task);
        MyThread0202 t2 = new MyThread0202(task);
        t1.start();
        t2.start();

        Thread.sleep(7000);
        long begin = CommonUtils.beginTime1;
        if (CommonUtils.beginTime2 < CommonUtils.beginTime1) {
            begin = CommonUtils.beginTime2;
        }
        long end = CommonUtils.endTime1;
        if (CommonUtils.endTime2 > CommonUtils.endTime1) {
            end = CommonUtils.endTime2;
        }
        System.out.println("耗时：" + (end - begin));
    }
}

class Task {
    private String getData1;
    private String getData2;
    public  void doLongTask() {
        try {
            System.out.println("task begin");
            Thread.sleep(3000);
            String privateGetData1 = "长时间处理任务 返回值 1 。thread = " + Thread.currentThread().getName();
            String privateGetData2 = "长时间处理任务 返回值 2 。thread = " + Thread.currentThread().getName();
            synchronized (this) {
                getData1 = privateGetData1;
                getData2 = privateGetData2;
            }
            System.out.println(getData1);
            System.out.println(getData2);
            System.out.println("task end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class CommonUtils {
    public static long beginTime1;
    public static long endTime1;
    public static long beginTime2;
    public static long endTime2;
}
class MyThread0201 extends Thread {
    private Task task;
    public MyThread0201(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        super.run();
        CommonUtils.beginTime1 = System.currentTimeMillis();
        task.doLongTask();
        CommonUtils.endTime1 = System.currentTimeMillis();
    }
}
class MyThread0202 extends Thread {
    private Task task;
    public MyThread0202(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        super.run();
        CommonUtils.beginTime2 = System.currentTimeMillis();
        task.doLongTask();
        CommonUtils.endTime2 = System.currentTimeMillis();
    }
}
// -------------------------------------------------------------------------------

/**
 * 同步代码块的使用
 *
 * 下面的例子展示如何使用同步代码块
 * 但是代码执行的效率 并没有得到的 提升，怎么处理？
 *
 */
class Run0202 {
    public static void main(String[] args) {
        ObjectService02 service = new ObjectService02();
        MyThread0203 t1 = new MyThread0203(service);
        MyThread0204 t2 = new MyThread0204(service);
        t1.setName("a");
        t2.setName("b");
        t1.start();
        t2.start();
    }
}
class ObjectService02 {
    public void serviceMethod() {
        try {
            synchronized (this) {
                System.out.println("begin time = " + System.currentTimeMillis());
                Thread.sleep(2000);
                System.out.println("end time = " + System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class MyThread0203 extends Thread {
    private ObjectService02 service;
    public MyThread0203(ObjectService02 service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.serviceMethod();
    }
}
class MyThread0204 extends Thread {
    private ObjectService02 service;
    public MyThread0204(ObjectService02 service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.serviceMethod();
    }
}

// ------------------------------------------------------------------------------------------

/**
 *  使用同步代码块解决同步方法的弊端
 *
 *  代码如上一样，只是稍作修改.
 *  修改后测试发现，虽然时间缩短了，时间加快了。
 *  但是sync块真的是同步的吗？真的持有当前对象锁吗？答案是，下面我们来验证。
 */
class Run0203 {
    public static void main(String[] args) {
    }
}
// ---------------------------------------------------------------------------

/**
 * 一半异步，一半同步
 *
 * 本实现来证明在：sync块中就是同步，不在的就是异步
 * 结论：sync中的代码排队执行
 */
class Run0204 {
    public static void main(String[] args) {
        Task0202 task = new Task0202();
        MyThread0205 t1 = new MyThread0205(task);
        MyThread0206 t2 = new MyThread0206(task);
        t1.start();
        t2.start();
    }
}
class Task0202 {
    public void doLongTask() {
        for (int i = 0; i < 100; i++) {
            System.out.println("async run... thread = " + Thread.currentThread().getName() + " i = " + (i + 1));
        }
        System.out.println("");
        synchronized (this) {
            for (int i = 0; i < 100; i++) {
                System.out.println("sync run... thread = " + Thread.currentThread().getName() + " i = " + (i + 1));
            }
        }
    }
}
class MyThread0205 extends Thread {
    private Task0202 task;
    public MyThread0205(Task0202 task) {
        this.task = task;
    }

    @Override
    public void run() {
        super.run();
        task.doLongTask();
    }
}
class MyThread0206 extends Thread {
    private Task0202 task;
    public MyThread0206(Task0202 task) {
        this.task = task;
    }

    @Override
    public void run() {
        super.run();
        task.doLongTask();
    }
}
// ------------------------------------------------------------------------

/**
 * synchronized块之间的同步性
 *     说明：当一个线程访问一个对象的sync(this)块时，其他线程对同一个object的sync(this)块都将被阻塞。这说明sync使用的是同一个“对象监视器”。
 *
 */
class Run0205 {
    public static void main(String[] args) {
        ObjectService0201 service = new ObjectService0201();
        MyThread0207 t1 = new MyThread0207(service);
        MyThread0208 t2 = new MyThread0208(service);
        t1.setName("a");
        t2.setName("b");
        t1.start();
        t2.start();
    }
}
class ObjectService0201 {
    public void serviceA() {
        synchronized (this) {
            try {
                System.out.println("serviceA start . time = " + System.currentTimeMillis());
                Thread.sleep(2000);
                System.out.println("serviceA end . time = " + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public void serviceB() {
        synchronized (this) {
            System.out.println("serviceB start . time = " + System.currentTimeMillis());
            System.out.println("serviceB end . time = " + System.currentTimeMillis());
        }
    }
}
class MyThread0207 extends Thread {
    private ObjectService0201 service;
    public MyThread0207(ObjectService0201 service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.serviceA();
    }
}
class MyThread0208 extends Thread {
    private ObjectService0201 service;
    public MyThread0208(ObjectService0201 service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.serviceB();
    }
}
// ---------------------------------------------------------------------------------
/**
 * 验证sync(this)代码块锁定的是当前对象
 *
 */
class Run0206 {
    public static void main(String[] args) {
        Task0203 task = new Task0203();
        MyThread0209 t1 = new MyThread0209(task);
        MyThread0210 t2 = new MyThread0210(task);
        t1.setName("A");
        t2.setName("B");
        t1.start();
        t2.start();
    }
}
class Task0203 {
    public void doOtherMethod() {
        synchronized (this) {
            System.out.println("==========================doOtherMethod====");
        }
    }
    public void doLongTask() {
        synchronized (this) {
            for (int i = 0; i < 1000; i++) {
                System.out.println("sync doLongTask thread = " + Thread.currentThread().getName() + " i = " + (i + 1));
            }
        }
    }
}
class MyThread0209 extends Thread {
    private Task0203 task;
    public MyThread0209(Task0203 task) {
        this.task = task;
    }
    @Override
    public void run() {
        super.run();
        task.doLongTask();
    }
}
class MyThread0210 extends Thread {
    private Task0203 task;
    public MyThread0210(Task0203 task) {
        this.task = task;
    }
    @Override
    public void run() {
        super.run();
        task.doOtherMethod();
    }
}
// ---------------------------------------------------------------------------------------------

/**
 * 将任意对象作为对象监视器
 * 说明：
 *     多个线程调用同一个对象中的不同的sync方法或sync(this)同步块，调用的效果就是按顺序执行，也就是同步的、阻塞的。
 *     这说明sync方法或sync(this)同步块有两个作用：
 *
 * Java支持任意对象作为对象监视器。这个任意对象可以任意实例变量及方法的参数。使用格式是：
 *    synchronized(非this对象)
 *
 *    结论：当持有“对象监视器”为同一个对象的前提下，同一时间只有一个线程可以执行synchronized(非this对象)中的代码
 *
 *
 *    下面案例证明结论：
 *       1.当多个线程持有的对象监视器为同一对象时，同一时间只有一个线程可以执行sync(非this对象)中的代码块
 *    锁非this对象有一个优点：
 *        当一个类中有很多synchronized方法时，，这是虽然可以实现同步，阻塞，但是效率不高。
 *        但是如果使用同步代码块锁非this对象，则synchronized(非this)代码块和同步方法是异步的，不与同步代码块抢夺this锁。
 *        证明见下一个小结。
 */
class Run0207 {
    public static void main(String[] args) {
        UserService02 service = new UserService02();
        MyThread0211 t1 = new MyThread0211(service);
        MyThread0212 t2 = new MyThread0212(service);
        t1.setName("A");
        t2.setName("B");
        t1.start();
        t2.start();
    }
}
class UserService02 {
    private String username;
    private String password;
    private String anyThing = new String();

    public void setUsernamePassword(String username, String password) {
        try {
            synchronized (anyThing) {
                System.out.println("thread = " + Thread.currentThread().getName() + " 在" + System.currentTimeMillis() + "进入了同步块");
                this.username = username;
                Thread.sleep(3000);
                this.password = password;
                System.out.println("thread = " + Thread.currentThread().getName() + " 在" + System.currentTimeMillis() + "离开了同步块");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class MyThread0211 extends Thread {
    private UserService02 service;
    public MyThread0211(UserService02 service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.setUsernamePassword("a", "aa");
    }
}
class MyThread0212 extends Thread {
    private UserService02 service;
    public MyThread0212(UserService02 service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.setUsernamePassword("b", "bb");
    }
}
// -----------------------------------------------------------------------------
/**
 * 证明上个小结内容：略
 */
class Run0208 {
    public static void main(String[] args) {

    }
}
// --------------------------------------------------------------------------------



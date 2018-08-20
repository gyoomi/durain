/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.singletonandthread;

import java.io.*;

/**
 * 6.单例模式和多线程
 *   说明：本章主要考虑的内容，就是如何使单例模式在多线程环境下是安全的、正确的。
 *
 * 6.1 立即加载/饿汉模式
 *
 * @author Leon
 * @version 2018/8/20 16:44
 */
public class Demo01 {
    public static void main(String[] args) {
        Runnable runnable = () -> {
            System.out.println("threadName：" + Thread.currentThread().getName() + "  " + MyObject0101.getInstance().hashCode());
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);
        t1.start();
        t2.start();
        t3.start();
    }
}
class MyObject0101 {
    private static MyObject0101 obj = new MyObject0101();

    private MyObject0101() {}

    public static MyObject0101 getInstance() {
        // 方法没有加同步，容易出现线程安全问题
        // 只能由一个实例变量，不能有其他的实例变量
        return obj;
    }
}
// ----------------------------------------------------
/**
 * 6.2 延迟加载/懒汉模式
 *     1.延迟加载解析
 *     2.懒汉模式的缺点
 *       说明：多个线程会得到不同的对象。破坏了单例模式。
 *     3.上面等我问题解决
 *       1)synchronized关键字，实现get方法同步
 *       2）效率低
 *     4.使用同步代码块，效果也是一样的。效率一样低下。
 *     5.使用DCL双重检查机制解决单例和效率问题
 *
 */
class Run0101 {
    public static void main(String[] args) {
        Runnable runnable = () -> {
            System.out.println(MyObject0102.getInstance().hashCode());
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);
        t1.start();
        t2.start();
        t3.start();
    }
}
class MyObject0102 {
    private static MyObject0102 obj;
    private MyObject0102() {}
    public synchronized static MyObject0102 getInstance() {
        try {
            if (obj == null) {
                // 模拟一些业务代码
                Thread.sleep(2000);
                synchronized (MyObject0102.class) {
                    if (obj == null) {
                        obj = new MyObject0102();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
// ---------------------------------------------------
/**
 * 6.3 使用静态内置类来实现单例模式
 *     说明：dcl双重检查机制可以解决多线程下单例模式的问题，同样使用静态内置类也可以解决。
 *
 *
 */
class Run0102 {
    public static void main(String[] args) {
        Runnable runnable = () -> System.out.println(MyObject0103.getInstance().hashCode());
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);
        t1.start();
        t2.start();
        t3.start();
    }
}
class MyObject0103 {
    private MyObject0103() {}

    public static MyObject0103 getInstance() {
        return MyObjectHandler.obj;
    }

    public static class MyObjectHandler {
        private static MyObject0103 obj = new MyObject0103();
    }
}
// ---------------------------------------------------
/**
 * 6.4 序列化和反序列化单例模式的实现
 *     说明：
 *         静态内置类可以达到线程安全的，但是遇到序列化对象时，其结果创建的还是多例的。
 *     结果：
 *     1480010240
 *     ----------------------------------------------
 *     81628611
 *     打印的并不是一个对象
 *     解决方法：
 *     在MyObject0104类中添加readResolve()方法
 *
 */
class Run0103 {
    public static void main(String[] args) {
        MyObject0104 instance = MyObject0104.getInstance();
        try (
                FileOutputStream fos = new FileOutputStream("D://myobject.txt");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                FileInputStream fis = new FileInputStream("D://myobject.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            oos.writeObject(instance);
            System.out.println(instance.hashCode());
            System.out.println("----------------------------------------------");
            MyObject0104 instance2 = (MyObject0104) ois.readObject();
            System.out.println(instance2.hashCode());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
class MyObject0104 implements Serializable {
    private MyObject0104() {}
    public static MyObject0104 getInstance() {
        return MyObjectHandler.obj;
    }
    protected Object readResolve() throws ObjectStreamException {
        System.out.println("readResolve调用了");
        return MyObjectHandler.obj;
    }

    public static class MyObjectHandler {
        private static MyObject0104 obj = new MyObject0104();
    }


}
// --------------------------------------------------
/**
 * 其他：父类Ｂ静态代码块->子类Ａ静态代码块->父类Ｂ非静态代码块->父类Ｂ构造函数->子类Ａ非静态代码块->子类Ａ构造函数
 * 6.5 使用static代码块来实现单例模式
 *     说明：静态代码块在使用类的时候就已经执行了，所以可以应用静态代码块这个特性来实现单例模式。
 *     结论：
 *         135417039
 *         135417039
 *         135417039
 *         135417039
 *
 *
 */
class Run0105 {
    public static void main(String[] args) {
        Runnable runnable = () -> System.out.println(MyObject0105.getInstance().hashCode());
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);
        Thread t4 = new Thread(runnable);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
class MyObject0105 {
    private MyObject0105() {}
    private static MyObject0105 obj;
    public static MyObject0105 getInstance() {
        return obj;
    }
    static {
        obj = new MyObject0105();
    }
}
// --------------------------------------------------
/**
 * 6.6 使用枚举数据类型来实现单例模式
 *     说明：
 *         枚举enum和静态代码块相似，在使用枚举类的时候构造函数会被调用，也可以使用这个特性来实现单例模式。
 */
class Run0106 {
    public static void main(String[] args) {
        Runnable runnable = () -> System.out.println(MyObject0106.MY.getStr().hashCode());
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);
        t1.start();
        t2.start();
        t3.start();
    }
}
enum MyObject0106 {
    MY;
    private String str;
    private MyObject0106() {
        str = new String();
    }
    public String getStr() {
        return str;
    }
}
// -------------------------------------------
/**
 * 6.7 完善使用enum类实现单例模式
 *     说明：
 *         前一章节，虽然实现了单例模式，但是违反了职能单一原则。
 *
 *
 */
class Run0107 {
    public static void main(String[] args) {
        Runnable runnable = () -> System.out.println(MyObject0107.getStr().hashCode());
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);
        Thread t4 = new Thread(runnable);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
class MyObject0107 {
    public enum MyEnumSingleton {
        MY;
        private String str;
        private MyEnumSingleton() {
            str = new String();
        }
        public String getStr() {
            return str;
        }
    }

    public static String getStr() {
        return MyEnumSingleton.MY.getStr();
    }
}





















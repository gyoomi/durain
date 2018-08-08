/**
 * Copyright © 2018, LeonKeh
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * 释放锁的不良后果
 *
 * 产生原因：由于stop()会释放锁给数据造成不一致的后果。
 * 这样就会导致处理的数据的遭到破坏，最终导致程序的执行流程报错。
 *
 * 测试结果：
 *      b:aa
 * 原因总结：
 *     stop已过期，，在功能是有缺陷的，不推荐使用
 * @author Leon
 * @version 2018/8/8 20:48
 */
public class ThreadDemo17 {

    public static void main(String[] args) {
        try {
            SynchronizedObject object = new SynchronizedObject();
            MyThread18 thread = new MyThread18(object);
            thread.start();
            Thread.sleep(5000);
            thread.stop();
            System.out.println(object.getUsername() + ":" + object.getPassword());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

class SynchronizedObject {
    private String username = "a";
    private String password = "aa";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public synchronized void printString(String username, String password) {
        try {
            this.username = username;
            Thread.sleep(20000);
            this.password = password;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyThread18 extends Thread {

    private SynchronizedObject object;

    public MyThread18(SynchronizedObject object) {
        this.object = object;
    }

    @Override
    public void run() {
        object.printString("b", "bb");
    }
}

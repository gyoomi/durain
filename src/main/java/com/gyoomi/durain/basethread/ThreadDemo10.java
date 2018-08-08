/**
 * Copyright © 2018, TaoDing
 * <p>
 * All Rights Reserved.
 */

package com.gyoomi.durain.basethread;

/**
 * getId()方法
 * 作用是：获取线程的唯一标识
 * @author Leon
 * @version 2018/8/8 11:36
 */
public class ThreadDemo10 {

    public static void main(String[] args) {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + ":" + thread.getId() + ":" + thread.getState());
    }
}

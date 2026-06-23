/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practice;

/**
 *
 * @author AARON EKWARU
 */
public class SleepingThread1 extends Thread {
    SleepingThread1(String s){ super(s); } 
    public void run() { 
        System.out.println(Thread.currentThread().getName()+" Started"); 
        try{ 
        SleepingThread1.sleep(1500); 
        System.out.println(Thread.currentThread().getName()+" Sleeping.."); 
    }
        catch(InterruptedException ie){ 
        System.out.println(ie); 
    } 
        System.out.println(Thread.currentThread().getName()+" Finished"); 
    }
    public static void main(String[] args) {
        SleepingThread1 ob1=new SleepingThread1("First thread"); 
        SleepingThread1 ob2=new SleepingThread1("Second thread");
        System.out.println(ob1.getName()+" State: "+ ob1.getState()); 
        ob1.start(); 
        System.out.println(ob1.getName()+" State: "+ ob1.getState());
        System.out.println(ob2.getName()+" State: "+ ob2.getState()); 
        ob2.start(); 
        System.out.println(ob2.getName()+" State: "+ ob2.getState()); 
    } 
    }    
}

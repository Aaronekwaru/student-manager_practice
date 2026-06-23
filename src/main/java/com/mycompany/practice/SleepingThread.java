/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practice;

/**
 *
 * @author AARON EKWARU
 */
public class SleepingThread extends Thread { 
        SleepingThread(String s){ super(s); } 
        public void run(){ 
            System.out.println(Thread.currentThread().getName()+" Started"); 
        try{ 
            SleepingThread.sleep(1000); 
        }
        catch(InterruptedException ie){ 
            System.out.println(ie); 
        } 
        System.out.println(Thread.currentThread().getName()+" Finished"); 
    } 
    public static void main(String[] args){
        SleepingThread ob=new SleepingThread("First Thread"); 
        SleepingThread ob1=new SleepingThread("Second Thread"); 
        ob.start(); 
        ob1.start(); 
        } 
    }  


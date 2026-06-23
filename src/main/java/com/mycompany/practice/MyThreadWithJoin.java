/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.practice;

/**
 *
 * @author AARON EKWARU
 */
   // public class MyThreadisAlive extends Thread { 
        /*public void run() { 
            System.out.println("r1 "); 
        try { 
            Thread.sleep(500); 
        } 
        //catch(InterruptedException ie) {/* do something*/ } 
            //System.out.println("r2 "); 
        //}*/
        /*public static void main(String[] args){ 
            MyThreadisAlive t1=new MyThreadisAlive(); 
            MyThreadisAlive t2=new MyThreadisAlive(); 
            t1.start(); 
            t2.start(); 
            System.out.println(t1.isAlive()); 
            System.out.println(t2.isAlive()); 
    }*/
//}

    public class MyThreadWithJoin extends Thread{ 
       public void run() { 
            System.out.println("Thread1 "); 
       try{ Thread.sleep(600); 
       }
       catch(InterruptedExceptionie){} 
            System.out.println("Thread2"); 
} 
       public static void main(String[] args){ 
            MyThreadWithJoinob1=newMyThreadWithJoin();
            MyThreadWithJoinob2=newMyThreadWithJoin(); 
            ob1.start(); 
       try{ 
            ob1.join(); /*Waiting for t1 to finish*/ }
       catch(InterruptedException ie){} 
            ob2.start(); 
}   }

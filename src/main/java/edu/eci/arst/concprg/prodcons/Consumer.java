/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;

/**
 *
 * @author hcadavid
 */
public class Consumer extends Thread{
    
    private static Queue<Integer> queue;
    private static Object lock;

    private long elementsRemoved;
    
    
    public Consumer(Queue<Integer> queue){
        this.queue=queue; lock = new Object(); elementsRemoved=0;
    }

    public static Queue<Integer> getQueue(){
        return queue;
    }

    public void notifyThread(){
        if (queue.size()%14==0) lock.notifyAll();
    }
    
    @Override
    public void run() {

        while (elementsRemoved < Producer.getStockLimit() ) {
                int elem=queue.poll();
                elementsRemoved++;
                System.out.println("Consumer consumes "+elem);
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                }
        }
    }
}

package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback=null;
    
    private int health;
    
    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());

    private static AtomicBoolean paused;
    private static Object lock;

    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb) {
        super(name);
        this.updateCallback=ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue=defaultDamageValue;
        paused = new AtomicBoolean(false);
        lock = new Object();
    }

    public static void pauseNotify() {
        lock.notifyAll();
    }

    public void run() {
        while (true) {
            if (ControlFrame.getPaused()) {
                pause();
            } else {
                while (paused.get()) {
                    try {
                        paused.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Immortal im;
                int myIndex = immortalsPopulation.indexOf(this);
                int nextFighterIndex = r.nextInt(immortalsPopulation.size());
                //avoid self-fight
                if (nextFighterIndex == myIndex) {
                    nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
                }
                im = immortalsPopulation.get(nextFighterIndex);
                this.fight(im);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void pause(){
        synchronized (ControlFrame.getLock()){
            ControlFrame.notifyThreadLock();
        } synchronized (lock){
            if (ControlFrame.getPaused()){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void fight(Immortal i2) {
        boolean alive = false;
        if (i2.getHealth() > 0) {
            synchronized (i2) {
                i2.changeHealth(i2.getHealth() - defaultDamageValue);
                alive = true;
            }
        }
        synchronized (this) {
             if(alive) {
                 if (!ControlFrame.pass()) this.health += defaultDamageValue;
                 updateCallback.processReport("Fight: " + this + " vs " + i2 + "\n");
             }
             else {
                    updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
             }
        }
    }

    public void changeHealth(int v) {
        health = v;
    }

    public int getHealth() {
        return health;
    }

    public static Object getLock(){
        return lock;
    }

    public static boolean getPausedLock(){
        return paused.get();
    }

    @Override
    public String toString() {
        return name + "[" + health + "]";
    }

}

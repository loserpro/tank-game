package com.tankgame;

import java.util.Vector;

public class BossTank extends Tank implements Runnable{
    Shoot shoot = null;
    Vector shootsboss = new Vector();
    boolean isLive = true;

    public BossTank(int x,int y){
        super(x,y);
    }
    public void run(){
        while (true) {
            //这里我们判断如果shots size（）=0，放入到shots集合，并启
            if (isLive && shootsboss.size() == 0) {//这里可设置地方坦克子弹
                Shoot s = null;
                //判断坦克的方向，创建对应的子弹
                switch (getDirect()) {
                    case 0:
                        s = new Shoot(getX() + 20, getY(), 0);
                        break;
                    case 1:
                        s = new Shoot(getX() + 60, getY() + 20, 1);
                        break;
                    case 2:
                        s = new Shoot(getX() + 10, getY() + 60, 2);
                        break;
                    case 3://向左
                        s = new Shoot(getX(), getY() + 20, 3);
                        break;
                }
                shootsboss.add(s);
                new Thread(s).start();
            }
            //根据坦克的方向来继续移动
            switch (getDirect()) {
                case 0://向上
                    //让坦克保持一个方向走30步
                    for (int i = 0; i < 30; i++) {
                        if (getY() > 0) {
                            moveUp();
                        }
                        //休眠50毫秒
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 1://向右
                    for (int i = 0; i < 30; i++) {
                        if (getX() + 60 < 1000) {
                            moveRight();
                        }
                        //休眠50毫秒
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 2://向下
                    for (int i = 0; i < 30; i++) {
                        if (getY() + 60 < 750) {
                            moveDown();
                        }
                        //休眠50毫秒
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 3://向左
                    for (int i = 0; i < 30; i++) {
                        if (getX() > 0) {
                            moveLeft();
                        }
                        //休眠50毫秒
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
            }
            //然后随机改变坦克方向 0-3
            setDirect((int) (Math.random() * 4));
            //写并发程序，考虑清楚什么时候退出
            if (!isLive) {
                break;
            }
        }
    }
}

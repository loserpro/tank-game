package com.tankgame;

import java.util.Vector;

//自己的坦克
public class Hero extends Tank {
    Shoot shoot = null;
    //可以发射多颗子弹
    Vector<Shoot> shoots = new Vector<>();
    public Hero(int x, int y) {
        super(x, y);
    }

    //射击
    public void shootEnemyTank(int ShountCount){
        //作弊可关闭,调整子弹数量
        if(shoots.size() == ShountCount){
            return;
        }
        //创建shoot对象，根据当前Hero对象的位置和方向来创建Shoot
        switch (getDirect()){//得到Hero对象方向
            case 0: //向上
                shoot = new Shoot(getX() + 20, getY(), 0);
                break;
            case 1: //向上
                shoot = new Shoot(getX() + 60, getY() + 20, 1);
                break;
            case 2: //向上
                shoot = new Shoot(getX() + 20, getY() + 60, 2);
                break;
            case 3: //向上
                shoot = new Shoot(getX(), getY() + 20, 3);
                break;
        }

        //把新创建的shoot放入到shoots
        shoots.add(shoot);
        //启动shoot线程
        new Thread(shoot).start();
    }
}

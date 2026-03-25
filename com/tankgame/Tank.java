package com.tankgame;

public class Tank {
    private int x; //坦克的横坐标
    private int y; //坦克的纵坐标
    private int direct;//坦克方向0 1 2 3
    private int speed = 5; //控制坦克速度
    boolean isLive = true;
    //上下左右移动方法
    public void moveUp(){
            y -= speed;
    }
    public void moveRight(){
        x+=speed;
    }
    public void moveDown(){
        y+=speed;
    }
    public void moveLeft(){
        x-=speed;
    }

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getDirect() {return direct;}
    public int getSpeed() {return speed;}
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setDirect(int direct) { this.direct = direct;}
    public void setSpeed(int speed) { this.speed = speed;}
}

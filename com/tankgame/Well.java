package com.tankgame;

//墙体类
public class Well {
    private int health = 1;
    private int height;
    private int width;
    public int x;
    public int y;

    public Well(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getHealth() {
        return health;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
}

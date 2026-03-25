package com.tankgame;



import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

//坦克大战绘图区域
//为了实现监听，实现keyListener
//为了让Panel不停的重绘子弹，需要将MyPanel实现Runnable，当做一个线程使用
public class MyPanel extends JPanel implements KeyListener,Runnable {
    //定义碰撞属性
    private boolean collsionLive = true;
    //定义总得分
    public int finScore = 0;
    //定义我方坦克血量
    public int blood = 100;
    public int bossBoold = 50; //定义boss血量
    //定义加速
    public boolean accelerate = false;
    //定义我的坦克
    Hero hero = null;
    //定义Boss坦克
    BossTank boss = null;
    //定义敌人坦克，放入Vector（集合）中
    Vector<EnemyTank> enemyTanks = new Vector<>();
    //定义墙体类，放入到Vector（集合）中
    Vector<Well> wells = new Vector<>();
    //定义一个Vetor，用于存放炸弹
    //说明，当子弹击中坦克时，加入一个Bomb对象到bombs
    Vector<Bomb> bombs = new Vector<>();
    int enemyTankSize = 100;//敌方坦克数量
    int wellSize = 16; //墙体数量
    //定义三张图片，用于显示爆战效果
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;

    public MyPanel() {
        hero = new Hero(500,600);//初始化自己的坦克

        //始化boss坦克
        boss = new BossTank(500 ,100);
        boss.setDirect(2);
        //启动敌人坦克线程，使boss坦克动起来
        new Thread(boss).start();
        Shoot shoot1 = new Shoot(boss.getX() + 20,boss.getY() + 60,boss.getDirect());
        boss.shootsboss.add(shoot1);
        new Thread(shoot1).start();


//        hero.setSpeed(5);
        //初使化敌人坦克
        for(int i =0;i<enemyTankSize;i++){
            EnemyTank enemyTank = new EnemyTank((100 * (i + 1)),0);
            enemyTank.setDirect(2);
            //启动敌人坦克线程，使敌方坦克动起来
            new Thread(enemyTank).start();
            //给该enemyTank加入一颗子弹
            Shoot shoot = new Shoot(enemyTank.getX() + 20,enemyTank.getY() + 60,enemyTank.getDirect());
            //加入enemyTank的vector成员
            enemyTank.shoots.add(shoot);
            //启动shoot对象
            new Thread(shoot).start();
            //加入
            enemyTanks.add(enemyTank);
        }
        //促使化墙体
        for(int i =0;i<wellSize;i++){
            Well well = new Well((50 * (i + 1)),500);
            //加入
            wells.add(well);
        }

        //初始化图片对象
        image1 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("images/bomb1.png"));
        image2 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("images/bomb2.png"));
        image3 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("images/bomb3.png"));
    }
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.black);
        g.fillRect(0,0,1000,750);//填充矩形，默认黑色
        if(hero!=null && hero.isLive){
            //画出自己坦克-封装方法
            drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 1);
        }



        //画出墙体
//        drawWell(g,0,400,50,60,1);
//        drawWell(g,60,400,50,60,1);

        //画出墙体，遍历集合
        for(int i =0;i<wells.size();i++){
            Well well = wells.get(i);
            if(well.getHealth() == 1){
                drawWell(g,well.getX(),well.getY(),50,60,1);
            }
        }

        //画草丛
        drawWell(g,0,200,70,70,4);
        drawWell(g,70,200,70,70,4);
        drawWell(g,140,200,70,70,4);
        drawWell(g,210,200,70,70,4);
        drawWell(g,280,200,70,70,4);
        drawWell(g,350,200,70,70,4);
        drawWell(g,420,200,70,70,4);
        drawWell(g,490,200,70,70,4);
        drawWell(g,560,200,70,70,4);
        drawWell(g,630,200,70,70,4);
        drawWell(g,700,200,70,70,4);
        drawWell(g,770,200,70,70,4);
        drawWell(g,840,200,70,70,4);
        drawWell(g,910,200,70,70,4);



        //画出hero射击的子单个
//        if(hero.shoot != null && hero.shoot.isLive == true){
//            System.out.println("子弹被绘制....");
// //            g.fill3DRect(hero.shoot.x, hero.shoot.y, 1, 1,false);
//            g.draw3DRect(hero.shoot.x, hero.shoot.y, 1, 1,false);
//        }
        for(int i = 0;i < hero.shoots.size();i++){
            Shoot shoot = hero.shoots.get(i);
            if(shoot != null && shoot.isLive){
                g.draw3DRect(shoot.x, shoot.y, 1, 2,false);
            }else {//如果该shoot对象已经无效，就从shoots集合中拿掉
                hero.shoots.remove(shoot);
            }
        }
        //如果bombs集合中有对象，就画出
        for(int i = 0;i < bombs.size();i++){
            //取出炸弹
            Bomb bomb = bombs.get(i);
            //根据当前这个bomb对象的life值去画出对应的图片
            if(bomb.life > 6){
                g.drawImage(image1, bomb.x, bomb.y, 60, 60, this);
            }else if(bomb.life > 3){
                g.drawImage(image2, bomb.x, bomb.y, 60, 60, this);
            }else {
                g.drawImage(image3, bomb.x, bomb.y, 60, 60, this);
            }
            //让这个炸弹的生命值减少
            bomb.lifeDown();
            //如果bomb life为0，就从bombs的集合中删除
            if(bomb.life == 0){
                bombs.remove(bomb);
            }
        }

        //绘制得分
        drawScore(g);
//        drawWell(g,int 100,int 100,int width, int height,int type);
        //绘制boss
        if(boss.isLive){
//            System.out.println(boss.isLive);
            drawBossTank(boss.getX(),boss.getY(),g,boss.getDirect());
            //画出子弹
            for(int i = 0;i < boss.shootsboss.size();i++){
                Shoot shoot = (Shoot) boss.shootsboss.get(i);
                //绘制
                if(shoot.isLive){
                    g.draw3DRect(shoot.x, shoot.y, 1, 1,false);
                }else {
                    boss.shootsboss.remove(shoot);
                }
            }
        }

        //画出敌人坦克，遍历Vector（集合）
        for(int i = 0; i < enemyTanks.size();i++){
            //从Vector取出坦克
            EnemyTank enemyTank = enemyTanks.get(i);
            //判断当前坦克是否还存活
            if(enemyTank.isLive){//当敌人坦克是存活的，才画出该坦克
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirect(), 0);
                //画出enemytank所有子弹
                for(int j = 0; j < enemyTank.shoots.size(); j++){
                    //取出子弹
                    Shoot shoot = enemyTank.shoots.get(j);
                    //绘制
                    if (shoot.isLive) {//islive == true
                        g.draw3DRect(shoot.x, shoot.y, 1, 1,false);
                    }else {
                        //从vector移除
                        enemyTank.shoots.remove(shoot);
                    }
                }
            }
        }

    }

    //绘制墙体
    public void drawWell(Graphics g,int x,int y,int width, int height,int type) {
        // 根据墙体类型设置不同的颜色和样式
        switch (type) {
            case 1:
                g.setColor(new Color(139, 69, 19)); // 砖红色
                g.fillRect(x, y, width, height);
                // 绘制砖块纹理
                g.setColor(new Color(160, 82, 45));
                for (int i = 0; i < width; i += 10) {
                    for (int j = 0; j < height; j += 5) {
                        if ((i/10 + j/5) % 2 == 0) {
                            g.fillRect(x + i, y + j, 10, 5);
                        }
                    }
                }
                break;

            case 2:
                g.setColor(new Color(128, 128, 128)); // 灰色
                g.fillRect(x, y, width, height);
                // 绘制金属纹理
                g.setColor(new Color(169, 169, 169));
                for (int i = 0; i < width; i += 8) {
                    g.drawLine(x + i, y, x + i, y + height);
                }
                break;

            case 3:
                g.setColor(new Color(0, 0, 205, 128)); // 半透明蓝色
                g.fillRect(x, y, width, height);
                // 绘制水波效果
                g.setColor(new Color(30, 144, 255, 192));
                for (int i = 0; i < height; i += 4) {
                    int offset = (int)(Math.sin(System.currentTimeMillis() * 0.001 + i * 0.2) * 3);
                    g.drawLine(x, y + i, x + width, y + i);
                }
                break;

            case 4:
                g.setColor(new Color(0, 100, 0, 64)); // 半透明绿色
                g.fillRect(x, y, width, height);
                // 绘制草丛纹理
                g.setColor(new Color(34, 139, 34, 128));
                for (int i = 0; i < width; i += 5) {
                    for (int j = 0; j < height; j += 5) {
                        g.fillOval(x + i, y + j, 5, 5);
                    }
                }
                break;

            case 5:
                g.setColor(new Color(220, 230, 250, 160)); // 半透明浅蓝色
                g.fillRect(x, y, width, height);
                // 绘制冰裂纹
                g.setColor(new Color(173, 216, 230, 192));
                g.drawLine(x, y, x + width, y + height);
                g.drawLine(x + width, y, x, y + height);
                break;
        }
    }
    //墙体绘制结束
    //绘制得分
    public void drawScore(Graphics g) {

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("player: TOM" , 20, 20);
        g.drawString("Score: " + finScore, 20, 40);
        g.drawString("Blood: " + blood, 20, 60);
        g.drawString("bulletCount: " + shootCount, 20, 80);

        g.drawString("EnemyInfo", 20, 120);
        g.drawString("bossBoold: " + bossBoold, 20, 140);

        g.drawString("W,A,S,D move.SPACNE speedUp.ENTER slowDown." , 200, 20);
        g.drawString("P increaseAmmo.M decreaseAmmo.H myHp.E enemyHp" , 200, 40);
    }

    //x坦克左上角x坐标
    //y坦克左上角y坐标
    //direct坦克方向
    //type坦克类型
    //编写方法，画出坦克
    public void drawTank(int x, int y, Graphics g, int direct, int type) {
        //根据不同类型的坦克，设置不同颜色
        switch (type) {
            case 0://敌人的坦克
                g.setColor(Color.cyan); //lightGray
                break;
            case 1://我们的坦克
                g.setColor(Color.yellow);
                break;
        }

        //根据坦克方向，来绘制坦克
        //direct 表示方向（0：向上 1向右 2向下 3向左）
        switch (direct) {
            case 0://表示向上
                g.fill3DRect(x, y, 10,60,false);//画出坦克左边的轮子
                g.fill3DRect(x+30, y, 10,60,false);//画出坦克右边的轮子
                g.fill3DRect(x+10,y+10, 20, 40,false);//画出坦克盖子
                g.fillOval(x+10, y+20, 20, 20);//画出圆形盖子
                g.drawLine(x+20, y+30, x+20, y);//画出炮筒
                break;
            case 1://表示向右
                g.fill3DRect(x, y, 60,10,false);//画出坦克左边的轮子
                g.fill3DRect(x, y+30, 60,10,false);//画出坦克右边的轮子
                g.fill3DRect(x+10,y+10, 40, 20,false);//画出坦克盖子
                g.fillOval(x+20, y+10, 20, 20);//画出圆形盖子
                g.drawLine(x+30, y+20, x+60, y+20);//画出炮筒
                break;
            case 2://表示向下
                g.fill3DRect(x, y, 10,60,false);//画出坦克左边的轮子
                g.fill3DRect(x+30, y, 10,60,false);//画出坦克右边的轮子
                g.fill3DRect(x+10,y+10, 20, 40,false);//画出坦克盖子
                g.fillOval(x+10, y+20, 20, 20);//画出圆形盖子
                g.drawLine(x+20, y+30, x+20, y+60);//画出炮筒
                break;
            case 3://表示向左
                g.fill3DRect(x, y, 60,10,false);//画出坦克左边的轮子
                g.fill3DRect(x, y+30, 60,10,false);//画出坦克右边的轮子
                g.fill3DRect(x+10,y+10, 40, 20,false);//画出坦克盖子
                g.fillOval(x+20, y+10, 20, 20);//画出圆形盖子
                g.drawLine(x+30, y+20, x, y+20);//画出炮筒
                break;
            default:
                System.out.println("暂时没有处理");
        }
    }

    //绘制boss坦克
    public void drawBossTank(int x, int y, Graphics g, int direct) {
        //根据不同类型的坦克，设置不同颜色
                g.setColor(Color.lightGray);
        //根据坦克方向，来绘制坦克
        //direct 表示方向（0：向上 1向右 2向下 3向左）
        switch (direct) {
            case 0://表示向上
                g.fill3DRect(x, y, 10,60,false);//画出坦克左边的轮子
                g.fill3DRect(x+30, y, 10,60,false);//画出坦克右边的轮子
                g.fill3DRect(x+10,y+10, 20, 40,false);//画出坦克盖子
                g.fillOval(x+10, y+20, 20, 20);//画出圆形盖子
                g.drawLine(x+20, y+30, x+20, y);//画出炮筒
                break;
            case 1://表示向右
                g.fill3DRect(x, y, 60,10,false);//画出坦克左边的轮子
                g.fill3DRect(x, y+30, 60,10,false);//画出坦克右边的轮子
                g.fill3DRect(x+10,y+10, 40, 20,false);//画出坦克盖子
                g.fillOval(x+20, y+10, 20, 20);//画出圆形盖子
                g.drawLine(x+30, y+20, x+60, y+20);//画出炮筒
                break;
            case 2://表示向下
                g.fill3DRect(x, y, 10,60,false);//画出坦克左边的轮子
                g.fill3DRect(x+30, y, 10,60,false);//画出坦克右边的轮子
                g.fill3DRect(x+10,y+10, 20, 40,false);//画出坦克盖子
                g.fillOval(x+10, y+20, 20, 20);//画出圆形盖子
                g.drawLine(x+20, y+30, x+20, y+60);//画出炮筒
                break;
            case 3://表示向左
                g.fill3DRect(x, y, 60,10,false);//画出坦克左边的轮子
                g.fill3DRect(x, y+30, 60,10,false);//画出坦克右边的轮子
                g.fill3DRect(x+10,y+10, 40, 20,false);//画出坦克盖子
                g.fillOval(x+20, y+10, 20, 20);//画出圆形盖子
                g.drawLine(x+30, y+20, x, y+20);//画出炮筒
                break;
            default:
                System.out.println("暂时没有处理");
        }
    }

    //多颗子弹击中敌人坦克
    public void hitEnemyTank(){
        //遍历子弹
        for(int j = 0;j < hero.shoots.size();j++){
            Shoot shoot = hero.shoots.get(j);
            if(shoot != null & shoot.isLive){//当我的子弹还在存活
                //遍历敌人所有的坦克
                for(int i = 0;i < enemyTanks.size();i++){
                    EnemyTank enemyTank = enemyTanks.get(i);
                    hiTank(shoot, enemyTank);
                }
            }
        }
    }
    //多颗子弹击中敌方boss
    public void hitBossTank(){
        //遍历子弹
        for(int j = 0;j < hero.shoots.size();j++){
            Shoot shoot = hero.shoots.get(j);
            if(shoot != null & shoot.isLive){//当我的子弹还在存活
                //遍历敌人所有的坦克
                hitBoss(shoot, boss);
                }
            }
    }

    //多颗子弹击中墙体
    public void hitWell01(){
        //遍历子弹
        for(int j = 0;j < hero.shoots.size();j++){
            Shoot shoot = hero.shoots.get(j);
            if(shoot != null & shoot.isLive){//当我的子弹还在存活
                //遍历所有墙体
                for(int i = 0;i < wells.size();i++){
                    Well well = wells.get(i);
                    heroHitWell(shoot, well);
                }
            }
        }
    }

    //编写方法，判断我方子弹是否击中敌人坦克
    public void hiTank(Shoot s, Tank enemyTank){
        //判断s 击中坦克
        switch (enemyTank.getDirect()){
            case 0://坦克向上,上下坦克体积一样大，子弹进入坦克体积来判断子弹是否击中目标
            case 2://坦克向下
                if(s.x > enemyTank.getX() && s.x < enemyTank.getX() + 40
                && s.y > enemyTank.getY() && s.y < enemyTank.getY() + 60){
                    s.isLive = false;
                    enemyTank.isLive = false;
                    //当我的子弹击中敌人坦克后，将enemyTank从Vector拿掉
                    enemyTanks.remove(enemyTank);
                    finScore += 100;
                    //创建Bomb对象，加入到bombs集合
                    Bomb bomb = new Bomb(enemyTank.getX(), enemyTank.getY());
                    bombs.add(bomb);
                }
                break;
            case 1://向右
            case 3://向走
                if(s.x > enemyTank.getX() && s.x < enemyTank.getX() + 60
                        && s.y > enemyTank.getY() && s.y < enemyTank.getY() + 40) {
                    s.isLive = false;
                    enemyTank.isLive = false;
                    //当我的子弹击中敌人坦克后，将enemyTank从Vector拿掉
                    enemyTanks.remove(enemyTank);
                    finScore += 100;
                    //创建Bomb对象，加入到bombs集合
                    Bomb bomb = new Bomb(enemyTank.getX(), enemyTank.getY());
                    bombs.add(bomb);
                }
                break;
        }
    }

    //我方击中boss坦克
    public void hitBoss(Shoot s, Tank bossTank){
        //判断s 击中坦克
        switch (bossTank.getDirect()){
            case 0://坦克向上,上下坦克体积一样大，子弹进入坦克体积来判断子弹是否击中目标
            case 2://坦克向下
                if(s.x > bossTank.getX() && s.x < bossTank.getX() + 40
                        && s.y > bossTank.getY() && s.y < bossTank.getY() + 60){
                    s.isLive = false;
                    bossBoold --;
                    if(bossBoold == 0){
                        boss.isLive = false;
                    }
                    //当我的子弹击中敌人坦克后，将enemyTank从Vector拿掉
                    finScore += 100;
                    //创建Bomb对象，加入到bombs集合
                    Bomb bomb = new Bomb(bossTank.getX(), bossTank.getY());
                    bombs.add(bomb);
                }
                break;
            case 1://向右
            case 3://向走
                if(s.x > bossTank.getX() && s.x < bossTank.getX() + 60
                        && s.y > bossTank.getY() && s.y < bossTank.getY() + 40) {
                    s.isLive = false;
                    bossBoold --;
                    if(bossBoold == 0){
                        boss.isLive = false;
                    }
                    //当我的子弹击中敌人坦克后，将enemyTank从Vector拿掉
                    finScore += 100;
                    //创建Bomb对象，加入到bombs集合
                    Bomb bomb = new Bomb(bossTank.getX(), bossTank.getY());
                    bombs.add(bomb);
                }
                break;
        }
    }

    //编写我方坦克被击中的方法
    public void hitTank(Shoot s, Tank heroTank){
        //判断s 击中坦克
        switch (heroTank.getDirect()){
            case 0://坦克向上,上下坦克体积一样大，子弹进入坦克体积来判断子弹是否击中目标
            case 2://坦克向下
                if(s.x > heroTank.getX() && s.x < heroTank.getX() + 40
                        && s.y > heroTank.getY() && s.y < heroTank.getY() + 60){
                    s.isLive = false;
                    blood --;
                    if(blood == 0){
                        heroTank.isLive = false;
                    }
                    //创建Bomb对象，加入到bombs集合
                    Bomb bomb = new Bomb(heroTank.getX(), heroTank.getY());
                    bombs.add(bomb);
                }
                break;
            case 1://向右
            case 3://向走
                if(s.x > heroTank.getX() && s.x < heroTank.getX() + 60
                        && s.y > heroTank.getY() && s.y < heroTank.getY() + 40) {
                    s.isLive = false;
                    blood --;
                    if(blood == 0){
                        heroTank.isLive = false;
                    }
                    //创建Bomb对象，加入到bombs集合
                    Bomb bomb = new Bomb(heroTank.getX(), heroTank.getY());
                    bombs.add(bomb);
                }
                break;
        }
    }

    //编写方法，判读我方坦克是否击中墙体
    public void heroHitWell(Shoot s, Well well){
        if(s.x > well.getX() && s.x < well.getX() + 50 && s.y > well.getY() && s.y < well.getY() + 60){
            s.isLive = false;
            well.setHealth(0);
            wells.remove(well);
//            Bomb bomb = new Bomb(well.getX(), well.getY());
//            bombs.add(bomb);
        }
    }


    //编写方法，判断敌方坦克是否击中墙体
    public void hitWell02(){
        //遍历所有的敌人坦克
        for(int i = 0;i < enemyTanks.size();i++){
            //取出敌人坦克
            EnemyTank enemyTank = enemyTanks.get(i);
            //遍历enemytank对象的所有子弹
            for(int j = 0;j < enemyTank.shoots.size();j++){
                //取出子弹
                Shoot shoot = enemyTank.shoots.get(j);
                //判段shoot是否击中墙体
                if(shoot != null && shoot.isLive){ //敌方子弹存在
                    //遍历墙体
                    for(int k = 0;k < wells.size();k++){
                        Well well = wells.get(k);
                        heroHitWell(shoot, well);
                    }
                }
            }
        }
    }

    //编写方法，判断boss坦克进攻我的坦克
    public void bossHit(){
        //遍历子弹
        for(int i = 0;i < boss.shootsboss.size();i++){
            Shoot shoot = (Shoot) boss.shootsboss.get(i);
            if(shoot.isLive && hero.isLive){
                hitTank(shoot, hero);
            }
        }
    }
    //编写方法，判断敌人坦克是否击中我的坦克
    public void hitHero(){
        //遍历所有的敌人坦克
        for(int i = 0;i < enemyTanks.size();i++){
            //取出敌人坦克
            EnemyTank enemyTank = enemyTanks.get(i);
            //遍历enemytank对象的所有子弹
            for(int j = 0;j < enemyTank.shoots.size();j++){
                //取出子弹
                Shoot shoot = enemyTank.shoots.get(j);
                //判段shoot是否击中我的坦克
                if(hero.isLive && shoot.isLive){
                    hitTank(shoot,hero);
                }
//                if(!(hero.isLive)){ //敌方击中我方坦克退出游戏
//                    System.exit(0);
//                }
            }
        }
    }

    //编写方法，判断是否和墙体发生碰撞，我方,碰撞停止前进
    public void collsionWell(){
        //遍历墙体
        for(int i = 0;i < wells.size();i++){
            Well well = wells.get(i);
            if (hero.isLive && well.getHealth() == 1){
                collsion(hero, well);
            }
        }
    }

    //碰撞检测
    public void collsion(Hero hero,Well well){
        if(hero.getX()+40 > well.getX() && hero.getX() < well.getX() + 50 &&
                hero.getY() > well.getY() && hero.getY() < well.getY() + 70){
//            System.out.println("和墙发生了碰撞");
            System.out.println("heroX: "+hero.getX());
            System.out.println("heroY: "+hero.getY());
            System.out.println("wellX: "+well.getX());
            System.out.println("wellY: "+well.getY());
            collsionLive = false;
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    //设置子弹加数量
    int shootCount = 1;
    @Override
    //处理wasd键按下的情况
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W){//按下w键（上）
            hero.setDirect(0);
            collsionWell();
                if (hero.getY() > 0 && collsionLive) {
                    if(accelerate){
                        hero.moveUp();
                        hero.moveUp();
                    }else {
                        hero.moveUp();
                    }
//                    System.out.println("上");
                }
        }else if(e.getKeyCode() == KeyEvent.VK_D){//按下d键（右）
            hero.setDirect(1);
            if(hero.getX() + 60 < 1000){if(accelerate){
                hero.moveRight();
                hero.moveRight();
            }else {
                hero.moveRight();
            }}
//            System.out.println("右");
//            collsionLive = true;
        }else if(e.getKeyCode() == KeyEvent.VK_S){//按下s键（下）
            hero.setDirect(2);
//            System.out.println("下");
            if(hero.getY() + 60 < 750){if(accelerate){
                hero.moveDown();
                hero.moveDown();
            }else {
                hero.moveDown();
            }}
        }else if(e.getKeyCode() == KeyEvent.VK_A){//按下a键（左）
            hero.setDirect(3);
//            System.out.println("左");
            if(hero.getX() > 0){
                if(accelerate){
                    hero.moveLeft();
                    hero.moveLeft();
                }else {
                    hero.moveLeft();
                }

            }
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) { //加速 space加速功能
            accelerate = true;
        }else if(e.getKeyCode() == KeyEvent.VK_ENTER){ //恢复正常速度 enter正常速度
            accelerate = false;
        }


        //增加我方血量
        if(e.getKeyCode() == KeyEvent.VK_H){
            if(blood < 999){
                blood ++;
                System.out.println("H");
            }
        }

        //增加敌方血量
        if(e.getKeyCode() == KeyEvent.VK_E){
            if(bossBoold < 999){
                bossBoold ++;
            }
        }


        if(e.getKeyCode() == KeyEvent.VK_P){ //+子弹
            System.out.println("用户按下了+，开始射击。");
            if(shootCount <100) {
                shootCount ++;
            }
        }
        //设置子弹减数量
        if(e.getKeyCode() == KeyEvent.VK_M){ //-子弹
            if(shootCount > 1){
                shootCount --;
            }
        }

        //如果用户按下的是J，就需要发射
        if(e.getKeyCode() == KeyEvent.VK_J){
            System.out.println("用户按下了J，开始射击。");
            //判断hero的子弹是否销毁,一颗子弹
//            if(hero.shoot == null || !hero.shoot.isLive){ //对象为空或者子弹线程结束
//                hero.shootEnemyTank();
//            }
            //发射多颗子弹

            hero.shootEnemyTank(shootCount);
        }

        //让面板重绘
        this.repaint(); //调用paint方法
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //处理按键释放
        int keyCode = e.getKeyCode(); //获取键码

        //执行操作
        if(keyCode == KeyEvent.VK_W){
            collsionLive = true;
        }else if(keyCode == KeyEvent.VK_D){
            collsionLive = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_S){
            collsionLive = true;
        }else if(keyCode == KeyEvent.VK_A){
            collsionLive = true;
        }
    }

    public void run(){//每隔100毫秒，重绘区域,刷新绘图区域，子弹就移动
        while(true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //判断是否击中了敌人坦克，单个子弹
            hitEnemyTank();//发射多颗
            hitWell01();//判断我方是否击中墙体
            hitBossTank();//击中敌方boss
            //boss攻击我方坦克
            bossHit();
            //判断敌人坦克是否击中我们
            hitHero();
            hitWell02();//判断敌方是否击中墙体
            this.repaint();
        }
    }
}

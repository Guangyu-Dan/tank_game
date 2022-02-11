package com.guangyu.tankgame3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.URL;
import java.util.Vector;

public class MyPanel extends JPanel implements KeyListener, Runnable {
    Hero hero = null;
    Vector<EnemyTank> enemyTanks = new Vector<>();
    Vector<Node> nodes = new Vector<>();
    Vector<Bomb> bombs = new Vector<>();
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;

    int enemyTankAmount = 3;
    public MyPanel(String key){
        File file = new File(Recorder.getRecordFile());
        if (file.exists()){
            nodes = Recorder.getNodesAndEnemyTankRec();
        } else {
            System.out.println("file not exist, can only start a new game");
            key = "1";
        }

        Recorder.setEnemyTanks(enemyTanks);

        hero = new Hero(500, 100);
        hero.setSpeed(5);

        switch (key){
            case "1":
                for (int i = 0; i < enemyTankAmount; i++){
                    EnemyTank enemyTank = new EnemyTank((100*(i+1)), 0);
                    enemyTank.setEnemyTanks(enemyTanks);
                    enemyTank.setDirect(2);
                    new Thread(enemyTank).start();
                    Shot shot = new Shot(enemyTank.getX()+20, enemyTank.getY()+60, enemyTank.getDirect());
                    enemyTank.shots.add(shot);
                    new Thread(shot).start();
                    enemyTanks.add(enemyTank);
                }
                break;
            case "2":
                for (int i = 0; i < nodes.size(); i++){
                    Node node = nodes.get(i);
                    EnemyTank enemyTank = new EnemyTank(node.getX(),node.getY());
                    enemyTank.setEnemyTanks(enemyTanks);
                    enemyTank.setDirect(node.getDirect());
                    new Thread(enemyTank).start();
                    Shot shot = new Shot(enemyTank.getX()+20, enemyTank.getY()+60, enemyTank.getDirect());
                    enemyTank.shots.add(shot);
                    new Thread(shot).start();
                    enemyTanks.add(enemyTank);
                }
                break;
            default:
                System.out.println("Wrong input");
        }

        image1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("./bomb_1.jpg"));
        image2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("./bomb_2.jpg"));
        image3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("./bomb_3.jpg"));
    }

    public void showInfo(Graphics g){
        g.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.BOLD, 25);
        g.setFont(font);
        g.drawString("You destoried", 1020, 30);
        drawTank(1020, 60, g, 0, 0);
        g.setColor(Color.BLACK);
        g.drawString(Recorder.getAllEnemyTankNum() + "", 1080, 100);
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        g.fillRect(0, 0, 1000, 750);
        showInfo(g);
        if (hero != null && hero.isLive) {
            drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 1);
        }

        for (int i = 0; i < hero.shots.size(); i++){
            Shot shot = hero.shots.get(i);
            if (shot != null && shot.isLive) {
                g.draw3DRect(shot.x, shot.y, 1, 1, false);;
            } else {
                hero.shots.remove(shot);
            }
        }

        for (int i = 0; i < bombs.size(); i++) {
            Bomb bomb = bombs.get(i);
            if (bomb.life > 6){
                g.drawImage(image1, bomb.x, bomb.y, 60,60,this);
            } else if (bomb.life > 3) {
                g.drawImage(image2, bomb.x, bomb.y, 60,60,this);
            } else {
                g.drawImage(image3, bomb.x, bomb.y, 60,60,this);
            }
            bomb.lifeDown();
            if (bomb.life == 0) {
                bombs.remove(bomb);
            }
        }

        for (int i = 0; i < enemyTanks.size(); i++){
            EnemyTank enemyTank = enemyTanks.get(i);
            //System.out.println("tank " + i + "islive:"  + enemyTank.isLive);
            if (enemyTank.isLive) {
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirect(), 0);
                for (int j = 0; j < enemyTank.shots.size(); j++) {
                    Shot shot = enemyTank.shots.get(j);
                    if (shot.isLive) {
                        g.draw3DRect(shot.x, shot.y, 1, 1, false);
                    } else {
                        enemyTank.shots.remove(shot);
                    }
                }
            }
        }
    }
    public void drawTank(int x, int y, Graphics g, int direct, int type){
        switch (type){
            case 0:
                g.setColor(Color.cyan);
                break;
            case 1:
                g.setColor(Color.yellow);
                break;
        }
        //direct == 0: upward 1: right 2: down 3: left
        switch (direct) {
            case 0: //upward
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x+30, y, 10, 60, false);
                g.fill3DRect(x+10, y+10, 20, 40, false);
                g.fillOval(x+10, y+20, 20,20);
                g.drawLine(x+20, y+30, x+20, y);
                break;
            case 1: //right
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y+30, 60, 10, false);
                g.fill3DRect(x+10, y+10, 40, 20, false);
                g.fillOval(x+20, y+10, 20,20);
                g.drawLine(x+30, y+20, x+60, y+20);
                break;
            case 2: //down
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x+30, y, 10, 60, false);
                g.fill3DRect(x+10, y+10, 20, 40, false);
                g.fillOval(x+10, y+20, 20,20);
                g.drawLine(x+20, y+30, x+20, y+60);
                break;
            case 3: //left
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y+30, 60, 10, false);
                g.fill3DRect(x+10, y+10, 40, 20, false);
                g.fillOval(x+20, y+10, 20,20);
                g.drawLine(x+30, y+20, x, y+20);
                break;
            default:

        }

    }

    public void hitHero(){
        for (int i = 0; i < enemyTanks.size(); i++){
            EnemyTank enemyTank = enemyTanks.get(i);
            for (int j = 0; j < enemyTank.shots.size(); j++){
                Shot shot = enemyTank.shots.get(j);
                if (hero.isLive && shot.isLive){
                    hitTank(shot,hero);
                }
            }
        }
    }

    public synchronized void  hitTank(Shot s, Tank enemyTank){
        switch (enemyTank.getDirect()){
            case 0:
            case 2:
                if (s.x > enemyTank.getX() && s.x < enemyTank.getX() + 40
                        && s.y > enemyTank.getY() && s.y < enemyTank.getY() + 60) {
                    s.isLive = false;
                    enemyTank.isLive = false;
                    if (enemyTank instanceof EnemyTank){
                        Recorder.addEnemyTankNum();
                    }
                    Bomb bomb = new Bomb(enemyTank.getX(), enemyTank.getY());
                    bombs.add(bomb);
                    enemyTanks.remove(enemyTank);
                }
            case 1:
            case 3:
                if (s.x > enemyTank.getX() && s.x < enemyTank.getX() + 60
                        && s.y > enemyTank.getY() && s.y < enemyTank.getY() + 40){
                    s.isLive = false;
                    enemyTank.isLive = false;
                    if (enemyTank instanceof EnemyTank){
                        Recorder.addEnemyTankNum();
                    }
                    Bomb bomb = new Bomb(enemyTank.getX(), enemyTank.getY());
                    bombs.add(bomb);
                    enemyTanks.remove(enemyTank);
                }

        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W){
            hero.setDirect(0);
            if (hero.getY() > 0) {
                hero.moveUp();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_D){
            hero.setDirect(1);
            if (hero.getX() + 60 < 1000) {
                hero.moveRight();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_S){
            hero.setDirect(2);
            if (hero.getY() + 60 < 750) {
                hero.moveDown();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_A){
            hero.setDirect(3);
            if (hero.getX() > 0) {
                hero.moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_J){
            hero.shotEnemyTank();
        }
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (hero.shot != null && hero.shot.isLive){

                for (int j = 0; j < hero.shots.size(); j++) {
                    Shot shot = hero.shots.get(j);
                    for (int i = 0; i < enemyTanks.size(); i++){
                        EnemyTank enemyTank = enemyTanks.get(i);
                        hitTank(shot, enemyTank);
                    }
                }
            }
            hitHero();
            this.repaint();
        }
    }
}

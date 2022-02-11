package com.guangyu.tankgame3;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

public class TankGame03 extends JFrame {
    MyPanel mp = null;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        TankGame03 TankGame01 = new TankGame03();
    }

    public TankGame03(){
        System.out.println("Please select 1: new game. 2: continue last game");
        String key = scanner.next();
        mp = new MyPanel(key);
        Thread thread = new Thread(mp);
        thread.start();
        this.add(mp);
        this.setSize(1250,790);
        this.addKeyListener(mp);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Recorder.keepRecord();
                System.exit(0);
            }
        });
    }
}

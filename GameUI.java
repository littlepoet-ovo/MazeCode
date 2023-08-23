package com.sdxf.game;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameUI extends JFrame implements ActionListener, GameRunningData{
    private int useTime = 0;
    private int sourceMoney;
    private int useMoney = 0;
    private Database d;
    private int mode;//1为闯关模式 0为娱乐模式
    private JMenuItem restarJMI;
    private JMenuItem backJMI;
    private String nowLevel;// 传入的关卡名称
    private JLabel timeJLB = new JLabel("10:00");
    private int sumS = 10; // 游戏时间限制（秒）
    private ScheduledExecutorService scheduler;
    private boolean isRuning = true; //游戏是否运行中
    private JPanel stop = new JPanel();
    private GamePanel start;//游戏主体，参数传入
    private JButton exitJBT = new JButton("退出");
    private JButton stopJBT = new JButton("暂停");
    JLabel moneyJLB;
    public GameUI(Database d, int mode, String nowLevel){
        this.start = new GamePanel(9, 12,this,0,"");
        this.nowLevel = nowLevel;
        this.mode = mode;
        this.d = d;
        initJFrame();
        initJMB();
        initJLB();
        if(mode==1){
            initTime();
        }
        initJPanel();
        initButton();
        start.requestFocus();
        setVisible(true);
    }

    private void initButton() {
        exitJBT.setBounds(80,640,200,50);
        stopJBT.setBounds(500,640,200,50);
        exitJBT.setFont(new Font("宋体",Font.BOLD,15));
        stopJBT.setFont(new Font("宋体",Font.BOLD,15));
        exitJBT.addActionListener(this);
        stopJBT.addActionListener(this);
        this.getContentPane().add(exitJBT);
        this.getContentPane().add(stopJBT);
    }

    private void initJPanel() {
        JLabel jlb1 = new JLabel("游戏暂停中。。。");
        jlb1.setFont(new Font("黑体",Font.BOLD,40));
        jlb1.setBounds(251,150,350,50);
        stop.add(jlb1);
        stop.setBounds(10,120,765,500);
        stop.setLayout(null);
        // 创建黑色单线边框
        Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);

        // 将边框应用到JPanel
        stop.setBorder(blackBorder);
        this.getContentPane().add(stop);
        this.getContentPane().add(start);
        stop.setVisible(false);
    }

    private void initTime() {
        scheduler = Executors.newScheduledThreadPool(1);
        // 创建一个周期性任务，每隔1秒执行一次
        scheduler.scheduleAtFixedRate(() -> {
            if(isRuning){
                int tempM = sumS/60;
                int tempS = sumS%60;
                timeJLB.setText(String.format("%02d:%02d",tempM,tempS));
                useTime++;
                if(sumS<=0){
                    scheduler.shutdown();
                    JOptionPane.showMessageDialog(null,"时间到了");
                }
                sumS--;
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void initJLB() {
        String title;
        if(mode == 1) title = "闯关模式";
        else title = "娱乐模式";
        JLabel jlb1 = new JLabel(title);
        JLabel jlb2 = new JLabel(this.nowLevel);
        jlb1.setFont(new Font("黑体",Font.BOLD,30));
        jlb1.setBounds(330,20,150,50);
        jlb2.setFont(new Font("黑体",Font.BOLD,25));
        jlb2.setBounds(460,20,150,50);
        this.sourceMoney = d.getMoney();
        moneyJLB = new JLabel(String.format("金币：%d",this.sourceMoney));
        moneyJLB.setFont(new Font("宋体",Font.BOLD,15));
        moneyJLB.setBounds(620,80,100,30);
        if(mode==1){
            timeJLB.setFont(new Font("宋体",Font.BOLD,20));
            timeJLB.setBounds(400,80,100,30);
            timeJLB.setForeground(Color.red);
            this.getContentPane().add(timeJLB);
        }

        this.getContentPane().add(jlb1);
        this.getContentPane().add(jlb2);
        this.getContentPane().add(moneyJLB);

    }

    private void initJMB() {
        JMenuBar JMB = new JMenuBar();
        JMenu sel = new JMenu("选择");
        restarJMI = new JMenuItem("重新开始");
        backJMI = new JMenuItem("返回主界面");
        restarJMI.addActionListener(this);
        backJMI.addActionListener(this);
        sel.add(restarJMI);
        sel.add(backJMI);
        JMB.add(sel);
        this.setJMenuBar(JMB);
    }

    private void initJFrame() {
        this.setSize(800,780);
        String title;
        if(mode == 1) title = "拼图游戏 - 闯关模式 - " + d.getUsername();
        else title = "拼图游戏 - 娱乐模式 - " + d.getUsername();
        this.setTitle(title);
        this.setLocationRelativeTo(null);//居中
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);//关闭模式设置
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               closeApp();
            }
        });
        this.setLayout(null);//清除格式

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if(o==restarJMI){
            new GameUI(d,mode, "");
            System.out.println("点击了重新开始");
            closeApp();
        }else if(o==backJMI){
            closeApp();
            System.out.println("点击了返回主窗口");
        }else if(o==exitJBT){
            closeApp();
            System.out.println("点击了退出按钮");
        }else if(o==stopJBT){
            System.out.println("点击了暂停按钮");
            if (isRuning){
                start.Focus();
                isRuning = false;
                start.setVisible(false);
                stop.setVisible(true);
                stopJBT.setText("继续");
            }else{
                isRuning = true;
                start.setVisible(true);
                stop.setVisible(false);
                stopJBT.setText("暂停");
            }
        }
    }
    private void closeApp(){
        if(mode==1) scheduler.shutdown();
//        JOptionPane.showMessageDialog(null,"将要关闭窗口");
        System.exit(0);
//                dispose();
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Database d = new Database();
        d.link();
        System.out.println(d.login("zijie","123456"));
        new GameUI(d,0, "");
    }

    @Override
    public void Winning(int money) {
        System.out.println("游戏胜利！");
        if(mode==1){
            int ans = d.saveGameData(useTime,useMoney,nowLevel);
            if(ans == 200){
                System.out.println("运行成功");
            }else if(ans == 201){
                System.out.println("打破纪录");
            }else{
                System.out.println("运行失败");
            }
        }
        int ans = d.setMoney(money-useMoney);
        if(ans==200){
            System.out.println("修改成功");
        }else{
            System.out.println("改动是吧，错误代码"+ans);
        }


    }

    @Override
    public boolean useMoney(int need) {
        if(need + useMoney <= sourceMoney){
            useMoney += need;
            moneyJLB.setText(String.format("金币：%d",this.sourceMoney-this.useMoney));
            return true;
        }
        return false;
    }

    @Override
    public void failed(boolean isReserve) {
        if (!isReserve){
            System.out.println("游戏失败且扣除游戏币");
            d.setMoney(-1*useMoney);
        }else{
            System.out.println("游戏失败不扣除金币");
        }

    }
}

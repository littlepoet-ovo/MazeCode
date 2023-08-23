package com.sdxf.game;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameUI extends JFrame implements ActionListener, GameRunningData {
    private int useTime = 0;
    private int sourceMoney;
    private int useMoney = 0;
    private Database d;
    private int mode;//1为闯关模式 0为娱乐模式
    private JMenuItem restarJMI;
    private JMenuItem backJMI;
    private String nowLevel;// 传入的关卡名称
    private JLabel timeJLB = new JLabel("300:00");
    private int sumS = 300; // 游戏时间限制（秒）
    private ScheduledExecutorService scheduler;
    private boolean isRuning = true; //游戏是否运行中
    private JPanel stop = new JPanel();
    private GamePanel start;//游戏主体，参数传入
    private JButton exitJBT = new JButton("退出");
    private JButton stopJBT = new JButton("暂停");
    JLabel moneyJLB;
    int currIndex,sumIndex;

    public GameUI(Database d, int mode, String levelName, int currIndex, int sumIndex) {
        this.nowLevel = levelName;
        this.mode = mode;
        this.d = d;
        this.currIndex=currIndex;
        this.sumIndex=sumIndex;

        initJFrame();
//        initJMB();
        initJLB();
        if (mode == 1) {
            initTime();
        }
        LoadMap();
        initJPanel();
        initButton();
        setVisible(true);
    }

    public void LoadMap(){
        System.out.println("yes");
        this.start = new GamePanel(d, 9, 12, this, mode, nowLevel);
        start.Focus();
        this.repaint();
    }

    private void initButton() {
        exitJBT.setBounds(80, 640, 200, 50);
        stopJBT.setBounds(500, 640, 200, 50);
        exitJBT.setFont(new Font("宋体", Font.BOLD, 15));
        stopJBT.setFont(new Font("宋体", Font.BOLD, 15));
        exitJBT.addActionListener(this);
        stopJBT.addActionListener(this);
        this.getContentPane().add(exitJBT);
        this.getContentPane().add(stopJBT);
    }

    private void initJPanel() {
        JLabel jlb1 = new JLabel("游戏暂停中。。。");
        jlb1.setFont(new Font("黑体", Font.BOLD, 40));
        jlb1.setBounds(251, 150, 350, 50);
        stop.add(jlb1);
        stop.setBounds(10, 120, 765, 500);
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
            if (isRuning) {
                int tempM = sumS / 60;
                int tempS = sumS % 60;
                timeJLB.setText(String.format("%02d:%02d", tempM, tempS));
                useTime++;
                if (sumS <= 0) {
                   isRuning=false;
                    failed("time");
                }
                sumS--;
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void initJLB() {
        String title;
        if (mode == 1) title = "闯关模式";
        else title = "娱乐模式";
        JLabel jlb1 = new JLabel(title);
        JLabel jlb2 = new JLabel(this.nowLevel);
        jlb1.setFont(new Font("黑体", Font.BOLD, 30));
        jlb1.setBounds(330, 20, 150, 50);
        jlb2.setFont(new Font("黑体", Font.BOLD, 25));
        jlb2.setBounds(460, 20, 150, 50);
        this.sourceMoney = d.getMoney();
        moneyJLB = new JLabel(String.format("金币：%d", this.sourceMoney));
        moneyJLB.setFont(new Font("宋体", Font.BOLD, 15));
        moneyJLB.setBounds(620, 80, 100, 30);
        if (mode == 1) {
            timeJLB.setFont(new Font("宋体", Font.BOLD, 20));
            timeJLB.setBounds(400, 80, 100, 30);
            timeJLB.setForeground(Color.red);
            this.getContentPane().add(timeJLB);
        }

        this.getContentPane().add(jlb1);
        this.getContentPane().add(jlb2);
        this.getContentPane().add(moneyJLB);

    }

//    private void initJMB() {
//        JMenuBar JMB = new JMenuBar();
//        JMenu sel = new JMenu("选择");
//        restarJMI = new JMenuItem("重新开始");
//        backJMI = new JMenuItem("返回主界面");
//        restarJMI.addActionListener(this);
//        backJMI.addActionListener(this);
//        sel.add(restarJMI);
//        sel.add(backJMI);
//        JMB.add(sel);
//        this.setJMenuBar(JMB);
//    }

    private void initJFrame() {
        this.setSize(800, 780);
        String title;
        if (mode == 1) title = "迷宫游戏 - 闯关模式 - " + d.getUsername();
        else title = "迷宫游戏 - 娱乐模式 - " + d.getUsername();
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
        if (o == restarJMI) {
            new GameUI(d, mode, "", currIndex, sumIndex);
            System.out.println("点击了重新开始");
            closeApp();
        } else if (o == backJMI) {
            closeApp();
            System.out.println("点击了返回主窗口");
        } else if (o == exitJBT) {
            closeApp();
            System.out.println("点击了退出按钮");
        } else if (o == stopJBT) {
            System.out.println("点击了暂停按钮");
            if (isRuning) {
                start.Focus();
                isRuning = false;
                start.setVisible(false);
                stop.setVisible(true);
                stopJBT.setText("继续");
            } else {
                isRuning = true;
                start.setVisible(true);
                stop.setVisible(false);
                stopJBT.setText("暂停");
            }
        }
    }

    private void closeApp() {
        if (mode == 1) scheduler.shutdown();
//        JOptionPane.showMessageDialog(null,"将要关闭窗口");
//        System.exit(0);
        dispose();
    }

//    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        Database d = new Database();
//        d.link();
//        System.out.println(d.login("zijie", "123456"));
//        new GameUI(d, 0, "", currIndex);
//    }

    @Override
    public void Winning(int money) {
        int result=JOptionPane.showConfirmDialog(this, "真是个聪明蛋！再来一局吧！","提示", JOptionPane.YES_NO_OPTION);
            System.out.println("游戏胜利！");
            if (mode == 1) {
                int ans = d.saveGameData(useTime, useMoney, nowLevel);
                if (ans == 200) {
                    System.out.println("运行成功");
                } else if (ans == 201) {
                    System.out.println("打破纪录");
                } else {
                    System.out.println("运行失败");
                }
                int ans1 = d.setMoney(money - useMoney);
                if (ans1 == 200) {
                    System.out.println("修改成功");
                } else {
                    System.out.println("改动失败，错误代码" + ans);
                }
            }
                if(result==JOptionPane.YES_OPTION) {
                    if(mode==1){
                        if(currIndex==sumIndex){
                            JOptionPane.showMessageDialog(this,"你已经完成了所有关卡了，即将退出此界面");
                            this.dispose();
                            new modeTrick(d);
                        }
                        else {
                            this.start=new GamePanel(d,9,12,this,1,String.valueOf(currIndex+1));
                            start.requestFocus();
                        }

                    }
                    else{
                        LoadMap();
                    }
                }
                else{
                    this.dispose();
                    if (mode == 0) {
                        new modeSel(d);
                    }
                    else {
                        new modeTrick(d);
                    }
                }
    }

    @Override
    public void useMoney(int need) {
        if (need + useMoney <= sourceMoney) {
            useMoney += need;
            moneyJLB.setText(String.format("金币：%d", this.sourceMoney - this.useMoney));
        }
        else {
            failed("money");
        }
    }

    //时间到了或者金币不够的情况
    @Override
    public void failed(String limit) {
        if (limit == "money") {
            isRuning=false;
            int result = JOptionPane.showConfirmDialog(this, "金币不够了哦，需要提供50金币继续游戏嘛！", "提示", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                d.setMoney(50);
                isRuning=true;
            } else {
                this.dispose();
                if (mode == 0) {
                    new modeSel(d);
                }
                else {
                    new modeTrick(d);
                }
            }
        }
        else {
            int result = JOptionPane.showConfirmDialog(this, "时间已经到了哦,再挑战一次吧？", "提示", JOptionPane.YES_NO_OPTION);
            d.setMoney(-1*useMoney-20);
            if(result==JOptionPane.YES_OPTION){
                LoadMap();
            }
            else {
                this.dispose();
                if (mode == 0) {
                    new modeSel(d);
                }
                else {
                    new modeTrick(d);
                }
            }
        }
    }
}

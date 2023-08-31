package com.sdxf.game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
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
    private JLabel timeJLB = new JLabel("05:00");
    private int sumS = 500; // 游戏时间限制（秒）
    private ScheduledExecutorService scheduler;
    private boolean isRuning = true; //游戏是否运行中
    private JPanel stop = new JPanel();
    private GamePanel start;//游戏主体，参数传入
    private JButton exitJBT = new JButton("退出");
    private JButton stopJBT = new JButton("暂停");
    JLabel levelJLB;
    JLabel moneyJLB;
    int currIndex,sumIndex,flag=0;
    URL url;
    Clip clip;

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
        LoadMap(nowLevel);
        initJPanel();
        initButton();
        setVisible(true);
    }

    public void LoadMap(String level){
        if(flag==1){
            stopMusic();
            flag=0;
        }
        if(mode==1) {
            currIndex = Integer.parseInt(level);
            System.out.println(level);
        }
        this.sourceMoney = d.getMoney();
        moneyJLB.setText(String.format("金币：%d", this.sourceMoney));
        levelJLB.setText(this.nowLevel);
        sumS = 300;
        useMoney = 0;
        if(this.start!=null){
            this.remove(this.start);
        }
        this.start = new GamePanel(d, 9, 12, this, mode, level);
        this.getContentPane().add(this.start);
        this.repaint();
        isRuning = true;
        start.setVisible(false);
        start.setVisible(true);
        start.requestFocus();
        playMusic();
    }

    private void initButton() {
        exitJBT.setBounds(80, 640, 200, 50);
        stopJBT.setBounds(500, 640, 200, 50);

        exitJBT.setFont(new Font("华文新魏", Font.BOLD, 35));
        exitJBT.setContentAreaFilled(false);//按钮设置为透明，这样就不会挡着后面的背景
        exitJBT.setBorder(BorderFactory.createRaisedBevelBorder());
        exitJBT.setBackground(Color.cyan);//设置按钮颜色
//        exitJBT.setFont(new Font("宋体", Font.BOLD, 15));
//        stopJBT.setFont(new Font("宋体", Font.BOLD, 15));

        stopJBT.setFont(new Font("华文新魏", Font.BOLD, 35));
        stopJBT.setContentAreaFilled(false);//按钮设置为透明，这样就不会挡着后面的背景
        stopJBT.setBorder(BorderFactory.createRaisedBevelBorder());
        stopJBT.setBackground(Color.cyan);//设置按钮颜色

        exitJBT.addActionListener(this);
        stopJBT.addActionListener(this);
        this.getContentPane().add(exitJBT);
        this.getContentPane().add(stopJBT);


//        //播放音乐图标
//        JButton btnStart = new JButton();
//        btnStart.setBounds(420, 80, 50, 50);
//        btnStart.setContentAreaFilled(false);
//        btnStart.setBorder(BorderFactory.createRaisedBevelBorder());
//        url = Main.class.getResource("/image/pause1.jpg");
//        ImageIcon imReturn = new ImageIcon(url); // Icon由图片文件形成
//        Image imag = imReturn.getImage(); // 但这个图片太大不适合做Icon
//        // 为把它缩小点，先要取出这个Icon的image ,然后缩放到合适的大小
//        Image smallIma = imag.getScaledInstance(50, 50, Image.SCALE_AREA_AVERAGING);
//        // 再由修改后的Image来生成合适的Icon
//        ImageIcon smallIc = new ImageIcon(smallIma);
//        // 最后设置它为按钮的图片
//        btnStart.setIcon(smallIc);
//
//        //停止音乐图标
//        JButton btnStop = new JButton();
//        btnStop.setBounds(420, 80, 50, 50);
//        btnStop.setContentAreaFilled(false);
//        btnStop.setBorder(BorderFactory.createRaisedBevelBorder());
//        url = Main.class.getResource("/image/continue.jpg");
//        ImageIcon imaReturn = new ImageIcon(url); // Icon由图片文件形成
//        Image image1 = imaReturn.getImage(); // 但这个图片太大不适合做Icon
//        // 为把它缩小点，先要取出这个Icon的image ,然后缩放到合适的大小
//        Image smallImage1 = image1.getScaledInstance(50, 50, Image.SCALE_AREA_AVERAGING);
//        // 再由修改后的Image来生成合适的Icon
//        ImageIcon smallIcon = new ImageIcon(smallImage1);
//        // 最后设置它为按钮的图片
//        btnStop.setIcon(smallIcon);

        //播放音乐（窗口打开时）
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowOpened(WindowEvent e) {
//                playMusic();
//                //只能不播放
////                btnStop.setVisible(false);
////                btnStop.setEnabled(false);
//            }
//        });
    }
    //背景音乐播放
    public void playMusic(){
        flag=1;
        try {
            // 加载音乐文件，文件路径，创建音频输入流
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/music/刘晨 - 我还有点小糊涂 (片段版伴奏).wav"));

            // 创建音频剪辑
            clip = AudioSystem.getClip();

            // 打开音频剪辑并开始播放
            clip.open(audioInputStream);
            clip.start();
            // 设定循环播放次数
            clip.loop(Clip.LOOP_CONTINUOUSLY);//-1代表无限循环播放
//            while (clip.getMicrosecondPosition() < clip.getMicrosecondLength()) {
//                // 可以添加其他操作
//                playMusic();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断音乐是否播放完毕
//    public boolean isMusicPlaying() {
//        return clip.isRunning();
//    }
    //停止播放
    public void stopMusic(){
//        System.out.println(1);
//            System.out.println(2);
        clip.stop();
        clip.close();

    }

    private void initJPanel() {
        JLabel jlb1 = new JLabel("游戏暂停中。。。");
        jlb1.setFont(new Font("华文新魏", Font.BOLD, 40));
        jlb1.setBounds(251, 150, 350, 50);
        stop.add(jlb1);
        stop.setBounds(10, 120, 765, 500);
        stop.setLayout(null);
        // 创建黑色单线边框
        Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);

        // 将边框应用到JPanel
        stop.setBorder(blackBorder);
        this.getContentPane().add(stop);
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
//   if(isMusicPlaying()==false){
//       stopMusic();
//   }
    }

    private void initJLB() {
        String title;
        if (mode == 1) title = "闯关模式";
        else title = "娱乐模式";
        JLabel jlb1 = new JLabel(title);
        levelJLB = new JLabel(this.nowLevel);
        jlb1.setFont(new Font("华文行楷", Font.BOLD, 30));
        jlb1.setBounds(330, 20, 150, 50);
        levelJLB.setFont(new Font("华文行楷", Font.BOLD, 25));
        levelJLB.setBounds(460, 20, 150, 50);
        this.sourceMoney = d.getMoney();
        moneyJLB = new JLabel(String.format("金币：%d", this.sourceMoney));
        moneyJLB.setFont(new Font("华文行楷", Font.BOLD, 20));
        moneyJLB.setBounds(620, 80, 100, 30);
        if (mode == 1) {
            timeJLB.setFont(new Font("华文行楷", Font.BOLD, 20));
            timeJLB.setBounds(400, 80, 100, 30);
            timeJLB.setForeground(Color.red);
            this.getContentPane().add(timeJLB);
        }

        this.getContentPane().add(jlb1);
        this.getContentPane().add(levelJLB);
        this.getContentPane().add(moneyJLB);

    }

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

        JLabel backGround = new JLabel();
        URL url = Main.class.getResource("/image/Back1.jpg");
        ImageIcon icon = new ImageIcon(url);//加载图像文件到ImageIcon对象中
        backGround.setIcon(icon);//将ImageIcon设置成JLabel的图标
        JPanel imPanel=(JPanel) this.getContentPane();//注意内容面板必须强转为JPanel才可以实现下面的设置透明
        imPanel.setOpaque(false);//将内容面板设为透明
        backGround.setBounds(0, 0, this.getWidth(), this.getHeight());//设置标签位置大小，记得大小要和窗口一样大
        icon.setImage(icon.getImage().getScaledInstance(backGround.getWidth(), backGround.getHeight(), Image.SCALE_DEFAULT));//图片自适应标签大小
        this.getLayeredPane().add(backGround, Integer.valueOf(Integer.MIN_VALUE));//标签添加到层面板

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
                isRuning = false;
                start.setVisible(false);
                stop.setVisible(true);
                stopJBT.setText("继续");
            } else {
                start.Focus();
                isRuning = true;
                start.setVisible(true);
                stop.setVisible(false);
                stopJBT.setText("暂停");
            }
        }
    }

    private void closeApp() {
        stopMusic();
        if (mode == 1) {
            scheduler.shutdown();
            new modeTrick(d);
        }else{
            new modeSel(d);
        }
        dispose();

    }
    @Override
    public void Winning(int money) {
        System.out.println("游戏胜利！");
        if (mode == 1) {
            int ans = d.saveGameData(useTime, useMoney, "第"+nowLevel+"关");
            if (ans == 200) {
                System.out.println("运行成功");
            } else if (ans == 201) {
                JOptionPane.showMessageDialog(this,"恭喜你！打破上一次的记录啦！");
            } else {
                System.out.println("运行失败");
            }
        }
        int ans1 = d.setMoney(money - useMoney);
        if (ans1 == 200) {
            System.out.println("修改成功");
        } else {
            System.out.println("改动失败，错误代码" + ans1);
        }
        int result=JOptionPane.showConfirmDialog(this, "真是个聪明蛋！奖励10个金币，再来一局吧！","提示", JOptionPane.YES_NO_OPTION);
        if(result==JOptionPane.YES_OPTION) {
            if(mode==1){
                System.out.println("当前关卡"+currIndex+"总关卡"+sumIndex);
                if(currIndex==sumIndex){
                    JOptionPane.showMessageDialog(this,"你已经完成了所有关卡了，即将退出此界面");
                    this.dispose();
                    new modeTrick(d);
                }
                else {
                    nowLevel = String.format("%d",Integer.parseInt(nowLevel)+1);
                    System.out.println(nowLevel);
                    LoadMap(nowLevel);
                }

            }
            else{
                LoadMap(nowLevel);
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
                this.sourceMoney += 50;
                moneyJLB.setText(String.format("金币：%d", this.sourceMoney-this.useMoney));
                isRuning=true;
            } else {
                stopMusic();
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
                LoadMap(nowLevel);
            }
            else {
                stopMusic();
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

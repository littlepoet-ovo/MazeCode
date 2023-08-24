//ctrl+Alt+L 对齐代码
package com.sdxf.game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

public class modeSel extends JFrame {
    Database d;
    Clip clip;
    public modeSel(Database d) {

        this.d = d;
        //数据库调用显示
        String userName = d.getUsername();
        String money =String.valueOf(d.getMoney());

        //窗口设计
        setTitle("迷宫游戏 - 游戏模式 - "+userName);
        setDefaultCloseOperation(this.EXIT_ON_CLOSE);//结束程序

        setLayout(null);
        setSize(500, 700);
        setLocationRelativeTo(null);//窗口居中显示
        setResizable(false);//设置窗口为不可缩放

        //给窗口加上背景图片
        JLabel backGround = new JLabel();
        URL url = Main.class.getResource("/image/Back1.jpg");
        ImageIcon icon = new ImageIcon(url);//加载图像文件到ImageIcon对象中
        backGround.setIcon(icon);//将ImageIcon设置成JLabel的图标
        JPanel imPanel=(JPanel) this.getContentPane();//注意内容面板必须强转为JPanel才可以实现下面的设置透明
        imPanel.setOpaque(false);//将内容面板设为透明
        backGround.setBounds(0, 0, this.getWidth(), this.getHeight());//设置标签位置大小，记得大小要和窗口一样大
        icon.setImage(icon.getImage().getScaledInstance(backGround.getWidth(), backGround.getHeight(), Image.SCALE_DEFAULT));//图片自适应标签大小
        this.getLayeredPane().add(backGround, Integer.valueOf(Integer.MIN_VALUE));//标签添加到层面板

        //标签设计
        JLabel laTitle = new JLabel("游戏模式");
        laTitle.setBounds(165, 90, 150, 50);//设置标签位置及大小
        laTitle.setFont(new Font("华文彩云", Font.BOLD, 35));//设置标签字体及大小

        //用户图标
        url = Main.class.getResource("/image/user1.jpg");
        ImageIcon  iname= new ImageIcon(url); // Icon由图片文件形成
        Image mage = iname.getImage(); // 但这个图片太大不适合做Icon
        // 为把它缩小点，先要取出这个Icon的image ,然后缩放到合适的大小
        Image smallImag = mage.getScaledInstance(70, 55, Image.SCALE_FAST);
        // 再由修改后的Image来生成合适的Icon
        ImageIcon smallIco = new ImageIcon(smallImag);
        JLabel laName = new JLabel(userName,smallIco,JLabel.CENTER);
        laName.setBounds(20, 5, 180, 50);
        laName.setFont(new Font("华文新魏", Font.BOLD, 25));
//        laName.setForeground(Color.cyan);//设置标签字体颜色

        //金钱图标
        url = Main.class.getResource("/image/money4.jpg");
        ImageIcon imoney = new ImageIcon(url); // Icon由图片文件形成
        Image image = imoney.getImage(); // 但这个图片太大不适合做Icon
        // 为把它缩小点，先要取出这个Icon的image ,然后缩放到合适的大小
        Image smallImage = image.getScaledInstance(70, 55, Image.SCALE_FAST);
        // 再由修改后的Image来生成合适的Icon
        ImageIcon smallIcon1 = new ImageIcon(smallImage);
        JLabel laMoney = new JLabel(money,smallIcon1,JLabel.CENTER);
        laMoney.setBounds(320, 5, 150, 50);
        laMoney.setFont(new Font("华文新魏", Font.BOLD, 25));

        JButton btnModel = new JButton("娱乐模式");
        btnModel.setBounds(141, 200, 200, 80);
        btnModel.setFont(new Font("华文新魏", Font.BOLD, 35));
        btnModel.setContentAreaFilled(false);//按钮设置为透明，这样就不会挡着后面的背景
        btnModel.setBorder(BorderFactory.createRaisedBevelBorder());
     //   btnModel.setBorderPainted(false);//按钮边框
        btnModel.setBackground(Color.orange);//设置按钮颜色

        JButton btnTrick = new JButton("趣味闯关");
        btnTrick.setBounds(141, 345, 200, 80);
        btnTrick.setFont(new Font("华文新魏", Font.BOLD, 35));
        btnTrick.setBorder(BorderFactory.createRaisedBevelBorder());//设置按钮突出
        btnTrick.setContentAreaFilled(false); //按钮设置为透明，这样就不会挡着后面的背景
        btnTrick.setBackground(Color.orange);//设置按钮颜色

        JButton btnRank = new JButton("排行榜");
        btnRank.setBounds(100, 490, 120, 50);
        btnRank.setFont(new Font("华文行楷", Font.BOLD, 25));
        btnRank.setContentAreaFilled(false); //按钮设置为透明，这样就不会挡着后面的背景
        btnRank.setBorder(BorderFactory.createRaisedBevelBorder());//设置按钮突出

        JButton btnExit = new JButton("退出");
        btnExit.setBounds(285, 490, 120, 50);
        btnExit.setFont(new Font("华文行楷", Font.BOLD, 25));
//        btnExit.setBackground(Color.green);
        //按钮设置为透明，这样就不会挡着后面的背景
        btnExit.setContentAreaFilled(false);
        btnExit.setBorder(BorderFactory.createRaisedBevelBorder());

//        //播放音乐图标
//        JButton btnStart=new JButton();
//        btnStart.setBounds(420, 80,50,50);
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
//        JButton btnStop=new JButton();
//        btnStop.setBounds(420, 80,50,50);
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

//        //播放音乐（窗口打开时）
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowOpened(WindowEvent e) {
//                playMusic();
//                //只能不播放
//                btnStop.setVisible(false);
//                btnStop.setEnabled(false);
//            }
//        });

//        //播放音乐开始按钮
//        btnStart.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                stopMusic();
//                //只能不播放
//                btnStart.setEnabled(false);
//                btnStart.setVisible(false);
//                btnStop.setVisible(true);
//                btnStop.setEnabled(true);
//            }
//        });
//
//        //停止播放
//        btnStop.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                playMusic();
//                //只能播放
//                btnStart.setVisible(true);
//                btnStop.setVisible(false);
//                btnStart.setEnabled(true);
//                btnStop.setEnabled(false);
//            }
//        });

        //排行榜
        btnRank.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RankUI(d);
            }
        });

        //退出事件响应
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(modeSel.this, "你确定不玩这么好玩的迷宫游戏嘛！", "提示", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        //娱乐模式
        btnModel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new GameUI(d,0,"", 0,0);
            }
        });

        //闯关模式
        btnTrick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new modeTrick(d);
            }
        });

        add(laName);
        add(laMoney);
        add(laTitle);
        add(btnModel);
        add(btnTrick);
        add(btnRank);
        add(btnExit);

        setVisible(true);
    }

//    //背景音乐播放
//    public void playMusic(){
//        try {
//            // 加载音乐文件
//            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/music/刘晨 - 我还有点小糊涂 (片段版伴奏).wav"));
//
//            // 创建音频剪辑
//            clip = AudioSystem.getClip();
//
//            // 打开音频剪辑并开始播放
//            clip.open(audioInputStream);
//            clip.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    //停止播放
//    public void stopMusic(){
//        System.out.println(1);
//        if (clip != null && clip.isRunning()) {
//            System.out.println(2);
//            clip.stop();
//            clip.close();
//        }
//    }

    public static void main(String[] args) {
        new modeSel(new Database());
    }
}

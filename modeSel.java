//ctrl+Alt+L 对齐代码
package com.sdxf.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class modeSel extends JFrame {
    Database d;

    public modeSel(Database d) {
        this.d = d;
        //数据库调用显示
        String userName = d.getUsername();
        String money =String.valueOf(d.getMoney());

        //窗口设计
        setTitle("迷宫游戏——游戏模式——"+userName);
        setDefaultCloseOperation(this.EXIT_ON_CLOSE);//结束程序

        setSize(500, 700);

        setLayout(null);
        setLocationRelativeTo(null);//窗口居中显示
         setResizable(false);//设置窗口为不可缩放

        //给窗口加上背景图片
        JLabel backGround = new JLabel();
        ImageIcon icon = new ImageIcon("src/image/Back1.jpg");//加载图像文件到ImageIcon对象中
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


        JLabel laName = new JLabel("用户名：" + userName);
        laName.setBounds(40, 0, 150, 50);
        laName.setFont(new Font("华文新魏", Font.BOLD, 25));
//        laName.setForeground(Color.cyan);//设置标签字体颜色

        ImageIcon imoney = new ImageIcon("src/image/money.jpg"); // Icon由图片文件形成
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
        btnModel.setBackground(Color.orange);//设置标签颜色

        JButton btnTrick = new JButton("趣味闯关");
        btnTrick.setBounds(141, 345, 200, 80);
        btnTrick.setFont(new Font("华文新魏", Font.BOLD, 35));
        btnTrick.setBorder(BorderFactory.createRaisedBevelBorder());//设置按钮突出
        btnTrick.setContentAreaFilled(false); //按钮设置为透明，这样就不会挡着后面的背景
        btnTrick.setBackground(Color.orange);//设置标签颜色

        JButton btnRank = new JButton("排行榜");
        btnRank.setBounds(100, 480, 120, 50);
        btnRank.setFont(new Font("华文行楷", Font.BOLD, 25));
        btnRank.setContentAreaFilled(false); //按钮设置为透明，这样就不会挡着后面的背景
        btnRank.setBorder(BorderFactory.createRaisedBevelBorder());//设置按钮突出

        JButton btnExit = new JButton("退出");
        btnExit.setBounds(285, 480, 120, 50);
        btnExit.setFont(new Font("华文行楷", Font.BOLD, 25));
//        btnExit.setBackground(Color.green);
        //按钮设置为透明，这样就不会挡着后面的背景
        btnExit.setContentAreaFilled(false);
        btnExit.setBorder(BorderFactory.createRaisedBevelBorder());

        //退出事件响应
        btnRank.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RankUI(d);
            }
        });
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(modeSel.this, "你确定不玩这么好玩的迷宫游戏嘛！", "提示", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        btnModel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GameUI(d,0,"");
            }
        });
        btnTrick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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


    public static void main(String[] args) {
        new modeSel(new Database());
    }


}

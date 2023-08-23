package com.sdxf.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class modeTrick extends JFrame {
    public modeTrick(Database d) {
        String [] data={"1","2"};
        //窗口设计
        setTitle("闯关模式——关卡选择");
        setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        setSize(500, 700);
        setLayout(null);//自由布局
        setLocationRelativeTo(null);//窗口居中显示

        //标签设计
        JButton back=new JButton();
        //设置返回图标
        ImageIcon imaReturn = new ImageIcon("src/image/return2.jpg"); // Icon由图片文件形成
        Image image = imaReturn.getImage(); // 但这个图片太大不适合做Icon
        // 为把它缩小点，先要取出这个Icon的image ,然后缩放到合适的大小
        Image smallImage = image.getScaledInstance(75, 55, Image.SCALE_FAST);
        // 再由修改后的Image来生成合适的Icon
        ImageIcon smallIcon1 = new ImageIcon(smallImage);
        // 最后设置它为按钮的图片
        back.setIcon(smallIcon1);
        back.setBounds(10, 10, 70, 50);//设置标签位置及大小
//        back.setFont(new Font("微软雅黑", Font.BOLD, 35));//设置标签字体及大小

        JLabel username=new JLabel("用户名:"+d.getUsername());
        username.setBounds(300,10,150,50);
        JLabel money=new JLabel("金币:"+d.getMoney());
        money.setBounds(400,10,150,50);
        JLabel lalevel=new JLabel("游戏关卡");
        lalevel.setBounds(200,100,500,50);
        JList levels=new JList(data);
        levels.setBounds(90,170,300,400);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modeTrick.this.dispose();
            }
        });

        add(back);
        add(username);
        add(money);
        add(lalevel);
        add(levels);

        setVisible(true);

    }
    public static void main(String[] args) {
        new modeTrick(new Database());
    }
}

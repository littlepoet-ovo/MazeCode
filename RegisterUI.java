package com.sdxf.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Component;
import java.net.URL;
import java.sql.SQLException;
import javax.swing.*;

public class RegisterUI extends JFrame implements ActionListener {
    JTextField account_jtf = new JTextField();
    JPasswordField password_jtf = new JPasswordField();
    JButton register_btn = new JButton("注册");
    JButton back_btn = new JButton("返回登录");
    Database d;

    public RegisterUI(Database d) {
        this.d = d;
        this.init();
        this.setTitle("迷宫游戏 - 注册");
        this.setSize(400, 270);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(2);
        this.setLayout(null);
        this.setResizable(false);
    }

    public void init() {
        //int ans = d.login(,);

        JLabel account_jlb = new JLabel("账号");
        JLabel password_jlb = new JLabel("密码");
        JLabel account_icon = new JLabel();
        JLabel password_icon = new JLabel();

        JPanel pane=new JPanel();
        pane.setLayout(null);

        URL url=Main.class.getResource("/image/account2.png");
        ImageIcon icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(25,25,Image.SCALE_DEFAULT));
        account_icon.setIcon(icon);

        url=Main.class.getResource("/image/password2.png");
        icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(25,25,Image.SCALE_DEFAULT));
        password_icon.setIcon(icon);

        account_icon.setBounds(20,20,30,50);
        account_jlb.setBounds(50,20,60,50);
        account_jlb.setFont(new Font("华文行楷",Font.BOLD,25));
        account_jtf.setBounds(100,30,220,30);
        account_jtf.setFont(new Font("华文新魏",Font.BOLD,20));
        password_icon.setBounds(20,90,30,50);
        password_jlb.setBounds(50,90,60,50);
        password_jlb.setFont(new Font("华文行楷",Font.BOLD,25));
        password_jtf.setBounds(100,100,220,30);
//        password_jtf.setFont(new Font("华文新魏",Font.BOLD,20));

        register_btn.setBounds(70,170,120,40);
        back_btn.setBounds(220,170,120,40);
        register_btn.setFont(new Font("华文行楷",Font.BOLD,20));
        back_btn.setFont(new Font("华文行楷",Font.BOLD,20));
        register_btn.addActionListener(this);
        register_btn.setContentAreaFilled(false);
        register_btn.setBorder(BorderFactory.createRaisedBevelBorder());
        back_btn.addActionListener(this);
        back_btn.setContentAreaFilled(false);
        back_btn.setBorder(BorderFactory.createRaisedBevelBorder());

        pane.add(account_icon);
        pane.add(account_jlb);
        pane.add(account_jtf);
        pane.add(password_icon);
        pane.add(password_jlb);
        pane.add(password_jtf);
        pane.add(register_btn);
        pane.add(back_btn);

        JLabel lab=new JLabel();
        url=Main.class.getResource("/image/beijiin.jpeg");
        icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(400,300,Image.SCALE_DEFAULT));
        lab.setIcon(icon);
        lab.setBounds(0,0,400,300);
        //lab.setOpaque(true);
        pane.add(lab);

        add(pane);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.register_btn) {
            String account = this.account_jtf.getText();
            String password = this.password_jtf.getText();
            if(account.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(null,"账号密码不能为空");
                return ;
            }

            int register_state=d.registrant(account,password,50);

            if(register_state==200){
                JOptionPane.showMessageDialog(null,"注册成功！,请返回登录");
            }else {
                JOptionPane.showMessageDialog(null,"注册失败！,账号名已存在");
            }

        } else if (e.getSource() == this.back_btn) {
            this.dispose();
        }
    }
}

package com.sdxf.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Component;
import java.net.URL;
import javax.swing.*;

public class LoginUI extends JFrame implements ActionListener {
    JTextField account_jtf = new JTextField();
    JPasswordField password_jtf = new JPasswordField();
    JButton login_btn = new JButton("登录");
    JButton register_btn = new JButton("去注册");
    Database d;

    public LoginUI(Database d) {
        this.d = d;
        this.init();
        this.setTitle("登录");
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(2);
        this.setLayout(null);
    }

    public void init() {
        JLabel account_jlb = new JLabel("账号");
        JLabel password_jlb = new JLabel("密码");
        JLabel account_icon = new JLabel();
        JLabel password_icon = new JLabel();

        JPanel pane=new JPanel();
        pane.setLayout(null);

        URL url=Main.class.getResource("/image/account.jpeg");
        ImageIcon icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(25,25,Image.SCALE_DEFAULT));
        account_icon.setIcon(icon);

        url=Main.class.getResource("/image/password.jpeg");
        icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(25,25,Image.SCALE_DEFAULT));
        password_icon.setIcon(icon);


        account_icon.setBounds(20,20,30,50);
        account_jlb.setBounds(50,20,60,50);
        account_jlb.setFont(new Font("宋体",Font.BOLD,15));
        account_jtf.setBounds(100,30,220,30);
        password_icon.setBounds(20,90,30,50);
        password_jlb.setBounds(50,90,60,50);
        password_jlb.setFont(new Font("宋体",Font.BOLD,15));
        password_jtf.setBounds(100,100,220,30);

        login_btn.setBounds(70,170,120,40);
        register_btn.setBounds(220,170,120,40);
        register_btn.setFont(new Font("宋体",Font.BOLD,15));
        login_btn.setFont(new Font("宋体",Font.BOLD,15));
        register_btn.addActionListener(this);
        login_btn.addActionListener(this);

        pane.add(account_icon);
        pane.add(account_jlb);
        pane.add(account_jtf);
        pane.add(password_icon);
        pane.add(password_jlb);
        pane.add(password_jtf);
        pane.add(login_btn);
        pane.add(register_btn);

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
        if (e.getSource() == this.login_btn) {
            String account = this.account_jtf.getText();
            String password = this.password_jtf.getText();
            int login_state=d.login(account,password);
            if(login_state==200){
                JOptionPane.showMessageDialog(null,"登录成功！");
                this.dispose();

                //进入下一界面
            }else if(login_state==-1){
                JOptionPane.showMessageDialog(null,"账号不存在！");
            }else if(login_state==-2){
                JOptionPane.showMessageDialog(null,"账号或密码错误！");
            }

        } else if (e.getSource() == this.register_btn) {
            new RegisterUI(d);
            this.dispose();
        }
    }
}

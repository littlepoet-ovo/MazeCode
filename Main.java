package com.sdxf.game;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Database d = new Database();
        int link_state=d.link();
        if(link_state==200){
            new LoginUI(d);//进入登录页面
        }else {
            JOptionPane.showMessageDialog(null,"数据库连接失败！");
        }
    }
}

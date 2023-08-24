package com.sdxf.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class modeTrick extends JFrame {
    public modeTrick(Database d) {
        //String [] data={"1","2"};
        //窗口设计
        setTitle("迷宫游戏 - 闯关模式 - 关卡选择 - "+d.getUsername());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                new modeSel(d);
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               dispose();
            }
        });        setSize(500, 700);
        setLayout(null);//自由布局
        setLocationRelativeTo(null);//窗口居中显示

        //标签设计
        JButton back=new JButton();
        back.setBorder(BorderFactory.createRaisedBevelBorder());//设置按钮突出
        //设置返回图标
        URL url = Main.class.getResource("/image/return2.jpg");
        ImageIcon icon = new ImageIcon(url);//加载图像文件到ImageIcon对象中
        Image image = icon.getImage(); // 但这个图片太大不适合做Icon
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

        MapUtil maputil=new MapUtil(9,12);
        JList levels=new JList(maputil.getLevelList());
        levels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane jslevels=new JScrollPane(levels);
        jslevels.setBounds(50,170,60,400);

        levels.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==1) {
                    String s = levels.getSelectedValue().toString();
                    String itemName = s.substring(1, s.length() - 1);
                    int currIndex = levels.getSelectedIndex();
                    int sumIndex = levels.getModel().getSize();
                    new GameUI(d, 1, itemName, currIndex, sumIndex);
                    dispose();
                }
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modeTrick.this.dispose();
                new modeSel(d);
            }
        });

        add(back);
        add(username);
        add(money);
        add(lalevel);
        add(jslevels);

        setVisible(true);

    }
    public static void main(String[] args) {
        new modeTrick(new Database());
    }
}

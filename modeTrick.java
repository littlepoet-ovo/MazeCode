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
        setTitle("迷宫游戏 - 闯关模式 - 关卡选择 - " + d.getUsername());
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
        });
        setSize(500, 700);
        setLayout(null);//自由布局
        setLocationRelativeTo(null);//窗口居中显示

        //标签设计
        JButton back = new JButton();
        back.setBorder(BorderFactory.createRaisedBevelBorder());//设置按钮突出
        //设置背景
        JLabel backGround = new JLabel();
        URL url = Main.class.getResource("/image/BackGround1.jpg");
        ImageIcon icon = new ImageIcon(url);//加载图像文件到ImageIcon对象中
        backGround.setIcon(icon);//将ImageIcon设置成JLabel的图标
        JPanel imPanel=(JPanel) this.getContentPane();//注意内容面板必须强转为JPanel才可以实现下面的设置透明
        imPanel.setOpaque(false);//将内容面板设为透明
        backGround.setBounds(0, 0, this.getWidth(), this.getHeight());//设置标签位置大小，记得大小要和窗口一样大
        icon.setImage(icon.getImage().getScaledInstance(backGround.getWidth(), backGround.getHeight(), Image.SCALE_DEFAULT));//图片自适应标签大小
        this.getLayeredPane().add(backGround, Integer.valueOf(Integer.MIN_VALUE));//标签添加到层面板

        //设置返回图标
        url = Main.class.getResource("/image/return2.jpg");
        ImageIcon con = new ImageIcon(url);//加载图像文件到ImageIcon对象中
        Image image = con.getImage(); // 但这个图片太大不适合做Icon
        // 为把它缩小点，先要取出这个Icon的image ,然后缩放到合适的大小
        Image smallImage = image.getScaledInstance(75, 55, Image.SCALE_FAST);
        // 再由修改后的Image来生成合适的Icon
        ImageIcon smallIcon1 = new ImageIcon(smallImage);
        // 最后设置它为按钮的图片
        back.setIcon(smallIcon1);
        back.setBounds(10, 10, 70, 50);//设置标签位置及大小
//        back.setFont(new Font("微软雅黑", Font.BOLD, 35));//设置标签字体及大小

        JLabel username = new JLabel("用户名:" + d.getUsername());
        username.setBounds(100, 10, 150, 50);
        username.setFont(new Font("华文新魏", Font.BOLD, 25));

        JLabel money = new JLabel("金币:" + d.getMoney());
        money.setBounds(300, 10, 150, 50);
        money.setFont(new Font("华文新魏", Font.BOLD, 25));

        JLabel lalevel = new JLabel("游戏关卡");
        lalevel.setBounds(160, 100, 500, 50);
        lalevel.setFont(new Font("华文彩云", Font.BOLD, 35));//设置标签字体及大小

        MapUtil maputil = new MapUtil(9, 12);
        JList levels = new JList(maputil.getLevelList());
        levels.setCellRenderer(new MyListCellRenderer());
        levels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lalevel.setOpaque(false);
        JScrollPane jslevels = new JScrollPane(levels);
        this.getContentPane().add(jslevels);
        jslevels.setBounds(115, 220, 250, 320);

        levels.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
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
        add(backGround);
        setVisible(true);

    }

    class MyListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//            Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            // 根据选择状态来设置背景色和前景色
            if (isSelected) {
                renderer.setBackground(Color.BLUE);
                renderer.setForeground(Color.WHITE);
            } else {
                renderer.setBackground(Color.WHITE);
                renderer.setForeground(Color.BLACK);
            }
            renderer.setBackground(new Color(1,3,4,5));
            Font font = renderer.getFont();
            renderer.setFont(new Font("华文行楷", Font.PLAIN, 30));
            // 设置其他样式，例如字体、边框等
//            renderer.setFont(new Font("Arial", Font.PLAIN, 12));
            renderer.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 10));
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            return renderer;
        }
    }
    public static void main(String[] args) {
        new modeTrick(new Database());
    }
}

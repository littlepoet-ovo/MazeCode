package com.sdxf.game;
//丁子杰
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class RankUI extends JFrame implements ActionListener{
    Database d;
    JButton selJB = new JButton("加载");
    JComboBox<String> modleSel = new JComboBox<>(new String[] {"金币排行", "闯关排行"});
    JComboBox<String> levSel = new JComboBox<>(new String[] {"level1", "level2"});
    JScrollPane moneyJSP;
    JTable moneyT;
    JScrollPane levelJSP;
    JTable levelT;
    public RankUI(Database d) {
        this.d = d;
        initJFrame();
        initHead();
        initMoneyTable();
        initModTable();
        levelJSP.setVisible(false);
        //setFont(new Font("宋体",Font.BOLD,15));
        this.setVisible(true);
    }
    private void initModTable() {
        levelT = new JTable(new DefaultTableModel(0, 5));
        DefaultTableModel model = (DefaultTableModel) levelT.getModel();
        String[] columns = {"序号", "账号", "花费时间", "使用金币", "通关日期"};
        model.setColumnIdentifiers(columns);
        TableColumnModel columnModel = levelT.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(1).setPreferredWidth(70);
        columnModel.getColumn(2).setPreferredWidth(60);
        columnModel.getColumn(3).setPreferredWidth(60);
        columnModel.getColumn(4).setPreferredWidth(120);
//        model.addRow(new Object[]{"1", "user123", 100});
        levelJSP = new JScrollPane(levelT); // Wrap the table in a JScrollPane
        levelJSP.setBounds(10, 80, 350, 400);
        this.getContentPane().add(levelJSP); // Add the JScrollPane to the content pane
    }
    private void initMoneyTable() {
        moneyT = new JTable(new DefaultTableModel(0, 3));
        DefaultTableModel model = (DefaultTableModel) moneyT.getModel();
        String[] columns = {"序号", "账号", "金币数"};
        model.setColumnIdentifiers(columns);
        TableColumnModel columnModel = moneyT.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(210);
        columnModel.getColumn(2).setPreferredWidth(70);
//        model.addRow(new Object[]{"1", "user123", 100});
        moneyJSP = new JScrollPane(moneyT); // Wrap the table in a JScrollPane
        moneyJSP.setBounds(10, 80, 350, 400);
        this.getContentPane().add(moneyJSP); // Add the JScrollPane to the content pane
    }

    private void initHead() {
        JLabel jlb1 = new JLabel("排行榜类型:");
        jlb1.setFont(new Font("宋体",Font.BOLD,15));
        jlb1.setBounds(10,10,90,30);

        modleSel.setSelectedItem("金币排行"); // 设置默认选择
        modleSel.setBounds(105,10,90,30);
        modleSel.setFont(new Font("宋体",Font.BOLD,15));

        JLabel jlb2 = new JLabel("关卡类型：");
        jlb2.setFont(new Font("宋体",Font.BOLD,15));
        jlb2.setBounds(200,10,80,30);

        levSel.setBounds(270,10,90,30);
        levSel.setFont(new Font("宋体",Font.BOLD,15));
        jlb2.setVisible(false);
        levSel.setVisible(false);
        selJB.setBounds(10,45,350,30);
        selJB.setFont(new Font("宋体",Font.BOLD,15));
        this.getContentPane().add(jlb1);
        this.getContentPane().add(modleSel);
        this.getContentPane().add(jlb2);
        this.getContentPane().add(levSel);
        this.getContentPane().add(selJB);
        selJB.addActionListener(this);
        modleSel.addActionListener(e -> {
            JComboBox<String> combo = (JComboBox<String>) e.getSource();
            String selectedValue = (String) combo.getSelectedItem();
            if(selectedValue.equals("闯关排行")){
                levelJSP.setVisible(true);
                moneyJSP.setVisible(false);
                jlb2.setVisible(true);
                levSel.setVisible(true);
            }else {
                levelJSP.setVisible(false);
                moneyJSP.setVisible(true);
                jlb2.setVisible(false);
                levSel.setVisible(false);
            }
            System.out.println("选中的选项: " + selectedValue);
        });
    }

    private void initJFrame() {
        this.setSize(380,520);
        this.setTitle("排行榜");
        this.setLocationRelativeTo(null);//居中
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);//关闭模式设置
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(null,"将要关闭窗口");
                System.exit(0);
//                dispose();
            }
        });
        this.setLayout(null);//清除格式

    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Database d = new Database();
        d.link();
        RankUI r = new RankUI(d);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if(o==selJB){
            String mod = modleSel.getItemAt(modleSel.getSelectedIndex());
            String lev = levSel.getItemAt(levSel.getSelectedIndex());
            String data = "";
            DefaultTableModel model;
            if(mod.equals("闯关排行")){
                data = d.getGameRank(lev);
                String[] dataP = data.split("\\|");
                model = (DefaultTableModel) levelT.getModel();
                while (model.getRowCount()>0){
                    model.removeRow(0);
                }
                for (int i = 0; i < dataP.length; i++) {
                    String[] dataS = dataP[i].split("&");
                    String[] rslt = new String[5];
                    rslt[0] = String.format("%d",i+1);
                    for (int j = 0; j < 4; j++) {
                        rslt[j+1] = dataS[j];
                    }
                    model.addRow(rslt);

                }

            }else if(mod.equals("金币排行")){
                model = (DefaultTableModel) moneyT.getModel();
                while (model.getRowCount()>0){
                    model.removeRow(0);
                }
                data = d.getMoneyRank();
                String[] dataP = data.split("\\|");
                for (int i = 0; i < dataP.length; i++) {
                    String[] dataS = dataP[i].split("&");
                    String[] rslt = new String[3];
                    rslt[0] = String.format("%d",i+1);
                    for (int j = 0; j < 2; j++) {
                        rslt[j+1] = dataS[j];
                    }
                    model.addRow(rslt);

                }
            }
            System.out.println(data);
        }

    }
}

package com.sdxf.game;

import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel{//游戏迷宫面板
    GameRunningData grd;
    int[][] map;//游戏迷宫二维数组
    MapUtil mapUtil;
    int row,col;
    int leftX,leftY;
    //JTable table = new JTable(new DefaultTableModel(row,col));
    //JTable table = new JTable(10,10);
    int flag=0;
    int currentRow=0,currentColumn=1;
    int newRow=0,newColumn=1;
    JTable table;
    int totalmoney=0,money=0;
    Database d;
    int gameModel;

    public GamePanel(Database d,int row, int col, GameRunningData grd, int gameModel, String levelName){
        this.grd = grd;
        this.row = row;
        this.col = col;
        this.d=d;
        this.gameModel=gameModel;
        leftX = 0;
        leftY = 0;
        mapUtil = new MapUtil(row, col);
        map = mapUtil.getNeedMap(gameModel,levelName);
        setBounds(10,120,765,500);

        table = new JTable(2*row+1,2*col+1);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        String[] columns = new String[2*col+1];
        for(int i=0;i<2*col+1;i++) columns[i]="";
        model.setColumnIdentifiers(columns);

        int cellSize = 25; // 设置单元格的尺寸
        table.setRowHeight(cellSize);
        for (int column = 0; column < table.getColumnCount(); column++) {
            table.getColumnModel().getColumn(column).setPreferredWidth(cellSize);
        }

        for (int column = 0; column < table.getColumnCount(); column++) {
            table.getColumnModel().getColumn(column).setCellRenderer(renderer);
        }

        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(765,500));

        this.add(scrollPane);
        //设置焦点到入口单元格
        startMove();
    }

    TableCellRenderer renderer = new DefaultTableCellRenderer() {
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            int val=map[row][column];
            if (val==-2) {
                cellComponent.setBackground(Color.GRAY);
            } else if (val==-1) {
                cellComponent.setBackground(Color.white);
            }else if(val==0){
                cellComponent.setBackground(Color.RED);
            }else {
                cellComponent.setBackground(Color.CYAN);
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setValueAt(val,row,column);
            }

            //当传来的数据为颜色时就改变背景颜色
            if(value instanceof Color){
                cellComponent.setBackground((Color)value);
                setText("");//单元格的文本设置为空字符串，从而隐藏单元格的内容
            }
            //撞墙时保持焦点不变
            if(flag==1) {
                Focus();
            }

            return cellComponent;
        }
    };
    public void Focus(){
        table.changeSelection(currentRow,currentColumn, false, false);//设置焦点为键盘点击之前的
        table.requestFocus();//单独默认00
    }

    //键盘和鼠标监听事件
    private void startMove() {
        table.changeSelection(0, 1, false, false);
        table.requestFocus();//单独默认00
        //键盘
        table.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                moveKey(e.getKeyCode());
            }
            public void keyTyped(KeyEvent e) {
            }
            public void keyReleased(KeyEvent e) {
            }
        });
        //鼠标
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if(Math.abs(currentColumn-col)==1&&currentRow==row || Math.abs(currentRow-row)==1&&currentColumn==col){
                    moveMouse(row, col);
                }
            }
        });
    }

    //玩家移动
    private void movePlayer() {

        if (isValidateMove(newRow, newColumn) == true) {//判断是否为障碍物
            if(map[newRow][newColumn]>=1&&map[newRow][newColumn]<=9){
                money=map[newRow][newColumn];
                totalmoney+=money;
                //传递收费站金额
              grd.useMoney(money);
            }

            // 更新玩家位置
            if (table.getValueAt(newRow, newColumn) == Color.black) {//返回黑色消失
                table.setValueAt("", currentRow, currentColumn);
                table.setValueAt(Color.orange,newRow,newColumn);
            } else  {
                if(map[currentRow][currentColumn]==-1) {//前进路径变成黑色，自身变成黄色
                    table.setValueAt(Color.black, currentRow, currentColumn);
                }
                table.setValueAt(Color.orange,newRow, newColumn);
            }

            //当到达终点时弹出对话框
            if (map[newRow][newColumn] == 0 && newRow ==18&& newColumn ==23 ) {
                //传送总金额
                grd.Winning(totalmoney+10);
            }

            //实时更新用户所在位置
            currentRow = newRow;
            currentColumn = newColumn;
            //更新位置
            table.repaint();
        }

        //如果下一步是墙保持原来位置不变
        else if(isValidateMove(newRow, newColumn) == false){
            newRow = currentRow;
            newColumn = currentColumn;
        }
    }

    //判断路径是否合法
    private boolean isValidateMove(int x,int y){
        if(x<=0||x>18||y<=0||y>24||map[x][y]==-2){
            flag=1;
            table.transferFocus();//单独默认00
            return false;
        }
        else return true;
    }

    //键盘操作
    private void moveKey(int keyCode){

        if(map[currentRow][currentColumn]!=-2) {
            switch (keyCode) {
                case KeyEvent.VK_UP:
                    newRow--;
                    break;
                case KeyEvent.VK_DOWN:
                    newRow++;
                    break;
                case KeyEvent.VK_LEFT:
                    newColumn--;
                    break;
                case KeyEvent.VK_RIGHT:
                    newColumn++;
                    break;
            }
        }
        movePlayer();

    }

    //鼠标操作
    private void moveMouse(int row, int col){
        if(map[currentRow][currentColumn]!=-2) {
            newRow = row;
            newColumn = col;
        }
        movePlayer();
    }

}

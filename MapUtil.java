
package com.sdxf.game;

import java.util.*;
import java.io.*;


class Edge{

    int start;//边的起点
    int end;//边的终点
    int value;//边的权值

    public Edge(){

    }

    public Edge(int start,int end,int value){
        this.start = start;//边的起点
        this.end = end;//边的终点
        this.value = value;//边的权值
    }


}
class Union {

    int count;
    int[] parent;//记录根节点
    int[] rank;//层数

    public Union(int count){
        this.count = count;
        parent = new int[count];
        rank = new int[count];
        for(int i=0;i<count;i++){
            parent[i] = i;
            rank[i] = 1;
        }
    }

    //返回根节点的同时进行路径压缩
    public int find(int p){
        while(parent[p]!=p){
            p = parent[p];//迭代
            parent[p] = parent[parent[p]];//路径压缩
        }
        return parent[p];
    }

    //返回两元素是否相连
    public boolean isConnected(int p,int q){
        return find(p)==find(q);
    }

    //连接两元素
    public void union(int p,int q){
        int pRoot = find(p);
        int qRoot = find(q);
        if(pRoot == qRoot){
            return ;
        }
        if(rank[pRoot]<rank[qRoot]){//根节点树层数不变
            parent[pRoot] = qRoot;
        }else if(rank[pRoot]>rank[qRoot]){
            parent[qRoot] = pRoot;
        }else{
            parent[pRoot] = qRoot;//p加到q上
            rank[qRoot]++;//路径压缩，新加的树最多两层，根节点树层数只加一
        }
    }

}

public class MapUtil {
    Union unionUtil;
    ArrayList<Edge> list;//存储（row-1）*col+(col-1)*row条带权边
    ArrayList<Edge> map;//存储需要打通的边
    int row;
    int col;

    public MapUtil(int row,int col){
        this.row = row;
        this.col = col;
        unionUtil = new Union(row*col);
        list = new ArrayList<Edge>();
        map = new ArrayList<Edge>();
        init();
        //mapStore();
        //getLocalMap(3);
        //System.out.println("地图文件中有的地图数量：");
        //System.out.println(getLocalMapSize());
        //System.out.println("地图文件的地图名称列表：");
        //getLevelList();
    }

    public void init(){
        for(int i=0;i<col-1;i++){//网格地图最上横边添加
            list.add(new Edge(i,i+1,(int) (Math.random()*100000)));
        }

        for(int i=0;i<row-1;i++){//网格地图最左纵边添加
            list.add(new Edge(i*col,(i+1)*col,(int) (Math.random()*100000)));
        }

        for(int i=1;i<row;i++){
            for(int j=1;j<col;j++){
                list.add(new Edge(i*col+j-1,i*col+j,(int) (Math.random()*100000)));//每个点左邻边
                list.add(new Edge((i-1)*col+j,i*col+j,(int) (Math.random()*100000)));//每个点上邻边
            }
        }

        Collections.sort(list,new Comparator<Edge>() {//升序排序
            @Override
            public int compare(Edge o1, Edge o2) {
                if(o1.value<o2.value)
                    return -1;
                else
                    return 1;
            }
        });//根据 Collections.sort() 的实现，当比较器返回负数时，表示第一个元素应该排在第二个元素之前，即升序排序

        int count = 0;
        //最小生成树
        for(Edge edge:list){
            int start = edge.start;
            int end = edge.end;
            if(!unionUtil.isConnected(start, end)){//如果还没相连
                unionUtil.union(start, end);//连接
                map.add(edge);//将打通的通道存储
                count++;
                if(count==col*row-1)//生成迷宫完毕
                    break;
            }
        }

    }

    public void mapStore(){
        int [][] target=getRandomMap(getMap());

        int num=getLocalMapSize();
            num++;
        try {
            // 创建一个输出流来写入文件
            String  name="src\\map\\"+num+".map";
            FileOutputStream fileOut = new FileOutputStream(name);

            // 创建一个对象输出流来将数组序列化为字节流并写入文件
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

            // 写入数组对象
            objOut.writeObject(target);

            // 关闭输出流
            objOut.close();

            //System.out.println("数组已成功序列化到文件。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int [][] getLocalMap(int num){
        int [][] array=null;

        String  name="src\\map\\"+num+".map";
        try {
            // 创建一个输入流来读取文件
            FileInputStream fileIn = new FileInputStream(name);

            // 创建一个对象输入流来读取字节流并反序列化为对象
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            // 读取对象并转换为二维数组
             array = (int[][]) objIn.readObject();

            // 关闭输入流
            objIn.close();

            // 打印二维数组
//            for (int[] row : array) {
//                for (int val : row) {
//                    System.out.print(val + " ");
//                }
//                System.out.println();
//            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return array;
    }

    public int [][] getRandomMap(int [][] map){
       int[] dx={-1,1,0,0};//左右上下的顺序
       int[] dy={0,0,-1,1};
       int row=map.length;
       int col=map[0].length;
       Random random = new Random();
       int cnt=0;
       //int ran=random.nextInt(100);

       for(int i=0;i<row;i++){
           for(int j=0;j<col;j++){
               int ran=random.nextInt(100);

               int flag1=0,flag2=0,flag3=0,flag4=0;
               if(map[i][j]==-1&&ran%6==1){//该点为通路，且近1/6的概率设置收费站
                   for(int k=0;k<4;k++){
                       int x=i+dx[k];
                       int y=j+dy[k];
                       if (x < 1 || x >= row-1 || y< 1 || y >= col-1) continue;

                       if(map[x][y]==-1){
                           if(k==0){//该点左方的点为路
                               flag1=1;
                           }else if(k==1){//该点右方的点为路
                               flag2=1;
                           }else if(k==2){//该点上方的点为路
                               flag3=1;
                           }else{//该点下方的点为路
                               flag4=1;
                           }
                       }
                   }
                   //保证只有左右通或上下通
                   if(flag1==1&&flag2==1&&flag3==0&&flag4==0||flag1==0&&flag2==0&&flag3==1&&flag4==1) map[i][j]=random.nextInt(9)+1;
                   flag1=0;flag2=0;flag3=0;flag4=0;
               }
           }
       }

//        for (int[] row1 : map) {
//            for (int val : row1) {
//                System.out.print(val + " ");
//            }
//            System.out.println();
//        }
       return map;
    }
    public int[][] getMap(){
        int[][] array1 = new int[2*row+1][2*col+1];
        //网格留空
        for(int i=1;i<2*row+1;i+=2){
            for(int j=1;j<2*col+1;j+=2){
                array1[i][j] = -1;
            }
        }

        //网格纵向画墙
        for(int i=1;i<2*row+1;i+=2){
            for(int j=2;j<2*col+1;j+=2){
                array1[i][j] = -2;
            }
        }

        //网格横向画墙
        for(int i=0;i<2*row+1;i+=2){
            for(int j=1;j<2*col+1;j++){
                array1[i][j] = -2;
            }
        }


        //边界最左边纵向画墙
        for(int i=0;i<2*row+1;i++){
            array1[i][0] = -2;
        }




        for(Edge edge:map){//打通道路
            int start = edge.start;
            int end = edge.end;
            if(Math.abs(start-end)==1){//左右连通
                int x = start/col;
                int y = start%col;
                array1[2*x+1][2*y+2] = -1;
            }else{//上下连通
                int x = start/col;
                int y = start%col;
                array1[2*x+2][2*y+1] = -1;
            }
        }

        array1[0][1]=0;//设定起点与终点
        array1[2*row][2*col-1]=0;

//        for (int[] row : array1) {//打印二维数组
//            for (int val : row) {
//                System.out.print(val + " ");
//            }
//            System.out.println();
//        }
        return array1;
    }

    public int  getLocalMapSize(){
        String directoryPath = "src/map"; // 指定目录路径
        int count = 0; // 用于记录以".map"结尾的文件数量

        File directory = new File(directoryPath);
        File[] files = directory.listFiles(); // 获取目录中的所有文件

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".map")) {
                    count++;
                }
            }
        }
        return count;
    }

    public String [] getLevelList(){
        String directoryPath = "src/map"; // 指定目录路径
        StringBuilder fileNames = new StringBuilder(); // 用于存储文件名

        File directory = new File(directoryPath);
        File[] files = directory.listFiles(); // 获取目录中的所有文件

        ArrayList<String> stringList = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    String levelName = fileName.substring(0,fileName.length()-4)+"level";
                    stringList.add(levelName);
                }
            }
        }

        String[] stringArray = stringList.toArray(new String[stringList.size()]);
       // System.out.println(Arrays.toString(stringArray));
        return stringArray;
    }

    public int[][] getNeedMap(int model,String level){
        if(model==0){//随机
            return getRandomMap(getMap());
        }else {//固定
//            String  levelName = level.substring(0,level.length()-4);
            int levelNum=Integer.parseInt(level);
            return getLocalMap(levelNum);
        }
    }


//        public void print(){
//        int index = 0;
//        for(Edge edge:map){
//            if(index%10==0)
//                System.out.println();
//            System.out.print("("+edge.start+","+edge.end+","+edge.value+")");
//            index ++;
//        }
//    }

}

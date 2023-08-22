package com.sdxf.game;
/*
    丁子杰
    link() 功能：连接数据库 返回值：200 连接成功 | -1 连接失败
    login(username,password) [账号,密码] 功能：用户登录 返回值：200 登录成功 | -1 账号不存在 | -2 密码错误
    registrant(username, password, money)[账号,密码,初始金额] 功能：用户注册 返回值：200 注册成功| -1 注册失败,该用户名已存在
    getMoney() 功能：查询用户当前金币数 返回值： -1 查询失败 | -2 用户未登录 | 其他数字 查询成功，返回值即为用户的金币数
    setMoney(money)[正数为增加金币，负数为减少金币] 返回值：200 修改成功 | -1 修改失败 | -2 改动量不能为0 | -3 当前用户未登录
    getUsername() 功能：获取用户名 若未登录返回-1
    saveGameData(useTime,useMoney,level)[花费时间(单位：秒),花费的金币,关卡等级] 功能：玩家通关之后保存数据，用于之后rank排名 返回值：201 打破纪录 | 200 成功 | -1 失败 | -2 未登录
    getGameRank(level)[关卡等级] 功能：提供关卡等级后返回排序后的名单 返回值：用&分割同一组的不同数据，用|分割不同组， 样例数据：用户名&花费时间(秒)&使用金币&通关日期
    getMoneyRank() 功能：返回根据拥有金币数量高低的排名 返回值：用户名&金币数 eg:gaodie&600|wangleru&600|zijie&300
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;
import java.util.TimeZone;
public class Database {
    private Connection connection;
    private String username = "";
    private boolean isLogin = false;
    public int link() throws ClassNotFoundException, SQLException {
        // 1.导入驱动类
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2.用户信息和url
        String url = "jdbc:mysql://localhost:3306/game?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "root";
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        // 3.获取连接
        connection = DriverManager.getConnection(url, user, password);
        if(connection!=null)
            return 200;
        else
            return -1;
    }
    public int registrant(String username, String password, int money) {
        String sql = "INSERT into user VALUES('"+username+"','"+getMD5Hash(password)+"',"+money+")";
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            stmt.close();
        }catch(Exception e){
            return -1;
        }
        return 200;

    }
    public int login(String username, String password){
        String sql = "select password from user where username='"+username+"';";
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            if(rs.getString(1).equals(getMD5Hash(password))){
                this.username = username;
                isLogin = true;
                rs.close();
                stmt.close();
                return 200;
            }else{
                rs.close();
                stmt.close();
                return -2;
            }

        }catch (Exception e){
            System.out.println(e);
            return -1;
        }

    }
    public int setMoney(int money){
        if(!isLogin) return -3;
        String sql = "";
        int soureMoney = this.getMoney();
        if(soureMoney >= 0 && soureMoney+money >= 0){
            if(money > 0){
                sql = "UPDATE user SET money=money + "+money+" WHERE username='"+this.username+"';";
            }else if(money < 0){
                sql = "UPDATE user SET money=money  "+money+" WHERE username='"+this.username+"';";
            }else{
                return -2;
            }
        }else{
            sql = "UPDATE user SET money=0 WHERE username='"+this.username+"';";
        }

        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            stmt.close();
        }catch(Exception e){
            System.out.println(sql);
            System.out.println(e);
            return -1;
        }
        return 200;

    }
    public int getMoney(){
        if(!isLogin) return -2;
        String sql = "select money from user where username='"+this.username+"';";
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getInt(1);

        }catch (Exception e){
            System.out.println(e);
            return -1;
        }

    }
    public String getUsername(){
        if(!isLogin) return "-1";
        return this.username;
    }
    public int saveGameData(int useTime, int useMoney, String level){
        if(!isLogin) return -2;
        String sql = "SELECT * FROM rank WHERE username='"+this.username+"' AND level='"+level+"';";
        LocalDateTime passDate = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                int tempT = rs.getInt("useTime");
                int tempM = rs.getInt("useMoney");
                rs.close();
                if(useMoney<=tempM&&useTime<=tempT && (useMoney!=tempM||useTime!=tempT)){
                    sql = "UPDATE rank SET useTime="+useTime+", useMoney="+useMoney+" WHERE username='"+this.username+"' AND level='"+level+"';";
                    stmt.execute(sql);
                    stmt.close();
                    return 201;
                }
            }else{
                sql = "INSERT INTO rank VALUES('"+this.username+"',"+useTime+","+useMoney+",'"+passDate.format(dtf)+"','"+level+"')";
                stmt.execute(sql);
                stmt.close();
                return 201;
            }
        }catch(Exception e){
            System.out.println(e);
            return -1;
        }
        return 200;
    }
    public String getGameRank(String level){
        String sql = "SELECT * FROM rank WHERE level='"+level+"' ORDER BY useTime , useMoney ;";
        StringJoiner sj = new StringJoiner("|");
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                String temp = rs.getString(1)+"&"+rs.getString(2)+"&"+rs.getString(3)+"&"+rs.getString(4);
                sj.add(temp);
            }
            stmt.close();
            rs.close();
        }catch (Exception e){
            System.out.println(e);
        }
        return sj.toString();
    }
    public String getMoneyRank(){
        String sql = "SELECT * FROM user ORDER BY money DESC;";
        StringJoiner sj = new StringJoiner("|");
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                String temp = rs.getString(1)+"&"+rs.getString(3);
                sj.add(temp);
            }
            stmt.close();
            rs.close();
        }catch (Exception e){
            System.out.println(e);
        }
        return sj.toString();
    }
    private static String getMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b & 0xFF));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Database d = new Database();
        d.link();
//        d.registrant("gaodie","gd123",600);
//        d.registrant("wangleru","wlr123",600);
//        d.registrant("zijie","123456",600);
//        System.out.println( d.login("gaodie", "gd123"));
//        System.out.println(d.getGameRank("2"));
//        d.registrant("zijie","123456",300);
//        int res = d.saveGameData(10,99,"2");
//        System.out.println(res);
//        System.out.println(d.getMoneyRank());

    }

}

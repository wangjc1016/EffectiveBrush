package com.wjc.brush;

import java.sql.*;

import static com.wjc.brush.MainFrame.Lmessage1;


public class Conn {
    static Connection con; // 声明Connection对象
    private static Connection connection;
    private static long updateTime;
    public static String user;
    public static  String password;

    public static Connection getConnection(String user_in, String password_in, String url_in, String name_in) { // 建立返回值为Connection的方法
        try { // 加载数据库驱动类
            Class.forName("com.mysql.cj.jdbc.Driver");
//            System.out.println("数据库驱动加载成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        user = user_in;//数据库登录名
        password = password_in;//密码
        try { // 通过访问数据库的URL获取数据库连接对象
            con = DriverManager.getConnection("jdbc:mysql://" + url_in + ":3306/" + name_in + "?useUnicode=true&characterEncoding=gbk", user, password);
            System.out.println("数据库连接成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con; // 按方法要求返回一个Connection对象
    }
    public static void main(String[] args) { // 主方法，测试连接
    }
    public static Connection connectMySQL() {
        if (connection == null || System.currentTimeMillis() - updateTime> 1000 * 120) {
            // 第一次调用时创建 Connection 对象
            connection = createConnection();
        }
        return connection;
    }
    private static Connection createConnection() {
        updateTime = System.currentTimeMillis();

        if(Lmessage1 != null) Lmessage1.setText("正在请求数据库...");
        // 创建数据库连接
        System.out.println("正在连接数据库...");
        Connection connection = getConnection("EffectiveBrush","EB123456", "47.115.217.225", "question_bank");
//        Connection connection = getConnection("root","123456", "localhost", "question_bank");
        // 设置其他数据库连接属性
        return connection;
    }

    public static ResultSet queryMySQL(String sql) throws SQLException {
        Connection connection = Conn.connectMySQL();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet;
    }
    public static int updateMySQL(String sql) throws SQLException {
        Connection connection = Conn.connectMySQL();
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

/**
 * 根据传入的sql语句执行查询，并把结果返回给第二个数组，这是题目查询
 * @param sql sql语句
 */
    static Object[][] sqlQuery(String sql) throws ClassNotFoundException, SQLException {
        updateTime = System.currentTimeMillis();
        Object[][] row;
//        连接MySQL
//        首先查询有几行，并根据行数设置数组和表格行数
        ResultSet resultSet = Conn.queryMySQL("select count(*) from (" + sql + ") question;");
        resultSet.next();
        int cntRow = Integer.parseInt(resultSet.getString("count(*)"));
        System.out.println("查询到" + cntRow + "条数据");
        row = new Object[cntRow][12];

//        5.执行SQL并返回数据集
        resultSet = Conn.queryMySQL(sql + "");
        int cnt = 0;
        while (resultSet.next()) {
            row[cnt][0] = resultSet.getString("id");
            row[cnt][1] = resultSet.getString("dic");
            row[cnt][2] = resultSet.getString("type");
            row[cnt][3] = resultSet.getString("ques");
            row[cnt][4] = resultSet.getString("ans");
            row[cnt][5] = resultSet.getString("analysis");
            row[cnt][6] = resultSet.getString("option_A");
            row[cnt][7] = resultSet.getString("option_B");
            row[cnt][8] = resultSet.getString("option_C");
            row[cnt][9] = resultSet.getString("option_D");
            row[cnt][10] = resultSet.getString("correct_count");
            row[cnt][11] = resultSet.getString("wrong_count");
            cnt++;
        }
        resultSet.close();
        return row;
    }
}

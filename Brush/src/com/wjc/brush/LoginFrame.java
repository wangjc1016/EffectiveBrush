package com.wjc.brush;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.wjc.brush.Conn.connectMySQL;
import static com.wjc.brush.Conn.updateMySQL;
import static com.wjc.brush.MainFrame.getQuestionID;
import static com.wjc.brush.MainFrame.showQuestion;
import static java.lang.System.exit;

public class LoginFrame extends JFrame implements ActionListener {

    private JLabel l_user,l_pwd;  //用户名标签，密码标签
    private JTextField t_user; //用户名文本框
    private JPasswordField t_pwd; //密码文本框
    private JButton b_ok,b_cancel; //登录按钮，退出按钮
    private int ifAdmin = 0;

    public LoginFrame(){
        super("欢迎使用刷题系统!");
        l_user=new JLabel("用户名：",JLabel.RIGHT);
        l_pwd=new JLabel("密    码：",JLabel.RIGHT);
        t_user=new JTextField(31);
        t_pwd=new JPasswordField(31);
        b_ok=new JButton("登录");
        b_cancel=new JButton("退出");
        //布局方式FlowLayout，一行排满排下一行
        Container c=this.getContentPane();
        c.setLayout(new FlowLayout());
        c.add(l_user);
        c.add(t_user);
        c.add(l_pwd);
        c.add(t_pwd);
        c.add(b_ok);
        c.add(b_cancel);
        //为按钮添加监听事件
        b_ok.addActionListener(this);
        b_cancel.addActionListener(this);
        t_user.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyChar()== KeyEvent.VK_ENTER )   //按回车键执行相应操作;
                {
                    b_ok.doClick();
                }
            }
        });

        t_pwd.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyChar()== KeyEvent.VK_ENTER )   //按回车键执行相应操作;
                {
                    b_ok.doClick();
                }
            }
        });

        //界面大小不可调整
        this.setResizable(false);
        this.setSize(455,150);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //界面显示居中
        Dimension screen = this.getToolkit().getScreenSize();
        this.setLocation((screen.width-this.getSize().width)/2,(screen.height-this.getSize().height)/2);
        this.setVisible(true);
        connectMySQL();
    }

    public void actionPerformed(ActionEvent e) {
        if(b_cancel==e.getSource()){
            //添加退出代码
            exit(0);
        }else if(b_ok==e.getSource()){
            if(ifAdmin == 0){
                int flag = 0;
                try {
                    flag = judgeLogin();
                } catch (SQLException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                if(flag == 1){
                    if(t_user.getText().equals("admin")){
                        ifAdmin = 1;
                        JFrame jFrame = new JFrame();
                        JOptionPane.showMessageDialog(jFrame, "进入新建账号模式");
                        t_user.setText("");
                        t_pwd.setText("");
                        b_ok.setText("创建");
                    }
                    else{
                        try {
                            new MainFrame(t_user.getText());
                            showQuestion(getQuestionID());
                            this.setVisible(false);
                        } catch (SQLException | ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                }
                else {
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, "账号或密码错误");
                    t_user.setText("");
                    t_pwd.setText("");
                }
            }
            else{
                String hashpwd = PasswordEncryptor.encryptPassword(t_pwd.getText());
                String sql = "insert into user(username,password) VALUES('" + t_user.getText() + "','" + hashpwd + "')";
                try {
                    updateMySQL(sql);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, "创建账号成功");
                b_ok.setText("登录");
                t_user.setText("");
                t_pwd.setText("");
                ifAdmin = 0;
            }
        }
    }

    private int judgeLogin() throws SQLException, ClassNotFoundException{
        String hashpwd = PasswordEncryptor.encryptPassword(t_pwd.getText());
        System.out.println(hashpwd);
        String sql = "SELECT count(*) FROM user where username = '" + t_user.getText() + "' && password = '" + hashpwd + "'";
        Connection connection = connectMySQL();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        String res = resultSet.getString(1);
        if(!res.equals("0")){
            return 1;
        }
        return -1;
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}

class PasswordEncryptor {
    public static String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}

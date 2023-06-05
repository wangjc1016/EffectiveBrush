package com.wjc.brush;

import com.sun.jdi.Value;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import static com.wjc.brush.Conn.sqlQuery;
import static com.wjc.brush.Conn.updateMySQL;
import static java.lang.System.exit;

public class MainFrame extends JFrame implements ActionListener {
    private static JLabel Lid;
    private static JLabel Lidtext;
    private static JLabel Ldic;
    private static JLabel Ltype;
    private static JLabel Lques;
    private static JLabel Lans;
    private static JLabel Lanalysis;
    private static JLabel LopA;
    private static JLabel LopB;
    private static JLabel LopC;
    private static JLabel LopD;
    private static JLabel Lmessage;
    private static JProgressBar progressBar1, progressBar2;
    static JLabel Lmessage1;
    private JLabel fontSizeAdd, fontSizeMinus;
    private static JTextField TuserAns;
    private JButton b_submit, b_last, b_change, b_output, b_zhan;
    private static int[] choose = new int[5];
    private int fontSize = 34;
    private static int lastID = -1;
    private static String GetUserAnswerStats;
    Container c;
    static String loginname;
    private static int correct, wrong;
    public MainFrame(String loginName) throws SQLException, ClassNotFoundException {
        super(loginName + " 欢迎使用刷题系统!");
        this.loginname = loginName;
        GetUserAnswerStats = "SELECT q.*,  " +
                "       COUNT(CASE WHEN ua.yn = 1 THEN 1 END) AS correct_count,  " +
                "       COUNT(CASE WHEN ua.yn = 0 THEN 1 END) AS wrong_count " +
                "FROM question q " +
                "LEFT JOIN user_ans ua ON q.id = ua.qid AND ua.uname = '" + loginname + "' " +
                "GROUP BY q.id";
        //布局方式NULL，便于设置界面
        c = this.getContentPane();
        c.setLayout(null);
        //设置每个块的位置属性
        {
            Lidtext = new JLabel("题目ID:", JLabel.LEFT);
            Lidtext.setBounds(50, 10, 150, 55);
            Lidtext.setFont(new Font("黑体", Font.PLAIN, 34));
            Lidtext.setOpaque(true);
            Lidtext.setBackground(Color.gray);
            c.add(Lidtext);

            Lid = new JLabel("-1", JLabel.LEFT);
            Lid.setBounds(200, 10, 200, 55);
            Lid.setFont(new Font("黑体", Font.PLAIN, 34));
            Lid.setOpaque(true);
            Lid.setBackground(Color.gray);
            c.add(Lid);

            Ldic = new JLabel("题目章节号", JLabel.LEFT);
            Ldic.setBounds(500, 10, 400, 55);
            Ldic.setFont(new Font("黑体", Font.PLAIN, 34));
            Ldic.setOpaque(true);
            Ldic.setBackground(Color.gray);
            c.add(Ldic);

            Ltype = new JLabel("题目类型", JLabel.LEFT);
            Ltype.setBounds(950, 10, 400, 55);
            Ltype.setFont(new Font("黑体", Font.PLAIN, 34));
            Ltype.setOpaque(true);
            Ltype.setBackground(Color.gray);
            c.add(Ltype);

            Lques = new JLabel("<html>" + "正在连接数据库..." + "</html>", JLabel.LEFT);
            Lques.setBounds(50, 100, 1300, 300);
            Lques.setFont(new Font("黑体", Font.PLAIN, 34));
            Lques.setOpaque(true);
            Lques.setBackground(Color.gray);
            c.add(Lques);

            LopA = new JLabel("选项A。选项A。选项A。选项A。选项A。选项A。选项A。选项A。选项A。", JLabel.LEFT);
            LopA.setBounds(50, 420, 1300, 60);
            LopA.setFont(new Font("黑体", Font.PLAIN, 34));
            LopA.setOpaque(true);
            LopA.setBackground(Color.gray);
            c.add(LopA);

            LopB = new JLabel("选项B。选项B。选项B。选项B。选项B。选项B。选项B。选项B。选项B。", JLabel.LEFT);
            LopB.setBounds(50, 490, 1300, 60);
            LopB.setFont(new Font("黑体", Font.PLAIN, 34));
            LopB.setOpaque(true);
            LopB.setBackground(Color.gray);
            c.add(LopB);

            LopC = new JLabel("选项C。选项C。选项C。选项C。选项C。选项C。选项C。选项C。选项C。", JLabel.LEFT);
            LopC.setBounds(50, 560, 1300, 60);
            LopC.setFont(new Font("黑体", Font.PLAIN, 34));
            LopC.setOpaque(true);
            LopC.setBackground(Color.gray);
            c.add(LopC);

            LopD = new JLabel("选项D。选项D。选项D。选项D。选项D。选项D。选项D。选项D。选项D。", JLabel.LEFT);
            LopD.setBounds(50, 630, 1300, 60);
            LopD.setFont(new Font("黑体", Font.PLAIN, 34));
            LopD.setOpaque(true);
            LopD.setBackground(Color.gray);
            c.add(LopD);

            Lans = new JLabel("正确答案", JLabel.CENTER);
            Lans.setBounds(50, 720, 160, 55);
            Lans.setFont(new Font("黑体", Font.PLAIN, 34));
            Lans.setOpaque(true);
            Lans.setBackground(Color.DARK_GRAY);
            Lans.setForeground(Color.DARK_GRAY);
            c.add(Lans);

            Lanalysis = new JLabel("解析：1111", JLabel.LEFT);
            Lanalysis.setBounds(250, 720, 240, 55);
            Lanalysis.setFont(new Font("黑体", Font.PLAIN, 34));
            Lanalysis.setOpaque(true);
            Lanalysis.setBackground(Color.LIGHT_GRAY);
            c.add(Lanalysis);

            TuserAns = new JTextField(36);
            TuserAns.setBounds(530, 720, 100, 55);
            TuserAns.setFont(new Font("思源黑体", Font.BOLD, 34));
            c.add(TuserAns);

            TuserAns.addKeyListener(new KeyAdapter(){
                public void keyPressed(KeyEvent e)
                {
                    if(e.getKeyChar()== KeyEvent.VK_ENTER )   //按回车键执行相应操作;
                    {
                        try {
                            judge();
                        } catch (SQLException | ClassNotFoundException | InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            });

            b_submit = new JButton("提交答案");
            b_submit.setBounds(660, 720, 200, 55);
            b_submit.setFont(new Font("黑体", Font.BOLD, 30));
            b_submit.setBorder(BorderFactory.createRaisedBevelBorder());
            b_submit.setContentAreaFilled(false);
            c.add(b_submit);

            b_zhan = new JButton("斩题");
            b_zhan.setBounds(900, 720, 100, 55);
            b_zhan.setFont(new Font("黑体", Font.BOLD, 30));
            b_zhan.setBorder(BorderFactory.createRaisedBevelBorder());
            b_zhan.setContentAreaFilled(false);
            c.add(b_zhan);

            b_last = new JButton("上一题");
            b_last.setBounds(1020, 720, 160, 55);
            b_last.setFont(new Font("黑体", Font.BOLD, 30));
            b_last.setBorder(BorderFactory.createRaisedBevelBorder());
            b_last.setContentAreaFilled(false);
            c.add(b_last);

            b_change = new JButton("换一题");
            b_change.setBounds(1190, 720, 160, 55);
            b_change.setFont(new Font("黑体", Font.BOLD, 30));
            b_change.setBorder(BorderFactory.createRaisedBevelBorder());
            b_change.setContentAreaFilled(false);
            c.add(b_change);

            fontSizeAdd = new JLabel("A+", JLabel.CENTER);
            fontSizeAdd.setBounds(1280, 820, 30, 30);
            fontSizeAdd.setFont(new Font("黑体", Font.PLAIN, 20));
            fontSizeAdd.setOpaque(true);
            fontSizeAdd.setBackground(Color.WHITE);
            c.add(fontSizeAdd);

            fontSizeMinus = new JLabel("A-", JLabel.CENTER);
            fontSizeMinus.setBounds(1320, 820, 30, 30);
            fontSizeMinus.setFont(new Font("黑体", Font.PLAIN, 20));
            fontSizeMinus.setOpaque(true);
            fontSizeMinus.setBackground(Color.WHITE);
            c.add(fontSizeMinus);

            Lmessage = new JLabel("题目作答记录 题目作答记录", JLabel.RIGHT);
            Lmessage.setBounds(760, 820, 500, 30);
            Lmessage.setFont(new Font("黑体", Font.PLAIN, 20));
            c.add(Lmessage);

            Lmessage1 = new JLabel("题目作答记录 题目作答记录 题目作答记录 题目作答记录 题目作答记录 题目作答记录", JLabel.LEFT);
            Lmessage1.setBounds(50, 820, 1000, 30);
            Lmessage1.setFont(new Font("黑体", Font.PLAIN, 20));
            c.add(Lmessage1);

            b_output = new JButton("导出作答记录");
            b_output.setBounds(900, 820, 160, 30);
            b_output.setFont(new Font("黑体", Font.BOLD, 20));
            b_output.setBorder(BorderFactory.createRaisedBevelBorder());
            b_output.setContentAreaFilled(false);
            c.add(b_output);

            progressBar1 = new JProgressBar();
            progressBar1.setBounds(50, 786, 1300, 25);
            progressBar1.setMaximum(1006);
            progressBar1.setMinimum(0);
            progressBar1.setValue(0);
            progressBar1.setStringPainted(true);
            progressBar1.setForeground(Color.GREEN);
            progressBar1.setBackground(Color.WHITE);
            c.add(progressBar1);

        }
        LopA.addMouseListener(new MouseListener() {
                                  @Override
                                  public void mouseClicked(MouseEvent e) {
                                      if(choose[1] == 0) {
                                          choose[1] = 1;
                                          LopA.setBackground(Color.gray);
                                      }
                                      else {
                                          choose[1] = 0;
                                          LopA.setBackground(Color.LIGHT_GRAY);
                                      }
                                      if(Ltype.getText().equals(" 题目类型：单选题") || Ltype.getText().equals(" 题目类型：判断题") || b_submit.getText().equals("继续答题")) {
                                          try {
                                              judge();
                                          } catch (SQLException | ClassNotFoundException | InterruptedException ex) {
                                              throw new RuntimeException(ex);
                                          }
                                      }
                                  }
                                  @Override
                                  public void mousePressed(MouseEvent e) {}
                                  @Override
                                  public void mouseReleased(MouseEvent e) {}
                                  @Override
                                  public void mouseEntered(MouseEvent e) {}
                                  @Override
                                  public void mouseExited(MouseEvent e) {}
                              });
        LopB.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(choose[2] == 0) {
                    choose[2] = 1;
                    LopB.setBackground(Color.gray);
                }
                else {
                    choose[2] = 0;
                    LopB.setBackground(Color.LIGHT_GRAY);
                }
                if(Ltype.getText().equals(" 题目类型：单选题") || Ltype.getText().equals(" 题目类型：判断题") || b_submit.getText().equals("继续答题")) {
                    try {
                        judge();
                    } catch (SQLException | ClassNotFoundException | InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        LopC.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(choose[3] == 0) {
                    choose[3] = 1;
                    LopC.setBackground(Color.gray);
                }
                else {
                    choose[3] = 0;
                    LopC.setBackground(Color.LIGHT_GRAY);
                }
                if(Ltype.getText().equals(" 题目类型：单选题") || Ltype.getText().equals(" 题目类型：判断题") || b_submit.getText().equals("继续答题")) {
                    try {
                        judge();
                    } catch (SQLException | ClassNotFoundException | InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        LopD.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(choose[4] == 0) {
                    choose[4] = 1;
                    LopD.setBackground(Color.gray);
                }
                else {
                    choose[4] = 0;
                    LopD.setBackground(Color.LIGHT_GRAY);
                }
                if(Ltype.getText().equals(" 题目类型：单选题") || Ltype.getText().equals(" 题目类型：判断题") || b_submit.getText().equals("继续答题")) {
                    try {
                        judge();
                    } catch (SQLException | ClassNotFoundException | InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        Lans.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(Lans.getBackground() == Color.DARK_GRAY){
                    Lans.setBackground(Color.gray);
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        fontSizeAdd.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fontSize++;
                Lques.setFont(new Font("黑体", Font.PLAIN,fontSize));
                LopA.setFont(new Font("黑体", Font.PLAIN,fontSize));
                LopB.setFont(new Font("黑体", Font.PLAIN,fontSize));
                LopC.setFont(new Font("黑体", Font.PLAIN,fontSize));
                LopD.setFont(new Font("黑体", Font.PLAIN,fontSize));
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        fontSizeMinus.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fontSize--;
                Lques.setFont(new Font("黑体", Font.PLAIN,fontSize));
                LopA.setFont(new Font("黑体", Font.PLAIN,fontSize));
                LopB.setFont(new Font("黑体", Font.PLAIN,fontSize));
                LopC.setFont(new Font("黑体", Font.PLAIN,fontSize));
                LopD.setFont(new Font("黑体", Font.PLAIN,fontSize));
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        b_submit.addActionListener(this);
        b_last.addActionListener(this);
        b_change.addActionListener(this);
        b_output.addActionListener(this);
        b_zhan.addActionListener(this);

        this.setResizable(true);
        this.setSize(1420,900);
        Dimension screen = this.getToolkit().getScreenSize();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation((screen.width-this.getSize().width)/2,(screen.height-this.getSize().height)/2);
        this.setVisible(true);
    }

    public static void showQuestion(int id) throws SQLException, ClassNotFoundException {
        lastID = Integer.parseInt(Lid.getText());
        String sql = "select * from ("+GetUserAnswerStats+") question where id = " + id;
        Object[][] res = sqlQuery(sql);
        Lid.setText(""+(String) res[0][0]);
        Ldic.setText(" 题目章节号："+(String) res[0][1]);
        Ltype.setText(" 题目类型："+(String) res[0][2]);
        Lques.setText("<html>"+(String) res[0][3]+ "</html>");
        LopA.setText(" 选项A1： "+(String) res[0][6]);
        LopB.setText(" 选项B2： "+(String) res[0][7]);
        LopC.setText(" 选项C3： "+(String) res[0][8]);
        LopD.setText(" 选项D4： "+(String) res[0][9]);
        Lans.setText((String) res[0][4]);
        Lanalysis.setText("解析："+(String) res[0][5]);
        Lmessage.setText("正确" + res[0][10] + "次 | 错误"+ res[0][11] + "次");
        correct = Integer.parseInt((String) res[0][10]);
        wrong = Integer.parseInt((String) res[0][11]);

        if(((String)res[0][2]).equals("判断题")){
            LopC.setVisible(false);
            LopD.setVisible(false);
        }
        else{
            LopC.setVisible(true);
            LopD.setVisible(true);
        }

        sql = "SELECT " +
                "    SUM(CASE WHEN yn = 1 THEN 1 ELSE 0 END) AS correct_count, " +
                "    SUM(CASE WHEN yn = 0 THEN 1 ELSE 0 END) AS wrong_count " +
                "FROM " +
                "    user_ans " +
                "WHERE " +
                "    uname = '" + loginname + "' " +
                "    AND DATE(time) = CURDATE()";
        Connection connection = Conn.connectMySQL();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        String todaycorrect = resultSet.getString("correct_count");
        String todaywrong = resultSet.getString("wrong_count");

        sql = "SELECT " +
                "    SUM(CASE WHEN yn = 1 THEN 1 ELSE 0 END) AS correct_count, " +
                "    SUM(CASE WHEN yn = 0 THEN 1 ELSE 0 END) AS wrong_count " +
                "FROM " +
                "    user_ans " +
                "WHERE " +
                "    uname = '" + loginname + "' ";
        resultSet = statement.executeQuery(sql);
        resultSet.next();
        String totalcorrect = resultSet.getString("correct_count");
        String totalwrong = resultSet.getString("wrong_count");

        Lmessage1.setText("总答题数 正确"+totalcorrect+"次 错误"+totalwrong+"次 正确率"+calculateAccuracy(totalcorrect,totalwrong)+" | 今日答题 正确"+todaycorrect+"次 错误"+todaywrong+"次 正确率"+calculateAccuracy(todaycorrect,todaywrong));
        //初始化
        for (int i = 0; i < 5; i++) {
            choose[i] = 0;
        }
        Lques.setBackground(Color.LIGHT_GRAY);
        LopA.setBackground(Color.LIGHT_GRAY);
        LopB.setBackground(Color.LIGHT_GRAY);
        LopC.setBackground(Color.LIGHT_GRAY);
        LopD.setBackground(Color.LIGHT_GRAY);
        Lans.setBackground(Color.DARK_GRAY);
        TuserAns.setText("");
    }

    public static String calculateAccuracy(String correctCount, String wrongCount) {
        if(correctCount == null || wrongCount == null){
            return "0.00%";
        }
        int correct = Integer.parseInt(correctCount);
        int wrong = Integer.parseInt(wrongCount);
        if(wrong + correct == 0)
            return "0.00%";
        double accuracy = (double) correct / (correct + wrong) * 100;
        String formattedAccuracy = String.format("%.2f", accuracy) + "%";
        return formattedAccuracy;
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        new MainFrame("wjc");
        showQuestion(getQuestionID());
    }

    public static int getQuestionID() throws SQLException {
        System.out.println("----------------------------------------");
        String sql = "select * from (select id, correct_count, wrong_count, correct_count+wrong_count count from (" + GetUserAnswerStats + ") question) cnt where count = 0 || wrong_count*2>correct_count";
        Object[][] row;
//        连接MySQL
//        首先查询有几行，并根据行数设置数组和表格行数
        ResultSet resultSet = Conn.queryMySQL("select count(*) from (" + sql + ") question;");
        resultSet.next();
        int cntRow = Integer.parseInt(resultSet.getString("count(*)"));
        if(cntRow == 0){
            JFrame jFrame = new JFrame();
            JOptionPane.showMessageDialog(jFrame, "当前条件下已经没有题目\n程序即将退出");
            exit(0);
        }
        Random rand = new Random();
        int randRow = rand.nextInt(cntRow);
        row = new Object[cntRow][1];

//        5.执行SQL并返回数据集
        resultSet = Conn.queryMySQL(sql + "");
        int cnt = 0;
        while (resultSet.next()) {
            row[cnt][0] = resultSet.getString("id");
            cnt++;
        }
        resultSet.close();
        System.out.println("剩余还有" + cntRow + "条数据 | 抽取到第"+randRow+"行 | id = "+row[randRow][0]);
        progressBar1.setValue(1006-Integer.parseInt(String.valueOf(cntRow)));
        return Integer.parseInt((String) row[randRow][0]);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object temp=e.getSource();
        if(temp==b_submit){
            try {
                judge();
            } catch (SQLException | ClassNotFoundException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } else if (temp == b_last){
            if(lastID != -1){
                try {
                    showQuestion(lastID);
                } catch (SQLException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if (temp == b_change){
            try {
                showQuestion(getQuestionID());
            } catch (SQLException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }else if (temp == b_output){
            try {
                new outPutTable(loginname);
            } catch (SQLException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }else if (temp == b_zhan){
            int n = JOptionPane.showConfirmDialog(null, "确认斩题吗？\n斩题后该题将永远不会出现", "斩题",JOptionPane.YES_NO_OPTION);
//            System.out.println(n);
            if(n == 0){
                //确认
                String sql = "INSERT INTO user_ans (uname, qid, yn, time) VALUES ('" + loginname + "', " + Lid.getText() + ", " + 1 + ", '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "')";
//            Lmessage1.setText("正在请求数据库...");
                try {
                    for (int i = 0; i < wrong * 2 - correct; i++) {
                        updateMySQL(sql);
                    }
                    System.out.println("斩题成功");
                    showQuestion(getQuestionID());
                } catch (SQLException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private void judge() throws SQLException, ClassNotFoundException, InterruptedException {
        if(b_submit.getText().equals("提交答案")) {
            int[] trueAns = new int[5];

            StringToArray(choose, TuserAns.getText());
            StringToArray(trueAns, Lans.getText());
            int flag = 1;
            for (int i = 1; i <= 4; i++) {
                if (choose[i] != trueAns[i]) {
                    flag = 0;
                    break;
                }
            }
            System.out.println("choose " + Arrays.toString(choose));
            System.out.println("trueAns " + Arrays.toString(trueAns));
            System.out.println("judgeAns " + (flag == 1 ? "正确" : "错误"));
            String sql = "INSERT INTO user_ans (uname, qid, yn, time) VALUES ('" + loginname + "', " + Lid.getText()
                    + ", " + flag + ", '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "')";
//            Lmessage1.setText("正在请求数据库...");
            int affectRow = updateMySQL(sql);
            System.out.println("添加" + affectRow + "条数据");
            if (flag == 1) {
                Timer timer = new Timer(100, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 定时器触发的操作
                        Lques.setBackground(Color.LIGHT_GRAY);
                        try {
                            showQuestion(getQuestionID());
                        } catch (SQLException | ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        // 停止计时器
                        ((Timer) e.getSource()).stop();
                    }
                });

                Lques.setBackground(Color.GREEN);
                // 启动计时器
                timer.start();
            } else {
                Timer timer = new Timer(200, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 定时器触发的操作
                        Lques.setBackground(Color.LIGHT_GRAY);
                        b_submit.setText("继续答题");
                        // 停止计时器
                        ((Timer) e.getSource()).stop();
                    }
                });
                LopA.setBackground(Color.LIGHT_GRAY);
                LopB.setBackground(Color.LIGHT_GRAY);
                LopC.setBackground(Color.LIGHT_GRAY);
                LopD.setBackground(Color.LIGHT_GRAY);
                if(trueAns[1] == 1)
                    LopA.setBackground(Color.GRAY);
                if(trueAns[2] == 1)
                    LopB.setBackground(Color.GRAY);
                if(trueAns[3] == 1)
                    LopC.setBackground(Color.GRAY);
                if(trueAns[4] == 1)
                    LopD.setBackground(Color.GRAY);

                Lques.setBackground(Color.RED);
                Lans.setBackground(Color.lightGray);

                // 启动计时器
                timer.start();
            }
        }
        else {
            b_submit.setText("提交答案");
            try {
                showQuestion(getQuestionID());
            } catch (SQLException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    private void StringToArray(int[] trueAns, String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
//                case 'A', 'a', '1' -> trueAns[1] = 1;
//                case 'B', 'b', '2' -> trueAns[2] = 1;
//                case 'C', 'c', '3' -> trueAns[3] = 1;
//                case 'D', 'd', '4' -> trueAns[4] = 1;
                case 'A':
                case 'a':
                case '1':
                    trueAns[1] = 1;
                    break;
                case 'B':
                case 'b':
                case '2':
                    trueAns[2] = 1;
                    break;
                case 'C':
                case 'c':
                case '3':
                    trueAns[3] = 1;
                    break;
                case 'D':
                case 'd':
                case '4':
                    trueAns[4] = 1;
                    break;

            }
        }
    }
}


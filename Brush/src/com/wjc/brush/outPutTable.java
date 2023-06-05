package com.wjc.brush;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class outPutTable  extends JFrame implements ActionListener {
    private JTable table;
    private JLabel l_message;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        new outPutTable("wjc");
    }
    public outPutTable(String username) throws SQLException, ClassNotFoundException{
        super(username+" 答题数据导出");
        Container c=this.getContentPane();
        c.setLayout(new BorderLayout());
        String[] cloum = new String[]{"题目ID", "目录", "题型", "题干", "正确答案", "答案解析", "A", "B", "C", "D", "正确计数", "错误计数", "正确率"};

        String sql = "SELECT q.*,  " +
                "       COUNT(CASE WHEN ua.yn = 1 THEN 1 END) AS correct_count,  " +
                "       COUNT(CASE WHEN ua.yn = 0 THEN 1 END) AS wrong_count " +
                "FROM question q " +
                "LEFT JOIN user_ans ua ON q.id = ua.qid AND ua.uname = '" + username + "' " +
                "GROUP BY q.id";
        Object[][] row;
//        连接MySQL
//        首先查询有几行，并根据行数设置数组和表格行数
        ResultSet resultSet = Conn.queryMySQL("select count(*) from (" + sql + ") question;");
        resultSet.next();
        int cntRow = Integer.parseInt(resultSet.getString("count(*)"));
        System.out.println("查询到" + cntRow + "条数据");
        row = new Object[cntRow + 1][13];
        row[0] = new String[]{"题目ID", "目录", "题型", "题干", "正确答案", "答案解析", "A", "B", "C", "D", "正确计数", "错误计数", "正确率"};
//        5.执行SQL并返回数据集
        resultSet = Conn.queryMySQL(sql + "");
        int cnt = 1;
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
            row[cnt][12] = calculateAccuracy((String) row[cnt][10], (String) row[cnt][11]);
            cnt++;
        }
        resultSet.close();

        table = new JTable(row, cloum);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(580,750));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(table);
        c.add(scrollPane,BorderLayout.NORTH);

        l_message = new JLabel("          使用Ctrl+A全选，使用Ctrl+C复制，即可在Excel中粘贴");
        l_message.setFont(new Font("黑体", Font.PLAIN, 34));
        c.add(l_message,BorderLayout.SOUTH);
        this.setResizable(true);
        this.setSize(1200,920);
        Dimension screen = this.getToolkit().getScreenSize();
        this.setLocation((screen.width-this.getSize().width)/2,(screen.height-this.getSize().height)/2);
        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

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

}

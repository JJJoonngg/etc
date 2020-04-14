//작성자 : 김종신
package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class read_id extends JFrame implements ActionListener {
	private Border border;// 경계선 표시를 위해 인터넷 참조
	Vector<String> boardColumn = new Vector<String>();
	private DefaultTableModel model;
	private JTable tb = new JTable();
	private JScrollPane scroll;
	JPanel main, content, top, bottom;
	JTextField title, writer;
	JTextArea contents;
	JButton gomain, t, n, prev, next;
	Connection con = makeConnection();
	String sql;
	ResultSet rs;
	int a = 0;
	int b = 0;
	int c = 0;
	private Statement stmt = con.createStatement();

	public read_id(String id) throws SQLException {

		sql = "select * from board where bid = '" + id + "';";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		top = new JPanel();
		t = new JButton("제목 : ");
		t.setBackground(Color.BLACK);
		t.setOpaque(false);
		n = new JButton("작성자 : ");
		n.setBackground(Color.BLACK);
		n.setOpaque(false);
		title = new JTextField(30);
		writer = new JTextField(10);
		writer.setText("" + id);
		top.add(t);
		top.add(title);
		top.add(n);
		top.add(writer);

		bottom = new JPanel();
		gomain = new JButton("게시판으로");
		gomain.setBackground(Color.BLACK);
		gomain.setOpaque(false);
		gomain.addActionListener(this);
		next = new JButton("다음 게시물");
		next.setBackground(Color.BLACK);
		next.setOpaque(false);
		next.addActionListener(this);
		prev = new JButton("이전 게시물");
		prev.setBackground(Color.BLACK);
		prev.setOpaque(false);
		prev.addActionListener(this);
		prev.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bottom.add(gomain);
		bottom.add(prev);
		bottom.add(next);

		content = new JPanel();
		contents = new JTextArea(20, 60);
		content.add(contents);
		border = BorderFactory.createLineBorder(Color.BLACK, 1);
		content.setBorder(border);

		try {
			sql = "select * from board where bid = '" + id + "';";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				a++;
				title.setText("" + rs.getString("title"));
				contents.setText("" + rs.getString("content"));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		b = a;
		c = b - 1;
		main = new JPanel();
		main.add(top, BorderLayout.NORTH);
		main.add(content, BorderLayout.CENTER);
		main.add(bottom, BorderLayout.SOUTH);
		add(main);
		setTitle("내용");
		setSize(700, 500);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == next) {
			if ((b <= a) || (a == c)) {
				JOptionPane.showMessageDialog(null, "마지막 게시물 입니다.");
			} else if (b > a) {
				try {
					rs.next();
					title.setText("" + rs.getString("title"));
					contents.setText("" + rs.getString("content"));
					a++;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource() == prev) {
			if (a == 0) {
				JOptionPane.showMessageDialog(null, "첫번째 게시물 입니다.");
			} else if (a > 0) {
				try {
					rs.previous();
					title.setText("" + rs.getString("title"));
					contents.setText("" + rs.getString("content"));
					a--;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource() == gomain) {
			JOptionPane.showMessageDialog(null, "게시판으로 돌아갑니다.");
			setVisible(false);
			try {
				new notice();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static Connection makeConnection() {

		String url = "jdbc:mysql://localhost/soccer";
		String id = "root";
		String password = "onlyroot";
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, id, password);
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {
		}
		return con;

	}
}
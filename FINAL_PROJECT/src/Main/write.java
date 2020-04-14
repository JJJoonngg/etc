//작성자 : 김종신
package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import Login.LoginFrame;

public class write extends JFrame implements ActionListener {
	Connection con = makeConnection();
	private Border border;
	JPanel main, content, top, bottom;
	JButton save, cancel, t, n;
	JTextField title, writer;
	JTextArea contents;
	String id;

	public write() throws SQLException {
		id = LoginFrame.id;
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
		save = new JButton("저장");
		save.addActionListener(this);
		cancel = new JButton("취소");
		cancel.addActionListener(this);
		save.setBackground(Color.BLACK);
		save.setOpaque(false);
		cancel.setBackground(Color.BLACK);
		cancel.setOpaque(false);
		bottom.add(save);
		bottom.add(cancel);

		content = new JPanel();
		contents = new JTextArea(20, 60);
		content.add(contents);
		border = BorderFactory.createLineBorder(Color.BLACK, 1);
		content.setBorder(border);
		main = new JPanel();
		main.add(top, BorderLayout.NORTH);
		main.add(content, BorderLayout.CENTER);
		main.add(bottom, BorderLayout.SOUTH);

		add(main);
		setTitle("내용작성");
		setSize(700, 500);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String t = title.getText().toString();
		String c = contents.getText().toString();
		if (e.getSource() == cancel) {
			JOptionPane.showMessageDialog(null, "게시판으로 돌아갑니다.");
			setVisible(false);
			try {
				new notice();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == save) {
			try {
				Statement stmt = con.createStatement();
				String searchQry = "select title from board;";
				String insertQry = "insert into board(bid,title,content) values";
				insertQry += "('" + id + "','" + t + "','" + c + "')";
				ResultSet rs1 = stmt.executeQuery(searchQry);
				int a = 0;
				while (rs1.next()) {
					if (t.equals(rs1.getString("title").toString())) {
						a++;
					}
				}
				if (a == 0) {
					int rs = stmt.executeUpdate(insertQry);
					setVisible(false);
					JOptionPane.showMessageDialog(null, "입력 완료 게시판으로 돌아갑니다.");
					try {
						new notice();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else if (a > 0) {
					JOptionPane.showMessageDialog(null, "동일한 제목을 가진 게시물이 존재합니다.");
				}

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	public static Connection makeConnection() {
		// TODO Auto-generated method stub
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
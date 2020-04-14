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

public class read_title extends JFrame implements ActionListener {
	private Border border;// 경계선 표시를 위해 인터넷 참조
	Vector<String> boardColumn = new Vector<String>();
	private DefaultTableModel model;
	private JTable tb = new JTable();
	private JScrollPane scroll;
	JPanel main, content, top, bottom;
	JTextField titl, writer;
	JTextArea contents;
	JButton gomain, t, n;
	String id;
	Connection con = makeConnection();
	private Statement stmt;

	public read_title(String title) throws SQLException {

		top = new JPanel();

		t = new JButton("제목 : ");
		t.setBackground(Color.BLACK);
		t.setOpaque(false);
		n = new JButton("작성자 : ");
		n.setBackground(Color.BLACK);
		n.setOpaque(false);
		titl = new JTextField(30);
		titl.setText("" + title);
		writer = new JTextField(10);

		top.add(t);
		top.add(titl);
		top.add(n);
		top.add(writer);

		bottom = new JPanel();
		gomain = new JButton("게시판으로");
		gomain.setBackground(Color.BLACK);
		gomain.setOpaque(false);
		gomain.addActionListener(this);
		bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bottom.add(gomain);

		content = new JPanel();
		contents = new JTextArea(20, 60);
		content.add(contents);
		border = BorderFactory.createLineBorder(Color.BLACK, 1);
		content.setBorder(border);

		try {
			String sql = "select * from board where title = '" + title + "';";
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				writer.setText("" + rs.getString("bid"));
				contents.setText("" + rs.getString("content"));
			}
		} catch (SQLException e1) {

			e1.printStackTrace();
		}

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
		if (e.getSource() == gomain) {
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

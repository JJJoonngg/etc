//작성자 : 김종신
package Main;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class notice extends JFrame implements ActionListener, ItemListener {
	JPanel main, board, search, top;
	Vector<String> boardColumn = new Vector<String>();
	private DefaultTableModel model;
	private JTable tb = new JTable();
	private JScrollPane scroll;
	private Border border;// 경계선 표시를 위해 인터넷 참조
	Choice list;
	JTextField text;
	JButton create, gomain, btn;
	Connection con = makeConnection();
	private Statement stmt = con.createStatement();

	public notice() throws SQLException {

		top = new JPanel();
		create = new JButton("작성하기");
		gomain = new JButton("Main으로");
		create.setBackground(Color.BLACK);
		gomain.setBackground(Color.BLACK);
		create.setOpaque(false);
		gomain.setOpaque(false);
		create.addActionListener(this);
		gomain.addActionListener(this);
		top.setLayout(new FlowLayout(FlowLayout.RIGHT));
		top.add(create);
		top.add(gomain);

		board = new JPanel();
		String[] header = { "제          목", "아이디" };
		for (int i = 0; i < header.length; i++) {
			boardColumn.addElement(header[i]);
		}
		model = new DefaultTableModel(boardColumn, 0);
		tb = new JTable(model);
		scroll = new JScrollPane(tb);
		board.add(scroll);

		String sql = "SELECT title,bid FROM board";
		while (model.getRowCount() > 0)
			model.removeRow(0);
		try {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				model.addRow(new String[] { rs.getString("title"), rs.getString("bid") });
			}
		} catch (SQLException e1) {
// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		board.setLayout(new BoxLayout(board, BoxLayout.Y_AXIS));
		search = new JPanel();

		list = new Choice();
		list.add("제목");
		list.add("아이디");
		text = new JTextField(20);
		btn = new JButton("Search");
		btn.addActionListener(this);
		btn.setBackground(Color.BLACK);
		btn.setOpaque(false);
		search.add(list);
		search.add(text);
		search.add(btn);
		main = new JPanel();
		main.setLayout(new BorderLayout());
		border = BorderFactory.createLineBorder(Color.BLACK, 1);
		main.setBorder(border);
		main.add(top, BorderLayout.NORTH);
		main.add(board, BorderLayout.CENTER);
		main.add(search, BorderLayout.SOUTH);

		add(main);
		setTitle("Notice Board");
		setSize(500, 500);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
// TODO Auto-generated method stub
		int a = 0;
		int b = 0;
		String s = text.getText().toString();
		if (e.getSource() == gomain) {
			try {
				new maintool();
			} catch (SQLException e1) {
// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			setVisible(false);
		} else if (e.getSource() == create) {
			try {
				new write();
			} catch (SQLException e1) {
// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			setVisible(false);
		} else if (e.getSource() == btn) {
			switch (list.getSelectedItem()) {
			case "제목":
				String search_title = "select title from board";
				try {
					ResultSet rs = stmt.executeQuery(search_title);
					while (rs.next()) {
						if (s.equals(rs.getString("title").toString())) {
							a++;
						}
					}
					if (a > 0) {
						new read_title(s);
						setVisible(false);
					} else if (a == 0) {
						JOptionPane.showMessageDialog(null, "찾고자하는 제목을 가진 게시물이 없습니다.");
					}

				} catch (SQLException e1) {
// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case "아이디":
				String search_id = "select bid from board";
				try {
					ResultSet rs1 = stmt.executeQuery(search_id);
					while (rs1.next()) {
						if (s.equals(rs1.getString("bid").toString())) {
							b++;
						}
					}
					if (b > 0) {
						new read_id(s);
						setVisible(false);
					} else if (b == 0) {
						JOptionPane.showMessageDialog(null, "찾고자하는 작성자를 가진 게시물이 없습니다.");
					}
				} catch (SQLException e1) {
// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
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

	@Override
	public void itemStateChanged(ItemEvent e) {
// TODO Auto-generated method stub

	}

}
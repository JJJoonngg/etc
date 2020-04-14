//�ۼ��� : ������
package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import Game.Penalty;
import Login.LoginFrame;
import Login.login;

public class maintool extends JFrame implements ActionListener {
	private JPanel backpanel, mainpanel, rankingpanel, sidepanel, side1, side2, bttn, left;
	private JButton[] btnz = new JButton[4];
	JScrollPane scrollPane;
	Vector<String> RankColumn = new Vector<String>();
	Vector<String> RankRow = new Vector<String>();
	Vector<String> resultColumn = new Vector<String>();
	Vector<String> qaColumn = new Vector<String>();
	String now;
	private DefaultTableModel model, model1, model2;
	private JTable tb = new JTable();
	private JTable tb1 = new JTable();
	private JTable tb2 = new JTable();
	private JScrollPane scroll, scroll1, scroll2;
	private Border border;
	private JTextField n;
	Connection con = makeConnection();
	private Statement stmt = con.createStatement();

	public maintool() throws SQLException {

		now = LoginFrame.id;

		bttn = new JPanel();
		String[] take = { "게시판", "승부차기 게임", "Logout", "ID : " };
		for (int i = 0; i < take.length; i++) {
			btnz[i] = new JButton(take[i]);
			btnz[i].addActionListener(this);
			btnz[i].setBackground(Color.BLACK);
			btnz[i].setOpaque(false);
			bttn.add(btnz[i]);
		}
		n = new JTextField(10);
		n.setText("" + now);
		bttn.add(n);

		side1 = new JPanel();
		String[] header1 = { "TITLE", "NICKNAME" };
		for (int i = 0; i < header1.length; i++) {
			qaColumn.addElement(header1[i]);
		}
		model1 = new DefaultTableModel(qaColumn, 0);
		tb1 = new JTable(model1);
		scroll1 = new JScrollPane(tb1);
		side1.add(scroll1);

		String sql1 = "SELECT title,bid FROM board";
		while (model1.getRowCount() > 0)
			model1.removeRow(0);
		try {
			ResultSet rs = stmt.executeQuery(sql1);
			while (rs.next()) {
				model1.addRow(new String[] { rs.getString("title"), rs.getString("bid") });
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		side2 = new JPanel();
		String[] header2 = { "승부차기 순위", "아이디", "Goal", "No Goal" };
		for (int i = 0; i < header2.length; i++) {
			resultColumn.addElement(header2[i]);
		}
		model2 = new DefaultTableModel(resultColumn, 0);
		tb2 = new JTable(model2);
		scroll2 = new JScrollPane(tb2);
		side2.add(scroll2);
		int j = 1;
		String sql2 = "SELECT * FROM score order by goal desc";
		while (model2.getRowCount() > 0)
			model2.removeRow(0);
		try {
			ResultSet rs = stmt.executeQuery(sql2);
			while (rs.next()) {
				model2.addRow(new String[] { Integer.toString(j), rs.getString("sid"),
						Integer.toString(rs.getInt("goal")), Integer.toString(rs.getInt("nogoal")) });
				j++;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		side1.setLayout(new BoxLayout(side1, BoxLayout.Y_AXIS));
		side2.setLayout(new BoxLayout(side2, BoxLayout.Y_AXIS));
		sidepanel = new JPanel();
		sidepanel.setLayout(new BoxLayout(sidepanel, BoxLayout.Y_AXIS));
		sidepanel.add(side1);
		sidepanel.add(side2);

		left = new JPanel();
		String[] header = { "순위", "팀 명", "MATCH수", "WIN", "DRAW", "LOSE", "승점" };
		for (int i = 0; i < header.length; i++) {
			RankColumn.addElement(header[i]);
		}
		model = new DefaultTableModel(RankColumn, 0);
		tb = new JTable(model);
		scroll = new JScrollPane(tb);
		int i = 1;
		left.add(scroll);
		String sql = "SELECT * FROM ranking order by point desc";
		while (model.getRowCount() > 0)
			model.removeRow(0);
		try {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				model.addRow(
						new String[] { Integer.toString(i), rs.getString("team"), Integer.toString(rs.getInt("mat")),
								Integer.toString(rs.getInt("win")), Integer.toString(rs.getInt("draw")),
								Integer.toString(rs.getInt("lose")), Integer.toString(rs.getInt("point")) });
				i++;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		rankingpanel = new JPanel();
		rankingpanel.add(left);

		border = BorderFactory.createLineBorder(Color.BLACK, 1);
		rankingpanel.setBorder(border);
		sidepanel.setBorder(border);

		mainpanel = new JPanel();
		mainpanel.setLayout(new GridLayout(0, 2, 2, 2));
		mainpanel.add(rankingpanel);
		mainpanel.add(sidepanel);

		backpanel = new JPanel();
		backpanel.setLayout(new BorderLayout());
		backpanel.add(bttn, BorderLayout.NORTH);
		backpanel.add(mainpanel, BorderLayout.CENTER);

		add(backpanel);
		setTitle("Football Comunity");
		setSize(950, 440);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnz[2]) {
			JOptionPane.showMessageDialog(null, "�α׾ƿ��մϴ�.");
			setVisible(false);
			try {
				new login();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == btnz[1]) {
			setVisible(false);
			try {
				new Penalty();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == btnz[0]) {
			setVisible(false);
			try {
				new notice();
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

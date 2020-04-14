//작성자 : 김종신 
package Game;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import Login.LoginFrame;
import Main.maintool;

public class Penalty extends JFrame implements ActionListener {
	private JPanel mainpanel, markpanel, sidepanel, bttn;
	private JButton[] choice = new JButton[4];
	private JButton reset = new JButton();
	private JButton result = new JButton();
	private JButton save = new JButton();
	private JButton comment = new JButton();
	Vector<String> resultColumn = new Vector<String>();
	private DefaultTableModel model;
	private JTable tb = new JTable();
	private JScrollPane scroll;
	private Border border;
	private JLabel[] label = new JLabel[6];
	private JLabel post = new JLabel();
	ImageIcon goalpost = new ImageIcon("image/main.jpg");
	ImageIcon success = new ImageIcon("image/success.jpg");
	ImageIcon fail = new ImageIcon("image/fail.jpg");
	ImageIcon ball = new ImageIcon("image/ball.jpg");
	ImageIcon goal = new ImageIcon("image/goal.jpg");
	ImageIcon nogoal = new ImageIcon("image/nogoal.jpg");
	private int computer, shot, count = 0, good = 0, bad = 0;
	Random random;
	String id;
	Connection con = makeConnection();
	private Statement stmt = con.createStatement();

	public Penalty() throws SQLException {

		id = LoginFrame.id;

		bttn = new JPanel();
		bttn.setLayout(new GridLayout(0, 2, 1, 1));
		String[] Shoot = { "Left UP", "Right UP", "Left Down", "Right Down" };
		for (int i = 0; i < Shoot.length; i++) {
			choice[i] = new JButton(Shoot[i]);
			choice[i].addActionListener(this);
			bttn.add(choice[i]);
		}
		reset = new JButton("Reset");
		reset.addActionListener(this);
		bttn.add(reset);

		result = new JButton("Goal : " + good + "  No Goal : " + bad);
		bttn.add(result);

		save = new JButton("결과저장버튼");
		save.addActionListener(this);
		bttn.add(save);

		comment = new JButton("게임종료버튼");
		comment.addActionListener(this);
		bttn.add(comment);

		random = new Random();
		computer = random.nextInt(4);
		shot = random.nextInt(4);

		markpanel = new JPanel();
		for (int i = 0; i < 5; i++) {
			label[i] = new JLabel();
			label[i].setIcon(ball);
			markpanel.add(label[i]);
		}

		post = new JLabel();
		post.setIcon(goalpost);
		markpanel.add(post);

		sidepanel = new JPanel();

		sidepanel.setLayout(new BoxLayout(sidepanel, BoxLayout.Y_AXIS));

		bttn.setBorder(border);
		sidepanel.add(bttn);
		sidepanel.add(markpanel);

		border = BorderFactory.createLineBorder(Color.BLACK, 1);

		sidepanel.setBorder(border);

		mainpanel = new JPanel();
		mainpanel.setLayout(new GridLayout(0, 1, 2, 0));
		mainpanel.add(sidepanel);

		add(mainpanel);
		setTitle("Penalty Game");
		setSize(400, 600);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == choice[0] || e.getSource() == choice[1] || e.getSource() == choice[2]
				|| e.getSource() == choice[3]) {
			if (shot == computer) {
				label[count].setIcon(nogoal);
				post.setIcon(fail);
				computer = random.nextInt(4);
				shot = random.nextInt(4);
				count++;
				bad++;
			} else {
				label[count].setIcon(goal);
				post.setIcon(success);
				computer = random.nextInt(4);
				shot = random.nextInt(4);
				count++;
				good++;
			}
			result.setText("Goal : " + good + "No Goal : " + bad);
		} else if (e.getSource() == reset) {
			for (int i = 0; i < 5; i++)
				label[i].setIcon(ball);
			post.setIcon(goalpost);
			computer = random.nextInt(4);
			shot = random.nextInt(4);
			count = 0;
			good = 0;
			bad = 0;
			result.setText("Goal : " + good + "No Goal : " + bad);
		} else if (e.getSource() == save) {
			try {
				int a = 0;
				Statement stmt = con.createStatement();
				String searchQry = "select sid from score";
				String insertQry = "insert into score(sid,goal,nogoal) values";
				insertQry += "('" + id + "','" + good + "','" + bad + "')";
				String updateQry = "update score set ";
				updateQry += "goal = " + good + ",nogoal =" + bad + " where sid = '" + id + "';";
				ResultSet rs = stmt.executeQuery(searchQry);
				while (rs.next()) {
					if (id.equals(rs.getString("sid").toString())) {
						a++;
					}
				}
				if (a > 0) {
					int rs2 = stmt.executeUpdate(updateQry);
					JOptionPane.showMessageDialog(null, "저장완료.");
				} else if (a == 0) {
					int rs1 = stmt.executeUpdate(insertQry);
				}

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == comment) {
			JOptionPane.showMessageDialog(null, "게임을 종료하고 처음으로 돌아갑니다.");
			setVisible(false);
			try {
				new maintool();
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

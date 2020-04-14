//작성자 : 안신애

package Login;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Main.maintool;

public class LoginFrame extends JFrame implements ActionListener {

	JPanel jp, jpl, jp1, jp2;
	JTextField jt1, jt2;
	JButton jb, jb2;
	JLabel jl0, jl1, jl2;
	String user_id;
	String user_pw;
	public static String id;
	Connection con = makeConnection();

	public LoginFrame() throws SQLException {

		setTitle("Login");

		jp = new JPanel();
		jpl = new JPanel();
		jp1 = new JPanel();
		jp2 = new JPanel();
		jl0 = new JLabel("로그인을 해주세요.");
		jt1 = new JTextField(10);
		jt2 = new JPasswordField(10);
		jl1 = new JLabel("I D ");
		jl2 = new JLabel("PW");
		jb = new JButton("login");
		jb.addActionListener(this);
		jb2 = new JButton("회원가입");
		jb2.addActionListener(this);

		jpl.add(jl0);
		jp.add(jl1);
		jp.add(jt1);
		jp1.add(jl2);
		jp1.add(jt2);
		jp2.add(jb);
		jp2.add(jb2);

		setLayout(new GridLayout(4, 1));
		add(jpl);
		add(jp);
		add(jp1);
		add(jp2);

		setResizable(false);
		setEnabled(true);
		setBounds(500, 300, 300, 180);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jb) {
			id = jt1.getText().toString();// 전역변수로 사용을 위해 선언
			user_id = jt1.getText().toString();// Sql 탐색시 필요한 id
			user_pw = jt2.getText().toString(); // Sql 탐색시 필요한 paasword
			int a = 0;
			String sql1 = "select id,pass from member;";
			Statement stmt;
			try {
				stmt = con.createStatement();
				ResultSet rs1 = stmt.executeQuery(sql1);
				while (rs1.next()) {
					if (user_id.equals(rs1.getString("id").toString())
							&& user_pw.equals(rs1.getString("pass").toString())) {
						a++;
					}
				}
				if (a > 0) {
					JOptionPane.showMessageDialog(null, "Soccer Comunity에 오신걸 환영합니다.");
					new maintool();
					setVisible(false);
				} else if (a == 0) {
					JOptionPane.showMessageDialog(null, "아이디나 비밀번호가 틀렸습니다.");
				}
			} catch (SQLException e1) {
// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == jb2)
			new MemberJoin();
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

	public static void main(String[] args) throws SQLException {
		new LoginFrame();
	}
}
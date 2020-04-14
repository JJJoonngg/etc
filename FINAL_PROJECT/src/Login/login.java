//�ۼ��� : �Ƚž�

package Login;

import java.sql.SQLException;

import javax.swing.JFrame;

public class login extends JFrame {

	static LoginFrame lf = null;

	public login() throws SQLException {
		lf = new LoginFrame();
	}

	public static void main(String[] args) throws SQLException {
		new login();
	}
}

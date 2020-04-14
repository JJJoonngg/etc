//작성자 : 안신애

package Login;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

// JoinForm 클래스 start
public class MemberJoin extends JFrame implements ActionListener, ItemListener {

	// 클래스 내에서 사용될 멤버변수들을 선언
	// JFrame, Panel, Label, JTextField, Button, Choice, JRadioButton
	JFrame f;
	Panel p1, p2, p3, p3_1, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, pt;
	Label l1, l2, l3, l3_1, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13, l14, l15, l16, l17, l18, l19, l20, label2;
	JTextField name, id, pw, pw_check, tf4, tf5, tf6, tf7, phone, tf9, name0, name1;
	Button btn1, btn2, btn3;
	Choice list, list2, list3, list4, mail, list6;
	static Choice hp;
	JRadioButton cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8;
	Connection con = makeConnection();
	String phonenumber;

	// 생성자 정의 부분
	public MemberJoin() {

		// 회원가입 프레임 생성
		f = new JFrame("회 원 가 입");
		f.setSize(900, 800); // 프레임 창 크기
		f.setVisible(true);
		f.setBackground(Color.pink); // 프레임 배경색
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우 종료
		f.setLocation(400, 100); // 프레임 창 위치
		f.setVisible(true); // 디스플레이
		f.setResizable(false);

		// 화면배치를 시작하게 되는 부분
		// p1~ p13까지 객체생성을 하면서 FlowLayout을 사용해서 화면배치
		p1 = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		p2 = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		p3 = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		p3_1 = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		p4 = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		p5 = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		p6 = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		p7 = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		p8 = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		p9 = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		p10 = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		p11 = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		p12 = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		p13 = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 5)); // 중앙정렬과 세로간격 가로간격
		p14 = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		// 나머지는 객체 생성
		p15 = new Panel();
		p16 = new Panel();
		p17 = new Panel();
		p18 = new Panel();
		p19 = new Panel();
		pt = new Panel();

		// label들은 각각의 이름을 가진 Label객체들이 생성
		l1 = new Label("이      름");
		l2 = new Label("I    D");
		l3 = new Label("비밀번호");
		l3_1 = new Label("비밀번호 재입력");
		l4 = new Label("비밀번호 힌트");
		l5 = new Label("비밀번호 힌트 답");
		l6 = new Label("성별");
		l7 = new Label("생년월일");
		l8 = new Label("결혼여부");
		l9 = new Label("주       소");
		l10 = new Label("연락처(Tel)");
		l11 = new Label("연락처(H.P)");
		l12 = new Label("E-Mail");
		l13 = new Label("좋아하는 축구팀");
		l14 = new Label("직접입력");
		l15 = new Label("SMS수신여부");
		l16 = new Label("선택하세요.(없을시 선택 X)");
		l17 = new Label("SMS 수신을 선택하시면 다양한 서비스와 이벤트 등 유용한 정보를받아보실 수 있습니다.");
		l18 = new Label(" -");
		l19 = new Label("@");
		l20 = new Label(" -");
		label2 = new Label("회 원 가 입");

		// 각각 20이라는 길이를 가진채로 TextField 객체가 생성
		name = new JTextField(10);
		id = new JTextField(20);
		pw = new JPasswordField(20);
		pw_check = new JPasswordField(20);
		tf4 = new JTextField(25);
		tf5 = new JTextField(5);
		tf6 = new JTextField(40);
		tf7 = new JTextField(11);
		phone = new JTextField(11);
		tf9 = new JTextField(15);
		name0 = new JTextField(20);
		name1 = new JTextField(9);

		// 각각의 이름을 가진 버튼객체가 생성

		btn1 = new Button("아이디중복체크");
		btn1.addActionListener(this);
		btn2 = new Button("가입완료");
		btn2.addActionListener(this);
		btn3 = new Button("가입취소");
		btn3.addActionListener(this);

		// 아래 값들을 가진 Choice객체가 생성, 비밀번호 힌트
		list = new Choice();
		list.add("당신이 태어난 곳은?");
		list.add("가장 좋아하는 색은?");
		list.add("인생의 좌우명은?");
		list.add("가장 기억에 남는 장소는?");
		list.add("가장 소중한것 한가지는?");
		list.add("주민등록번호 맨 뒷자리는?");
		list.add("가장 감명깊게 읽은 책은?");
		list.add("존경하는 인물은?");
		list.add("핸드폰 1번에 저장된 사람은?");

		// 생년월일 (년)
		list2 = new Choice();
		for (int i = 2015; i > 1979; i--)
			list2.add("" + i);
		// 생년월일(월)
		list3 = new Choice();
		for (int i = 1; i < 13; i++)
			list3.add("" + i);

		// 생년월일(일)
		list4 = new Choice();
		for (int i = 1; i < 32; i++)
			list4.add("" + i);

		// 이메일 주소
		mail = new Choice();
		mail.add("직접입력");
		mail.add("hanmail.net");
		mail.add("nate.com");
		mail.add("empas.com");
		mail.add("lycos.com");
		mail.add("dreamwiz.com");
		mail.add("naver.com");
		mail.add("paran.com");
		mail.add("yahoo.com");
		mail.add("korea.com");
		mail.add("hotmail.com"); // 이 부분이 이메일 주소 리스트 입니다
		mail.addItemListener(this);

		// 연락처
		list6 = new Choice();
		list6.add("02");
		list6.add("031");
		list6.add("032");
		list6.add("033");
		list6.add("041");
		list6.add("042");
		list6.add("043");
		list6.add("051");
		list6.add("052");
		list6.add("053");

		hp = new Choice();
		hp.add("010");
		hp.add("011");
		hp.add("016");
		hp.add("017");
		hp.add("018");
		hp.add("019");
		hp.addItemListener(this);

		// 최초 '남'에 체크되어져 있는 라디오버튼 객체가 생성(성별/결혼여부/이메일 수신여부)
		ButtonGroup rb = new ButtonGroup();
		JRadioButton cb1 = new JRadioButton("남");
		JRadioButton cb2 = new JRadioButton("여");
		rb.add(cb1);
		rb.add(cb2);

		ButtonGroup rb1 = new ButtonGroup();
		JRadioButton cb3 = new JRadioButton("기혼");
		JRadioButton cb4 = new JRadioButton("미혼");
		rb1.add(cb3);
		rb1.add(cb4);

		ButtonGroup rb2 = new ButtonGroup();
		JRadioButton cb5 = new JRadioButton("보르시아 도르트문트");
		JRadioButton cb6 = new JRadioButton("바르셀로나");
		JRadioButton cb7 = new JRadioButton("첼시");
		JRadioButton cb8 = new JRadioButton("AC밀란");
		rb2.add(cb5);
		rb2.add(cb6);
		rb2.add(cb7);
		rb2.add(cb8);

		ButtonGroup rb3 = new ButtonGroup();
		JRadioButton cb9 = new JRadioButton("수신");
		JRadioButton cb10 = new JRadioButton("수신 안 함");
		rb3.add(cb9);
		rb3.add(cb10);

		// pt부분에 label2를 붙이고 있구요..
		pt.add(label2);

		// p1에 이름 텍스트 박스
		p1.add(name);

		// 마찬가지로 p2에 아이디 와 중복체크 박스
		p2.add(id);
		p2.add(btn1);

		// p3에는 비밀번호 텍스트박스
		p3.add(pw);
		p3_1.add(pw_check);

		// p4에에는 비번 힌트 리스트박스
		p4.add(list);

		// p5에는 비번 힌트 정답
		p5.add(tf4);

		// p5에는 성별 체크박스
		p6.add(cb1);
		p6.add(cb2);

		// p7에는 생년월일 리스트박스
		p7.add(list2);
		p7.add(list3);
		p7.add(list4);
		// p8에는 결혼여부 체크박스
		p8.add(cb3);
		p8.add(cb4);
		// p9에는 주소 텍스트박스+텍스트박스
		p9.add(tf5);
		p9.add(tf6);

		// p10에는 연락처(tel) 텍스트박스
		p10.add(list6);
		p10.add(l20);
		p10.add(tf7);

		// p11에는 연락처(핸드폰) 텍스트
		p11.add(hp);
		p11.add(l18);
		p11.add(phone);

		// p12에는 이메일 텍스트 박스 + 리스트박스 + 직접입력 텍스트박스
		p12.add(name1);
		p12.add(l19);
		p12.add(tf9);
		p12.add(mail);
		p12.add(l14);
		p12.add(name0);

		// p13에는 이메일 수신여부 체크박스
		p13.add(cb5);
		p13.add(cb6);
		p13.add(cb7);
		p13.add(cb8);
		p13.add(l16);

		p14.add(cb9);
		p14.add(cb10);
		p14.add(l17);

		// 전체적인 Layout방식으로는 FlowLayout을 사용
		// p14영역에는 GridLayout을 적용시켜서 화면배치
		// 그리고 l1부터 l5까지를 붙임

		p15.setLayout(new GridLayout(15, 1));
		p15.add(l1);
		p15.add(l2);
		p15.add(l3);
		p15.add(l3_1);
		p15.add(l4);
		p15.add(l5);
		p15.add(l6);
		p15.add(l7);
		p15.add(l8);
		p15.add(l9);
		p15.add(l10);
		p15.add(l11);
		p15.add(l12);
		p15.add(l13);

		p15.add(l15);

		// p15영역에도 GridLayout을 적용시켜서 화면배치
		// p1부터 p13까지를 추가

		p16.setLayout(new GridLayout(15, 1)); // 15를 16으로

		p16.add(p1);
		p16.add(p2);
		p16.add(p3);
		p16.add(p3_1);
		p16.add(p4);
		p16.add(p5);
		p16.add(p6);
		p16.add(p7);
		p16.add(p8);
		p16.add(p9);
		p16.add(p10);
		p16.add(p11);
		p16.add(p12);
		p16.add(p13);
		p16.add(p14);

		// p16영역에는 BorderLayout을 적용시켜서 화면배치

		p17.setLayout(new BorderLayout()); // 16을 17로

		// p17에 버튼 두개를 붙임 //17을 18로
		p18.add(btn2);
		p18.add(btn3);

		// p18영역에도 역시 BorderLayout을 적용시켜서 화면배치
		// 서쪽에 p14를, 중앙에 p15을, 남쪽부분에 p16

		p19.setLayout(new BorderLayout()); // 18을19로
		p19.add(p15, "West"); // 15
		p19.add(p16, "Center"); // 15 16으로
		p19.add(p17, "South"); // 17

		// 프레임의 남쪽에는 pt를, 중앙에 p10을, 남쪽에 p9를 각각 추가
		f.add(pt, "North");
		f.add(p19, "Center"); // 18은 19로
		f.add(p18, "South"); // 17은 18로
		f.pack(); // 프레임을 적절한 크기로 만드는 메소드

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

	@Override
	public void actionPerformed(ActionEvent e) {
// TODO Auto-generated method stub
		String n = name.getText().toString();
		String i = id.getText().toString();
		String p = pw.getText().toString();
		String p_c = pw_check.getText().toString();
		String ph = phone.getText().toString();
		switch (hp.getSelectedItem()) {
		case "010":
			ph = "010" + ph;
			break;
		case "011":
			ph = "011" + ph;
			break;
		case "016":
			ph = "016" + ph;
			break;
		case "017":
			ph = "017" + ph;
			break;
		case "018":
			ph = "018" + ph;
			break;
		case "019":
			ph = "019" + ph;
			break;
		}
		if (e.getSource() == btn2) {
			if (p.equals(p_c)) {
				try {
					Statement stmt = con.createStatement();
					String insert = "insert into member (name, id, pass, phone) values";
					insert += "('" + n + "','" + i + "'," + p + "," + ph + ");";

					int rs = stmt.executeUpdate(insert);
					JOptionPane.showMessageDialog(null, "가입 성공");
					f.setVisible(false);
				} catch (SQLException e1) {
// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
			}
		} else if (e.getSource() == btn1) {
			String sql1 = "select id from member;";
			int a = 0;
			try {
				Statement stmt = con.createStatement();
				ResultSet rs1 = stmt.executeQuery(sql1);
				while (rs1.next()) {
					if (i.equals(rs1.getString("id").toString())) {
						a++;
					}
				}
				if (a == 0) {
					JOptionPane.showMessageDialog(null, "가입 가능한 아이디 입니다.");
				} else if (a > 0) {
					JOptionPane.showMessageDialog(null, "존재하는 아이디 입니다.");
				}
			} catch (SQLException e1) {
// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} else if (e.getSource() == btn3)
			f.setVisible(false);
	}

}
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;

public class ComponentSideMenu extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * 사이드 바를 생성합니다.
	 * */
	public ComponentSideMenu(App app) {
		setPreferredSize(new Dimension(200, app.getHeight()));
		setBackground(new Color(210, 210, 210));
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel verticalLayout = new JPanel();
		add(verticalLayout);
		verticalLayout.setPreferredSize(new Dimension(180, app.getHeight()));
		verticalLayout.setBackground(new Color(210, 210, 210));
		verticalLayout.setLayout(new BoxLayout(verticalLayout, BoxLayout.Y_AXIS));
		
		JButton searchPageButton = new JButton("영화 검색");
		verticalLayout.add(searchPageButton);
		searchPageButton.setPreferredSize(new Dimension(180, 30));
		
		JButton myTicketPageButton = new JButton("나의 예매");
		verticalLayout.add(myTicketPageButton);
		myTicketPageButton.setPreferredSize(new Dimension(180, 30));
		
		JButton logoutButton = new JButton("로그아웃");
		verticalLayout.add(logoutButton);
		logoutButton.setPreferredSize(new Dimension(180, 30));
		
		
		// 이벤트 핸들러
		searchPageButton.addActionListener(e -> app.showPage(App.SEARCH_PAGE));
		myTicketPageButton.addActionListener(e -> app.showPage(App.MY_TICKET_PAGE));
		logoutButton.addActionListener(e -> app.showPage(App.LOGIN_PAGE));
	}

}

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;

public class MyTicketPage extends JPanel {
    private static final long serialVersionUID = 1L;

	public MyTicketPage(App app) {
    	setBackground(new Color(255, 255, 255));
    	setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    	
    	ComponentSideMenu sideMenuBar = new ComponentSideMenu(app);
    	add(sideMenuBar, BorderLayout.WEST);
    	
    	JPanel windowLayout = new JPanel();
		windowLayout.setBackground(new Color(255, 255, 255));
		windowLayout.setPreferredSize(new Dimension(650, getHeight()));
		add(windowLayout, BorderLayout.CENTER);
		windowLayout.setLayout(null);
		
		// 페이지, 헤더 구성.
		JLabel titleLabel = new JLabel("예매된 티켓");
		titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 24));
		titleLabel.setBounds(23, 25, 149, 30);
		windowLayout.add(titleLabel);
		
		JButton btnRemoveReservations = new JButton("선택된 예매 삭제");
		btnRemoveReservations.setForeground(new Color(255, 0, 0));
		btnRemoveReservations.setBounds(505, 25, 117, 29);
		windowLayout.add(btnRemoveReservations);
		
		JScrollPane scrollPane = new JScrollPane();
//		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBackground(new Color(255, 255, 255));
		scrollPane.setLocation(23, 67);
		scrollPane.setSize(599, 496);
		windowLayout.add(scrollPane);
		
		JPanel verticalLayout = new JPanel();
		scrollPane.setViewportView(verticalLayout);
		verticalLayout.setBackground(new Color(255, 255, 255));
		verticalLayout.setLayout(new GridLayout(0, 1, 10, 10));
		
		// 카드 레이아웃.
		
		for (int i = 0; i < 10; i++) {
			verticalLayout.add(new ComponentTicketCard(app));
		}
    }
}

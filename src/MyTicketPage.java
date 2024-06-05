import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

public class MyTicketPage extends JPanel {
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
		
        windowLayout.add(new JLabel("My Ticket Page"));
    }
}

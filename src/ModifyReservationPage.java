import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

public class ModifyReservationPage extends JPanel {
    private static final long serialVersionUID = 1L;

	public ModifyReservationPage(App app) {
    	setBackground(new Color(255, 255, 255));
    	setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    	
    	ComponentSideMenu sideMenuBar = new ComponentSideMenu(app);
    	add(sideMenuBar, BorderLayout.WEST);
    	
    	JPanel windowLayout = new JPanel();
		windowLayout.setBackground(new Color(255, 255, 255));
		windowLayout.setPreferredSize(new Dimension(650, getHeight()));
		add(windowLayout, BorderLayout.CENTER);
		windowLayout.setLayout(null);
		
		windowLayout.add(new JLabel("Modify Reservation Page"));
    }
}

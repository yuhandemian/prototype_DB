import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ComponentTicketCard extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public ComponentTicketCard(App app, JoinedTicketObj ticket) {
		setBackground(new Color(242, 242, 242));
		setBounds(6, 5, 583, 160);
		setPreferredSize(new Dimension(580, 160));
		setBackground(new Color(255, 255, 255));
//		setBorder(BorderFactory.createEtchedBorder());
		setLayout(null);
		
		JButton btnChangeMovie = new JButton("영화 변경");
		btnChangeMovie.setBounds(334, 113, 80, 29);
		add(btnChangeMovie);
		
		JButton btnChangeTime = new JButton("시간 변경");
		btnChangeTime.setBounds(412, 113, 80, 29);
		add(btnChangeTime);
		
		JButton btnRemoveReservation = new JButton("영화 삭제");
		btnRemoveReservation.setBounds(486, 113, 80, 29);
		btnRemoveReservation.setForeground(new Color(255, 0, 0));
		add(btnRemoveReservation);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("");
		chckbxNewCheckBox.setBounds(538, 10, 28, 23);
		add(chckbxNewCheckBox);
		
		JLabel ticketTitleLabel = new JLabel(ticket.MovieTitle);
		ticketTitleLabel.setBounds(17, 10, 482, 16);
		ticketTitleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		add(ticketTitleLabel);
		
		JLabel locationLabel = new JLabel(ticket.ScheduleDate + "  " + ticket.ScheduleTime + "  " + ticket.TheaterName + "  " + ticket.SeatName);
		locationLabel.setBounds(17, 36, 482, 16);
		locationLabel.setForeground(new Color(127, 127, 127));
		locationLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		add(locationLabel);
		
		JLabel costLabel = new JLabel(ticket.SalePrice + "원");
		costLabel.setBounds(17, 64, 482, 16);
		costLabel.setForeground(new Color(127, 127, 127));
		costLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		add(costLabel);
		
		// Event Listeners
		addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	app.showPage(App.RESERVATION_DETAIL);
            }
        });
		btnChangeMovie.addActionListener(e -> app.showPage(App.RESERVATION_PAGE));
		btnChangeTime.addActionListener(e -> app.showPage(App.RESERVATION_PAGE));
	}

}

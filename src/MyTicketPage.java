import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.*;

// TODO: Create GET_USER_ID Function
public class MyTicketPage extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JPanel verticalLayout;

    // Todo: remove Ticket;
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
//		scrollPane.setBackground(new Color(255, 255, 255));
		scrollPane.setLocation(23, 67);
		scrollPane.setSize(599, 496);
		scrollPane.setBorder(null);
		windowLayout.add(scrollPane);
		
		verticalLayout = new JPanel();
		scrollPane.setViewportView(verticalLayout);
		verticalLayout.setBorder(null);
		verticalLayout.setBackground(new Color(255, 255, 255));
		verticalLayout.setLayout(new GridLayout(0, 1, 10, 10));
		
		// 카드 레이아웃
		updateTicketCards(app);
    }
	
	private void updateTicketCards(App app) {
		JoinedTicketObj[] tickets = getTicketData();
		
		verticalLayout.removeAll();
		
		for (JoinedTicketObj ticket : tickets) {
			verticalLayout.add(new ComponentTicketCard(app, ticket));
		}
		
		verticalLayout.revalidate();
		verticalLayout.repaint();
	}
	
	private JoinedTicketObj[] getTicketData() {
		final int USER_ID = 1;
		
    	ArrayList<JoinedTicketObj> tickets = new ArrayList<>();
    	String query = "SELECT * FROM Booking, Ticket, Theater, Seat, ScreeningSchedule, Movie "
    			+ "WHERE booking.CustomerID = " + USER_ID + " "
    			+ "AND Ticket.BookingID = Booking.BookingID "
    			+ "AND Ticket.TheaterID = Theater.TheaterID "
    			+ "AND Ticket.SeatID = Seat.SeatID AND Ticket.TheaterID = Seat.TheaterID "
    			+ "AND ticket.ScheduleID = ScreeningSchedule.ScheduleID "
    			+ "AND ScreeningSchedule.MovieID = Movie.MovieID";
    	
    	try (Connection conn = DatabaseConnection.getUserConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
    		
    		while (rs.next()) {
    			tickets.add(new JoinedTicketObj(rs));
    		}
    		
       } catch (SQLException e) {
           JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
       }
    	
    	return tickets.toArray(new JoinedTicketObj[0]);
    }
}



import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

class JoinedTicketObj {
	public String TicketID;
	public String ScheduleID;
	public String TheaterID;
	public String SeatID;
	public String BookingID;
	
	public String MovieTitle;
	public String ScheduleDate;
	public String ScheduleTime;
	public String TheaterName;
	public String SeatName;
	public String StandardPrice;
	public String TicketPrice;
	
	public String IsIssued;
	public String SalePrice;

	
	public JoinedTicketObj(ResultSet rs) {
		try {
			
			TicketID = rs.getString("TicketID");
			ScheduleID = rs.getString("ScheduleID");
			TheaterID = rs.getString("TheaterID");
			SeatID = rs.getString("SeatID");
			BookingID = rs.getString("BookingID");
			
			MovieTitle = rs.getString("Title");
			ScheduleDate = rs.getString("StartDate");
			ScheduleTime = rs.getString("StartTime");
			TheaterName = rs.getString("TheaterName");
			SeatName = rs.getString("SeatName");
			StandardPrice = rs.getString("StandardPrice");
			TicketPrice = rs.getString("SalePrice");
			SalePrice = rs.getString("SalePrice");
			
			IsIssued = rs.getString("IsIssued");
			
			System.out.println("Ticket : " + MovieTitle);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "JoinedTicketObj -> Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}

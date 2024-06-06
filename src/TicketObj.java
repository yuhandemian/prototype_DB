

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

class TicketObj {
	public String TicketID;
	public String ScheduleID;
	public String TheaterID;
	public String SeatID;
	public String BookingID;
	public String IsIssued;
	public String StandardPrice;
	public String SalePrice;

	
	public TicketObj(ResultSet rs) {
		try {
			
			TicketID = rs.getString("TicketID");
			ScheduleID = rs.getString("ScheduleID");
			TheaterID = rs.getString("TheaterID");
			SeatID = rs.getString("SeatID");
			BookingID = rs.getString("BookingID");
			IsIssued = rs.getString("IsIssued");
			StandardPrice = rs.getString("StandardPrice");
			SalePrice = rs.getString("SalePrice");
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
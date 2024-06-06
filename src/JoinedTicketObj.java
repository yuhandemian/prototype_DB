import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

class JoinedTicketObj {
    public int TicketID;
    public int ScheduleID;
    public int TheaterID;
    public int SeatID;
    public int BookingID;

    public String MovieTitle;
    public String ScheduleDate;
    public String ScheduleTime;
    public String TheaterName;
    public String SeatName;
    public int StandardPrice;
    public int SalePrice;

    public String IsIssued;

    public JoinedTicketObj(ResultSet rs) {
        try {
            TicketID = rs.getInt("TicketID");
            ScheduleID = rs.getInt("ScheduleID");
            TheaterID = rs.getInt("TheaterID");
            SeatID = rs.getInt("SeatID");
            BookingID = rs.getInt("BookingID");

            MovieTitle = rs.getString("Title");
            ScheduleDate = rs.getString("StartDate");
            ScheduleTime = rs.getString("StartTime");
            TheaterName = rs.getString("TheaterName");
            SeatName = rs.getString("SeatName");
            StandardPrice = rs.getInt("StandardPrice");
            SalePrice = rs.getInt("SalePrice");

            IsIssued = rs.getString("IsIssued");

            System.out.println("Ticket : " + MovieTitle);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "JoinedTicketObj -> Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyTicketPage extends JPanel {
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    private App app;

    public MyTicketPage(App app) {
        this.app = app;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"티켓 ID", "영화명", "상영일", "상영관 번호", "좌석번호", "판매가격", "티켓 발행 여부"}, 0);
        ticketTable = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ticketTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ticketTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = ticketTable.getSelectedRow();
                    int ticketID = (int) ticketTable.getValueAt(row, 0);
                    app.showReservationDetail(ticketID);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(ticketTable);
        add(scrollPane, BorderLayout.CENTER);

        loadTickets();
    }

    private void loadTickets() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getUserConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT T.TicketID, M.Title, S.StartDate, S.TheaterID, T.SeatID, T.SalePrice, T.IsIssued " +
                             "FROM Ticket T " +
                             "JOIN ScreeningSchedule S ON T.ScheduleID = S.ScheduleID " +
                             "JOIN Movie M ON S.MovieID = M.MovieID " +
                             "WHERE T.BookingID = ?")) { // 여기서 BookingID는 예시로 사용, 실제 사용자 ID 등을 사용해야 함
            ps.setInt(1, 1);  // 예시로 1번 BookingID를 사용
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int ticketID = rs.getInt("TicketID");
                    String title = rs.getString("Title");
                    Date startDate = rs.getDate("StartDate");
                    int theaterID = rs.getInt("TheaterID");
                    int seatID = rs.getInt("SeatID");
                    double salePrice = rs.getDouble("SalePrice");
                    boolean isIssued = rs.getBoolean("IsIssued");

                    tableModel.addRow(new Object[]{ticketID, title, startDate, theaterID, seatID, salePrice, isIssued ? "발행됨" : "발행되지 않음"});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading tickets: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class UserBookingDetailsPage extends JPanel {
    private static final long serialVersionUID = 1L;
    private App app;
    private JPanel bookingDetailsPanel;

    public UserBookingDetailsPage(App app) {
        this.app = app;
        setLayout(new BorderLayout());

        // 사이드 바 추가
        ComponentSideMenu sideMenu = new ComponentSideMenu(app);
        add(sideMenu, BorderLayout.WEST);

        // 예매 정보 패널
        bookingDetailsPanel = new JPanel();
        bookingDetailsPanel.setLayout(new BoxLayout(bookingDetailsPanel, BoxLayout.Y_AXIS));
        bookingDetailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(bookingDetailsPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // 예매 정보 불러오기
        loadUserBookingDetails(app.getCurrentUserId());
    }

    private void loadUserBookingDetails(int userId) {
        try (Connection conn = DatabaseConnection.getUserConnection()) {
            String query = "SELECT Movie.Title, ScreeningSchedule.StartDate, ScreeningSchedule.StartTime, " +
                           "Theater.TheaterName, Seat.SeatName " +
                           "FROM Ticket " +
                           "JOIN ScreeningSchedule ON Ticket.ScheduleID = ScreeningSchedule.ScheduleID " +
                           "JOIN Movie ON ScreeningSchedule.MovieID = Movie.MovieID " +
                           "JOIN Theater ON ScreeningSchedule.TheaterID = Theater.TheaterID " +
                           "JOIN Seat ON Ticket.SeatID = Seat.SeatID " +
                           "JOIN Booking ON Ticket.BookingID = Booking.BookingID " +
                           "WHERE Booking.CustomerID = ?";

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String movieTitle = rs.getString("Title");
                        String startDate = rs.getString("StartDate");
                        String startTime = rs.getString("StartTime");
                        String theaterName = rs.getString("TheaterName");
                        String seatName = rs.getString("SeatName");

                        addBookingDetail(movieTitle, startDate, startTime, theaterName, seatName);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "예매 정보를 불러오는 중 오류 발생: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void addBookingDetail(String movieTitle, String startDate, String startTime, String theaterName, String seatName) {
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        detailPanel.setPreferredSize(new Dimension(600, 150));

        JLabel movieTitleLabel = new JLabel("영화 제목: " + movieTitle);
        movieTitleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
        detailPanel.add(movieTitleLabel);

        JLabel scheduleDateLabel = new JLabel("상영 날짜: " + startDate);
        scheduleDateLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        detailPanel.add(scheduleDateLabel);

        JLabel scheduleTimeLabel = new JLabel("상영 시간: " + startTime);
        scheduleTimeLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        detailPanel.add(scheduleTimeLabel);

        JLabel theaterLabel = new JLabel("상영관: " + theaterName);
        theaterLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        detailPanel.add(theaterLabel);

        JLabel seatLabel = new JLabel("좌석 번호: " + seatName);
        seatLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        detailPanel.add(seatLabel);

        bookingDetailsPanel.add(detailPanel);
        bookingDetailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
}

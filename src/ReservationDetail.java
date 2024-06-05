import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReservationDetail extends JPanel {
    private JLabel movieNameLabel, bookingIdLabel, scheduleLabel, seatLabel, standardPriceLabel, salePriceLabel, issuedLabel;
    private JButton changeMovieButton, changeTimeButton, cancelButton;
    private int ticketID;
    private App app;

    public ReservationDetail(App app) {
        this.app = app;
        setLayout(null);

        JButton backButton = new JButton("뒤로가기");
        backButton.setBounds(10, 10, 100, 30);
        add(backButton);

        backButton.addActionListener(e -> app.showPage(App.MY_TICKET_PAGE));

        movieNameLabel = new JLabel();
        movieNameLabel.setBounds(10, 50, 300, 30);
        add(movieNameLabel);

        bookingIdLabel = new JLabel();
        bookingIdLabel.setBounds(10, 90, 300, 30);
        add(bookingIdLabel);

        scheduleLabel = new JLabel();
        scheduleLabel.setBounds(10, 130, 300, 30);
        add(scheduleLabel);

        seatLabel = new JLabel();
        seatLabel.setBounds(10, 170, 300, 30);
        add(seatLabel);

        standardPriceLabel = new JLabel();
        standardPriceLabel.setBounds(10, 210, 300, 30);
        add(standardPriceLabel);

        salePriceLabel = new JLabel();
        salePriceLabel.setBounds(10, 250, 300, 30);
        add(salePriceLabel);

        issuedLabel = new JLabel();
        issuedLabel.setBounds(10, 290, 300, 30);
        add(issuedLabel);

        changeMovieButton = new JButton("영화 변경");
        changeMovieButton.setBounds(10, 330, 100, 30);
        add(changeMovieButton);

        changeTimeButton = new JButton("시간 변경");
        changeTimeButton.setBounds(120, 330, 100, 30);
        add(changeTimeButton);

        cancelButton = new JButton("예매 취소");
        cancelButton.setBounds(230, 330, 100, 30);
        add(cancelButton);

        changeMovieButton.addActionListener(e -> changeMovie());
        changeTimeButton.addActionListener(e -> changeTime());
        cancelButton.addActionListener(e -> cancelTicket());
    }

    public void displayTicketDetails(int ticketID) {
        this.ticketID = ticketID;

        try (Connection conn = DatabaseConnection.getUserConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT M.Title, T.BookingID, S.StartDate, S.StartTime, T.SeatID, T.StandardPrice, T.SalePrice, T.IsIssued " +
                             "FROM Ticket T " +
                             "JOIN ScreeningSchedule S ON T.ScheduleID = S.ScheduleID " +
                             "JOIN Movie M ON S.MovieID = M.MovieID " +
                             "WHERE T.TicketID = ?")) {
            ps.setInt(1, ticketID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String movieTitle = rs.getString("Title");
                    int bookingID = rs.getInt("BookingID");
                    Date startDate = rs.getDate("StartDate");
                    Time startTime = rs.getTime("StartTime");
                    int seatID = rs.getInt("SeatID");
                    double standardPrice = rs.getDouble("StandardPrice");
                    double salePrice = rs.getDouble("SalePrice");
                    boolean isIssued = rs.getBoolean("IsIssued");

                    movieNameLabel.setText("영화명: " + movieTitle);
                    bookingIdLabel.setText("예매 번호: " + bookingID);
                    scheduleLabel.setText("상영 일정: " + startDate + " " + startTime);
                    seatLabel.setText("좌석: " + seatID);
                    standardPriceLabel.setText("표준 가격: " + standardPrice);
                    salePriceLabel.setText("판매 가격: " + salePrice);
                    issuedLabel.setText("티켓 발행 여부: " + (isIssued ? "발행됨" : "발행되지 않음"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading ticket details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changeMovie() {
        JOptionPane.showMessageDialog(this, "영화 변경 기능은 아직 구현되지 않았습니다.", "영화 변경", JOptionPane.INFORMATION_MESSAGE);
    }

    private void changeTime() {
        JOptionPane.showMessageDialog(this, "시간 변경 기능은 아직 구현되지 않았습니다.", "시간 변경", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cancelTicket() {
        int confirm = JOptionPane.showConfirmDialog(this, "예매를 취소하시겠습니까?", "예매 취소 확인", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DatabaseConnection.getUserConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Ticket WHERE TicketID = ?")) {
            ps.setInt(1, ticketID);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "예매가 취소되었습니다.", "예매 완료", JOptionPane.INFORMATION_MESSAGE);
            app.showPage(App.MY_TICKET_PAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error canceling ticket: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

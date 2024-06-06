import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReservationDetail extends JPanel {
    private JLabel movieNameLabel, bookingIdLabel, scheduleLabel, seatLabel, standardPriceLabel, salePriceLabel, issuedLabel;
    private JButton changeMovieButton, changeTimeButton, cancelButton;
    private int ticketID;
    private App app;
    private JoinedTicketObj currentTicket;

    public ReservationDetail(App app) {
        this.app = app;
        setLayout(new BorderLayout());

        // 사이드 메뉴 바 추가
        ComponentSideMenu sideMenuBar = new ComponentSideMenu(app);
        add(sideMenuBar, BorderLayout.WEST);

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(detailPanel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("예매 상세보기");
        titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(titleLabel);

        movieNameLabel = new JLabel();
        movieNameLabel.setFont(new Font("Lucida Grande", Font.BOLD, 18));
        movieNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(movieNameLabel);

        detailPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        bookingIdLabel = new JLabel();
        bookingIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(bookingIdLabel);

        scheduleLabel = new JLabel();
        scheduleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(scheduleLabel);

        seatLabel = new JLabel();
        seatLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(seatLabel);

        standardPriceLabel = new JLabel();
        standardPriceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(standardPriceLabel);

        salePriceLabel = new JLabel();
        salePriceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(salePriceLabel);

        issuedLabel = new JLabel();
        issuedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(issuedLabel);

        detailPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        changeMovieButton = new JButton("영화 변경");
        changeMovieButton.setBackground(Color.WHITE);
        buttonPanel.add(changeMovieButton);

        changeTimeButton = new JButton("시간 변경");
        changeTimeButton.setBackground(Color.WHITE);
        buttonPanel.add(changeTimeButton);

        cancelButton = new JButton("예매 취소");
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setForeground(Color.RED);
        buttonPanel.add(cancelButton);

        detailPanel.add(buttonPanel);

        changeMovieButton.addActionListener(e -> {
            app.setCurrentTicket(currentTicket);
            app.showPage(App.MODIFY_RESERVATION_PAGE);
        });

        changeTimeButton.addActionListener(e -> {
            app.setCurrentTicket(currentTicket);
            app.showPage(App.MODIFY_RESERVATION_PAGE);
        });

        cancelButton.addActionListener(e -> cancelTicket());
    }

    public void displayTicketDetails(int ticketID) {
        this.ticketID = ticketID;

        try (Connection conn = DatabaseConnection.getUserConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT T.TicketID, T.ScheduleID, T.TheaterID, T.SeatID, T.BookingID, M.Title, S.StartDate, S.StartTime, TH.TheaterName, SE.SeatName, T.StandardPrice, T.SalePrice, T.IsIssued " +
                             "FROM Ticket T " +
                             "JOIN ScreeningSchedule S ON T.ScheduleID = S.ScheduleID " +
                             "JOIN Movie M ON S.MovieID = M.MovieID " +
                             "JOIN Theater TH ON T.TheaterID = TH.TheaterID " +
                             "JOIN Seat SE ON T.SeatID = SE.SeatID " +
                             "WHERE T.TicketID = ?")) {
            ps.setInt(1, ticketID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    currentTicket = new JoinedTicketObj(rs);

                    movieNameLabel.setText(currentTicket.MovieTitle);
                    bookingIdLabel.setText("예매번호: " + currentTicket.BookingID);
                    scheduleLabel.setText("상영 날짜: " + currentTicket.ScheduleDate + " " + currentTicket.ScheduleTime);
                    seatLabel.setText("좌석: " + currentTicket.TheaterName + " " + currentTicket.SeatName);
                    standardPriceLabel.setText("표준가격: " + currentTicket.StandardPrice + "원");
                    salePriceLabel.setText("판매가격: " + currentTicket.SalePrice + "원");
                    issuedLabel.setText("발행여부: " + (currentTicket.IsIssued.equals("1") ? "Yes" : "No"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading ticket details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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

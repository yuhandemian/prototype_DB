import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

public class ReservationPage extends JPanel {
    private static final long serialVersionUID = 1L;
    private JComboBox<String> movieComboBox;
    private JComboBox<String> dateComboBox;
    private JPanel timePanel;
    private JButton[][] seatButtons;
    private JLabel ticketCostLabel;
    private App app;
    private int selectedMovieID;
    private int selectedScheduleID;
    private int selectedSeatID;
    private int ticketCost = 7000;
    private String selectedSeatName;
    private HashMap<JButton, Integer> timeButtonToScheduleIDMap = new HashMap<>();

    public ReservationPage(App app) {
        this.app = app;
        setBackground(new Color(255, 255, 255));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        ComponentSideMenu sideMenuBar = new ComponentSideMenu(app);
        add(sideMenuBar, BorderLayout.WEST);

        JPanel windowLayout = new JPanel();
        windowLayout.setBackground(new Color(255, 255, 255));
        windowLayout.setPreferredSize(new Dimension(650, getHeight()));
        add(windowLayout, BorderLayout.CENTER);
        windowLayout.setLayout(null);

        JLabel titleLabel = new JLabel("영화 예매");
        titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 24));
        titleLabel.setBounds(23, 25, 149, 30);
        windowLayout.add(titleLabel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setLocation(23, 67);
        scrollPane.setSize(599, 496);
        windowLayout.add(scrollPane);

        JPanel verticalLayout = new JPanel();
        verticalLayout.setBackground(new Color(255, 255, 255));
        scrollPane.setViewportView(verticalLayout);
        verticalLayout.setLayout(new BoxLayout(verticalLayout, BoxLayout.Y_AXIS));

        JLabel subTitle1 = new JLabel("영화 선택");
        subTitle1.setFont(new Font("Lucida Grande", Font.BOLD, 20));
        verticalLayout.add(subTitle1);

        movieComboBox = new JComboBox<>();
        loadMovies();
        verticalLayout.add(movieComboBox);

        JLabel subTitle2 = new JLabel("날짜 선택");
        subTitle2.setFont(new Font("Lucida Grande", Font.BOLD, 20));
        verticalLayout.add(subTitle2);

        dateComboBox = new JComboBox<>();
        verticalLayout.add(dateComboBox);

        JLabel subTitle3 = new JLabel("시간 선택");
        subTitle3.setFont(new Font("Lucida Grande", Font.BOLD, 20));
        verticalLayout.add(subTitle3);
        scrollPane.setBackground(new Color(255, 255, 255));

        timePanel = new JPanel();
        timePanel.setBackground(new Color(255, 255, 255));
        timePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        verticalLayout.add(timePanel);

        JLabel subTitle4 = new JLabel("좌석 선택");
        subTitle4.setFont(new Font("Lucida Grande", Font.BOLD, 20));
        verticalLayout.add(subTitle4);

        final int SEATS_ROW = 10; // 기본 좌석 수를 10x10으로 가정
        final int SEATS_COL = 10;

        JPanel seatsLayout = new JPanel();
        seatsLayout.setBackground(new Color(255, 255, 255));
        verticalLayout.add(seatsLayout);
        seatsLayout.setLayout(new GridLayout(SEATS_ROW, SEATS_COL, 10, 10));

        seatButtons = new JButton[SEATS_ROW][SEATS_COL];
        for (int i = 0; i < SEATS_ROW; i++) {
            for (int j = 0; j < SEATS_COL; j++) {
                seatButtons[i][j] = new JButton("X");
                seatButtons[i][j].setPreferredSize(new Dimension(10, 10));
                seatButtons[i][j].setBackground(Color.WHITE);
                seatButtons[i][j].setForeground(Color.BLACK);
                seatsLayout.add(seatButtons[i][j]);

                final int row = i;
                final int col = j;
                seatButtons[i][j].addActionListener(e -> selectSeat(row, col));
            }
        }

        JPanel costLayout = new JPanel();
        costLayout.setBackground(new Color(255, 255, 255));
        verticalLayout.add(costLayout);
        costLayout.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));

        JLabel subsubTitle = new JLabel("티켓 가격");
        subsubTitle.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        costLayout.add(subsubTitle);

        ticketCostLabel = new JLabel(ticketCost + "원");
        ticketCostLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        costLayout.add(ticketCostLabel);

        JButton reserveButton = new JButton("예매하기");
        reserveButton.setForeground(Color.BLACK);
        reserveButton.setBackground(new Color(0, 121, 255));
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reserveTicket();
            }
        });
        verticalLayout.add(reserveButton);

        JButton cancelButton = new JButton("취소");
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setBackground(new Color(0, 121, 255));
        cancelButton.addActionListener(e -> app.showPage(App.SEARCH_PAGE));
        verticalLayout.add(cancelButton);

        movieComboBox.addActionListener(e -> loadDatesForSelectedMovie());
        dateComboBox.addActionListener(e -> loadTimesForSelectedMovie());
    }

    private void loadMovies() {
        try (Connection conn = DatabaseConnection.getUserConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MovieID, Title FROM Movie")) {
            while (rs.next()) {
                movieComboBox.addItem(rs.getString("Title"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "영화 목록을 불러오는 중 오류 발생: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDatesForSelectedMovie() {
        String selectedMovie = (String) movieComboBox.getSelectedItem();
        try (Connection conn = DatabaseConnection.getUserConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT StartDate FROM ScreeningSchedule WHERE MovieID = (SELECT MovieID FROM Movie WHERE Title = ?) ORDER BY StartDate")) {
            ps.setString(1, selectedMovie);
            try (ResultSet rs = ps.executeQuery()) {
                dateComboBox.removeAllItems();
                while (rs.next()) {
                    dateComboBox.addItem(rs.getString("StartDate"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "상영 날짜를 불러오는 중 오류 발생: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTimesForSelectedMovie() {
        String selectedMovie = (String) movieComboBox.getSelectedItem();
        String selectedDate = (String) dateComboBox.getSelectedItem();
        try (Connection conn = DatabaseConnection.getUserConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT ScheduleID, StartTime FROM ScreeningSchedule WHERE MovieID = (SELECT MovieID FROM Movie WHERE Title = ?) AND StartDate = ? ORDER BY StartTime")) {
            ps.setString(1, selectedMovie);
            ps.setString(2, selectedDate);
            try (ResultSet rs = ps.executeQuery()) {
                timePanel.removeAll();
                timeButtonToScheduleIDMap.clear();
                while (rs.next()) {
                    int scheduleID = rs.getInt("ScheduleID");
                    String startTime = rs.getString("StartTime");

                    JButton timeButton = new JButton(startTime);
                    timeButton.setForeground(Color.BLACK);
                    timeButton.setBackground(Color.WHITE);
                    timeButton.addActionListener(e -> loadSeatsForSelectedTime(scheduleID));
                    timePanel.add(timeButton);

                    // Store the scheduleID for this time button
                    timeButtonToScheduleIDMap.put(timeButton, scheduleID);
                }
                timePanel.revalidate();
                timePanel.repaint();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "상영 시간을 불러오는 중 오류 발생: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSeatsForSelectedTime(int scheduleID) {
        selectedScheduleID = scheduleID;
        LocalDate currentDate = app.getCurrentDate();
        LocalTime currentTime = app.getCurrentTime();
        
        try (Connection conn = DatabaseConnection.getUserConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT TheaterID, StartDate, StartTime FROM ScreeningSchedule WHERE ScheduleID = ?")) {
            ps.setInt(1, scheduleID);

            int theaterID = 0;
            LocalDate movieDate = null;
            LocalTime movieTime = null;
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    theaterID = rs.getInt("TheaterID");
                    movieDate = rs.getDate("StartDate").toLocalDate();
                    movieTime = rs.getTime("StartTime").toLocalTime();
                }
            }

            boolean isPastShow = movieDate.isBefore(currentDate) || (movieDate.isEqual(currentDate) && movieTime.isBefore(currentTime));

            // 좌석 초기화
            for (int i = 0; i < seatButtons.length; i++) {
                for (int j = 0; j < seatButtons[i].length; j++) {
                    seatButtons[i][j].setText("X");
                    seatButtons[i][j].setEnabled(false);
                    seatButtons[i][j].setBackground(Color.WHITE);
                    seatButtons[i][j].setForeground(Color.BLACK);
                }
            }

            try (PreparedStatement ps2 = conn.prepareStatement(
                    "SELECT SeatID, SeatName, IsAvailable FROM Seat WHERE TheaterID = ?")) {
                ps2.setInt(1, theaterID);

                try (ResultSet rs2 = ps2.executeQuery()) {
                    while (rs2.next()) {
                        int seatID = rs2.getInt("SeatID");
                        String seatName = rs2.getString("SeatName");
                        boolean isAvailable = rs2.getBoolean("IsAvailable");
                        int row = (seatID - 1) / 10;
                        int col = (seatID - 1) % 10;

                        if (isPastShow) {
                            seatButtons[row][col].setText("X");
                            seatButtons[row][col].setEnabled(false);
                            seatButtons[row][col].setBackground(Color.GRAY);
                        } else {
                            seatButtons[row][col].setText(seatName);
                            seatButtons[row][col].setEnabled(isAvailable);
                            seatButtons[row][col].setBackground(isAvailable ? Color.WHITE : Color.GRAY);
                        }
                    }

                    // 이미 예약된 좌석 불러오기
                    if (!isPastShow) {
                        try (PreparedStatement ps3 = conn.prepareStatement(
                                "SELECT SeatID FROM Ticket WHERE ScheduleID = ?")) {
                            ps3.setInt(1, selectedScheduleID);

                            try (ResultSet rs3 = ps3.executeQuery()) {
                                while (rs3.next()) {
                                    int seatID = rs3.getInt("SeatID");
                                    int row = (seatID - 1) / 10;
                                    int col = (seatID - 1) % 10;
                                    seatButtons[row][col].setBackground(Color.GRAY);
                                    seatButtons[row][col].setEnabled(false);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "좌석 정보를 불러오는 중 오류 발생: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setSelectedMovie(String movieTitle) {
        if (movieTitle != null && !movieTitle.isEmpty()) {
            movieComboBox.setSelectedItem(movieTitle);
        }
    }

    private void selectSeat(int row, int col) {
        for (int i = 0; i < seatButtons.length; i++) {
            for (int j = 0; j < seatButtons[i].length; j++) {
                if (seatButtons[i][j].getBackground() == Color.GREEN) {
                    seatButtons[i][j].setBackground(Color.WHITE);
                }
            }
        }
        seatButtons[row][col].setBackground(Color.BLUE);
        selectedSeatID = row * 10 + col + 1; // 예시로 좌석 ID를 계산
        selectedSeatName = seatButtons[row][col].getText();
        ticketCostLabel.setText(ticketCost + "원");
    }

    private void reserveTicket() {
        String selectedMovie = (String) movieComboBox.getSelectedItem();
        String selectedDate = (String) dateComboBox.getSelectedItem();
        String selectedTime = getSelectedTimeFromScheduleID(selectedScheduleID);

        int confirm = JOptionPane.showConfirmDialog(this, "영화: " + selectedMovie + "\n날짜: " + selectedDate + "\n시간: " + selectedTime + "\n좌석: " + selectedSeatName + "\n가격: " + ticketCost + "원\n예매를 확정하시겠습니까?", "예매 확인", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = DatabaseConnection.getUserConnection()) {
            // 실제 TheaterID 값을 가져오기 위해 ScheduleID를 사용
            int theaterID = 0;
            try (PreparedStatement ps1 = conn.prepareStatement(
                    "SELECT TheaterID FROM ScreeningSchedule WHERE ScheduleID = ?")) {
                ps1.setInt(1, selectedScheduleID);
                try (ResultSet rs = ps1.executeQuery()) {
                    if (rs.next()) {
                        theaterID = rs.getInt("TheaterID");
                    }
                }
            }

            // 실제 BookingID 값을 생성하기 위한 예제 코드
            int bookingID = 0;
            try (PreparedStatement ps2 = conn.prepareStatement(
                    "INSERT INTO Booking (PaymentMethod, PaymentStatus, Amount, CustomerID, PaymentDate) VALUES (?, ?, ?, ?, NOW())", Statement.RETURN_GENERATED_KEYS)) {
                ps2.setString(1, "Credit Card");
                ps2.setString(2, "Completed");
                ps2.setInt(3, ticketCost);
                ps2.setInt(4, app.getCurrentUserId());
                ps2.executeUpdate();

                try (ResultSet rs = ps2.getGeneratedKeys()) {
                    if (rs.next()) {
                        bookingID = rs.getInt(1);
                    }
                }
            }

            // 티켓 예약
            try (PreparedStatement ps3 = conn.prepareStatement(
                    "INSERT INTO Ticket (ScheduleID, TheaterID, SeatID, BookingID, IsIssued, StandardPrice, SalePrice) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                ps3.setInt(1, selectedScheduleID); // ScheduleID
                ps3.setInt(2, theaterID); // TheaterID
                ps3.setInt(3, selectedSeatID); // SeatID
                ps3.setInt(4, bookingID); // BookingID
                ps3.setBoolean(5, true); // IsIssued
                ps3.setInt(6, ticketCost); // StandardPrice
                ps3.setInt(7, ticketCost); // SalePrice

                ps3.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "예매가 완료되었습니다.", "Success", JOptionPane.INFORMATION_MESSAGE);

            // 예매 완료 후 예매 확인 페이지 업데이트
            app.showPage(App.MY_TICKET_PAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "예매 중 오류 발생: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getSelectedTimeFromScheduleID(int scheduleID) {
        try (Connection conn = DatabaseConnection.getUserConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT StartTime FROM ScreeningSchedule WHERE ScheduleID = ?")) {
            ps.setInt(1, scheduleID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("StartTime");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "시간 정보를 불러오는 중 오류 발생: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}

       

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ReservationPage extends JPanel {
    private JComboBox<String> movieComboBox, dateComboBox;
    private JPanel schedulePanel, seatPanel;
    private JLabel priceLabel;
    private JButton reserveButton;
    private int selectedScheduleID = -1;
    private int selectedSeatID = -1;
    private double ticketPrice = 0.0;
    private Map<String, Integer> movieMap = new HashMap<>();
    private Map<String, Integer> scheduleMap = new HashMap<>();

    public ReservationPage(App app) {
        setLayout(new BorderLayout());

        // 상단에 뒤로가기 버튼 배치
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("뒤로가기");
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        // 뒤로가기 버튼 동작 정의
        backButton.addActionListener(e -> app.showPage(App.SEARCH_PAGE));

        // 영화 선택 패널
        JPanel moviePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        movieComboBox = new JComboBox<>();
        movieComboBox.addActionListener(e -> loadDates());
        moviePanel.add(new JLabel("영화 선택:"));
        moviePanel.add(movieComboBox);
        add(moviePanel, BorderLayout.NORTH);

        // 날짜 선택 패널
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dateComboBox = new JComboBox<>();
        dateComboBox.addActionListener(e -> loadSchedules());
        datePanel.add(new JLabel("상영 날짜 선택:"));
        datePanel.add(dateComboBox);
        add(datePanel, BorderLayout.WEST);

        // 상영 일정 패널
        schedulePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        schedulePanel.setBorder(BorderFactory.createTitledBorder("상영 일정"));
        add(new JScrollPane(schedulePanel), BorderLayout.CENTER);

        // 좌석 선택 패널
        seatPanel = new JPanel(new GridLayout(0, 10, 5, 5));
        seatPanel.setBorder(BorderFactory.createTitledBorder("좌석 선택"));
        add(new JScrollPane(seatPanel), BorderLayout.EAST);

        // 가격 및 예매 버튼 패널
        JPanel reservePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        priceLabel = new JLabel("가격: 0원");
        reserveButton = new JButton("예매하기");
        reserveButton.addActionListener(e -> reserveTicket());
        reservePanel.add(priceLabel);
        reservePanel.add(reserveButton);
        add(reservePanel, BorderLayout.SOUTH);

        loadMovies();
    }

    private void loadMovies() {
        try (Connection conn = DatabaseConnection.getUserConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MovieID, Title FROM Movie WHERE EXISTS (SELECT * FROM ScreeningSchedule WHERE Movie.MovieID = ScreeningSchedule.MovieID)")) {

            while (rs.next()) {
                int movieID = rs.getInt("MovieID");
                String title = rs.getString("Title");
                movieMap.put(title, movieID);
                movieComboBox.addItem(title);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading movies: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDates() {
        dateComboBox.removeAllItems();
        scheduleMap.clear();

        String selectedMovie = (String) movieComboBox.getSelectedItem();
        if (selectedMovie == null) return;

        int movieID = movieMap.get(selectedMovie);

        try (Connection conn = DatabaseConnection.getUserConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT StartDate FROM ScreeningSchedule WHERE MovieID = ?")) {
            ps.setInt(1, movieID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String startDate = rs.getString("StartDate");
                    dateComboBox.addItem(startDate);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading dates: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSchedules() {
        schedulePanel.removeAll();
        scheduleMap.clear();

        String selectedMovie = (String) movieComboBox.getSelectedItem();
        String selectedDate = (String) dateComboBox.getSelectedItem();
        if (selectedMovie == null || selectedDate == null) return;

        int movieID = movieMap.get(selectedMovie);

        try (Connection conn = DatabaseConnection.getUserConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT ScheduleID, StartTime FROM ScreeningSchedule WHERE MovieID = ? AND StartDate = ?")) {
            ps.setInt(1, movieID);
            ps.setString(2, selectedDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int scheduleID = rs.getInt("ScheduleID");
                    String startTime = rs.getString("StartTime");
                    scheduleMap.put(startTime, scheduleID);

                    JButton scheduleButton = new JButton(startTime);
                    scheduleButton.addActionListener(e -> loadSeats(scheduleID));
                    schedulePanel.add(scheduleButton);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading schedules: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        schedulePanel.revalidate();
        schedulePanel.repaint();
    }

    private void loadSeats(int scheduleID) {
        seatPanel.removeAll();
        selectedScheduleID = scheduleID;
        selectedSeatID = -1;
        ticketPrice = 0.0;
        priceLabel.setText("가격: 0원");

        try (Connection conn = DatabaseConnection.getUserConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT SeatID, IsAvailable, SeatName FROM Seat WHERE TheaterID = (SELECT TheaterID FROM ScreeningSchedule WHERE ScheduleID = ?)")) {
            ps.setInt(1, scheduleID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int seatID = rs.getInt("SeatID");
                    boolean isAvailable = rs.getBoolean("IsAvailable");
                    String seatName = rs.getString("SeatName");

                    JButton seatButton = new JButton(seatName);
                    seatButton.setEnabled(isAvailable);
                    seatButton.setBackground(isAvailable ? Color.WHITE : Color.GRAY);

                    seatButton.addActionListener(e -> {
                        selectedSeatID = seatID;
                        ticketPrice = 10000; // 예시로 티켓 가격을 10000원으로 설정
                        priceLabel.setText("가격: " + ticketPrice + "원");
                        seatButton.setBackground(Color.BLUE);
                    });

                    seatPanel.add(seatButton);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading seats: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        seatPanel.revalidate();
        seatPanel.repaint();
    }

    private void reserveTicket() {
        if (selectedScheduleID == -1 || selectedSeatID == -1) {
            JOptionPane.showMessageDialog(this, "상영 일정 및 좌석을 선택해주세요.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "영화에 대한 정보, 좌석 정보, 납부할 금액을 확인해주세요.\n\n영화를 예매하시겠습니까?", "예매 확인", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DatabaseConnection.getUserConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Ticket (ScheduleID, SeatID, BookingID, IsIssued, StandardPrice, SalePrice) VALUES (?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, selectedScheduleID);
            ps.setInt(2, selectedSeatID);
            ps.setInt(3, 1); // 예시로 1을 사용
            ps.setBoolean(4, true); // 예매 상태를 true로 설정
            ps.setDouble(5, ticketPrice);
            ps.setDouble(6, ticketPrice);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "예매가 완료되었습니다.", "예매 완료", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

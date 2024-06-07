import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class ModifyReservationPage extends JPanel {
    private static final long serialVersionUID = 1L;
    private JComboBox<String> movieComboBox;
    private JComboBox<String> dateComboBox;
    private JComboBox<String> timeComboBox;
    private JButton[][] seatButtons;
    private JLabel ticketCostLabel;
    private App app;
    private int selectedMovieID;
    private int selectedScheduleID;
    private int selectedSeatID;
    private String selectedSeatName;
    private int ticketCost = 7000;
    private JoinedTicketObj currentTicket;

    public ModifyReservationPage(App app) {
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

        JLabel titleLabel = new JLabel("예매 수정");
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

        timeComboBox = new JComboBox<>();
        verticalLayout.add(timeComboBox);

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

        JButton modifyButton = new JButton("수정하기");
        modifyButton.addActionListener(e -> modifyTicket());
        verticalLayout.add(modifyButton);

        JButton cancelButton = new JButton("취소");
        cancelButton.addActionListener(e -> app.showPage(App.MY_TICKET_PAGE));
        verticalLayout.add(cancelButton);

        movieComboBox.addActionListener(e -> loadDatesForSelectedMovie());
        dateComboBox.addActionListener(e -> loadTimesForSelectedMovie());
        timeComboBox.addActionListener(e -> loadSeatsForSelectedTime());
    }

    public void setTicketDetails(JoinedTicketObj currentTicket) {
        this.currentTicket = currentTicket;
        movieComboBox.setSelectedItem(currentTicket.MovieTitle);
        loadDatesForSelectedMovie();
        dateComboBox.setSelectedItem(currentTicket.ScheduleDate);
        loadTimesForSelectedMovie();
        timeComboBox.setSelectedItem(currentTicket.ScheduleTime);
        loadSeatsForSelectedTime();
        selectSeat((currentTicket.SeatID - 1) / 10, (currentTicket.SeatID - 1) % 10);
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
                timeComboBox.removeAllItems();
                while (rs.next()) {
                    timeComboBox.addItem(rs.getString("StartTime"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "상영 시간을 불러오는 중 오류 발생: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSeatsForSelectedTime() {
        String selectedTime = (String) timeComboBox.getSelectedItem();
        String selectedMovie = (String) movieComboBox.getSelectedItem();
        String selectedDate = (String) dateComboBox.getSelectedItem();

        try (Connection conn = DatabaseConnection.getUserConnection();
             PreparedStatement ps1 = conn.prepareStatement(
                     "SELECT ScheduleID, TheaterID FROM ScreeningSchedule WHERE MovieID = (SELECT MovieID FROM Movie WHERE Title = ?) AND StartTime = ? AND StartDate = ?")) {
            ps1.setString(1, selectedMovie);
            ps1.setString(2, selectedTime);
            ps1.setString(3, selectedDate);

            int theaterID = 0;

            try (ResultSet rs1 = ps1.executeQuery()) {
                if (rs1.next()) {
                    selectedScheduleID = rs1.getInt("ScheduleID");
                    theaterID = rs1.getInt("TheaterID");
                }
            }

            for (int i = 0; i < seatButtons.length; i++) {
                for (int j = 0; j < seatButtons[i].length; j++) {
                    seatButtons[i][j].setText("X");
                    seatButtons[i][j].setEnabled(false);
                    seatButtons[i][j].setBackground(Color.WHITE);
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

                        seatButtons[row][col].setText(seatName);
                        seatButtons[row][col].setEnabled(isAvailable);
                        seatButtons[row][col].setBackground(isAvailable ? Color.WHITE : Color.GRAY);
                    }

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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "좌석 정보를 불러오는 중 오류 발생: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        selectedSeatID = row * 10 + col + 1; // 좌석 ID를 계산
        selectedSeatName = seatButtons[row][col].getText(); // 좌석 이름 저장
        ticketCostLabel.setText(ticketCost + "원");
    }

    private void modifyTicket() {
        String selectedMovie = (String) movieComboBox.getSelectedItem();
        String selectedTime = (String) timeComboBox.getSelectedItem();
        String selectedDate = (String) dateComboBox.getSelectedItem();

        int confirm = JOptionPane.showConfirmDialog(this, "영화: " + selectedMovie + "\n날짜: " + selectedDate + "\n시간: " + selectedTime + "\n좌석: " + selectedSeatName + "\n가격: " + ticketCost + "원\n예매를 수정하시겠습니까?", "예매 수정 확인", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = DatabaseConnection.getUserConnection()) {
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

            try (PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE Ticket SET ScheduleID = ?, TheaterID = ?, SeatID = ?, StandardPrice = ?, SalePrice = ? WHERE TicketID = ?")) {
                ps2.setInt(1, selectedScheduleID);
                ps2.setInt(2, theaterID);
                ps2.setInt(3, selectedSeatID);
                ps2.setInt(4, ticketCost);
                ps2.setInt(5, ticketCost);
                ps2.setInt(6, currentTicket.TicketID);

                ps2.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "예매가 수정되었습니다.", "Success", JOptionPane.INFORMATION_MESSAGE);

            app.showPage(App.MY_TICKET_PAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "예매 수정 중 오류 발생: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
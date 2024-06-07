import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class LoginPage extends JPanel {
    private App app;
    private JComboBox<String> userComboBox;
    private JComboBox<Integer> yearComboBox;
    private JComboBox<Integer> monthComboBox;
    private JComboBox<Integer> dayComboBox;
    private JComboBox<String> timeComboBox;

    public LoginPage(App app) {
        this.app = app;
        setBackground(Color.WHITE); // 배경을 하얀색으로 설정
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // 간격을 좁게 설정

        // 로그인 레이블
        JLabel loginLabel = new JLabel("로그인");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(loginLabel, gbc);

        // 관리자 로그인 버튼
        JButton adminButton = new JButton("관리자 로그인");
        gbc.gridy = 1;
        add(adminButton, gbc);

        // 사용자 로그인
        userComboBox = new JComboBox<>();
        loadUsers();
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(userComboBox, gbc);

        JButton userButton = new JButton("회원 로그인");
        gbc.gridx = 1;
        add(userButton, gbc);

        // 기본 시간 설정 레이블
        JLabel dateTimeLabel = new JLabel("기본 시간 설정");
        dateTimeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        dateTimeLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(dateTimeLabel, gbc);

        // 날짜 및 시간 선택
        yearComboBox = new JComboBox<>(generateYears());
        monthComboBox = new JComboBox<>(generateMonths());
        dayComboBox = new JComboBox<>(generateDays());
        timeComboBox = new JComboBox<>(generateTimes());

        JPanel dateTimePanel = new JPanel(new GridBagLayout());
        dateTimePanel.setBackground(Color.WHITE);
        GridBagConstraints dtGbc = new GridBagConstraints();
        dtGbc.insets = new Insets(2, 2, 2, 2); // 간격을 더 좁게 설정

        dtGbc.gridx = 0;
        dtGbc.gridy = 0;
        dateTimePanel.add(yearComboBox, dtGbc);

        dtGbc.gridx = 1;
        dateTimePanel.add(monthComboBox, dtGbc);

        dtGbc.gridx = 2;
        dateTimePanel.add(dayComboBox, dtGbc);

        dtGbc.gridx = 3;
        dateTimePanel.add(timeComboBox, dtGbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(dateTimePanel, gbc);

        // 기본값 설정
        yearComboBox.setSelectedItem(2023);
        monthComboBox.setSelectedItem(3);
        dayComboBox.setSelectedItem(2);
        timeComboBox.setSelectedItem("13:00");

        userButton.addActionListener(e -> {
            Connection userConnection = DatabaseConnection.getUserConnection();
            if (userConnection != null) {
                app.setConnection(userConnection);
                app.setCurrentDateTime(getSelectedDate(), getSelectedTime());
                app.setCurrentUserId(getSelectedUserId());
                app.showPage(App.SEARCH_PAGE);
            } else {
                JOptionPane.showMessageDialog(this, "사용자 DB 연결에 실패했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        adminButton.addActionListener(e -> {
            Connection adminConnection = DatabaseConnection.getAdminConnection();
            if (adminConnection != null) {
                app.setConnection(adminConnection);
                app.setCurrentDateTime(getSelectedDate(), getSelectedTime());
                app.showPage(App.ADMIN_PAGE);
            } else {
                JOptionPane.showMessageDialog(this, "관리자 DB 연결에 실패했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadUsers() {
        try (Connection conn = DatabaseConnection.getAdminConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT CustomerName FROM Customer")) {
            while (rs.next()) {
                userComboBox.addItem(rs.getString("CustomerName"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "회원 목록을 불러오는 중 오류 발생: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate getSelectedDate() {
        int year = (Integer) yearComboBox.getSelectedItem();
        int month = (Integer) monthComboBox.getSelectedItem();
        int day = (Integer) dayComboBox.getSelectedItem();
        return LocalDate.of(year, month, day);
    }

    private LocalTime getSelectedTime() {
        String selectedTime = (String) timeComboBox.getSelectedItem();
        return LocalTime.parse(selectedTime);
    }

    private Integer[] generateYears() {
        List<Integer> years = new ArrayList<>();
        for (int year = 2020; year <= 2030; year++) {
            years.add(year);
        }
        return years.toArray(new Integer[0]);
    }

    private Integer[] generateMonths() {
        List<Integer> months = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            months.add(month);
        }
        return months.toArray(new Integer[0]);
    }

    private Integer[] generateDays() {
        List<Integer> days = new ArrayList<>();
        for (int day = 1; day <= 31; day++) {
            days.add(day);
        }
        return days.toArray(new Integer[0]);
    }

    private String[] generateTimes() {
        List<String> times = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                times.add(String.format("%02d:%02d", hour, minute));
            }
        }
        return times.toArray(new String[0]);
    }

    private int getSelectedUserId() {
        String selectedUser = (String) userComboBox.getSelectedItem();
        try (Connection conn = DatabaseConnection.getAdminConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT CustomerID FROM Customer WHERE CustomerName = ?")) {
            ps.setString(1, selectedUser);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("CustomerID");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "사용자 ID를 불러오는 중 오류 발생: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return -1; // Default 값
    }
}

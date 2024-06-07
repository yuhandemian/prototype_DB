import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class MovieDetail extends JPanel {
    private JLabel titleLabel, directorLabel, actorsLabel, genreLabel, ratingLabel, durationLabel, releaseDateLabel, synopsisLabel, scoreLabel;
    private JLabel posterLabel, statusLabel;
    private JButton reserveButton;
    private App app;
    private String currentMovieTitle;

    public MovieDetail(App app) {
        this.app = app;
        setLayout(new BorderLayout());

        // 사이드 바 추가
        ComponentSideMenu sideMenu = new ComponentSideMenu(app);
        add(sideMenu, BorderLayout.WEST);

        // 영화 상세 정보 패널
        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        posterLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        detailPanel.add(posterLabel, gbc);

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 24));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        detailPanel.add(titleLabel, gbc);

        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
        statusLabel.setForeground(new Color(0, 102, 255));
        gbc.gridy = 1;
        detailPanel.add(statusLabel, gbc);

        scoreLabel = new JLabel();
        scoreLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
        scoreLabel.setForeground(Color.ORANGE);
        gbc.gridy = 2;
        detailPanel.add(scoreLabel, gbc);

        gbc.gridy = 3;
        detailPanel.add(new JLabel("감독:"), gbc);
        directorLabel = new JLabel();
        gbc.gridx = 2;
        detailPanel.add(directorLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        detailPanel.add(new JLabel("배우:"), gbc);
        actorsLabel = new JLabel();
        gbc.gridx = 2;
        detailPanel.add(actorsLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        detailPanel.add(new JLabel("장르:"), gbc);
        genreLabel = new JLabel();
        gbc.gridx = 2;
        detailPanel.add(genreLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        detailPanel.add(new JLabel("기본 정보:"), gbc);
        ratingLabel = new JLabel();
        durationLabel = new JLabel();
        gbc.gridx = 2;
        detailPanel.add(ratingLabel, gbc);
        gbc.gridy = 7;
        detailPanel.add(durationLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        detailPanel.add(new JLabel("개봉:"), gbc);
        releaseDateLabel = new JLabel();
        gbc.gridx = 2;
        detailPanel.add(releaseDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        detailPanel.add(new JLabel("줄거리:"), gbc);
        synopsisLabel = new JLabel();
        synopsisLabel.setVerticalAlignment(SwingConstants.TOP);
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        detailPanel.add(synopsisLabel, gbc);

        reserveButton = new JButton("예매하기");
        reserveButton.setBackground(new Color(0, 121, 255));
        reserveButton.setForeground(Color.BLACK);
        reserveButton.addActionListener(e -> {
            app.setSelectedMovieTitle(currentMovieTitle);
            app.showPage(App.RESERVATION_PAGE);
        });

        gbc.gridy = 11;
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        detailPanel.add(reserveButton, gbc);

        add(detailPanel, BorderLayout.CENTER);
    }

    public void displayMovieDetails(String movieTitle) {
        this.currentMovieTitle = movieTitle;
        try {
            Connection conn = app.getConnection();
            if (conn != null && !conn.isClosed()) {
                try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Movie LEFT JOIN ScreeningSchedule ON Movie.MovieID = ScreeningSchedule.MovieID WHERE Title = ?")) {
                    ps.setString(1, movieTitle);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            titleLabel.setText(rs.getString("Title"));
                            directorLabel.setText(rs.getString("Director"));
                            actorsLabel.setText(rs.getString("Actors"));
                            genreLabel.setText(rs.getString("Genre"));
                            ratingLabel.setText(rs.getString("Rating"));
                            durationLabel.setText(rs.getInt("Duration") + " 분");
                            releaseDateLabel.setText(rs.getString("ReleaseDate"));
                            synopsisLabel.setText("<html>" + rs.getString("Synopsis") + "</html>");
                            scoreLabel.setText("★ " + rs.getDouble("Score"));

                            LocalDate currentDate = app.getCurrentDate();
                            LocalTime currentTime = app.getCurrentTime();
                            LocalDate movieDate = rs.getDate("StartDate") != null ? rs.getDate("StartDate").toLocalDate() : null;
                            LocalTime movieTime = rs.getTime("StartTime") != null ? rs.getTime("StartTime").toLocalTime() : null;

                            if (movieDate == null || movieTime == null) {
                                statusLabel.setText("정보 없음");
                                reserveButton.setVisible(false);
                            } else if (movieDate.isBefore(currentDate) || (movieDate.isEqual(currentDate) && movieTime.isBefore(currentTime))) {
                                statusLabel.setText("상영 종료");
                                reserveButton.setVisible(false);
                            } else {
                                statusLabel.setText("상영 중");
                                reserveButton.setVisible(true);
                            }

                            String posterURL = rs.getString("PosterURL");
                            if (posterURL != null && !posterURL.isEmpty()) {
                                @SuppressWarnings("deprecation")
                                ImageIcon icon = new ImageIcon(new java.net.URL(posterURL));
                                Image image = icon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
                                posterLabel.setIcon(new ImageIcon(image));
                            } else {
                                posterLabel.setIcon(null);
                            }
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Connection is not valid", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | MalformedURLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.MalformedURLException;

public class MovieDetail extends JPanel {
    private JLabel titleLabel, directorLabel, actorsLabel, genreLabel, ratingLabel, durationLabel, releaseDateLabel, synopsisLabel, scoreLabel;
    private JLabel posterLabel;
    private JButton reserveButton;
    private App app;

    public MovieDetail(App app) {
        this.app = app;
        setLayout(new BorderLayout());

        // 상단에 뒤로가기 버튼 배치
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("뒤로가기");
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        // 뒤로가기 버튼 동작 정의
        backButton.addActionListener(e -> app.showPage(App.SEARCH_PAGE));

        // 영화 상세 정보 패널
        JPanel detailPanel = new JPanel(new GridLayout(10, 2));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel();
        directorLabel = new JLabel();
        actorsLabel = new JLabel();
        genreLabel = new JLabel();
        ratingLabel = new JLabel();
        durationLabel = new JLabel();
        releaseDateLabel = new JLabel();
        synopsisLabel = new JLabel();
        scoreLabel = new JLabel();
        posterLabel = new JLabel();

        detailPanel.add(new JLabel("영화명:"));
        detailPanel.add(titleLabel);
        detailPanel.add(new JLabel("감독명:"));
        detailPanel.add(directorLabel);
        detailPanel.add(new JLabel("배우명:"));
        detailPanel.add(actorsLabel);
        detailPanel.add(new JLabel("장르:"));
        detailPanel.add(genreLabel);
        detailPanel.add(new JLabel("상영 등급:"));
        detailPanel.add(ratingLabel);
        detailPanel.add(new JLabel("상영 시간:"));
        detailPanel.add(durationLabel);
        detailPanel.add(new JLabel("개봉 날짜:"));
        detailPanel.add(releaseDateLabel);
        detailPanel.add(new JLabel("영화 상세 설명:"));
        detailPanel.add(synopsisLabel);
        detailPanel.add(new JLabel("평점:"));
        detailPanel.add(scoreLabel);

        add(detailPanel, BorderLayout.CENTER);
        add(posterLabel, BorderLayout.WEST);

        // 예매하기 버튼
        reserveButton = new JButton("예매하기");
        reserveButton.addActionListener(e -> app.showPage(App.RESERVATION_PAGE));
        add(reserveButton, BorderLayout.SOUTH);
    }

    public void displayMovieDetails(String movieTitle) {
        try (Connection conn = DatabaseConnection.getUserConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Movie WHERE Title = ?")) {
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
                    scoreLabel.setText(String.valueOf(rs.getDouble("Score")));

                    String posterURL = rs.getString("PosterURL");
                    if (posterURL != null && !posterURL.isEmpty()) {
                        posterLabel.setIcon(new ImageIcon(new java.net.URL(posterURL)));
                    } else {
                        posterLabel.setIcon(null);
                    }
                }
            }
        } catch (SQLException | MalformedURLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.MalformedURLException;

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
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        posterLabel = new JLabel();
        posterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
        statusLabel.setForeground(new Color(0, 102, 255));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel = new JLabel();
        scoreLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
        scoreLabel.setForeground(Color.ORANGE);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        directorLabel = new JLabel();
        actorsLabel = new JLabel();
        genreLabel = new JLabel();
        ratingLabel = new JLabel();
        durationLabel = new JLabel();
        releaseDateLabel = new JLabel();
        synopsisLabel = new JLabel();
        synopsisLabel.setVerticalAlignment(SwingConstants.TOP);

        reserveButton = new JButton("예매하기");
        reserveButton.setBackground(new Color(0, 121, 255));
        reserveButton.setForeground(Color.BLACK); // 글자 색을 검은색으로 설정
        reserveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        reserveButton.addActionListener(e -> {
            app.setSelectedMovieTitle(currentMovieTitle);
            app.showPage(App.RESERVATION_PAGE);
        });

        detailPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailPanel.add(posterLabel);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailPanel.add(titleLabel);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailPanel.add(statusLabel);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailPanel.add(scoreLabel);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailPanel.add(new JLabel("감독:"));
        detailPanel.add(directorLabel);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailPanel.add(new JLabel("배우:"));
        detailPanel.add(actorsLabel);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailPanel.add(new JLabel("장르:"));
        detailPanel.add(genreLabel);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailPanel.add(new JLabel("기본 정보:"));
        detailPanel.add(ratingLabel);
        detailPanel.add(durationLabel);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailPanel.add(new JLabel("개봉:"));
        detailPanel.add(releaseDateLabel);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailPanel.add(reserveButton);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailPanel.add(new JLabel("줄거리:"));
        detailPanel.add(synopsisLabel);

        add(detailPanel, BorderLayout.CENTER);
    }

    public void displayMovieDetails(String movieTitle) {
        this.currentMovieTitle = movieTitle;
        try {
            Connection conn = app.getConnection();
            if (conn != null && !conn.isClosed()) {
                try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Movie WHERE Title = ?")) {
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

                            String status = "현재 상영중";
                            statusLabel.setText(status);

                            String posterURL = rs.getString("PosterURL");
                            if (posterURL != null && !posterURL.isEmpty()) {
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

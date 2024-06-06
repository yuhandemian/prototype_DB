import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import javax.swing.*;

public class SearchPage extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextField movieNameTextField;
    private JTextField directorNameTextField;
    private JTextField actorNameTextField;
    private JComboBox<String> genreComboBox;
    private JPanel verticalLayout;

    private final static String DEFAULT_GENRE = "장르 선택";
    private String[] genreName = getGenres();

    public SearchPage(App app) {
        setBackground(new Color(255, 255, 255));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        ComponentSideMenu sideMenuBar = new ComponentSideMenu(app);
        add(sideMenuBar, BorderLayout.WEST);

        JPanel windowLayout = new JPanel();
        windowLayout.setBackground(new Color(255, 255, 255));
        windowLayout.setPreferredSize(new Dimension(650, getHeight()));
        add(windowLayout, BorderLayout.CENTER);
        windowLayout.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(50, 20, 550, 150);
        windowLayout.add(panel);
        panel.setBackground(new Color(255, 255, 255));
        panel.setLayout(null);

        // 영화 이름 입력 상자
        JLabel movieNameLabel = new JLabel("영화명:");
        movieNameLabel.setBounds(10, 10, 80, 25);
        panel.add(movieNameLabel);
        movieNameTextField = new JTextField();
        movieNameTextField.setBounds(100, 10, 200, 25);
        panel.add(movieNameTextField);

        // 감독 이름 입력 상자
        JLabel directorNameLabel = new JLabel("감독명:");
        directorNameLabel.setBounds(10, 40, 80, 25);
        panel.add(directorNameLabel);
        directorNameTextField = new JTextField();
        directorNameTextField.setBounds(100, 40, 200, 25);
        panel.add(directorNameTextField);

        // 배우 이름 입력 상자
        JLabel actorNameLabel = new JLabel("배우명:");
        actorNameLabel.setBounds(10, 70, 80, 25);
        panel.add(actorNameLabel);
        actorNameTextField = new JTextField();
        actorNameTextField.setBounds(100, 70, 200, 25);
        panel.add(actorNameTextField);

        // 장르 선택 상자
        JLabel genreLabel = new JLabel("장르:");
        genreLabel.setBounds(10, 100, 80, 25);
        panel.add(genreLabel);
        genreComboBox = new JComboBox<>(genreName);
        genreComboBox.setBounds(100, 100, 200, 25);
        panel.add(genreComboBox);

        // 검색 버튼
        JButton searchButton = new JButton("검색");
        searchButton.setBounds(310, 10, 80, 25);
        panel.add(searchButton);

        // 취소 버튼
        JButton resetButton = new JButton("취소");
        resetButton.setBounds(310, 40, 80, 25);
        panel.add(resetButton);

        JLabel resultLabel = new JLabel("0개의 영화");
        resultLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        resultLabel.setBounds(50, 180, 100, 30);
        windowLayout.add(resultLabel);

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateMoviePoster(app, resultLabel);
            }
        });

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                movieNameTextField.setText("");
                directorNameTextField.setText("");
                actorNameTextField.setText("");
                genreComboBox.setSelectedItem(DEFAULT_GENRE);

                updateMoviePoster(app, resultLabel);
            }
        });

        // 스크롤 뷰 구현
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(6, 220, 636, 362);
        scrollPane.setBorder(null);
        windowLayout.add(scrollPane);

        verticalLayout = new JPanel();
        scrollPane.setViewportView(verticalLayout);
        verticalLayout.setBackground(new Color(255, 255, 255));
        verticalLayout.setLayout(new GridLayout(0, 3, 10, 10)); // GridLayout으로 변경

        updateMoviePoster(app, resultLabel);
    }

    private void updateMoviePoster(App app, JLabel resultLabel) {
        MovieObj[] movies = getMovieData(
                movieNameTextField.getText(),
                directorNameTextField.getText(),
                actorNameTextField.getText(),
                genreComboBox.getSelectedItem().toString(),
                app.getCurrentDate(),
                app.getCurrentTime()
        );

        verticalLayout.removeAll();

        for (MovieObj movie : movies) {
            ComponentMoviePoster poster = new ComponentMoviePoster(app, movie);
            verticalLayout.add(poster);
        }

        resultLabel.setText(movies.length + "개의 영화");
        verticalLayout.revalidate();
        verticalLayout.repaint();
    }

    private String[] getGenres() {
        ArrayList<String> genres = new ArrayList<>();
        genres.add(DEFAULT_GENRE);

        try (Connection conn = DatabaseConnection.getUserConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT Genre FROM Movie")) {

            while (rs.next()) {
                String genre = rs.getString("Genre");
                genres.add(genre);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return genres.toArray(new String[0]);
    }

    private MovieObj[] getMovieData(String title, String director, String actor, String genre, LocalDate currentDate, LocalTime currentTime) {
        ArrayList<MovieObj> movies = new ArrayList<>();
        String query = getSearchQuery(title, director, actor, genre);

        try (Connection conn = DatabaseConnection.getUserConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                MovieObj movie = new MovieObj(rs);
                movie.setScreeningStatus(currentDate, currentTime); // 현재 날짜와 시간에 기반한 상영 상태 설정
                movies.add(movie);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return movies.toArray(new MovieObj[0]);
    }

    private static String getSearchQuery(String title, String director, String actor, String genre) {
        // 공백 제거 및 null 체크
        title = (title != null && !title.trim().isEmpty()) ? title.trim() : null;
        director = (director != null && !director.trim().isEmpty()) ? director.trim() : null;
        actor = (actor != null && !actor.trim().isEmpty()) ? actor.trim() : null;
        genre = (genre != null && !genre.trim().equals(DEFAULT_GENRE)) ? genre.trim() : null;

        // 기본 SQL 문
        String baseQuery = "SELECT Movie.*, ScreeningSchedule.StartDate, ScreeningSchedule.StartTime FROM Movie " +
                "LEFT JOIN ScreeningSchedule ON Movie.MovieID = ScreeningSchedule.MovieID";

        // 조건 리스트
        ArrayList<String> conditions = new ArrayList<>();

        // 조건 추가
        if (title != null) {
            conditions.add("Title LIKE '%" + title + "%'");
        }
        if (director != null) {
            conditions.add("Director LIKE '%" + director + "%'");
        }
        if (actor != null) {
            conditions.add("Actors LIKE '%" + actor + "%'");
        }
        if (genre != null) {
            conditions.add("Genre = '" + genre + "'");
        }

        // WHERE 절 구성
        if (!conditions.isEmpty()) {
            baseQuery += " WHERE " + String.join(" AND ", conditions);
        }

        return baseQuery;
    }
}

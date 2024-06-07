import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

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
        setLayout(new BorderLayout());

        ComponentSideMenu sideMenuBar = new ComponentSideMenu(app);
        add(sideMenuBar, BorderLayout.WEST);

        JPanel windowLayout = new JPanel();
        windowLayout.setBackground(new Color(255, 255, 255));
        windowLayout.setPreferredSize(new Dimension(650, getHeight()));
        windowLayout.setLayout(new BorderLayout());
        add(windowLayout, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel movieNameLabel = new JLabel("영화명:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(movieNameLabel, gbc);
        movieNameTextField = new JTextField(20);
        gbc.gridx = 1;
        searchPanel.add(movieNameTextField, gbc);

        JLabel directorNameLabel = new JLabel("감독명:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        searchPanel.add(directorNameLabel, gbc);
        directorNameTextField = new JTextField(20);
        gbc.gridx = 1;
        searchPanel.add(directorNameTextField, gbc);

        JLabel actorNameLabel = new JLabel("배우명:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        searchPanel.add(actorNameLabel, gbc);
        actorNameTextField = new JTextField(20);
        gbc.gridx = 1;
        searchPanel.add(actorNameTextField, gbc);

        JLabel genreLabel = new JLabel("장르:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        searchPanel.add(genreLabel, gbc);
        genreComboBox = new JComboBox<>(genreName);
        gbc.gridx = 1;
        searchPanel.add(genreComboBox, gbc);

        JButton searchButton = new JButton("검색");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        searchPanel.add(searchButton, gbc);

        JButton resetButton = new JButton("취소");
        gbc.gridy = 2;
        searchPanel.add(resetButton, gbc);

        windowLayout.add(searchPanel, BorderLayout.NORTH);

        JLabel resultLabel = new JLabel("0개의 영화");
        resultLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        windowLayout.add(resultLabel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(null);
        windowLayout.add(scrollPane, BorderLayout.CENTER);

        verticalLayout = new JPanel();
        scrollPane.setViewportView(verticalLayout);
        verticalLayout.setBackground(new Color(255, 255, 255));
        verticalLayout.setLayout(new GridLayout(0, 3, 10, 10));

        // 검색 버튼 이벤트 리스너 추가
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateMoviePoster(app, resultLabel);
            }
        });

        // 초기화 버튼 이벤트 리스너 추가
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                movieNameTextField.setText("");
                directorNameTextField.setText("");
                actorNameTextField.setText("");
                genreComboBox.setSelectedItem(DEFAULT_GENRE);
                updateMoviePoster(app, resultLabel);
            }
        });

        // 초기 영화 포스터 업데이트
        updateMoviePoster(app, resultLabel);
    }

    private void updateMoviePoster(App app, JLabel resultLabel) {
        MovieObj[] movies = getMovieData(
                movieNameTextField.getText(),
                directorNameTextField.getText(),
                actorNameTextField.getText(),
                genreComboBox.getSelectedItem().toString(),
                app.getCurrentDate(),  // 현재 날짜
                app.getCurrentTime()   // 현재 시간
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

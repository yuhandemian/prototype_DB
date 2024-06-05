import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.sql.*;

public class SearchPage extends JPanel {
    private JTextField titleField, directorField, actorField;
    private JComboBox<String> genreComboBox;
    private JTable resultTable;
    private JLabel resultCountLabel;
    private DefaultTableModel tableModel;
    private App app; // App 객체를 저장할 변수 추가

    public SearchPage(App app) {
        if (app == null) {
            throw new IllegalArgumentException("App 인스턴스가 null입니다.");
        }
        this.app = app; // App 클래스의 인스턴스를 저장

        setLayout(new BorderLayout());

        // 검색 패널 배치
        JPanel searchPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("영화 검색"));

        searchPanel.add(new JLabel("영화명:"));
        titleField = new JTextField();
        searchPanel.add(titleField);

        searchPanel.add(new JLabel("감독명:"));
        directorField = new JTextField();
        searchPanel.add(directorField);

        searchPanel.add(new JLabel("배우명:"));
        actorField = new JTextField();
        searchPanel.add(actorField);

        searchPanel.add(new JLabel("장르:"));
        genreComboBox = new JComboBox<>(new String[]{"", "Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Romance"});
        searchPanel.add(genreComboBox);

        JButton searchButton = new JButton("검색");
        searchButton.addActionListener(e -> searchMovies());
        searchPanel.add(searchButton);

        JButton clearButton = new JButton("취소");
        clearButton.addActionListener(e -> clearSearch());
        searchPanel.add(clearButton);

        add(searchPanel, BorderLayout.NORTH);

        // 검색 결과 테이블 배치
        tableModel = new DefaultTableModel(new Object[]{"영화명", "상영 등급", "상영시간", "상영 중 여부", "포스터", "상영 정보", "예매하기"}, 0);
        resultTable = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable.setRowHeight(100);

        resultTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = resultTable.rowAtPoint(evt.getPoint());
                int col = resultTable.columnAtPoint(evt.getPoint());
                if (col == 0 || col == 4) { // 영화 제목이나 포스터 클릭 시
                    String movieTitle = (String) resultTable.getValueAt(row, 0);
                    app.showMovieDetails(movieTitle);
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // 검색 결과 개수 레이블 배치
        resultCountLabel = new JLabel("검색된 영화 수: 0");
        add(resultCountLabel, BorderLayout.PAGE_END);
    }

    private void searchMovies() {
        String title = titleField.getText();
        String director = directorField.getText();
        String actor = actorField.getText();
        String genre = (String) genreComboBox.getSelectedItem();

        StringBuilder query = new StringBuilder(
                "SELECT M.Title, M.Rating, M.Duration, S.StartDate, S.StartTime, M.PosterURL " +
                "FROM Movie M " +
                "LEFT JOIN ScreeningSchedule S ON M.MovieID = S.MovieID " +
                "WHERE 1=1"
        );
        if (!title.isEmpty()) {
            query.append(" AND M.Title LIKE '%").append(title).append("%'");
        }
        if (!director.isEmpty()) {
            query.append(" AND M.Director LIKE '%").append(director).append("%'");
        }
        if (!actor.isEmpty()) {
            query.append(" AND M.Actors LIKE '%").append(actor).append("%'");
        }
        if (genre != null && !genre.isEmpty()) {
            query.append(" AND M.Genre='").append(genre).append("'");
        }

        try (Connection conn = DatabaseConnection.getUserConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query.toString())) {

            tableModel.setRowCount(0);
            int count = 0;
            while (rs.next()) {
                String movieTitle = rs.getString("Title");
                String rating = rs.getString("Rating");
                int duration = rs.getInt("Duration");
                Date startDate = rs.getDate("StartDate");
                Time startTime = rs.getTime("StartTime");
                String posterURL = rs.getString("PosterURL");

                boolean isShowing = startDate != null && startDate.after(new java.util.Date());

                String screeningInfo = startDate != null ? startDate.toString() + " " + startTime.toString() : "상영 정보 없음";

                JButton bookButton = new JButton("예매하기");
                bookButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        app.showPage(App.RESERVATION_PAGE);
                    }
                });

                JPanel buttonPanel = new JPanel();
                buttonPanel.add(isShowing ? bookButton : new JLabel("상영 종료"));

                tableModel.addRow(new Object[]{
                        movieTitle, rating, duration, isShowing ? "상영 중" : "상영 종료", new JLabel(new ImageIcon(new java.net.URL(posterURL))), screeningInfo, buttonPanel
                });
                count++;
            }
            resultCountLabel.setText("검색된 영화 수: " + count);
        } catch (SQLException | MalformedURLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearSearch() {
        titleField.setText("");
        directorField.setText("");
        actorField.setText("");
        genreComboBox.setSelectedIndex(0);
        searchMovies();
    }
}

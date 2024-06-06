import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.*;

public class SearchPage extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JTextField movieNameTextField;
	private JTextField diractoNameTextField;
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
		panel.setBounds(155, 36, 303, 117);
		windowLayout.add(panel);
		panel.setBackground(new Color(255, 255, 255));
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		// 영화 이름 입력 상자
		JLabel movieNameLabel = new JLabel("영화명 :");
		panel.add(movieNameLabel);
		movieNameLabel.setForeground(Color.BLACK);
		movieNameLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		movieNameLabel.setLabelFor(movieNameTextField);
		
		movieNameTextField = new JTextField();
		movieNameTextField.setPreferredSize(new Dimension(270, 22));
		panel.add(movieNameTextField);
		movieNameTextField.setColumns(20);
		
				
		// 감독 이름 입력 상자
		JLabel diractorNameLabel = new JLabel("감독명 :");
		panel.add(diractorNameLabel);
		diractorNameLabel.setForeground(Color.BLACK);
		diractorNameLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		diractorNameLabel.setLabelFor(diractoNameTextField);
		
		diractoNameTextField = new JTextField();
		diractoNameTextField.setPreferredSize(new Dimension(270, 22));
		panel.add(diractoNameTextField);
		diractoNameTextField.setColumns(20);
		
		
		// 배우 이름 입력 상자
		JLabel actorNameLabel = new JLabel("배우명 :");
		panel.add(actorNameLabel);
		actorNameLabel.setForeground(Color.BLACK);
		actorNameLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		actorNameLabel.setLabelFor(actorNameTextField);
		
		actorNameTextField = new JTextField();
		actorNameTextField.setPreferredSize(new Dimension(270, 22));
		panel.add(actorNameTextField);
		actorNameTextField.setColumns(20);
		
		// 배우 이름 입력 상자
		JLabel genreLabel = new JLabel("   장르 :");
		panel.add(genreLabel);
		genreLabel.setForeground(Color.BLACK);
		genreLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		genreLabel.setLabelFor(genreComboBox);
		
		genreComboBox = new JComboBox<String>(genreName);
		genreComboBox.setPreferredSize(new Dimension(253, 22));
		panel.add(genreComboBox);
		
		JButton searchButton = new JButton("검색");
		searchButton.setBounds(470, 123, 50, 29);
		windowLayout.add(searchButton);
		searchButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		searchButton.setBackground(new Color(0, 121, 255));
		
		JButton resetButton = new JButton("취소");
		resetButton.setBounds(519, 123, 50, 29);
		windowLayout.add(resetButton);
		resetButton.setBackground(Color.WHITE);
		resetButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		
		JLabel lblNewLabel = new JLabel("0개 검색");
		lblNewLabel.setBounds(6, 153, 50, 14);
		windowLayout.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		
		JSeparator separator = new JSeparator();
		separator.setBounds(6, 167, 636, 14);
		windowLayout.add(separator);
		
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateMoviePoster(app);
			}
		});
		
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				movieNameTextField.setText("");
				diractoNameTextField.setText("");
				actorNameTextField.setText("");
				genreComboBox.setSelectedItem(DEFAULT_GENRE);
				
				updateMoviePoster(app);
			}
		});
		
		// 스크롤 뷰 구현
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 179, 636, 403);
		scrollPane.setBorder(null);
		windowLayout.add(scrollPane);
		
		verticalLayout = new JPanel();
		scrollPane.setViewportView(verticalLayout);
		verticalLayout.setBackground(new Color(255, 255, 255));
		verticalLayout.setMaximumSize(new Dimension(636, 987654321));
		verticalLayout.setLayout(new GridLayout(0, 3, 10, 10));
		
		updateMoviePoster(app);
    }
    
    
    private void updateMoviePoster(App app) {
    	MovieObj[] movies = getMovieData(
				    			movieNameTextField.getText(),
								diractoNameTextField.getText(),
								actorNameTextField.getText(),
								genreComboBox.getSelectedItem().toString()
				    		);
    	
    	verticalLayout.removeAll();
    	
    	for (MovieObj movie : movies) {
			ComponentMoviePoster poster = new ComponentMoviePoster(app, movie);
			verticalLayout.add(poster);
		}
    	
    	verticalLayout.revalidate();
    	verticalLayout.repaint();
    }
    
    
    private String[] getGenres() {
    	ArrayList<String> genres = new ArrayList<>();
    	genres.add(DEFAULT_GENRE);
    	
    	try (Connection conn = DatabaseConnection.getUserConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT Genre FROM movie")) {
    		
    		while (rs.next()) {
    			String genre = rs.getString("Genre");
    			genres.add(genre);
    		}
       } catch (SQLException e) {
           JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
       }
    	
    	return genres.toArray(new String[0]);
    }
    
    
    private MovieObj[] getMovieData(String title, String director, String actor, String genre) {
    	ArrayList<MovieObj> movies = new ArrayList<>();
    	String query = getSearchQuery(title, director, actor, genre);
    	
    	try (Connection conn = DatabaseConnection.getUserConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
    		
    		while (rs.next()) {
    			movies.add(new MovieObj(rs));
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
        String baseQuery = "SELECT * FROM movie";
        
        // 조건 리스트
        ArrayList<String> conditions = new ArrayList<>();

        // 조건 추가
        if (title != null) {
            conditions.add("title like '%" + title + "%'");
        }
        if (director != null) {
            conditions.add("director like '%" + director + "%'");
        }
        if (actor != null) {
            conditions.add("actor like '%" + actor + "%'");
        }
        if (genre != null) {
            conditions.add("genre = '" + genre + "'");
        }

        // WHERE 절 구성
        if (!conditions.isEmpty()) {
            baseQuery += " WHERE " + String.join(" AND ", conditions);
        }

        return baseQuery;
    }
}


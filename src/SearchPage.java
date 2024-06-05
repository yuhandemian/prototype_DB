import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class SearchPage extends JPanel {
	
	private JTextField movieNameTextField;
	private JTextField diractoNameTextField;
	private JTextField actorNameTextField;
	private JComboBox genreComboBox;
	
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
		
		genreComboBox = new JComboBox();
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 179, 636, 403);
		windowLayout.add(scrollPane);
		
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(6, 153, 50, 14);
		windowLayout.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		
		JSeparator separator = new JSeparator();
		separator.setBounds(6, 167, 636, 14);
		windowLayout.add(separator);
		
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				movieNameTextField.setText("");
				diractoNameTextField.setText("");
				actorNameTextField.setText("");
			}
		});
		
		for (int i = 0; i < 10; i++) {
			ComponentMoviePoster poster1 = new ComponentMoviePoster();
			scrollPane.add(poster1);
		}
    }
}

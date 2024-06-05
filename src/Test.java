import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JSeparator;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import javax.swing.Box;
import java.awt.Button;
import javax.swing.JScrollBar;
import java.awt.ScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JCheckBox;

public class Test extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField movieNameTextField;
	private JTextField diractoNameTextField;
	private JTextField actorNameTextField;
	private JComboBox genreComboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Test frame = new Test();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Test() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 620);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		JPanel menuLayout = new JPanel();
		menuLayout.setBackground(new Color(255, 255, 255));
		menuLayout.setPreferredSize(new Dimension(200, this.getHeight()));
		contentPane.add(menuLayout);
		
		JPanel windowLayout = new JPanel();
		windowLayout.setBackground(new Color(255, 255, 255));
		windowLayout.setPreferredSize(new Dimension(650, getHeight()));
		contentPane.add(windowLayout, BorderLayout.CENTER);
		windowLayout.setLayout(null);
		
		// 페이지, 헤더 구성.
		JLabel lblNewLabel = new JLabel("예매된 티켓");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 24));
		lblNewLabel.setBounds(23, 25, 149, 30);
		windowLayout.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("선택된 예매 삭제");
		btnNewButton.setForeground(new Color(255, 0, 0));
		btnNewButton.setBounds(505, 25, 117, 29);
		windowLayout.add(btnNewButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setLocation(23, 67);
		scrollPane.setSize(599, 496);
		windowLayout.add(scrollPane);
		
		JPanel verticalLayout = new JPanel();
		scrollPane.setViewportView(verticalLayout);
		scrollPane.setBackground(new Color(255, 255, 255));
		verticalLayout.setLayout(null);
		
		
		// 카드 레이아웃.
		JPanel cardLayout = new JPanel();
		cardLayout.setBackground(new Color(242, 242, 242));
		cardLayout.setBounds(6, 5, 583, 148);
		verticalLayout.add(cardLayout);
		cardLayout.setLayout(null);
		
		JButton btnNewButton_1 = new JButton("영화 변경");
		btnNewButton_1.setBounds(334, 113, 80, 29);
		cardLayout.add(btnNewButton_1);
		
		JButton btnNewButton_1_2 = new JButton("시간 변경");
		btnNewButton_1_2.setBounds(412, 113, 80, 29);
		cardLayout.add(btnNewButton_1_2);
		
		JButton btnNewButton_1_1 = new JButton("영화 삭제");
		btnNewButton_1_1.setForeground(new Color(255, 0, 0));
		btnNewButton_1_1.setBounds(486, 113, 80, 29);
		cardLayout.add(btnNewButton_1_1);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("");
		chckbxNewCheckBox.setBounds(538, 10, 28, 23);
		cardLayout.add(chckbxNewCheckBox);
		
		JLabel ticketTitleLabel = new JLabel("Ticket Title Label");
		ticketTitleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		ticketTitleLabel.setBounds(17, 10, 482, 16);
		cardLayout.add(ticketTitleLabel);
		
		JLabel locationLabel = new JLabel("New label");
		locationLabel.setForeground(new Color(127, 127, 127));
		locationLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		locationLabel.setBounds(17, 36, 482, 16);
		cardLayout.add(locationLabel);
		
		JLabel costLabel = new JLabel("7000원");
		costLabel.setForeground(new Color(127, 127, 127));
		costLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		costLabel.setBounds(17, 64, 482, 16);
		cardLayout.add(costLabel);
		
		
	}
}

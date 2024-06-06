import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import javax.swing.ScrollPaneConstants;
import javax.swing.JComboBox;
import java.awt.GridLayout;
import java.awt.FlowLayout;

public class Test extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

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
		JLabel titleLabel = new JLabel("영화 예매");
		titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 24));
		titleLabel.setBounds(23, 25, 149, 30);
		windowLayout.add(titleLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setLocation(23, 67);
		scrollPane.setSize(599, 496);
		windowLayout.add(scrollPane);
		
		JPanel verticalLayout = new JPanel();
		verticalLayout.setBackground(new Color(255, 255, 255));
		scrollPane.setViewportView(verticalLayout);
		verticalLayout.setLayout(new BoxLayout(verticalLayout, BoxLayout.Y_AXIS));
		
		// 스크롤 내부 구조
		JLabel subTitle1 = new JLabel("영화 선택");
		subTitle1.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		verticalLayout.add(subTitle1);
		
		JComboBox comboBox = new JComboBox();
		verticalLayout.add(comboBox);
		
		JLabel subTitle2 = new JLabel("시간 선택");
		subTitle2.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		verticalLayout.add(subTitle2);
		scrollPane.setBackground(new Color(255, 255, 255));
		
		JScrollPane timeLayoutScroll = new JScrollPane();
		verticalLayout.add(timeLayoutScroll);
		
		JPanel timeLayout = new JPanel();
		timeLayout.setBackground(new Color(255, 255, 255));
		timeLayoutScroll.setViewportView(timeLayout);
		timeLayout.setLayout(new BoxLayout(timeLayout, BoxLayout.X_AXIS));
		
		// 시간관련 버튼 추가.
		for (int i = 0; i < 10; i++) {
			timeLayout.add(new JButton("시간 추가"));
		}
		
		JLabel subTitle3 = new JLabel("좌석 선택");
		subTitle3.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		verticalLayout.add(subTitle3);
		
		final int SEATS_ROW = 8;
		final int SEATS_COL = 10;
		
		JPanel seatsLayout = new JPanel();
		seatsLayout.setBackground(new Color(255, 255, 255));
		verticalLayout.add(seatsLayout);
		seatsLayout.setLayout(new GridLayout(SEATS_ROW, SEATS_COL, 10, 10));
		
		for (int i = 0; i < SEATS_ROW * SEATS_COL; i++) {
			JButton btnNewButton = new JButton("X");
			btnNewButton.setPreferredSize(new Dimension(10, 10)); 
			seatsLayout.add(btnNewButton);
		}
		
		JPanel costLayout = new JPanel();
		costLayout.setBackground(new Color(255, 255, 255));
		verticalLayout.add(costLayout);
		costLayout.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		
		JLabel subsubTitle = new JLabel("티켓 가격");
		subsubTitle.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		costLayout.add(subsubTitle);
		
		JLabel ticketCostLabel = new JLabel("7000원");
		ticketCostLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		costLayout.add(ticketCostLabel);
	}
}

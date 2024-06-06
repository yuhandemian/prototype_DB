import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;

public class ComponentMoviePoster extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * 영화 search 페이지의 영화 포스터 컴포넌트
	 */
	public ComponentMoviePoster(App app) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBounds(6, 5, 165, 249);
		setBackground(new Color(255, 0, 0));
		JLabel posterLabel = new JLabel("");
		this.add(posterLabel);
		
		JLabel titleLabel = new JLabel("New label");
		titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		this.add(titleLabel);
		
		JLabel descriptionLabel = new JLabel("New label");
		descriptionLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		descriptionLabel.setForeground(new Color(127, 127, 127));
		this.add(descriptionLabel);
		
		JLabel disabledLabel = new JLabel("상영 종료");
		disabledLabel.setForeground(new Color(255, 0, 0));
		this.add(disabledLabel);
		
		JButton reservationButton = new JButton("영화 예매");
		add(reservationButton);
		
		reservationButton.addActionListener(e -> app.showPage(App.RESERVATION_PAGE));
	}

}

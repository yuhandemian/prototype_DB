

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ComponentMoviePoster extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public ComponentMoviePoster() {	
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
	}

}

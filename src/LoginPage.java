import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JPanel {
    public LoginPage(App app) {
        setLayout(null);

        JButton userButton = new JButton("회원");
        JButton adminButton = new JButton("관리자");

        userButton.setBounds(10, 10, 120, 140);
        adminButton.setBounds(150, 10, 120, 140);

        add(userButton);
        add(adminButton);

        userButton.addActionListener(e -> app.showPage(App.SEARCH_PAGE));
        adminButton.addActionListener(e -> app.showPage(App.ADMIN_PAGE));
    }
}

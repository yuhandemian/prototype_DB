import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.Connection;

public class LoginPage extends JPanel {
    public LoginPage(App app) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 사용자 로그인 버튼
        JButton userLoginButton = new JButton("User Login");
        userLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Connection conn = DatabaseConnection.getUserConnection();
                if (conn != null) {
                    JOptionPane.showMessageDialog(null, "User Login Successful");
                    app.showPage(App.SEARCH_PAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "User Login Failed");
                }
            }
        });

        // 관리자 로그인 버튼
        JButton adminLoginButton = new JButton("Admin Login");
        adminLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Connection conn = DatabaseConnection.getAdminConnection();
                if (conn != null) {
                    JOptionPane.showMessageDialog(null, "Admin Login Successful");
                    app.showPage(App.ADMIN_PAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Admin Login Failed");
                }
            }
        });

        // 사용자 로그인 버튼 위치 설정
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userLoginButton, gbc);

        // 관리자 로그인 버튼 위치 설정
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(adminLoginButton, gbc);
    }
}

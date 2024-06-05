import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class LoginPage extends JPanel {
    private App app;

    public LoginPage(App app) {
        this.app = app;
        setLayout(new BorderLayout()); // Layout 설정 변경

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2)); // 버튼 패널 추가 및 Layout 변경

        JButton userButton = new JButton("회원");
        JButton adminButton = new JButton("관리자");

        // 버튼 패널에 버튼 추가
        buttonPanel.add(userButton);
        buttonPanel.add(adminButton);

        // 중앙에 버튼 패널 추가
        add(buttonPanel, BorderLayout.CENTER);

        // 회원 버튼에 대한 액션 리스너
        userButton.addActionListener(e -> {
            Connection userConnection = DatabaseConnection.getUserConnection();
            if (userConnection != null) {
                app.setConnection(userConnection);
                app.showPage(App.SEARCH_PAGE);
            } else {
                JOptionPane.showMessageDialog(this, "사용자 DB 연결에 실패했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 관리자 버튼에 대한 액션 리스너
        adminButton.addActionListener(e -> {
            Connection adminConnection = DatabaseConnection.getAdminConnection();
            if (adminConnection != null) {
                app.setConnection(adminConnection);
                app.showPage(App.ADMIN_PAGE);
            } else {
                JOptionPane.showMessageDialog(this, "관리자 DB 연결에 실패했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

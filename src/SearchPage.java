import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchPage extends JPanel {
    public SearchPage(App app) {
        setLayout(null);

        JButton backButton = new JButton("로그아웃");
        backButton.setBounds(10, 10, 100, 30);
        add(backButton);

        backButton.addActionListener(e -> app.showPage(App.LOGIN_PAGE));
        // 여기에 추가적인 검색 기능 버튼 및 이벤트 리스너를 추가
    }
}

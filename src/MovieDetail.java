import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MovieDetail extends JPanel {
    public MovieDetail(App app) {
        setLayout(null);

        JButton backButton = new JButton("뒤로가기");
        backButton.setBounds(10, 10, 100, 30);
        add(backButton);

        backButton.addActionListener(e -> app.showPage(App.SEARCH_PAGE));
        // 여기에 영화 상세 정보 표시 및 이벤트 리스너를 추가
    }
}

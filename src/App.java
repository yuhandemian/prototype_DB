import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    public static final String LOGIN_PAGE = "LoginPage";
    public static final String ADMIN_PAGE = "AdminPage";
    public static final String MOVIE_DETAIL = "MovieDetail";
    public static final String RESERVATION_DETAIL = "ReservationDetail";
    public static final String SEARCH_PAGE = "SearchPage";
    public static final String RESERVATION_PAGE = "ReservationPage";
    public static final String MODIFY_RESERVATION_PAGE = "ModifyReservationPage";
    public static final String MY_TICKET_PAGE = "MyTicketPage";

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public App() {
        setTitle("Movie Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new LoginPage(this), LOGIN_PAGE);
        mainPanel.add(new AdminPage(this), ADMIN_PAGE);
        mainPanel.add(new MovieDetail(this), MOVIE_DETAIL);
        mainPanel.add(new ReservationDetail(this), RESERVATION_DETAIL);
        mainPanel.add(new SearchPage(this), SEARCH_PAGE);
        mainPanel.add(new ReservationPage(this), RESERVATION_PAGE);
        mainPanel.add(new ModifyReservationPage(this), MODIFY_RESERVATION_PAGE);
        mainPanel.add(new MyTicketPage(this), MY_TICKET_PAGE);

        add(mainPanel);
        cardLayout.show(mainPanel, LOGIN_PAGE); // 시작 페이지는 로그인 페이지로 설정

        setVisible(true);
    }

    public void showPage(String page) {
        cardLayout.show(mainPanel, page);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new App();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

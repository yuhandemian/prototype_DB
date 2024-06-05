import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class App extends JFrame {
    public static final String LOGIN_PAGE = "LoginPage";
    public static final String SEARCH_PAGE = "SearchPage";
    public static final String ADMIN_PAGE = "AdminPage";
    public static final String RESERVATION_PAGE = "ReservationPage";
    public static final String MOVIE_DETAIL_PAGE = "MovieDetailPage";
    public static final String MY_TICKET_PAGE = "MyTicketPage";
    public static final String RESERVATION_DETAIL_PAGE = "ReservationDetailPage";

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Connection connection;
    private MovieDetail movieDetailPage;
    private MyTicketPage myTicketPage;
    private ReservationDetail reservationDetailPage;
    private JPanel navigationPanel;

    public App() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        add(cardPanel, BorderLayout.CENTER);

        // 초기 페이지들 추가
        cardPanel.add(new LoginPage(this), LOGIN_PAGE); // LoginPage 추가
        cardPanel.add(new SearchPage(this), SEARCH_PAGE);
        cardPanel.add(new AdminPage(this), ADMIN_PAGE);
        cardPanel.add(new ReservationPage(this), RESERVATION_PAGE);

        movieDetailPage = new MovieDetail(this);
        myTicketPage = new MyTicketPage(this);
        reservationDetailPage = new ReservationDetail(this);

        cardPanel.add(movieDetailPage, MOVIE_DETAIL_PAGE);
        cardPanel.add(myTicketPage, MY_TICKET_PAGE);
        cardPanel.add(reservationDetailPage, RESERVATION_DETAIL_PAGE);

        // 네비게이션 패널 추가
        navigationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(navigationPanel, BorderLayout.NORTH);

        JButton searchButton = new JButton("영화 검색");
        JButton myTicketsButton = new JButton("나의 예매");
        JButton logoutButton = new JButton("로그아웃");

        searchButton.addActionListener(e -> showPage(SEARCH_PAGE));
        myTicketsButton.addActionListener(e -> showPage(MY_TICKET_PAGE));
        logoutButton.addActionListener(e -> showPage(LOGIN_PAGE));

        navigationPanel.add(searchButton);
        navigationPanel.add(myTicketsButton);
        navigationPanel.add(logoutButton);

        // 기본 크기 및 종료 설정
        setTitle("Movie Reservation System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showPage(LOGIN_PAGE);
    }

    public void showPage(String page) {
        if (page.equals(LOGIN_PAGE) || page.equals(ADMIN_PAGE)) {
            navigationPanel.setVisible(false);
        } else {
            navigationPanel.setVisible(true);
        }
        cardLayout.show(cardPanel, page);
    }

    public void setConnection(Connection connection) {
        // LoginPage에서는 Connection 설정하지 않음
        if (!(cardPanel.getComponent(cardPanel.getComponentCount() - 1) instanceof LoginPage)) {
            this.connection = connection;
            System.out.println("Connection 설정 완료");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void showMovieDetails(String movieTitle) {
        movieDetailPage.displayMovieDetails(movieTitle);
        showPage(MOVIE_DETAIL_PAGE);
    }

    public void showReservationDetail(int ticketID) {
        reservationDetailPage.displayTicketDetails(ticketID);
        showPage(RESERVATION_DETAIL_PAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.setVisible(true);
        });
    }
}

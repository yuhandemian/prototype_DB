import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;

public class App extends JFrame {
    public static final String LOGIN_PAGE = "LoginPage";
    public static final String SEARCH_PAGE = "SearchPage";
    public static final String ADMIN_PAGE = "AdminPage";
    public static final String RESERVATION_PAGE = "ReservationPage";
    public static final String MOVIE_DETAIL_PAGE = "MovieDetailPage";
    public static final String MY_TICKET_PAGE = "MyTicketPage";
    public static final String RESERVATION_DETAIL_PAGE = "ReservationDetailPage";
    public static final String MODIFY_RESERVATION_PAGE = "ModifyReservationPage";

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Connection connection;
    private JPanel loginPage;
    private JPanel searchPage;
    private JPanel adminPage;
    private ReservationPage reservationPage;
    private MovieDetail movieDetailPage;
    private MyTicketPage myTicketPage;
    private ReservationDetail reservationDetailPage;
    private ModifyReservationPage modifyReservationPage;
    private LocalDate currentDate;
    private LocalTime currentTime;
    private String selectedMovieTitle;
    private JoinedTicketObj currentTicket;
    private int currentUserId;

    public App() {
        setTitle("Movie Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        add(mainPanel, BorderLayout.CENTER);

        // 기본 크기 및 종료 설정
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showPage(String page) {
        switch (page) {
            case LOGIN_PAGE:
                if (loginPage == null) loginPage = new LoginPage(this);
                mainPanel.add(loginPage, LOGIN_PAGE);
                break;
            case SEARCH_PAGE:
                if (searchPage == null) searchPage = new SearchPage(this);
                mainPanel.add(searchPage, SEARCH_PAGE);
                break;
            case ADMIN_PAGE:
                if (adminPage == null) adminPage = new AdminPage(this);
                mainPanel.add(adminPage, ADMIN_PAGE);
                break;
            case RESERVATION_PAGE:
                if (reservationPage == null) reservationPage = new ReservationPage(this);
                reservationPage.setSelectedMovie(selectedMovieTitle); // 선택된 영화 제목 설정
                mainPanel.add(reservationPage, RESERVATION_PAGE);
                break;
            case MOVIE_DETAIL_PAGE:
                if (movieDetailPage == null) movieDetailPage = new MovieDetail(this);
                mainPanel.add(movieDetailPage, MOVIE_DETAIL_PAGE);
                break;
            case MY_TICKET_PAGE:
                if (myTicketPage == null) myTicketPage = new MyTicketPage(this);
                else myTicketPage.updateTicketCards(this); // 예매 확인 페이지 갱신
                mainPanel.add(myTicketPage, MY_TICKET_PAGE);
                break;
            case RESERVATION_DETAIL_PAGE:
                if (reservationDetailPage == null) reservationDetailPage = new ReservationDetail(this);
                mainPanel.add(reservationDetailPage, RESERVATION_DETAIL_PAGE);
                break;
            case MODIFY_RESERVATION_PAGE:
                if (modifyReservationPage == null) modifyReservationPage = new ModifyReservationPage(this);
                modifyReservationPage.setTicketDetails(currentTicket); // Set the ticket details
                mainPanel.add(modifyReservationPage, MODIFY_RESERVATION_PAGE);
                break;
        }
        cardLayout.show(mainPanel, page);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
        System.out.println("Connection 설정 완료");
    }

    public Connection getConnection() {
        return connection;
    }

    public void showMovieDetails(String movieTitle) {
        if (movieDetailPage == null) movieDetailPage = new MovieDetail(this);
        movieDetailPage.displayMovieDetails(movieTitle);
        showPage(MOVIE_DETAIL_PAGE);
    }

    public void showReservationDetail(int ticketID) {
        if (reservationDetailPage == null) reservationDetailPage = new ReservationDetail(this);
        reservationDetailPage.displayTicketDetails(ticketID);
        showPage(RESERVATION_DETAIL_PAGE);
    }

    public void setCurrentDateTime(LocalDate date, LocalTime time) {
        this.currentDate = date;
        this.currentTime = time;
        System.out.println("Current Date and Time 설정 완료: " + date + " " + time);
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public LocalTime getCurrentTime() {
        return currentTime;
    }

    public void setSelectedMovieTitle(String movieTitle) {
        this.selectedMovieTitle = movieTitle;
    }

    public void setCurrentTicket(JoinedTicketObj ticket) {
        this.currentTicket = ticket;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            Connection conn = DatabaseConnection.getUserConnection();
            app.setConnection(conn);
            app.showPage(LOGIN_PAGE); // 초기 페이지 설정
        });
    }
}

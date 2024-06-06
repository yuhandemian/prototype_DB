import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyTicketPage extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JPanel verticalLayout;
    private ArrayList<ComponentTicketCard> ticketCards;

    public MyTicketPage(App app) {
        setBackground(new Color(255, 255, 255));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        ComponentSideMenu sideMenuBar = new ComponentSideMenu(app);
        add(sideMenuBar, BorderLayout.WEST);
        
        JPanel windowLayout = new JPanel();
        windowLayout.setBackground(new Color(255, 255, 255));
        windowLayout.setPreferredSize(new Dimension(650, getHeight()));
        add(windowLayout, BorderLayout.CENTER);
        windowLayout.setLayout(null);
        
        // 페이지, 헤더 구성.
        JLabel titleLabel = new JLabel("예매된 티켓");
        titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 24));
        titleLabel.setBounds(23, 25, 149, 30);
        windowLayout.add(titleLabel);

        JButton deleteSelectedButton = new JButton("선택된 예매 취소");
        deleteSelectedButton.setBounds(500, 25, 150, 30);
        deleteSelectedButton.setForeground(Color.RED);
        windowLayout.add(deleteSelectedButton);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setLocation(23, 67);
        scrollPane.setSize(599, 496);
        scrollPane.setBorder(null);
        windowLayout.add(scrollPane);
        
        verticalLayout = new JPanel();
        scrollPane.setViewportView(verticalLayout);
        verticalLayout.setBorder(null);
        verticalLayout.setBackground(new Color(255, 255, 255));
        verticalLayout.setLayout(new GridLayout(0, 1, 10, 10));
        
        deleteSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<ComponentTicketCard> selectedCards = new ArrayList<>();
                for (ComponentTicketCard ticketCard : ticketCards) {
                    if (ticketCard.isSelected()) {
                        selectedCards.add(ticketCard);
                    }
                }

                if (!selectedCards.isEmpty()) {
                    int confirm = JOptionPane.showConfirmDialog(MyTicketPage.this, "정말로 선택된 예매를 삭제하시겠습니까?", "예매 삭제", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        for (ComponentTicketCard ticketCard : selectedCards) {
                            ticketCard.removeReservation(ticketCard.getTicketID());
                        }
                        JOptionPane.showMessageDialog(MyTicketPage.this, "선택된 예매가 삭제되었습니다.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        app.showPage(App.MY_TICKET_PAGE); // 삭제 후 페이지 갱신
                    }
                } else {
                    JOptionPane.showMessageDialog(MyTicketPage.this, "선택된 예매가 없습니다.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // 카드 레이아웃
        updateTicketCards(app);
    }
    
    public void updateTicketCards(App app) {
        JoinedTicketObj[] tickets = getTicketData(app.getCurrentUserId());
        verticalLayout.removeAll();
        ticketCards = new ArrayList<>();

        LocalDate currentDate = app.getCurrentDate();
        LocalTime currentTime = app.getCurrentTime();

        for (JoinedTicketObj ticket : tickets) {
            ComponentTicketCard ticketCard = new ComponentTicketCard(app, ticket);
            if (isTicketExpired(ticket, currentDate, currentTime)) {
                ticketCard.setExpired();
            }
            verticalLayout.add(ticketCard);
            ticketCards.add(ticketCard);
        }

        verticalLayout.revalidate();
        verticalLayout.repaint();
    }

    private JoinedTicketObj[] getTicketData(int userId) {
        ArrayList<JoinedTicketObj> tickets = new ArrayList<>();
        String query = "SELECT * FROM Booking, Ticket, Theater, Seat, ScreeningSchedule, Movie "
                + "WHERE Booking.CustomerID = " + userId + " "
                + "AND Ticket.BookingID = Booking.BookingID "
                + "AND Ticket.TheaterID = Theater.TheaterID "
                + "AND Ticket.SeatID = Seat.SeatID AND Ticket.TheaterID = Seat.TheaterID "
                + "AND Ticket.ScheduleID = ScreeningSchedule.ScheduleID "
                + "AND ScreeningSchedule.MovieID = Movie.MovieID "
                + "ORDER BY Booking.BookingID DESC";

        try (Connection conn = DatabaseConnection.getUserConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                tickets.add(new JoinedTicketObj(rs));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return tickets.toArray(new JoinedTicketObj[0]);
    }

    private boolean isTicketExpired(JoinedTicketObj ticket, LocalDate currentDate, LocalTime currentTime) {
        LocalDate ticketDate = LocalDate.parse(ticket.ScheduleDate);
        LocalTime ticketTime = LocalTime.parse(ticket.ScheduleTime);
        return ticketDate.isBefore(currentDate) || (ticketDate.isEqual(currentDate) && ticketTime.isBefore(currentTime));
    }
}

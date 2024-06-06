import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;

public class ComponentTicketCard extends JPanel {

    private static final long serialVersionUID = 1L;
    private int ticketID;
    private JCheckBox checkBox;
    private App app;
    private JLabel expiredLabel;

    public ComponentTicketCard(App app, JoinedTicketObj ticket) {
        this.app = app;
        this.ticketID = ticket.TicketID;
        setBackground(new Color(242, 242, 242));
        setBounds(6, 5, 583, 160);
        setPreferredSize(new Dimension(580, 160));
        setBackground(new Color(255, 255, 255));
        setLayout(null);

        JButton btnChangeMovie = new JButton("영화 변경");
        btnChangeMovie.setBounds(334, 113, 80, 29);
        add(btnChangeMovie);

        JButton btnChangeTime = new JButton("시간 변경");
        btnChangeTime.setBounds(412, 113, 80, 29);
        add(btnChangeTime);

        JButton btnRemoveReservation = new JButton("예매 취소");
        btnRemoveReservation.setBounds(486, 113, 80, 29);
        btnRemoveReservation.setForeground(new Color(255, 0, 0));
        add(btnRemoveReservation);

        checkBox = new JCheckBox("");
        checkBox.setBounds(538, 10, 28, 23);
        add(checkBox);

        JLabel ticketTitleLabel = new JLabel(ticket.MovieTitle);
        ticketTitleLabel.setBounds(17, 10, 482, 16);
        ticketTitleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
        add(ticketTitleLabel);

        JLabel locationLabel = new JLabel(ticket.ScheduleDate + "  " + ticket.ScheduleTime + "  " + ticket.TheaterName + "  " + ticket.SeatName);
        locationLabel.setBounds(17, 36, 482, 16);
        locationLabel.setForeground(new Color(127, 127, 127));
        locationLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        add(locationLabel);

        JLabel costLabel = new JLabel(ticket.SalePrice + "원");
        costLabel.setBounds(17, 64, 482, 16);
        costLabel.setForeground(new Color(127, 127, 127));
        costLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        add(costLabel);

        expiredLabel = new JLabel("기간 만료", SwingConstants.CENTER);
        expiredLabel.setBounds(0, 0, getWidth(), getHeight());
        expiredLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
        expiredLabel.setForeground(Color.RED);
        expiredLabel.setVisible(false);
        expiredLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(expiredLabel);

        btnChangeMovie.addActionListener(e -> {
            app.setCurrentTicket(ticket);
            app.showPage(App.MODIFY_RESERVATION_PAGE);
        });

        btnChangeTime.addActionListener(e -> {
            app.setCurrentTicket(ticket);
            app.showPage(App.MODIFY_RESERVATION_PAGE);
        });

        btnRemoveReservation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(ComponentTicketCard.this, "정말로 이 예매를 삭제하시겠습니까?", "예매 삭제", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    removeReservation(ticketID);
                    app.showPage(App.MY_TICKET_PAGE); // 삭제 후 페이지 갱신
                }
            }
        });

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                app.showReservationDetail(ticketID);
            }
        });
    }

    public int getTicketID() {
        return ticketID;
    }

    public boolean isSelected() {
        return !isExpired() && checkBox.isSelected();
    }

    public void removeReservation(int ticketID) {
        try (Connection conn = DatabaseConnection.getUserConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Ticket WHERE TicketID = ?")) {
            ps.setInt(1, ticketID);
            ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "예매 삭제 중 오류 발생: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setExpired() {
        setBackground(Color.LIGHT_GRAY);
        expiredLabel.setVisible(true);
        checkBox.setEnabled(false);
        for (Component component : getComponents()) {
            if (!(component instanceof JLabel) && !(component instanceof JCheckBox)) {
                component.setEnabled(false);
            }
        }
    }

    public boolean isExpired() {
        return expiredLabel.isVisible();
    }
}

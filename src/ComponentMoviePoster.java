import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ComponentMoviePoster extends JPanel {
    private static final long serialVersionUID = 1L;

    private MovieObj movie;
    private App app;

    public ComponentMoviePoster(App app, MovieObj movie) {
        this.movie = movie;
        this.app = app;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(150, 250));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel posterLabel = new JLabel();
        posterLabel.setIcon(new ImageIcon(new ImageIcon(movie.PosterURL).getImage().getScaledInstance(150, 225, Image.SCALE_SMOOTH)));
        add(posterLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        add(infoPanel, BorderLayout.SOUTH);

        JLabel titleLabel = new JLabel(movie.Title);
        titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        infoPanel.add(titleLabel);

        JLabel ratingLabel = new JLabel(movie.Rating + " 이용가, " + movie.Duration + "분");
        infoPanel.add(ratingLabel);

        if (movie.ScreeningStatus.equals("상영 중")) {
            JButton bookButton = new JButton("예매하기");
            bookButton.setBackground(Color.BLUE);
            bookButton.setForeground(Color.BLACK);
            bookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            bookButton.addActionListener(e -> {
                app.setSelectedMovieTitle(movie.Title);
                app.showPage(App.RESERVATION_PAGE);
            });
            infoPanel.add(bookButton);
        } else {
            JLabel statusLabel = new JLabel(movie.ScreeningStatus);
            statusLabel.setForeground(Color.RED);
            statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            infoPanel.add(statusLabel);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                app.showMovieDetails(movie.Title);
            }
        });
    }
}

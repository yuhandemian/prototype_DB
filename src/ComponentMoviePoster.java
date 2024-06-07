import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class ComponentMoviePoster extends JPanel {
    private static final long serialVersionUID = 1L;
    private App app;
    private MovieObj movie;
    private JLabel posterLabel;
    private JLabel titleLabel;
    private JButton bookButton;

    public ComponentMoviePoster(App app, MovieObj movie) {
        this.app = app;
        this.movie = movie;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setPreferredSize(new Dimension(200, 300));

        posterLabel = new JLabel("Loading...", SwingConstants.CENTER);
        add(posterLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        add(infoPanel, BorderLayout.SOUTH);

        titleLabel = new JLabel(movie.Title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        infoPanel.add(titleLabel);

        JLabel ratingLabel = new JLabel(movie.Rating + " 이용가, " + movie.Duration + "분", SwingConstants.CENTER);
        infoPanel.add(ratingLabel);

        if (movie.ScreeningStatus.equals("상영 중")) {
            bookButton = new JButton("예매하기");
            bookButton.setBackground(Color.BLUE);
            bookButton.setForeground(Color.BLACK); // 글씨 색을 검은색으로 설정
            bookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            bookButton.addActionListener(e -> {
                app.setSelectedMovieTitle(movie.Title);
                app.showPage(App.RESERVATION_PAGE);
            });
            infoPanel.add(bookButton);
        } else {
            JLabel statusLabel = new JLabel("상영종료", SwingConstants.CENTER);
            statusLabel.setForeground(Color.RED);
            statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            infoPanel.add(statusLabel);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() != bookButton) {
                    app.showMovieDetails(movie.Title);
                }
            }
        });

        loadImageAsync(movie.PosterURL);
    }

    private void loadImageAsync(String imageUrl) {
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                try {
                    URL url = new URL(imageUrl);
                    ImageIcon icon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(150, 225, Image.SCALE_SMOOTH));
                    return icon;
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        posterLabel.setIcon(icon);
                        posterLabel.setText("");
                    } else {
                        posterLabel.setText("No Image");
                    }
                } catch (Exception e) {
                    posterLabel.setText("Error");
                }
            }
        };
        worker.execute();
    }
}

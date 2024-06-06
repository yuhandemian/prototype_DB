import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AdminPage extends JPanel {
    private JComboBox<String> tableComboBox;
    private JTextArea insertTextArea;
    private JTextArea updateSetTextArea;
    private JTextArea updateWhereTextArea;
    private JTextArea deleteWhereTextArea;
    private JTable resultTable;
    private JScrollPane tableScrollPane;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private String currentMode = "Insert"; // 현재 모드를 추적하기 위한 변수
    private JComboBox<String> insertTableComboBox;
    private JComboBox<String> updateTableComboBox;
    private JComboBox<String> deleteTableComboBox;

    public AdminPage(App app) {
        setLayout(new BorderLayout());

        // 상단에 로그아웃 버튼 배치
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("로그아웃");
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        // 로그아웃 버튼 동작 정의
        backButton.addActionListener(e -> app.showPage(App.LOGIN_PAGE));

        // 중앙에 데이터베이스 초기화 버튼 배치
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("관리자 페이지");
        titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);

        JButton initializeButton = new JButton("초기화");
        initializeButton.setForeground(Color.RED);
        initializeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        initializeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "데이터베이스를 초기화하시겠습니까? 기존 데이터가 삭제됩니다.", "초기화 확인", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    DatabaseConnection.initializeDatabase();
                    JOptionPane.showMessageDialog(null, "데이터베이스 초기화 완료");
                }
            }
        });
        centerPanel.add(initializeButton);

        // 데이터베이스 테이블 조회 기능 추가
        JPanel viewPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tableComboBox = new JComboBox<>(new String[]{"Movie", "Theater", "ScreeningSchedule", "Seat", "Customer", "Booking", "Ticket"});
        JButton viewButton = new JButton("검색");
        viewButton.addActionListener(e -> viewTableData((String) tableComboBox.getSelectedItem()));
        viewPanel.add(new JLabel("테이블 명: "));
        viewPanel.add(tableComboBox);
        viewPanel.add(viewButton);
        centerPanel.add(viewPanel);

        // 결과 테이블
        resultTable = new JTable();
        tableScrollPane = new JScrollPane(resultTable);
        centerPanel.add(tableScrollPane);

        // 데이터베이스 수정/삭제/변경 기능 추가
        JPanel modifyPanel = new JPanel();
        modifyPanel.setLayout(new BoxLayout(modifyPanel, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton insertButton = new JButton("Insert");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        modifyPanel.add(buttonPanel);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel insertPanel = new JPanel(new BorderLayout());
        insertTextArea = new JTextArea(5, 40);
        JScrollPane insertScrollPane = new JScrollPane(insertTextArea);
        insertTableComboBox = new JComboBox<>(new String[]{"Movie", "Theater", "ScreeningSchedule", "Seat", "Customer", "Booking", "Ticket"});
        insertPanel.add(new JLabel("테이블 명: "), BorderLayout.NORTH);
        insertPanel.add(insertTableComboBox, BorderLayout.NORTH);
        insertPanel.add(insertScrollPane, BorderLayout.CENTER);

        JPanel updatePanel = new JPanel(new BorderLayout());
        updateSetTextArea = new JTextArea(5, 40);
        updateWhereTextArea = new JTextArea(5, 40);
        JScrollPane updateSetScrollPane = new JScrollPane(updateSetTextArea);
        JScrollPane updateWhereScrollPane = new JScrollPane(updateWhereTextArea);
        updateTableComboBox = new JComboBox<>(new String[]{"Movie", "Theater", "ScreeningSchedule", "Seat", "Customer", "Booking", "Ticket"});
        updatePanel.setLayout(new BoxLayout(updatePanel, BoxLayout.Y_AXIS));
        updatePanel.add(new JLabel("테이블 명: "), BorderLayout.NORTH);
        updatePanel.add(updateTableComboBox, BorderLayout.NORTH);
        updatePanel.add(new JLabel("SET 절"), BorderLayout.NORTH);
        updatePanel.add(updateSetScrollPane, BorderLayout.CENTER);
        updatePanel.add(new JLabel("WHERE 절"), BorderLayout.SOUTH);
        updatePanel.add(updateWhereScrollPane, BorderLayout.SOUTH);

        JPanel deletePanel = new JPanel(new BorderLayout());
        deleteWhereTextArea = new JTextArea(5, 40);
        JScrollPane deleteWhereScrollPane = new JScrollPane(deleteWhereTextArea);
        deleteTableComboBox = new JComboBox<>(new String[]{"Movie", "Theater", "ScreeningSchedule", "Seat", "Customer", "Booking", "Ticket"});
        deletePanel.add(new JLabel("테이블 명: "), BorderLayout.NORTH);
        deletePanel.add(deleteTableComboBox, BorderLayout.NORTH);
        deletePanel.add(new JLabel("WHERE 절"), BorderLayout.CENTER);
        deletePanel.add(deleteWhereScrollPane, BorderLayout.CENTER);

        cardPanel.add(insertPanel, "Insert");
        cardPanel.add(updatePanel, "Update");
        cardPanel.add(deletePanel, "Delete");

        modifyPanel.add(cardPanel);

        JButton executeButton = new JButton("실행");
        executeButton.addActionListener(e -> executeModification(currentMode));
        JButton clearButton = new JButton("취소");
        clearButton.addActionListener(e -> clearTextAreas());

        JPanel actionPanel = new JPanel();
        actionPanel.add(executeButton);
        actionPanel.add(clearButton);

        modifyPanel.add(actionPanel);
        centerPanel.add(modifyPanel);

        add(centerPanel, BorderLayout.CENTER);

        // 버튼 액션 리스너
        insertButton.addActionListener(e -> switchModifyMode("Insert"));
        updateButton.addActionListener(e -> switchModifyMode("Update"));
        deleteButton.addActionListener(e -> switchModifyMode("Delete"));

        // 초기 상태 설정
        switchModifyMode("Insert");
    }

    private void viewTableData(String tableName) {
        try (Connection conn = DatabaseConnection.getAdminConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {

            resultTable.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void switchModifyMode(String mode) {
        cardLayout.show(cardPanel, mode);
        clearTextAreas();
        currentMode = mode;
    }

    private void executeModification(String mode) {
        String query = "";
        String tableName = "";

        switch (mode) {
            case "Insert":
                tableName = (String) insertTableComboBox.getSelectedItem();
                query = "INSERT INTO " + tableName + " " + insertTextArea.getText();
                break;
            case "Update":
                tableName = (String) updateTableComboBox.getSelectedItem();
                query = "UPDATE " + tableName + " SET " + updateSetTextArea.getText() + " WHERE " + updateWhereTextArea.getText();
                break;
            case "Delete":
                tableName = (String) deleteTableComboBox.getSelectedItem();
                query = "DELETE FROM " + tableName + " WHERE " + deleteWhereTextArea.getText();
                break;
        }

        try (Connection conn = DatabaseConnection.getAdminConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(query);
            JOptionPane.showMessageDialog(null, mode + " executed successfully");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearTextAreas() {
        insertTextArea.setText("");
        updateSetTextArea.setText("");
        updateWhereTextArea.setText("");
        deleteWhereTextArea.setText("");
    }

    private static javax.swing.table.TableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Column names
        ArrayList<String> columnNames = new ArrayList<>();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Data of the table
        ArrayList<ArrayList<Object>> data = new ArrayList<>();
        while (rs.next()) {
            ArrayList<Object> row = new ArrayList<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                row.add(rs.getObject(columnIndex));
            }
            data.add(row);
        }

        // Convert data to Object[][]
        Object[][] dataArray = new Object[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            dataArray[i] = data.get(i).toArray();
        }

        // Convert column names to String[]
        String[] columnArray = new String[columnNames.size()];
        columnNames.toArray(columnArray);

        return new javax.swing.table.DefaultTableModel(dataArray, columnArray);
    }
}

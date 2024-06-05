import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class AdminPage extends JPanel {
    private JComboBox<String> tableComboBox;
    private JTextArea queryTextArea;
    private JTable resultTable;
    private JScrollPane tableScrollPane;

    public AdminPage(App app) {
        setLayout(new BorderLayout());

        // 상단에 로그아웃 버튼 배치
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("로그아웃");
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        // 로그아웃 버튼 동작 정의
        backButton.addActionListener(e -> app.showPage(App.LOGIN_PAGE));

        // 중앙에 데이터베이스 초기화 버튼 배치
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton initializeButton = new JButton("Initialize Database");
        initializeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to initialize the database? This will delete all existing data and insert sample data.", "Confirm Initialization", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    DatabaseConnection.initializeDatabase();
                    JOptionPane.showMessageDialog(null, "Database initialized successfully");
                }
            }
        });
        centerPanel.add(initializeButton);

        // 데이터베이스 테이블 조회 기능 추가
        JPanel viewPanel = new JPanel(new FlowLayout());
        tableComboBox = new JComboBox<>(new String[]{"Movie", "Theater", "ScreeningSchedule", "Seat", "Customer", "Booking", "Ticket"});
        JButton viewButton = new JButton("View Table");
        viewButton.addActionListener(e -> viewTableData((String) tableComboBox.getSelectedItem()));
        viewPanel.add(new JLabel("Select Table: "));
        viewPanel.add(tableComboBox);
        viewPanel.add(viewButton);
        centerPanel.add(viewPanel);

        // 결과 테이블
        resultTable = new JTable();
        tableScrollPane = new JScrollPane(resultTable);
        add(tableScrollPane, BorderLayout.SOUTH);

        // 데이터베이스 수정/삭제/변경 기능 추가
        JPanel modifyPanel = new JPanel(new BorderLayout());
        JComboBox<String> modifyComboBox = new JComboBox<>(new String[]{"Insert", "Update", "Delete"});
        modifyComboBox.addActionListener(e -> switchModifyMode((String) modifyComboBox.getSelectedItem()));
        queryTextArea = new JTextArea(5, 40);
        JScrollPane queryScrollPane = new JScrollPane(queryTextArea);
        JButton executeButton = new JButton("Execute");
        executeButton.addActionListener(e -> executeModification((String) modifyComboBox.getSelectedItem()));
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> queryTextArea.setText(""));

        JPanel modifyOptionsPanel = new JPanel();
        modifyOptionsPanel.add(modifyComboBox);
        modifyOptionsPanel.add(executeButton);
        modifyOptionsPanel.add(clearButton);

        modifyPanel.add(modifyOptionsPanel, BorderLayout.NORTH);
        modifyPanel.add(queryScrollPane, BorderLayout.CENTER);

        centerPanel.add(modifyPanel);

        add(centerPanel, BorderLayout.CENTER);
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
        switch (mode) {
            case "Insert":
                queryTextArea.setText("INSERT INTO table_name (column1, column2, ...) VALUES (value1, value2, ...);");
                break;
            case "Update":
                queryTextArea.setText("UPDATE table_name SET column1 = value1, column2 = value2, ... WHERE condition;");
                break;
            case "Delete":
                queryTextArea.setText("DELETE FROM table_name WHERE condition;");
                break;
        }
    }

    private void executeModification(String mode) {
        String query = queryTextArea.getText();
        try (Connection conn = DatabaseConnection.getAdminConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(query);
            JOptionPane.showMessageDialog(null, mode + " executed successfully");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static javax.swing.table.TableModel buildTableModel(ResultSet rs) throws SQLException {
        java.sql.ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Column names
        Vector<String> columnNames = new Vector<>();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Data of the table
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new javax.swing.table.DefaultTableModel(data, columnNames);
    }
}

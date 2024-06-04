import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getAdminConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 드라이버 로드
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1", "root", "1234"); // JDBC 연결
            System.out.println("DB 연결 완료 - 관리자");
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC 드라이버 로드 오류 - 관리자");
        } catch (SQLException e) {
            System.out.println("DB 연결 오류 - 관리자");
        }
        return null;
    }

    public static Connection getUserConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 드라이버 로드
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1", "user1", "user1"); // JDBC 연결
            System.out.println("DB 연결 완료 - 사용자");
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC 드라이버 로드 오류 - 사용자");
        } catch (SQLException e) {
            System.out.println("DB 연결 오류 - 사용자");
        }
        return null;
    }
}

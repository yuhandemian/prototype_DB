import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost/db1";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getAdminConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 드라이버 로드
            Connection adminConn = DriverManager.getConnection(URL, USER, PASSWORD); // JDBC 연결
            System.out.println("DB 연결 완료 - 관리자");
            return adminConn;
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
            Connection userConn = DriverManager.getConnection(URL, "user1", "user1"); // JDBC 연결
            System.out.println("DB 연결 완료 - 사용자");
            return userConn;
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC 드라이버 로드 오류 - 사용자");
        } catch (SQLException e) {
            System.out.println("DB 연결 오류 - 사용자");
        }
        return null;
    }


    public static void initializeDatabase() {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.createStatement();

            // 데이터베이스 초기화
            stmt.executeUpdate("DROP DATABASE IF EXISTS db1;");
            stmt.executeUpdate("CREATE DATABASE db1;");
            stmt.executeUpdate("USE db1;");
            stmt.executeUpdate("SET SQL_SAFE_UPDATES = 0;");

            // 테이블 생성
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Movie (" +
                "MovieID INT PRIMARY KEY AUTO_INCREMENT," +
                "Title VARCHAR(255) NOT NULL," +
                "Duration INT NOT NULL," +
                "Rating VARCHAR(10)," +
                "Director VARCHAR(255)," +
                "Actors VARCHAR(255)," +
                "Genre VARCHAR(50)," +
                "Synopsis VARCHAR(255)," +
                "ReleaseDate DATE," +
                "Score DOUBLE," +
                "PosterURL VARCHAR(255)" +
            ");");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Theater (" +
                "TheaterID INT PRIMARY KEY AUTO_INCREMENT," +
                "TheaterName VARCHAR(255)," +
                "SeatCount INT," +
                "IsActive TINYINT(1)," +
                "SeatRows INT," +
                "SeatColumns INT" +
            ");");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ScreeningSchedule (" +
                "ScheduleID INT PRIMARY KEY AUTO_INCREMENT," +
                "MovieID INT," +
                "TheaterID INT," +
                "StartDate DATE," +
                "DayOfWeek VARCHAR(10)," +
                "ShowNumber INT," +
                "StartTime TIME," +
                "FOREIGN KEY (MovieID) REFERENCES Movie(MovieID)," +
                "FOREIGN KEY (TheaterID) REFERENCES Theater(TheaterID)" +
            ");");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Seat (" +
                "SeatID INT PRIMARY KEY AUTO_INCREMENT," +
                "TheaterID INT," +
                "IsAvailable TINYINT(1)," +
                "SeatName VARCHAR(10)," +
                "FOREIGN KEY (TheaterID) REFERENCES Theater(TheaterID)" +
            ");");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Customer (" +
                "CustomerID INT PRIMARY KEY AUTO_INCREMENT," +
                "CustomerName VARCHAR(255)," +
                "PhoneNumber VARCHAR(20)," +
                "Email VARCHAR(255)" +
            ");");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Booking (" +
                "BookingID INT PRIMARY KEY AUTO_INCREMENT," +
                "PaymentMethod VARCHAR(50)," +
                "PaymentStatus VARCHAR(50)," +
                "Amount INT," +
                "CustomerID INT," +
                "PaymentDate DATE," +
                "FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)" +
            ");");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Ticket (" +
                "TicketID INT PRIMARY KEY AUTO_INCREMENT," +
                "ScheduleID INT," +
                "TheaterID INT," +
                "SeatID INT," +
                "BookingID INT," +
                "IsIssued TINYINT(1)," +
                "StandardPrice INT," +
                "SalePrice INT," +
                "FOREIGN KEY (ScheduleID) REFERENCES ScreeningSchedule(ScheduleID)," +
                "FOREIGN KEY (TheaterID) REFERENCES Theater(TheaterID)," +
                "FOREIGN KEY (SeatID) REFERENCES Seat(SeatID)," +
                "FOREIGN KEY (BookingID) REFERENCES Booking(BookingID)" +
            ");");

            // 데이터 삽입
            stmt.executeUpdate("INSERT INTO Movie (Title, Duration, Rating, Director, Actors, Genre, Synopsis, ReleaseDate, Score, PosterURL) VALUES " +
                "('Action Movie 1', 120, 'R', 'Christopher Nolan', 'Leonardo DiCaprio', 'Action', 'A thrilling action movie', '2023-01-15', 8.5, 'http://example.com/poster1.jpg')," +
                "('Comedy Movie 1', 115, 'PG-13', 'Bong Joon Ho', 'Song Kang Ho', 'Comedy', 'A hilarious comedy movie', '2023-02-10', 7.8, 'http://example.com/poster2.jpg')," +
                "('Drama Movie 1', 110, 'PG', 'Steven Spielberg', 'Tom Hanks', 'Drama', 'An emotional drama movie', '2023-03-15', 9.0, 'http://example.com/poster3.jpg')," +
                "('Horror Movie 1', 130, 'R', 'Jordan Peele', 'Lupita Nyong''o', 'Horror', 'A terrifying horror movie', '2023-04-20', 8.2, 'http://example.com/poster4.jpg'),"+
                "('Sci-Fi Movie 1', 140, 'PG-13', 'Denis Villeneuve', 'Timothée Chalamet', 'Sci-Fi', 'An epic sci-fi movie', '2023-05-25', 8.7, 'http://example.com/poster5.jpg')," +
                "('Romance Movie 1', 100, 'PG', 'Greta Gerwig', 'Saoirse Ronan', 'Romance', 'A touching romance movie', '2023-06-30', 8.1, 'http://example.com/poster6.jpg')," +
                "('Action Movie 2', 125, 'R', 'James Cameron', 'Arnold Schwarzenegger', 'Action', 'Another thrilling action movie', '2023-07-05', 7.9, 'http://example.com/poster7.jpg')," +
                "('Comedy Movie 2', 105, 'PG-13', 'Taika Waititi', 'Chris Hemsworth', 'Comedy', 'Another hilarious comedy movie', '2023-08-10', 8.3, 'http://example.com/poster8.jpg')," +
                "('Drama Movie 2', 135, 'PG', 'Martin Scorsese', 'Robert De Niro', 'Drama', 'Another emotional drama movie', '2023-09-15', 8.9, 'http://example.com/poster9.jpg')," +
                "('Horror Movie 2', 115, 'R', 'Ari Aster', 'Florence Pugh', 'Horror', 'Another terrifying horror movie', '2023-10-20', 8.5, 'http://example.com/poster10.jpg')," +
                "('Sci-Fi Movie 2', 145, 'PG-13', 'Ridley Scott', 'Sigourney Weaver', 'Sci-Fi', 'Another epic sci-fi movie', '2023-11-25', 8.8, 'http://example.com/poster11.jpg')," +
                "('Romance Movie 2', 110, 'PG', 'Nora Ephron', 'Meg Ryan', 'Romance', 'Another touching romance movie', '2023-12-30', 8.4, 'http://example.com/poster12.jpg');");

            stmt.executeUpdate("INSERT INTO Theater (TheaterName, SeatCount, IsActive, SeatRows, SeatColumns) VALUES " +
                "('Theater 1', 100, 1, 10, 10)," +
                "('Theater 2', 150, 1, 15, 10)," +
                "('Theater 3', 200, 1, 20, 10)," +
                "('Theater 4', 120, 1, 12, 10)," +
                "('Theater 5', 180, 1, 18, 10)," +
                "('Theater 6', 130, 1, 13, 10)," +
                "('Theater 7', 140, 1, 14, 10)," +
                "('Theater 8', 160, 1, 16, 10)," +
                "('Theater 9', 170, 1, 17, 10)," +
                "('Theater 10', 110, 1, 11, 10)," +
                "('Theater 11', 190, 1, 19, 10)," +
                "('Theater 12', 105, 1, 10, 10);");

            stmt.executeUpdate("INSERT INTO ScreeningSchedule (MovieID, TheaterID, StartDate, DayOfWeek, ShowNumber, StartTime) VALUES " +
                "(1, 1, '2023-01-15', 'Monday', 1, '18:00:00')," +
                "(2, 2, '2023-02-10', 'Tuesday', 1, '18:00:00')," +
                "(3, 3, '2023-03-15', 'Wednesday', 1, '18:00:00')," +
                "(4, 4, '2023-04-20', 'Thursday', 1, '18:00:00')," +
                "(5, 5, '2023-05-25', 'Friday', 1, '18:00:00')," +
                "(6, 6, '2023-06-30', 'Saturday', 1, '18:00:00')," +
                "(7, 7, '2023-07-05', 'Sunday', 1, '18:00:00')," +
                "(8, 8, '2023-08-10', 'Monday', 1, '18:00:00')," +
                "(9, 9, '2023-09-15', 'Tuesday', 1, '18:00:00')," +
                "(10, 10, '2023-10-20', 'Wednesday', 1, '18:00:00')," +
                "(11, 11, '2023-11-25', 'Thursday', 1, '18:00:00')," +
                "(12, 12, '2023-12-30', 'Friday', 1, '18:00:00');");

            stmt.executeUpdate("INSERT INTO Seat (TheaterID, IsAvailable, SeatName) VALUES " +
                "(1, 1, 'A1'), (1, 1, 'A2'), (1, 1, 'A3'), (1, 1, 'A4')," +
                "(2, 1, 'B1'), (2, 1, 'B2'), (2, 1, 'B3'), (2, 1, 'B4')," +
                "(3, 1, 'C1'), (3, 1, 'C2'), (3, 1, 'C3'), (3, 1, 'C4')," +
                "(4, 1, 'D1'), (4, 1, 'D2'), (4, 1, 'D3'), (4, 1, 'D4')," +
                "(5, 1, 'E1'), (5, 1, 'E2'), (5, 1, 'E3'), (5, 1, 'E4')," +
                "(6, 1, 'F1'), (6, 1, 'F2'), (6, 1, 'F3'), (6, 1, 'F4')," +
                "(7, 1, 'G1'), (7, 1, 'G2'), (7, 1, 'G3'), (7, 1, 'G4')," +
                "(8, 1, 'H1'), (8, 1, 'H2'), (8, 1, 'H3'), (8, 1, 'H4')," +
                "(9, 1, 'I1'), (9, 1, 'I2'), (9, 1, 'I3'), (9, 1, 'I4')," +
                "(10, 1, 'J1'), (10, 1, 'J2'), (10, 1, 'J3'), (10, 1, 'J4')," +
                "(11, 1, 'K1'), (11, 1, 'K2'), (11, 1, 'K3'), (11, 1, 'K4')," +
                "(12, 1, 'L1'), (12, 1, 'L2'), (12, 1, 'L3'), (12, 1, 'L4');");

            stmt.executeUpdate("INSERT INTO Customer (CustomerName, PhoneNumber, Email) VALUES " +
                "('John Doe', '010-1234-5678', 'john@example.com')," +
                "('Jane Smith', '010-2345-6789', 'jane@example.com')," +
                "('Alice Johnson', '010-3456-7890', 'alice@example.com')," +
                "('Bob Brown', '010-4567-8901', 'bob@example.com')," +
                "('Carol White', '010-5678-9012', 'carol@example.com')," +
                "('David Green', '010-6789-0123', 'david@example.com')," +
                "('Eva Black', '010-7890-1234', 'eva@example.com')," +
                "('Frank Gray', '010-8901-2345', 'frank@example.com')," +
                "('Grace Blue', '010-9012-3456', 'grace@example.com')," +
                "('Hank Yellow', '010-0123-4567', 'hank@example.com')," +
                "('Ivy Pink', '010-1234-5678', 'ivy@example.com')," +
                "('Jack Purple', '010-2345-6789', 'jack@example.com');");

            stmt.executeUpdate("INSERT INTO Booking (PaymentMethod, PaymentStatus, Amount, CustomerID, PaymentDate) VALUES " +
                "('Credit Card', 'Completed', 12000, 1, '2023-01-15')," +
                "('Credit Card', 'Completed', 15000, 2, '2023-02-10')," +
                "('Credit Card', 'Completed', 18000, 3, '2023-03-15')," +
                "('Credit Card', 'Completed', 12000, 4, '2023-04-20')," +
                "('Credit Card', 'Completed', 15000, 5, '2023-05-25')," +
                "('Credit Card', 'Completed', 18000, 6, '2023-06-30')," +
                "('Credit Card', 'Completed', 12000, 7, '2023-07-05')," +
                "('Credit Card', 'Completed', 15000, 8, '2023-08-10')," +
                "('Credit Card', 'Completed', 18000, 9, '2023-09-15')," +
                "('Credit Card', 'Completed', 12000, 10, '2023-10-20')," +
                "('Credit Card', 'Completed', 15000, 11, '2023-11-25')," +
                "('Credit Card', 'Completed', 18000, 12, '2023-12-30');");

            stmt.executeUpdate("INSERT INTO Ticket (ScheduleID, TheaterID, SeatID, BookingID, IsIssued, StandardPrice, SalePrice) VALUES " +
                "(1, 1, 1, 1, 1, 12000, 10000)," +
                "(2, 2, 2, 2, 1, 15000, 10000)," +
                "(3, 3, 3, 3, 1, 18000, 10000)," +
                "(4, 4, 4, 4, 1, 12000, 10000)," +
                "(5, 5, 5, 5, 1, 15000, 10000)," +
                "(6, 6, 6, 6, 1, 18000, 15000)," +
                "(7, 7, 7, 7, 1, 12000, 15000)," +
                "(8, 8, 8, 8, 1, 15000, 15000)," +
                "(9, 9, 9, 9, 1, 18000, 15000)," +
                "(10, 10, 10, 10, 1, 12000, 15000)," +
                "(11, 11, 11, 11, 1, 15000, 20000)," +
                "(12, 12, 12, 12, 1, 18000, 20000);");

        } catch (ClassNotFoundException e) {
            // 드라이버 로딩 실패
            System.out.println("드라이버 로딩 실패");
        } catch (SQLException e) {
            // SQL 에러
            System.out.println("에러: " + e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null && !conn.isClosed()) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

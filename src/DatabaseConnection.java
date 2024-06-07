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
            Connection adminConn = DriverManager.getConnection("jdbc:mysql://localhost/db1", "root", "1234"); // JDBC 연결
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
            Connection userConn = DriverManager.getConnection("jdbc:mysql://localhost/db1", "user1", "user1"); // JDBC 연결
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
                "('인셉션', 120, '청불', '크리스토퍼 놀란', '레오나르도 디카프리오', '액션', '스릴 넘치는 액션 영화', '2023-01-15', 8.5, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/zTgjeblxSLSvomt6F6UYtpiD4n7.jpg')," +
                "('기생충', 115, '12세', '봉준호', '송강호', '코미디', '웃긴 코미디 영화', '2023-02-10', 7.8, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/eRM0PykovgtK4lin1D4BUql8TBa.jpg')," +
                "('포레스트 검프', 110, '전체', '로버트 저메키스', '톰 행크스', '드라마', '감동적인 드라마 영화', '2023-03-15', 9.0, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/xdJxoq0dtkchOkUz5UVKuxn7a2V.jpg')," +
                "('겟 아웃', 130, '청불', '조던 필', '루피타 뇽오', '호러', '무서운 호러 영화', '2023-04-20', 8.2, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/paPvmoLgUUQojsSdAZmf7dwkKGT.jpg')," +
                "('듄', 140, '12세', '드니 빌뇌브', '티모시 샬라메', 'SF', '에픽 SF 영화', '2023-05-25', 8.7, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/7zV8FTYofAORGm0Umgh1mNNCym8.jpg')," +
                "('작은 아씨들', 100, '전체', '그레타 거윅', '시얼샤 로넌', '로맨스', '감동적인 로맨스 영화', '2023-06-30', 8.1, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/6qzMhtTyNwpaAthrAbojojfHWrs.jpg')," +
                "('터미네이터 2', 125, '청불', '제임스 카메론', '아놀드 슈워제네거', '액션', '스릴 넘치는 액션 영화', '2023-07-05', 7.9, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/nDVUiEqoeXaDQuM81KkUVNviYIm.jpg')," +
                "('토르: 라그나로크', 105, '12세', '타이카 와이티티', '크리스 헴스워스', '코미디', '웃긴 코미디 영화', '2023-08-10', 8.3, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/jwswXltzpGaKZCtz1CiDjXHQYAs.jpg')," +
                "('택시 드라이버', 135, '전체', '마틴 스콜세지', '로버트 드 니로', '드라마', '감동적인 드라마 영화', '2023-09-15', 8.9, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/g2xIBoOC6TnvsGLrQBTrK4YEw1t.jpg')," +
                "('미드소마', 115, '청불', '아리 애스터', '플로렌스 퓨', '호러', '무서운 호러 영화', '2023-10-20', 8.5, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/bAaeuzr7vzf8EWbhVAyXVIMhXZa.jpg')," +
                "('에일리언', 145, '12세', '리들리 스콧', '시고니 위버', 'SF', '에픽 SF 영화', '2023-11-25', 8.8, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/teaDpXchvks0fJRkD8pEzwv8nsl.jpg')," +
                "('유브 갓 메일', 110, '전체', '노라 애프런', '멕 라이언', '로맨스', '감동적인 로맨스 영화', '2023-12-30', 8.4, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/e2uVtH6TpMfUl7WeOM70ezkcjsU.jpg')," +
                "('존 오브 인터레스트', 105, '12세', '조나단 글레이저', '크리스티안 프리델, 산드라 ...', '드라마', '감동적인 영화', '2024-06-05', 9.1, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/qqXQERsbWGrUldJjr0fDHuWjhPG.jpg')," +
                "('드림 시나리오', 102, '15세', '크리스토퍼 보글리', '니콜라스 케이지, 줄리안 니...', '코미디', '흥미로운 시나리오', '2024-05-29', 8.9, 'https://www.themoviedb.org/t/p/w1280/uarf91ft8eVDGMgMH4o6ip7rkCm.jpg')," +
                "('찬란한 내일로', 95, '12세', '난니 모레티', '난니 모레티, 마티유 아말릭 ...', '드라마', '희망적인 이야기', '2024-05-29', 8.7, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/9WDl0UZBrF6UYDst3YuMM99o9c9.jpg')," +
                "('낸 골딘, 모든 아름다움과...', 122, '15세', '로라 포이트러스', '낸 골딘', '다큐멘터리', '아름다움에 대한 이야기', '2024-05-15', 9.0, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/fiGdZhBQip33XV8hjgxIqN9OHGR.jpg')," +
                "('힙노시스: LP 커버의 전...', 101, '15세', '안톤 코르빈', '오프리 파웰, 스톰 소거슨, ...', '다큐멘터리', 'LP 커버의 역사', '2024-05-01', 8.8, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/fhqORl8T9JSbOCxXAKZhQ1SyvJo.jpg')," +
                "('챌린저스', 132, '15세', '루카 구아다니노', '젠데이아 콜먼, 마이크 파이...', '드라마', '챌린지에 대한 이야기', '2024-04-24', 9.2, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/iUDG0gsPZxNm9IOLqpIUZiUMVMZ.jpg')," +
                "('땅에 쓰는 시', 113, '전체', '정다운', '정영선', '드라마', '감동적인 시 이야기', '2024-04-17', 8.5, 'https://media.themoviedb.org/t/p/w600_and_h900_bestv2/dxR8fWoqC56T66URAc0KY7oMrlN.jpg');");

            stmt.executeUpdate("INSERT INTO Theater (TheaterName, SeatCount, IsActive, SeatRows, SeatColumns) VALUES " +
                "('영화관 1', 100, 1, 10, 10)," +
                "('영화관 2', 150, 1, 15, 10)," +
                "('영화관 3', 200, 1, 20, 10)," +
                "('영화관 4', 120, 1, 12, 10)," +
                "('영화관 5', 180, 1, 18, 10)," +
                "('영화관 6', 130, 1, 13, 10)," +
                "('영화관 7', 140, 1, 14, 10)," +
                "('영화관 8', 160, 1, 16, 10)," +
                "('영화관 9', 170, 1, 17, 10)," +
                "('영화관 10', 110, 1, 11, 10)," +
                "('영화관 11', 190, 1, 19, 10)," +
                "('영화관 12', 105, 1, 10, 10);");

            stmt.executeUpdate("INSERT INTO ScreeningSchedule (MovieID, TheaterID, StartDate, DayOfWeek, ShowNumber, StartTime) VALUES " +
                "(1, 1, '2023-01-15', '월요일', 1, '18:00:00')," +
                "(1, 1, '2023-01-16', '화요일', 2, '20:00:00')," +
                "(2, 2, '2023-02-10', '화요일', 1, '19:00:00')," +
                "(2, 2, '2023-02-11', '수요일', 2, '21:00:00')," +
                "(3, 3, '2023-03-15', '수요일', 1, '20:00:00')," +
                "(4, 4, '2023-04-20', '목요일', 1, '21:00:00')," +
                "(5, 5, '2023-05-25', '금요일', 1, '22:00:00')," +
                "(6, 6, '2023-06-30', '토요일', 1, '15:00:00')," +
                "(7, 7, '2023-07-05', '일요일', 1, '16:00:00')," +
                "(8, 8, '2023-08-10', '월요일', 1, '17:00:00')," +
                "(9, 9, '2023-09-15', '화요일', 1, '18:00:00')," +
                "(10, 10, '2023-10-20', '수요일', 1, '19:00:00')," +
                "(11, 11, '2023-11-25', '목요일', 1, '20:00:00')," +
                "(12, 12, '2023-12-30', '금요일', 1, '21:00:00')," +
                "(13, 1, '2024-06-05', '수요일', 1, '20:20:00')," +
                "(13, 2, '2024-06-06', '목요일', 2, '22:20:00')," +
                "(14, 2, '2024-05-29', '수요일', 1, '20:00:00')," +
                "(15, 3, '2024-05-29', '수요일', 2, '21:30:00')," +
                "(16, 4, '2024-05-15', '수요일', 1, '18:40:00')," +
                "(17, 5, '2024-05-01', '수요일', 1, '17:20:00')," +
                "(18, 6, '2024-04-24', '수요일', 1, '16:10:00')," +
                "(19, 7, '2024-04-17', '수요일', 1, '15:00:00')," +
                "(19, 8, '2024-04-18', '목요일', 2, '17:30:00');");

            stmt.executeUpdate("INSERT INTO Seat (TheaterID, IsAvailable, SeatName) VALUES " +
                "(1, 1, 'A1'), (1, 1, 'A2'), (1, 1, 'A3'), (1, 1, 'A4'), (1, 1, 'A5')," +
                "(1, 1, 'B1'), (1, 1, 'B2'), (1, 1, 'B3'), (1, 1, 'B4'), (1, 1, 'B5')," +
                "(2, 1, 'C1'), (2, 1, 'C2'), (2, 1, 'C3'), (2, 1, 'C4'), (2, 1, 'C5')," +
                "(2, 1, 'D1'), (2, 1, 'D2'), (2, 1, 'D3'), (2, 1, 'D4'), (2, 1, 'D5')," +
                "(3, 1, 'E1'), (3, 1, 'E2'), (3, 1, 'E3'), (3, 1, 'E4'), (3, 1, 'E5')," +
                "(3, 1, 'F1'), (3, 1, 'F2'), (3, 1, 'F3'), (3, 1, 'F4'), (3, 1, 'F5')," +
                "(4, 1, 'G1'), (4, 1, 'G2'), (4, 1, 'G3'), (4, 1, 'G4'), (4, 1, 'G5')," +
                "(4, 1, 'H1'), (4, 1, 'H2'), (4, 1, 'H3'), (4, 1, 'H4'), (4, 1, 'H5')," +
                "(5, 1, 'I1'), (5, 1, 'I2'), (5, 1, 'I3'), (5, 1, 'I4'), (5, 1, 'I5')," +
                "(5, 1, 'J1'), (5, 1, 'J2'), (5, 1, 'J3'), (5, 1, 'J4'), (5, 1, 'J5')," +
                "(6, 1, 'K1'), (6, 1, 'K2'), (6, 1, 'K3'), (6, 1, 'K4'), (6, 1, 'K5')," +
                "(7, 1, 'L1'), (7, 1, 'L2'), (7, 1, 'L3'), (7, 1, 'L4'), (7, 1, 'L5')," +
                "(8, 1, 'M1'), (8, 1, 'M2'), (8, 1, 'M3'), (8, 1, 'M4'), (8, 1, 'M5')," +
                "(9, 1, 'N1'), (9, 1, 'N2'), (9, 1, 'N3'), (9, 1, 'N4'), (9, 1, 'N5')," +
                "(10, 1, 'O1'), (10, 1, 'O2'), (10, 1, 'O3'), (10, 1, 'O4'), (10, 1, 'O5')," +
                "(11, 1, 'P1'), (11, 1, 'P2'), (11, 1, 'P3'), (11, 1, 'P4'), (11, 1, 'P5')," +
                "(12, 1, 'Q1'), (12, 1, 'Q2'), (12, 1, 'Q3'), (12, 1, 'Q4'), (12, 1, 'Q5');");

            stmt.executeUpdate("INSERT INTO Customer (CustomerName, PhoneNumber, Email) VALUES " +
                "('유재석', '010-1234-5678', 'yoo@example.com')," +
                "('강호동', '010-2345-6789', 'kang@example.com')," +
                "('신동엽', '010-3456-7890', 'shin@example.com')," +
                "('김구라', '010-4567-8901', 'kim@example.com')," +
                "('박명수', '010-5678-9012', 'park@example.com')," +
                "('정준하', '010-6789-0123', 'jung@example.com')," +
                "('하하', '010-7890-1234', 'haha@example.com')," +
                "('이경규', '010-8901-2345', 'lee@example.com')," +
                "('김병만', '010-9012-3456', 'kimbyung@example.com')," +
                "('장도연', '010-0123-4567', 'jang@example.com')," +
                "('유병재', '010-1234-5678', 'yoo2@example.com')," +
                "('김민경', '010-2345-6789', 'kim@example.com');");

            stmt.executeUpdate("INSERT INTO Booking (PaymentMethod, PaymentStatus, Amount, CustomerID, PaymentDate) VALUES " +
                "('신용카드', '완료', 12000, 1, '2023-01-15')," +
                "('신용카드', '완료', 15000, 2, '2023-02-10')," +
                "('신용카드', '완료', 18000, 3, '2023-03-15')," +
                "('신용카드', '완료', 12000, 4, '2023-04-20')," +
                "('신용카드', '완료', 15000, 5, '2023-05-25')," +
                "('신용카드', '완료', 18000, 6, '2023-06-30')," +
                "('신용카드', '완료', 12000, 7, '2023-07-05')," +
                "('신용카드', '완료', 15000, 8, '2023-08-10')," +
                "('신용카드', '완료', 18000, 9, '2023-09-15')," +
                "('신용카드', '완료', 12000, 10, '2023-10-20')," +
                "('신용카드', '완료', 15000, 11, '2023-11-25')," +
                "('신용카드', '완료', 18000, 12, '2023-12-30');");

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

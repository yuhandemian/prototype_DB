import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class MovieObj {
    public int MovieID;
    public String Title;
    public String Director;
    public String Actors;
    public String Genre;
    public String Rating;
    public int Duration;
    public String Synopsis;
    public String PosterURL;
    public LocalDate StartDate;
    public LocalTime StartTime;
    public String ScreeningStatus;

    public MovieObj(ResultSet rs) {
        try {
            MovieID = rs.getInt("MovieID");
            Title = rs.getString("Title");
            Director = rs.getString("Director");
            Actors = rs.getString("Actors");
            Genre = rs.getString("Genre");
            Rating = rs.getString("Rating");
            Duration = rs.getInt("Duration");
            Synopsis = rs.getString("Synopsis");
            PosterURL = rs.getString("PosterURL");
            StartDate = rs.getDate("StartDate") != null ? rs.getDate("StartDate").toLocalDate() : null;
            StartTime = rs.getTime("StartTime") != null ? rs.getTime("StartTime").toLocalTime() : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setScreeningStatus(LocalDate currentDate, LocalTime currentTime) {
        if (StartDate == null || StartTime == null) {
            ScreeningStatus = "정보 없음";
        } else if (StartDate.isBefore(currentDate) || (StartDate.isEqual(currentDate) && StartTime.isBefore(currentTime))) {
            ScreeningStatus = "상영 종료";
        } else {
            ScreeningStatus = "상영 중";
        }
    }
}

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

class MovieObj {
	public String MovieID;
	public String Title;
	public String Duration;
	public String Rating;
	public String Director;
	public String Actors;
	public String Genre;
	public String Synopsis;
	public String ReleaseDate;
	public String Score;
	public String PosterURL;
	
	public MovieObj(ResultSet rs) {
		try {
			MovieID = rs.getString("MovieID");
			Title = rs.getString("Title");
			Duration = rs.getString("Duration");
			Rating = rs.getString("Rating");
			Director = rs.getString("Director");
			Actors = rs.getString("Actors");
			Genre = rs.getString("Genre");
			Synopsis = rs.getString("Synopsis");
			ReleaseDate = rs.getString("ReleaseDate");
			Score = rs.getString("Score");
			PosterURL = rs.getString("PosterURL");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}

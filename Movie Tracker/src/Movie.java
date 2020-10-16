// Author: Maik De Leon Lopez
public class Movie {
	// Movie Data
	private String title;
	private int year;
	private double duration;
	private String genre;
	private double rating;

	// Constructor
	public Movie(String title, int year, double duration, String genre, double rating) {
		this.title = title;
		this.year = year;
		this.duration = duration;
		this.genre = genre;
		this.rating = rating;
	}
	
	// Helper Methods
	public String getTitle() {
		return this.title;
	}
	public String getGenre() {
		return this.genre;
	}
	
	// To String Method
	public String toString() {
		String s = "Title: "+this.title+"| Year: "+this.year+"| Duration: "+this.duration+"| Genre: "+this.genre+"| Rating: "+this.rating+"\n";
		return s;
	}
}

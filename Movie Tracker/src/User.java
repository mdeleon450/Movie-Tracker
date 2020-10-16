// Author: Maik De Leon Lopez
public class User {
	// Data
	private String firstName;
	private String lastName;
	private String userID;
	private String password;
		
	public User(String firstName, String lastName, String userID, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userID = userID;
		this.password = password;
	}
	
	public String getUserID() {
		return this.userID;
	}
	
	public String getPass() {
		return this.password;
	}
	
	public String toString() {
		//System.out.print("First Name: "+this.firstName+" Last Name: "+this.lastName+" UserName: "+this.userID);
		String s = this.firstName+"/N"+this.lastName+"/N"+this.userID+"/N"+this.password+"/N";
		return s;
	}
	
	//An equals method that checks if two users are the same
	public boolean equals(User current, User target) {
		return ((current.firstName.equals(target.firstName))&&(current.lastName.equals(target.lastName))&&(current.userID.equals(target.userID))&&(current.password.equals(target.password)));
	}
}

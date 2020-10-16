
// Author: Maik De Leon Lopez

// Course: CSC 311-01

// Date: 03/26/2019

import java.io.*;

import java.util.*;

public class MoviePlayer {

	public static void main(String[] args) {
		final int HISTORY_LIMIT = 100;						//Max size for queue
		DHArrayList<Object> movList = new DHArrayList<>();
		initializeMovies(movList);							//Put the movies in the ArrayList
		DHArrayList usersList = readUserData();				//Read in the users registered
		printUsers(usersList);
		CircularQueue movQueue = new CircularQueue(HISTORY_LIMIT);	//Create our Queue of maxSize
		int[] movieCounter = new int[movList.getSize()];		//Will be used to count how many times each movie was watched
		int[] genreCounter = new int[3];						//Same as above but for genres (only 3 genres available)
		User current = null;									//No user is logged in
		Scanner reader = new Scanner(System.in);
		int selector;
		boolean flag = true;
		while (flag) {				//Read user input until 9 is selected
			System.out.println(
					"1. Register User\n2. Login Existing User\n3. Watch a Movie\n4. Watch History (Oldest to Recent) \n5. Watch History (Recent to Oldest) \n"
							+ "6. Number of times Each Movie is Watched\n7. Most Watched Movie\n8. Most Watched Genre\n9. Logout\n");
			selector = reader.nextInt();
			if (selector == 9) {	//If 9 is chosen we still want to save the current login into userList and into a txt file
				current = selectOption(movList, usersList, selector, current, movieCounter, genreCounter, movQueue);
				flag = false;
			} else {
				current = selectOption(movList, usersList, selector, current, movieCounter, genreCounter, movQueue);
			}
		}
	}

	public static DHArrayList readUserData() {
		// Method to Read in File
		// Place UserId and Password into ArrayList
		try {
			DHArrayList<String> userInfo = new DHArrayList<>();
			DHArrayList<User> usersList = new DHArrayList<>();
			User temp;
			Scanner readfile;
			readfile = new Scanner(new File("movieusers.txt"));
			readfile.useDelimiter("/N"); 	//Each piece of information is delimited by /N
			while (readfile.hasNext()) {
				userInfo.add(readfile.next());
			}
			// System.out.println(userInfo.getSize());
			readfile.close();
			for (int i = 0; i < userInfo.getSize() - 1; i += 4) {
				temp = new User(userInfo.get(i), userInfo.get(i + 1), userInfo.get(i + 2), userInfo.get(i + 3));	//The information for a user is as follows:
				usersList.add(temp);																				//first,last,username,password
			}										//We then add them as users into our ArrayList
			return usersList;
		} catch (FileNotFoundException e) {
			System.out.println("No Existing Users");	//If file was not found 
			return null;
		}
	}

	// This Method Executes the Desired Option and Once Done it Returns to the
	// Prompt Loop
	public static User selectOption(DHArrayList movList, DHArrayList usersList, int selector, User current,
			int[] movieCounter, int[] genreCounter, CircularQueue movQueue) {
		Scanner reader = new Scanner(System.in);
		boolean sentinel = true;
		boolean flag = false;
		Movie[] genreList = new Movie[movList.getSize()];
		String first;
		String last;
		String userName;
		String password = null;
		String passEntered;
		String movieInput;
		int tempRand;
		int largestNum = 0;
		switch (selector) {
		// Register user and create user login
		case 1:
			if (current != null) {	//If someone is already logged in then ignore
				System.out.println("User already Logged in");
				return current;
			}
			sentinel = false;	// The default value is false, used later to check if the username is unique
			System.out.println("Please Enter First Name: ");
			first = reader.nextLine();
			System.out.println("Please Enter Last Name: ");
			last = reader.nextLine();
			userName = null;	// User name temporarily is null
			for (int i = 2; i > 0; i--) {	// User gets 2 attempts at creating a unique username
				System.out.println("Attempts Remaining: " + i + "\nPlease Enter User Name (Must Be Unique) : ");
				userName = reader.nextLine();
				sentinel = isUnique(userName, usersList);	//Sentinel will hold the value true if it is unique
				if (sentinel) {
					break;			//if it is unique we break from the for loop
				}
			}			//if the username is unique and is not null
			if (sentinel && userName != null) {
				sentinel = true;
				while (sentinel) {		//We ask for password until a valid one is provided /N is not allowed
					System.out.println("Please Enter a Password (/N not Allowed in Password): ");
					password = reader.nextLine();
					if (!password.contains("/N")) {
						sentinel = false;	//if the password doesn't contain the /N we exit the while loop
					}
				}
				current = new User(first, last, userName, password);
				usersList.add(current);
				return current;
			} else {
				//If username is not unique
				// Check if Password contains /N
				sentinel = true;
				while (sentinel) {
					System.out.println("Please Enter a Password (/N not Allowed in Password): ");
					password = reader.nextLine();
					if (!password.contains("/N")) {
						sentinel = false;
					}
				}
				//We create a unique username by using the first 4 letters of their last name joined by 4 random numbers
				while (!isUnique(userName, usersList)) {
					userName = last.substring(0, 4);
					for (int i = 0; i < 4; i++) {
						tempRand = (int) (Math.random() * 9);
						userName += "" + tempRand;
					}
					current = new User(first, last, userName, password);	//Create the username everytime in the while loop
				}
				usersList.add(current);		//add the user created to the userList
				return current;
			}
			// break;
		case 2:
			// Login Existing User
			// Read currentUser Data From txt file named myLogin.txt
			if (current != null) {	//If user is already logged in ignore
				System.out.println("User already Logged in");
				return current;
			}
			boolean userFlag = false;	//Will use to denote that we have found a matching userName
			int userIndex = - 99;		//Used to get the user
			System.out.println("Enter UserName: ");
			String tempUserName = reader.nextLine();
			for(int i = 0; i < usersList.getSize(); i++) {
				if(tempUserName.equals(((User) usersList.get(i)).getUserID())) {
					userFlag = true;				//If the username the matches one from our list
					userIndex = i;					//get that index and set the password to the user password
					password = ((User) usersList.get(i)).getPass();
				}
			}
			if(userFlag == false || userIndex == -99) {	//If username was not found
				System.out.println("Couldn't Find UserName: "+tempUserName);
				return null;
			}
			else {	//Username is found
				int i = 2;	//User gets 2 tries to match the password
				sentinel = true;
				System.out.println("Please Enter Password: ");
				passEntered = reader.nextLine();
				while(sentinel) {
					if(password.equals(passEntered) == true) {	//If the passwords match
						System.out.println("Login Successful");
						return (User) usersList.get(userIndex);	//return the UserData to Main
					}
					else {
						System.out.println("Incorrect Password\n"+i+" Attempts Remain\nEnter Password: ");
						passEntered = reader.nextLine();	//If passwords don't match try again
						i--;
					}
					if(i <= 0) {
						System.out.println("Returning to Main Menu");
						sentinel = false;	//Too many Attempts return to main menu
						return null;
					}
				}
			}

			break;
		case 3:
			// Pick a Movie to Watch can Search for Title or Go Back To Main Menu
			if (current == null) {	//If no one is logged in
				System.out.println("Please Login to Use The System");
				return null;
			}
			sentinel = true;
			while (sentinel) {	// A prompt loop that asks the user for inputs 1-3
				System.out.println("1. Search By Title\n2. Search By Genre\n3. Go Back to Main Menu ");
				int choice = reader.nextInt();
				if (choice > 3 || choice < 1) {
					System.out.println("Invalid Input: Enter 1, 2 or 3");
				} else {
					if (choice == 1) {
						// Search Movie by Title
						System.out.println("Please Enter Movie Title: ");
						movieInput = reader.nextLine();	//Used to get rid of the \n char that was left by .nextInt()
						movieInput = reader.nextLine();	//actually read user input
						for (int i = 0; i < movList.getSize(); i++) {
							if (movieInput.toLowerCase().equals(((Movie) movList.get(i)).getTitle().toLowerCase())) {
								System.out.println("Watching: " + movieInput);	//If user input is equal to a movie title, watch that movie
								movQueue.offer(movList.get(i));	//add the movie to the queue for later use 
								movieCounter[i]++;				//increment the numbers watched for that movie
								switch (((Movie) movList.get(i)).getGenre().toLowerCase()) {	//Increment the genre counter for that movie's genre
								case "drama":				
									genreCounter[0]++;
									break;
								case "horror":
									genreCounter[1]++;
									break;
								case "sci-fi":
									genreCounter[2]++;
									break;
								}
								return current;		//Once the movie is found we go back to the main method
							}
						}
						System.out.println(movieInput + " Was Not Found, Sorry"); // if the movie wasn't found then print
					} else if (choice == 2) {
						// Search Movie by Genre
						System.out.println("Please Enter Movie Genre: ");
						movieInput = reader.nextLine();	//to get rid of the \n left by the .nextInt()
						movieInput = reader.nextLine();
						for (int i = 0; i < movList.getSize(); i++) {
							if (movieInput.toLowerCase().equals(((Movie) movList.get(i)).getGenre().toLowerCase())) {	//if the genre input by the user is equal to a genre
								System.out.println(((Movie) movList.get(i)).getTitle());	//print out the movies that contain the genre
								genreList[i] = (Movie) (movList.get(i));					//add those movies with the same genre to an array of movies
								flag = true;												//set flag to be true since we found the genre
							}
						}
						if (flag == true) {		//if we found the genre
							System.out.println("Select Movie Title: ");
							movieInput = reader.nextLine();		//ask the user for which movie they want to watch
							for (int i = 0; i < genreList.length; i++) {
								if (movieInput.toLowerCase()
										.equals(((Movie) movList.get(i)).getTitle().toLowerCase())) {	//if the movie inputed is equal to a movie title, watch that movie
									System.out.println("Watching: " + movieInput);
									movQueue.offer(movList.get(i));			//add to the queue for later use
									movieCounter[i]++;						//increment that movies watches
									switch (((Movie) movList.get(i)).getGenre().toLowerCase()) {	//increment the genres watches
									case "drama":
										genreCounter[0]++;
										break;
									case "horror":
										genreCounter[1]++;
										break;
									case "sci-fi":
										genreCounter[2]++;
										break;
									}
									return current;
								}
							}
						} else {
							System.out.println("No Such Genre, Try Again");	//if genre was not found
						}
					} else {
						// Go Back To Main Menu	// if 3 was selected we go back to the main menu
						sentinel = false;
					}
				}
			}
			break;
		case 4:
			//Print the Watch History of Movies in Chronological Order(Duplicates Allowed)
			//The Queue already has them in this order from Oldest to Recently Watched Since a Queue is FIFO
			if (current == null) {
				System.out.println("Please Login to Use The System");	//Print if user is not logged in
				return null;
			}
			largestNum = 0; //Set the largest number to 0
			for (int i = 0; i < movieCounter.length; i++) {
				if (largestNum < movieCounter[i]) {
					largestNum = movieCounter[i];	//If a counter is larger than that number set it to the counter
				}
			}
			if (largestNum == 0) {	//If the largest number is still 0 then nothing was watched
				System.out.println("No Movie Has Been Watched");
			} else {
				System.out.println(movQueue);	//Print out chronological order
			}
			break;
		case 5: 
			//Print the Watch History of Movies in Reverse Chronological Order(No Duplicates Allowed)
			//We will have to reverse the queue and set restrictions for to not have duplicates
			if (current == null) {
				System.out.println("Please Login to Use The System");
				return null;
			}
			largestNum = 0;	//Same Logic as Case 4
			for (int i = 0; i < movieCounter.length; i++) {
				if (largestNum < movieCounter[i]) {
					largestNum = movieCounter[i];
				}
			}
			if (largestNum == 0) {
				System.out.println("No Movie Has Been Watched");
			} else {	//If we have a movie watched
				movQueue = reverseQueue(movQueue);	//Reverse the Queue to have Reverse Chronological Order
				printUnique(movQueue);	//Print out only the unique movies in order
				movQueue = reverseQueue(movQueue);	//Reverse the Queue for future processing in Chronological Order
			}
			break;
		case 6:
			//Print out the Number of times each movie was watched
			if (current == null) {
				System.out.println("Please Login to Use The System");
				return null;
			}
			// Count How Many Times Each Movie was Watched
			System.out.println("Times Each Movie Was Watched");
			for (int i = 0; i < movieCounter.length; i++) {		//We have saved the number of times each movie was watched in a int[] called movieCounter
				System.out.println(((Movie) movList.get(i)).getTitle() + " : " + movieCounter[i]);
			}
			break;
		case 7:
			//Print out the Most Watched Movie
			if (current == null) {
				System.out.println("Please Login to Use The System");
				return null;
			}
			// Set the largestNumber to be the first movie
			largestNum = movieCounter[0];
			for (int i = 0; i < movieCounter.length; i++) {
				if (largestNum <= movieCounter[i]) {	//if the largestNumber is smaller than or equal to the movie counter
					largestNum = movieCounter[i];		//Set the largestNum to be the counter
					genreList[0] = (Movie) movList.get(i);	//We put the movie whose counter is the largest in the first element of the movie array named genreList
				}
			}
			if (largestNum == 0) {	
				System.out.println("No Movie Has Been Watched");
			} else {
				System.out.println(	//The largestNumber of watches should be in the first element of the movie array
						"The Most Watched Movie Was: " + genreList[0].getTitle() + " " + largestNum + " Times");
			}
			break;
		case 8:
			//Print out the Most Watched Genre
			if (current == null) {
				System.out.println("Please Login to Use The System");
				return null;
			}
			tempRand = 0; //We will use tempRand as a temporary integer
			largestNum = genreCounter[0];	//Set the largest number to be the first genre
			for (int i = 0; i < genreCounter.length; i++) {
				if (largestNum < genreCounter[i]) {	//If the largest number is less than the genre counter
					largestNum = genreCounter[i];	//Make the largestNumber be equal to the genre counter
					tempRand = i;		//keep the index of the genre in the temporary Int
				}
			}
			if (largestNum == 0) {
				System.out.println("No Movie Has Been Watched");
			} else {
				System.out.println("The Most Watched Genre is: ");
				switch (tempRand) {		//Checks which number is equal to the index (tempRand) depending on what it is, we pick the genre
				case 0:
					System.out.println("Drama " + largestNum + " times");
					break;
				case 1:
					System.out.println("Horror " + largestNum + " times");
					break;
				case 2:
					System.out.println("Sci-Fi " + largestNum + " times");
					break;
				}
			}
			break;
		case 9:
			//LogOut Option
			if (current == null) {
				System.out.println("Quitting");
				return null;
			} else {
				// Write User Info Onto myLogin.txt file
				// Check if User id matches with any in the array
				// If yes ignore
				// Else add it to the array
				// Write all array entries into users.txt
				sentinel = false;
				for (int i = 0; i < usersList.getSize(); i++) {
					if (current.equals(current, (User) usersList.get(i))) { // If the current user is in the userList then sentinel=true
						sentinel = true;
					}
				}
				if (!sentinel) {	//If current user is not in the usersList then add them
					usersList.add(current);
				}
				try {
					FileWriter fileWriter = new FileWriter("myLogin.txt");	
					PrintWriter printWriter = new PrintWriter(fileWriter);
					printWriter.print(current.toString());		//We print the current users info into a myLogin.txt
					printWriter.close();
				} catch (IOException e) {
					System.out.println("Unable to Write to myLogin.txt");
				}
				try {
					FileWriter fileWriter = new FileWriter("movieusers.txt");
					PrintWriter printWriter = new PrintWriter(fileWriter);
					printWriter.print(usersList.toString());	//We update the movieuser.txt to contain all users in the usersList
					printWriter.close();
				} catch (IOException e) {
					System.out.println("Unable to Write to movieusers.txt");
				}
				break;
			}
		default:
			// If the Selector is not 1-9 Display Error Message
			System.out.println("Please Enter Options 1-9");
			break;
		}
		return current;
	}

	public static boolean isUnique(String username, DHArrayList userData) {
		for (int i = 0; i < userData.getSize(); i++) {
			if (username.equals(((User) userData.get(i)).getUserID())) { //If the username is equal to the userID of a user then it is not unique
				return false;
			}
		}
		return true;	// The username is Unique
	}

	public static CircularQueue reverseQueue(CircularQueue movQueue) {
		ArrayStack<Movie> movStack = new ArrayStack<>(100);	//Using a stack to reverse the contents of the Queue
		while (!movQueue.isEmpty()) {	//While the queue is not empty
			movStack.push((Movie) movQueue.poll());	//Push the contents of the queue into the stack, while removing it from the queue 
		}
		while (!movStack.isEmpty()) {
			movQueue.offer(movStack.pop());	//offer the contents of the stack, now in reverse order, while removing it from the stack
		}
		return movQueue;	//Reversed Chronological Ordered Queue
	}

	public static void printUnique(CircularQueue movQueue) {
		Movie temp;
		int[] movieCounter = new int[7]; 	//Since we have 7 movies for now
		int num = 0;
		int queueSize = movQueue.getSize();	//Get the size of the queue
		Movie[] tempArr = new Movie[queueSize];	//Make an array of that size
		while (!movQueue.isEmpty()) {
			temp = (Movie) movQueue.poll();
			tempArr[num] = temp;	//Save the contents of the queue into the array (preserves order)
			num++;	//increment the index of the array
		}
		for (int i = 0; i < queueSize; i++) {
			movQueue.offer(tempArr[i]);	//Put back all the elements in order into the queue
		}
		System.out.println("Front: ");	//Would like to have a similar output as option 4
		for (int i = 0; i < queueSize; i++) {
			switch (tempArr[i].getTitle().toLowerCase()) {	//Check the movie title if it corresponds to any print out the movie 
			case "the road":
				if (movieCounter[0] == 0) {	//This is what prevents duplicates, if the movie hasn't been printed then print else ignore
					movieCounter[0]++;
					System.out.print(" | " + tempArr[i]);
				}
				break;
			case "2001: a space odyssey":
				if (movieCounter[1] == 0) {
					movieCounter[1]++;
					System.out.print(" | " + tempArr[i]);
				}
				break;
			case "the silence of the lambs":
				if (movieCounter[2] == 0) {
					movieCounter[2]++;
					System.out.print(" | " + tempArr[i]);
				}
				break;
			case "apocalypse now":
				if (movieCounter[3] == 0) {
					movieCounter[3]++;
					System.out.print(" | " + tempArr[i]);
				}
				break;
			case "jurassic park":
				if (movieCounter[4] == 0) {
					movieCounter[4]++;
					System.out.print(" | " + tempArr[i]);
				}
				break;
			case "the exorcist":
				if (movieCounter[5] == 0) {
					movieCounter[5]++;
					System.out.print(" | " + tempArr[i]);
				}
				break;
			case "the shining":
				if (movieCounter[6] == 0) {
					movieCounter[6]++;
					System.out.print(" | " + tempArr[i]);
				}
				break;
			}
		}
	}
	
	public static void printUsers(DHArrayList usersList) {
		System.out.println("Existing Users: ");
		for(int i = 0 ; i < usersList.getSize(); i++) {
			System.out.println(((User) usersList.get(i)).getUserID());
		}
	}
	public static void initializeMovies(DHArrayList movList) {
		movList.add(new Movie("The Road", 2009, 1.51, "Drama", 7.3));
		movList.add(new Movie("2001: A Space Odyssey", 1968, 2.44, "Sci-Fi", 8.3));
		movList.add(new Movie("The Silence of the Lambs", 1991, 2.18, "Drama", 8.6));
		movList.add(new Movie("Apocalypse Now", 1979, 2.33, "Drama", 8.5));
		movList.add(new Movie("Jurassic Park", 1993, 2.7, "Sci-Fi", 8.1));
		movList.add(new Movie("The Exorcist", 1973, 2.12, "Horror", 8.0));
		movList.add(new Movie("The Shining", 1980, 2.26, "Horror", 8.4));
	}

}

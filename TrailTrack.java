// --== CS400 File Header Information ==--
// Name: Lucy Jiang
// Email: jiang68@wisc.edu
// new commit

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.lang.Math;

/** 
 * This class is to keep track of trail stored in a external csv file
 * It implement the HashTable datastructure, each trail is stored in Node.class
 * Each node is stored at their Hash index (given by Hashkey) as a LinkedList of Nodes
 * The backend allows to browse through the stored list, update list rating, add new trail
 * remove stored trail, 
 * The front end allwos user to interface with the backend through promopt and user input
 */
public class TrailTrack {
	private int capacity;
	private int occupied;
	private LinkedList<Node<String>>[] map;
	private double lat;
	private double lon;

	/**
	 * constructor trail TrailTrack
	 * the default occupied is 0, 
	 * it intitiate an empty list of LinkedList[] 
	 * It set the default GPS location in near UW-Madison
	 * @para capacity the list size of the LinkedList[]
	 */
	@SuppressWarnings("unchecked")
	public TrailTrack(int capacity) {
		this.capacity = capacity;
		this.occupied = 0;
		this.map = (LinkedList<Node<String>>[]) new LinkedList[capacity];
		this.lat = 4;
		this.lon = 24;
	}

	/**
	 * constructor trail TrailTrack
	 * the default occupied is 0, 
	 * it intitiate the list of LinkedList[] from csv file given
	 * It set the default GPS location in near UW-Madison
	 * @para capacity the list size of the LinkedList[]
	 * @para filename of csv file
	 */
	@SuppressWarnings("unchecked")
	public TrailTrack(int capacity, String filename) {
		this.capacity = capacity;
		this.occupied = 0;
		this.map = (LinkedList<Node<String>>[]) new LinkedList[capacity];
		this.lat = 4;
		this.lon = 24;

		// initialize HashTable from csv file
		try {
			String splitBy = ",";
			File myFile = new File(filename);
			Scanner myReader = new Scanner(myFile);
			while(myReader.hasNextLine()) {
				String[] data = myReader.nextLine().split(splitBy);
				// System.out.println(data[0]);
				Node<String> test = new Node<String>(data[0], Double.valueOf(data[1]), Double.valueOf(data[2]), Double.valueOf(data[3]), Double.valueOf(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]), Integer.parseInt(data[7]), Integer.parseInt(data[8]));
				put(test);
				// System.out.println(test.toString());
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("No trail found");
		}
	}

	/**
	 * It allows user to updatethe GPS location
	 * @para lat lon GPS location (0-60)
	 */
	public void updateCurrent(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
		return;
	}

	/**
	 * This function return the number of stored node
	 * @return number of occupied
	 */
	public int size() {
		return this.occupied;
	}
	
	/**
	 * This function add new trail Node to the HashTable
	 * It calls helpful expand() if loading capacity is greater than 0.85
	 * @para new trail Node
	 */
	@SuppressWarnings("unchecked")
	public boolean put(Node<String> trail) {
		Double s = Double.valueOf(this.occupied)/Double.valueOf(this.capacity);
		if (s >= 0.85) {
			expand();
		}
		int index = Math.abs(trail.getKey().hashCode()%this.capacity);
		// check if list at index is initalized
		if (this.map[index] != null) {
			// check if trail has already been added
			if (containsKey(trail.getKey())) {
				return false;
			}
			map[index].add(trail);
			this.occupied ++;
			return true;
		} else {
			LinkedList<Node<String>> list = new LinkedList<Node<String>>();
			list.add(trail);
			this.map[index] = list;
			this.occupied ++;
			return true;
		}
	}

	/**
	 * This is a private helper function that expand the HashTable
	 * by expand the size of LinkedList[], and remap individual nodes to new Hash index
	 */
	@SuppressWarnings("unchecked")
	private void expand(){
		// copy original to a tmp
		LinkedList<Node<String>>[] tmpMap = (LinkedList<Node<String>>[]) new LinkedList[capacity];
		for (int i = 0; i < map.length; i++) {
			tmpMap[i] = map[i];
		}
		// expand
		this.occupied = 0;
		this.capacity = this.capacity * 2;
		this.map = (LinkedList<Node<String>>[]) new LinkedList[this.capacity];

		// move from tmp to map
		for (int i = 0; i < tmpMap.length; i++) {
			if (tmpMap[i] != null){
				for (int j = 0; j < tmpMap[i].size(); j++) {
					put(tmpMap[i].get(j));
				}
			}
		}
		return;
	}

	/**
	 * This is a private function that checks if current HashTable contains the given key
	 * @para key
	 * @return true if contains, false if not
	 */
	private boolean containsKey(String key) {
		if (key == null) {
			return false;
		}
		// check if there is any list at the index
		int index = Math.abs(key.hashCode()%this.capacity);
		if (map[index] == null) {
			return false;
		}
		// check if key matches
		for (int i = 0; i < map[index].size(); i++) {
			if (map[index].get(i).getKey().equals(key)){
				return true;
			}
		}
		return false;
	}



	/**
	 * This is a function that removes node with given key name
	 * @para key
	 * @return true if removed, false if cannot
	 */
	public boolean remove(String key) {
		if (!containsKey(key)) {
			return false;
		}
		int index = Math.abs(key.hashCode()%this.capacity);
		for (int i = 0; i < this.map[index].size(); i++) {
			if (this.map[index].get(i).getKey().equals(key)){
				map[index].remove(i);
				this.occupied--;
				return true;
			}
		}
		return false;

	}

	/** 
	 * This is function that upate the review of the key with given rating
	 * @para key to be changed
	 * @para rating new rating
	 * @return true if updated, false if cannot
	 */
	public boolean updateReview(String key, int rating) {
		if (!containsKey(key)) {
			return false;
		}
		int index = Math.abs(key.hashCode()%this.capacity);
		for (int i = 0; i < this.map[index].size(); i++) {
			if (this.map[index].get(i).getKey().equals(key)){
				map[index].get(i).newReview(rating);
				return true;
			}
		}
		return false;
	}

	/** 
	 * This is function that report the review of the key with given rating
	 * @para key 
	 * @return rating
	 */
	public int getReview(String key) {
		if (!containsKey(key)) {
			throw new IllegalArgumentException("Illegal input");
		}
		int index = Math.abs(key.hashCode()%this.capacity);
		for (int i = 0; i < this.map[index].size(); i++) {
			if (this.map[index].get(i).getKey().equals(key)){
				return map[index].get(i).getReview();
			}
		}
		throw new IllegalArgumentException("Illegal input");
	}

	/**
	 * This is function that loop throught the whole HasthTable and 
	 * print out all trails currently stored
	 * @return total count of trails
	 */
	public int listAll () {
		int count = 0;
		for (int i = 0; i < this.map.length; i++) {
			if (this.map[i] != null){
				for (int j = 0; j < this.map[i].size(); j++) {
					count ++;
					System.out.println(this.map[i].get(j).toString());
				}
			}
		}
		return count;
	}

	/** 
	 * THis is function that only print out the list that qualifies the given limit
	 * @para limit upper limit of distance
	 * @para allow 1 if dog allowed, 0 if not
	 * @para driveLimit limit of distance bwtween GPS location
	 * @return count of how many qualifies
	 */
	public int filterList (double limit, int allow, double driveLimit) {
		int count = 0;
		for (int i = 0; i < this.map.length; i++) {
			if (this.map[i] != null){
				for (int j = 0; j < this.map[i].size(); j++) {
					//if ((this.map[i].get(j).getDog() <= allow)) {
					if ((this.map[i].get(j).getDog() <= allow) && (this.map[i].get(j).getDistance() <= limit) && (this.map[i].get(j).getDrive(this.lat, this.lon) <= driveLimit)) {
						count ++;
						System.out.println(this.map[i].get(j).toString());
					}
				}
			}
		}
		return count;
	}

	/**
	 * This is function that save the current nodes in the HashTable to a csv file
	 */
	public void saveToFile() {
		try {
			FileWriter myWriter = new FileWriter("trail.csv");
			for (int i = 0; i < this.map.length; i++) {
				if (this.map[i] != null){
					for (int j = 0; j < this.map[i].size(); j++) {
						myWriter.write(this.map[i].get(j).toCSV());
						// System.out.println(this.map[i].get(j).toCSV());
						// System.out.println(this.map[i].get(j).getDrive(4,24));
						// System.out.println(filterDistance(this.map[i].get(j),2));
						// System.out.println(filterDistance(this.map[i].get(j),2));
					}
				}
			}
			myWriter.close();
			System.out.println("Change saved");
		} catch (IOException e) {
			System.out.println("An error occurred.");
		}
		return;
	}

	/**
	 * This is a static function that prompt for user input action
	 * @return integer corresponding to action
	 */
	public static int getAction() {
		Scanner myObj = new Scanner(System.in);
		System.out.println("1: Update review \n2: List All Trail \n3: Filter \n4: Remove\n5: Add new trail\n6: Update my location\nType any other key to quite\n");
		String input = myObj.nextLine();
		try {
			if ((Integer.parseInt(input) >= 1) && (Integer.parseInt(input) <= 6)) {
				return (Integer.parseInt(input));
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;
	}

	/**
	 * Main front end function
	 * It first load Nodes of trails from csv files
	 * It then promopt for user action
	 * Action will be prompted for user responses
	 * If user input is invalid, error will be throw
	 * If at main menu, the current nodes will be saved, and quit the program
	 */
	public static void main(String[] args) {
		// Initialize HashTable of trails from csv file
		TrailTrack trails = new TrailTrack(10, "trail.csv");
		System.out.println(trails.size());

		// prompt for action
		int action = getAction();
		while (action != 0) {
			if (action == 1) {
				Scanner myObj = new Scanner(System.in);
				System.out.println("Please enter trail name:\n");
				String input = myObj.nextLine();
				if (trails.containsKey(input)) {
					System.out.println("Please enter your rating (1-5):\n");
					String rating = myObj.nextLine();
					try {
						if ((Integer.parseInt(rating) >= 1) && (Integer.parseInt(rating) <= 5)) {
							trails.updateReview(input, Integer.parseInt(rating));
							System.out.println("Review updated. \n");
						}
					} catch (Exception e) {
						System.out.println("Invalid rating. \n");
					}
				} else {
					System.out.println("No such trail.\n");
				}
				
			}
			if (action == 2) {
				trails.listAll();
			}
			if (action == 3) {
				Scanner myObj = new Scanner(System.in);
				System.out.println("Filter by distance (1-10/n):\n");
				String input1 = myObj.nextLine();
				try {
					double limit;
					if (input1.equals("n")) {
						limit = 100;
					} else if (Double.valueOf(input1) >= 0 && Double.valueOf(input1) <= 10) {
						limit = Double.valueOf(input1);
					} else {
						throw new IllegalArgumentException("Illegal input");
					}
					System.out.println("Dog allows (y/n):\n");
					int allow;
					String input2 = myObj.nextLine();
					if (input2.equals("y")) {
						allow = 1;
					} else if (input2.equals("n")) {
						allow = 0;
					} else {
						throw new IllegalArgumentException("Illegal input");
					}
					System.out.println("Filter by distance (1-10/n):\n");
					String input3 = myObj.nextLine();
					double driveLimit;
					if (input3.equals("n")) {
						driveLimit = 100;
					} else if (Double.valueOf(input3) >= 0 && Double.valueOf(input3) <= 10) {
						driveLimit = Double.valueOf(input3);
					} else {
						throw new IllegalArgumentException("Illegal input");
					}
					trails.filterList(limit, allow, driveLimit);
				} catch (Exception e) {
					System.out.println("Invalid input. \n");
				}
			}
			if (action == 4) {
				Scanner myObj = new Scanner(System.in);
				System.out.println("Please enter trail name that you want to remove:\n");
				String input = myObj.nextLine();
				if (trails.containsKey(input)) {
					trails.remove(input);
					System.out.println("Removed successfully.\n");

				} else {
					System.out.println("No such trail.\n");
				}

			}
			if (action == 5) {
				Scanner myObj = new Scanner(System.in);
				System.out.println("Please enter new trail information:\n");
				System.out.println("TrailName Disctance(miles,0-100),time(hour),latitude(0-60),longitude(0-60),difficulty(1-3),rate(0-5),loop(1/0),dog(1/0)\n");
				String input = myObj.nextLine();
				String[] data = input.split(",");
				// check for number
				try {
					if (trails.containsKey(data[0])) {
						throw new IllegalArgumentException("Duplicate trail");
					}
					if (Double.valueOf(data[1]) <= 0 || Double.valueOf(data[1]) > 100) {
						throw new IllegalArgumentException("Illegal input distance");
					} 
					if (Double.valueOf(data[2]) <= 0) {
						throw new IllegalArgumentException("Illegal input time");
					}
					if (Double.valueOf(data[3]) < 0 || Double.valueOf(data[3]) > 60) {
						throw new IllegalArgumentException("Illegal input lat");
					}
					if (Double.valueOf(data[4]) < 0 || Double.valueOf(data[4]) > 60) {
						throw new IllegalArgumentException("Illegal input lon");
					}
					if (Integer.parseInt(data[5]) <= 0 || Integer.parseInt(data[5]) > 3) {
						throw new IllegalArgumentException("Illegal input diffi");
					}
					if (Integer.parseInt(data[6]) < 0 || Integer.parseInt(data[6]) > 5) {
						throw new IllegalArgumentException("Illegal input rating");
					}
					if (Integer.parseInt(data[7]) < 0 || Integer.parseInt(data[7]) > 1) {
						throw new IllegalArgumentException("Illegal input loop ");
					}
					if (Integer.parseInt(data[8]) < 0 || Integer.parseInt(data[8]) > 1) {
						throw new IllegalArgumentException("Illegal input dog");
					}
					Node<String> test = new Node<String>(data[0], Double.valueOf(data[1]), Double.valueOf(data[2]), Double.valueOf(data[3]), Double.valueOf(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]), Integer.parseInt(data[7]), Integer.parseInt(data[8]));
					trails.put(test);
					System.out.println("New trail added");
					System.out.println(test.toString());
				} catch (Exception e) {
					System.out.println("Invalid input. \n");
				}
			}
			if (action == 6) {
				Scanner myObj = new Scanner(System.in);
				System.out.println("Please enter new GPS location in N43 W89:\n");
				System.out.println("latitude(0-60) longitude(0-60)\n");
				String input = myObj.nextLine();
				String[] data = input.split(" ");
				// check for number
				try {
					if (Double.valueOf(data[0]) < 0 || Double.valueOf(data[0]) > 60) {
						throw new IllegalArgumentException("Illegal input lat");
					}
					if (Double.valueOf(data[1]) < 0 || Double.valueOf(data[1]) > 60) {
						throw new IllegalArgumentException("Illegal input lon");
					}
					trails.lat = Double.valueOf(data[0]);
					trails.lon = Double.valueOf(data[1]);
					System.out.println("Current location added");
				} catch (Exception e) {
					System.out.println("Invalid input. \n");
				}
			}
			action = getAction();
		}
		// save trails to file, when exit the program
		trails.saveToFile();
		return;
	}
}

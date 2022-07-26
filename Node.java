// --== CS400 File Header Information ==--
// Name: Lucy Jiang
// Email: jiang68@wisc.edu
// Notes to Grader: <optional extra notes>

import java.lang.Math;

/**
 * This class is to initiate Node class that stores information about individual trails
 */ 
public class Node<KeyType> {
	// stored data
	private KeyType key;
	private double distance;
	private double time;
	private double latitude;
	private double longitude;
	private int loop;
	private int dog;
	private int difficulty;
	private int rate;

	/* constructor to make a new Node trail from given information
	 * @para key Name of the trail
	 * @para distance Distance of the trail
	 * @para time Time to finish trail 
	 * @para latitude longitude GPS location  (0-40)
	 * @para difficulty integer input of difficulty level (1-3)
	 * @para rate integer input of difficulty level (0-5)
	 * @para loop 1 if loop, 0 if not
	 * @para dog 1 if dog allowed, 0 if not
	 */
	public Node(KeyType key, double distance, double time, double latitude, double longitude, int difficulty, int rate, int loop, int dog) {
		this.key = key;
		this.distance = distance;
		this.time = time;
		this.latitude = latitude;
		this.longitude = longitude;
		this.difficulty = difficulty;
		this.rate = rate;
		this.loop = loop;
		this.dog = dog;
	}

	/**
	 * function that returns information of each trails in a human readable format
	 * @return output the string representation of the node
	 */
	public String toString() {
		String output = String.valueOf(this.key) + " is approximately "+ String.valueOf(this.distance)+ " miles long, which takes about "+ String.valueOf(this.time)+ " hours to finish.\n" ;
		if (this.difficulty == 1) {
			output = output.concat("This route is easy in terms difficulty level. ");
		} else if (this.difficulty == 2) {
			output = output.concat("This route is moderate in terms difficulty level. ");
		} else {
			output = output.concat("This route is hard in terms difficulty level. ");
		}
		// append rating
		if (this.rate == 0) {
			output = output.concat("You haven't rate this trail.\n");
		} else {
			output = output.concat("You rated this trail: ").concat(String.valueOf(this.rate)).concat(".\n");
		}
		// append aditional information about the trail
		if (this.loop == 1) {
			output = output.concat("This route is a loop.\n");
		}
		if (this.dog == 1) {
			output = output.concat("Alert: It also allows dog.\n");
		}
		return  output;
	}

	/**
	 * function that returns information of each trails in csv format
	 * @return output the string representation of the node in csv format
	 */
	public String toCSV() {
		String output = String.valueOf(this.key)+","+String.valueOf(this.distance)+","+String.valueOf(this.time)+","+String.valueOf(this.latitude)+","+String.valueOf(this.longitude)+","+String.valueOf(difficulty)+","+String.valueOf(this.rate)+","+String.valueOf(this.loop)+","+String.valueOf(this.dog)+",\n";
		return output;
	}

	/**
	 * function that  update review of the node
	 * @para rate new rate given (0-5)
	 */
	public void newReview(int rate){
		this.rate = rate;
	}

	/**
	 * function that returns the key of the node
	 * @return key of the current node
	 */
	public KeyType getKey() {
		return this.key;
	}

	/**
	 * function that returns the review
	 * @return review from the node
	 */
	public int getReview() {
		return this.rate;
	}

	/**
	 * function that returns the distance from the node
	 * @return distance from the node
	 */
	public double getDistance() {
		return this.distance;
	}

	/**
	 * function that returns if dog is allowed
	 * @return 1 allowed, 0 not
	 */
	public int getDog() {
		return this.dog;
	}

	/**
	 * function that calculate the distance of the trail from a given GPS
	 * @para lat lon GPS location (0-40)
	 * @return distance from location calculated
	 */
	public double getDrive(double lat, double lon) {
		return (Math.sqrt(Math.pow((this.latitude - lat),2) + Math.pow((this.longitude - lon),2)));
	}
}

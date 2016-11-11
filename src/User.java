import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Digital Wallet
 * User.java
 * This class creates unique object for each PayMo user
 * Each user has a field of id and a set of friends
 * 
 * @author Lily
 */
public class User {

	private String userID;
	private Set<User> friends;
	
	/**
	 * Constructor for User class
	 * 
	 * @param id: the User ID
	 */
	public User(String id){
		this.userID = id;
		this.friends = new HashSet<User>();
	}
	
	/**
	 * Method to implements algorithm to determine whether the payment with user b is trusted
	 * 
	 * @param user: Another user to make transaction with
	 * @param maxDegree: Maximum level of friends network
	 * @return True if the payment is trusted, false if the payment is unverified
	 */
	public boolean isTrusted(User user, int maxDegree){
		if (getID().equals(user.getID())) {
			return true;
		}
		Set<User> visited = new HashSet<User>();
		Set<User> start = new HashSet<User>();
		Set<User> target = new HashSet<User>();
		start.add(this);
		target.add(user);
		
		int degree = 1;
		while(degree <= maxDegree) {
			// Always make the start set size smaller than target, improves efficiency
			if (start.size() > target.size()) {
				Set<User> temp = start;
				start = target;
				target = temp;
			}
			if (target.isEmpty()) {
				return false;
			}
			Set<User> newStart = new HashSet<User>();
			for (User friend : start){
				for (User f : friend.getFriends()) {
					if (target.contains(f)) {
						return true;
					}
					if (!visited.contains(f)) {
						visited.add(f);
						newStart.add(f);
					}
				}
			}
			start = newStart;
			degree++;
		}
		return false;
	}
	
	/**
	 * This method enables modify user ID
	 */
	public void setID(String id){
		this.userID = id;
	}
	
	/**
	 * Add user to friends set if it is not already present
	 */
	public void addFriend(User user) {
		if(this.getID().equals(user.getID())) {
			return;
		}
		this.friends.add(user);
	}
	
	/**
	 * This method get user ID
	 */
	public String getID(){
		return this.userID;
	}
	
	/**
	 * This method get set of friends for a perticular user
	 */
	public Set<User> getFriends() {
		return this.friends;
	}
}

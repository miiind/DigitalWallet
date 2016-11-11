import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Digital Wallet
 * UserPool.java
 * This class creates user network and deals with file input/output
 * 
 * @author Lily
 */
public class UserPool {

	private Map<String, User> userPool;
	private static final String TRUSTED = "trusted";
	private static final String UNVERIFIED = "unverified";

	/**
	 * Constructor that initializes user network from an existing user pool,
	 * save the effort of processing past data repeatedly
	 * 
	 * @param pool: an existing user network
	 */
    public UserPool(UserPool pool) {
        this.userPool = new HashMap<>(pool.getPool());
    }
    
    /**
     * Constructor that 
     * 
     * @param fileName: path to the input data file
     */
	public UserPool(String fileName) {
		userPool = new HashMap<String, User>();
		init(fileName);
	}
	
	/**
	 * Build initial user network from input file
	 * 
	 * @param fileName: path to the input data file
	 */
	public void init(String fileName) {
		try {
			File file = new File(fileName);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			String line = br.readLine(); // ignore the header line
			line = br.readLine();
			while (line != null) {
				
				String[] records = line.split(",");
				// ignore records that are incomplete or have wrong format
				if (records.length < 4) {
					line = br.readLine();
					continue;
				}
				
				String user1 = records[1].trim();
				String user2 = records[2].trim();
				if (!userPool.containsKey(user1)) {
					User u1 = new User(user1);
					userPool.put(user1, u1);
				}
				if (!userPool.containsKey(user2)) {
					User u2 = new User(user2);
					userPool.put(user2, u2);
				}
				getUser(user1).addFriend(getUser(user2));
				getUser(user2).addFriend(getUser(user1));						
				line = br.readLine();		
			}

			br.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method gets user network
	 */
    public Map<String, User> getPool(){
        return this.userPool;
    }
    
    /**
     * This method finds the user with given id
     * 
     * @param key: user ID
     * @return user
     */
	public User getUser(String key) {
		return userPool.get(key);
	}
	
	/**
	 * This method determines possibility of fraud in transactions,
	 * and triggers warning or verified info in output file
	 * 
	 * @param inputFile: path to transaction file that needs to be analyzed
	 * @param outputFile: path to output file 
	 * @param maxDegree: the max degree of friend can be trusted in network
	 */
	public void writeFile(String inputFile, String outputFile, int maxDegree) {
		File input = new File(inputFile);
		File output = new File(outputFile);
		try {
			FileReader fr = new FileReader(input);
			FileWriter fw = new FileWriter(output);
			BufferedReader reader = new BufferedReader(fr);
			BufferedWriter writer = new BufferedWriter(fw);
			String rline = reader.readLine();  // ignore the header line
			rline = reader.readLine();
			while (rline != null) {
				String[] records = rline.split(",");
				// in case there are wrong formatted records
				if (records.length < 4) {
					rline = reader.readLine();
					continue;
				}
				String payer = records[1].trim();
				String receiver = records[2].trim();
				boolean isValid;
				if (userPool.containsKey(payer) && userPool.containsKey(receiver)) {
					isValid = this.getUser(payer).isTrusted(this.getUser(receiver), maxDegree);
				} else {
					isValid = false;
					if (!userPool.containsKey(payer)){
						userPool.put(payer, new User(payer));
					} 
					if (!userPool.containsKey(receiver)){
						userPool.put(receiver, new User(receiver));
					} 	
				}
				if (isValid) {
					writer.write(TRUSTED);
					writer.newLine();
				} else {
					this.getUser(payer).addFriend(this.getUser(receiver));
					this.getUser(receiver).addFriend(this.getUser(payer));
					writer.write(UNVERIFIED);
					writer.newLine();
				}
				rline = reader.readLine();
			}
			reader.close();
			writer.close();
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}

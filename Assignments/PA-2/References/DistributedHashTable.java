/* FILE: DistributedHashTable.java
 * USEAGE: --
 * DESCRIPTION: 
 * REQUIREMENTS: -- 
 * BUGS: -- 
 * AUTHOR: xXxSpicyBoiiixXx (Md Ali)
 * ORGANIZATION: IIT
 * VERSION: 1.0
 * CREATED: 10/17/2020
 * REVISION: -- 
*/

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/***
 * The class DistributedHashTable defines the methods and structures (variables) which maintains the Hash Table in the network.
 */
public class DistributedHashTable {
	
	// <KEY, VALUE> Hash Table
	private static ConcurrentHashMap<String, String> hashTable = new ConcurrentHashMap<String, String>();
	
	// <PEER_IP, <KEY, VALUE>> Hash Table
	private static ConcurrentHashMap<String, HashMap<String, String>> replicatedHashTable = new ConcurrentHashMap<String, HashMap<String, String>>();
	
	// Database
	private static ConcurrentHashMap<String, ArrayList<String>> indexDatabase = new ConcurrentHashMap<String, ArrayList<String>>();
	
	// Peer Locations
	private static ConcurrentHashMap<String, ArrayList<String>> peerIndexedLocations = new ConcurrentHashMap<String, ArrayList<String>>();
	
	// Replicated Nodes
	private static List<String> replicationNodes = Collections.synchronizedList(new ArrayList<String>());
	
	
	private static HashMap<Integer, String> networkMap = new HashMap<Integer, String>();
	private static ArrayList<String> replicationNodes = new ArrayList<String>();
	
	private static final String REPLICA_LOCATION = "replica/";
	
	private static final int SERVER_SOCKET_PORT = 10000;
	private static final int PEER_SERVER_PORT = 11000;
	private static final String LOCAL_ADDRESS = NetworkUtility.getLocalAddress();
	
	// Counter for peers connected
	private static int totalPeers = 0;
	
	/**
	 *  This methods adds a (KEY,VALUE) pair in the Distributed Hash Table (hashTable) if the KEY is not already present.
	 *  key		KEY should be 24 bytes (12 characters) maximum.
	 *  value		VALUE should be 1000 bytes (500 characters) maximum.
	 *  If confirm = true, then KEY check is not done and the (KEY,VALUE) pair is inserted to the hashTable even if the KEY already exists.
	 * 	In this case, the old value of the KEY is replaced by the new VALUE.
	 * 	Returns true if KEY is added in the hashTable successfully else returns false if a VALUE with the KEY already exists.
	 */
	public static boolean putInHashTable(String key, String value, boolean confirm) {
		if (confirm || !hashTable.containsKey(key)) {
			hashTable.put(key, value);
			return true;
		} else {
			return false;
		}
	}
	
	/***
	 * This methods retrieves the VALUE of the KEY from the Distributed Hash Table (hashTable).
	 * @param key	KEY which is to be searched in the hashTable.
	 * @return		Returns VALUE for the KEY specified if the KEY exist in the hashTable else returns NULL.
	 */
	public static String getFromHashTable(String key) {
		return hashTable.get(key);
	}
	
	/***
	 * This methods deletes a (KEY,VALUE) pair from the Distributed Hash Table (hashTable) using KEY. It does nothing if the KEY doesn't exist in the hashTable.
	 * @param key	KEY of the (KEY,VALUE) pair which has to be deleted from the hashTable.
	 */
	public static void removeFromHashTable(String key) {
		hashTable.remove(key);
	}
	
	/**
	 * This methods adds a (KEY,VALUE) pair in the Replication Hash Table (replicatedHashTable).
	 * @param nodeAddress	IP address of the peer(node) whose hashTable has to be replicated and kept the (KEY, VALUE) pairs in replicatedHashTable.
	 * @param key		KEY should be 24 bytes (12 characters) maximum.
	 * @param value		VALUE should be 1000 bytes (500 characters) maximum.
	 */
	public static void putInReplicaHashTable(String nodeAddress, String key, String value) {
		if (replicatedHashTable.containsKey(nodeAddress)) {
			HashMap<String, String> innerMap = replicatedHashTable.get(nodeAddress);
			innerMap.put(key, value);
		} else {
			HashMap<String, String> innerMap = new HashMap<String, String>();
			innerMap.put(key, value);
			replicatedHashTable.put(nodeAddress, innerMap);
		}
	}
	
	/***
	 * This methods retrieves the VALUE of the KEY from the Replication Hash Table (replicatedHashTable).
	 * @param key	KEY which is to be searched in the replicatedHashTable.
	 * @return		Returns VALUE for the KEY specified if the KEY exist in the replicatedHashTable else returns NULL.
	 */
	public static String getFromReplicaHashTable(String key) {
		String value = null;
		
		for (Map.Entry<String, HashMap<String, String>> record : replicatedHashTable.entrySet()) {
			HashMap<String, String> innerMap = replicatedHashTable.get(record.getKey().toString());
			value = innerMap.get(key);
			if (value != null) {
				break;
			}
		}
		return value;
	}
	
	/***
	 * This methods deletes a (KEY,VALUE) pair from the Replication Hash Table (replicatedHashTable) using KEY. It does nothing if the KEY doesn't exist in the replicatedHashTable.
	 * @nodeAddress	IP address of the peer(node) whose (KEY, VALUE) pair is to be deleted from the replicatedHashTable.
	 * @param key	KEY of the (KEY,VALUE) pair which has to be deleted from the replicatedHashTable.
	 */
	public static void removeFromReplicaHashTable(String nodeAddress, String key) {
		HashMap<String, String> innerMap = replicatedHashTable.get(nodeAddress);
		if (innerMap != null) {
			innerMap.remove(key);
		}
	}

	/***
	 * This method returns the replicatedHashTable which has the replica of the Hash Tables of all the peers in the network.
	 * @return	Returns replicatedHashTable of type ConcurrentHashMap
	 */
	public static ConcurrentHashMap<String, HashMap<String, String>> getReplicatedHashTable() {
		return replicatedHashTable;
	}

	/***
	 * This method sets the replicatedHashTable which has the replica of the Hash Tables of all the peers in the network.
	 * @param replicatedHashTable	ConcurrentHashMap structure containing IP address and hash table of all the peers in the network.
	 */
	public static void setReplicatedHashTable(ConcurrentHashMap<String, HashMap<String, String>> replicatedHashTable) {
		DistributedHashTable.replicatedHashTable = replicatedHashTable;
	}

	/***
	 * This method returns a list of nodes' IP address which are responsible for replication of hash tables.
	 * @return	Returns an ArrayList containing list of replication nodes.
	 */
	public static ArrayList<String> getReplicationNodes() {
		return replicationNodes;
	}

	/***
	 * This method returns the hashTable which stores the (KEY, VALUE) pairs of the calling Peer.
	 * @return	Returns hashTable of type ConcurrentHashMap
	 */
	public static ConcurrentHashMap<String, String> getHashTable() {
		return hashTable;
	}

	/***
	 * This method sets the hashTable which has the (KEY, VALUE) pairs of the calling Peer.
	 * @param hashTable ConcurrentHashMap containing (KEY, VALUE) pairs of the calling Peer. 
	 */
	public static void setHashTable(ConcurrentHashMap<String, String> hashTable) {
		DistributedHashTable.hashTable = hashTable;
	}
	
	/***
	 * This method returns a Map having (ID, IP ADDRESS) of the peers in the network.
	 * @return	Returns the HashMap which has the ID and IP address of all the Peers in the network.
	 */
	public static HashMap<Integer, String> getNetworkMap() {
		return networkMap;
	}

	/***
	 * This method returns the PORT number on which this application runs.
	 * @return	Returns the Port number which the Server is listening to.
	 */
	public static int getPeerServerPort() {
		return PEER_SERVER_PORT;
	}
	
	/***
	 * This method returns the IP Address of the Peer.
	 * @return	Returns the STRING IP Address of the Peer.
	 */
	public static String getLocalAddress() {
		return LOCAL_ADDRESS;
	}

	
	private static class Indexer extends Thread {
		private Socket socket;
		private int clientNumber;
		
		public Indexer(Socket socket, int clinetNumber) {
			
			this.socket = socket;
			this.clientNumber = clientNumber;
			print("\nNew connection with Peer # " + clientNumber + " at " + socket.getInetAddress());
			totalPeers++;
			print("Total numer of peers connected:" + totalPeers);
		}
	}
	
	public void run() {
        try {
        	// Initializing output stream using the socket's output stream
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            
            // Initializing input stream using the socket's input stream
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            String clientIp = socket.getInetAddress().getHostAddress();

            // Send a welcome message to the client
            Response response = new Response();
            response.setResponseCode(200);
            response.setResponseData("Hello, you are Peer #" + clientNumber + ".\nDo you want your node to act as a replication node? This requires your disk space to be large. (Y/N):");
            out.writeObject(response);
            
            Request peerRequest = (Request) in.readObject();
            String requestType = peerRequest.getRequestType();
            String replicaChoice = (String) peerRequest.getRequestData();
            
            if (replicaChoice.equalsIgnoreCase("Y")) {
            	System.out.println("Replication with this node accepted.");
            	
            	if (!replicationNodes.contains(clientIp)) {
            		replicationNodes.add(clientIp);
				}
            	
            	// Just to remind peer if he is acting as a replication node
            	if (peerIndexedLocations.containsKey(clientIp)) {
    				peerIndexedLocations.get(clientIp).add(REPLICA_LOCATION);
    			} else {
    				ArrayList<String> paths = new ArrayList<String>();
    	        	paths.add(REPLICA_LOCATION);
    	        	peerIndexedLocations.put(clientIp, paths);
    			}
            	
            	response = new Response();
            	response.setResponseCode(200);
                response.setResponseData(indexDatabase);
                out.writeObject(response);
			}
            
            response = new Response();
            response.setResponseCode(200);
            response.setResponseData(peerIndexedLocations.get(clientIp));
            out.writeObject(response);
            
            while(true) {
            	// Read the request object received from the Peer
            	peerRequest = (Request) in.readObject();
                requestType = peerRequest.getRequestType();
                
                if (requestType.equalsIgnoreCase("REGISTER")) {
                	// If Request Type = REGISTER, then call register(...) method to register the peer's files
                	ArrayList<String> indexedLocations = register(clientNumber, clientIp, (ArrayList<String>) peerRequest.getRequestData(), out);
                	response = new Response();
                	response.setResponseCode(200);
                    response.setResponseData(indexedLocations);
                    out.writeObject(response);
				} else if (requestType.equalsIgnoreCase("LOOKUP")) {
					print("\nLooking up a file.");
					String fileName = (String) peerRequest.getRequestData();
					
					// If Request Type = LOOKUP, then call search(...) method to search for the specified file
					print("Request from Peer # " + clientNumber + " (" + clientIp + ") to look for file " + fileName);
					HashMap<Integer, String> searchResults = search(fileName);
					
					// If file found then respond with all the peer locations that contain the file or else send File Not Found message
					if (searchResults.size() > 0) {
						response = new Response();
						response.setResponseCode(200);
						response.setResponseData(searchResults);
						out.writeObject(response);
						print("File Found.");
					} else {
						response = new Response();
						response.setResponseCode(404);
						response.setResponseData("File Not Found.");
						out.writeObject(response);
						print("File Not Found.");
					}
				} else if(requestType.equalsIgnoreCase("UNREGISTER")) {
					// If Request Type = UNREGISTER, then call unregister(...) method to remove all the files of the requested 
					// peer from the indexing server's database
					response = new Response();
					if (unregister(clientIp)) {
						response.setResponseCode(200);
						response.setResponseData("Your files have been un-registered from the indexing server.");
						print("Peer # " + clientNumber + " (" + clientIp + ") has un-registered all its files.");
					} else {
						response.setResponseCode(400);
						response.setResponseData("Error in un-registering files from the indexing server.");
					}
					out.writeObject(response);
				} else if(requestType.equalsIgnoreCase("GET_BACKUP_NODES")) {
					// Sends replication peers/nodes to the peer who is not able to download a file from its original peer.
					System.out.println("\n" + clientIp + " requested backup nodes info. Sending backup nodes info.");
					response = new Response();
					response.setResponseCode(200);
					response.setResponseData(replicationNodes);
					out.writeObject(response);
					System.out.println("Backup nodoes information sent.");
				} else if(requestType.equalsIgnoreCase("DISCONNECT")) {
					print("\nPeer # " + clientNumber + " disconnecting...");
					try {
						// Close the connection and then stop the thread.
	                    socket.close();
	                } catch (IOException e) {
	                    print("Couldn't close a socket.");
	                }
	                Thread.currentThread().interrupt();
	                break;
				}
            }
        } catch(EOFException e) {
        	Thread.currentThread().interrupt();
        } catch (Exception e) {
            print("Error handling Peer # " + clientNumber + ": " + e);
            Thread.currentThread().interrupt();
        }
    }
    
    // Stop thread once the peer has disconnected or some error has occurred in serving the peer.
    public void interrupt() {
    	print("\nConnection with Peer # " + clientNumber + " closed");
    	totalPeers--;
    	print("Total number of peers connected:" + totalPeers);
    	if (totalPeers == 0) {
    		print("No more peers connected.");
		}
    }
    
    private void print(String message) {
    	LogUtility log = new LogUtility("server");
    	log.write(message);
    	log.close();
        System.out.println(message);
    }
    
    private ArrayList<String> register(int peerId, String peerAddress, ArrayList<String> files, ObjectOutputStream out) throws IOException {
    	print("\nRegistering files from Peer " + peerAddress);
    	
    	// Appending HHmmss just to make the key unique because a single peer may register multiple times. We aren't using the last appended data.
    	String time = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
    	
    	// Retrieving path and storing them separately
    	if (peerIndexedLocations.containsKey(peerAddress)) {
			peerIndexedLocations.get(peerAddress).add(files.get(0));
		} else {
			ArrayList<String> paths = new ArrayList<String>();
        	paths.add(files.get(0));
        	peerIndexedLocations.put(peerAddress, paths);
		}
    	files.remove(0);
    	
    	// Using StringBuffer to avoid creation of multiple string objects while appending
    	StringBuffer sb = new StringBuffer();
    	sb.append(clientNumber).append("#").append(peerAddress).append("#").append(time);
        indexDatabase.put(sb.toString(), files);
        
        print(files.size() + " files synced with Peer " + clientNumber + " and added to index database");
        
        ConcurrentHashMap<String, ArrayList<String>> newFiles = new ConcurrentHashMap<String, ArrayList<String>>();
        newFiles.put(sb.toString(), files);
        sendReplicateCommand(newFiles);
        
        return peerIndexedLocations.get(peerAddress);
    }
    
    private boolean unregister(String peerAddress) throws IOException {
    	int oldSize = indexDatabase.size();
    	ArrayList<String> deleteFiles = null;
    	
    	for (Map.Entry e : indexDatabase.entrySet()) {
			String key = e.getKey().toString();
			ArrayList<String> value = (ArrayList<String>) e.getValue();
			
			if (key.contains(peerAddress)) {
				deleteFiles = indexDatabase.get(key);
				indexDatabase.remove(key);
			}
		}
    	int newSize = indexDatabase.size();
    	
    	
    	// Send request to delete the unregistered files from the replication node
    	if (newSize < oldSize) {
    		Request serverRequest = new Request();
        	Socket socket = null;
        	try {
        		serverRequest.setRequestType("DELETE_DATA");
            	for (String node : replicationNodes) {
            		socket = new Socket(node, PEER_SERVER_PORT);
            		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
    				serverRequest.setRequestData(deleteFiles);
    				out.writeObject(serverRequest);
    				out.close();
    				socket.close();
    			}
            	socket = null;
			} catch (Exception e) {
				print("Error in replication:" + e);
			} finally {
				serverRequest = null;
				if (socket != null && socket.isConnected()) {
					socket.close();
				}
			}
		}
    	
    	return (newSize < oldSize);
    }
    
    private HashMap<Integer, String> search(String fileName) {
    	HashMap<Integer, String> searchResults = new HashMap<Integer, String>();
		for (Map.Entry e : indexDatabase.entrySet()) {
			String key = e.getKey().toString();
			ArrayList<String> value = (ArrayList<String>) e.getValue();
			
			for (String file : value) {
				if (file.equalsIgnoreCase(fileName)) {
					int peerId = Integer.parseInt(key.split("#")[0].trim());
					String hostAddress = key.split("#")[1].trim();
					searchResults.put(peerId, hostAddress);
				}
			}
		}
		return searchResults;
    }

    private void sendReplicateCommand(ConcurrentHashMap<String, ArrayList<String>> newFiles) throws IOException {
    	Request serverRequest = new Request();
    	Socket socket = null;
    	try {
    		serverRequest.setRequestType("REPLICATE_DATA");
        	for (String node : replicationNodes) {
        		socket = new Socket(node, PEER_SERVER_PORT);
        		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				serverRequest.setRequestData(newFiles);
				out.writeObject(serverRequest);
				out.close();
				socket.close();
			}
        	socket = null;
		} catch (Exception e) {
			print("Error in replication:" + e);
		} finally {
			serverRequest = null;
			if (socket != null && socket.isConnected()) {
				socket.close();
			}
		}
    }
    public static void main(String[] args) throws IOException {
		FileInputStream fileStream = null;
		
		try {
			// Load network information from network.config file
			Properties configuration = new Properties();
			fileStream = new FileInputStream("network.config");
			configuration.load(fileStream);
			fileStream.close();
			
			
			// Reading nodes IP addresses from the configuration file
			String peerList = configuration.getProperty("NODES");
			
			if (peerList != null) {
				String[] peers = peerList.split(",");
				
				for (int i = 0; i < peers.length; i++) {
					if (IPAddressValidator.validate(peers[i].trim())) {
						networkMap.put(i + 1, peers[i].trim());
					} else {
						System.out.println("One of the Peer's IP address in the configuration file is invalid. Exiting...");
						System.exit(0);
					}					
				}
				
				if (networkMap.isEmpty()) {
					System.out.println("No nodes(peers) present in Configuration File. No nodes... No Distributed Hash Table... Bye...");
					System.exit(0);
				}
			} else {
				System.out.println("Configuration File is not as expected. Cannot run program. Bye...");
				System.exit(0);
			}
			
			// Loading replication nodes IP addresses from configuration file
			peerList = configuration.getProperty("REPLICATION_NODES");
			
			if (peerList != null) {
				String[] peers = peerList.split(",");
				
				for (int i = 0; i < peers.length; i++) {
					if (IPAddressValidator.validate(peers[i].trim())) {
						replicationNodes.add(peers[i].trim());
					} else {
						System.out.println("One of the Replication Peer's IP address in the configuration file is invalid. Exiting...");
						System.exit(0);
					}
				}
			}
			
			//System.out.println(networkMap);
			//System.out.println(replicationNodes);
		} catch (Exception e) {
			System.out.println("ERROR in Loading Configuration File. Cannot run program. Bye...");
			System.exit(0);
		} finally {
			try {
				if (fileStream != null) {
					fileStream.close();
				}
			} catch (Exception e2) { }
		}
		
		// Start a new Thread which acts as Client on Peer side
		System.out.println("...Peer client started...");
		PeerClient peerClient = new PeerClient();
		peerClient.start();
		private static class PeerServer extends Thread {
			private Socket socket;
			private LogUtility log = new LogUtility("peer");
			
	        public PeerServer(Socket socket) {
	            this.socket = socket;
	            log.write("File downloading with " + socket.getInetAddress() + " started.");
	        }
		
		
		/**
		 * Peer's server implementation. It runs in an infinite loop listening
		 * on port 20000. When a a file download is requested, it spawns a new
		 * thread to do the servicing and immediately returns to listening.
		 */
		System.out.println("...Peer indexing server started...");
		ServerSocket listener = new ServerSocket(SERVER_SOCKET_PORT);
		int peerId = 1;
		
        try {
            while (true) {
            	new Indexer(listener.accept(), peerID++.start());
            	PeerServer peerServer = new PeerServer(listener.accept());
               peerServer.start();
            }
        } finally {
            listener.close();
        }
	}
}
}



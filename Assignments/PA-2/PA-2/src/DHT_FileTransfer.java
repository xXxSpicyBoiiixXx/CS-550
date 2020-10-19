/* FILE: DHT_FileTransfer.java
 * USEAGE: --
 * DESCRIPTION: I made this not to be in one space as this was problematic so the DHT is in the network instead of one local peer.
 * OPTIONS: --
 * REQUIREMENTS: -- 
 * BUGS: -- 
 * AUTHOR: xXxSpicyBoiiixXx (Md Ali)
 * ORGANIZATION: IIT
 * VERSION: 1.0
 * CREATED: 10/02/2020
 * REVISION: -- 
*/

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class DHT_FileTransfer {
		// <FILE NAME, PEER ADDRESS> in the  Hash Table
		private static ConcurrentHashMap<String, String> hashTable = new ConcurrentHashMap<String, String>();
		
		// <PEER_IP, <FILE NAME, PEER ADDRESS>> in the Hash Table
		private static ConcurrentHashMap<String, HashMap<String, String>> replicatedHashTable = new ConcurrentHashMap<String, HashMap<String, String>>();
		
		// Maps the network
		private static HashMap<Integer, String> networkMap = new HashMap<Integer, String>();
		
		// Puts an array of replicated nodes
		private static ArrayList<String> replicationNodes = new ArrayList<String>();
		
		// Where files will be located and where the replica nodes will be
		private static String filesLocation = null;
		private static String replicaLocation = null;
		
		// Server port is set to 11000
		// The netwrok utility will get the local address automaticall but this is put into the network config file anyways. 
		private static final int PEER_SERVER_PORT = 11000;
		private static final String LOCAL_ADDRESS = NetworkUtility.getLocalAddress();
		
		// Adds a key, value. <FILENAME, IP> in the DHT if the key isn't here. 
		// Some restrictions are the the key can only be 12 char max and the value is 500 char max
		public static boolean putInHashTable(String key, String value, boolean confirm) {
			if (confirm || !hashTable.containsKey(key)) {
				hashTable.put(key, value);
				return true;
			} else {
				return false;
			}
		}
		
		// Retrieves the key/value pair from the DHT
		public static String getFromHashTable(String key) {
			return hashTable.get(key);
		}
		
		// Deletes the key/value pair from the DHT
		public static void removeFromHashTable(String key) {
			hashTable.remove(key);
		}
		
		// Adds key/value pair in the DHT replica
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
		
		// Retrives the key/value pair form the DHT replica
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
		
		// Deletes the key/value pair from the DHT replica
		public static void removeFromReplicaHashTable(String nodeAddress, String key) {
			HashMap<String, String> innerMap = replicatedHashTable.get(nodeAddress);
			if (innerMap != null) {
				innerMap.remove(key);
			}
		}

		// GIve all replica DHT
		public static ConcurrentHashMap<String, HashMap<String, String>> getReplicatedHashTable() {
			return replicatedHashTable;
		}

		// Seting Replica DHT
		public static void setReplicatedHashTable(ConcurrentHashMap<String, HashMap<String, String>> replicatedHashTable) {
			DHT_FileTransfer.replicatedHashTable = replicatedHashTable;
		}
		
		// Gives a list of all replica nodes 
		public static ArrayList<String> getReplicationNodes() {
			return replicationNodes;
		}

		// Give the conncurrent DHT
		public static ConcurrentHashMap<String, String> getHashTable() {
			return hashTable;
		}

		// DHT of calling peer
		public static void setHashTable(ConcurrentHashMap<String, String> hashTable) {
			DHT_FileTransfer.hashTable = hashTable;
		}
		
		// GIve network mapping of ID and IP
		public static HashMap<Integer, String> getNetworkMap() {
			return networkMap;
		}

		// Gets the peer port that is being used as a server. It should be 11000
		public static int getPeerServerPort() {
			return PEER_SERVER_PORT;
		}
		
		// Gets the local address of the peer
		public static String getLocalAddress() {
			return LOCAL_ADDRESS;
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
							System.out.println("One or more Peer's IP address in the netowrk config file is invalid. Please open other VMs or something of the sort....Exiting...");
							System.exit(0);
						}					
					}
					
					if (networkMap.isEmpty()) {
						System.out.println("No peers present in configuration file. No nodes... No Distributed Hash Table...Exiting....");
						System.exit(0);
					}
				} else {
					System.out.println("Configuration file is not as expected. Cannot run program. Exiting....");
					System.exit(0);
				}
				
				// Loading replication nodes IP addresses from config file
				peerList = configuration.getProperty("REPLICATION_NODES");
				
				if (peerList != null) {
					String[] peers = peerList.split(",");
					
					for (int i = 0; i < peers.length; i++) {
						if (IPAddressValidator.validate(peers[i].trim())) {
							replicationNodes.add(peers[i].trim());
						} else {
							System.out.println("One of the Replication Peer's IP address in the config file is invalid. Exiting...");
							System.exit(0);
						}
					}
				}
				
				// Read Files Location whose files are to be shared
				if (configuration.getProperty("FILES_LOCATION") != null) {
					filesLocation = configuration.getProperty("FILES_LOCATION");
				} else {
					filesLocation = "files/";
				}
				
				// Read Replica Files Location where all files are to be stored for replication purpose
				if (configuration.getProperty("REPLICA_LOCATION") != null) {
					replicaLocation = configuration.getProperty("REPLICA_LOCATION");
				} else {
					replicaLocation = "replica/";
				}

			} catch (Exception e) {
				System.out.println("Can't load config file...Exiting...");
				System.exit(0);
			} finally {
				try {
					if (fileStream != null) {
						fileStream.close();
					}
				} catch (Exception e2) { }
			}
			
			// Start a new Thread which acts as Client on Peer side
			System.out.println("...peer client started...");
			PeerClient peerClient = new PeerClient();
			peerClient.start();
			
			// Listening for clients on port 10000
			System.out.println("...peer server started....");
			ServerSocket listener = new ServerSocket(PEER_SERVER_PORT);
	        try {
	            while (true) {
	            	Server peerServer = new Server(listener.accept());
	               peerServer.start();
	            }
	        } finally {
	            listener.close();
	        }
		}
		
		// Gets File Locations 
		public static String getFilesLocation() {
			return filesLocation;
		}

		// Gets Replication Locations
		public static String getReplicaLocation() {
			return replicaLocation;
		}

}

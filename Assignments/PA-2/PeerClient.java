/* FILE: PeerClient.java
 * USEAGE: --
 * DESCRIPTION: This is the client but will also act as a server on port 11000 once all the files are registered with the main server.
 * OPTIONS: --
 * REQUIREMENTS: -- 
 * BUGS: -- 
 * AUTHOR: xXxSpicyBoiiixXx (Md Ali)
 * ORGANIZATION: IIT
 * VERSION: 1.1
 * CREATED: 10/01/2020
 * REVISION: -- 
*/

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PeerClient extends Thread {
	
	private HashMap<Integer, String> networkMap = null;
	private ArrayList<String> replicationNodes = null;
	
	private int portAddress = 0;
	private String localAddress = null;
	private String filesLocation = null;
	
	public PeerClient() {
		networkMap = DHT_FileTransfer.getNetworkMap();
		replicationNodes = DHT_FileTransfer.getReplicationNodes();
		portAddress = DHT_FileTransfer.getPeerServerPort();
		localAddress = DHT_FileTransfer.getLocalAddress();
		filesLocation = DHT_FileTransfer.getFilesLocation();
	}
	
	public void run() {
		BufferedReader input = null;
		
		try {
			input = new BufferedReader(new InputStreamReader(System.in));
			
			HashMap<String, String> hm = retrieveHashTable();
			if (hm != null) {
				for (Map.Entry e : hm.entrySet()) {
					DHT_FileTransfer.putInHashTable(e.getKey().toString(), e.getValue().toString(), true);
				}
			}
			
			if (replicationNodes.contains(localAddress)) {
				System.out.println("...replication service started....");
				ReplicationService service = new ReplicationService(null, null, "REPLICATE");
				service.start();
			}
	        
	        long startTime, endTime;
	        double time;
			
	        String key, value, confirm, fileName;
	        
	        while (true) {
	        	System.out.println("\nWhat do you want to do?");
		        System.out.println("1.Register a file with the peers.");
		        System.out.println("2.Search for a file.");
		        System.out.println("3.Un-register a file with the peers.");
		        System.out.println("4.Print log of this peer.");
		        System.out.println("5.Exit.");
		        System.out.print("Enter choice and press ENTER:");
		        int option;
		        
		        // Check if the user has entered only numbers.
		        try {
		        	option = Integer.parseInt(input.readLine());
				} catch (NumberFormatException e) {
					System.out.println("Wrong choice. Try again!!!");
					continue;
				}
		        
		        switch (option) {
				case 1:
					System.out.println("\nEnter name of the file with extension you want to put in file sharing system:");
					fileName = input.readLine();
					
					if(fileName.trim().length() == 0) {
						System.out.println("Invalid Filename.");
						continue;
					}
					
					File file = new File(filesLocation + fileName);
					
					if (file.exists()) {
						startTime = System.currentTimeMillis();

						if(put(fileName, localAddress)) {
							System.out.println(fileName + " added to the file sharing system and is available to download for other peers.");
						} else {
							System.out.println("Unable to add " + fileName + " to the file sharing system. Please try again later.");
						}
						
						endTime = System.currentTimeMillis();
						time = (double) Math.round(endTime - startTime) / 1000;
						System.out.println("Time Taken: " + time + " seconds.");
					} else {
						System.out.println("File with the given filename does not exist in [" + filesLocation + "] path.");
					}
					break;

				case 2:
					System.out.println("\nEnter name of the file you want to look for:");
					fileName = input.readLine();
					String hostAddress;
					
					startTime = System.currentTimeMillis();
					value = get(fileName);
					
					endTime = System.currentTimeMillis();
					time = (double) Math.round(endTime - startTime) / 1000;
					if (value != null) {
						System.out.println("File Found. Lookup time: " + time + " seconds.");
						hostAddress = value;
						if (fileName.trim().endsWith(".txt")) {
							System.out.print("\nDo you want to download (D) or print this file (P)? Enter (D/P):");
							String download = input.readLine();
							
							if (download.equalsIgnoreCase("D")) {
								System.out.println("The file will be downloaded in the 'downloads' folder in the current location.");
								obtain(hostAddress, portAddress, fileName);
							} else if (download.equalsIgnoreCase("P")) {
								obtain(hostAddress, portAddress, fileName);
								FileUtility.printFile(fileName);
							}
						} else {
							System.out.print("\nDo you want to download this file?(Y/N):");
							String download = input.readLine();
							if (download.equalsIgnoreCase("Y")) {
								obtain(hostAddress, portAddress, fileName);
							}	
						}
					}  else {
						System.out.println("File not found. Lookup time: " + time + " seconds.");
					}					
					break;
					
				case 3:
					System.out.println("\nEnter the name of the file you want remove from file sharing system:");
					key = input.readLine();
					
					// Validating key
					if (!validateKey(key))
						continue;

					System.out.print("\nAre you sure (Y/N)?:");
					confirm = input.readLine();
					
					if (confirm.equalsIgnoreCase("Y")) {
						startTime = System.currentTimeMillis();
						
						if (delete(key)) {
							System.out.println("The file was successfully removed from the file sharing system.");
						} else {
							System.out.println("There was an error in removing the file. Please try again later.");
						}
						
						endTime = System.currentTimeMillis();
						time = (double) Math.round(endTime - startTime) / 1000;
						System.out.println("Time taken: " + time + " seconds");
					}

					break;

				case 4:
					(new LogUtility("peer")).print();
					break;
		
				case 5:

					System.out.print("\nThe files shared by this peer will no longer be accessible by other peers in this network. Are you sure you want to exit? (Y/N)?:");
					confirm = input.readLine();
					
					if (confirm.equalsIgnoreCase("Y")) {
						System.out.println("Thanks for using this system.");
						System.exit(0);
					}
					
					break;
					
				default:
					System.out.println("Wrong choice. Try again!!!");
					break;
				}
	        }
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {

				if (input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Download stuff from the requested peer
	private void obtain(String hostAddress, int port, String fileName) {
		boolean isDownloaded = false;
		long startTime = System.currentTimeMillis();
		
		if (!FileUtility.downloadFile(hostAddress, port, fileName, false)) {
			List<String> backupNodes = DHT_FileTransfer.getReplicationNodes();
			
			for (String node : backupNodes) {
				if(FileUtility.downloadFile(node, port, fileName, true)) {
					isDownloaded = true;
					break;
				}
			}
		} else {
			isDownloaded = true;
		}
		
		long endTime = System.currentTimeMillis();
		double time = (double) Math.round(endTime - startTime) / 1000;

		if (isDownloaded) {
			System.out.println("File downloaded successfully in " + time + " seconds.");
		} else {
			System.out.println("Unable to connect to the host. Unable to  download file. Try using a different peer if available.");
		}
	}
	
	// Get DHT from other nodes
	private HashMap<String, String> retrieveHashTable() {
		Socket socket = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		Request peerRequest = null;
		Response serverResponse	= null;
		HashMap<String, String> hm = null;
		
		for (String nodeAddress : replicationNodes) {
			if (nodeAddress.equalsIgnoreCase(localAddress)) {
				continue;
			}
			
			try {

				socket = new Socket(nodeAddress, portAddress);

			
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();

				in = new ObjectInputStream(socket.getInputStream());

				peerRequest = new Request();
				peerRequest.setRequestType("GET_R_HASHTABLE");
				out.writeObject(peerRequest);

				// Read the response message from the server
				serverResponse = (Response) in.readObject();
				if (serverResponse != null && serverResponse.getResponseCode() == 200) {
					hm = (HashMap<String, String>) serverResponse.getResponseData();
				} 
					
				socket.close();
				break;
			} catch (Exception ex) {
				
			} finally {
				try {

					if (out != null)
						out.close();

					if (in != null)
						in.close();

					if (socket != null)
						socket.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	
		return hm;
	}
	
	private boolean put(String key, String value) {
		Socket socket = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		Request peerRequest = null;
		Response serverResponse	= null;
		String data = key + "," + value;
		
		long startTime, endTime;
	    double time;
	        
		try {
			
			int node = hash(key);
			String nodeAddress = networkMap.get(node);
			
			if (nodeAddress.equals(localAddress)) {
			
				boolean result = DHT_FileTransfer.getHashTable().containsKey(key);
				
				if (result) {
					System.out.print("\nA file with the same name is already registered by this peer. Would you like to overwrite it? (Y/N): ");
					String confirm = (new BufferedReader(new InputStreamReader(System.in))).readLine();
					
					if (confirm.equalsIgnoreCase("Y")) {
						DHT_FileTransfer.putInHashTable(key, value, true);
						
						ReplicationService service = new ReplicationService(key, value, "REGISTER");
						service.start();
						return true;
					}
				} else {
					DHT_FileTransfer.putInHashTable(key, value, true);
					
					ReplicationService service = new ReplicationService(key, value, "REGISTER");
					service.start();
					return true;
				}
			} else {
			
		        socket = new Socket(nodeAddress, portAddress);
		       
		        out = new ObjectOutputStream(socket.getOutputStream());
		        out.flush();
		        
		   
		        in = new ObjectInputStream(socket.getInputStream());
	
				peerRequest = new Request();
				peerRequest.setRequestType("REGISTER");
				peerRequest.setRequestData(data);
				out.writeObject(peerRequest);
			
		        serverResponse = (Response) in.readObject();
		        
		        if (serverResponse.getResponseCode() == 200) {
		        
					
					return true;
				} else if (serverResponse.getResponseCode() == 300) {
					System.out.print("\nA VALUE with the specified KEY already exists in  the Distributed Hash Table. Would you like to overwrite it? (Y/N): ");
					String confirm = (new BufferedReader(new InputStreamReader(System.in))).readLine();
					
					if (confirm.equalsIgnoreCase("Y")) {
						return forcePut(key, value);
					}
				} else {
					
					return false;
				}
			}
		} catch(Exception e) {
		
		} finally {
			try { 
				if (out != null)
					out.close();
				
				if (in != null)
					in.close();
				
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	private boolean forcePut(String key, String value) {
		Socket socket = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		Request peerRequest = null;
		Response serverResponse	= null;
		String data = key + "," + value;
		
		long startTime, endTime;
	    double time;
	        
		try {
			startTime = System.currentTimeMillis();
			
			int node = hash(key);
			String nodeAddress = networkMap.get(node);
			
	        socket = new Socket(nodeAddress, portAddress);
	        
	        out = new ObjectOutputStream(socket.getOutputStream());
	        out.flush();
	        
	  
	        in = new ObjectInputStream(socket.getInputStream());

		
			peerRequest = new Request();
			peerRequest.setRequestType("REGISTER_FORCE");
			peerRequest.setRequestData(data);
			out.writeObject(peerRequest);
			
	        // Read the response message from the server
	        serverResponse = (Response) in.readObject();
	        
	        if (serverResponse.getResponseCode() == 200) {
	        	endTime = System.currentTimeMillis();
				time = (double) Math.round(endTime - startTime) / 1000;
				System.out.println("Time taken: " + time + " seconds");
				
				return true;
			} else {
				System.out.println(serverResponse.getResponseData());
				return false;
			}
	        
		} catch(Exception e) {

		} finally {
			try {
			
				if (out != null)
					out.close();
				
				if (in != null)
					in.close();
				
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}

	private String get(String key) {
		Socket socket = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		Request peerRequest = null;
		Response serverResponse	= null;
		String value = null;
		String nodeAddress = null;
		
		try {
			int node = hash(key);
			nodeAddress = networkMap.get(node);
			
		
			if (nodeAddress.equals(localAddress)) {
			
				value = FileTransferSystem.getFromHashTable(key);
			} else {
				
		        socket = new Socket(nodeAddress, portAddress);
		     
		        out = new ObjectOutputStream(socket.getOutputStream());
		        out.flush();
		        
		        in = new ObjectInputStream(socket.getInputStream());

				
				peerRequest = new Request();
				peerRequest.setRequestType("LOOKUP");
				peerRequest.setRequestData(key);
				out.writeObject(peerRequest);
				
		        serverResponse = (Response) in.readObject();
		        
		        if (serverResponse.getResponseCode() == 200) {
					value = serverResponse.getResponseData().toString().split(",")[1].trim();
				}
			}
		} catch(Exception e) {
		
			value = searchReplica(key, nodeAddress);
		} finally {
			try {
				 
				if (out != null)
					out.close();
				
				if (in != null)
					in.close();
				
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return value;
	}
	
	private String searchReplica(String key, String peerAddress) {
		Socket socket = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		Request peerRequest = null;
		Response serverResponse	= null;
		String value = null;
		
		for (String nodeAddress : replicationNodes) {
			try {
			
				if (nodeAddress.equals(localAddress)) {
		
					value = DHT_FileTransfer.getFromReplicaHashTable(key);
				} else {
				
			        socket = new Socket(nodeAddress, portAddress);
			        
			        out = new ObjectOutputStream(socket.getOutputStream());
			        out.flush();
			        
			        in = new ObjectInputStream(socket.getInputStream());

					peerRequest = new Request();
					peerRequest.setRequestType("R_LOOKUP");
					peerRequest.setRequestData(key);
					out.writeObject(peerRequest);
					
			        serverResponse = (Response) in.readObject();
			        
			        if (serverResponse.getResponseCode() == 200) {
						value = serverResponse.getResponseData().toString().split(",")[1].trim();
					}
				}
			} catch(Exception ex) {
				
			} finally {
				try {
					
					if (out != null)
						out.close();
					
					if (in != null)
						in.close();
					
					if (socket != null)
						socket.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			
			if (value != null) {
				break;
			}
		}
		return value;
	}
	

	private boolean delete(String key) {
		Socket socket = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		Request peerRequest = null;
		Response serverResponse	= null;
		
		try {
			int node = hash(key);
			String nodeAddress = networkMap.get(node);
			
	
	        socket = new Socket(nodeAddress, portAddress);
	        
	        out = new ObjectOutputStream(socket.getOutputStream());
	        out.flush();
	        
	      
	        in = new ObjectInputStream(socket.getInputStream());

			peerRequest = new Request();
			peerRequest.setRequestType("UNREGISTER");
			peerRequest.setRequestData(key);
			out.writeObject(peerRequest);
			
	        serverResponse = (Response) in.readObject();
	        
	        
	        if (serverResponse.getResponseCode() == 200) {
				return true;
			} else {
				return false;
			}
	        
		} catch(Exception e) {
			
		} finally {
			try {
		
				if (out != null)
					out.close();
				
				if (in != null)
					in.close();
				
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	private int hash(String key) {

		int totalServers = networkMap.size();

		long hashCode = key.hashCode();
		
		hashCode = (hashCode < 0) ? -hashCode : hashCode;
		
		int hash = (int) (hashCode % totalServers) + 1;
		return hash;
	}
	
	
	private boolean validateKey(String key) {
		if (key.trim().length() == 0) {
			System.out.println("Invalid KEY.");
			return false;
		} else if (key.trim().length() > 10) {
			System.out.println("Invalid KEY. KEY should not be more than 24 bytes (12 characters).");
			return false;
		}
		return true;
	}
	

	private boolean validateValue(String value) {
		if (value.trim().length() == 0) {
			System.out.println("Invalid VALUE.");
			return false;
		} else if (value.trim().length() > 500) {
			System.out.println("Invalid VALUE. VALUE should not be more than 1000 bytes (500 characters).");
			return false;
		}
		return true;
	}
	}

/*public class PeerClient {

	private static List<String> myIndexedLoc = Collections.synchronizedList(new ArrayList<String>());
	private static final int CLIENT_SERVER_PORT = 11000;
	private static final String REPLICATION_PATH = "replica/";

	public static void main(String[] args) throws IOException {
		// Start a new Thread which acts as Client on Peer side
		System.out.println("...Peer Side Client Started....");
		new ClientClient().start();
		System.out.println("...Peer Side Server Started...");
		ServerSocket listener = new ServerSocket(CLIENT_SERVER_PORT);
        try {
            while (true) {
                new ClientServer(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
	}

	private static class ClientServer extends Thread {
		private Socket socket;
		private LogUtility log = new LogUtility("peer");

        public ClientServer(Socket socket) {
            this.socket = socket;
            log.write("File downloading with " + socket.getInetAddress() + " started.");
        }
        
		public void run() {
			OutputStream out = null;
			ObjectInputStream in = null;
			BufferedInputStream fileInput = null;

			try {
				String clientIp = socket.getInetAddress().getHostAddress();
				log.write("Serving download request for " + clientIp);

				in = new ObjectInputStream(socket.getInputStream());
				Request request = (Request) in.readObject();

				if (request.getRequestType().equalsIgnoreCase("DOWNLOAD")) {
					String fileName = (String) request.getRequestData();
					String fileLocation = FileUtility.getFileLocation(fileName, myIndexedLoc);
					log.write("Uploding/Sending file " + fileName);

					File file = new File(fileLocation + fileName);
					
					byte[] mybytearray = new byte[(int) file.length()];
					fileInput = new BufferedInputStream(new FileInputStream(file));
					fileInput.read(mybytearray, 0, mybytearray.length);
					out = socket.getOutputStream();
					out.write(mybytearray, 0, mybytearray.length);
					out.flush();
					log.write("File sent successfully.");
				} else if (request.getRequestType().equalsIgnoreCase("REPLICATE_DATA")) {
					ConcurrentHashMap<String, ArrayList<String>> data = (ConcurrentHashMap<String, ArrayList<String>>) request.getRequestData();
					new ReplicationService(data).start();
				} else if (request.getRequestType().equalsIgnoreCase("DELETE_DATA")) {
					ArrayList<String> deleteFiles = (ArrayList<String>) request.getRequestData();
					if (deleteFiles != null) {
						for (String fileName : deleteFiles) {
							File file = new File(REPLICATION_PATH + fileName);
							file.delete();
							file = null;
						}
					}
				}
			} catch (Exception e) {
				log.write("Error in sending file.");
				log.write("ERROR:" + e);
			} finally {
				try {
					if (out != null)
						out.close();

					if (in != null)
						in.close();

					if (fileInput != null)
						fileInput.close();

					if (socket != null)
						socket.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				Thread.currentThread().interrupt();
			}
		}

		@Override
		public void interrupt() {
			log.close();
			super.interrupt();
		}
	}

	private static class ClientClient extends Thread {

		public void run() {
			Socket socket = null;
			ObjectInputStream in = null;
			BufferedReader input = null;
			ObjectOutputStream out = null;
			Request peerRequest = null;
			Response serverResponse	= null;

			try {
				input = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Enter Server IP Address:");
		        String serverAddress = input.readLine();
		        long startTime, endTime;
		        double time;

		        if(serverAddress.trim().length() == 0 || !IPAddressValidator.validate(serverAddress)) {
					System.out.println("Invalid Server IP Address.");
					System.exit(0);
				}

		        // Make connection with server using the specified Host Address and Port 10000
		        socket = new Socket(serverAddress, 10000);

		        // Initializing output stream using the socket's output stream
		        out = new ObjectOutputStream(socket.getOutputStream());
		        out.flush();

		        // Initializing input stream using the socket's input stream
		        in = new ObjectInputStream(socket.getInputStream());

		        // Read the initial welcome message from the server
		        serverResponse = (Response) in.readObject();
		        System.out.print((String) serverResponse.getResponseData());
		        String replicaChoice = input.readLine();

		        // Setup a Request object with Request Type = REPLICATION and Request Data = Choice
				peerRequest = new Request();
				peerRequest.setRequestType("REPLICATION");
				peerRequest.setRequestData(replicaChoice);
				out.writeObject(peerRequest);

				if (replicaChoice.equalsIgnoreCase("Y")) {
					// Read the Replication response from the server
					myIndexedLoc.add(REPLICATION_PATH);
					serverResponse = (Response) in.readObject();
					ConcurrentHashMap<String, ArrayList<String>> data = (ConcurrentHashMap<String, ArrayList<String>>) serverResponse.getResponseData();
					new ReplicationService(data).start();
				}

				// Previously indexed locations if any
				serverResponse = (Response) in.readObject();
				ArrayList<String> indexedLocations =  (ArrayList<String>) serverResponse.getResponseData();
				if (indexedLocations != null) {
					for (String x : indexedLocations) {
						if (!myIndexedLoc.contains(x)) {
							myIndexedLoc.add(x);
						}
					}
				}

		        while (true) {
		        	// Display different choices to the user
		        	System.out.println("\nWhat do you want to do?");
			        System.out.println("[1] Register files with the server.");
			        System.out.println("[2] Lookup for a file in the server.");
			        System.out.println("[3] Un-register all files of this client from the server.");
			        System.out.println("[4] Print download log of this client.");
			        System.out.println("[5] Exit.");
			        System.out.print("Enter choice and press ENTER:");
			        int option;

			        try {
			        	option = Integer.parseInt(input.readLine());
					} catch (NumberFormatException e) {
						System.out.println("INVALID OPTION. PLEASE TRY AGIAN.");
						continue;
					}

			        switch (option) {
					case 1:
						System.out.println("\nEnter path of the files to sync with the server:");
						String path = input.readLine();

						if(path.trim().length() == 0) {
							System.out.println("Invalid Path.");
							continue;
						}

						ArrayList<String> files = FileUtility.getFiles(path);

						File file = new File(path);
						if (file.isFile()) {
							myIndexedLoc.add(path.substring(0, path.lastIndexOf("/")));
							System.out.println(path.substring(0, path.lastIndexOf("/")));
							files.add(0, path.substring(0, path.lastIndexOf("/")));
						} else if (file.isDirectory()) {
							myIndexedLoc.add(path);
							files.add(0, path);
						}

						if (files.size() > 1) {
							startTime = System.currentTimeMillis();

							peerRequest = new Request();
							peerRequest.setRequestType("REGISTER");
							peerRequest.setRequestData(files);
							out.writeObject(peerRequest);

							serverResponse = (Response) in.readObject();
							endTime = System.currentTimeMillis();
							time = (double) Math.round(endTime - startTime) / 1000;

							if (serverResponse.getResponseCode() == 200) {
							
								System.out.println((files.size() - 1) + " files registered with the server. Time taken:" + time + " seconds.");
							} else {
								System.out.println("Unable to register files with server. Please try again later.");
							}
						} else {
							System.out.println("0 files found at this location. Nothing registered with the server.");
						}
						break;

					case 2:
						System.out.println("\nEnter name of the file you want to look for at the server:");
						String fileName = input.readLine();
						String hostAddress;

						startTime = System.currentTimeMillis();
						peerRequest = new Request();
						peerRequest.setRequestType("LOOKUP");
						peerRequest.setRequestData(fileName);
						out.writeObject(peerRequest);

						serverResponse = (Response) in.readObject();
						endTime = System.currentTimeMillis();
						time = (double) Math.round(endTime - startTime) / 1000;

						if (serverResponse.getResponseCode() == 200) {
							System.out.println("File Found. Lookup time: " + time + " seconds.");

							HashMap<Integer, String> lookupResults = (HashMap<Integer, String>) serverResponse.getResponseData();

							// Printing all Peer details that contain the searched file
							if (lookupResults != null) {
								for (Map.Entry e : lookupResults.entrySet()) {
									System.out.println("\nPeer ID:" + e.getKey().toString());
									System.out.println("Host Address:" + e.getValue().toString());
								}
							}

							if (fileName.trim().endsWith(".txt")) {
								System.out.print("\nDo you want to download (D) or print this file (P)? Enter (D/P):");
								String download = input.readLine();

								if(lookupResults.size() > 1) {
									System.out.print("Enter Peer ID from which you want to download the file:");
									int peerId = Integer.parseInt(input.readLine());
									hostAddress = lookupResults.get(peerId);
								} else {
									Map.Entry<Integer,String> entry = lookupResults.entrySet().iterator().next();
									hostAddress = entry.getValue();
								}

								if (download.equalsIgnoreCase("D")) {
									System.out.println("The file will be downloaded in the 'downloads' folder in the current location.");
									obtain(hostAddress, 11000, fileName, out, in);
								} else if (download.equalsIgnoreCase("P")) {
									obtain(hostAddress, 11000, fileName, out, in);
									FileUtility.printFile(fileName);
								}
							} else {
								System.out.print("\nDo you want to download this file?(Y/N):");
								String download = input.readLine();
								if (download.equalsIgnoreCase("Y")) {
									if(lookupResults.size() > 1) {
										System.out.print("Enter client ID from which you want to download the file:");
										int peerId = Integer.parseInt(input.readLine());
										hostAddress = lookupResults.get(peerId);
									} else {
										Map.Entry<Integer,String> entry = lookupResults.entrySet().iterator().next();
										hostAddress = entry.getValue();
									}
									obtain(hostAddress, 11000, fileName, out, in);
								}
							}
						} else {
							System.out.println((String) serverResponse.getResponseData());
							System.out.println("Lookup time: " + time + " seconds.");
						}
						break;

					case 3:

						System.out.print("\nAre you sure (Y/N)?:");
						String confirm = input.readLine();

						if (confirm.equalsIgnoreCase("Y")) {
							startTime = System.currentTimeMillis();
							peerRequest = new Request();
							peerRequest.setRequestType("UNREGISTER");
							peerRequest.setRequestData("Un-register all files from index server.");
							out.writeObject(peerRequest);
							endTime = System.currentTimeMillis();
							time = (double) Math.round(endTime - startTime) / 1000;

							serverResponse = (Response) in.readObject();
							System.out.println((String) serverResponse.getResponseData());
							System.out.println("Time taken:" + time + " seconds.");
						}
						break;

					// Printing the download log
					case 4:
						(new LogUtility("peer")).print();
						break;

					// Handling Peer exit functionality
					case 5:
						// Setup a Request object with Request Type = DISCONNECT and Request Data = general message
						peerRequest = new Request();
						peerRequest.setRequestType("DISCONNECT");
						peerRequest.setRequestData("Disconnecting from server.");
						out.writeObject(peerRequest);
						System.out.println("Thanks for using this system.");
						System.exit(0);
						break;
					default:
						System.out.println("INVALID INPUT, PLEASE TRY AGAIN.");
						break;
					}
		        }
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				try {

					if (out != null)
						out.close();

					if (in != null)
						in.close();

					if (socket != null)
						socket.close();

					if (input != null)
						input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void obtain(String hostAddress, int port, String fileName, ObjectOutputStream out, ObjectInputStream in) {
			boolean isDownloaded = false;
			long startTime = System.currentTimeMillis();

			if (!FileUtility.downloadFile(hostAddress, port, fileName)) {
				try {
					Request peerRequest = new Request();
					peerRequest.setRequestType("GET_BACKUP_NODES");
					peerRequest.setRequestData("Send list of backup nodes.");
					out.writeObject(peerRequest);

					Response serverResponse = (Response) in.readObject();
					List<String> backupNodes = (List<String>) serverResponse.getResponseData();

					//System.out.println(backupNodes);
					for (String node : backupNodes) {
						if(FileUtility.downloadFile(node, port, fileName)) {
							isDownloaded = true;
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				isDownloaded = true;
			}

			long endTime = System.currentTimeMillis();
			double time = (double) Math.round(endTime - startTime) / 1000;

			if (isDownloaded) {
				System.out.println("File downloaded successfully in " + time + " seconds.");
			} else {
				System.out.println("Unable to connect to the host. Unable to  download file. Try using a different peer if available.");
			}
		}
	}

	private static class ReplicationService extends Thread {
		private static ConcurrentHashMap<String, ArrayList<String>> data = new ConcurrentHashMap<String, ArrayList<String>>();

		public ReplicationService (ConcurrentHashMap<String, ArrayList<String>> data) {
			ReplicationService.data = data;
		}

		public void run () {
			for (Map.Entry e : data.entrySet()) {
				String key = e.getKey().toString();
				ArrayList<String> value = (ArrayList<String>) e.getValue();
				String hostAddress = key.split("#")[1].trim();
				for (String file : value) {
					// Replicate file from the respective peer
					replicate(hostAddress, 11000, file);
				}
			}
			this.interrupt();
		}

		private void replicate(String hostAddress, int port, String fileName) {
			FileUtility.replicateFile(hostAddress, port, fileName);
		}
	}
}*/

/* FILE: Server.java
 * USEAGE: --
 * DESCRIPTION: -- 
 * OPTIONS: --
 * REQUIREMENTS: -- 
 * BUGS: -- 
 * AUTHOR: xXxSpicyBoiiixXx (Md Ali)
 * ORGANIZATION: IIT
 * VERSION: 1.0
 * CREATED: 10/01/2020
 * REVISION: -- 
*/

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;

public class Server {
	
	private static ConcurrentHashMap<String, ArrayList<String>> indexDatabase = new ConcurrentHashMap<String, ArrayList<String>>();
	private static ConcurrentHashMap<String, ArrayList<String>> indexedLocations = new ConcurrentHashMap<String, ArrayList<String>>();
	private static List<String> replicationNodes = Collections.synchronizedList(new ArrayList<String>());
	private static final int SERVER_PORT = 10000; 
	private static final String REPLICA_LOCATION = "replica/";


	private static int totalClients = 0;
	
	/*
	 * Server's main method to run the server. This server 
	 * listens on port 10000. When a connection is requestes 
	 * there will be a new thread to service it and return a 
	 * listening handle. The server will keep a log of the 
	 * client's id thatt connects to the server for file sharing
	 *
	*/
		
private void print(String message) {
	LogUtility log = new LogUtility("Server");
	log.write(message);
	log.close();
	System.out.println(message);
}
     
private static class Indexer extends Thread { 
	
	private Socket socket;
	private int clientNumber;
	
	public Indexer(Socket socket, int clientNumber) {
		
		this.socket = socket; 
		this.clientNumber = clientNumber;
		print("\n Connection to Client # " + clientNumber + " on " + socket.getInetAddress());
		totalClients++;
		print("Total number of clients connected: " + totalClients);
	}
}


public void run() {
	try {
		
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.flush();
		
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		String clientIP = socket.getInetAddress().getHostAddress();
		
		Response response = new Response();
		response.setResponseCode(200);
		response.setResponseData("Hello, you are client number: " + clientNumber + ".\nDo you want your node to act as replication? Please note! This will take a lot of disk space (Y/N):");
		out.writeObject(response);
		
		Request clientRequest = (Request) in.readObject();
		String requestType = clientRequest.getRequestType();
		String replicaChoice = (String) clientRequest.getRequestData();	
		
		if(replicaChoice.equalsIgnoreCase("Y")) {
			System.out.println("Replication with this node accepted.");
			
			if(!replicationNodes.contains(clientIP)) {
				replicationNodes.add(clientIP);
			}
		
			if(clientIndexedLocations.containtsKey(clientIP)) {
				clientIndexedLocations.get(clientIP).add(REPLICA_LOCATION);
			}
			
			else { 
				ArrayList<String> paths = new ArrayList<String>();
				paths.add(REPLICA_LOCATION);
				clientIndexedLocations.put(clientIP, paths);
			}
			
			response = new Response();
			response.setResponseCode(200);
			response.setResponsesData(indexDatabase);
			out.writeObject(response);
		
		}
		
		response = new Response();
		response.setResponseCode(200);
		response.setResponsesData(indexDatabase);
		out.writeObject(response);
		
		while(true) {
			
			clientRequest = (Request) in.readObject();
			requestType = clientRequest.getRequestType();
			
			if(requestType.equalsIgnoreCase("REGISTER")) {
				ArrayList<String> indexedLocations = register(clientNumber, clientIP, (ArrayList<String>) clientRequest.getRequestData(), out);
            	response = new Response();
            	response.setResponseCode(200);
                response.setResponseData(indexedLocations);
                out.writeObject(response);
			}
			
			else if (requestType.equalsIgnoreCase("LOOKUP")) {
				print("\nLooking up a file.");
				String fileName = (String) clientRequest.getRequestData();
				print("Request from client number: " + clientNumber + " (" + clientIP + ") to look for file " + fileName);
				HashMap<Integer, String> searchResults = search(fileName);
				
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
			}
			
			else if(requestType.equalsIgnoreCase("UNREGISTER")) {
				response = new Response();
				if (unregister(clientIP)) {
					response.setResponseCode(200);
					response.setResponseData("Your files have been un-registered from the indexing server.");
					print("Peer # " + clientNumber + " (" + clientIP + ") has un-registered all its files.");
				} else {
					response.setResponseCode(400);
					response.setResponseData("Error in un-registering files from the indexing server.");
				}
				out.writeObject(response);
			} else if(requestType.equalsIgnoreCase("GET_BACKUP_NODES")) {
				System.out.println("\n" + clientIp + " requested backup nodes info. Sending backup nodes info.");
				response = new Response();
				response.setResponseCode(200);
				response.setResponseData(replicationNodes);
				out.writeObject(response);
				System.out.println("Backup nodoes information sent.");
			} else if(requestType.equalsIgnoreCase("DISCONNECT")) {
				print("\nClient number " + clientNumber + " disconnecting...");
				
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
	} catch(Exception e) {
		print("Error handing client number: " + clientNumber + ": " + e);
		Thread.currentThread().interrupt();	
		
	}
}

public void interrupt() {
	print("\nConnection with Peer # " + clientNumber + " closed");
	totalClients--;
	print("Total number of peers connected:" + totalClients;
	if (totalClients == 0) {
		print("No more peers connected.");
	}
}

private ArrayList<String> register(int clientID, String clientAddress, ArrayList<String> files, ObjectOutputStream out) throws IOException {
	print("\nRegistering files from Client " + clientAddress);
	
	// Appending HHmmss just to make the key unique because a single peer may register multiple times. We aren't using the last appended data.
	String time = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
	
	// Retrieving path and storing them separately
	if (clientIndexedLocations.containsKey(clientAddress)) {
		clientIndexedLocations.get(clientAddress).add(files.get(0));
	} else {
		ArrayList<String> paths = new ArrayList<String>();
    	paths.add(files.get(0));
    	clientIndexedLocations.put(clientAddress, paths);
	}
	files.remove(0);
	
	// Using StringBuffer to avoid creation of multiple string objects while appending
	StringBuffer sb = new StringBuffer();
	sb.append(clientNumber).append("#").append(clientAddress).append("#").append(time);
    indexDatabase.put(sb.toString(), files);
    
    print(files.size() + " files synced with Peer " + clientNumber + " and added to index database");
    
    ConcurrentHashMap<String, ArrayList<String>> newFiles = new ConcurrentHashMap<String, ArrayList<String>>();
    newFiles.put(sb.toString(), files);
    sendReplicateCommand(newFiles);
    
    return clientIndexedLocations.get(clientAddress);
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
	
	if (newSize < oldSize) {
		Request serverRequest = new Request();
    	Socket socket = null;
    	try {
    		serverRequest.setRequestType("DELETE_DATA");
        	for (String node : replicationNodes) {
        		socket = new Socket(node, CLIENT_PORT);
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
    		socket = new Socket(node, CLIENT_PORT);
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
public static void main(String[] args) throws Exception{

	System.out.println("...Staring server....");

	int clientID = 1;

	ServerSocket listener = new ServerSocket(SERVER_PORT);

	try { 
		while (true) {
			
			new Indexer(listener.accept(), clientID++).start();
		}
	}
	
	finally { 
		
		listener.close();	
	}

} 
}


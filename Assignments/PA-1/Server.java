/* FILE: Server.java
 * USEAGE: --
 * DESCRIPTION: The server that will host files and show what registered clients have in each of theier respective files. This port will be 10000, the clients will run 
 * their "server" on port 11000. This is all done on the local host.
 * OPTIONS: --
 * REQUIREMENTS: -- 
 * BUGS: -- 
 * AUTHOR: xXxSpicyBoiiixXx (Md Ali)
 * ORGANIZATION: IIT
 * VERSION: 1.0
 * CREATED: 10/01/2020
 * REVISION: -- 
*/

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

  // serverDatabase is a concurrent hash map used to store all the files registered from various clients.
  // The key of the database contains Client ID and Client IP Address separated by # (Example: 1#127.0.0.1)
  private static ConcurrentHashMap < String,
  ArrayList < String >> serverDatabase = new ConcurrentHashMap < String,
  ArrayList < String >> ();
  private static ConcurrentHashMap < String,
  ArrayList < String >> clientIndexedLocations = new ConcurrentHashMap < String,
  ArrayList < String >> ();
  private static List < String > replicationNodes = Collections.synchronizedList(new ArrayList < String > ());

  //server will be having an open socket on 10000, while the client server like will have an open socket at 11000.
  private static final int SERVER_SOCKET_PORT = 10000;
  private static final int CLIENT_SERVER_PORT = 11000;

  // This will give a replica location if a client would like to do so.
  private static final String REPLICA_LOCATION = "replica/";

  // totalPeers stores the count of peers connected to the indexing server
  private static int totalClients = 0;

  //Starting up server. 
  public static void main(String[] args) throws Exception {
    System.out.println("...Server Started...");
    int clientId = 1;

    ServerSocket listener = new ServerSocket(SERVER_SOCKET_PORT);
    try {
      while (true) {
        new Indexer(listener.accept(), clientId++).start();
      }
    } finally {
      listener.close();
    }
  }

  // A private thread to handle client's file sharing requests on a specified socket.
  private static class Indexer extends Thread {
    private Socket socket;
    private int clientNumber;

    public Indexer(Socket socket, int clientNumber) {
      this.socket = socket;
      this.clientNumber = clientNumber;
      print("\nNew connection with client # " + clientNumber + " at " + socket.getInetAddress());
      totalClients++;
      print("Total number of clients connected:" + totalClients);
    }

    public void run() {
      try {
        // Initializing output stream using the socket's output stream
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();

        // Initializing input stream using the socket's input stream
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());
        String clientIp = socket.getInetAddress().getHostAddress();

        // Send a welcome message to the client and asks if they want to be a replication node.
        Response response = new Response();
        response.setResponseCode(200);
        response.setResponseData("Hello, you are client #" + clientNumber + ".\nDo you want your node to act as a replication node? This requires your disk space to be large. (Y/N):");
        out.writeObject(response);

        Request clientRequest = (Request) in .readObject();
        String requestType = clientRequest.getRequestType();
        String replicaChoice = (String) clientRequest.getRequestData();

        if (replicaChoice.equalsIgnoreCase("Y")) {
          System.out.println("Replication with this node accepted.");

          if (!replicationNodes.contains(clientIp)) {
            replicationNodes.add(clientIp);
          }

          // Just to remind client if it will be acting as a replication node
          if (clientIndexedLocations.containsKey(clientIp)) {
            clientIndexedLocations.get(clientIp).add(REPLICA_LOCATION);
          } else {
            ArrayList < String > paths = new ArrayList < String > ();
            paths.add(REPLICA_LOCATION);
            clientIndexedLocations.put(clientIp, paths);
          }

          response = new Response();
          response.setResponseCode(200);
          response.setResponseData(serverDatabase);
          out.writeObject(response);
        }

        response = new Response();
        response.setResponseCode(200);
        response.setResponseData(clientIndexedLocations.get(clientIp));
        out.writeObject(response);

        while (true) {
          // Read the request object received from the Peer
          clientRequest = (Request) in .readObject();
          requestType = clientRequest.getRequestType();

          if (requestType.equalsIgnoreCase("REGISTER")) {
            // If Request Type = REGISTER, then call register(...) method to register the peer's files
            ArrayList < String > indexedLocations = register(clientNumber, clientIp, (ArrayList < String > ) clientRequest.getRequestData(), out);
            response = new Response();
            response.setResponseCode(200);
            response.setResponseData(indexedLocations);
            out.writeObject(response);
          }

          else if (requestType.equalsIgnoreCase("LOOKUP")) {
            print("\nLooking up a file.");
            String fileName = (String) clientRequest.getRequestData();

            // If Request Type = LOOKUP, then call search(...) method to search for the specified file
            print("Request from client # " + clientNumber + " (" + clientIp + ") to look for file " + fileName);
            HashMap < Integer,
            String > searchResults = search(fileName);

            // If file found then respond with all the client locations that contain the file or else send File Not Found message
            if (searchResults.size() > 0) {
              response = new Response();
              response.setResponseCode(200);
              response.setResponseData(searchResults);
              out.writeObject(response);
              print("File Found.");
            }

            else {
              response = new Response();
              response.setResponseCode(404);
              response.setResponseData("File Not Found.");
              out.writeObject(response);
              print("File Not Found.");
            }
          }

          else if (requestType.equalsIgnoreCase("UNREGISTER")) {
            response = new Response();

            if (unregister(clientIp)) {
              response.setResponseCode(200);
              response.setResponseData("Your files have been un-registered from the indexing server.");
              print("Client # " + clientNumber + " (" + clientIp + ") has un-registered all its files.");
            }

            else {
              response.setResponseCode(400);
              response.setResponseData("Error in un-registering files from server.");
            }
            out.writeObject(response);
          } else if (requestType.equalsIgnoreCase("GET_BACKUP_NODES")) {
            // Sends replication client nodes to the user
            System.out.println("\n" + clientIp + " requested backup nodes info. Sending backup nodes info.");
            response = new Response();
            response.setResponseCode(200);
            response.setResponseData(replicationNodes);
            out.writeObject(response);
            System.out.println("Backup nodoes information sent.");
          }

          else if (requestType.equalsIgnoreCase("DISCONNECT")) {
            print("\nClient # " + clientNumber + " disconnecting...");
            try {
              socket.close();
            } catch(IOException e) {
              print("Couldn't close a socket.");
            }
            Thread.currentThread().interrupt();
            break;
          }
        }
      } catch(EOFException e) {
        Thread.currentThread().interrupt();
      } catch(Exception e) {
        print("Error handling Client # " + clientNumber + ": " + e);
        Thread.currentThread().interrupt();
      }
    }

    // Stop thread once the client has disconnected or some error has occurred in serving the peer.
    public void interrupt() {
      print("\nConnection with client # " + clientNumber + " closed");
      totalClients--;
      print("Total number of client connected:" + totalClients);
      if (totalClients == 0) {
        print("No more peers connected.");
      }
    }

    private void print(String message) {
      LogUtility log = new LogUtility("server");
      log.write(message);
      log.close();
      System.out.println(message);
    }

    private ArrayList < String > register(int clientId, String clientAddress, ArrayList < String > files, ObjectOutputStream out) throws IOException {
      print("\nRegistering files from client " + clientAddress);

      String time = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());

      // Retrieving path and storing them separately
      if (clientIndexedLocations.containsKey(clientAddress)) {
        clientIndexedLocations.get(clientAddress).add(files.get(0));
      } else {
        ArrayList < String > paths = new ArrayList < String > ();
        paths.add(files.get(0));
        clientIndexedLocations.put(clientAddress, paths);
      }
      files.remove(0);

      // Using StringBuffer to avoid creation of multiple string objects while appending
      StringBuffer sb = new StringBuffer();
      sb.append(clientNumber).append("#").append(clientAddress).append("#").append(time);
      serverDatabase.put(sb.toString(), files);

      print(files.size() + " files synced with Peer " + clientNumber + " and added to index database");

      ConcurrentHashMap < String,
      ArrayList < String >> newFiles = new ConcurrentHashMap < String,
      ArrayList < String >> ();
      newFiles.put(sb.toString(), files);
      sendReplicateCommand(newFiles);

      return clientIndexedLocations.get(clientAddress);
    }

    private boolean unregister(String clientAddress) throws IOException {
      int oldSize = serverDatabase.size();
      ArrayList < String > deleteFiles = null;

      for (Map.Entry e: serverDatabase.entrySet()) {
        String key = e.getKey().toString();
        ArrayList < String > value = (ArrayList < String > ) e.getValue();

        if (key.contains(clientAddress)) {
          deleteFiles = serverDatabase.get(key);
          serverDatabase.remove(key);
        }
      }
      int newSize = serverDatabase.size();

      // Send request to delete the unregistered files from the replication node
      if (newSize < oldSize) {
        Request serverRequest = new Request();
        Socket socket = null;
        try {
          serverRequest.setRequestType("DELETE_DATA");
          for (String node: replicationNodes) {
            socket = new Socket(node, CLIENT_SERVER_PORT);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            serverRequest.setRequestData(deleteFiles);
            out.writeObject(serverRequest);
            out.close();
            socket.close();
          }
          socket = null;
        } catch(Exception e) {
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

    private HashMap < Integer,
    String > search(String fileName) {
      HashMap < Integer,
      String > searchResults = new HashMap < Integer,
      String > ();
      for (Map.Entry e: serverDatabase.entrySet()) {
        String key = e.getKey().toString();
        ArrayList < String > value = (ArrayList < String > ) e.getValue();

        for (String file: value) {
          if (file.equalsIgnoreCase(fileName)) {
            int clientId = Integer.parseInt(key.split("#")[0].trim());
            String hostAddress = key.split("#")[1].trim();
            searchResults.put(clientId, hostAddress);
          }
        }
      }
      return searchResults;
    }

    private void sendReplicateCommand(ConcurrentHashMap < String, ArrayList < String >> newFiles) throws IOException {
      Request serverRequest = new Request();
      Socket socket = null;
      try {
        serverRequest.setRequestType("REPLICATE_DATA");
        for (String node: replicationNodes) {
          socket = new Socket(node, CLIENT_SERVER_PORT);
          ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
          serverRequest.setRequestData(newFiles);
          out.writeObject(serverRequest);
          out.close();
          socket.close();
        }
        socket = null;
      } catch(Exception e) {
        print("Error in replication:" + e);
      } finally {
        serverRequest = null;
        if (socket != null && socket.isConnected()) {
          socket.close();
        }
      }
    }
  }
}

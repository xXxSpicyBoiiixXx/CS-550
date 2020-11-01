import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

                value = DHT_FileTransfer.getFromHashTable(key);
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
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class DHT_FileTransfer {

    private static ConcurrentHashMap<String, String> hashTable = new ConcurrentHashMap<String, String>();
    private static ConcurrentHashMap<String, HashMap<String, String>> replicatedHashTable = new ConcurrentHashMap<String, HashMap<String, String>>();
    private static HashMap<Integer, String> networkMap = new HashMap<Integer, String>();
    private static ArrayList<String> replicationNodes = new ArrayList<String>();
    private static String filesLocation = null;
    private static String replicaLocation = null;

    private static final int PEER_SERVER_PORT = 11000;
    private static final String LOCAL_ADDRESS = NetworkUtility.getLocalAddress();

    public static boolean putInHashTable(String key, String value, boolean confirm){
        if(confirm || !hashTable.containsKey(key)){
            hashTable.put(key, value);
            return true;
        }
        else {
            return false;
        }
    }

    public static String getFromHashTable(String key){
        return hashTable.get(key);
    }

    public static void removeFromHashTable(String key){
        hashTable.remove(key);
    }

    public static void putInReplicaHashTable(String nodeAddress, String key, String value){
        if(replicatedHashTable.containsKey(nodeAddress)){
            HashMap<String, String> innerMap = replicatedHashTable.get(nodeAddress);
            innerMap.put(key, value);
        }
        else{
            HashMap<String, String> innerMap = new HashMap<String, String>();
            innerMap.put(key, value);
            replicatedHashTable.put(nodeAddress, innerMap);
        }
    }

    public static String getFromReplicaHashTable(String key){
        String value = null;

        for(Map.Entry<String, HashMap<String, String>> record : replicatedHashTable.entrySet()) {
            HashMap<String, String> innerMap = replicatedHashTable.get(record.getKey().toString());
            value = innerMap.get(key);
            if (value != null) {
                break;
            }
        }
        return value;
        }

    public static void removeFromReplicaHashTable(String nodeAddress, String key){
        HashMap<String, String> innerMap = replicatedHashTable.get(nodeAddress);
        if(innerMap != null){
            innerMap.remove(key);
        }
    }

    public static ConcurrentHashMap<String, HashMap<String, String>> getReplicatedHashTable(){
        return replicatedHashTable;
    }

    public static void setReplicatedHashTable(ConcurrentHashMap<String, HashMap<String, String>> replicatedHashTable){
        DHT_FileTransfer.replicatedHashTable = replicatedHashTable;
    }

    public static ArrayList<String> getReplicationNodes() {
        return replicationNodes;
    }

    public static ConcurrentHashMap<String, String> getHashTable() {
        return hashTable;
    }

    public static void setHashTable(ConcurrentHashMap<String, String> hashTable){
        DHT_FileTransfer.hashTable = hashTable;
    }

    public static HashMap<Integer, String> getNetworkMap() {
        return networkMap;
    }

    public static int getPeerServerPort(){
        return PEER_SERVER_PORT;
    }

    public static String getLocalAddress() {
        return LOCAL_ADDRESS;
    }

    public static void main(String[] args) throws IOException {
        FileInputStream fileStream = null;

        try{
            Properties configuration = new Properties();
            fileStream = new FileInputStream("network.config");
            configuration.load(fileStream);
            fileStream.close();

            String peerList = configuration.getProperty("NODES");

            if(peerList != null){
                String[] peers = peerList.split(",");

                for(int i = 0; i < peers.length; i++){
                    if(IPAddressValidator.validate(peers[i].trim())){
                        networkMap.put(i + 1, peers[i].trim());
                    }
                    else{
                        System.out.println("One or more IP address in the network config file is invalid. Please open other VMs or something of the sort.....Exiting....");
                        System.exit(0);
                    }
                }

                if(networkMap.isEmpty()){
                    System.out.println("No peers present in configuration file. No nodes....No DHT.... Exiting....");
                    System.exit(0);
                }
            }
            else{
                System.out.println("Configuration file is not as expected. Cannot run program. Exiting....");
                System.exit(0);
            }

            peerList = configuration.getProperty("REPLICATION_NODES");

            if(peerList != null){
                String[] peers = peerList.split(",");

                for(int i = 0; i < peers.length; i++){
                    if(IPAddressValidator.validate(peers[i].trim())){
                        replicationNodes.add(peers[i].trim());
                    }
                    else{
                        System.out.println("one of the Replication Peer's IP address in the config file is invalid. Exiting....");
                        System.exit(0);
                    }
                }
            }

            if(configuration.getProperty("FILES LOCATION") != null){
                filesLocation = configuration.getProperty("FILES LOCATION");
            }
            else{
                filesLocation = "files/";
            }

            if(configuration.getProperty("REPLICA LOCATION") != null){
                replicaLocation = configuration.getProperty("REPLICA LOCATION");
            }
            else {
                replicaLocation = "replica/";
            }
    } catch(Exception e){
            System.out.println("Can't load configuration file....Exiting....");
            System.exit(0);
        }
        finally {
            try {
                if(fileStream != null) {
                    fileStream.close();
                }
            }
            catch(Exception e2){}
        }

        System.out.println("....Peer Client Started....");
        PeerClient peerClient = new PeerClient();
        peerClient.start();

        System.out.println("...Peer Server Started....");
        ServerSocket listener = new ServerSocket(PEER_SERVER_PORT);

        try{
            while(true){
                Server peerServer = new Server(listener.accept());
                peerServer.start();
            }
        }finally{
            listener.close();
        }
    }

    public static String getFilesLocation(){
    return filesLocation;
    }

    public static String getReplicaLocation() {
        return replicaLocation;
    }
}

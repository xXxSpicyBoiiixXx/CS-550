import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReplicationService extends Thread {
    private ArrayList<String> replicationNodes = null;
    private int portAddress;
    private String localAddress = null;
    private HashMap<Integer, String> networkMap = null;

    private String key = null;
    private String value = null;
    private String requestType = null;

    public ReplicationService(String key, String value, String requestType)
    {
        replicationNodes = DHT_FileTransfer.getReplicationNodes();
        portAddress = DHT_FileTransfer.getPeerServerPort();
        localAddress = DHT_FileTransfer.getLocalAddress();
        networkMap = DHT_FileTransfer.getNetworkMap();

        this.key = key;
        this.value = value;
        this.requestType = requestType;
    }

    public void run() {
        Socket socket = null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        Request peerRequest = null;
        Response serverResponse = null;

        String data = key + "," + value;

        if(requestType.equalsIgnoreCase("REPLICATE")){
            replicateHashTable();
            replicateFiles();
            this.interrupt();
        }

        for(String nodeAddress : replicationNodes){
            try{
                if(requestType.equalsIgnoreCase("REGISTER")){
                if(requestType.equalsIgnoreCase(localAddress)){
                   LogUtility log = new LogUtility("peer");
                   log.write(String.format("Serving REPLICATE - REGISTER(%s,%s) request of %s.", key, value, localAddress));
                   DHT_FileTransfer.putInReplicaHashTable(nodeAddress, key, value);
                   replicate(value, portAddress, key);
                   log.write(String.format("REPLICATE - REGISTER(%s,%s) for %s completed successfully.", key, value, localAddress));
                   log.close();
                }
                else {

                    socket = new Socket(nodeAddress, portAddress);

                    out = new ObjectOutputStream(socket.getOutputStream());
                    out.flush();

                    in = new ObjectInputStream(socket.getInputStream());

                    peerRequest = new Request();
                    peerRequest.setRequestType("R_REGISTER");
                    peerRequest.setRequestData(data);
                    out.writeObject(peerRequest);

                    serverResponse =  (Response) in.readObject();
                    socket.close();
                }
            }
                else if(requestType.equalsIgnoreCase("UNREGISTER")){
                    socket = new Socket(nodeAddress, portAddress);

                    out = new ObjectOutputStream(socket.getOutputStream());
                    out.flush();

                    in = new ObjectInputStream(socket.getInputStream());

                        peerRequest = new Request();
                        peerRequest.setRequestType("R_UNREGISTER");
                        peerRequest.setRequestData(key);
                        out.writeObject(peerRequest);

                        serverResponse = (Response) in.readObject();
                        socket.close();
                }
        } catch (Exception ex){
                ex.printStackTrace();
            } finally {
                try {
                    if(out != null)
                        out.close();

                    if(in != null)
                        in.close();

                    if(socket != null)
                        socket.close();
                }
                catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
    }
        this.interrupt();
}

public boolean replicateHashTable() {
ConcurrentHashMap<String, HashMap<String, String>> replicatedHashTable = null;
if (replicationNodes.size() > 1) {
    replicatedHashTable = getReplicationData();
}
else{
    replicatedHashTable = getAllHashTables();
}

if (replicatedHashTable != null && replicatedHashTable.size() > 0) {
    DHT_FileTransfer.setReplicatedHashTable(replicatedHashTable);
    return true;
}
return false;
    }

    private void replicateFiles(){
        for(Map.Entry peer : DHT_FileTransfer.getReplicatedHashTable().entrySet()){
            HashMap<String, String> hashTable = (HashMap<String, String>) peer.getValue();

            for (Map.Entry record : hashTable.entrySet()){

                String filename = record.getKey().toString();
                String peerAddress = record.getValue().toString();

                replicate(peerAddress, portAddress, fileName);
            }
        }
        this.interrupt();
    }

    private ConcurrentHashMap<String, HashMap<String, String>> getReplicationData(){
        Socket socket = null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        Request peerRequest = null;
        Response serverResponse = null;
        ConcurrentHashMap<String, HashMap<String, String>> hm = null;

        for (Map.Entry<Integer, String> peer : networkMap.entrySet()){
            if(peer.getValue().equalsIgnoreCase(localAddress)){
                if(DHT_FileTransfer.getHashTable().size() > 0){
                    replicatedHashTable.put(peer.getValue(), new HashMap<String, String>(DHT_FileTransfer()));
                }
                continue;
            }

            try {
                socket = new Socket(peer.getValue(), portAddress);
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();

                in = new ObjectInputStream(socket.getInputStream());

                peerRequest = new Request();
                peerRequest.setRequestType("GET_HASHTABLE");
                out.writeObject(peerRequest);

                serverResponse = (Response) in.readObject();

                if (serverResponse != null && serverResponse.getResponseCode() == 200){
                    hm = (ConcurrentHashMap<String, String>) serverResponse.getResponseData();
                }
                socket.close();
                socket = null;
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally{
                try{
                    if(out != null)
                        out.close();

                    if(in != null)
                        in.close();
                } catch(IOException ioe){
                    ioe.printStackTrace();
                }
            }

            if (hm != null && hm.size() > 0){
                replicateHashTable().put(peer.getValue(), new HashMap<String, String>(hm));
                hm = null;
            }
        }
        return replicateHashTable();
    }
}

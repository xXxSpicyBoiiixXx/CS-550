import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileUtility {

    private static final String downloadLocation = "downloads/";
    private static final String replicaLocation = DHT_FileTransfer.getReplicaLocation();

    public static ArrayList<String> getFiles(String path){
        ArrayList<String> files = new ArrayList<String>();
        File folder = new File(path);
        if(folder.isDirectory()){
            File[] listOfFiles = folder.listFiles();
            if(listOfFiles != null){
                for (int i = 0; i < listOfFiles.length; i++){
                    if (listOfFiles[i].isFile()){
                        String file = listOfFiles[i].getName();
                        if(!file.endsWith("~")){
                            files.add(file);
                        }
                    }
                }
            }
        }
        else if(folder.isFile()){
            files.add(path.substring(path.lastIndexOf("/") + 1, path.length()));
        }
        return files;
    }

    public static String getFileLocation(String fileName, List<String> locations){
        String fileLocation ="";
        boolean fileFound = false;

        for (String path : locations){
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();

            for(int i = 0; i < listOfFiles.length; i++){
                if(listOfFiles[i].getName().equals(fileName)){
                    fileLocation = path.endsWith("/") ? path : path.concat("/");
                    fileFound = true;
                    break;
                }
            }
        }
        fileLocation = fileFound ? fileLocation : "File Not Found.";
        return fileLocation;
    }

    public static boolean downloadFile(String hostAddress, int port, String fileName, boolean fromReplica)
    {
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        Socket socket = null;
        boolean isDownloaded = false;

        try {
            socket = new Socket(hostAddress, port);
            System.out.println("\nDownloading file " + fileName);

            File file = new File(downloadLocation);
            if(!file.exists())
                file.mkdir();

            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();

            System.out.println("Requesting file....");
            Request request = new Request();

            if(fromReplica){
                request.setRequestType("R_DOWNLOAD");
            }
            else{
                request.setRequestType("DOWNLOAD");
            }
            request.setRequestData(fileName);
            out.writeObject(request);

            System.out.println("Downloading file....");
            in = new ObjectInputStream(socket.getInputStream());

            file = new File(downloadLocation + fileName);
            byte[] bytes = (byte[]) in.readObject();
            Files.write(file.toPath(), bytes);

            if((new File(downloadLocation + fileName)).length() == 0){
                isDownloaded = false;
                (new File(downloadLocation + fileName)).delete();
            }
            else {
                isDownloaded = true;
            }
        }
        catch(SocketException e){
            isDownloaded = false;
        }
        catch(Exception e){
            isDownloaded = false;
        }
        finally{
            try{
                if (out != null)
                    out.close();

                if (in  != null)
                    in.close();

                if(socket != null)
                    socket.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return isDownloaded;
    }

    public static boolean replicateFile(String hostAddress, int port, String fileName){
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        Socket socket = null;
        boolean isReplicated = false;
        LogUtility log = new LogUtility("replication");

        try{
            long startTime = System.currentTimeMillis();

            socket = new Socket(hostAddress, port);

            File file = new File(replicaLocation);
            if(!file.exists())
                file.mkdir();

            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();

            log.write("Requesting file...." + fileName);
            Request request = new Request();
            request.setRequestType("DOWNLOAD");
            request.setRequestData(fileName);
            out.writeObject(request);
            log.write("Downloading file ... " + fileName);
            in = new ObjectInputStream(socket.getInputStream());

            file = new File(replicaLocation + fileName);
            byte[] bytes = (byte[]) in.readObject();
            Files.write(file.toPath(), bytes);

            long endTime = System.currentTimeMillis();
            double time = (double) Math.round(endTime - startTime) / 1000;
            log.write("File downloaded successfully in " + time + " seconds.");
            isReplicated = true;
        } catch(SocketException e) {
            log.write("Unable to connect to the host. Unable to  download file.");
            isReplicated = false;
            log.write("Error:" + e);
        } catch (Exception e) {
            log.write("Unable to download file. Please check if you have write permission.");
            isReplicated = false;
            log.write("Error:" + e);
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
                if (socket != null)
                    socket.close();
                if (log != null)
                    log.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return isReplicated;
    }
    public static void printFile(String fileName) {
        File file = new File(downloadLocation + fileName);
        if (file.exists()) {
            System.out.println("\nThe file has been downloaded in the downloads folder. Will only print the first 1000 characters.");
            BufferedReader br = null;
            int charCount = 0;

            try {

                br = new BufferedReader(new FileReader(downloadLocation + fileName));
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    charCount += line.length();
                    if(charCount > 1000) break;
                }
            } catch (Exception e) {
                System.out.println("Unable to print file.");
                System.out.println("Error:" + e);
            } finally {
                try {
                    if (br != null)
                        br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("\nThe file could not be printed because it may not have been downloaded.");
        }
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(replicaLocation + fileName);
        if (file.exists()) {
            return file.delete();
        }
        return false;

    }
}

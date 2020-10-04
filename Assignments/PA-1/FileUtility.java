/* FILE: FileUtility.java
 * USEAGE: --
 * DESCRIPTION: -- 
 * OPTIONS: --
 * REQUIREMENTS: -- 
 * BUGS: -- 
 * AUTHOR: xXxSpicyBoiiixXx (Md Ali)
 * ORGANIZATION: IIT
 * VERSION: 1.0
 * CREATED: 10/03/2020
 * REVISION: -- 
*/


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/***
 * This class provides various file related methods like downloading file,
 * getting all files from a given location, printing text file etc.
 */
public class FileUtility {

	private static final String downloadLocation = "downloads/";
	private static final String replicaLocation = "replica/";
	private static final int BUFFER_SIZE = 1024 * 64; // 64 KiloBytes
	
	/***
	 * This method retrieves all the files from the given path.
	 * @param path	Path whose files are to be returned
	 * @return		Filename of all the files contained in the given path
	 */
	public static ArrayList<String> getFiles(String path) {
		ArrayList<String> files = new ArrayList<String>();
		File folder = new File(path);
		if (folder.isDirectory()) {
			File[] listOfFiles = folder.listFiles();
			//path = path.endsWith("/") ? path : path.concat("/");
			
			if (listOfFiles != null) {
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						String file = listOfFiles[i].getName();
						if(!file.endsWith("~")) {
							files.add(file);
						}	
					}
				}
			}
		} else if (folder.isFile()) {
			files.add(path.substring(path.lastIndexOf("/") + 1, path.length()));
		}
		
		return files;
	}
	
	/***
	 * This method finds the location of the file amongst the given locations.
	 * @param fileName	Name of the file whose location is to be searched
	 * @param locations	List of all the locations in which file has to be located
	 * @return			Returns the path where the file is located
	 */
	public static String getFileLocation(String fileName, List<String> locations) {
		String fileLocation = "";
		boolean fileFound = false;
		
		for (String path : locations) {
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			
			for (int i = 0; i < listOfFiles.length; i++) {
				if(listOfFiles[i].getName().equals(fileName)) {
					fileLocation = path.endsWith("/") ? path : path.concat("/");
					fileFound = true;
					break;
				}
			}
		}
		fileLocation = fileFound ? fileLocation : "File Not Found."; 
		return fileLocation;
	}
	
	/***
	 * This method downloads the specified file from the specified host (peer).
	 * @param hostAddress	IP Address of the peer from which the file has to be downloaded
	 * @param port			Port of the peer from which the file has to be downloaded
	 * @param fileName		Name of the file to be downloaded
	 * @return				Returns true if the file is successfully downloaded else returns false
	 */
	public static boolean downloadFile(String hostAddress, int port, String fileName) {
		InputStream in = null;
		BufferedOutputStream fileOutput = null;
		ObjectOutputStream out = null;
		Socket socket = null;
		boolean isDownloaded = false;
		
		try {
			// Establish connection to the peer which contains the file for downloading.
			socket = new Socket(hostAddress, port);
			System.out.println("\nDownloading file " + fileName);
			
			// Create a download folder if it doesn't exist
			File file = new File(downloadLocation);
			if (!file.exists())
				file.mkdir();

			// Create an output stream using the socket's output stream.
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();

			System.out.println("Requesting file.........");
			// Setup a Request object with Request Type = DOWNLOAD and Request Data = name of the file to be downloaded
			Request request = new Request();
			request.setRequestType("DOWNLOAD");
			request.setRequestData(fileName);
			out.writeObject(request);

			// Download file from the output stream
			System.out.println("Downloading file........");
			byte[] mybytearray = new byte[BUFFER_SIZE];
			in = socket.getInputStream();
			fileOutput = new BufferedOutputStream(new FileOutputStream(downloadLocation + fileName));
			
			// Reading incoming stream in chunks
			int bytesRead;
			while ((bytesRead = in.read(mybytearray, 0, mybytearray.length)) > 0)
			{
				fileOutput.write(mybytearray, 0, bytesRead);
			}
			
			if((new File(downloadLocation + fileName)).length() == 0) {
				isDownloaded = false;
				(new File(downloadLocation + fileName)).delete();
			} else {
				isDownloaded = true;
			}
		} catch(SocketException e) {
			//System.out.println("Unable to connect to the host. Unable to  download file. Try using a different peer if available.");
			isDownloaded = false;
			//System.out.println("Error:" + e);
			//e.printStackTrace();
		} catch (Exception e) {
			//System.out.println("Unable to download file. Please check if you have write permission.");
			isDownloaded = false;
			//System.out.println("Error:" + e);
			//e.printStackTrace();
		} finally {
			try {
				// Closing all streams. Close the stream only if it is initialized
				if (out != null)
					out.close();
				
				if (in != null)
					in.close();
				
				if (fileOutput != null)
					fileOutput.close();
				
				if (socket != null)
					socket.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return isDownloaded;
	}
	
	/***
	 * This method replicates the specified file from the specified host (peer) to the self node.
	 * This method is same as downloadFile(...) with minor modifications
	 * @param hostAddress	IP Address of the peer from which the file has to be replicated
	 * @param port			Port of the peer from which the file has to be replicated
	 * @param fileName		Name of the file to be replicated
	 * @return				Returns true if the file is successfully replicated else returns false
	 */
	public static boolean replicateFile(String hostAddress, int port, String fileName) {
		InputStream in = null;
		BufferedOutputStream fileOutput = null;
		ObjectOutputStream out = null;
		Socket socket = null;
		boolean isReplicated = false;
		LogUtility log = new LogUtility("replication");
		
		try {
			long startTime = System.currentTimeMillis();
			
			// Establish connection to the peer which contains the file for downloading.
			socket = new Socket(hostAddress, port);
			
			// Create a replica folder if it doesn't exist
			File file = new File(replicaLocation);
			if (!file.exists())
				file.mkdir();

			// Create an output stream using the socket's output stream.
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();

			log.write("Requesting file ... " + fileName);
			// Setup a Request object with Request Type = DOWNLOAD and Request Data = name of the file to be downloaded
			Request request = new Request();
			request.setRequestType("DOWNLOAD");
			request.setRequestData(fileName);
			out.writeObject(request);

			// Download file from the output stream
			log.write("Downloading file ... " + fileName);
			byte[] mybytearray = new byte[BUFFER_SIZE];
			in = socket.getInputStream();
			fileOutput = new BufferedOutputStream(new FileOutputStream(replicaLocation + fileName));
			
			int bytesRead;
			while ((bytesRead = in.read(mybytearray, 0, mybytearray.length)) > 0)
			{
				fileOutput.write(mybytearray, 0, bytesRead);
			}			
			
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
				// Closing all streams. Close the stream only if it is initialized
				if (out != null)
					out.close();
				
				if (in != null)
					in.close();
				
				if (fileOutput != null)
					fileOutput.close();
				
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

	/***
	 * This method prints the content of the text file after downloading it from the specified host (peer).
	 * @param hostAddress	IP Address of the peer from which the file has to be printed after downloading
	 * @param port			Port of the peer from which the file has to be printed after downloading
	 * @param fileName		Name of the text file to be printed after downloading
	 */
	public static void printFile(String fileName) {
		File file = new File(downloadLocation + fileName);
		if (file.exists()) {
			System.out.println("\nTHE FILE HAS BEEN DOWNLOADED TO THE downloads FOLDER IN THE CURRENT LOCATION");
			System.out.println("AND THE CONTENTS OF THE FILE ARE BELOW. PRINTING ONLY FIRST 1000 CHARACTERS.");
			System.out.println("=========================================================================");
			BufferedReader br = null;
			int charCount = 0;
			
			try {
				// Printing file using FileReader
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
			System.out.println("=========================================================================");
		} else {
			System.out.println("\nThe file could not be printed because it may not have been downloaded.");
		}
	}
}


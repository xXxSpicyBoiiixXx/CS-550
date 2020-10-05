/* FILE: Test.java
 * USEAGE: --
 * DESCRIPTION: Test for looking up files or downloading.
 * OPTIONS: --
 * REQUIREMENTS: -- 
 * BUGS: -- 
 * AUTHOR: xXxSpicyBoiiixXx (Md Ali)
 * ORGANIZATION: IIT
 * VERSION: 1.0
 * CREATED: 10/03/2020
 * REVISION: -- 
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Test {

	private final static int TEST_COUNT = 1000;
	
	public static void main(String[] args) {
		
		BufferedReader input = null;
        
		try {
			input = new BufferedReader(new InputStreamReader(System.in));
			String hostAddress, fileName;
			
			// Menu option for the user
			System.out.println("\nWhat do you want to test?");
			System.out.println("[1] Lookup");
			System.out.println("[2] Download");
			System.out.println("[3] Exit");
			System.out.print("Enter choice and press ENTER:");
			int option = 0;

			// Fail Safe
			try {
				option = Integer.parseInt(input.readLine());
			} catch (NumberFormatException e) {
				System.out.println("INVALID CHOICE. PLEASE CHOOSE A VALID OPTION.");
				System.exit(0);
			}

			switch (option) {
			case 1:
				System.out.println("\nEnter server address and name of the file you want to search:");
				hostAddress = input.readLine();
				fileName = input.readLine();
				(new LookupTest(hostAddress, fileName)).start();
				(new LookupTest(hostAddress, fileName)).start();
				(new LookupTest(hostAddress, fileName)).start();
				break;

			case 2:
				System.out.println("\nEnter client address and two file names you want to download:");
				hostAddress = input.readLine();
				String file1 = input.readLine();
				String file2 = input.readLine();
				
				(new DownloadTest(hostAddress, file1)).start();
				(new DownloadTest(hostAddress, file2)).start();
				break;
				
			case 3:
				System.out.println("Thanks for using this system.");
				System.exit(0);
				break;
			default:
				System.out.println("INVALID CHOICE. PLEASE CHOOSE A VALID OPTION.");
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class LookupTest extends Thread {
		private String serverAddress;
		private String fileName;
		
		public LookupTest(String host, String file) {
			this.serverAddress = host;
			this.fileName = file;
		}
		
		public void run() {
			Socket socket = null;
			ObjectInputStream in = null;
			ObjectOutputStream out = null;
			Request peerRequest = null;
			Response serverResponse	= null;
			long startTime, endTime, totalTime = 0;
			double avgTime;
			
			try {
				socket = new Socket(serverAddress, 10000);
				BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		        out = new ObjectOutputStream(socket.getOutputStream());
		        out.flush();
		        in = new ObjectInputStream(socket.getInputStream());
		        
				for (int i = 0; i < TEST_COUNT; i++) {
					startTime = System.currentTimeMillis();
					
					peerRequest = new Request();
					peerRequest.setRequestType("LOOKUP");
					peerRequest.setRequestData(fileName);
					out.writeObject(peerRequest);
					
					serverResponse = (Response) in.readObject();
					endTime = System.currentTimeMillis();
					totalTime += (endTime - startTime);
				}
				avgTime = (double) Math.round(totalTime / (double) TEST_COUNT) / 1000;

				System.out.println("Average lookup time for " + TEST_COUNT + " lookup requests is " + avgTime + " seconds.");
				input.readLine();
				this.interrupt();
			} catch (Exception e) {
				e.printStackTrace();
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
		}
	}
	
	private static class DownloadTest extends Thread {
		private String clientAddress;
		private String fileName;
		private static int counter = 1;
		
		public DownloadTest(String host, String file) {
			this.clientAddress = host;
			this.fileName = file;
		}
		
		public void run() {
			long startTime, endTime, totalTime = 0, totalFileSize = 0;
			double time, avgSpeed;
			System.out.println("Test Starting...");
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			
			try {
				for (int i = 0; i < TEST_COUNT; i++) {
					startTime = System.currentTimeMillis();
					FileUtility.downloadFile(clientAddress, 11000, fileName);
					endTime = System.currentTimeMillis();
					totalTime += (endTime - startTime);
					File file = new File("downloads/" + fileName);
					totalFileSize += file.length();
					file.delete();
				}
				time = (double) Math.round(totalTime / 1000.0);
				avgSpeed = (totalFileSize / (1024 * 1024)) / time;
				
				System.out.println("Average speed for downloading " + TEST_COUNT + " files is " + avgSpeed + " MBps. \nPress ENETER.");
				input.readLine();
				this.interrupt();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

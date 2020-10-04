/* FILE: Client.java
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Client {
	
	private static List<String> myIndexedLoc = Collections.synchronizedList(new ArrayList<String>());
	private static final String REPLICATION_PATH = "replica/";
	
	private static class PClient extends Thread {
		
		public void run() {
			
			Socket socket = null;
			ObjectInputStream in = null;
			BufferedReader input = null;
		}
 		
		

	}

}

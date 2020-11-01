import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogUtility {

    private String logFile = "";
    private BufferedWriter writer = null;
    private final String logLocation = "logs/";

    public LogUtility(String logType) {
        try {
            if (logType.equalsIgnoreCase("Peer")) {
                logFile = "download.log";
            } else if (logType.equalsIgnoreCase("Server")) {
                logFile = "server.log";
            } else if (logType.equalsIgnoreCase("Replication")) {
                logFile = "replication.log";
            }
            // Create a logs folder if it doesn't exist
            File file = new File(logLocation);
            if (!file.exists()) file.mkdir();

            writer = new BufferedWriter(new FileWriter(logLocation + logFile, true));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public boolean write(String logText) {
        boolean isWriteSuccess = false;
        try {
            String timeLog = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
            if (writer != null) {
                logText = String.format("%s => %s", timeLog, logText);
                writer.write(logText);
                String newline = System.getProperty("line.separator");
                writer.write(newline);
                isWriteSuccess = true;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return isWriteSuccess;
    }

    public void print() {
        BufferedReader br = null;
        File file = new File(logFile);
        int charCount = 0;

        System.out.println("\nLOG");

        try {
            br = new BufferedReader(new FileReader(logLocation + logFile));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                charCount += line.length();
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        if (charCount == 0) {
            System.out.println("NO LOGS TO PRINT");
        }

    }

    public void close() {
        try {
            if (writer != null) {
                String newline = System.getProperty("line.separator");
                writer.write(newline);
                writer.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    protected void finalize() throws Throwable {
        if (writer != null) {
            writer.close();
        }
        super.finalize();
    }

}
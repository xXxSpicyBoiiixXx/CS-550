import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileCheckSumMD5 {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        // Enter file name here. 
        String hex = checksum("/example/1.txt", md);
        System.out.println(hex);
    }

    private static String checksum(String filepath, MessageDigest md) throws IOException {
        try (InputStream fis = new FileInputStream(filepath)) {
            byte[] buffer = new byte[1024];
            int nread;
            while ((nread = fis.read(buffer)) != -1) {
                md.update(buffer, 0, nread);
            }
        }

        // bytes to hex
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();

    }

}
import java.net.*;
import java.util.*;

public class NetworkUtility {

    public static void main(String args[]) throws SocketException {
        System.out.println(getLocalAddress());
    }

    public static String getLocalAddress() {
        String localAddress = null;
        String loopbackAddress = InetAddress.getLoopbackAddress().getHostAddress();
        boolean found = false;

        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    if (inetAddress instanceof Inet4Address && !inetAddress.getHostAddress().equals(loopbackAddress)) {
                        localAddress = inetAddress.getHostAddress();
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return localAddress;
    }

}
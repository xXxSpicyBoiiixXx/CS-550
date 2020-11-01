import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPAddressValidator {
    private static Pattern pattern;
    private static Matcher matcher;

    private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public static boolean validate(final String ip){
        pattern = Pattern.compile(IPADDRESS_PATTERN);
        matcher = pattern.matcher(ip);

        return matcher.matches();
    }

    public static void main(String[] args){
        // Just using local IP at the moment
        System.out.println("127.0.0.1");
    }
}

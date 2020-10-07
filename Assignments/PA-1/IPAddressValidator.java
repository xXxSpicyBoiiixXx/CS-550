/* FILE: IPAddressValidator.java
 * USEAGE: --
 * DESCRIPTION: Showcases if the IP is valid, if not it will return false. I have put the local host as the validation.
 * OPTIONS: --
 * REQUIREMENTS: -- 
 * BUGS: -- 
 * AUTHOR: xXxSpicyBoiiixXx (Md Ali)
 * ORGANIZATION: IIT
 * VERSION: 1.0
 * CREATED: 10/02/2020
 * REVISION: -- 
*/

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPAddressValidator {
  private static Pattern pattern;
  private static Matcher matcher;

  private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

  public static boolean validate(final String ip) {
    pattern = Pattern.compile(IPADDRESS_PATTERN);
    matcher = pattern.matcher(ip);
    return matcher.matches();
  }

  // Hardcoded in localhost, but can be what you need it to be. 
  public static void main(String[] args) {
    System.out.println(validate("127.0.0.1"));
  }
}
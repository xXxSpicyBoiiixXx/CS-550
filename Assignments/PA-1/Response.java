/* FILE: Response.java
 * USEAGE: --
 * DESCRIPTION: This will output the response.
 * OPTIONS: --
 * REQUIREMENTS: -- 
 * BUGS: -- 
 * AUTHOR: xXxSpicyBoiiixXx (Md Ali)
 * ORGANIZATION: IIT
 * VERSION: 1.0
 * CREATED: 10/03/2020
 * REVISION: -- 
*/


import java.io.Serializable;

public class Response implements Serializable {

	private int responseCode;
	private Object responseData;
	
	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public Object getResponseData() {
		return responseData;
	}

	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}

}

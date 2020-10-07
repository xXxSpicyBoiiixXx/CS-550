/* FILE: Response.java
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

import java.io.Serializable;

public class Response implements Serializable {
	
	private int responseCode; 
	private String responseData;
	private Object otherData;
	
	public int getResponsesCode() 
	{
		return responseCode;
	}
	
	public void setResponseCode(int responseCode)
	{
		this.responseCode = responseCode;
	}
	
	public String getResponseData() 
	{
		return responseData;
	}
	
	public void setResponseData(String responseData)
	{
		this.responseData = responseData; 
	}
	
	public Object getOtherData() 
	{
		return otherData; 
	}
	
	public void setOtherData(Object otherData) 
	{
		this.otherData = otherData; 
	}
}

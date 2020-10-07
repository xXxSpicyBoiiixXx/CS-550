/* FILE: Request.java
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

public class Request implements Serializable {
	
	private String requestType;
	private Object requestData;
	
	public String getRequestType() 
	{
		return requestType;
	}
	
	public void setRequestType(String requestType)
	{
		this.requestType = requestType;
	}
	
	public Object getRequestData() 
	{
		return requestData;
	}
	
	public void setRequestData(Object requestData)
	{
		this.requestData = requestData;
	}

}

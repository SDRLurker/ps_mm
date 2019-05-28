import java.security.*;

class md5code{
private MessageDigest md = null; 
static private md5code md5 = null; 
private static final char[] hexChars ={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'}; 

/** 
* Constructor is private so you must use the getInstance method 
*/ 
private md5code() throws NoSuchAlgorithmException 
{ 
md = MessageDigest.getInstance("MD5"); 
} 


/** 
* This returns the singleton instance 
*/ 
public static md5code getInstance()throws NoSuchAlgorithmException 
{ 

if (md5 == null) 
{ 
md5 = new md5code(); 
} 

return (md5); 
} 

public String hashData(byte[] dataToHash) 
{ 
return hexStringFromBytes((calculateHash(dataToHash))); 
} 


private byte[] calculateHash(byte[] dataToHash) 
{ 
md.update(dataToHash, 0, dataToHash.length); 
return (md.digest()); 
} 


public String hexStringFromBytes(byte[] b) 
{ 
String hex = ""; 
int msb; 
int lsb = 0; 
int i; 
// MSB maps to idx 0 
for (i = 0; i < b.length; i++) 
{ 
msb = ((int)b[i] & 0x000000FF) / 16; 
lsb = ((int)b[i] & 0x000000FF) % 16; 
hex = hex + hexChars[msb] + hexChars[lsb]; 
} 
return(hex); 
} 
} //end of class

import java.io.*;
import java.net.*;

class login extends Thread{
 Socket ns;
 private InputStream is;
 private DataInputStream dis;
 // private BufferedReader dis;
 private OutputStream os;
 private DataOutputStream dos;
 private String nsad = "messenger.hotmail.com";
 public String id,password,smnemonic=null,rmnemonic = null,param,nickname=""; // r네모닉 받을 명령어, s네모닉 보낼 명령어
 static int trialid=0;
 int gtcnum = -1;
 public boolean error = false;

 public login(String id, String password){
  try{
   ns = new Socket(nsad,1863);
   is = ns.getInputStream();
   // dis = new BufferedReader(new InputStreamReader(ns.getInputStream()));
   dis = new DataInputStream(is);
   os = ns.getOutputStream();
   dos = new DataOutputStream(os);
   this.id = id;
   this.password = password;
  }catch(Exception e){
   System.out.println(e); error = true; trialid=-1;
  }  
 }

 public login(String id, String password, String nsad){
  try{
   this.nsad = nsad;
   ns = new Socket(nsad,1863);
   is = ns.getInputStream();
   // dis = new BufferedReader(new InputStreamReader(ns.getInputStream()));
   dis = new DataInputStream(is);
   os = ns.getOutputStream();
   dos = new DataOutputStream(os);
   this.id = id;
   this.password = password;   
  }catch(Exception e){
   System.out.println(e); error = true; trialid=-1;
  }  
 }

 public void close(){
  try{
   ns.close();
  }catch(Exception e){
   trialid=-1;
  }
 }

 private int location(String received,int space2){
  int pointer,space;
  for(pointer=0,space=0;pointer<=received.length()-1 && space<space2;pointer++)
   if(received.charAt(pointer)==' ') space++;
  return pointer;
 }

 private int location2(String received,int space2){
  int pointer,space;
  for(pointer=0,space=0;pointer<=received.length()-1 && space<space2;pointer++)
   if(received.charAt(pointer)==',') space++;
  return pointer;
 }

 public void readCookie(){
  try{
   String cookie = dis.readLine();
   while(cookie.length()!=0){
    System.out.println(cookie);
    cookie = dis.readLine();
   }
  }catch(Exception e){
   System.out.println(e); error = true;    
  }
 }

 public void send(String mnemonic){
  String send,send2;
  try{
   if(mnemonic.equals("VER")){
    send = mnemonic + " " + trialid + " MSNP8 CVRO";
    System.out.println(send);
    dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);
   }
   if(mnemonic.equals("XFR")){
    send = mnemonic + " " + trialid + " SB";
    System.out.println(send);
    dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);
   }
   if(mnemonic.equals("CVR")){
    send = mnemonic + " " + trialid + " 0x0412 winnt 5.1 i386 MSNMSGR 5.0.0515 MSMSGS YourEmail";
    System.out.println(send);
    dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);
   }
   if(mnemonic.equals("USR")){
    if(rmnemonic==null || rmnemonic.equals("CVR")){
     send = mnemonic + " " + trialid + " TWN I " + id;
     System.out.println(send);
     dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa); 
    }else{
     try{
      Socket https;
      if(id.substring(id.length()-7,id.length()).equals("msn.com")) https = new Socket("msnialogin.passport.com",80);
      else if(id.substring(id.length()-11,id.length()).equals("hotmail.com")) https = new Socket("loginnet.passport.com",80);
      else https = new Socket("login.passport.com",80);
      DataInputStream udis = new DataInputStream(https.getInputStream());
      DataOutputStream udos = new DataOutputStream(https.getOutputStream());
      udos.writeBytes("GET /login2.srf HTTP/1.1"); udos.writeByte(0xd); udos.writeByte(0xa);
      udos.writeBytes("Host: login.passport.com"); udos.writeByte(0xd); udos.writeByte(0xa);
      udos.writeBytes("User-Agent: SDRLurker"); udos.writeByte(0xd); udos.writeByte(0xa);
      String idimsi="";
      for(int i=0;i<id.length();i++) if(id.charAt(i)=='@') idimsi = idimsi + "%40"; else idimsi = idimsi + id.charAt(i);
      udos.writeBytes("Authorization: Passport1.4 OrgVerb=GET,OrgURL=http%3A%2F%2Fmessenger%2Emsn%2Ecom,sign-in=" + idimsi + ",pwd=" + password + "," + param); udos.writeByte(0xd); udos.writeByte(0xa);
      udos.writeByte(0xd); udos.writeByte(0xa); 
      String imsi="", sendinfo="";
      imsi = udis.readLine();
      while(imsi.length()>0){
       // System.out.println(imsi);
       imsi = udis.readLine();
       if(imsi.length()>=20) if(imsi.substring(0,20).equals("Authentication-Info:")) sendinfo = imsi.substring(location2(imsi,4)+9,location2(imsi,5)-2);
      }
      send = mnemonic + " " + trialid + " TWN S " + sendinfo;
      System.out.println(send);
      dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);
     }catch(Exception e){
      System.out.println(e);
     }

    }
   }
   if(mnemonic.equals("SYN")){
    send = mnemonic + " " + trialid + " 0";
    System.out.println(send);
    dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);
   }
   if(mnemonic.equals("QRY")){
    send = mnemonic + " " + trialid + " PROD0038W!61ZTF9 32";
    send2 = param + "VT6PX?UQTM4WM%YR";
    md5code md = md5code.getInstance();
    send2 = md.hashData(send2.getBytes());
    System.out.println(send);
    System.out.println(send2);
    dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa); dos.writeBytes(send2);
   }
   if(mnemonic.equals("OUT")){
    send = mnemonic;
    System.out.println(send);
    dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa); 
    trialid=-1;    
   }
  }catch(Exception e){
   System.out.println(e); error = true; trialid=-1;
  }finally{
   trialid++;
  }  
 }

 public void send(String mnemonic,String prmeter){
  String send = "",r = "";
  // byte rr[] = new byte[14];

  int total=0, divided=0, point=0, atext;
  try{
   if(mnemonic.equals("GTC")){
    send = mnemonic + " " + trialid + " " + prmeter;
    System.out.println(send);     
    dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);     
   }
   if(mnemonic.equals("CHG")){
    send = mnemonic + " " + trialid + " " + prmeter;
    System.out.println(send);     
    dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);     
   }
   if(mnemonic.equals("ADD")){
    smnemonic = "ADD";
    send = mnemonic + " " + trialid + " " + prmeter + " " + param + " " + param;
    System.out.println(send);     
    dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);     
   }
   if(mnemonic.equals("REM")){
    smnemonic = "REM";
    send = mnemonic + " " + trialid + " " + prmeter + " " + param;
    System.out.println(send);     
    dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);     
   }
   if(mnemonic.equals("REA")){
    for(int i=0;i<prmeter.length();i++){
     if((int)prmeter.charAt(i)>=0 && (int)prmeter.charAt(i)<=0x7F) total = total + 1;
     if((int)prmeter.charAt(i)>=0x80 && (int)prmeter.charAt(i)<=0x7FF) total = total + 2;
     if((int)prmeter.charAt(i)>=0x800 && (int)prmeter.charAt(i)<=0xFFFF) total = total + 3;
     if(prmeter.charAt(i) == '%') total = total + 2;
     if(prmeter.charAt(i) == ' ') total = total + 2;
    }
    byte output[] = new byte[total];
    System.out.println(total);
    boolean checking[] = new boolean[16];
    for(int i=0;i<checking.length;i++) checking[i]=false;
    for(int i=0;i<prmeter.length();i++){
     divided = (int)prmeter.charAt(i);
     for(int j=0;j<16;j++) checking[j] = false;
     if((int)prmeter.charAt(i)>=0 && (int)prmeter.charAt(i)<=0x7F && prmeter.charAt(i)!='%' && prmeter.charAt(i)!=' ') { output[point] = (byte)prmeter.charAt(i); point++; }
     if(prmeter.charAt(i) == '%') {
      output[point] = (byte)'%';
      point++; output[point] = (byte)'2';
      point++; output[point] = (byte)'5';
      point++; 
     }
     if(prmeter.charAt(i) == ' ') {
      output[point] = (byte)'%';
      point++; output[point] = (byte)'2';
      point++; output[point] = (byte)'0';
      point++; 
     }
     if((int)prmeter.charAt(i)>=0x80 && (int)prmeter.charAt(i)<=0x7FF) {
      for(int k=15;divided>0;divided = divided/2,k--) if(divided%2==1) checking[k] = true;
      atext=192;
      for(int l=0;l<5;l++) if(checking[l]) atext = atext + (int)Math.pow(2,4-l);
      output[point] = (byte)atext; point++;
      atext=128;
      for(int l=5;l<11;l++) if(checking[l]) atext = atext + (int)Math.pow(2,10-l);
      output[point] = (byte)atext; point++;
     }
     if((int)prmeter.charAt(i)>=0x800){
      for(int k=15;divided>0;divided = divided/2,k--) if(divided%2==1) checking[k] = true;
      atext=224;
      for(int l=0;l<4;l++) if(checking[l]) atext = atext + (int)Math.pow(2,3-l);
      output[point] = (byte)atext; point++;
      atext=128;
      for(int l=4;l<10;l++) if(checking[l]) atext = atext + (int)Math.pow(2,9-l);
      output[point] = (byte)atext; point++;
      atext=128;
      for(int l=10;l<16;l++) if(checking[l]) atext = atext + (int)Math.pow(2,15-l);
      output[point] = (byte)atext; point++;
     }
    }
    send = mnemonic + " " + trialid + " " + id + " ";
    System.out.print(send + prmeter);
    dos.writeBytes(send);
    for(int j=0;j<point;j++) dos.writeByte(output[j]);
    System.out.println();
    dos.writeByte(0xd); dos.writeByte(0xa);     
   }  
  }catch(Exception e){
   System.out.println(e); error = true; trialid=-1;
  }finally{
   trialid++;
  }  
 }

 public String receive(){
  String receive = null;
  try{
   receive = dis.readLine();
   System.out.println(receive);   
  }catch(Exception e){
   System.out.println(e); error = true;
  }
  return receive;
 }

 public byte[] receiveBytes(){
  byte[] r=null,s;
  int i,j;
  try{
   s = new byte[500];
   s[0] = dis.readByte();
   for(i=0; s[i]!=0xd; i++) s[i+1] = dis.readByte();
   i++; s[i+1] = dis.readByte();
   j=i;
   r = new byte[j];
   for(i=0;i<j;i++) r[i] = s[i];
  }catch(Exception e){
   System.out.println(e); error = true;
  }
  return r;  
 }

 public String[] needed(String receive){
  String[] parameters;
  parameters = new String[3];
  for(int i=0;i<parameters.length;i++) parameters[i] = "";
  int sp1,sp2,sp3,sp4,sp5,sp6;
  if(receive.length()>=3){
   if(receive.substring(0,3).equals("209")) rmnemonic = "209"; //확인되지 않은 E-mail주소
   if(receive.substring(0,3).equals("215")) rmnemonic = "215"; //사람 추가 에러
   if(receive.substring(0,3).equals("216")) rmnemonic = "216"; //사람 제거 에러
   if(receive.substring(0,3).equals("911")){ rmnemonic = "911"; error=true; trialid=0; } 
   if(receive.substring(0,3).equals("919")){ rmnemonic = "919"; error=true; trialid=0; }
   if(receive.substring(0,3).equals("928")){ rmnemonic = "928"; error=true; trialid=0; }
   if(receive.substring(0,3).equals("500")){ rmnemonic = "500"; error=true; trialid=0; } 
   if(receive.substring(0,3).equals("601")){ rmnemonic = "601"; error=true; trialid=0; }// 여기까지 에러코드

   if(receive.substring(0,3).equals("INF"))  rmnemonic = "INF";
   if(receive.substring(0,3).equals("REA"))  rmnemonic = "REA";
   if(receive.substring(0,3).equals("CVR"))  rmnemonic = "CVR";
   if(receive.substring(0,3).equals("QRY"))  rmnemonic = "QRY";
   if(receive.substring(0,3).equals("USR")){
    rmnemonic = "USR";
    sp1 = location(receive,2);
    sp2 = location(receive,3);
    if(receive.substring(sp1,sp2-1).equals("TWN")){
     // parameters = new String[1];
     sp3 = location(receive,4);
     param = receive.substring(sp3,receive.length());
    }
   }
   if(receive.substring(0,3).equals("XFR")){
    rmnemonic = "XFR";
    // parameters = new String[1];
    sp1 = location(receive,3);
    sp2 = location(receive,4);
    parameters[0] = receive.substring(sp1,sp2-6);
   }
   if(receive.substring(0,3).equals("CHL")){
    rmnemonic = "CHL";
    sp1 = location(receive,2);
    param = receive.substring(sp1,receive.length());
    send("QRY");
   }
   if(receive.substring(0,3).equals("GTC")){
    rmnemonic = "GTC";
    sp1 = location(receive,3);
    param = receive.substring(sp1,receive.length());
   }
   if(receive.substring(0,3).equals("ADD")){
    rmnemonic = "ADD";
    sp1 = location(receive,2);
    sp2 = location(receive,3);
    sp3 = location(receive,4);
    sp4 = location(receive,5);
    // parameters = new String[3];
    parameters[0] = receive.substring(sp1,sp2-1);
    parameters[1] = receive.substring(sp3,sp4-1);
    parameters[2] = receive.substring(sp4,receive.length());
    param = parameters[1];
   }
   if(receive.substring(0,3).equals("REM")){
    rmnemonic = "REM";
    sp1 = location(receive,2);
    sp2 = location(receive,3);
    sp3 = location(receive,4);
    // parameters = new String[2];
    parameters[0] = receive.substring(sp1,sp2-1);
    parameters[1] = receive.substring(sp3,receive.length());
    param = receive.substring(sp3,receive.length());
   }
   if(receive.substring(0,3).equals("RNG")){
    rmnemonic = "RNG";
    sp1 = location(receive,1);
    sp2 = location(receive,2);
    sp3 = location(receive,3);
    sp4 = location(receive,4);
    sp5 = location(receive,5);
    sp6 = location(receive,6);
    parameters[0] = receive.substring(sp1,sp2-1);
    parameters[1] = receive.substring(sp2,sp3-6);
    parameters[2] = receive.substring(sp4,sp5-1);
    param = receive.substring(sp5,sp6-1);
   }
   if(receive.substring(0,3).equals("LST")){
    rmnemonic = "LST";
    sp1 = location(receive,1);
    sp2 = location(receive,2);
    sp3 = location(receive,3);
    parameters[0] = receive.substring(sp1,sp2-1);
    parameters[1] = receive.substring(sp2,sp3-1);
    parameters[2] = receive.substring(sp3,receive.length());
    param = parameters[0];
   }
  }
  return parameters;
 }

 public String[] need(byte[] receive){
  String[] parameters;
  String nickname2="";
  parameters = new String[3];
  for(int i=0;i<parameters.length;i++) parameters[i] = "";
  int sp1,sp2,sp3,sp4;
  try{
   String received = new String(receive,"UTF8");
   System.out.println(received);
   if(received.substring(0,3).equals("USR")){
    rmnemonic = "USR";
    sp1 = location(received,2);
    sp2 = location(received,3);
    if(received.substring(sp1,sp2-1).equals("OK")){
     sp3 = location(received,4);
     sp4 = location(received,5);
     nickname2 = received.substring(sp3,sp4-1);

     for(int i=0;i<nickname2.length()-2;i++)
      if(nickname2.substring(i,i+3).equals("%20")) { nickname = nickname + " "; i = i + 2; }
      else nickname = nickname + nickname2.charAt(i);
     if(nickname2.length()>2) { if(!nickname2.substring(nickname2.length()-3,nickname2.length()).equals("%20")) nickname = nickname + nickname2.substring(nickname2.length()-2,nickname2.length()); }
     else nickname = nickname + nickname2.substring(0,nickname2.length());

    }
   }
  }catch(Exception e){ System.out.println(e); }
  return parameters;
 }
}
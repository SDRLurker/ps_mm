import java.io.*;
import java.net.*;
import java.util.*;

class chat extends Thread{
 private String ip,id,key1,key2;
 public String user_id;
 private InputStream is;
 private DataInputStream dis;
 private static URLConnection u;
 private static BufferedReader udis;
 private OutputStream os;
 private DataOutputStream dos;
 private Socket cs;
 private int trialid=0,users=0;
 private boolean Readline=true,msgcheck=false;
 private String rmnemonic; //���� ��ɾ�
 private String msg=""; //���� �޼���
 private int msgcount=0,msgnum=0; //���� �޼��� ī����
 private boolean status=true;
 public String mode=""; //���

 private String answer="",provider = "Computer",said=null; 
 private boolean ps_zero = false, ps_16 = false, mq = false;// ����Ʈ����ũ�� ����
 private String rotto_ans; // �ζǿ� ����
 private int score=90,pokerstatus=1;
 private int[][] cards = new int[5][2];
 private int[] temp = new int[5]; // ��ī�� ����
 private int hang_score=0, hang_turn=-1;
 private String nickname="",quest="",hint="",quest_see=""; // ��ǿ� ����
 private int poll_num, poll_status=0, poll_turn=-1; 
 private String q1="",q2="",a1="",a2=""; 
 private boolean modify=false, urlcheck_poll=false;// ��������� ����
 private boolean yang=false, ma=false; //�����º�ȯ�� ����
 public int inv_ck,filesize,your_port,auth_ck;
 public String filename;
 private byte pds_status=0;
 public byte pds_num=0;
 public String pds_title, pds_con="", pds_command, your_ip;
 public boolean upload=false; 
 private MSNFTPup msnftpup; //���ε�� ����
 
 public chat(String ip, String id, String key1, String key2, String user_id){
  try{
   this.ip = ip;
   this.id = id;
   this.key1 = key1;
   this.key2 = key2;
   this.user_id = user_id;
   cs = new Socket(ip,1863);
   is = cs.getInputStream();
   dis = new DataInputStream(is);
   // dis = new BufferedReader(new InputStreamReader(cs.getInputStream()));
   os = cs.getOutputStream();
   dos = new DataOutputStream(os);
   System.out.println("Switch Board Server�� �����ϴ�...( " + ip +" )");
  }catch(Exception e){
  }
 }

 public void close(){
  try{
   cs.close();
  }catch(Exception e){ }
 }

 public void send(String mnemonic){
  String send;
  try{
   if(mnemonic.equals("ANS")){
    send = mnemonic + " " + trialid + " " + id + " " + key1 + " " + key2;
    // System.out.println(send);     
    dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);
   }
   if(mnemonic.equals("OUT")){
    send = mnemonic;
    // System.out.println(send);     
    dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);
    status = false;
   }
   if(mnemonic.equals("cancel")){
    int imsi = 143 + Integer.toString(inv_ck).length();
    send = "MSG " + trialid + " N " + imsi;
    dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);
    dos.writeBytes("MIME-Version: 1.0"); dos.writeByte(0xd); dos.writeByte(0xa);
    dos.writeBytes("Content-Type: text/x-msmsgsinvite; charset=UTF-8"); dos.writeByte(0xd); dos.writeByte(0xa);
    dos.writeByte(0xd); dos.writeByte(0xa);
    dos.writeBytes("Invitation-Command: CANCEL"); dos.writeByte(0xd); dos.writeByte(0xa);
    send = "Invitation-Cookie: " + inv_ck;
    dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);
    dos.writeBytes("Cancel-Code: REJECT"); dos.writeByte(0xd); dos.writeByte(0xa);
    dos.writeByte(0xd); dos.writeByte(0xa);
   }
   if(mnemonic.equals("accept")){
    int imsi = 177 + Integer.toString(inv_ck).length();
    send = "MSG " + trialid + " N " + imsi;
    dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);
    dos.writeBytes("MIME-Version: 1.0"); dos.writeByte(0xd); dos.writeByte(0xa);
    dos.writeBytes("Content-Type: text/x-msmsgsinvite; charset=UTF-8"); dos.writeByte(0xd); dos.writeByte(0xa);
    dos.writeByte(0xd); dos.writeByte(0xa);
    dos.writeBytes("Invitation-Command: ACCEPT"); dos.writeByte(0xd); dos.writeByte(0xa);
    send = "Invitation-Cookie: " + inv_ck;
    dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);
    dos.writeBytes("Launch-Application: FALSE"); dos.writeByte(0xd); dos.writeByte(0xa);
    dos.writeBytes("Request-Data: IP-Address: "); dos.writeByte(0xd); dos.writeByte(0xa);
    dos.writeByte(0xd); dos.writeByte(0xa);
   }
  }catch(Exception e){   
  }finally{
   trialid++;
  }  
 }

 public String receive(){
  String receive = null;
  try{
   if(!msgcheck){
   receive = dis.readLine();
   // System.out.println(receive);
   }
  }catch(Exception e){

  }
  return receive;
 }

 private int location(String received,int space2){
  int pointer,space;
  for(pointer=0,space=0;pointer<=received.length()-1 && space<space2;pointer++)
   if(received.charAt(pointer)==' ') space++;
  return pointer;
 }

 public void sendmsg(String sendmsgs,String Color){
  int length = 121;
  length = length + sendmsgs.length() + Color.length() + 4;
  String send = null;
  try{
   msgcheck = true;
   send = "MSG " + trialid + " N " + length;
   // System.out.println(send);
   dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);
   // System.out.println("MIME-Version: 1.0");
   dos.writeBytes("MIME-Version: 1.0"); dos.writeByte(0xd); dos.writeByte(0xa);  
   // System.out.println("Content-Type: text/plain; charset=UTF-8");
   dos.writeBytes("Content-Type: text/plain; charset=UTF-8"); dos.writeByte(0xd); dos.writeByte(0xa);  
   send = "X-MMS-IM-Format: FN=%EA%B5%B4%EB%A6%BC; EF=; CO=" + Color + "; CS=0; PF=22";
   dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa); dos.writeByte(0xd); dos.writeByte(0xa);
   // System.out.println(sendmsgs);
   dos.writeBytes(sendmsgs);
  }catch(Exception e){
   System.out.println(e);
  }finally{
   trialid++;
   msgcheck = false;
  }
 }

 public void sendmsg2(String sendmsgs,String Color){
  int length;
  String send = null;
  try{
   msgcheck = true;
   int total=0, point=0, divided=0,atext;
   for(int i=0;i<sendmsgs.length();i++){
    if((int)sendmsgs.charAt(i)>=0 && (int)sendmsgs.charAt(i)<=0x7F) total = total + 1;
    if((int)sendmsgs.charAt(i)>=0x80 && (int)sendmsgs.charAt(i)<=0x7FF) total = total + 2;
    if((int)sendmsgs.charAt(i)>=0x800 && (int)sendmsgs.charAt(i)<=0xFFFF) total = total + 3;
   }
   byte output[] = new byte[total];
   boolean checking[] = new boolean[16];
   for(int i=0;i<sendmsgs.length();i++){
    divided = (int)sendmsgs.charAt(i);
    for(int j=0;j<16;j++) checking[j] = false;
    if((int)sendmsgs.charAt(i)>=0 && (int)sendmsgs.charAt(i)<=0x7F) { output[point] = (byte)sendmsgs.charAt(i); point++; }
    if((int)sendmsgs.charAt(i)>=0x80 && (int)sendmsgs.charAt(i)<=0x7FF) {
     for(int k=15;divided>0;divided = divided/2,k--) if(divided%2==1) checking[k] = true;
     atext=192;
     for(int l=0;l<5;l++) if(checking[l]) atext = atext + (int)Math.pow(2,4-l);
     output[point] = (byte)atext; point++;
     atext=128;
     for(int l=5;l<11;l++) if(checking[l]) atext = atext + (int)Math.pow(2,10-l);
     output[point] = (byte)atext; point++;
    }
    if((int)sendmsgs.charAt(i)>=0x800){
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
   length = 125 + total + Color.length();
   send = "MSG " + trialid + " N " + length;
   // System.out.println(send);
   dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa);
   // System.out.println("MIME-Version: 1.0");
   dos.writeBytes("MIME-Version: 1.0"); dos.writeByte(0xd); dos.writeByte(0xa);  
   // System.out.println("Content-Type: text/plain; charset=UTF-8");
   dos.writeBytes("Content-Type: text/plain; charset=UTF-8"); dos.writeByte(0xd); dos.writeByte(0xa);  
   send = "X-MMS-IM-Format: FN=%EA%B5%B4%EB%A6%BC; EF=; CO=" + Color + "; CS=0; PF=22";
   dos.writeBytes(send); dos.writeByte(0xd); dos.writeByte(0xa); dos.writeByte(0xd); dos.writeByte(0xa);
   for(int p=0;p<total;p++) dos.writeByte(output[p]);
  }catch(Exception e){ System.out.println(e); }finally{
   trialid++;
   msgcheck = false;
  }
 }

/* ��/���� ��ȯ */
 public String calendar(boolean to, String dates)
 {
  // URLConnection u;
  // BufferedReader udis;
  String r="";
  try{
   if(to) u = new URL("http://www.ajumma.co.kr/transday.php3?ko=" + dates + "&sel=sol").openConnection();
   else u = new URL("http://www.ajumma.co.kr/transday.php3?ko=" + dates + "&sel=lun").openConnection();
   udis = new BufferedReader(new InputStreamReader(u.getInputStream()));
   for(int i=0;i<19;i++) udis.readLine();
   r = udis.readLine();
   r = r.substring(34,r.length()-88);
  }catch(Exception e){ System.out.println(e); }   
   return r;  
 }

/* ��� */
 public void make_hang(){
  hang_turn=5;
  try{
   u = new URL("http://vam.new21.net/ps/hangman/stage.txt").openConnection();
   udis = new BufferedReader(new InputStreamReader(u.getInputStream()));
   int total = Integer.parseInt(udis.readLine());
   int random = (int)(Math.random()*10000%total)+1;
   for(int i=0;i<random-1;i++){
    udis.readLine();
    udis.readLine();
   }
   quest = udis.readLine();
   hint = udis.readLine();
  }catch(Exception e){ System.out.println(e); }
 }

 public void see_hang(){
  String temp = quest_see;
  quest_see="";
  for(int i=0;i<quest.length();i++)
   if(quest.charAt(i) == msg.toLowerCase().charAt(0) || temp.length() == quest.length() * 2 && quest.charAt(i) == temp.charAt(2*i)) quest_see = quest_see + quest.charAt(i) + " ";   
   else quest_see = quest_see + "_ ";
 }
/* �ζ� */
 public void rotto(int i){
  int a=1,b=1,c=1,d=1,e=1,f=1,temp;
  while(a==b||a==c||a==d||a==e||a==f||b==c||b==d||b==e||b==f||c==d||c==e||c==f||d==e||d==f||e==f){
   a = (int)(Math.random()*1000%45)+1;
   b = (int)(Math.random()*1000%45)+1;
   c = (int)(Math.random()*1000%45)+1;
   d = (int)(Math.random()*1000%45)+1;
   e = (int)(Math.random()*1000%45)+1;
   f = (int)(Math.random()*1000%45)+1;
  }
  if(i==1) rotto_ans = Integer.toString(a);
  if(i==6){
   if(a>b) { temp = a; a = b; b= temp; } if(a>c) { temp = a; a = c; c= temp; } if(a>d) { temp = a; a = d; d= temp; } if(a>e) { temp = a; a = e; e= temp; } if(a>f) { temp = a; a = f; f= temp; }
   if(b>c) { temp = b; b = c; c= temp; } if(b>d) { temp = b; b = d; d= temp; } if(b>e) { temp = b; b = e; e= temp; } if(b>f) { temp = b; b = f; f= temp; }
   if(c>d) { temp = c; c = d; d= temp; } if(c>e) { temp = c; c = e; e= temp; } if(c>f) { temp = c; c = f; f= temp; }
   if(d>e) { temp = d; d = e; e= temp; } if(d>f) { temp = d; d = f; f= temp; }
   if(e>f) { temp = e; e = f; f= temp; }
   rotto_ans = a + " - " + b + " - " + c + " - " + d + " - " + e + " - " + f;
  }
 }
/* ����Ʈ����ũ ����*/

 public void make(boolean zero,boolean hex){
 int a=1,b=1,c=1,d,e;
 if(zero==true) e=0;
 else e=1;
 if(hex==true) d=16-e;
 else d=10-e;
  while(a==b || b==c || c==a){
   a = (int)(Math.random()*1000%d)+e;
   b = (int)(Math.random()*1000%d)+e;
   c = (int)(Math.random()*1000%d)+e;
  }
 
  answer = Integer.toHexString(a) + Integer.toHexString(b) + Integer.toHexString(c);
  // System.out.println(answer);
 }

/* public void poll_check(boolean m)
 {
  try{
   int i=0;
   String temp="";
   a2 = msg;
   // URLConnection u;

   for(i=0;i<a1.length();i++) if(a1.charAt(i)==' ') temp = temp + "%20"; else temp = temp + a1.charAt(i);
   a1=temp; temp="";
   for(i=0;i<a2.length();i++) if(a2.charAt(i)==' ') temp = temp + "%20"; else temp = temp + a2.charAt(i);
   a2=temp;
   
   if(!m) u = new URL("http://vam.new21.net/ps/input.php?num=" + poll_num + "&a1=" + a1 + "&a2=" + a2 + "&user_id=" + user_id).openConnection();
   else u = new URL("http://vam.new21.net/ps/modify.php?num=" + poll_num + "&a1=" + a1 + "&a2=" + a2 + "&user_id=" + user_id).openConnection();
   // System.out.println(m);

   BufferedReader udis = new BufferedReader(new InputStreamReader(u.getInputStream()));
   if(udis.readLine().equals("0")) sendmsg2("���������������������� ���� �˸� ����������������������      �亯 �����մϴ�... �������� ��Ͽ� �����߽��ϴ�!!������������������������������������������������������","FF0000");
   else sendmsg2("������������������������ ��    �� ������������������������      �����ͺ��̽� ���� �����Դϴ�... �ٽ� �õ����ּ���...��������������������������������������������������������","0000FF");
   poll_status=3;
   if(!modify) { mode=""; sendFile("opening.dat"); }
   modify=false;
  }catch(Exception e){
   System.out.println(e);
   sendmsg2("�������������� ��    �� ��������������        �������� ��� �����Դϴ�...������������������������������������","0000FF");
   poll_status=3;
   if(!modify) { mode=""; sendFile("opening.dat"); }
   modify=false;
  }
 }

 public void poll()
 {
  try{
   // URLConnection u;
   RandomAccessFile poll = new RandomAccessFile("poll.dat","r");
   poll_num = Integer.parseInt(poll.readLine());
   q1 = poll.readLine();
   q2 = poll.readLine();
   poll.close();
   if(!urlcheck_poll){
    u = new URL("http://vam.new21.net/ps/search.php?id=" + user_id + "&num=" + poll_num).openConnection();
    udis = new BufferedReader(new InputStreamReader(u.getInputStream()));
    poll_turn = Integer.parseInt(udis.readLine());
    urlcheck_poll=true;
   }
   if(poll_status==0 && mode.equals("��������")){
    if(poll_turn==0){
     sendFile("open.dat"); sendFile("research.dat"); sendmsg(q1,"0"); msgnum = msgcount; poll_status=1;
    }else if(msgcount!=0)
    { sendFile("research2.dat"); msgnum = msgcount; poll_status=3; }
    else if(msgcount==0) { sendFile("opening.dat"); mode=""; }
   }
   if(poll_status==1 && msgcount >= msgnum+1 && mode.equals("��������")){ 
    if(msg.length()>=2) if(!msg.trim().equals("�ڷ��") && !msg.trim().toLowerCase().equals("ps") && !msg.trim().equals("���") && !msg.trim().equals("�ζ�") && !msg.trim().equals("��ɾ�") && !msg.trim().equals("���") && !msg.trim().equals("�ð�") && !msg.substring(0,2).equals("����") && !msg.substring(0,2).equals("���") && !msg.trim().equals("�ð�")){ a1= msg; if(!msg.trim().equals("��������")) sendmsg(q2,"0"); msgnum = msgcount; poll_status=2; }
    if(msg.length()<2) if(!msg.trim().equals("��")) { a1= msg; if(!msg.trim().equals("��������")) sendmsg(q2,"0"); msgnum = msgcount; poll_status=2; }
    if(msg.trim().equals("��������")) poll_status=0;
   }
   if(poll_status==2 && msgcount >= msgnum+1 && mode.equals("��������")){
    if(msg.length()>=2) if(!msg.trim().equals("�ڷ��") && !msg.trim().toLowerCase().equals("ps") && !msg.trim().equals("���") && !msg.trim().equals("�ζ�") && !msg.trim().equals("��ɾ�") && !msg.trim().equals("���") && !msg.trim().equals("�ð�") && !msg.substring(0,2).equals("����") && !msg.substring(0,2).equals("���")) poll_check(modify); // ���,��ɾ� ���ü��...
    if(msg.length()<2) if(!msg.trim().equals("��")) poll_check(modify);
    if(msg.trim().equals("��������")) poll_status=0;
   }
   if(poll_status==3 && msgcount >= msgnum+1 && mode.equals("��������")){
    if(msg.trim().equals("����") || msg.trim().equals("2")) { modify=true; sendmsg(q1,"0"); msgnum = msgcount; poll_status=1; }
    if(msg.trim().equals("����") || msg.trim().equals("1")) { sendmsg2("         ��������   ���뺸��   ��������http://vam.new21.net/ps/view.php?num=" + poll_num + "         ����������������������������","0"); msgnum=msgcount; }
    if(msg.trim().equals("0")) { mode=""; poll_status=0; sendFile("opening.dat"); }
    if(msg.trim().equals("��ɾ�")) { sendmsg2("�� ����������  ��������������������������������ɾ�ݳ��� ����  ������������������������","0"); msgnum=msgcount; }
    if(msg.trim().equals("��������")) poll_status=0;
   }
  }catch(Exception e){
   System.out.println(e);
  }
 } */

 public int analysis(){
  int r=-4;

  if(msg.trim().equals("��ɾ�") && mode.length()==0) r=-1;

  if(mode.equals("PS")){ 
   if(msg.length()==6 && msg.substring(0,2).equals("/��") && answer.length()==0 && !mq || msg.trim().equals("6") && answer.length()==0 && !mq) r=2113;

   if(msg.trim().equals("6") && answer.length()>0 && provider.equals(said)) r=2116;
   if(msg.trim().equals("6") && answer.length()>0 && !mq && !provider.equals(said)) { sendmsg2("�� ���ڸ� ���ڸ� �Է��ϼ��� >>","000000"); msgcount = msgnum; mq=true; }
   if(msg.length()>=2) if(!msg.trim().equals("�ڷ��") && !msg.trim().equals("��������") && !msg.trim().toLowerCase().equals("ps") && !msg.trim().equals("���") && !msg.trim().equals("�ζ�") && !msg.trim().equals("��ɾ�") && !msg.trim().equals("���") && !msg.trim().equals("�ð�") && !msg.substring(0,2).equals("����") && !msg.substring(0,2).equals("���"))
   if(!msg.equals("/��") && !msg.equals("/�� 0") && !msg.equals("/�� 16") && !msg.equals("/�� 0 16") && !msg.equals("/�� 16 0") && !msg.substring(0,2).equals("/��") && !msg.substring(0,2).equals("/��") && msg.length()!=3)
    if(msg.length()!=3 && answer.length()>0 && mq && msgcount>=msgnum+1) { sendmsg2("�������������������� ��    �� ��������������������    ���ڸ��� ���ڰ� �ƴմϴ�... �ٽ� �Է��ϼ���...������������������������������������������������","0000ff"); msgcount=msgnum; }
   if(msg.length()<2) if(!msg.trim().equals("��")) if(msg.length()!=3 && answer.length()>0 && mq && msgcount>=msgnum+1) { sendmsg2("�������������������� ��    �� ��������������������    ���ڸ��� ���ڰ� �ƴմϴ�... �ٽ� �Է��ϼ���...������������������������������������������������","0000ff"); msgcount=msgnum; }
   if(msg.length()==3 && answer.length()>0 && mq) msg = "/�� " + msg;

   if(msg.length()==6 && msg.substring(0,2).equals("/��") && answer.length()>0){
    if(ps_16==true) msg = msg.toLowerCase();
    if(msg.charAt(4) != answer.charAt(0) && msg.charAt(5) != answer.charAt(1) && msg.charAt(3) != answer.charAt(2)) r=2109;
    if(msg.charAt(5) != answer.charAt(0) && msg.charAt(3) != answer.charAt(1) && msg.charAt(4) != answer.charAt(2)) r=2109;
    if(msg.charAt(3) != answer.charAt(0) && msg.charAt(4) != answer.charAt(1) && msg.charAt(5) != answer.charAt(2)) r=2109;
    if(msg.charAt(4) == answer.charAt(0) || msg.charAt(5) == answer.charAt(0) || msg.charAt(3) == answer.charAt(1) || msg.charAt(5) == answer.charAt(1) || msg.charAt(3) == answer.charAt(2) || msg.charAt(4) == answer.charAt(2)) r=2108;
    if(msg.charAt(3) == answer.charAt(0) || msg.charAt(4) == answer.charAt(1) || msg.charAt(5) == answer.charAt(2)) r=2107;
    if(msg.charAt(4) == answer.charAt(0) && msg.charAt(3) == answer.charAt(1) || msg.charAt(4) == answer.charAt(0) && msg.charAt(5) == answer.charAt(1) || msg.charAt(5) == answer.charAt(0) && msg.charAt(3) == answer.charAt(1)) r=2105;
    if(msg.charAt(4) == answer.charAt(0) && msg.charAt(3) == answer.charAt(2) || msg.charAt(5) == answer.charAt(0) && msg.charAt(4) == answer.charAt(2) || msg.charAt(5) == answer.charAt(1) && msg.charAt(3) == answer.charAt(2)) r=2105;
    if(msg.charAt(5) == answer.charAt(1) && msg.charAt(4) == answer.charAt(2) || msg.charAt(5) == answer.charAt(1) && msg.charAt(3) == answer.charAt(2) || msg.charAt(3) == answer.charAt(1) && msg.charAt(4) == answer.charAt(2)) r=2105;
    if(msg.charAt(5) == answer.charAt(0) && msg.charAt(3) == answer.charAt(2)) r=2105;
    if(msg.charAt(5) == answer.charAt(0) && msg.charAt(4) == answer.charAt(1) || msg.charAt(4) == answer.charAt(0) && msg.charAt(5) == answer.charAt(2)) r=2106;
    if(msg.charAt(3) == answer.charAt(0) && msg.charAt(4) == answer.charAt(2) || msg.charAt(3) == answer.charAt(1) && msg.charAt(5) == answer.charAt(2)) r=2106;
    if(msg.charAt(3) == answer.charAt(0) && msg.charAt(5) == answer.charAt(1) || msg.charAt(4) == answer.charAt(1) && msg.charAt(3) == answer.charAt(2)) r=2106;
    if(msg.charAt(3) == answer.charAt(0) && msg.charAt(5) == answer.charAt(1) && msg.charAt(4) == answer.charAt(2)) r=2104;
    if(msg.charAt(4) == answer.charAt(1) && msg.charAt(5) == answer.charAt(0) && msg.charAt(3) == answer.charAt(2)) r=2104;
    if(msg.charAt(5) == answer.charAt(2) && msg.charAt(4) == answer.charAt(0) && msg.charAt(3) == answer.charAt(1)) r=2104;
    if(msg.charAt(3) == answer.charAt(0) && msg.charAt(4) == answer.charAt(1) || msg.charAt(4) == answer.charAt(1) && msg.charAt(5) == answer.charAt(2) || msg.charAt(5) == answer.charAt(2) && msg.charAt(3) == answer.charAt(0)) r=2103;
    if(msg.charAt(4) == answer.charAt(0) && msg.charAt(5) == answer.charAt(1) && msg.charAt(3) == answer.charAt(2)) r=2102;
    if(msg.charAt(5) == answer.charAt(0) && msg.charAt(3) == answer.charAt(1) && msg.charAt(4) == answer.charAt(2)) r=2102;
    if(msg.charAt(3) == answer.charAt(0) && msg.charAt(4) == answer.charAt(1) && msg.charAt(5) == answer.charAt(2)) { r=2101; mq=false; }
    if(msg.charAt(3) == msg.charAt(4) || msg.charAt(4) == msg.charAt(5) || msg.charAt(3) == msg.charAt(5))  r=2111;
    
    if(ps_16==false) for(int i=3;i<6;i++) if(msg.charAt(i)<'0' || msg.charAt(i)>'9')  r=2114;
    if(ps_16==true)  for(int i=3;i<6;i++) if(msg.charAt(i)<'0' || msg.charAt(i)>'9' && msg.charAt(i)<'A' || msg.charAt(i)>'F' && msg.charAt(i)<'a' || msg.charAt(i)>'f') { r=2115; mq=true; }
    if(ps_zero==false) for(int i=3;i<6;i++) if(msg.charAt(i)=='0') r=2112;

    if(provider.equals(said)) r=2116;
   }

   if(msg.length()==2 && msg.equals("/��") && answer.length()>0 || msg.trim().equals("1") && msgcount >= msgnum+1 && answer.length()>0) r=2121;
   if(msg.length()==2 && msg.equals("/��") && answer.length()==0 || msg.trim().equals("1") && msgcount >= msgnum+1 && answer.length()==0 && !mq) r=2131;
   if(msg.length()==4 && msg.equals("/�� 0") && answer.length()>0 || msg.trim().equals("2") && answer.length()>0 && !mq) r=2121;
   if(msg.length()==4 && msg.equals("/�� 0") && answer.length()==0 || msg.trim().equals("2") && answer.length()==0 && !mq) r=2132;
   if(msg.length()==5 && msg.equals("/�� 16") && answer.length()>0 || msg.trim().equals("3") && answer.length()>0 && !mq) r=2121;
   if(msg.length()==5 && msg.equals("/�� 16") && answer.length()==0 || msg.trim().equals("3") && answer.length()==0 && !mq) r=2133;
   if(msg.length()==7 && msg.equals("/�� 0 16") && answer.length()>0 || msg.trim().equals("4") && answer.length()>0 && !mq) r=2121;
   if(msg.length()==7 && msg.equals("/�� 0 16") && answer.length()==0 || msg.trim().equals("4") && answer.length()==0 && !mq) r=2134;
   if(msg.length()==7 && msg.equals("/�� 16 0") && answer.length()>0 || msg.trim().equals("4") && answer.length()>0 && !mq) r=2121;
   if(msg.length()==7 && msg.equals("/�� 16 0") && answer.length()==0 || msg.trim().equals("4") && answer.length()==0 && !mq) r=2134;

   if(msg.trim().equals("5") && answer.length()==0 && !mq) { sendmsg2("�� ���ڸ� ���ڸ� �Է��ϼ��� >>","000000"); msgcount = msgnum; mq=true; }
   if(msg.length()>=2) if(!msg.trim().equals("�ڷ��") && !msg.trim().equals("��������") && !msg.trim().toLowerCase().equals("ps") && !msg.trim().equals("���") && !msg.trim().equals("�ζ�") && !msg.trim().equals("��ɾ�") && !msg.trim().equals("���") && !msg.trim().equals("�ð�") && !msg.substring(0,2).equals("����") && !msg.substring(0,2).equals("���"))
   if(!msg.equals("/��") && !msg.equals("/�� 0") && !msg.equals("/�� 16") && !msg.equals("/�� 0 16") && !msg.equals("/�� 16 0") && !msg.substring(0,2).equals("/��") && msg.length()!=3)
    if(msg.length()!=3 && answer.length()==0 && mq && msgcount>=msgnum+1) { sendmsg2("�������������������� ��    �� ��������������������    ���ڸ��� ���ڰ� �ƴմϴ�... �ٽ� �Է��ϼ���...������������������������������������������������","0000ff");}
   if(msg.length()<2) if(!msg.trim().equals("��")) if(msg.length()!=3 && answer.length()==0 && mq && msgcount>=msgnum+1) { sendmsg2("�������������������� ��    �� ��������������������    ���ڸ��� ���ڰ� �ƴմϴ�... �ٽ� �Է��ϼ���...������������������������������������������������","0000ff");}
   if(msg.length()==3 && answer.length()==0 && mq) msg = "/�� " + msg;
   
   if(msg.length()==6 && msg.substring(0,2).equals("/��") && answer.length()>0 || msg.trim().equals("5") && answer.length()>0 && !mq) r=2121;
   if(msg.length()==6 && msg.substring(0,2).equals("/��") && answer.length()==0){
    msg = msg.toLowerCase();
    for(int i=3;i<6;i++) if(msg.charAt(i)>'0' && msg.charAt(i)<='9') { r=2135; ps_zero=false; ps_16=false; }
    for(int i=3;i<6;i++) if(r==2135) if(msg.charAt(i)=='0') { r=2136; ps_zero=true; ps_16=false; }
    for(int i=3;i<6;i++) if(msg.charAt(i)>='a' && msg.charAt(i)<='f') { r=2137; ps_zero=false; ps_16=true; }
    for(int i=3;i<6;i++) if(r==2137) if(msg.charAt(i)=='0') { r=2138; ps_zero=true; ps_16=true; }
    for(int i=3;i<6;i++) if(msg.charAt(i)<'0' || msg.charAt(i)>'9' && msg.charAt(i)<'a' || msg.charAt(i)>'f') r=2115;
    if(msg.charAt(3) == msg.charAt(4) || msg.charAt(4) == msg.charAt(5) || msg.charAt(3) == msg.charAt(5)) r=2111;
    if(users>2) r=2122;
    if(r>=2135 && r<=2138) mq=false;
   }
   if(msg.equals("/����") || msg.trim().equals("7") && !mq){
    if(answer.length()>0){
     if(ps_zero==false && ps_16==false && provider.equals("Computer")) r=2141;
     if(ps_zero==true && ps_16==false && provider.equals("Computer")) r=2142;
     if(ps_zero==false && ps_16==true && provider.equals("Computer")) r=2143;
     if(ps_zero==true && ps_16==true && provider.equals("Computer")) r=2144;
     if(ps_zero==false && ps_16==false && !provider.equals("Computer")) r=2145;
     if(ps_zero==true && ps_16==false && !provider.equals("Computer")) r=2146;
     if(ps_zero==false && ps_16==true && !provider.equals("Computer")) r=2147;
     if(ps_zero==true && ps_16==true && !provider.equals("Computer")) r=2148;
    }else r=2149;
   }
   if(msg.equals("��ɾ�")) r=2151;
   if(msg.trim().equals("0") && !mq) r=-5;
  }

  if(mode.equals("�ζ�")){ 
   if(msg.trim().equals("1")) r=2301;
   if(msg.trim().equals("6") || msg.trim().equals("2")) r=2302;
   if(msg.trim().equals("��ɾ�")) r=2303;
   if(msg.trim().equals("0")) r=-5;
  }

  if(mode.equals("���")){ 
   boolean alpha = true;
   if(msg.length()>=1) for(int i=0;i<msg.length();i++) if(msg.toLowerCase().charAt(i)<'a' || msg.toLowerCase().charAt(i)>'z') alpha=false;
   if(alpha && hang_turn>0 && hang_turn<=5) r=2418;
   if(hang_turn==-1) r=2411;
   if(msg.trim().equals("��ɾ�")) r=2421;
   if(msg.trim().equals("0")) r=-5;
   if(msg.trim().equals("����") || msg.trim().equals("1")) r=2422;
   if(hang_turn==5 && msg.length()==1 && msg.toLowerCase().charAt(0)>='a' && msg.toLowerCase().charAt(0)<='z') r=2412;
   if(hang_turn==4 && msg.length()==1 && msg.toLowerCase().charAt(0)>='a' && msg.toLowerCase().charAt(0)<='z') r=2413;
   if(hang_turn==3 && msg.length()==1 && msg.toLowerCase().charAt(0)>='a' && msg.toLowerCase().charAt(0)<='z') r=2414;
   if(hang_turn==2 && msg.length()==1 && msg.toLowerCase().charAt(0)>='a' && msg.toLowerCase().charAt(0)<='z') r=2415;
   if(hang_turn==1 && msg.length()==1 && msg.toLowerCase().charAt(0)>='a' && msg.toLowerCase().charAt(0)<='z') r=2416;
   if(hang_turn==0) r=2418;   

   if(msg.toLowerCase().equals(quest) && hang_turn>=0) r=2420;
  }

  if(mode.equals("��Ŀ")){ 
   int a=1,b=1,c=1,d=1,e=1;
   while(a==b || a==c || a==d || a==e || b==c || b==d || b==e || c==d || c==e || d==e){
    a = (int)(Math.random()*1000%13)+1;
    b = (int)(Math.random()*1000%13)+1;
    c = (int)(Math.random()*1000%13)+1;
    d = (int)(Math.random()*1000%13)+1;
    e = (int)(Math.random()*1000%13)+1;
   }
   cards[0][0] = a; cards[1][0] = b; cards[2][0] = c; cards[3][0] = d; cards[4][0] = e;   
  }

  if(mode.equals("�ڷ��")){
   if(msg.trim().equals("2")) r=42;
   if(msg.trim().equals("1")) r=41;
   if(msg.trim().equals("0")) r=-5;
  }
  
  if(mode.equals("�ڷ�Ǵٿ�")){
   if(pds_status==1 && msgcount>=msgnum+1){
    try{ pds_num = Byte.parseByte(msg); pds_status=2;} 
    catch(Exception e){ 
     System.out.println(e);
     msgcount=msgnum;
     if(msg.length()>=2) if(!msg.trim().equals("�ڷ��") && !msg.trim().equals("��������") && !msg.trim().toLowerCase().equals("ps") && !msg.trim().equals("���") && !msg.trim().equals("�ζ�") && !msg.trim().equals("��ɾ�") && !msg.trim().equals("���") && !msg.trim().equals("�ð�") && !msg.substring(0,2).equals("����") && !msg.substring(0,2).equals("���")) sendmsg2("������������������  ��  ��  ������������������         ��ȣ�� ���� �Է� �����մϴ�...  '���ε�'�޴��� �˸´� ��ȣ�� �Է��� �ּ���...��������������������������������������������","0000FF");
     if(msg.length()<2) if(!msg.trim().equals("��")) sendmsg2("������������������  ��  ��  ������������������         ��ȣ�� ���� �Է� �����մϴ�...  '���ε�'�޴��� �˸´� ��ȣ�� �Է��� �ּ���...��������������������������������������������","0000FF"); 
    }
   }
   if(pds_status==2 && msgcount>=msgnum+1){
    
   }
   if(msg.trim().equals("0")) { r=4; pds_status=0; }
  }

  if(mode.equals("�ڷ�Ǿ�")){
   if(pds_status==1 && msgcount>=msgnum+1){ 
    try{ pds_num = Byte.parseByte(msg); pds_status=2;} 
    catch(Exception e){ 
     System.out.println(e);
     msgcount=msgnum;
     if(msg.length()>=2) if(!msg.trim().equals("�ڷ��") && !msg.trim().equals("��������") && !msg.trim().toLowerCase().equals("ps") && !msg.trim().equals("���") && !msg.trim().equals("�ζ�") && !msg.trim().equals("��ɾ�") && !msg.trim().equals("���") && !msg.trim().equals("�ð�") && !msg.substring(0,2).equals("����") && !msg.substring(0,2).equals("���")) sendmsg2("������������������  ��  ��  ������������������         ��ȣ�� ���� �Է� �����մϴ�...  '���ε�'�޴��� �˸´� ��ȣ�� �Է��� �ּ���...��������������������������������������������","0000FF");
     if(msg.length()<2) if(!msg.trim().equals("��")) sendmsg2("������������������  ��  ��  ������������������         ��ȣ�� ���� �Է� �����մϴ�...  '���ε�'�޴��� �˸´� ��ȣ�� �Է��� �ּ���...��������������������������������������������","0000FF"); 
    }
   }
   if(pds_status==2 && msgcount>=msgnum+1){
    sendmsg2("�� ���ε��� ������ ������ �����ּ��� >>","0");
    if(msg.length()>=2) if(!msg.trim().equals("�ڷ��") && !msg.trim().equals("��������") && !msg.trim().toLowerCase().equals("ps") && !msg.trim().equals("���") && !msg.trim().equals("�ζ�") && !msg.trim().equals("��ɾ�") && !msg.trim().equals("���") && !msg.trim().equals("�ð�") && !msg.substring(0,2).equals("����") && !msg.substring(0,2).equals("���")) { msgcount = msgnum; pds_status=3; }
    if(msg.length()<2) if(!msg.trim().equals("��")) { msgcount = msgnum; pds_status=3; }
   }
   if(pds_status==3 && msgcount>=msgnum+1){
    pds_title = msg;
    sendmsg2("�� ���ε��� ������ ������ �����ּ��� >>��, �� ���������� ���� ���� /���̶� ���ڸ� �־��ּ���...","0");
    msgcount=msgnum; pds_status=4;
   }
   if(pds_status==4 && msgcount>=msgnum+1){
    if(msg.length()>=2) if(!msg.trim().equals("�ڷ��") && !msg.trim().equals("��������") && !msg.trim().toLowerCase().equals("ps") && !msg.trim().equals("���") && !msg.trim().equals("�ζ�") && !msg.trim().equals("��ɾ�") && !msg.trim().equals("���") && !msg.trim().equals("�ð�") && !msg.substring(0,2).equals("����") && !msg.substring(0,2).equals("���")){
     if(!msg.substring(msg.length()-2,msg.length()).equals("/��")) { msgcount = msgnum; pds_con = pds_con + msg; sendmsg2("�� ���ε��� ������ ������ �����ּ��� >>��, �� ���������� ���� ���� /���̶� ���ڸ� �־��ּ���...","0");}
     if(msg.substring(msg.length()-2,msg.length()).equals("/��")) { 
      msgcount = msgnum;
      pds_con = pds_con + msg.substring(0,msg.length()-2);
      System.out.println(pds_con);
      sendmsg2("�� ���� ������ ���ε� ���ּ���...(����->����/���� ������)","0"); 
      upload=true; 
      pds_status=0;
     }
    }
    if(msg.length()<2) if(!msg.trim().equals("��")) { pds_con = pds_con + msg; msgcount = msgnum; }
   }
   if(msg.trim().equals("0")) { r=4; pds_status=0; pds_con = ""; pds_title = "";}
  }

  if(mode.equals("")){   
   if(msg.trim().equals("1") && !ma) { msgcount = msgnum; r=21; }
   if(msg.trim().equals("2") && !ma) r=23;
   if(msg.trim().equals("3") && !ma) r=24;
   // if(msg.trim().equals("4") && !ma) mode="��������";
   if(msg.trim().equals("5") && !ma) r=3;
   if(msg.trim().equals("8") && !ma) r=4;
  }
  
  if(msg.trim().equals("�ȳ�") && users>2) r=0;
  // if(msg.trim().equals("��������") && users==2) mode="��������"; 
  if(msg.trim().toLowerCase().equals("ps")) r=21;
  if(msg.trim().equals("���")) r=24;
  if(msg.trim().equals("�ζ�")) r=23;
  if(msg.trim().equals("���") && mode.length()>0)  r=-3; 
  if(msg.trim().equals("���") && mode.length()==0)  r=-2;
  if(msg.trim().equals("�ð�")) r=3;
  if(msg.trim().equals("�ڷ��")) r=4;
  if(msg.trim().equals("��")) { r=41; pds_status=0; pds_con = ""; pds_title = ""; }
  
  if(msg.trim().equals("6") && msgcount>=msgnum+1 && !ma) if(!mode.equals("PS") && !mode.equals("�ζ�") && !mode.equals("���") && !mode.equals("��������")){ sendmsg2("ex) 20030101�� ���������� �Է��ϼ��� >>","000000"); msgcount = msgnum; ma=true; yang=true; }
  if(msg.trim().equals("7") && msgcount>=msgnum+1 && !ma) if(!mode.equals("PS") && !mode.equals("�ζ�") && !mode.equals("���") && !mode.equals("��������")){ sendmsg2("ex) 20030101�� ���������� �Է��ϼ��� >>","000000"); msgcount = msgnum; ma=true; yang=false; }
  if(msg.length()>=2) if(!msg.trim().equals("�ڷ��") && !msg.trim().equals("��������") && !msg.trim().toLowerCase().equals("ps") && !msg.trim().equals("���") && !msg.trim().equals("�ζ�") && !msg.trim().equals("��ɾ�") && !msg.trim().equals("���") && !msg.trim().equals("�ð�") && !msg.substring(0,2).equals("����") && !msg.substring(0,2).equals("���"))
   if(msg.length()!=8 && ma && msgcount>=msgnum+1) r=316;
  if(msg.length()<2) if(!msg.trim().equals("��")) if(ma && msgcount>=msgnum+1) r=316;
  if(msg.length()==8 && ma && msgcount>=msgnum+1 && yang) { ma = false; msg = "��� " + msg; }
  if(msg.length()==8 && ma && msgcount>=msgnum+1 && !yang) { ma = false; msg = "���� " + msg; }

  if(msg.length()==11) if(msg.substring(0,2).equals("����")){
   r=311;
   for(int i=4;i<msg.length();i++) if(msg.charAt(i)<'0' || msg.charAt(i)>'9') r=315;
  }
  if(msg.length()==11) if(msg.substring(0,2).equals("���")){
   r=312;
   for(int i=4;i<msg.length();i++) if(msg.charAt(i)<'0' || msg.charAt(i)>'9') r=315;
  }
  if(msg.length()>1 && msg.length()!=11) if(msg.substring(0,2).equals("����")) r=313;
  if(msg.length()>1 && msg.length()!=11) if(msg.substring(0,2).equals("���")) r=314;

//  if(mode.equals("��������")) poll();

  return r;
 }

 public void logic(){
  try{
   RandomAccessFile checking = new RandomAccessFile("analysis.dat","r");
   long length = checking.length();
   long pointer = 0;
   checking.seek(0);
   while(pointer<length){
    if(Integer.parseInt(checking.readLine().trim()) == analysis())
     sendmsg(checking.readLine(),checking.readLine());
    else{
     checking.readLine();
     checking.readLine();
    }
    pointer = checking.getFilePointer();
   }
   checking.close();
   if(analysis()==-5){ mode=""; sendFile("opening.dat"); }
   if(analysis()==-3) sendmsg2("���� ���� " + mode + "����Դϴ�...","ff00ff");
   if(analysis()==-2) sendmsg2("����� �ƹ��� ��忡 ����Ǿ� ���� �ʽ��ϴ�...","ff00ff");
   if(analysis()==3){
     Date d = new Date(); 
     String week = null;
     int year = d.getYear() + 1900;
     int month = d.getMonth() + 1;
     switch(d.getDay()){
      case 0: week = "�Ͽ���(��)"; break;
      case 1: week = "������(��)"; break;
      case 2: week = "ȭ����(��)"; break;
      case 3: week = "������(�)"; break;
      case 4: week = "�����(��)"; break;
      case 5: week = "�ݿ���(��)"; break;
      case 6: week = "�����(��)"; break;
     }
     sendmsg2("������ " + year + "�� " + month + "�� " + d.getDate() + "�� " + week + "�̰�,���� �ð��� " + d.getHours() + "�� " + d.getMinutes() + "�� " + d.getSeconds() + "�� �Դϴ�...","000000");
   }
   if(analysis()==4){ mode="�ڷ��"; sendFile("pds.dat"); }
   if(analysis()==41){ mode="�ڷ�Ǿ�";
    u = new URL("http://vam.new21.net/ps/auth.php?email="+user_id).openConnection();
    udis = new BufferedReader(new InputStreamReader(u.getInputStream()));
    if(Integer.parseInt(udis.readLine())>0) { sendFile("pds_list.dat"); if(!upload){msgnum = msgcount; pds_status=1;} else sendmsg2("�� ���� ������ ���ε� ���ּ���...(����->����/���� ������)","0");}
    else{
     mode="�ڷ��"; 
     sendmsg2("����������������������  ��  ��  ����������������������                      ������ ���� �������Ƿ�...           '���ε�'����� ����Ͻ� �� �����ϴ�...   ps-gem@hanmail.net�� ������ ���� �������ּ���...����������������������������������������������������","0000FF");
     sendFile("pds.dat");
    }
   }
   if(analysis()==42) { mode="�ڷ�Ǵٿ�"; sendFile("pds_list2.dat"); pds_status=1; }
   if(analysis()==311) sendmsg2(msg.substring(3,7) + "�� ��� " + Integer.parseInt(msg.substring(7,9)) +"�� " + Integer.parseInt(msg.substring(9,11)) + "���� ���� " + calendar(false,msg.substring(3,11)) + "�Դϴ�...","000000");
   if(analysis()==312) sendmsg2(msg.substring(3,7) + "�� ���� " + Integer.parseInt(msg.substring(7,9)) +"�� " + Integer.parseInt(msg.substring(9,11)) + "���� ��� " + calendar(true,msg.substring(3,11)) + "�Դϴ�...","000000");
   if(analysis()==21) { mode = "PS"; sendFile("ps.dat"); mq=false;}
   if(analysis()==24) { mode = "���"; sendFile("hangman.dat"); hang_turn = -1; make_hang(); see_hang(); sendmsg2("��������   ����������  ��  ��  ����������               " + quest_see + " ( " + quest_see.length()/2 + " ���� )                  ��������  ��  Ʈ  ��������               " + hint +"��������   ����������������������������","000000");}
   if(analysis()==23) { mode = "�ζ�"; sendFile("rotto.dat"); }
   if(analysis()==2101) { answer = ""; provider = "Computer"; sendFile("ps2.dat");}
   if(analysis()==2131) { mq = false; ps_zero=false; ps_16=false; make(ps_zero,ps_16); }
   if(analysis()==2132) { mq = false; ps_zero=true; ps_16=false; make(ps_zero,ps_16); }
   if(analysis()==2133) { mq = false; ps_zero=false; ps_16=true; make(ps_zero,ps_16); }
   if(analysis()==2134) { mq = false; ps_zero=true; ps_16=true; make(ps_zero,ps_16); }
   if(analysis()==0) send("OUT");
   if(analysis()==2135) { mq = false; provider = said; answer = msg.substring(3,6); sendmsg2("������������������ ���� �˸� ������������������    1~9 ������ �� �ڸ� ���ڰ� ����������ϴ�...          ���������� ���� ������ ������������                 " + provider + "����������������������������������������������","ff0000"); }
   if(analysis()==2136) { mq = false; provider = said; answer = msg.substring(3,6); sendmsg2("������������������ ���� �˸� ������������������    0~9 ������ �� �ڸ� ���ڰ� ����������ϴ�...          ���������� ���� ������ ������������                 " + provider + "����������������������������������������������","ff0000"); } 
   if(analysis()==2137) { mq = false; provider = said; answer = msg.substring(3,6); sendmsg2("���������������������� ���� �˸� ����������������������    1~F(16����) ������ �� �ڸ� ���ڰ� ����������ϴ�...          ��������������  ���� ������  ��������������                         " + provider + "������������������������������������������������������","ff0000"); } 
   if(analysis()==2138) { mq = false; provider = said; answer = msg.substring(3,6); sendmsg2("���������������������� ���� �˸� ����������������������    0~F(16����) ������ �� �ڸ� ���ڰ� ����������ϴ�...          ��������������  ���� ������  ��������������                         " + provider + "������������������������������������������������������","ff0000"); }
   if(analysis()==2145) sendmsg2("����������������������  ��  ��  ����������������������   1~9 ������ �� �ڸ� ���ڰ� ������� �����Դϴ�...    �ٸ� ����� �ʴ��Ͽ� ���ڸ� ���߰� �Ͻʽÿ�...         �������������� ���� ������ ��������������                     " + provider + "����������������������������������������������������","888888");
   if(analysis()==2146) sendmsg2("����������������������  ��  ��  ����������������������   0~9 ������ �� �ڸ� ���ڰ� ������� �����Դϴ�...    �ٸ� ����� �ʴ��Ͽ� ���ڸ� ���߰� �Ͻʽÿ�...         �������������� ���� ������ ��������������                     " + provider + "����������������������������������������������������","888888");
   if(analysis()==2147) sendmsg2("��������������������������  ��  ��  ��������������������������     1~F(16����) ������ �� �ڸ� ���ڰ� ������� �����Դϴ�...            �ٸ� ����� �ʴ��Ͽ� ���ڸ� ���߰� �Ͻʽÿ�...         ������������������ ���� ������ ������������������                           " + provider + "������������������������������������������������������������","888888");
   if(analysis()==2148) sendmsg2("��������������������������  ��  ��  ��������������������������     0~F(16����) ������ �� �ڸ� ���ڰ� ������� �����Դϴ�...            �ٸ� ����� �ʴ��Ͽ� ���ڸ� ���߰� �Ͻʽÿ�...         ������������������ ���� ������ ������������������                           " + provider + "������������������������������������������������������������","888888");
   if(analysis()==2301) { rotto(1); sendmsg2("���� ���� ���� ����            " + rotto_ans + "������������������","0"); }
   if(analysis()==2302) { rotto(6); sendmsg2("������������ ���� ���� ������������        " + rotto_ans +"����������������������������������","0"); }
   if(analysis()==2411) { make_hang(); see_hang(); sendmsg2("��������   ����������  ��  ��  ����������               " + quest_see + " ( " + quest_see.length()/2 + " ���� )                  ��������  ��  Ʈ  ��������               " + hint +"��������   ����������������������������","000000"); hang_turn=5;}
   if(analysis()==2418) { see_hang(); sendmsg2("��������   ����������  ��  ��  ����������    :'(                      " + quest + "   /����        ������������������������   /   ��     ������ ������ ���ϼ̽��ϴ�...��������   ����������������������������","000000"); hang_turn=-1; hang_score = hang_score + (quest.length()*hang_turn);}
   if(analysis()==2416) { see_hang(); sendmsg2("��������   ����������  ��  ��  ����������    :)        " + quest_see + " ( " + quest_see.length()/2 + " ���� )   /����        ��������  ��  Ʈ  ��������   /           " + hint + "��������   ����������������������������","000000"); hang_turn--;}
   if(analysis()==2415) { see_hang(); sendmsg2("��������   ����������  ��  ��  ����������    :)        " + quest_see + " ( " + quest_see.length()/2 + " ���� )   /����        ��������  ��  Ʈ  ��������               " + hint + "��������   ����������������������������","000000"); hang_turn--;}
   if(analysis()==2414) { see_hang(); sendmsg2("��������   ����������  ��  ��  ����������    :)        " + quest_see + " ( " + quest_see.length()/2 + " ���� )   /��           ��������  ��  Ʈ  ��������               " + hint + "��������   ����������������������������","000000"); hang_turn--;}
   if(analysis()==2413) { see_hang(); sendmsg2("��������   ����������  ��  ��  ����������    :)        " + quest_see + " ( " + quest_see.length()/2 + " ���� )    ��           ��������  ��  Ʈ  ��������               " + hint + "��������   ����������������������������","000000"); hang_turn--;}
   if(analysis()==2412) { see_hang(); sendmsg2("��������   ����������  ��  ��  ����������    :)        " + quest_see + " ( " + quest_see.length()/2 + " ���� )                  ��������  ��  Ʈ  ��������               " + hint + "��������   ����������������������������","000000"); hang_turn--;}
   if(analysis()==2420) { sendmsg2("��������   ����������  ��  ��  ����������                           " + quest + "   (y)            ������������������������               ����� ������ ���߼̽��ϴ�^^;��������   ����������������������������","ff0000"); hang_score = hang_score + (quest.length()*(hang_turn+1)); hang_turn=-1; }
   if(analysis()==2422) sendmsg2("����  ��  ��  ����        " + hang_score + " ������������������","888888");
  }catch(Exception e){
   System.out.println(e);
  }
 }

/* ����Ʈ����ũ ��. */

 public void sendFile(String file){
  String send="";
  String rl;
  try{
   RandomAccessFile starting = new RandomAccessFile(file,"r");
   long length = starting.length();
   long pointer = 0;
   while(pointer<length){
    rl = starting.readLine();
    pointer = starting.getFilePointer();
    send = send + rl + "\r\n";
   }
   sendmsg(send,"0");
   starting.close();
  }catch(Exception e){
   System.out.println(e);
  }
 }

 public void needed(String receive){
  int pointer,msglength,sp1,sp2;
  String imsi;
  byte[] msgss=null;
  if(receive.length()>=3 && !msgcheck){ 
   if(receive.substring(0,3).equals("IRO")) users++;  
   if(receive.substring(0,3).equals("BYE")) users--;
   if(receive.substring(0,3).equals("JOI")) { users++; sendmsg("Hello :D","0"); }
   if(receive.substring(0,3).equals("ANS")) { sendFile("opening.dat"); /* mode="��������";  poll(); */}
   if(receive.substring(0,3).equals("MSG")) { 
    Readline = false;
    pointer = location(receive,3);
    sp1 = location(receive,1);
    sp2 = location(receive,2);
    said = receive.substring(sp1,sp2-1);
    msglength = Integer.parseInt(receive.substring(pointer,receive.length()));
    try{
     imsi = dis.readLine();
     msglength = msglength - imsi.length() - 2;
     // System.out.println(imsi + " " + imsi.length());

     imsi = dis.readLine();
     msglength = msglength - imsi.length() - 2;
     // System.out.println(imsi + " " + imsi.length());

     if(imsi.substring(0,33).equals("Content-Type: text/x-msmsgsinvite")){
      for(int i=0;msglength>0;i++){
       imsi = dis.readLine();
       msglength = msglength - imsi.length() - 2;
       // System.out.println(imsi + " " + imsi.length());
       if(imsi.length()>=18) if(imsi.substring(0,18).equals("Invitation-Cookie:")) inv_ck = Integer.parseInt(imsi.substring(19,imsi.length()));
       if(imsi.length()>=19) if(imsi.substring(0,19).equals("Invitation-Command:")) pds_command = imsi.substring(20,imsi.length());
       if(imsi.length()>=17) if(imsi.substring(0,17).equals("Application-File:")) filename = imsi.substring(18,imsi.length());
       if(imsi.length()>=21) if(imsi.substring(0,21).equals("Application-FileSize:")) filesize = Integer.parseInt(imsi.substring(22,imsi.length()));
       if(imsi.length()>=11) if(imsi.substring(0,11).equals("IP-Address:")) your_ip = imsi.substring(12,imsi.length());
       if(imsi.length()>=11) if(imsi.substring(0,11).equals("AuthCookie:")) auth_ck = Integer.parseInt(imsi.substring(12,imsi.length()));
       if(imsi.length()>=5) if(imsi.substring(0,5).equals("Port:")) your_port = Integer.parseInt(imsi.substring(6,imsi.length()));
      }
      // System.out.println(filename + ":" + filesize + " - " + inv_ck);
      if(!upload){
       send("cancel");
       sendmsg2("����������������������  ��  ��  ����������������������        ���� ���� ���ε尡 ������ ���°� �ƴմϴ�...   �ڷ�ǿ� '���ε�'����� �̿��� ������ �÷��ּ���...����������������������������������������������������","0000FF");
      }else{ 
       if(pds_command.equals("INVITE")) send("accept");
       if(pds_command.equals("CANCEL")){
        upload = false;
        sendmsg2("����������������  ��  ��  ����������������     ����ڿ� ���� ���ε尡 ��ҵ˴ϴ�...����������������������������������������","0000FF");
        mode="�ڷ��";
        sendFile("pds.dat");
       }
       if(pds_command.equals("ACCEPT")){
        msnftpup = new MSNFTPup(new Socket(your_ip,your_port),this,id);
        msnftpup.start();
        upload = false;
       }
      }
     }
     else if(imsi.substring(0,33).equals("Content-Type: text/x-mms-emoticon"))
      for(int i=0;i<msglength;i++) dis.readByte();
     else if(imsi.substring(0,24).equals("Content-Type: text/plain")){
      imsi = dis.readLine();
      msglength = msglength - imsi.length() - 2;
      // System.out.println(imsi);
      dis.readLine();
      msglength = msglength - 2;
      if(imsi.length()>=15) if(imsi.substring(0,15).equals("X-MMS-IM-Format")){
       msgss = new byte[msglength];
       for(int j=0;j<msglength;j++)  msgss[j] = dis.readByte(); 
       msg = new String(msgss,"UTF8");
       // System.out.println(msg + " "  + msg.length());
       msgcount++;
       // System.out.println(msgcount);
       logic();
      }
     }

    }catch(Exception e){
     System.out.println(e);
    }
    Readline = true;
   }
  }
 }

 public void run(){
  users++;
  send("ANS");
  while(status){
   if(Readline) needed(receive());
   if(users<=1) break;
  }
   close();
   System.out.println("Switch Board Server�� �����ϴ�...( " + ip +" )");
 }
}                                                           
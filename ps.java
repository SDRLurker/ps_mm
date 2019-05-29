import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class ps extends Frame implements ActionListener,ItemListener{
 TextField nickname,id,pass;
 Button change,connection,dis;
 Choice status;
 Checkbox detect;
 TextArea msg;
 login connect;
 boolean connections = false;
 public static ServerSocket s;
 
 public ps(){
  setTitle("PS Server for MSN Messenger");

  Label ids = new Label("���̵�");
  id = new TextField(20);
  id.setText("");
  status = new Choice();
  status.add("�¶���");
  status.add("�ٸ��빫��");
  status.add("�𵹾ƿ�����");
  status.add("�ڸ����");
  status.add("��ȭ��");
  status.add("�Ļ���");
  status.add("��������ǥ��");
  status.addItemListener(this);
  status.setEnabled(false);
  Label passs = new Label("��й�ȣ");
  pass = new TextField(6);
  pass.setText("");
  pass.setEchoChar('*');
  pass.addActionListener(this);

  Label nicknames = new Label("��ȭ��");
  nickname = new TextField(47);
  nickname.setEnabled(false);
  nickname.addActionListener(this);
  status.setSize(1,1);
  change = new Button("�ٲٱ�");
  change.setEnabled(false);

  Panel user = new Panel();
  Panel name = new Panel();
  user.add(ids); user.add(id);  user.add(passs); user.add(pass); user.add(status);
  name.add(nicknames);name.add(nickname); name.add(change);

  connection = new Button("����");
  dis = new Button("����");
  dis.setEnabled(false);
  
  Panel cn = new Panel();
  cn.setLayout(new BorderLayout());
  cn.add("West",connection); cn.add("East",dis);

  msg = new TextArea(5,60);
  msg.setEditable(false);
  msg.setBackground(new Color(255,255,255));
  msg.setText("");
  
  Panel up = new Panel();
  up.setLayout(new GridLayout(2,1));
  up.add(user);
  up.add(name);
  Panel middle = new Panel();
  middle.add(msg);
  Panel down = new Panel();
  down.add(cn);  

  add("North",up);
  add("Center",middle);
  add("South",down);

  pack();
  setVisible(true);
  setResizable(false);

  addWindowListener(
   new WindowAdapter()	
    { 
     public void windowClosing(WindowEvent e) 
      { 
       System.exit(0); 
      }
    }
  );

 }

 private void printmsg(String msgs){
  msg.append(msgs);
  msg.setSelectionStart(msg.getText().length());
  msg.append("\n");
 }


 class runserver extends Thread{
  int user=0;
  boolean usercheck=false;
  boolean check_list=false;
  public void run(){
   boolean rl[]=null;
   String rls[]=null;
   String als[]=null;
   while(!connect.error){
    String[] need = connect.needed(connect.receive());
    String parame = connect.param;
    if(need[0].equals("RL") && connect.rmnemonic.equals("ADD")) connect.send("ADD","AL");
    if(need[0].equals("RL") && connect.rmnemonic.equals("REM")) connect.send("REM","AL");
    if(connect.rmnemonic.equals("RNG")) new chat(need[1],connect.id,need[2],need[0],parame).start();
    if(connect.rmnemonic.equals("REA")) printmsg("��ȭ���� ���������� �ٲ�����ϴ�...");
    if(connect.rmnemonic.equals("209")) printmsg("��ȭ���� �ٲٴµ� �����Ͽ����ϴ�...");
    // if(connect.rmnemonic.equals("215")) check_list = false;
   
    if(connect.rmnemonic.equals("LST") && need[2].equals("8")) connect.send("ADD","AL");
    if(connect.rmnemonic.equals("LST") && need[2].equals("2")) connect.send("REM","AL");
    if(connect.rmnemonic.equals("LST") && need[2].equals("10")) user++;
    if(connect.rmnemonic.equals("QRY") && !usercheck){ System.out.println("����� ����� �� : " + user); usercheck=true; }
   }
  }
 }

 class lg extends Thread{
  public void run(){
    if(id.getText().length()>0 && pass.getText().length()>0){ //���̵�� �н����尡 ����� �Էµ� ���
     String[] catched;
     printmsg("�α����� �õ��մϴ�...");
     connect = new login(id.getText(),pass.getText());
     connections = true;
     id.setEditable(false);
     id.setBackground(new Color(255,255,255));
     pass.setEditable(false);
     pass.setBackground(new Color(255,255,255));

     dis.setEnabled(false);
     connection.setEnabled(false);
  
     while(!connect.error){
      connect.send("VER");
      connect.receive();
      connect.send("CVR");
      connect.receive();
      if(connect.error){
       printmsg("�α��ο� �����Ͽ����ϴ�...");
       nickname.setEnabled(false); 
       connection.setEnabled(true); 
       id.setEditable(true); 
       pass.setEditable(true);
       change.setEnabled(false);
       status.setEnabled(false);
       connections = false;
      }
      connect.send("USR");
      catched = new String[1];
      catched = connect.needed(connect.receive());
      if(connect.rmnemonic.equals("XFR")){ 
       connect.close();
       connect = new login(id.getText(),pass.getText(),catched[0]);
       dis.setEnabled(true);
      }
      else if(connect.rmnemonic.equals("911") || connect.rmnemonic.equals("919")){
       connect.close(); printmsg("������ �����߽��ϴ�..."); break;
      }
      else break;
     }  
     if(!connect.error){
      connect.send("USR");
      connect.need(connect.receiveBytes());
      if(connect.rmnemonic.equals("USR")){
       nickname.setEnabled(true);
       nickname.setText(connect.nickname);
       printmsg("������ �����߽��ϴ�...");
       connect.readCookie(); 
       connect.send("CHG","NLN");
       connect.receive();
      }
      else{
       connect.close();
       printmsg("������ �����߽��ϴ�...");
      }
     }
     if(!connect.error){
     dis.setEnabled(true);
     change.setEnabled(true);
     status.setEnabled(true);
     printmsg("�α��ο� �����߽��ϴ�...");
     try{ s = new ServerSocket(6891); } catch(Exception e){ System.out.println(e); }
     new runserver().start();
     connect.send("SYN");
     }
     if(connect.error){
      printmsg("�α��ο� �����Ͽ����ϴ�...");
      nickname.setEnabled(false); 
      connection.setEnabled(true); 
      id.setEditable(true); 
      pass.setEditable(true);
      change.setEnabled(false);
      status.setEnabled(false);
      connections = false;
     }
    }
   if(id.getText().length()==0 || pass.getText().length()==0)
    printmsg("ID�� Password�� �Է��ϼ̴��� Ȯ���� �ּ���...");   
   }
  }
  
  public boolean action(Event evt, Object arg) 
  {
   if(arg.equals("����") && !connections){ //������ ������Ű�� ���� ��ư�� ���õ� �׼𸮽���...
   new lg().start();
  }

  else if(arg.equals("����") && connections){
   connect.send("OUT");
   connect.receive();
   connect.error = true;
   connect.close();
   nickname.setEnabled(false); 
   connection.setEnabled(true); 
   dis.setEnabled(false); 
   id.setEditable(true); 
   pass.setEditable(true);
   change.setEnabled(false);
   status.setEnabled(false);
   connections = false;
   printmsg("������ ������ �����մϴ�...");
  }

  else if(arg.equals("�ٲٱ�")){
   connect.send("REA",nickname.getText());
  }

  else return super.action(evt,arg);
  return true;
 }

 public void actionPerformed(ActionEvent ae)
 {
  if(!connections) new lg().start();
  if(connections) connect.send("REA",nickname.getText());
 }

 public void itemStateChanged(ItemEvent ev){
  if(ev.getStateChange() == ItemEvent.SELECTED){
   if(ev.getItem().equals("�¶���")) connect.send("CHG","NLN");
   if(ev.getItem().equals("�ٸ��빫��")) connect.send("CHG","BSY");
   if(ev.getItem().equals("�𵹾ƿ�����")) connect.send("CHG","BRB");
   if(ev.getItem().equals("�ڸ����")) connect.send("CHG","AWY");
   if(ev.getItem().equals("�Ļ���")) connect.send("CHG","LUN");
   if(ev.getItem().equals("��ȭ��")) connect.send("CHG","PHN");
   if(ev.getItem().equals("��������ǥ��")) connect.send("CHG","HDN");
  }
 }



 static public void main(String args[]){
  Frame i = new ps();
 }
}


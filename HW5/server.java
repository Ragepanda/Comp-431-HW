import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.io.StringReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;


	public class server {
		
	
		public static void main(String[] args) throws IOException{
	String first;
	String next;
	String[] holder = new String[10];
	String mailer;
	String[] recipients;
	recipients = new String[25];
	String[] content;
	content = new String[100];
	int contentCount=0;
	int recipientCount=0;
	mailCheck mail = new mailCheck();
	rcptCheck rcpt = new rcptCheck();
	boolean helo = true;
	String heloCommand;
	String heloArray[]= new String[2];
	String[]  domain= new String[10];
	int port = Integer.parseInt(args[0]);
	String fileName[] = new String[2];
	
	
	try{ServerSocket testSocket = new ServerSocket(port);
	testSocket.close();}
	catch(BindException e){
		System.out.println("Port is busy, try another");
		System.exit(0);
	}
	
	ServerSocket welcomeSocket = new ServerSocket(port);
	
	while(true){
		Socket connectionSocket = welcomeSocket.accept();
		
		DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		DataInputStream input = new DataInputStream(connectionSocket.getInputStream());
		
		
			outToClient.writeUTF("220 cs.unc.edu\n");
				heloCommand=input.readUTF();
				if(heloCommand.equals("QUIT"))
					break;
				if(heloCommand.contains("HELO")){
					helo=false;
					heloArray = heloCommand.split(" ");
					outToClient.writeUTF("250"+heloArray[1]+"\n");
				}
				else{
					outToClient.writeUTF("503 Bad sequence of commands\n");
					System.out.println("BAD SEQUENCE");
				}
				
			
		while(true){
			first = input.readUTF();
			//boolean isMailValid = mail.check(first);
			if(first.equals(null)|first.equals("QUIT")) break;
			if(first.contains("MAIL FROM:")){
				outToClient.writeUTF("250 OK\n");
				mailer = first;
				next= input.readUTF();
				if(next.contains("MAIL")|first.contains("DATA")){
					outToClient.writeUTF("503 Bad sequence of commands\n");
					System.out.println("BAD SEQUENCE");
				}
				
				if(next.equals("QUIT"))
					break;
				holder = next.split(" ");
					if (holder[2].contains("<")&holder[2].contains("@")&holder[2].contains(">")){
						outToClient.writeUTF("250 OK\n");
						rcpt.setOutput("neutral");
						recipients[0]=next;
						recipientCount++;
					}
					else{
						outToClient.writeUTF("501 Syntax error in parameters or arguments\n");
						rcpt.setOutput("neutral");
					}
						rcpt.setOutput("neutral");
					
				while(true){
					next= input.readUTF();
					
					if(next.equals("QUIT")) break;
					
					
					if(next.contains("MAIL")){
						outToClient.writeUTF("503 Bad sequence of commands\n");
					}
					
					
					holder = next.split(" +");
					if(next.contains("RCPT")){
						if (rcpt.checkRcpt(next,holder)){
							if(!rcpt.checkPath(holder)){
								outToClient.writeUTF(rcpt.getOutput()+"\n");
								rcpt.setOutput("neutral");
							}
							outToClient.writeUTF(rcpt.getOutput()+"\n");
							recipients[recipientCount]=next;
							recipientCount++;
						}
						else{
								outToClient.writeUTF(rcpt.getOutput()+"\n");
								
						}

					}
					
					
					if(next.contains("DATA")){
						String Start = "354 Start";
						outToClient.writeUTF(Start+"\n");
						outToClient.writeUTF("354 Start");
						next = input.readUTF();
						while(true){
							content[contentCount]=next;
							content[contentCount].trim();
							
							
							next = input.readUTF();
							if(content[contentCount].length()==1){
								outToClient.writeUTF("250 OK\n");
								break;
							}
							contentCount++;
						}
						if(next.equals("QUIT")) break;
						
						//outToClient.writeUTF("250 OK\n");
						
							
							domain=mailer.split(" ");
							domain[2]=domain[2].replace("<", " ");
							domain[2]=domain[2].replace(">", " ");
							domain[2]=domain[2].trim();
							domain[2]=domain[2].replace("@", " ");
							fileName=domain[2].split(" ");
							
							PrintWriter writer = new PrintWriter(fileName[1]);
							holder = mailer.split(" ");
							holder[2]=holder[2].trim();
							writer.println("From: "+holder[2]);

							for(int i=0;i<recipientCount;i++){
								holder = recipients[i].split(" ");
								holder[2].trim();
								writer.println("To: "+holder[2]);
							}
					
							for(int i=0;i<contentCount;i++){
									writer.println(content[i]);
							}
							writer.close();
							System.out.println("Service Complete");
							break;
					}
					
			}
					break;
		}
			else{
				outToClient.writeUTF("503 Bad sequence of commands\n");
			}
	
	}
		connectionSocket.close();
		Arrays.fill(content, null);
		Arrays.fill(recipients, null);
		Arrays.fill(heloArray, null);
		Arrays.fill(holder, null);
		Arrays.fill(domain, null);
		Arrays.fill(fileName, null);
		recipientCount=0;
		contentCount=0;
		helo=true;
		}

	}
		
	}
	
	 class rcptCheck {
			String checkRcpt;
			String output="neutral";
			
			public boolean checkRcpt(String s, String[] StrSet){
				if(StrSet.length==1){
					setOutput("500 Syntax error: command unrecognized Poo");
					return false;
				}
				
				if(s.matches("RCPT TO:(.*)")){
					setOutput("250 OK");
					return true;
				}
				else{
					setOutput("500 Syntax error: command unrecognized");
					return false;
				}
			}
			
			public boolean checkPath(String[] StrSet){
				if(StrSet.length==2){
					if((StrSet[0]+" "+StrSet[1]).matches("RCPT TO:<(.+)@(.+)>")){
						setOutput("250 OK");
					return true;
				}
					else{
						setOutput("501 Syntax error in parameters or arguments");
						return false;}
					}
				
				if((StrSet[0]+" "+StrSet[1]+" "+StrSet[2]).matches("RCPT TO: <(.*)@(.*)>")){
					if((StrSet[0]+" "+StrSet[1]).matches("RCPT TO:<(.+)@(.+)>")){
						setOutput("250 OK");
						return true;
					}
					else{
						if((StrSet[0]+" "+StrSet[1]+" "+StrSet[2]).matches("RCPT TO:<(.+)( +)(.*)@(.+)>")|
								(StrSet[0]+" "+StrSet[1]+" "+StrSet[2]).matches("RCPT TO:<(.+)@(.*)( +)(.+)>")){
							setOutput("501 Syntax error in parameters or arguments");
							return false;
						}
						else{
							if((StrSet[0]+" "+StrSet[1]+" "+StrSet[2]).contains("!|#|$|%|^|&|*|(|)|-|=|+|_|~")){
								setOutput("501 Syntax error in parameters or arguments");
							return false;
							}
							else{
								setOutput("501 Syntax error in parameters or arguments");
								return false;
							}
						}
					}
					
				}
				
				if((StrSet[0]+" "+StrSet[1]+" "+StrSet[2]).matches("RCPT TO: <(.+)@(.+)>")){
					setOutput("250 OK");
					return true;
				}
				
				checkRcpt=StrSet[2];
				if (checkRcpt.contains("<")&checkRcpt.contains("@")&checkRcpt.contains(">")&!checkRcpt.contains("<@")&!checkRcpt.contains("@>")){
					if(checkRcpt.matches("<(.*)@(.*)>")|checkRcpt.matches("FROM:<(.*)@(.*)>")){
						setOutput("250 OK");
						return true;
					}
				}
				
				else{
					checkRcpt = StrSet[2];
						if(checkRcpt.matches("<(.+)(.*)@(.*)(.+)>")){
							setOutput("501 Syntax error in parameters or arguments\n");
							return false;
						}
						else{
							if(checkRcpt.matches("<@(.*)>")|checkRcpt.matches("<(.*)@>")){
								setOutput("501 Syntax error in parameters or arguments");
								return false;
							}
						
							setOutput("501 Syntax error in parameters or arguments\n");
						return false;
					}}
					
					
					
					
				
				setOutput("501 Syntax error in parameters or arguments\n");
					return false;
				
				
			
			}
			public String getOutput(){
				return output;
			}
			public void setOutput(String s){
				if(getOutput().equals("neutral"))
					output = s;
				if(!isOutputError())
					output = s;
				if(s.equals("neutral")){
					output=s;
				}
			}
			
			public boolean isOutputError(){
				if(getOutput().equals("501 Syntax error in parameters or arguments")|getOutput().equals("500 Syntax error: command unrecognized")){
					return true;
				}
				else{
					return false;
				}
			}
		}
	 
	  class mailCheck {

			public boolean check(String first){
				String[] holder;
				listCheck list = new listCheck();
				
					if(first.contains("RCPT")|first.contains("DATA")){
						return false;
					}
					holder=first.split(" +");
					
					if(list.checkMailFrom(first, holder))   
						if(list.checkPath(holder))
							return true;
						else 
							return false;
							
								return false;
							
							
			}
		}

	  class listCheck{
			
			
			
			
			String checkMail;
			public boolean checkMailFrom(String s, String[] StrSet){
				if(StrSet.length==1){
					System.out.println("500 Syntax error: command unrecognized\n");
					return false;
				}
				
				if(s.contains("MAIL FROM:")){
					return true;
				}
				else{
					System.out.println("500 Syntax error: command unrecognized\n");
					return false;
				}
			}
			
			public boolean checkPath(String[] StrSet){
				if(StrSet.length==2){
					if((StrSet[0]+" "+StrSet[1]).matches("MAIL FROM:<(.+)@(.+)>")){
					return true;
				}
					else{
						System.out.println("501 Syntax error in parameters or arguments");
						return false;}
					}
				
				if((StrSet[0]+" "+StrSet[1]+" "+StrSet[2]).matches("MAIL FROM:<(.*)@(.*)>")){
					if((StrSet[0]+" "+StrSet[1]).matches("MAIL FROM:<(.+)@(.+)>")){
						return true;
					}
					else{
						if((StrSet[0]+" "+StrSet[1]+" "+StrSet[2]).matches("MAIL FROM:<(.+)( +)(.*)@(.+)>")|
								(StrSet[0]+" "+StrSet[1]+" "+StrSet[2]).matches("MAIL FROM:<(.+)@(.*)( +)(.+)>")){
							System.out.println("501 Syntax error in parameters or arguments");
							return false;
						}
						else{
							if((StrSet[0]+" "+StrSet[1]+" "+StrSet[2]).contains("!|#|$|%|^|&|*|(|)|-|=|+|_|~")){
							System.out.println("501 Syntax error in parameters or arguments");
							return false;
							}
							else{
								System.out.println("501 Syntax error in parameters or arguments");
								return false;
							}
						}
					}
					
				}
				
				if((StrSet[0]+" "+StrSet[1]+" "+StrSet[2]).matches("MAIL FROM: <(.+)@(.+)>")){

					return true;
				}
				
				checkMail=StrSet[2];
				if (checkMail.contains("<")&checkMail.contains("@")&checkMail.contains(">")&!checkMail.contains("<@")&!checkMail.contains("@>")){
					if(checkMail.matches("<(.*)@(.*)>")|checkMail.matches("FROM:<(.*)@(.*)>")){
						return true;
					}
				}
				
				else{
					checkMail = StrSet[2];
						if(checkMail.matches("<(.+)(.*)@(.*)(.+)>")){
							System.out.println("501 Syntax error in parameters or arguments\n");
							return false;
						}
						else{
							if(checkMail.matches("<@(.*)>")|checkMail.matches("<(.*)@>")){
								System.out.println("501 Syntax error in parameters or arguments");
								return false;
							}
						
						System.out.println("501 Syntax error in parameters or arguments\n");
						return false;
					}}
					
					
					
					
				
					System.out.println("501 Syntax error in parameters or arguments\n");
					return false;
				
				
			
			}
		}
			

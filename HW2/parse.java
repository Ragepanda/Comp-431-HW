import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.io.StringReader;


	public class parse {
		
	
		public static void main(String[] args) throws IOException{
	String first;
	String next;
	String[] holder;
	String mailer;
	String[] recipients;
	recipients = new String[25];
	String[] content;
	content = new String[100];
	int contentCount=0;
	int recipientCount=0;
	mailCheck mail = new mailCheck();
	rcptCheck rcpt = new rcptCheck();
	
	System.out.println(" ");
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	
	while(true){
		while(true){
			first = input.readLine();
			boolean isMailValid = mail.check(first);
			if(first.equals(null)) break;
			if(isMailValid){
				System.out.println("250 OK");
				mailer = first;
				next= input.readLine();
				System.out.println(next);
				if(next.contains("MAIL")|first.contains("DATA")){
					System.out.println("503 Bad sequence of commands");
					break;
				}
				holder = next.split(" +");
				
					if (rcpt.checkRcpt(next,holder)){
						if(!rcpt.checkPath(holder)){
							break;
						}
						recipients[0]=next;
						recipientCount++;
					}
					else
						break;
				
				while(true){
					next= input.readLine();
					System.out.println(next);
					if(next.contains("MAIL")){
						System.out.println("503 Bad sequence of commands");
						break;
					}
					
					
					holder = next.split(" +");
					if(next.contains("RCPT")){
						if (rcpt.checkRcpt(next,holder)){
							if(!rcpt.checkPath(holder))
								break;
							recipients[recipientCount]=next;
							recipientCount++;
						}
					}
					if(next.contains("DATA")){
						System.out.println("354 Start mail input; end with <CRLF>.<CRLF>");
						next = input.readLine();
						while(!next.equals(".")){
							System.out.println(next);
							next = input.readLine();
							content[contentCount]=next;
							contentCount++;
						}
						System.out.println("250 OK");
						for(int i=0;i<recipientCount;i++){
							holder = recipients[i].split(" +");
							holder[2].replace("<", " ");
							holder[2].replace(">", " ");
							holder[2].trim();
							PrintWriter writer = new PrintWriter("forward/"+holder[2]+"");
							holder = mailer.split(" +");
							writer.println("From: "+holder[2]);
							for(i=0;i<recipientCount;i++){
								holder = recipients[i].split(" +");
								writer.println("To: "+holder[2]);
							}
							for(i=0;i<contentCount;i++){
								writer.println(content[i]);
							}
							writer.close();
						}
						break;
					}
					
					if(!next.contains("DATA")&!next.contains("RCPT")&!next.contains("MAIL")){
						System.out.println("500 Syntax error: command unrecognized");
						break;
					}
			}
		}	
	
	}
		Arrays.fill(recipients, null);
		recipientCount=0;
		if(first.equals(null))
			break;
		}

	}
	}
	
	
	

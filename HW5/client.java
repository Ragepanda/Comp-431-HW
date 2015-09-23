import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.IOException;

public class client {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException, InterruptedException {
		File file = new File("outgoing");
		String line="hold";
		String[] holder = new String[10];
		String[] emailMessage = new String[50];
		String domain = args[0];
		int port = Integer.parseInt(args[1]);
		String next = null;
		
		AcceptorStates state = new AcceptorStates();
		
		FileReader message = new FileReader(file);  // Class for managing states and swapping between them
		BufferedReader email = new BufferedReader(message);//BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		state.setMailState(); 							// Initializes program to use mail state
		 String mailCarry = email.readLine();          	// Mail carry is used to hold String values for mail, is used in Data State
		
		
		
		 Socket clientSocket = new Socket(domain,port);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			DataInputStream input = new DataInputStream(clientSocket.getInputStream());
		
		while(!line.equals(null)){       // Will terminate the program if there is an empty entry
			
			if (clientSocket.isClosed()){
				System.out.println("Socket Connection Lost");
				break;
			}
			if(input.readUTF().contains("220")){
				outToServer.writeUTF("HELO " +domain +"\n");	
			}
			else{
				outToServer.writeUTF("QUIT\n");
				System.out.println("QUIT");
			}
			
			
			if(!input.readUTF().contains("250")){
				outToServer.writeUTF("QUIT\n");
				break;
			}
			
			if(state.mailState()){                // The MAIL state
				line=mailCarry;					// The String for Mail is always brought in from another state, so mailCarry is used here
					
				holder =line.split(" ");        
				outToServer.writeUTF("MAIL FROM: "+holder[1]+"\n");
				line=input.readUTF();
				if(line.contains("250 OK")){          // Wait for 250 OK command
					mailCarry=null;     
					state.setRcptState();           // Moves to RCPT State
				}
				else{
					outToServer.writeUTF("QUIT\n");
					break;
				}
			}
			
			
			if(state.rcptState()){                   // The RCPT State
				line=email.readLine();
				holder = line.split(" ");
				outToServer.writeUTF("RCPT TO: "+holder[1]+"\n");
				
				line = input.readUTF();
				if(line.contains("250 OK")){
						state.setDataState();   // Moves to DATA state
				}
				else{
					outToServer.writeUTF("QUIT\n");
					break;
				}
				
				
			}
			
			if(state.dataState()){            // The DATA state
				
				outToServer.writeUTF("DATA\n");
				next = input.readUTF();
				next = input.readUTF();
				if(!next.equals("354 Start")){ // Waits for the 354 Start command
					outToServer.writeUTF("QUIT");
					break;
				}
					
				int i=0;
				emailMessage[0]="Hold";
				while(true){  //Handles data lines until it reaches a new "from" line
					emailMessage[i]=email.readLine();
					if(emailMessage[i]==null) break;
					i++;
				}
				i++;
				outToServer.flush();
				for(int k=0;k<i;k++){
					outToServer.writeUTF(emailMessage[k]+"\n");
				}
					outToServer.writeUTF(".\n"); 
			
				
				mailCarry=line;						 // Holds data for the Mail state, since more readUTF()s will be needed
				            // Prints "." to signify the end of the data entries
				
				if(input.readUTF().contains("250 OK\n")){  //Prompt for 250 OK
					outToServer.writeUTF("QUIT\n");
					state.setMailState();               // Returns to Mail State
					break;
				}
				else{
					outToServer.writeUTF("QUIT\n");
					break;
				}
				
			}
		}
		state.setMailState();
		clientSocket.close();
	
	}

}


class AcceptorStates {

	boolean mail;
	boolean rcpt;
	boolean data;
	public AcceptorStates(){
		mail=true;
		rcpt=false;
		data=false;
	}
		
		public void setMailState(){
			mail=true;
			rcpt=false;
			data = false;
		}
		
		public void setRcptState(){
			mail=false;
			rcpt=true;
			data = false;
		}
		
		public void setDataState(){
			mail=false;
			rcpt=false;
			data=true;
		}
		public boolean mailState(){
			return (mail==true&rcpt==false&data==false);
		}
		public boolean rcptState(){
			return(mail==false&rcpt==true&data==false);
		}
		public boolean dataState(){
			return(mail==false&rcpt==false&data==true);
		}
	}


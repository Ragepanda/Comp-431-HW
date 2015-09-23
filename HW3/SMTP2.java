import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class SMTP2 {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
		String line="hold";
		String[] holder = new String[10];
		
		FileReader message = new FileReader(file);
		AcceptorStates state = new AcceptorStates();     // Class for managing states and swapping between them
		BufferedReader email = new BufferedReader(message);
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		state.setMailState(); 							// Initializes program to use mail state
		 String mailCarry = email.readLine();          	// Mail carry is used to hold String values for mail, is used in Data State
		
		while(!line.equals(null)){       // Will terminate the program if there is an empty entry
			if(state.mailState()){                // The MAIL state
				line=mailCarry;					// The String for Mail is always brought in from another state, so mailCarry is used here
					
				holder =line.split(" ");        
				System.out.println("MAIL FROM: "+holder[1]);
				line=input.readLine();
				
				if(line.equals("250 OK")){          // Wait for 250 OK command
					System.out.println("250 OK");
					mailCarry=null;     
					state.setRcptState();           // Moves to RCPT State
				}
				else{
					System.out.println("QUIT");
					break;
				}
			}
			
			
			if(state.rcptState()){                   // The RCPT State
				line=email.readLine();
				holder = line.split(" ");
				System.out.println("RCPT TO: "+holder[1]);
				
				line = input.readLine();
				if(line.equals("250 OK")){
						System.out.println("250 OK");
						state.setDataState();   // Moves to DATA state
				}
				else{
					System.out.println("QUIT");
					break;
				}
				
				
			}
			
			if(state.dataState()){            // The DATA state
				
				System.out.println("DATA");
				
				if(input.readLine().equals("354 Start")) // Waits for the 354 Start command
					System.out.println("354 Start");
				else{
					System.out.println("QUIT");
					break;
				}
					
				line=email.readLine();
				while(!line.matches("From: <(.*)>( *)")){  //Handles data lines until it reaches a new "from" line
					System.out.println(line);
					line=email.readLine();
					if(line==null)   // If the end of file is reached at the end of a message, the breaks happen to
							break;			// Terminate the program
				}
				if(line==null){
					break;
				}
				
				mailCarry=line;						 // Holds data for the Mail state, since more readLine()s will be needed
				System.out.println(".");             // Prints "." to signify the end of the data entries
				
				if(input.readLine().equals("250 OK"))  //Prompt for 250 OK
					System.out.println("250 OK");
				
				else{
					System.out.println("QUIT");
					break;
				}
				state.setMailState();               // Returns to Mail State
			}
		}
		if(line==null)      // This checks to see if the loop exited because of a null value so that it doesn't 
			System.out.println("QUIT"); // accidentally replicate the QUIT command
	}

}


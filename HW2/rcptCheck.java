
public class rcptCheck {
	String checkRcpt;
	public boolean checkRcpt(String s, String[] StrSet){
		if(StrSet.length==1){
			System.out.println("500 Syntax error: command unrecognized Poo 1\n");
			return false;
		}
		
		if(s.matches("RCPT TO:(.*)")){
			return true;
		}
		else{
			System.out.println("500 Syntax error: command unrecognized Poo 2\n");
			return false;
		}
	}
	
	public boolean checkPath(String[] StrSet){
		if(StrSet.length==2){
			if((StrSet[0]+" "+StrSet[1]).matches("RCPT TO:<(.+)@(.+)>")){
			System.out.println("250 OK");
			return true;
		}
			else{
				System.out.println("501 Syntax error in parameters or arguments");
				return false;}
			}
		
		if((StrSet[0]+" "+StrSet[1]+" "+StrSet[2]).matches("RCPT TO:<(.*)@(.*)>")){
			if((StrSet[0]+" "+StrSet[1]).matches("RCPT TO:<(.+)@(.+)>")){
				System.out.println("250 OK");
				return true;
			}
			else{
				if((StrSet[0]+" "+StrSet[1]+" "+StrSet[2]).matches("RCPT TO:<(.+)( +)(.*)@(.+)>")|
						(StrSet[0]+" "+StrSet[1]+" "+StrSet[2]).matches("RCPT TO:<(.+)@(.*)( +)(.+)>")){
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
		
		if((StrSet[0]+" "+StrSet[1]+" "+StrSet[2]).matches("RCPT TO: <(.+)@(.+)>")){
			System.out.println("250 OK");
			return true;
		}
		
		checkRcpt=StrSet[2];
		if (checkRcpt.contains("<")&checkRcpt.contains("@")&checkRcpt.contains(">")&!checkRcpt.contains("<@")&!checkRcpt.contains("@>")){
			if(checkRcpt.matches("<(.*)@(.*)>")|checkRcpt.matches("FROM:<(.*)@(.*)>")){
				System.out.println("250 OK");
				return true;
			}
		}
		
		else{
			checkRcpt = StrSet[2];
				if(checkRcpt.matches("<(.+)(.*)@(.*)(.+)>")){
					System.out.println("501 Syntax error in parameters or arguments\n");
					return false;
				}
				else{
					if(checkRcpt.matches("<@(.*)>")|checkRcpt.matches("<(.*)@>")){
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

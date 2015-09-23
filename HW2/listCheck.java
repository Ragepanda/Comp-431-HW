
		public class listCheck{
			
			
			
			
		String checkMail;
		public boolean checkMailFrom(String s, String[] StrSet){
			if(StrSet.length==1){
				System.out.println("500 Syntax error: command unrecognized\n");
				return false;
			}
			
			if(s.matches("MAIL FROM:(.*)")){
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
				System.out.println("250 OK");
				return true;
			}
				else{
					System.out.println("501 Syntax error in parameters or arguments");
					return false;}
				}
			
			if((StrSet[0]+" "+StrSet[1]+" "+StrSet[2]).matches("MAIL FROM:<(.*)@(.*)>")){
				if((StrSet[0]+" "+StrSet[1]).matches("MAIL FROM:<(.+)@(.+)>")){
					System.out.println("250 OK");
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
				System.out.println("250 OK");
				return true;
			}
			
			checkMail=StrSet[2];
			if (checkMail.contains("<")&checkMail.contains("@")&checkMail.contains(">")&!checkMail.contains("<@")&!checkMail.contains("@>")){
				if(checkMail.matches("<(.*)@(.*)>")|checkMail.matches("FROM:<(.*)@(.*)>")){
					System.out.println("250 OK");
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
		
	
		


public class mailCheck {

	public boolean check(String first){
		String[] holder;
		listCheck list = new listCheck();
		
			if(first.contains("RCPT")|first.contains("DATA")){
				System.out.println("503 Bad sequence of commands");
				return false;
			}
			System.out.print(first+"\n");
			holder=first.split(" +");
			
			if(list.checkMailFrom(first, holder))   
				if(list.checkPath(holder))
					return true;
				else 
					return false;
					
						return false;
					
					
	}
}

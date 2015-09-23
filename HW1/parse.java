import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.StringTokenizer;
import java.io.StringReader;


	public class parse {
		
	
		public static void main(String[] args) throws IOException{
	boolean starter = true;
	String first;
	String[] holder;
	listCheck list = new listCheck();
	
	
	System.out.println(" ");
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	
	while(starter){
		first = input.readLine();
		if(first==null)
			starter=false;
		else{
			System.out.print(first+"\n");
			holder=first.split(" +");
			
			if(list.checkMailFrom(first, holder))
			list.checkPath(holder);
			}	
		}
	}
	}
	
	

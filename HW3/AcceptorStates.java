
public class AcceptorStates {

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



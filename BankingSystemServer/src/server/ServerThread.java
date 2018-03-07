package server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Statement;

public class ServerThread extends Thread{
	
	public Socket serverSocket;
	BufferedReader in;
	PrintWriter out;
	static Statement statement;
	String return_Message;
	String option;
	SQLOperation op = new SQLOperation();
	
	public ServerThread(Socket socket) throws Exception{
		
		if (socket != null){
			
			this.serverSocket = socket;
			in = new BufferedReader(
					new InputStreamReader(
							socket.getInputStream()));
			out = new PrintWriter(
					new BufferedWriter
						(new OutputStreamWriter(
							socket.getOutputStream())), true);
			option = null;
			statement = ServerPanel.connection.createStatement();
				
		}
	}
	
	public void run(){
		
		int nullCounter = 0;
		
		while(true){
			
			try {
				
				option = null;
				option = in.readLine();
				ServerPanel.logArea.setText(ServerPanel.logArea.getText() 
						+ "Receive " + option + "\n\r");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			if (option != null){
				
				String[] command = option.split("@#");
				
				switch(command[0]){
				case "register":	
					return_Message = op.register(command[1], command[2]);
					break;
				case "deposit":		
					return_Message = op.deposit(command[1], command[2]);
					break;
				case "withdraw":	
					return_Message = op.withdraw(command[1], command[2]);
					break;
				case "query":		
					return_Message = op.Query();
					break;
				case "loan":		
					return_Message = op.loan(command[1]);
					break;
				case "repay":		
					return_Message = op.repay(command[1], command[2]);
					break;
				case "modify":
					return_Message = op.modify(command[1], command[2]);
					break;
				case "logOut":
					return_Message = op.logOut();
					break;
				case "login":		
					return_Message = op.login(command[1], command[2]);
					break;
				}
				
				out.println(return_Message);
				out.flush();
				
			}
			else{
				nullCounter++;
			}
			
			if (nullCounter == 5){
				ServerPanel.logArea.setText(ServerPanel.logArea.getText()
						+ "ATM close.\n\r");
				try {
					in.close();
					out.close();
					serverSocket.close();
					System.exit(1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
}

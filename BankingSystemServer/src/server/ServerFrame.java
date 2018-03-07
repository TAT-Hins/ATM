package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;

public class ServerFrame extends JFrame{
	
	public ServerSocket serverSocket;
	
	public ServerFrame(){
		
		try {
			serverSocket = new ServerSocket(9090, 10);
			ServerPanel panel = new ServerPanel(this);
			this.add(panel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setTitle("Sever");
		this.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		while(true)
		{
			Socket socket = new Socket();
			
			try{
				
				socket = serverSocket.accept();
				
				if(socket!=null)
				{
					ServerPanel.logArea.setText(ServerPanel.logArea.getText() + "Connection set up.\n\r");
					ServerThread t = new ServerThread(socket);
					t.start();
				}
				else break;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try {
					if (serverSocket.accept().isClosed()){
						ServerPanel.logArea.setText(ServerPanel.logArea.getText()+"Disconnected\n\r");
						socket.close();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static void main(String[] args){
		ServerFrame frame = new ServerFrame();
	}
}

package server;
import java.awt.*;
import java.net.*;
import java.sql.*;
import java.util.Timer;
import javax.swing.*;

public class ServerPanel extends JPanel{
	
	JFrame serverFrame;
	static public JTextArea logArea;
	
	static String dbURL = "jdbc:mysql://localhost:3306/BankingSystem";
	static public Connection connection;
	static public Statement statement;
	
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(dbURL, "root", "BallKing961006");
			statement = connection.createStatement();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			ServerPanel.logArea.setText(
					ServerPanel.logArea.getText() + "Cannot find the jdbc driver.\n\r");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ServerPanel(JFrame frame){
		
		this.serverFrame = frame;
		
		logArea = new JTextArea("");
		logArea.setEditable(false);
		logArea.setText("connection details:\n\r");
	
		this.setLayout(new BorderLayout());
		this.add(logArea,BorderLayout.CENTER);
		
		Timer timer = new Timer();
		timer.schedule(new InterestThread(), 20000L,20000L);
		
	}
	
}

package server;

import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class SQLOperation {
	
	Connection connection;
	Statement statement;
	ResultSet rs;
	String query;
	String userName = null;
	
	static Date nDate = new Date();
	static SimpleDateFormat sDF = new SimpleDateFormat("yyyy-MM-dd");
	public static String sDate = sDF.format(nDate);
	
	private void setCurrentDate(String date){
		
		String setDateQuery;
		
		try {
			
			if (rs.getDate("1stTransactionTime") == null){
				
				setDateQuery = "UPDATE userData SET 1stTransactionTime = \""
						+ date + "\" WHERE userName = \"" + userName + "\";";
				
				ServerThread.statement.executeUpdate(setDateQuery);
			}
			else{
				
				if (rs.getDate("2ndTransactionTime") == null){
					
					query = "SELECT 1stTransactionTime FROM userData "
							+ "WHERE userName = \"" + userName + "\";";
					execute(query);
					String temp = rs.getDate("1stTransactionTime").toString();
					setDateQuery = "UPDATE userData SET 1stTransactionTime = \""
							+ date + "\",  2ndTransactionTime = \"" + temp 
							+ "\" WHERE userName = \"" + userName + "\";";
					ServerThread.statement.executeUpdate(setDateQuery);
					
				}
				else{
					
					query = "SELECT * FROM userData "
							+ "WHERE userName = \"" + userName + "\";";
					execute(query);
					String temp1 = rs.getDate("1stTransactionTime").toString();
					String temp2 = rs.getDate("2ndTransactionTime").toString();
					setDateQuery = "UPDATE userData SET 1stTransactionTime = \""
							+ date + "\",  2ndTransactionTime = \"" + temp1
							+ "\", 3rdTransactionTime = \"" + temp2
							+ "\" WHERE userName = \"" + userName + "\";";
					ServerThread.statement.executeUpdate(setDateQuery);
					
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void execute(String query){
		try{
			rs = ServerPanel.statement.executeQuery(query);
			rs.first();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String login(String name, String pswd){
		
		query = "SELECT * FROM userData "
				+ "WHERE userName = \"" + name + "\" AND userPswd = \"" + pswd +"\";";
		execute(query);
		
		try {
			
			if (rs.getRow() != 0){
				userName = name;
				setCurrentDate(sDate);
				return "login@#!!!";
			}
			else return "login@#???@#failed";
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "login@#???@#SQLException";
		}
	}
	
	public String Query(){
		
		String temp = null;
		query = "SELECT * FROM userData WHERE userName = \"" + userName + "\";";
		execute(query);
		
		try {
			temp = Double.toString(rs.getDouble("checkingAc")) + "@#"
					+ Double.toString(rs.getDouble("savingAc_05y")) + "@#"
					+ Double.toString(rs.getDouble("savingAc_1y")) + "@#"
					+ Double.toString(rs.getDouble("savingAc_5y")) + "@#"
					+ Double.toString(rs.getDouble("loanAc")) + "@#";
			return "query@#!!!@#" + temp;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "query@#???@#SQLException";
		}
		
	}
	
	public String register(String name, String pswd){
		
		query = "SELECT userName FROM userData "
				+ "WHERE userName = \"" + name + "\";";
		execute(query);
		
		try {
			
			if (rs.getRow() != 0){
				return "register@#???@#existed";
			}
			else{
				
				String regQuery = "INSERT INTO userData (userName, userPswd)"
						+ "VALUES (\"" + name + "\", \"" + pswd + "\");";
				ServerThread.statement.executeUpdate(regQuery);
				
				query = "SELECT userName FROM userData "
						+ "WHERE userName = \"" + name  + "\";";
				execute(query);
				
				if (rs.getString("userName").equals(name))
					return "register@#!!!";
				else return "register@#???@#failed";
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "register@#???@#SQLException";
		}
		
	}
	
	
	public String deposit(String amount, String account){
		
		double depositBalance = 0;
		double depositAmount = Double.parseDouble(amount);
		query = "SELECT " + account + " FROM userData"
				+ " WHERE userName = \"" + userName + "\";";
		ServerPanel.logArea.setText(
				ServerPanel.logArea.getText() + query + "\n\r");
		execute(query);
		
		try {
			depositBalance = rs.getDouble(account);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double newBalance = depositAmount + depositBalance;
		
		try {
			
			String depositQuery = "UPDATE userData SET "+ account + " = " + Double.toString(newBalance) +
				" WHERE userName = \"" + userName + "\";";
			ServerThread.statement.executeUpdate(depositQuery);
			
			execute(query);
			ServerPanel.logArea.setText(
					ServerPanel.logArea.getText() + rs.getDouble(account) + "\n\r");
			
			if (rs.getDouble(account) == newBalance){
				return "deposit@#!!!@#" + Double.toString(newBalance);
			}
			else return "deposit@#???@#failed";
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "deposit@#???@#SQLException";
		}
		
	}
	
	public String withdraw(String amount, String account){

		double depositBalance = 0;
		double depositAmount = Double.parseDouble(amount);
		
		if (account.equals("checkingAc")){
			
			query = "SELECT " + account + " FROM userData"
					+ " WHERE userName = \"" + userName + "\";";
			ServerPanel.logArea.setText(
					ServerPanel.logArea.getText() + query + "\n\r");
			execute(query);
			
			try {
				depositBalance = rs.getDouble(account);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			double newBalance = depositBalance - depositAmount;
			
			try {
				
				String depositQuery = "UPDATE userData SET "+ account + " = " + Double.toString(newBalance) +
					" WHERE userName = \"" + userName + "\";";
				ServerPanel.logArea.setText(
						ServerPanel.logArea.getText() + depositQuery + "\n\r");
				ServerThread.statement.executeUpdate(depositQuery);
				
				execute(query);
				ServerPanel.logArea.setText(
						ServerPanel.logArea.getText() + rs.getDouble(account) + "\n\r");
				
				if (rs.getDouble(account) == newBalance){
					return "withdraw@#!!!@#" + Double.toString(newBalance);
				}
				else return "withdraw@#???@#failed";
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "withdraw@#???@#SQLException";
			}
			
		}
		else{
			
			double checkingBalance = 0;
			
			query = "SELECT " + account + " FROM userData"
					+ " WHERE userName = \"" + userName + "\";";
			ServerPanel.logArea.setText(
					ServerPanel.logArea.getText() + query + "\n\r");
			execute(query);
			
			try {
				depositBalance = rs.getDouble(account);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			query = "SELECT checkingAc FROM userData"
					+ " WHERE userName = \"" + userName + "\";";
			ServerPanel.logArea.setText(
					ServerPanel.logArea.getText() + query + "\n\r");
			execute(query);
			
			try {
				checkingBalance = rs.getDouble("checkingAc");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			double newBalance = depositBalance + checkingBalance - Double.parseDouble(amount);
			
			try {
				
				String depositQuery = "UPDATE userData SET " + account + " = 0"
						+ " WHERE userName = \"" + userName + "\";";
				ServerThread.statement.executeUpdate(depositQuery);
				
				depositQuery = "UPDATE userData SET checkingAc = " + Double.toString(newBalance)
					+ " WHERE userName = \"" + userName + "\";";
				ServerThread.statement.executeUpdate(depositQuery);
				
				execute(query);
				ServerPanel.logArea.setText(
						ServerPanel.logArea.getText() + rs.getDouble("checkingAc") + "\n\r");
				
				
				if (rs.getDouble("checkingAc") == newBalance){
					return "withdraw@#!!!@#" + Double.toString(newBalance);
				}
				else return "withdraw@#???@#failed";
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "withdraw@#???@#SQLException";
			}
			
		}
		
	}
	
	public String loan(String amount){
		
		try {
			
			String loanQuery = "UPDATE userData SET loanAc = " 
					+ amount + ", loanTime = \"" + sDate 
					+ "\" WHERE userName = \"" + userName + "\";";
			ServerThread.statement.executeUpdate(loanQuery);
			
			query = "SELECT loanAc FROM userData WHERE userName = \""
					+ userName + "\";";
			execute(query);
			
			if (rs.getDouble("loanAc") == Double.parseDouble(amount)){
				return "loan@#!!!@#" + rs.getDouble("loanAc");
			}
			else return "loan@#???@#failed";
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "loan@#???@#SQLException";
		}
		
	}
	
	public String repay(String amount, String account){
		
		return "";
		
	}
	
	public String modify(String oldPswd, String newPswd){
		
		query = "SELECT userPswd FROM userData "
				+ "WHERE userName = \"" + userName + "\";";
		execute(query);
		
		try {
			
			if (!rs.getString("userPswd").equals(oldPswd)){
				return "modify@#???@#wrong";
			}
			else{
				
				String modQuery = "UPDATE userData SET userPswd = \"" 
						+ newPswd + "\" WHERE userName = \"" + userName + "\";";
				ServerThread.statement.executeUpdate(modQuery);
				
				query = "SELECT userPswd FROM userData "
						+ "WHERE userName = \"" + userName  + "\";";
				execute(query);
				
				if (rs.getString("userPswd").equals(newPswd))
					return "modify@#!!!";
				else return "modify@#???@#failed";
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "modify@#???@#SQLException";
		}
	}
	
	public String logOut(){
		
		query = null;
		userName = null;
		rs = null;
		return "logOut@#!!!";
		
	}
	
}

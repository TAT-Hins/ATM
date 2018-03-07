package server;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.*;

public class InterestThread extends TimerTask{
	ResultSet rs;
	java.sql.Statement dealStatement;
	NumberFormat nFormat;
	String account = null;
	
	public InterestThread()
	{
		try
		{
			rs = ServerPanel.statement.executeQuery("SELECT * FROM userData");
			dealStatement = ServerPanel.connection.createStatement();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void run()
	{
		
		try
		{
			rs = ServerPanel.statement.executeQuery("SELECT * FROM userData");
			while(rs.next())
			{
				
				String userName = rs.getString("userName");
				double checkingDeposit = rs.getDouble("checkingAc");
				double savingDeposit_05y = rs.getDouble("savingAc_05y");
				double savingDeposit_1y = rs.getDouble("savingAc_1y");
				double savingDeposit_5y = rs.getDouble("savingAC_5y");
				checkingDeposit *= 1.0015;
				savingDeposit_05y *= 1.02;
				savingDeposit_1y *= 1.03;
				savingDeposit_5y *= 1.045;
				BigDecimal b = new BigDecimal(checkingDeposit); 
				BigDecimal c = new BigDecimal(savingDeposit_05y); 
				BigDecimal d = new BigDecimal(savingDeposit_1y); 
				BigDecimal e = new BigDecimal(savingDeposit_5y); 
				double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				double f2 = c.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				double f3 = d.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				double f4 = e.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				dealStatement.executeUpdate("UPDATE userData SET checkingAc="+ f1 +" WHERE userName=\""+userName+"\"");
				dealStatement.executeUpdate("UPDATE userData SET savingAc_05y="+ f2 +" WHERE userName=\""+userName+"\"");
				dealStatement.executeUpdate("UPDATE userData SET savingAc_1y="+ f3 +" WHERE userName=\""+userName+"\"");
				dealStatement.executeUpdate("UPDATE userData SET savingAC_5y="+ f4 +" WHERE userName=\""+userName+"\"");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

package atm;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.text.BadLocationException;

public class SystemClient extends Thread{

	
	Socket clientSocket;
	BufferedReader in;
	PrintWriter out;
	String sentMessage;
	String receivedMessage;
	String username;
		
	public SystemClient(){
		
		try{
				
			clientSocket = new Socket("192.168.1.141", 9090);
			in = new BufferedReader(
					new InputStreamReader(
							this.clientSocket.getInputStream()));
			out = new PrintWriter(
					new BufferedWriter
						(new OutputStreamWriter(
							this.clientSocket.getOutputStream())), true);
				
		}catch(UnknownHostException uHE){
			uHE.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		
	public void sendMessage(){
		out.println(sentMessage);
		System.out.println("send " + sentMessage);
		out.flush();
	}
	
	public void receiveMessage(){
		try {
			receivedMessage = null;
			receivedMessage = in.readLine();
			System.out.println("receive " + receivedMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class LoginFrame extends JFrame{
		
		public LoginFrame(){
			LoginPanel panel = new LoginPanel(this);
			this.setTitle("����ϵͳ");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.add(panel);
			this.setSize(400, 300);
			
		}
	}
	
	public class LoginPanel extends JPanel{
		JFrame loginFrame;
		JButton regButton, logButton;
		JLabel userNameLabel, userPswdLabel;
		JTextField userNameField;
		JPasswordField userPswdField;
		String[] rMessageBox = null;
		
		public LoginPanel(JFrame frame){
			
			this.loginFrame = frame;
			userNameLabel = new JLabel("�û���");
			userPswdLabel = new JLabel("����");
			userNameField = new JTextField(25);
			userPswdField = new JPasswordField(20);
			
			regButton = new JButton("ע��");
			regButton.setSize(50, 50);
			regButton.addActionListener(new regListener());
			
			logButton = new JButton("��¼");
			logButton.setSize(50, 50);
			logButton.addActionListener(new logListener());
			
			JPanel namePanel = new JPanel();
			namePanel.add(userNameLabel);
			namePanel.add(userNameField);
			
			JPanel pswdPanel = new JPanel();
			pswdPanel.add(userPswdLabel);
			pswdPanel.add(userPswdField);
			
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(regButton);
			buttonPanel.add(logButton);
			
			this.setLayout(new GridLayout(3,1));
			this.add(namePanel);
			this.add(pswdPanel);
			this.add(buttonPanel);
		}
		
		private class logListener implements ActionListener{
			public void actionPerformed(ActionEvent event){
				
				if (userNameField.getText().equals("") 
						|| new String(userPswdField.getPassword()).equals("")){
					JOptionPane.showMessageDialog(loginFrame, 
							"�û��������벻��Ϊ�գ�", "��ʾ", JOptionPane.ERROR_MESSAGE);
				}
				else{
					
					sentMessage = "login@#" + userNameField.getText() + "@#" + new String(userPswdField.getPassword());
					sendMessage();
					
					receiveMessage();
					rMessageBox = receivedMessage.split("@#");
					
					if (rMessageBox[1].equals("!!!")){
						username = userNameField.getText();
						MainFrame mainFrame = new MainFrame();
						loginFrame.setVisible(false);
						mainFrame.setVisible(true);
						loginFrame.dispose();
					}
					else if (rMessageBox[1].equals("???")){
						
						switch(rMessageBox[2]){
							case "failed":
								JOptionPane.showMessageDialog(loginFrame, 
										"�û��������������!���������룡", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
								userPswdField.setText("");
								break;
							case "SQLException":
								JOptionPane.showMessageDialog(loginFrame, 
										"���ݿ�����쳣��", "��ʾ", JOptionPane.ERROR_MESSAGE);
								System.exit(1);
							default:
								JOptionPane.showMessageDialog(loginFrame, 
										"δ֪����", "��ʾ", JOptionPane.ERROR_MESSAGE);
								System.exit(1);
						}
						
					}
					
					else{
						JOptionPane.showMessageDialog(loginFrame, 
								"δ֪����", "��ʾ", JOptionPane.ERROR_MESSAGE);
						System.exit(1);
					}
					
				}
				
			}
		}
		
		private class regListener implements ActionListener{
			public void actionPerformed(ActionEvent event){
				RegFrame regFrame = new RegFrame();
				loginFrame.setVisible(false);
				regFrame.setVisible(true);
				loginFrame.dispose();
			}
		}
	}
	
	public class RegFrame extends JFrame{
		
		public RegFrame(){
			RegPanel panel = new RegPanel(this);
			this.setTitle("ע��");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.add(panel);
			this.setSize(400, 300);
		}
		
	}
	
	public class RegPanel extends JPanel{
		
		JFrame regFrame;
		JLabel userNameLabel, userPswdLabel, confirmPswdLabel, 
				notiArea1, notiArea2, notiArea3;
		JTextField userNameField;
		JPasswordField userPswdField, confirmPswdField;
		JButton confirmButton, backButton;
		String[] rMessageBox = null;
		
		public RegPanel(JFrame frame){
			
			this.regFrame = frame;
			userNameLabel = new JLabel("�û���");
			userPswdLabel = new JLabel("��������");
			confirmPswdLabel = new JLabel("ȷ������");
			notiArea1 = new JLabel("ע�⣺", JLabel.CENTER);
			notiArea2 = new JLabel("�û��������������20λ���µ�Ӣ�����ּ�������϶���", JLabel.CENTER);
			notiArea3 = new JLabel("����ʹ������!", JLabel.CENTER);
			
			userNameField = new JTextField(20);
			userPswdField = new JPasswordField(20);
			confirmPswdField = new JPasswordField(20);
			
			confirmButton = new JButton("ȷ��");
			confirmButton.setSize(50, 50);
			confirmButton.addActionListener(new confirmListener());
			backButton = new JButton("����");
			backButton.setSize(50, 50);
			backButton.addActionListener(new backListener());
			
			JPanel namePanel = new JPanel();
			namePanel.add(userNameLabel);
			namePanel.add(userNameField);
			
			JPanel pswdPanel = new JPanel();
			pswdPanel.add(userPswdLabel);
			pswdPanel.add(userPswdField);
			
			JPanel confirmPswdPanel = new JPanel();
			confirmPswdPanel.add(confirmPswdLabel);
			confirmPswdPanel.add(confirmPswdField);
			
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(confirmButton);
			buttonPanel.add(backButton);
			
			JPanel notiArea = new JPanel();
			notiArea.setLayout(new GridLayout(3,1));
			notiArea.add(notiArea1);
			notiArea.add(notiArea2);
			notiArea.add(notiArea3);
			
			this.setLayout(new GridLayout(5,1));
			this.add(notiArea);
			this.add(namePanel);
			this.add(pswdPanel);
			this.add(confirmPswdPanel);
			this.add(buttonPanel);
			
		}
		
		private class confirmListener implements ActionListener{
			public void actionPerformed(ActionEvent event)
			{
				while (!(new String(userPswdField.getPassword()).equals(
						new String(confirmPswdField.getPassword())))){
					JOptionPane.showMessageDialog(regFrame, "��������������������������", "��ʾ", JOptionPane.ERROR_MESSAGE);
					userPswdField.setText("");
					confirmPswdField.setText("");
				}
				
				sentMessage = "register@#" + userNameField.getText() + "@#" 
							+ new String(userPswdField.getPassword());
				sendMessage();
				
				receiveMessage();
				rMessageBox = receivedMessage.split("@#");
				
				if (rMessageBox[1].equals("!!!")){
					JOptionPane.showMessageDialog(regFrame, "ע��ɹ����뷵�����µ�¼��", "�ɹ�",JOptionPane.OK_OPTION);
					LoginFrame loginFrame = new LoginFrame();
					regFrame.setVisible(false);
					loginFrame.setVisible(true);
					regFrame.dispose();
				}
				else if (rMessageBox[1].equals("???")){
					
					switch(rMessageBox[2]){
						case "existed":	
							JOptionPane.showMessageDialog(regFrame, 
									"�û����Ѵ��ڣ�������ѡ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
							userNameField.setText("");
							userPswdField.setText("");
							confirmPswdField.setText("");
							break;
						case "failed":
							JOptionPane.showMessageDialog(regFrame, 
									"ע��ʧ�ܣ������³��ԣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
							userNameField.setText("");
							userPswdField.setText("");
							confirmPswdField.setText("");
							break;
						case "SQLException":
							JOptionPane.showMessageDialog(regFrame, 
									"���ݿ�����쳣��", "��ʾ", JOptionPane.ERROR_MESSAGE);
							System.exit(1);
						default:
							JOptionPane.showMessageDialog(regFrame, 
									"δ֪����", "��ʾ", JOptionPane.ERROR_MESSAGE);
							System.exit(1);
					}
					
				}
				
				else{
					JOptionPane.showMessageDialog(regFrame, 
							"δ֪����", "��ʾ", JOptionPane.ERROR_MESSAGE);
					System.exit(1);
				}
				
			}
		}
		
		private class backListener implements ActionListener{
			public void actionPerformed(ActionEvent event)
			{
				LoginFrame loginFrame = new LoginFrame();
				regFrame.setVisible(false);
				loginFrame.setVisible(true);
				regFrame.dispose();
			}
		}
		
	}
	
	public class MainFrame extends JFrame{
		
		public MainFrame(){
			MainPanel panel = new MainPanel(this);
			this.setTitle("���˵�");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.add(panel);
			this.setSize(400, 300);
		}
		
	}
	
	public class MainPanel extends JPanel{
		
		JFrame mainFrame;
		JLabel menu;
		JButton queryButton, depositButton, withdrawButton, loanButton,
				repayButton, pswdModifyButton, logOutButton, quitButton;
		String[] rMessageBox = null;
		
		
		public MainPanel(JFrame frame){
			
			this.mainFrame = frame;
			menu = new JLabel("��ӭ�����ϵͳ����ѡ��������Ҫ�ķ���");
			
			queryButton = new JButton("��ѯ");
			queryButton.setSize(50, 50);
			queryButton.addActionListener(new queryListener());
			
			depositButton = new JButton("���");
			depositButton.addActionListener(new depositListener());
			
			withdrawButton = new JButton("ȡ��");
			withdrawButton.addActionListener(new withdrawListener());
			
			loanButton = new JButton("����");
			loanButton.addActionListener(new loanListener());
			
			repayButton = new JButton("����");
			repayButton.addActionListener(new repayListener());
			
			pswdModifyButton = new JButton("�޸�����");
			pswdModifyButton.addActionListener(new modifyListener());
			
			logOutButton = new JButton("�˳���¼");
			logOutButton.addActionListener(new logOutListener());
			
			quitButton = new JButton("�˳�ϵͳ");
			quitButton.addActionListener(new quitListener());
			
			JPanel panel1 = new JPanel();
			panel1.add(depositButton);
			panel1.add(withdrawButton);
			
			JPanel panel2 = new JPanel();
			panel2.add(loanButton);
			panel2.add(repayButton);
			
			JPanel panel3 = new JPanel();
			panel3.add(queryButton);
			panel3.add(pswdModifyButton);
			
			JPanel panel4 = new JPanel();
			panel4.add(logOutButton);
			panel4.add(quitButton);
			
			this.setLayout(new GridLayout(5,1));
			this.add(menu);
			this.add(panel1);
			this.add(panel2);
			this.add(panel3);
			this.add(panel4);
			
		}
		
		private class queryListener implements ActionListener{
			public void actionPerformed(ActionEvent event){
				
				sentMessage = "query";
				sendMessage();
				receiveMessage();
				rMessageBox = receivedMessage.split("@#");
				
				if (rMessageBox[1].equals("!!!")){
					
					String printMessage = "����ǰ�ʻ����Ϊ��\n\r"
							+ "�����˻��� ��" + rMessageBox[2] + "\n\r"
							+ "���궨���˻��� ��" + rMessageBox[3] + "\n\r"
							+ "һ�궨���˻��� ��" + rMessageBox[4] + "\n\r"
							+ "���궨���˻��� ��" + rMessageBox[5];
					
					JOptionPane.showMessageDialog(mainFrame, 
							printMessage, "��ѯ���", JOptionPane.INFORMATION_MESSAGE);
					
				}
				
			}
		}
		
		private class depositListener implements ActionListener{
			public void actionPerformed(ActionEvent event){
				DepositFrame depositFrame = new DepositFrame();
				mainFrame.setVisible(false);
				depositFrame.setVisible(true);
				mainFrame.dispose();
			}
		}
		
		private class withdrawListener implements ActionListener{
			public void actionPerformed(ActionEvent event){
				WithdrawFrame withdrawFrame = new WithdrawFrame();
				mainFrame.setVisible(false);
				withdrawFrame.setVisible(true);
				mainFrame.dispose();
			}
		}
		
		private class loanListener implements ActionListener{
			public void actionPerformed(ActionEvent event){
				LoanFrame loanFrame = new LoanFrame();
				mainFrame.setVisible(false);
				loanFrame.setVisible(true);
				mainFrame.dispose();
			}
		}
		
		private class repayListener implements ActionListener{
			public void actionPerformed(ActionEvent event){
				RepayFrame repayFrame = new RepayFrame();
				mainFrame.setVisible(false);
				repayFrame.setVisible(true);
				mainFrame.dispose();
			}
		}
		
		private class modifyListener implements ActionListener{
			public void actionPerformed(ActionEvent event){
				ModifyFrame modifyFrame = new ModifyFrame();
				mainFrame.setVisible(false);
				modifyFrame.setVisible(true);
				mainFrame.dispose();
			}
		}
		
		private class logOutListener implements ActionListener{
			public void actionPerformed(ActionEvent event){
				
				sentMessage = "logOut";
				sendMessage();	receiveMessage();
				if (receivedMessage.equals("logOut@#!!!")){
					JOptionPane.showMessageDialog(mainFrame, 
						"�˳���¼�ɹ�����ӭ�ٴ�ʹ�ã�", "�˳���¼", JOptionPane.OK_OPTION);
					LoginFrame loginFrame = new LoginFrame();
					mainFrame.setVisible(false);
					loginFrame.setVisible(true);
					mainFrame.dispose();
				}
				
			}
		}
		
		private class quitListener implements ActionListener{
			public void actionPerformed(ActionEvent event){
				JOptionPane.showMessageDialog(mainFrame, "лл����ʹ�ã���ӭ�´�������", 
						"�˳�����", JOptionPane.OK_OPTION);
				try {
					in.close();
					out.close();
					clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.exit(0);
			}
		}
		
	}
	
	public class DepositFrame extends JFrame{
		
		public DepositFrame(){
			DepositPanel panel = new DepositPanel(this);
			this.setTitle("���");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.add(panel);
			this.setSize(400, 300);
		}
		
	}
	
	public class DepositPanel extends JPanel implements ActionListener{
		
		JFrame depositFrame;
		JLabel notiArea;
		JTextField amountField;
		JComboBox<String> accountSwitch;
		String[] keysLabel = {"1","2","3","ɾ��", "4","5","6",
				"���","7","8","9","����",".", "0", "00","ȷ��"};
		String tempMessage;
		String[] rMessageBox = null;
		
		public DepositPanel(JFrame frame){
			
			this.depositFrame = frame;
			tempMessage = "";
			
			String[] v = {"�����˻�", "���궨���˻�", 
					"һ�궨���˻�", "���궨���˻�"};
			accountSwitch = new JComboBox<String>(v);
			accountSwitch.setEditable(false);
			accountSwitch.addItemListener(new SwitchListener());
			
			notiArea = new JLabel("��ѡ��Ҫ������˻��������Ȼ�󰴡�ȷ�ϡ���");
			notiArea.setLabelFor(accountSwitch);
			
			amountField = new JTextField("0");
			amountField.setColumns(20);
			amountField.setEditable(false);
			
			JButton[] keys = new JButton[keysLabel.length];
			for (int i=0; i<16; i++){
				keys[i] = new JButton(keysLabel[i]);
				keys[i].addActionListener(this);
			}
			
			JPanel keyboard = new JPanel();
			keyboard.setLayout(new GridLayout(4,4));
			for (int i=0; i<16; i++){
				keyboard.add(keys[i]);
			}
			
			this.add(notiArea);
			this.add(accountSwitch);
			this.add(amountField);
			this.add(keyboard);
			
		}
		
		public void actionPerformed(ActionEvent event){
			
			String label = event.getActionCommand();
			
			if ("0123456789".indexOf(label) >= 0){
				if (amountField.getText().equals("0")){
					amountField.setText(label);
				}
				else{
					amountField.setText(amountField.getText() +
							Integer.toString("0123456789".indexOf(label)));
				}
			}
			else if (label.equals("00")){
				if (!amountField.getText().equals("0")){
					amountField.setText(amountField.getText() + "00");
				}
			}
			else if (label.equals(".")){
				if (amountField.getText().indexOf(".") < 0){
					amountField.setText(amountField.getText() + ".");
				}
			}
			else if (label.equals("���"))
				amountField.setText("0");
			else if (label.equals("ɾ��")){
				if (amountField.getText().length() == 1){
					amountField.setText("0");
				}
				else{
					try {
						amountField.setText(amountField.getText(
								0, amountField.getText().length()-1));
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}	
			else if (label.equals("����")){
				MainFrame mainFrame = new MainFrame();
				depositFrame.setVisible(false);
				mainFrame.setVisible(true);
				depositFrame.dispose();
			}
			else if (label.equals("ȷ��")){
				
				sentMessage = "deposit@#" + amountField.getText() + "@#" + tempMessage;
				sendMessage();
			
				receiveMessage();
				rMessageBox = receivedMessage.split("@#");
			
				if (rMessageBox[1].equals("!!!")){
					JOptionPane.showMessageDialog(depositFrame, "���ɹ���\n����ǰ�˻����Ϊ" + rMessageBox[2] + "Ԫ", 
							"���ɹ�",JOptionPane.OK_OPTION);
					amountField.setText("0");
				}
				else if (rMessageBox[1].equals("???")){
					
					switch(rMessageBox[2]){
						case "failed":
							JOptionPane.showMessageDialog(depositFrame, 
								"���ʧ�ܣ������³��ԣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
							amountField.setText("0");
							break;
						case "SQLException":
							JOptionPane.showMessageDialog(depositFrame, 
									"���ݿ�����쳣��", "��ʾ", JOptionPane.ERROR_MESSAGE);
							System.exit(1);
						default:
							JOptionPane.showMessageDialog(depositFrame, 
									"δ����", "��ʾ", JOptionPane.ERROR_MESSAGE);
							System.exit(1);
					}
				
				}
			
				else{
					JOptionPane.showMessageDialog(depositFrame, 
							"δ֪����", "��ʾ", JOptionPane.ERROR_MESSAGE);
					System.exit(1);
				}
				
			}
				
		}
		
		private class SwitchListener implements ItemListener{
			public void itemStateChanged(ItemEvent event){
				
				if (event.getStateChange() == ItemEvent.SELECTED){
					
					tempMessage = "checkingAc";
					String itemLabel = accountSwitch.getSelectedItem().toString();
					
					if (itemLabel.equals("�����˻�"))
						tempMessage = "checkingAc";
					else if (itemLabel.equals("���궨���˻�"))
						tempMessage = "savingAc_05y";
					else if (itemLabel.equals("һ�궨���˻�"))
						tempMessage = "savingAc_1y";
					else if (itemLabel.equals("���궨���˻�"))
						tempMessage = "savingAc_5y";
					
				}
				
			}
		}
		
	}
		
	public class WithdrawFrame extends JFrame{
		public WithdrawFrame(){
			WithdrawPanel panel = new WithdrawPanel(this);
			this.setTitle("����");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.add(panel);
			this.setSize(400, 300);
		}
	}
	
	public class WithdrawPanel extends JPanel implements ActionListener{

		JFrame withdrawFrame;
		JLabel notiArea, balanceShowLabel;
		JTextField amountField;
		JComboBox<String> accountSwitch;
		String[] keysLabel = {"1","2","3","ɾ��", "4","5","6",
				"���","7","8","9","����",".", "0", "00","ȷ��"};
		String tempMessage;
		String[] rMessageBox = null;
		String tempBalance;
		String itemLabel;
		
		public WithdrawPanel(JFrame frame){
			
			this.withdrawFrame = frame;
			tempMessage = "";
			
			String[] v = {"�����˻�", "���궨���˻�", 
					"һ�궨���˻�", "���궨���˻�"};
			accountSwitch = new JComboBox<String>(v);
			accountSwitch.setEditable(false);
			accountSwitch.addItemListener(new SwitchListener());
			
			notiArea = new JLabel("��ѡ��Ҫ������˻��������Ȼ�󰴡�ȷ�ϡ���");
			notiArea.setLabelFor(accountSwitch);
			balanceShowLabel = new JLabel();
			
			amountField = new JTextField("0");
			amountField.setColumns(20);
			amountField.setEditable(false);
			
			JButton[] keys = new JButton[keysLabel.length];
			for (int i=0; i<16; i++){
				keys[i] = new JButton(keysLabel[i]);
				keys[i].addActionListener(this);
			}
			
			JPanel keyboard = new JPanel();
			keyboard.setLayout(new GridLayout(4,4));
			for (int i=0; i<16; i++){
				keyboard.add(keys[i]);
			}
			
			JPanel panel0 = new JPanel();
			panel0.add(accountSwitch);
			panel0.add(amountField);
			
			this.add(notiArea);
			this.add(panel0);
			this.add(balanceShowLabel);
			this.add(keyboard);

			sentMessage = "query";
			sendMessage(); receiveMessage();
			rMessageBox = receivedMessage.split("@#");
			
		}
		
		private void confirmWithdraw(){
			
			sentMessage = "withdraw@#" + amountField.getText() + "@#" + tempMessage;
			sendMessage();
		
			receiveMessage();
			rMessageBox = receivedMessage.split("@#");
		
			if (rMessageBox[1].equals("!!!")){
				JOptionPane.showMessageDialog(withdrawFrame, "ȡ��ɹ���\n���Ļ����˻����Ϊ" + rMessageBox[2] + "Ԫ", 
						"ȡ��ɹ�",JOptionPane.OK_OPTION);
			}
			else if (rMessageBox[1].equals("???")){
				
				switch(rMessageBox[2]){
					case "failed":
						JOptionPane.showMessageDialog(withdrawFrame, 
							"ȡ��ʧ�ܣ������³��ԣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
						amountField.setText("");
						break;
					case "SQLException":
						JOptionPane.showMessageDialog(withdrawFrame, 
								"���ݿ�����쳣��", "��ʾ", JOptionPane.ERROR_MESSAGE);
						System.exit(1);
					default:
						JOptionPane.showMessageDialog(withdrawFrame, 
								"δ����", "��ʾ", JOptionPane.ERROR_MESSAGE);
						System.exit(1);
				}
			
			}
		
			else{
				JOptionPane.showMessageDialog(withdrawFrame, 
						"δ֪����", "��ʾ", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}
		
		public void actionPerformed(ActionEvent event){
			
			String label = event.getActionCommand();
			
			if ("0123456789".indexOf(label) >= 0){
				if (amountField.getText().equals("0")){
					amountField.setText(label);
				}
				else{
					amountField.setText(amountField.getText() +
							Integer.toString("0123456789".indexOf(label)));
				}
			}
			else if (label.equals("00")){
				if (!amountField.getText().equals("0")){
					amountField.setText(amountField.getText() + "00");
				}
			}
			else if (label.equals(".")){
				if (amountField.getText().indexOf(".") < 0){
					amountField.setText(amountField.getText() + ".");
				}
			}
			else if (label.equals("���"))
				amountField.setText("0");
			else if (label.equals("ɾ��")){
				if (amountField.getText().length() == 1){
					amountField.setText("0");
				}
				else{
					try {
						amountField.setText(amountField.getText(
								0, amountField.getText().length()-1));
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}	
			else if (label.equals("����")){
				MainFrame mainFrame = new MainFrame();
				withdrawFrame.setVisible(false);
				mainFrame.setVisible(true);
				withdrawFrame.dispose();
			}
			else if (label.equals("ȷ��")){
				
				if (tempMessage.equals("checkingAc")){
					confirmWithdraw();
				}
				else{
					
					String warningMessage = "ȷ��Ҫ��" + itemLabel
							+ "��ȡ����\n"
							+ "ע�⣺�˲����Ὣ���˻������д��ת�������˻���\n"
							+ "�㽫ʧȥ���ж��ڴ����Ϣ��";
					
					int i = JOptionPane.showConfirmDialog(withdrawFrame, 
							warningMessage, "ע��", JOptionPane.YES_NO_OPTION);
					
					switch(i){
					case 0: confirmWithdraw(); break;
					case 1: break;
					default: JOptionPane.showMessageDialog(withdrawFrame, 
							"δ֪����", "����", JOptionPane.ERROR_MESSAGE);
							break;
					
					}
					
				}
				
			}
				
		}
		
		private class SwitchListener implements ItemListener{
			public void itemStateChanged(ItemEvent event){
				
				if (event.getStateChange() == ItemEvent.SELECTED){
					
					if (rMessageBox[0].equals("query") && rMessageBox[5] != null){
						
						tempMessage = "checkingAc";
						itemLabel = accountSwitch.getSelectedItem().toString();
						
						if (itemLabel.equals("�����˻�")){
							tempMessage = "checkingAc";
							tempBalance = rMessageBox[2];
							balanceShowLabel.setText("��ǰ���Ϊ�� ��" + tempBalance);
						}
						else if (itemLabel.equals("���궨���˻�")){
							tempMessage = "savingAc_05y";
							tempBalance = rMessageBox[3];
							balanceShowLabel.setText("��ǰ���Ϊ�� ��" + tempBalance);
						}
						else if (itemLabel.equals("һ�궨���˻�")){
							tempMessage = "savingAc_1y";
							tempBalance = rMessageBox[4];
							balanceShowLabel.setText("��ǰ���Ϊ�� ��" + tempBalance);
						}
						else if (itemLabel.equals("���궨���˻�")){
							tempMessage = "savingAc_5y";
							tempBalance = rMessageBox[5];
							balanceShowLabel.setText("��ǰ���Ϊ�� ��" + tempBalance);
						}
						
					}
					
				}
			}
		}
		
	}
	
	
	public class LoanFrame extends JFrame{
		public LoanFrame(){
			LoanPanel panel = new LoanPanel(this);
			this.setTitle("����");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.add(panel);
			this.setSize(400, 300);
		}
	}

	public class LoanPanel extends JPanel implements ActionListener{

		JFrame loanFrame;
		JLabel notiLabel;
		JTextField loanField;
		JButton confirmButton, backButton;
		String[] keysLabel = {"1","2","3","ɾ��", "4","5","6",
				"���","7","8","9","����",".", "0", "00","ȷ��"};
		String[] rMessageBox;
		
		public LoanPanel(JFrame frame){
			
			this.loanFrame = frame;
			notiLabel = new JLabel("��������Ҫ�Ĵ����");
			
			loanField = new JTextField("0");
			loanField.setColumns(20);
			
			JButton[] keys = new JButton[keysLabel.length];
			for (int i=0; i<16; i++){
				keys[i] = new JButton(keysLabel[i]);
				keys[i].addActionListener(this);
			}
			
			JPanel keyboard = new JPanel();
			keyboard.setLayout(new GridLayout(4,4));
			for (int i=0; i<16; i++){
				keyboard.add(keys[i]);
			}
			
			this.add(notiLabel);
			this.add(loanField);
			this.add(keyboard);
			
		}
		
		public void actionPerformed(ActionEvent event){
			
			String label = event.getActionCommand();
			
			if ("0123456789".indexOf(label) >= 0){
				if (loanField.getText().equals("0")){
					loanField.setText(label);
				}
				else{
					loanField.setText(loanField.getText() +
							Integer.toString("0123456789".indexOf(label)));
				}
			}
			else if (label.equals("00")){
				if (!loanField.getText().equals("0")){
					loanField.setText(loanField.getText() + "00");
				}
			}
			else if (label.equals(".")){
				if (loanField.getText().indexOf(".") < 0){
					loanField.setText(loanField.getText() + ".");
				}
			}
			else if (label.equals("���"))
				loanField.setText("0");
			else if (label.equals("ɾ��")){
				if (loanField.getText().length() == 1){
					loanField.setText("0");
				}
				else{
					try {
						loanField.setText(loanField.getText(
								0, loanField.getText().length()-1));
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}	
			else if (label.equals("����")){
				MainFrame mainFrame = new MainFrame();
				loanFrame.setVisible(false);
				mainFrame.setVisible(true);
				loanFrame.dispose();
			}
			else if (label.equals("ȷ��")){
				
				sentMessage = "deposit@#" + loanField.getText();
				sendMessage();
			
				receiveMessage();
				rMessageBox = receivedMessage.split("@#");
			
				if (rMessageBox[1].equals("!!!")){
					JOptionPane.showMessageDialog(loanField, "����ɹ���\n����ǰ�Ѵ�����Ϊ" 
							+ rMessageBox[2] + "Ԫ",	"���ɹ�",JOptionPane.OK_OPTION);
					loanField.setText("0");
				}
				else if (rMessageBox[1].equals("???")){
					
					switch(rMessageBox[2]){
						case "failed":
							JOptionPane.showMessageDialog(loanField, 
								"����ʧ�ܣ������³��ԣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
							loanField.setText("0");
							break;
						case "SQLException":
							JOptionPane.showMessageDialog(loanField, 
									"���ݿ�����쳣��", "��ʾ", JOptionPane.ERROR_MESSAGE);
							System.exit(1);
						default:
							JOptionPane.showMessageDialog(loanField, 
									"δ����", "��ʾ", JOptionPane.ERROR_MESSAGE);
							System.exit(1);
					}
				
				}
			
				else{
					JOptionPane.showMessageDialog(loanField, 
							"δ֪����", "��ʾ", JOptionPane.ERROR_MESSAGE);
					System.exit(1);
				}
				
			}
			
		}
		
	}
	
	public class RepayFrame extends JFrame{
		public RepayFrame(){
			RepayPanel panel = new RepayPanel(this);
			this.setTitle("����");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.add(panel);
			this.setSize(400, 300);
		}
	}
	
	public class RepayPanel extends JPanel{

		JFrame repayFrame;
		
		public RepayPanel(JFrame frame){
			
			this.repayFrame = frame;
			
		}
		
	}
	
	public class ModifyFrame extends JFrame{
		
		public ModifyFrame(){
			ModifyPanel panel = new ModifyPanel(this);
			this.setTitle("�޸�����");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.add(panel);
			this.setSize(400, 300);
		}
		
	}

	public class ModifyPanel extends JPanel{

		JFrame modifyFrame;
		JLabel oldPswdLabel, newPswdLabel, confirmPswdLabel;
		JPasswordField oldPswdField, newPswdField, confirmPswdField;
		JButton confirmButton, backButton;
		String[] rMessageBox = null;
		
		public ModifyPanel(JFrame frame){
			
			this.modifyFrame = frame;
			oldPswdLabel = new JLabel("������");
			newPswdLabel = new JLabel("������");
			confirmPswdLabel = new JLabel("ȷ������");
			
			oldPswdField = new JPasswordField(20);
			newPswdField = new JPasswordField(20);
			confirmPswdField = new JPasswordField(20);
			
			confirmButton = new JButton("ȷ��");
			confirmButton.setSize(50, 50);
			confirmButton.addActionListener(new confirmListener());
			backButton = new JButton("����");
			backButton.setSize(50, 50);
			backButton.addActionListener(new backListener());
			
			JPanel namePanel = new JPanel();
			namePanel.add(oldPswdLabel);
			namePanel.add(oldPswdField);
			
			JPanel pswdPanel = new JPanel();
			pswdPanel.add(newPswdLabel);
			pswdPanel.add(newPswdField);
			
			JPanel confirmPswdPanel = new JPanel();
			confirmPswdPanel.add(confirmPswdLabel);
			confirmPswdPanel.add(confirmPswdField);
			
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(confirmButton);
			buttonPanel.add(backButton);
			
			this.add(namePanel);
			this.add(pswdPanel);
			this.add(confirmPswdPanel);
			this.add(buttonPanel);
			
		}
		
		private class confirmListener implements ActionListener{
			public void actionPerformed(ActionEvent event)
			{
				while (!(new String(newPswdField.getPassword()).equals(
						new String(confirmPswdField.getPassword())))){
					JOptionPane.showMessageDialog(modifyFrame, "��������������������������", "��ʾ", JOptionPane.ERROR_MESSAGE);
					newPswdField.setText("");
					confirmPswdField.setText("");
				}
				
				sentMessage = "modify@#" + new String(oldPswdField.getPassword()) + "@#" 
							+ new String(newPswdField.getPassword());
				sendMessage();
				
				receiveMessage();
				rMessageBox = receivedMessage.split("@#");
				
				if (rMessageBox[1].equals("!!!")){
					JOptionPane.showMessageDialog(modifyFrame, "�޸ĳɹ���", "�ɹ�",JOptionPane.OK_OPTION);
					MainFrame mainFrame = new MainFrame();
					modifyFrame.setVisible(false);
					mainFrame.setVisible(true);
					modifyFrame.dispose();
				}
				else if (rMessageBox[1].equals("???")){
					
					switch(rMessageBox[2]){
						case "wrong":	
							JOptionPane.showMessageDialog(modifyFrame, 
									"ԭ����������������룡", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
							oldPswdField.setText("");
							newPswdField.setText("");
							confirmPswdField.setText("");
							break;
						case "failed":
							JOptionPane.showMessageDialog(modifyFrame, 
									"ע��ʧ�ܣ������³��ԣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
							oldPswdField.setText("");
							newPswdField.setText("");
							confirmPswdField.setText("");
							break;
						case "SQLException":
							JOptionPane.showMessageDialog(modifyFrame, 
									"���ݿ�����쳣��", "��ʾ", JOptionPane.ERROR_MESSAGE);
							System.exit(1);
						default:
							JOptionPane.showMessageDialog(modifyFrame, 
									"δ֪����", "��ʾ", JOptionPane.ERROR_MESSAGE);
							System.exit(1);
					}
					
				}
				
				else{
					JOptionPane.showMessageDialog(modifyFrame, 
							"δ֪����", "��ʾ", JOptionPane.ERROR_MESSAGE);
					System.exit(1);
				}
				
			}
		}
		
		private class backListener implements ActionListener{
			public void actionPerformed(ActionEvent event)
			{
				MainFrame mainFrame = new MainFrame();
				modifyFrame.setVisible(false);
				mainFrame.setVisible(true);
				modifyFrame.dispose();
			}
		}
		
	}
	
	
	
	public static void main(String[] args){
		SystemClient client = new SystemClient();
		client.start();
		LoginFrame frame = client.new LoginFrame();
		frame.setVisible(true);
	}
	
}

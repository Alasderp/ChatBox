package ChatApp;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import ChatApp.ClientGUI;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextPane;
import java.awt.ScrollPane;
import javax.swing.JScrollBar;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class ClientGUI extends JFrame {

	private JPanel contentPane;
	private JTextField msgInput;
	private JTextArea chatWindow;
	private BufferedReader in;
	private PrintWriter out;
	private static int PORT = 8181;
	private static String address = "127.0.0.1";
	private Socket socket;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		ClientGUI clientGUI = new ClientGUI(address);
		clientGUI.setVisible(true);
		clientGUI.play();
	
	}

	/**
	 * Create the frame.
	 */
	public ClientGUI(String serverAddress) {
		
		//Create a connection to the server which will then create a ClientThread class on acceptance
		try {
			socket = new Socket(serverAddress, PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));		
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {

		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane((Component) null);
		
		msgInput = new JTextField();
		msgInput.setColumns(10);
		
		JButton submitBtn = new JButton("Send");
		submitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = msgInput.getText();
				msgInput.setText("");
				if(!message.equals("")) {
					out.println(message);
				}
			}
		});
			
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(184)
							.addComponent(submitBtn))
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
						.addComponent(msgInput, Alignment.TRAILING))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
					.addComponent(msgInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(submitBtn)
					.addGap(5))
		);
		
		JTextArea chatBox = new JTextArea();
		chatWindow = chatBox;
		scrollPane.setViewportView(chatBox);
		contentPane.setLayout(gl_contentPane);
		
	}
	
	public void play() {
		
		while(true) {
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			try {
				if (in.ready()) {
					
					String msg = in.readLine();
					chatWindow.append(msg);
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}
		
	}
	
}

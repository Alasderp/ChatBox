package rocketChat;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import reddit.RedditService;
import reddit.response.PostData;
import reddit.response.RedditPost;
import rocketChat.payloads.RocketChatMessage;
import rocketChat.payloads.request.Attachment;
import rocketChat.payloads.request.AttachmentField;
import rocketChat.payloads.request.Params;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

import static util.Util.toJson;

public class RocketClientGUI extends JFrame {

	private RocketChatClient rocketChatClient;

	private JPanel contentPane;
	private JTextField msgInput;
	private JTextArea chatWindow;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		RocketClientGUI clientGUI = new RocketClientGUI();
		clientGUI.setVisible(true);
		clientGUI.play();

	}

	/**
	 * Create the frame.
	 */
	public RocketClientGUI() {

		//Create a connection to the server which will then create a ClientThread class on acceptance
		try {
			rocketChatClient = new RocketChatClient();
			rocketChatClient.connect();
		} catch (Exception e) {
			e.printStackTrace();
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
				try {
					String message = msgInput.getText();
					msgInput.setText("");
					if(!message.equals("")) {

						RocketChatMessage rocketMessage = new RocketChatMessage();
						rocketMessage.setMsg("method");
						rocketMessage.setMethod("sendMessage");
						rocketMessage.setId("Alasdair");

						Params param = new Params();

						//PARAM ID MUST BE UNIQUE WHEN SENDING MESSAGES
						SecureRandom random = new SecureRandom();
						byte bytes[] = new byte[20];
						random.nextBytes(bytes);
						Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
						String token = encoder.encodeToString(bytes);

						param.set_id(token);
						param.setRid("y7PbFA8iyaiGZXXDo");
						param.setMsg(message);

						ArrayList<Params> params = new ArrayList<>();
						params.add(param);

						rocketMessage.setParams(params);

						WebSocketSession session = rocketChatClient.getSocketSession();

						session.sendMessage(new TextMessage(toJson(rocketMessage)));
					}
				}
				catch(Exception error){
					error.printStackTrace();
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

				//If rocket client has unread messages
				ArrayList<String> messageList = rocketChatClient.readMessages();
				if (!messageList.isEmpty()) {

					for(int x = 0; x < messageList.size(); x++) {

						String msg = messageList.get(x);

						chatWindow.append(msg + "\n");

						System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
						VoiceManager vm = VoiceManager.getInstance();

						Voice voice = vm.getVoice("kevin");
						voice.allocate();

						//Slow Low Pitch
						//voice.setPitch(5);
						//voice.setDurationStretch(1.1f);

						//Normal speed high pitch with some pitch variation
						voice.setPitch(200);
						voice.setPitchRange(100);

						voice.speak(msg);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
		
	}

}

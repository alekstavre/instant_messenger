package client_server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Server extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// declaring vars
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private ServerSocket server;
	private Socket connection;

	// server's constructor - setting the GUI
	public Server() {
		super("instant messenger");
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage(e.getActionCommand());
				userText.setText("");
			}

		});

		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(450, 200);
		setVisible(true);
	}

	// set the server to run
	public void runServer() {
		try {
			server = new ServerSocket(5555, 100);
			while (true) {
				try {
					connecting();
					loadStreams();
					chatting();
				} catch (Exception e) {
					showMsg("connection ended");
				} finally {
					endConn();
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void chatting() {
		String message = "you are connected";
		sendMessage(message);
		enableType(true);

		do {
			try {
				message = (String) input.readObject();
				showMsg("\n " + message);
			} catch (ClassNotFoundException | IOException e) {
				showMsg("\nUnable to send message");
			}

		} while (!message.equals("CLIENT - END"));
	}

	private void loadStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMsg("streams connected!");

	}

	private void connecting() throws IOException {
		showMsg("trying to connect...");
		connection = server.accept();
		showMsg("connected to: " + connection.getInetAddress().getHostName());
	}

	private void sendMessage(String message) {
		try {
			output.writeObject("server - " + message);
			output.flush();
			showMsg("server - " + message);
		} catch (IOException e) {
			chatWindow.append("cannot send your message");

		}
	}

	private void showMsg(final String text) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				chatWindow.append(text);

			}
		});
	}

	private void enableType(boolean tOrf) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				userText.setEditable(tOrf);

			}
		});

	}

	private void endConn() {
		showMsg("closing connection!");
		enableType(false);
		try {
			input.close();
			output.close();
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

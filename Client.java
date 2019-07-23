package client_server;

import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client extends JFrame {
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;

	// constructor
	public Client(String host) {
		super("Client System");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				sendMsg(event.getActionCommand());
				userText.setText("");
			}
		});
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(450, 200);
		setVisible(true);
	}

	// setting up the server
	public void startRunning() {
		try {
			connecting();
			loadStreams();
			chatting();
		} catch (EOFException eofException) {
			showMessage("\n Client disconnected");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			endConn();
		}
	}

	// connect to server
	private void connecting() throws IOException {
		showMessage("\n trying to connect... \n ");
		connection = new Socket(InetAddress.getByName(serverIP), 5555);
		showMessage("\n Connected to " + connection.getInetAddress().getHostName());
	}

	// setting up the streams
	private void loadStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n streams are set! \n");
	}

	// chatting with server
	private void chatting() throws IOException {
		enableType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);

			} catch (ClassNotFoundException classNotFoundException) {
				showMessage("\n message failed \n");
			}

		} while (!message.equals("SERVER - END"));
	}

	// close the streams and sockets
	private void endConn() {
		showMessage("\n Closing the chat \n");
		enableType(false);
		try {
			input.close();
			output.close();
			connection.close();
		} catch (IOException ioExceptiom) {
			ioExceptiom.printStackTrace();
		}
	}

	// sending messages
	private void sendMsg(String message) {
		try {
			output.writeObject("CLIENT - " + message);
			output.flush();
			showMessage("\n CLIENT - " + message);
		} catch (IOException ioException) {
			chatWindow.append("\n Something went wrong\n");
		}
	}

	// change or update the chatwindow area
	private void showMessage(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				chatWindow.append(message);
			}
		});
	}

	// gives user permission to type
	private void enableType(boolean tof) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				userText.setEditable(tof);
			}
		});
	}

}

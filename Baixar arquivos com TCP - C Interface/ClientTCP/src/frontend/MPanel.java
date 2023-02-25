package frontend;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import backend.TCPClient;

import javax.swing.JSeparator;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JProgressBar;

public class MPanel extends JPanel{
	private JTextField archField;
	private TCPClient socket;
	private JLabel msgServerLabel_1;
	private JButton sendButton;
	private JTextField localField;
	private JFileChooser chooserLocal;
	/**
	 * Create the panel.
	 */
	public MPanel() {
		setLayout(null);
		chooserLocal = new JFileChooser();
		chooserLocal.setCurrentDirectory(new File(System.getProperty("user.dir") + "\\arquivos"));
		chooserLocal.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		JLabel tamanholabel = new JLabel("recebido/total", SwingConstants.CENTER);
		tamanholabel.setBounds(90, 155, 200, 14);
		add(tamanholabel);
		
		JLabel nomeArqLabel = new JLabel("Nome do arquivo:");
		nomeArqLabel.setBounds(10, 106, 100, 15);
		add(nomeArqLabel);
		
		JButton connectButton = new JButton("Connect!"); 
		connectButton.setBounds(143, 28, 89, 23);
		add(connectButton);
		
		archField = new JTextField();
		nomeArqLabel.setLabelFor(archField);
		archField.setEnabled(false);
		archField.setBounds(10, 124, 280, 20);
		add(archField);
		archField.setColumns(10);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 202, 360, 2);
		add(separator);
		
		JLabel fromServerLabel = new JLabel("Server:");
		fromServerLabel.setBounds(10, 208, 43, 14);
		add(fromServerLabel);
		
		msgServerLabel_1 = new JLabel("Waiting connection..");
		msgServerLabel_1.setBounds(55, 208, 315, 14);
		add(msgServerLabel_1);
		
		sendButton = new JButton("Ok");
		sendButton.setEnabled(false);
		sendButton.setMargin(new Insets(0, 0, 0, 0));
		sendButton.setFont(new Font("Tahoma", Font.PLAIN, 9));
		sendButton.setBounds(295, 123, 45, 23);
		add(sendButton);
		
		JLabel localLabel = new JLabel("Local para salvar:");
		localLabel.setBounds(10, 58, 118, 15);
		add(localLabel);
		
		localField = new JTextField(chooserLocal.getCurrentDirectory().toString());
		localLabel.setLabelFor(localField);
		localField.setEnabled(false);
		localField.setBounds(10, 75, 280, 20);
		add(localField);
		localField.setColumns(10);
		
		JButton localButton = new JButton("Mudar");
		localButton.setMargin(new Insets(0, 0, 0, 0));
		localButton.setFont(new Font("Tahoma", Font.PLAIN, 9));
		localButton.setBounds(295, 74, 45, 23);
		add(localButton);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setMaximum(150);
		progressBar.setValue(0);
		progressBar.setBounds(10, 168, 360, 23);
		add(progressBar);
		
		connectButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					socket = new TCPClient("localhost", 6789);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Servidor desconectado", "Error", JOptionPane.ERROR_MESSAGE);
				}
				if(socket != null && socket.isConnected()) {
					archField.setEnabled(true);
					sendButton.setEnabled(true);
					msgServerLabel_1.setText("Connected");
				}
			}
		});
		
		sendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				socket.askFile(archField.getText(), localField.getText(), msgServerLabel_1, progressBar, tamanholabel);
			}
		});
		
		localButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ChangeLocal();
			}
		});
	}
	
	private void ChangeLocal() {
		chooserLocal.showOpenDialog(this);
		localField.setText(chooserLocal.getSelectedFile().getAbsolutePath());
	}
}

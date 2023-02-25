package backend;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class TCPClient extends Thread{ 
	private DataOutputStream outToServer;
	private BufferedReader inFromServer;
	private Socket clientSocket;
	private progressBar barra;
	private int progress = 0;
	private int maxProgre = 0;
			
	private FileOutputStream arquivoLocal;
	private InputStream arquivoSocket;
	private JLabel serverLabel;
	
	public TCPClient(String ip, int port) throws Exception { 
		clientSocket = new Socket("localhost", 6789);
		
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}
	public void askFile(String arqName, String local, JLabel fromServer, JProgressBar progressBar, JLabel progressLabel) {
		this.serverLabel = fromServer;
		
		//faz a requisição pro servidor
		try {
			outToServer.writeBytes(arqName + '\n');
			
			// Criando canal de transferencia
			arquivoSocket = clientSocket.getInputStream();
			
			
			maxProgre = Integer.parseInt(inFromServer.readLine());
			//System.out.println("progress: " + maxProgre);
			
			// Criando arquivo que sera recebido pelo servidor
			arquivoLocal = new FileOutputStream(local + "\\" + arqName);
						
			progressBar.setMaximum(maxProgre);
			//thread para a barra de progress
			barra = new progressBar(progressBar, progressLabel);
			
			this.start();
		}catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Arquivo nao encontrado ou nao existe", "Deu zebra", JOptionPane.ERROR_MESSAGE);
			try {
				clientSocket.close();
				outToServer.close();
				arquivoSocket.close();
				serverLabel.setText("Desconectado");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}catch (SocketException e) {
			JOptionPane.showMessageDialog(null, "Primeiro conecte-se a um servidor para baixar arquivos", "Deu zebra", JOptionPane.INFORMATION_MESSAGE);
			serverLabel.setText("Desconectado");
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void run() {
		barra.start();

		// Lendo o arquivo recebido pelo socket (arquivoSocket) e gravando no
		// arquivo local (arquivoLocal)
		byte[] cbuffer = new byte[1024];
		int bytesRead;
		
		serverLabel.setText("Recebendo arquivo...");
		
		try {
			while ((bytesRead = arquivoSocket.read(cbuffer)) != -1) {
				arquivoLocal.write(cbuffer, 0, bytesRead);
				progress = bytesRead + progress;
				barra.increment(progress);
				System.out.println("recebiamn");
			}
			serverLabel.setText("Arquivo recebido!");
			JOptionPane.showMessageDialog(null, "Arquivo recebido com sucesso", "Informativo", JOptionPane.INFORMATION_MESSAGE);
		
			arquivoLocal.close();
			arquivoSocket.close();
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	
	public boolean isConnected() {
		return clientSocket.isConnected();
	}
}
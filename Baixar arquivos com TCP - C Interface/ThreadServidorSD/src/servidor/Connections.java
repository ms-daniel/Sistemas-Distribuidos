package servidor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Connections extends Thread{
	private Socket connection;
	//
	private BufferedReader inFromClient;
	//
	private DataOutputStream outToClient; 
	
	
	public Connections(Socket connection) {
		this.connection = connection;
		try {
			this.connection.setSoTimeout(60000);
			inFromClient = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
			outToClient  = new DataOutputStream(this.connection.getOutputStream());
			
		}catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	public void run() {
		String archiveName = "";
		int capitalizedSentence;

		try {
			System.out.println("Esperando cliente");
			archiveName = inFromClient.readLine();
			System.out.println("arquivo a ser enviado: " + archiveName);
		}catch (IOException e2) {
			System.out.println("Problema na leitura vinda do cliente");
		}
		
		//tentara achar o arquivo com o nome enviado pelo cliente
		try {
			FileInputStream fileIn = new FileInputStream("arquivos\\" + archiveName);	//abrir arquivo//verificar se existe
			OutputStream socketOut = this.connection.getOutputStream();					//envio do arquivo
			
			byte[] cbuffer = new byte[1024];	//tamanho da janela de envio
	        int bytesRead;
	        
	        outToClient.writeBytes(Integer.toString(fileIn.available()) + '\n');
	        
	       
        	while ((bytesRead = fileIn.read(cbuffer)) != -1) {
	        	socketOut.flush();
	            socketOut.write(cbuffer, 0, bytesRead);
	            
	        }
	        System.out.println("arquivo enviado");
	        //outToClient.writeBytes("Arquivo Enviado!");
	        fileIn.close();
	        socketOut.close();
	        connection.close();

		} catch (FileNotFoundException e) {
			capitalizedSentence = -1;
			try {
				outToClient.write(capitalizedSentence);
				System.out.println("entrou");
				connection.close();
			} catch (IOException e1) {
				System.out.println("Problema na msg pro cliente");
			}
			System.out.println("Arquivo " + archiveName + " nao encontrado");
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//fecha conexao
		try {
			this.connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

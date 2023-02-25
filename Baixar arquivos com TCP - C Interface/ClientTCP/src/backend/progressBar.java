package backend;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class progressBar extends Thread{
	private JProgressBar barra;
	private JLabel progL;
	
	public progressBar(JProgressBar bar, JLabel label) {
		this.barra = bar;
		this.progL = label;
	}
	
	@Override
	public void run() {
		progL.setText(barra.getValue() + "/" + barra.getMaximum());
		while(barra.getValue() < barra.getMaximum()) {
			
		}
	}
	
	public void increment(int value) {
		barra.setValue(value);
		progL.setText(barra.getValue() + "/" + barra.getMaximum());
	}

}

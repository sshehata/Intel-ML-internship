package gui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

public class LoadingFrame extends JDialog {

	public LoadingFrame(JFrame frame) {
		super(frame);
		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[3]
					.getClassName());
		} catch (Exception e) {
			System.out.println(e);
		}
		setSize(300, 20);
		setUndecorated(true);
		setLocationRelativeTo(frame);
		setModalityType(ModalityType.MODELESS);
		JProgressBar bar = new JProgressBar();
		bar.setIndeterminate(true);
		this.add(bar);
		setVisible(true);
	}

	public static void main(String... args) {
		System.out.println(1);
		new LoadingFrame(null);
		System.out.println(2);
	}
}

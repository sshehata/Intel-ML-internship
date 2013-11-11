package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class LogPanel extends JPanel {
	private JTextArea log;

	public LogPanel() {
		setPreferredSize(new Dimension(500, 200));
		setLayout(new BorderLayout());
		setBorder(new EtchedBorder());

		log = new JTextArea();
		log.setEditable(false);
		log.setLineWrap(true);
		log.setBackground(UIManager.getDefaults().getColor("JPanel.background"));
		JScrollPane sp = new JScrollPane(log);
		add(sp);
	}

	public void appendLine(String line) {
		log.append(line);
	}
}

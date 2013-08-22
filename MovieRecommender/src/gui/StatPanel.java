package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class StatPanel extends JPanel {
	private int pos;
	private int neg;
	private int total;

	public StatPanel() {
		setPreferredSize(new Dimension(300, 800));
		setLayout(new BorderLayout());
		setBorder(new EtchedBorder());

		pos = 0;
		neg = 0;
		total = 0;
	}

	public void updateState(boolean pos) {
		total++;
		if (pos)
			this.pos++;
		else
			neg++;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}

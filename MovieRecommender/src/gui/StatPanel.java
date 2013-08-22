package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

@SuppressWarnings("serial")
public class StatPanel extends JPanel {
	private int pos;
	private int neg;
	private int total;

	public StatPanel() {
		setPreferredSize(new Dimension(300, 800));
		setLayout(null);
		setBorder(new EtchedBorder());

		pos = 0;
		neg = 0;
		total = 0;
		createChart();
	}

	public void updateState(boolean pos) {
		total++;
		if (pos)
			this.pos++;
		else
			neg++;
		createChart();
		repaint();
	}

	private void createChart() {
		DefaultPieDataset dataSet = new DefaultPieDataset();
		dataSet.setValue("positive", (double) pos / total);
		dataSet.setValue("negative", (double) neg / total);
		this.removeAll();
		JFreeChart chart = ChartFactory.createPieChart("Reviews", dataSet,
				true, true, false);
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionPaint("positive", new Color(34, 139, 34));
		plot.setSectionPaint("negative", Color.RED);
		ChartPanel cp = new ChartPanel(chart);
		cp.setSize(new Dimension(300,300));
		cp.setLocation(0, 0);
		add(cp);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setFont(new Font("Times New Roman", Font.BOLD, 12));
		g.drawString("Total Reviews", 125, 430);
		g.drawString("" + total, 160, 450);
		g.drawString("This movie got..", 20, 380);
		g.setColor(new Color(34, 139, 34));
		g.drawString("" + pos, 60, 420);
		g.setColor(Color.RED);
		g.drawString("" + neg, 60, 470);
		g.drawImage(new ImageIcon("resources/images/thumbsup.jpg").getImage(), 20, 400, 30, 30,
				null);
		g.drawImage(new ImageIcon("resources/images/thumbsdown.jpg").getImage(), 20, 450, 30,
				30, null);
		g.setColor((pos >= neg) ? new Color(34, 139, 34) : Color.RED);
		String recommendation = (pos >= neg) ? "We think you should watch this movie."
				: "You better give this movie a pass!";
		g.drawString(recommendation, 20, 520);
	}
}

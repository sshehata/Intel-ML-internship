package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.pdfbox.encryption.DocumentEncryption;
import org.apache.poi.hslf.dev.TextStyleListing;

public class ReviewPanel extends JPanel {

	private StyledDocument reviewDoc;
	private JTextPane reviewPane;
	private SimpleAttributeSet posReview;
	private SimpleAttributeSet negReview;

	public ReviewPanel() {
		setPreferredSize(new Dimension(500, 600));
		setLayout(new BorderLayout());
		setBorder(new EtchedBorder());

		reviewPane = new JTextPane();
		reviewPane.setEditable(false);
		reviewPane.setBackground(UIManager.getDefaults().getColor(
				"JPanel.background"));
		reviewDoc = reviewPane.getStyledDocument();
		JScrollPane sp = new JScrollPane(reviewPane);
		add(sp);

		posReview = new SimpleAttributeSet();
		StyleConstants.setForeground(posReview, new Color(34, 139, 34));
		negReview = new SimpleAttributeSet();
		StyleConstants.setForeground(negReview, Color.RED);
	}

	public void appendReview(File review, boolean pos) {
		try {
			SimpleAttributeSet currentSet = (pos) ? posReview : negReview;
			FileReader inStream = new FileReader(review);
			BufferedReader reader = new BufferedReader(inStream);
			String line = reader.readLine();
			while (line != null) {
				reviewDoc.insertString(reviewDoc.getLength(), line + "\n",
						currentSet);
				reviewPane.setCaretPosition(reviewDoc.getLength());
				line = reader.readLine();
			}
			reviewDoc.insertString(reviewDoc.getLength(), "\n#################END OF REVIEW##############\n\n",
					currentSet);
			reviewPane.setCaretPosition(reviewDoc.getLength());
			reader.close();
		} catch (BadLocationException | IOException e) {
			System.out.println(e);
		}

	}

	public void clear() {
		
	}
}

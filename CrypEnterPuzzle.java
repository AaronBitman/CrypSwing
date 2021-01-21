package crypSwing;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Frame;
import java.awt.Point;

/**
 * This class is a dialog window to get the puzzle text and an option.
 * It was copied from the following source (and modified):
 * http://www2.hawaii.edu/~takebaya/ics111/jdialog/jdialog.html
 * @author Bitman
 * @version 2.0 01/20/21
 */

public class CrypEnterPuzzle extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private String[] data; // to be returned to the frame
	private JTextArea puzzle;
	private JScrollPane scrollPane;
	private JCheckBox likeExclusion;
	private JButton btnOk;
	private JButton btnCancel;

	/**
	 * This constructor builds the response window.
	 * @param parent The frame to which to return the data
	 */
	public CrypEnterPuzzle(Frame parent) {
		super(parent,"Enter the puzzle.",true);
		Point loc = parent.getLocation();
		setLocation(loc.x+100,loc.y+100);
		data = new String[2];
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(2,2,2,2);
		constraints.fill = GridBagConstraints.HORIZONTAL;

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 4;
		puzzle = new JTextArea(7, 50);
		scrollPane = new JScrollPane(puzzle);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		puzzle.setLineWrap(true);
		puzzle.setWrapStyleWord(true);
		panel.add(scrollPane, constraints);
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		likeExclusion = new JCheckBox("Like-Exclusion", false);
		panel.add(likeExclusion, constraints);

		constraints.gridx = 1;
		btnOk = new JButton("OK");
		panel.add(btnOk, constraints);
		btnOk.addActionListener(this);
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		constraints.gridx = 2;
		panel.add(btnCancel,constraints);
		getContentPane().add(panel);
		pack();
   }
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		if (source == btnOk) {
			data[0] = puzzle.getText();
			data[1] = likeExclusion.isSelected() ? "Y" : "N";
		}
		else data[0] = null;
		dispose();
	}

   public String[] run() {
      this.setVisible(true);
      return data;
   }

}

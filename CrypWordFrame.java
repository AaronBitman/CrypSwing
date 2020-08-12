package crypSwing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.*;

/**
 * This class contains the code for the word-solving frame.
 * @author Bitman
 * @version 1.0 07/21/19
 */
public class CrypWordFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	final int MAXWORDLENGTH = 32;
	final int THIRDWORDLENGTH = MAXWORDLENGTH / 3;
	CrypWordKeyEvent CrypWordKeyActions = new CrypWordKeyEvent(this);
	CrypWordBtnEvent CrypWordBtnActions = new CrypWordBtnEvent(this);

	// Declare all the visual objects here.
	JLabel plaintextLabel = new JLabel("Plaintext: ", JLabel.RIGHT);
	JTextField[] plaintext = new JTextField[MAXWORDLENGTH];
	JLabel ciphertextLabel = new JLabel("Ciphertext: ", JLabel.RIGHT);
	JTextField[] ciphertext = new JTextField[MAXWORDLENGTH];
	JLabel actualCiphertextWordLabel = new JLabel("Actual Ciphertext Word: ", JLabel.RIGHT);
	JLabel actualCiphertextWord = new JLabel("", JLabel.LEFT);
	JCheckBox likeExclusion = new JCheckBox("Like-Exclusion", false);
	JLabel resultsLabel = new JLabel("Results: ", JLabel.RIGHT);
	JButton solve = new JButton("Solve");
	JButton clear = new JButton("Clear");
	JList<String> results = new JList<>();
    JScrollPane scroll = new JScrollPane(results,
    		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
    		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    /**
     * The constructor sets up the frame.
     */
	public CrypWordFrame() {
		
		int index;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Cryp - Single Word Solver");
		getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1; constraints.weighty = 1;
		constraints.ipadx = 3;
		
		// Plaintext (first) row
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(plaintextLabel, constraints);
		for (constraints.gridx = 1; constraints.gridx <= MAXWORDLENGTH; constraints.gridx++) {
			index = constraints.gridx-1;		
			plaintext[index] = new JTextField("", 1);
			add(plaintext[index], constraints);
			plaintext[index].setName("plaintext" + index);
			plaintext[index].addKeyListener(CrypWordKeyActions);
		}
		// Ciphertext (second) row
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(ciphertextLabel, constraints);
		for (constraints.gridx = 1; constraints.gridx <= MAXWORDLENGTH; constraints.gridx++) {
			index = constraints.gridx-1;
        	ciphertext[index] = new JTextField("", 1);
			add(ciphertext[index], constraints);
			ciphertext[index].setName("ciphertext" + index);
			ciphertext[index].addKeyListener(CrypWordKeyActions);
		}
		
		// Ciphertext Diplay (third) row
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(actualCiphertextWordLabel, constraints);
		constraints.gridx = 1;
		constraints.gridwidth = MAXWORDLENGTH;
		add(actualCiphertextWord, constraints);
		
		// Like-Exclusion, Solve, and Clear row
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = THIRDWORDLENGTH;
		add(likeExclusion, constraints);
		constraints.gridx += THIRDWORDLENGTH;
		add(solve, constraints);
		JRootPane rootPane = SwingUtilities.getRootPane(solve);
		rootPane.setDefaultButton(solve);
		solve.setActionCommand("SOLVE");
		solve.addActionListener(CrypWordBtnActions);
		constraints.gridx += THIRDWORDLENGTH;
		add(clear, constraints);
		clear.setActionCommand("CLEAR");
		clear.addActionListener(CrypWordBtnActions);
		
		// results (fifth) row
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		add(resultsLabel, constraints);
		constraints.gridx = 1;
		constraints.gridwidth = MAXWORDLENGTH;
		results.setFixedCellHeight(16);
		add(scroll, constraints);
		
		// Set up the frame.
		pack();
		setVisible(true);
		ciphertext[0].requestFocus();
	}
//	public static void main(String[] args) {
//		CrypWordFrame frame = new CrypWordFrame();
//	}
}
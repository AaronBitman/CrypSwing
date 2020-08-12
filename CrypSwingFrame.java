package crypSwing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.*;

/**
 * This class has the frame for the puzzle GUI.
 * @author Bitman
 * @version 1.0 07/21/19
 */
public class CrypSwingFrame extends JFrame {

	// Declare all constants here.
	private static final long serialVersionUID = 1L;
	final int LINELENGTH = 50;
	final int NUMBEROFLINES = 7;
	final int NUMBEROFHALFLINES = NUMBEROFLINES * 2;
	final int BUTTONWIDTH = 10;

	// Declare all visual objects here.
	JTextField[][] plaintext = new JTextField[LINELENGTH][NUMBEROFLINES];
	JLabel[][] ciphertext = new JLabel[LINELENGTH][NUMBEROFLINES];
	JLabel freqByPop = new JLabel(" ");
	JLabel freqAlpha = new JLabel(" ");
	JButton populatePuzzle = new JButton("Populate Puzzle");
	JButton solveWord = new JButton("Solve Word");
	JButton clear = new JButton ("Clear");
	
	// Declare all Event objects here.
	CrypSwingBtnEvent CrypPuzzleGridBagBtnActions;
	CrypSwingKeyEvent CrypPuzzleGridBagKeyActions;

	/**
	 * The constructor sets up all the controls and other important frame stuff.
	 */
	public CrypSwingFrame() {
		
		int doubleRowIndex = 0;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Cryp - Puzzle Solver");
		getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		CrypPuzzleGridBagKeyActions = new CrypSwingKeyEvent(this);

		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1; constraints.weighty = 1;
		constraints.ipadx = 3;

		for (constraints.gridy = 0; constraints.gridy < NUMBEROFHALFLINES; constraints.gridy++) {
			if (constraints.gridy%2==0) {
				// The row is a plaintext row.
				for (constraints.gridx = 0; constraints.gridx < LINELENGTH; constraints.gridx++) {
					plaintext[constraints.gridx][doubleRowIndex] = new JTextField("", 1);
					plaintext[constraints.gridx][doubleRowIndex].setHorizontalAlignment(JTextField.CENTER);
					add(plaintext[constraints.gridx][doubleRowIndex], constraints);
					plaintext[constraints.gridx][doubleRowIndex].setName("plaintext" + 
							String.format("%02d", constraints.gridx) + 
							String.format("%02d", doubleRowIndex));
					plaintext[constraints.gridx][doubleRowIndex].addKeyListener(CrypPuzzleGridBagKeyActions);
				}
			}
			else {
				// The row is a ciphertext row.
				for (constraints.gridx = 0; constraints.gridx < LINELENGTH; constraints.gridx++) {
					ciphertext[constraints.gridx][doubleRowIndex] = new JLabel(" ", JLabel.CENTER);
					add(ciphertext[constraints.gridx][doubleRowIndex], constraints);
				}		
				doubleRowIndex++;
			}
		}

		// Add places for frequency reports.
		constraints.gridx = 0;
		constraints.gridy++;
		constraints.gridwidth = LINELENGTH;
		add(freqByPop, constraints);
		constraints.gridy++;
		add(freqAlpha, constraints);

		//Add a row of buttons.
		CrypPuzzleGridBagBtnActions = new CrypSwingBtnEvent(this);
		constraints.gridx = 0;
		constraints.gridy++;
		constraints.gridwidth = BUTTONWIDTH;
		add(populatePuzzle, constraints);
		populatePuzzle.setActionCommand("POPULATE");
		populatePuzzle.addActionListener(CrypPuzzleGridBagBtnActions);
		constraints.gridx += BUTTONWIDTH;
		add(solveWord, constraints);
		solveWord.setActionCommand("SOLVEWORD");
		solveWord.addActionListener(CrypPuzzleGridBagBtnActions);
		constraints.gridx += BUTTONWIDTH;
		add(clear, constraints);
		clear.setActionCommand("CLEAR");
		clear.addActionListener(CrypPuzzleGridBagBtnActions);

		// Set up the frame.
		pack();
		setVisible(true);
	}
	/**
	 * The main method just opens the frame.
	 * @param args not used
	 */
	public static void main(String[] args) {
		CrypSwingFrame frame = new CrypSwingFrame();
	}
}

package crypSwing;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * This class has the frame for the puzzle GUI.
 * @author Bitman
 * @version 1.0 07/21/19
 * @version 2.0 01/20/21
 * @version 2.1 01/02/22
 * @version 3.0 11/30/22
 */
public class CrypSwingFrame extends JFrame {

	// Declare all constants here.
	private static final long serialVersionUID = 1L;
	static final int LINE_LENGTH = 50;
	static final int NUMBER_OF_LINES = 7;
	static final int NUMBER_OF_HALF_LINES = NUMBER_OF_LINES * 2;
	static final int BUTTON_WIDTH = 10;
	static Border noBorder;
	static Color windowColor;

	// Declare all visual objects here.
	CrypSwingLetterField[][] plaintext = new CrypSwingLetterField[LINE_LENGTH][NUMBER_OF_LINES];
	JLabel[][] ciphertext = new JLabel[LINE_LENGTH][NUMBER_OF_LINES];
	JLabel freqByPop = new JLabel(" ");
	JLabel freqAlpha = new JLabel(" ");
	JButton populatePuzzle = new JButton("Populate Puzzle");
	JButton solveWord = new JButton("Solve Word");
	JButton clear = new JButton ("Clear");
	JButton guess = new JButton ("Guess");
	JButton about = new JButton ("About");

	// Declare all Event objects here.
	CrypSwingBtnEvent CrypPuzzleGridBagBtnActions;
	CrypSwingKeyEvent CrypPuzzleGridBagKeyActions;

	// And here are some variables to remember which space last got focus.
	// (-1 indicates that no space has focus.)
	int rowWithFocus = -1, columnWithFocus = -1;

	/**
	 * The constructor sets up all the controls and other important frame stuff.
	 */
	public CrypSwingFrame() {
		noBorder = BorderFactory.createEmptyBorder();
		windowColor = getBackground();

		int doubleRowIndex = 0;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Cryp - Puzzle Solver");
		getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		CrypPuzzleGridBagKeyActions = new CrypSwingKeyEvent(this);

		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1; constraints.weighty = 1;
		constraints.ipadx = 3;

		for (constraints.gridy = 0; constraints.gridy < NUMBER_OF_HALF_LINES; constraints.gridy++) {
			if (constraints.gridy%2==0) {
				// The row is a plaintext row.
				for (constraints.gridx = 0; constraints.gridx < LINE_LENGTH; constraints.gridx++) {
					plaintext[constraints.gridx][doubleRowIndex] = new CrypSwingLetterField("", 1, doubleRowIndex, constraints.gridx, this);
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
				for (constraints.gridx = 0; constraints.gridx < LINE_LENGTH; constraints.gridx++) {
					ciphertext[constraints.gridx][doubleRowIndex] = new JLabel(" ", JLabel.CENTER);
					add(ciphertext[constraints.gridx][doubleRowIndex], constraints);
				}		
				doubleRowIndex++;
			}
		}

		// Add places for frequency reports.
		constraints.gridx = 0;
		constraints.gridy++;
		constraints.gridwidth = LINE_LENGTH;
		add(freqByPop, constraints);
		constraints.gridy++;
		add(freqAlpha, constraints);

		//Add a row of buttons.
		CrypPuzzleGridBagBtnActions = new CrypSwingBtnEvent(this);
		constraints.gridx = 0;
		constraints.gridy++;
		constraints.gridwidth = BUTTON_WIDTH;
		add(populatePuzzle, constraints);
		populatePuzzle.setActionCommand("POPULATE");
		populatePuzzle.addActionListener(CrypPuzzleGridBagBtnActions);
		constraints.gridx += BUTTON_WIDTH;
		add(solveWord, constraints);
		solveWord.setActionCommand("SOLVEWORD");
		solveWord.addActionListener(CrypPuzzleGridBagBtnActions);
		constraints.gridx += BUTTON_WIDTH;
		add(clear, constraints);
		clear.setActionCommand("CLEAR");
		clear.addActionListener(CrypPuzzleGridBagBtnActions);
		constraints.gridx += BUTTON_WIDTH;
		add(guess, constraints);
		guess.setActionCommand("GUESS");
		guess.addActionListener(CrypPuzzleGridBagBtnActions);
		constraints.gridx += BUTTON_WIDTH;
		add(about, constraints);
		about.setActionCommand("ABOUT");
		about.addActionListener(CrypPuzzleGridBagBtnActions);

		// Set up the frame.
		pack();
		setVisible(true);
		doubleRowIndex = 0;
		for (constraints.gridy = 0; constraints.gridy < NUMBER_OF_LINES; constraints.gridy++) {
			for (constraints.gridx = 0; constraints.gridx < LINE_LENGTH; constraints.gridx++)
				makeUneditable(plaintext[constraints.gridx][doubleRowIndex], ' ');
			doubleRowIndex++;
		}

	}

	/**
	 * This method records the row and column numbers of the space that just got focus.
	 * @param rowNum The row number
	 * @param colNum The column number
	 */
	public void recordFocus(int rowNum, int colNum) {
		this.rowWithFocus = rowNum;
		this.columnWithFocus = colNum;
	}
	
	/**
	 * This method determines if a character is valid for a ciphertext word. This method doesn't particularly
	 * belong in this object, which is just a handy place for a function called by multiple objects.
	 * @param c The character
	 * @return true if the character is valid for a ciphertext word; false otherwise.
	 */
	public static boolean validCiphertextChar(char c) {
		return ((c == '\'') || (c >= 'A' && c <= 'Z'));
	}
	
	/**
	 * This function takes a text field and makes it uneditable, displaying the ciphertext character instead.
	 * @param jtf     The text field
	 * @param display The character to display
	 */
	void makeUneditable (JTextField jtf, char display) {
		jtf.setFocusable(false);
		jtf.setBorder(noBorder);
		jtf.setBackground(windowColor);
		jtf.setText(Character.toString(display));
	}

	/**
	 * All over the puzzle, translate the ciphertext character into the user's choice - or guess - of plaintext.
	 * @param ciphertext The ciphertext character to translate
	 * @param plaintext  The user's choice of plaintext translation
	 */
	void translateEverywhere(String ciphertextLetter, String plaintextLetter) {
		int charIndex, lineIndex;

		for (lineIndex = 0; lineIndex < NUMBER_OF_LINES; lineIndex++)
			for (charIndex = 0; charIndex < LINE_LENGTH; charIndex++)
				if (ciphertext[charIndex][lineIndex].getText().charAt(0) == ciphertextLetter.charAt(0))
					plaintext[charIndex][lineIndex].setText(plaintextLetter);
	}

	/**
	 * Take the user's guess at the plaintext of one ciphertext word.
	 * @param rowNum    The row in the puzzle with the ciphertext word
	 * @param colNum    The column number in the puzzle where the ciphertext word begins
	 * @param plaintext The plaintext we're guessing
	 */
	public void guess(int rowNum, int colNum, String plaintext) {

		String currentText, ciphertextWord = "";
		int colIndex = colNum;
		CrypKey ignore = new CrypKey();

		// First of all, since it's possible that the user could have changed the ciphertext, check that it's still compatible.
		currentText = ciphertext[colIndex][rowNum].getText();
		if (currentText == null) return;
		if (currentText == "") return;
		if (!validCiphertextChar(currentText.charAt(0))) return;
		while (true) {
			ciphertextWord += currentText;
			colIndex++;
			if (colIndex > LINE_LENGTH) break;
			currentText = ciphertext[colIndex][rowNum].getText();
			if (currentText == null) break;
			if (currentText == "") break;
			if (!validCiphertextChar(currentText.charAt(0))) break;
		}
		if (ciphertextWord.length() != plaintext.length()) return;
		if (!CrypCodeBreaker.compatible(ciphertextWord, plaintext, false, ignore)) return;
		
		// Okay! If we got this far after all that validation, the word must be compatible!
		// Now let's commit ourselves to the user's guess.
		for (colIndex = 0; colIndex < ciphertextWord.length(); colIndex++)
			translateEverywhere(ciphertextWord.substring(colIndex, colIndex+1), plaintext.substring(colIndex, colIndex+1));
	}

	/**
	 * Determine all the plaintext letters that are taken, except for
	 * those whose ciphertext letters are in the ciphertextWord argument.
	 * @param ciphertextWord The ciphertext word that is the exception to our data-gathering
	 * @return String A compliation of all the plaintext letters that fit the criteria above
	 */
	public String usedPlaintext(String ciphertextWord) {
		String temp = "";
		for (int rowIndex = 0; rowIndex < NUMBER_OF_LINES; rowIndex++)
			for (int columnIndex = 0; columnIndex < LINE_LENGTH; columnIndex++)
				if (shouldBeAddedToUsedPlaintext(rowIndex, columnIndex, temp, ciphertextWord))
					temp += plaintext[columnIndex][rowIndex].getText();
		return temp;
	}

	/**
	 * Determine whether plaintext at the given row and column should be added to a list of used plaintext.
	 * @param rowIndex       The row in which the plaintext character was read
	 * @param columnIndex       The column in which the plaintext character was read
	 * @param plaintextSoFar The list of used plaintext as compiled so far
	 * @param ciphertextWord The ciphertext word whose characters shouldn't be added
	 * @return boolean true if the plaintext should be added, false otherwise
	 */
	private boolean shouldBeAddedToUsedPlaintext(int rowIndex, int columnIndex, String plaintextSoFar, String ciphertextWord) {
		String plaintextString = plaintext[columnIndex][rowIndex].getText();
		if (plaintextString == null) return false;
		if (plaintextString.trim().length() == 0) return false;
		char plaintextChar = plaintextString.charAt(0);
		if (plaintextChar < 'A' || plaintextChar > 'Z') return false;
		if (plaintextSoFar.contains(plaintextString)) return false;
		String correspondingCiphertextLetter = ciphertext[columnIndex][rowIndex].getText();
		// If the plaintext is a letter, presumably the corresponding ciphertext is too.
		if (ciphertextWord.contains(correspondingCiphertextLetter)) return false;
		return true;
	}

	/**
	 * The main method just opens the frame.
	 * @param args not used
	 */
	public static void main(String[] args) {
		CrypSwingFrame frame = new CrypSwingFrame();
	}
}

package crypSwing;

import java.awt.event.*;
import java.util.HashMap;
import java.util.Collections;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.JOptionPane;

/**
 * This class has the code for when a user hits a button in the main CrypSwing window.
 * @author Aaron Bitman
 * @version 1.0 07/21/19
 * @version 2.0 01/20/21
 * @version 2.1 01/02/22
 * @version 3.0 11/30/22
 */
public class CrypSwingBtnEvent implements ActionListener {
	
	CrypSwingFrame gui;
	static Border yesBorder;
	String puzzle;

	/**
	 * The constructor takes the GUI Frame to point to it.
	 * @param in The GUI puzzle frame.
	 */
	public CrypSwingBtnEvent(CrypSwingFrame in) {
		gui = in;
		yesBorder = gui.plaintext[0][0].getBorder();
	}

	/**
	 * This function takes a text field and makes it editable.
	 * @param jtf The text field
	 */
	private void makeEditable(JTextField jtf) {
		jtf.setFocusable(true);
		jtf.setBorder(yesBorder);
		jtf.setBackground(Color.WHITE);
	}
	
	/**
	 * This function wipes out a line of ciphertext, starting from a given character.
	 * @param startIndex From which character of the line (to its end) to wipe out
	 * @param lineIndex  Which line to wipe out
	 */
	private void wipeOutRestOfLine (int startIndex, int lineIndex) {
		//
		while (startIndex < CrypSwingFrame.LINE_LENGTH) {
			gui.ciphertext[startIndex][lineIndex].setText(" ");
			gui.makeUneditable(gui.plaintext[startIndex][lineIndex], ' ');
			startIndex++;
		}
	}
	
	/**
	 * This function clears all the plaintext in the GUI.
	 */
	private void clear() {

		for (int lineIndex = 0; lineIndex < CrypSwingFrame.NUMBER_OF_LINES; lineIndex++)
			for (int charIndex = 0; charIndex < CrypSwingFrame.LINE_LENGTH; charIndex++) {
				String textString = gui.plaintext[charIndex][lineIndex].getText();
				if (textString != null && textString.length() > 0) {
					char textChar = textString.charAt(0);
					if (textChar >= 'A' && textChar <= 'Z')
						gui.plaintext[charIndex][lineIndex].setText("");
				}
			}
	}
	
	/**
	 * This function populates the puzzle in the GUI.
	 * @param puzzle a String with the complete cryptogram
	 */
	private void populatePuzzle(String puzzle, String likeExclusion) {
		int charIndex, lineIndex = 0, startIndex = 0, endIndex, charInLine;
		char currentChar;

		int theVeryEnd = puzzle.length();
		final int MAX_LENGTH = CrypSwingFrame.LINE_LENGTH * CrypSwingFrame.NUMBER_OF_LINES;

		// If the puzzle is too long to fit in the window, truncate it accordingly.
		if (theVeryEnd >= MAX_LENGTH)
			theVeryEnd = MAX_LENGTH - 1;

		// Loop through the whole puzzle.
		while (startIndex < theVeryEnd && lineIndex < CrypSwingFrame.NUMBER_OF_LINES) {
			endIndex = startIndex + CrypSwingFrame.LINE_LENGTH;
			// Check if this is the last line of the puzzle.
			if (endIndex >= theVeryEnd)
				endIndex = theVeryEnd - 1;
			else {
				endIndex = puzzle.substring(0, endIndex).lastIndexOf(' ', endIndex) - 1;
				// Check if the word is too long to fit on a line of the gui.
				if (endIndex == -2)
					break;
			}

			// Use the substring to populate a line of the gui.
			charInLine = 0;
			for (charIndex = startIndex; charIndex <= endIndex; charIndex++) {
				currentChar = puzzle.charAt(charIndex);
				gui.ciphertext[charInLine][lineIndex].setText(Character.toString(currentChar));
				if (currentChar >= 'A' && currentChar <= 'Z') {
					makeEditable(gui.plaintext[charInLine][lineIndex]);
					gui.plaintext[charInLine][lineIndex].setText("");
				}
				else {
					gui.makeUneditable(gui.plaintext[charInLine][lineIndex], currentChar);
				}
				charInLine++;
			}
			// Now that we finished the REAL line, wipe out the rest of the line.
			wipeOutRestOfLine(charInLine, lineIndex);
			
			// Go to the next line.
			lineIndex++;
			startIndex = endIndex + 2;
		}
		// Clean up the screen from the end of the puzzle and on.
		while (lineIndex < CrypSwingFrame.NUMBER_OF_LINES) {
			wipeOutRestOfLine(0, lineIndex);
			lineIndex++;
		}
		// Populate the frequency reports.
		CrypFrequencyTable freqTable = new CrypFrequencyTable(puzzle);

		gui.freqByPop.setText(freqTable.getFreqByPop());
		gui.freqAlpha.setText(freqTable.getFreqAlpha());

		// Populate the plaintext with some initial guesses.
		CrypAlphabetMapping alphabetMap = new CrypAlphabetMapping(likeExclusion == "Y");
		CrypCiphertextWordMappingGroup wordMapGroup = new CrypCiphertextWordMappingGroup(puzzle, alphabetMap);
		for (HashMap.Entry<Character, CrypLetterMapping> entry : alphabetMap.translations.entrySet())
			if (entry.getValue().size() == 1)
				gui.translateEverywhere(entry.getKey().toString(), entry.getValue().possiblePlaintext());
		
		// Finally, hold onto the puzzle in String form in case the user wants to make another guess later.
		this.puzzle = puzzle;
	}
	
	/**
	 * This function builds an object with parameters to pass to the word frame.
	 */
	private CrypWordFrameParameters getCrypWordFrameParameters() {
		CrypWordFrameParameters temp;
		
		if (gui.rowWithFocus == -1 || gui.columnWithFocus == -1) return null;
		temp = new CrypWordFrameParameters(gui);
		return temp;
	}
	
	/**
	 * This function has the code for every button in the puzzle GUI.
	 * @param btnEvent Which button event was triggered
	 */
	public void actionPerformed(ActionEvent btnEvent) {
		String action = btnEvent.getActionCommand();
		switch (action) {
		case "POPULATE":
			// Open the window to let the user populate the puzzle, and if appropriate, populate it.
			CrypEnterPuzzle enterPuzzleWindow = new CrypEnterPuzzle(gui);
			String[] results = enterPuzzleWindow.run();
			if (results[0] != null)
				if (!results[0].trim().isEmpty()) {
					populatePuzzle(results[0].trim().toUpperCase(), results[1]);
				}
			break;
			
		case "SOLVEWORD":
			// Open the window to solve one word (possibly populating it from the puzzle window).
			CrypWordFrameParameters parameters;
			
			parameters = getCrypWordFrameParameters();
			CrypWordFrame wordFrame = new CrypWordFrame(gui, parameters);
			break;
			
		case "CLEAR":
			clear();
			break;
			
		case "GUESS":
			// Based on the plaintext letters already filled in, take a guess at the rest of them.
			CrypAlphabetMapping alphabetMap = new CrypAlphabetMapping(false);
			// Loop through all the known plaintext.
			for (int lineIndex = 0; lineIndex < CrypSwingFrame.NUMBER_OF_LINES; lineIndex++)
				for (int charIndex = 0; charIndex < CrypSwingFrame.LINE_LENGTH; charIndex++) {
					// If the plaintext is alphabetical, that's one clue we should use.
					String plaintextString = gui.plaintext[charIndex][lineIndex].getText();
					if (plaintextString != null)
						if (plaintextString.trim().length() > 0) {
							char plaintextChar = plaintextString.charAt(0);
							if (plaintextChar >= 'A' && plaintextChar <= 'Z') {
								// Now we can narrow down the alphabet map according to that known plaintext.
								alphabetMap.narrowDownLetters(gui.ciphertext[charIndex][lineIndex].getText().charAt(0), Collections.singletonList(plaintextChar));
							}
						}
				}
			// Now that we have a reduced alphabet map, reduce it further by guessing more words.
			CrypCiphertextWordMappingGroup wordMapGroup = new CrypCiphertextWordMappingGroup(this.puzzle, alphabetMap);
			// Based on the reduced alphabet map, look up translations.
			for (HashMap.Entry<Character, CrypLetterMapping> entry : alphabetMap.translations.entrySet())
				if (entry.getValue().size() == 1)
					gui.translateEverywhere(entry.getKey().toString(), entry.getValue().possiblePlaintext());
			break;
			
		case "ABOUT":
			JOptionPane.showMessageDialog(null, "CrypSwing version 3.0, 2022", "About CrypSwing", JOptionPane.PLAIN_MESSAGE);
		}
	}
}

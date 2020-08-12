package crypSwing;

import java.awt.event.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import crypSwing.CrypSwingFrame;

import java.awt.*;

/**
 * This class has the code for when a user hits a button in the main CrypSwing window.
 * @author Aaron Bitman
 * @version 1.0 07/21/19
 */
public class CrypSwingBtnEvent implements ActionListener {
	
	CrypSwingFrame gui;
	static Border noBorder, yesBorder;
	static Color windowColor;

	/**
	 * The constructor takes the GUI Frame to point to it.
	 * @param in The GUI puzzle frame.
	 */
	public CrypSwingBtnEvent(CrypSwingFrame in) {
		gui = in;
		noBorder = BorderFactory.createEmptyBorder();
		yesBorder = gui.plaintext[0][0].getBorder();
		windowColor = gui.getBackground();
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
	 * This function takes a text field and makes it uneditable, displaying the ciphertext character instead.
	 * @param jtf The text field
	 * @param display The character to display
	 */
	void makeUneditable (JTextField jtf, char display) {
		jtf.setFocusable(false);
		jtf.setBorder(noBorder);
		jtf.setBackground(windowColor);
		jtf.setText(Character.toString(display));
	}
	
	/**
	 * This function wipes out a line of ciphertext, starting from a given character.
	 * @param startIndex From which character of the line (to its end) to wipe out
	 * @param lineIndex Which line to wipe out
	 */
	private void wipeOutRestOfLine (int startIndex, int lineIndex) {
		//
		while (startIndex < gui.LINELENGTH) {
			gui.ciphertext[startIndex][lineIndex].setText(" ");
			makeUneditable(gui.plaintext[startIndex][lineIndex], ' ');
			startIndex++;
		}
	}
	
	/**
	 * This function clears all the plaintext in the GUI.
	 */
	private void clear() {
		int charIndex, lineIndex;
		
		for (lineIndex = 0; lineIndex < gui.NUMBEROFLINES; lineIndex++)
			for (charIndex = 0; charIndex < gui.LINELENGTH; charIndex++)
				gui.plaintext[charIndex][lineIndex].setText("");

	}
	
	/**
	 * This function populates the puzzle in the GUI.
	 * @param puzzle a String with the complete cryptogram
	 */
	private void populatePuzzle(String puzzle) {
		int charIndex, lineIndex = 0, startIndex = 0, endIndex, charInLine;
		char currentChar;

		int theVeryEnd = puzzle.length();
		final int MAXLENGTH = gui.LINELENGTH * gui.NUMBEROFLINES;

		// If the puzzle is too long to fit in the window, truncate it accordingly.
		if (theVeryEnd >= MAXLENGTH)
			theVeryEnd = MAXLENGTH - 1;

		// Loop through the whole puzzle.
		while (startIndex < theVeryEnd && lineIndex < gui.NUMBEROFLINES) {
			endIndex = startIndex + gui.LINELENGTH;
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
					makeUneditable(gui.plaintext[charInLine][lineIndex], currentChar);
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
		while (lineIndex < gui.NUMBEROFLINES) {
			wipeOutRestOfLine(0, lineIndex);
			lineIndex++;
		}
		// Populate the frequency reports.
		CrypFrequencyTable freqTable = new CrypFrequencyTable(puzzle);

		gui.freqByPop.setText(freqTable.getFreqByPop());
		gui.freqAlpha.setText(freqTable.getFreqAlpha());
	}

	/**
	 * This function has the code for every button in the puzzle GUI.
	 */
	public void actionPerformed(ActionEvent btnEvent) {
		String action = btnEvent.getActionCommand();
		switch (action) {
		case "POPULATE":
			String puzzleText;
			puzzleText = JOptionPane.showInputDialog("Enter message.");
			
			// I wanted a dialog box with a big text area, so I tried this:
			
			//JTextArea puzzleArea = new JTextArea();
			//puzzleArea.setLineWrap(true);
			//puzzleArea.setWrapStyleWord(true);
			//JScrollPane scrollPane = new JScrollPane(puzzleArea);
			//scrollPane.setPreferredSize (new Dimension(500,500));
			//puzzleText = JOptionPane.showInputDialog(null, scrollPane);
			
			// The problem with that is that the scroll pane takes the place of
			// the program's message to the user! I want it for the user's message
			// to the program, which, using the code above, is only one line.

			if (puzzleText != null)
			{
				puzzleText = puzzleText.trim().toUpperCase();
				if (!puzzleText.isEmpty())
					populatePuzzle(puzzleText);
			}
			break;
			
		case "SOLVEWORD":
			CrypWordFrame wordFrame = new CrypWordFrame();
			break;
			
		case "CLEAR":
			clear();
		}
	}
}

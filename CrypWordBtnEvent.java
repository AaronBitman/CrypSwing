package crypSwing;

import java.awt.event.*;
import java.util.ArrayList;

/**
 * This class contains the code for when a user hits a button in the Word-solving frame.
 * @author Bitman
 * @version 1.0 7/21/19
 * @version 2.0 01/20/21
 */
public class CrypWordBtnEvent implements ActionListener {
	
	CrypWordFrame gui;
	
	/**
	 * The constructor gets a reference to the Word-solving frame.
	 * @param in
	 */
	public CrypWordBtnEvent(CrypWordFrame in) {
		gui = in;
	}
	
	/**
	 * The code that gets triggered when a user hits a button.
	 */
	public void actionPerformed(ActionEvent btnEvent) {
		int index;
		String action = btnEvent.getActionCommand();
		switch (action) {
		case "CLEAR":
			for (index=0; index < CrypWordFrame.MAX_WORD_LENGTH; index++) {
				gui.ciphertext[index].setText("");
				gui.plaintext[index].setText("");
			}
			gui.actualCiphertextWord.setText("");
			gui.ciphertext[0].requestFocus();
			break;
		case "SOLVE":
			solve();
		}
	}
	
	/**
	 * Display all solutions for a word.
	 */
	private void solve() {
		CrypKey ck = new CrypKey();
		CrypKeyEntry cke = new CrypKeyEntry();
		int index, total, resultTotal;
		String temp;
		ArrayList<String> output = new ArrayList<String>();
		
		total = gui.actualCiphertextWord.getText().length();
		if (total==0) {
			output.add("Error: Ciphertext word not entered.");
		}
		else {
			for (index = 0; index < total; index++) {
				temp = gui.plaintext[index].getText();
				if (!temp.isEmpty()) {
					cke.setCrypKeyEntry(index, temp.charAt(0));
					ck.addEntry(cke);
				}
			}
			output = CrypCodeBreaker.breakWord(gui.actualCiphertextWord.getText(), gui.likeExclusion.isSelected(), ck);
			if (gui.usePuzzle.isSelected()) {
				String plaintextTaken = gui.puzzleFrame.usedPlaintext(gui.actualCiphertextWord.getText());
				for (index = output.size() - 1; index >= 0; index--)
					if (charsInCommon(output.get(index), plaintextTaken))
						output.remove(index);
			}
		}
		gui.results.setListData(output.toArray(new String[output.size()]));
		resultTotal = output.size();
		if (resultTotal < 2)
			gui.resultsLabel.setText("Results: ");
		else
			gui.resultsLabel.setText(resultTotal + " Results: ");
	}

	/**
	 * Determine whether two strings have at least one character in common.
	 * @param string1 The first string
	 * @param string2 The second string
	 * @return boolean true if the strings have at least one common character, false otherwise.
	 */
	static boolean charsInCommon(String string1, String string2) {
		for (char string1Char : string1.toCharArray())
			for (char string2Char : string2.toCharArray())
				if (string1Char == string2Char) return true;
		return false;
	}
}

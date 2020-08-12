package crypSwing;

import java.awt.event.*;

/**
 * This class contains the code for when a user hits a button in the Word-solving frame.
 * @author Bitman
 * @version 1.0 7/21/19
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
			for (index=0; index < gui.MAXWORDLENGTH; index++) {
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
		String[] output;
		
		total = gui.actualCiphertextWord.getText().length();
		if (total==0) {
			output = new String[1];
			output[0] = "Error: Ciphertext word not entered.";
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
		}
		gui.results.setListData(output);
		resultTotal = output.length;
		if (resultTotal < 2)
			gui.resultsLabel.setText("Results: ");
		else
			gui.resultsLabel.setText(resultTotal + " Results: ");
	}
}

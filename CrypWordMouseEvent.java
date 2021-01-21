package crypSwing;

import java.awt.event.*;

/**
 * This class contains the code for when a user clicks the mouse on anything other than a button in
 * the Word-solving frame. Its purpose is to handle double-clicking on the "Results" JScrollPane.
 * @author Bitman
 * @version 2.0 01/20/21
 */

public class CrypWordMouseEvent implements MouseListener {

	CrypWordFrame gui;
	
	/**
	 * The constructor gets a reference to the "Results" list in the word-solving frame.
	 * @param in A reference to the "Results" list.
	 */
	public CrypWordMouseEvent(CrypWordFrame in) {
		gui = in;
	}

	/**
	 * If the user clicked on a plaintext candidate, check that it's a
	 * double-click, validate and so forth, and then make the guess.
	 */
	public void mouseClicked(MouseEvent e) {
		String plaintextChoice;
		if (e.getClickCount() == 2) {
			plaintextChoice = gui.results.getSelectedValue();
			if (plaintextChoice != null)
				if (!plaintextChoice.contains(" "))
					// The line above assumes that all messages (e.g. "No results found"
					// or "No ciphertext entered") contain a space.
					gui.guess(plaintextChoice);
		}
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

}

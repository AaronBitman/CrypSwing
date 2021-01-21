package crypSwing;

import javax.swing.JTextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

//import crypSwing.CrypSwingFrame;

/**
 * This class is a text field which identifies its own row
 * and column number and can report when it gets focus.
 * @author Bitman
 * @version 2.0 01/20/21
 */

public class CrypSwingLetterField extends JTextField implements FocusListener {

	private static final long serialVersionUID = 1L;
	private int rowNum, colNum;
	private CrypSwingFrame gui;
	
	/**
	 * The constructor takes the extra information to record.
	 * @param text   The initial text of the text field
	 * @param cols   The number of columns
	 * @param rowNum Which row this field occupies in the window
	 * @param colNum Which column this field occupies in the window
	 * @param in     The frame
	 */
	public CrypSwingLetterField(String text, int cols, int rowNum, int colNum, CrypSwingFrame in) {
		super(text, cols);
		this.rowNum = rowNum;
		this.colNum = colNum;
		gui = in;
		this.addFocusListener(this);
	}

	/**
	 * When this field gains focus, report that fact to the frame.
	 */
	public void focusGained(FocusEvent e) {
		gui.recordFocus(this.rowNum, this.colNum);
	}

	/**
	 * When this field loses focus... well, there's nothing to do.
	 * But we need this method to implement FocusListener.
	 */
	public void focusLost(FocusEvent e) {}

}

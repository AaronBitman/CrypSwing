package crypSwing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
//import java.awt.event.MouseListener;
import java.awt.Point;

import javax.swing.*;

/**
 * This class contains the code for the word-solving frame.
 * @author Bitman
 * @version 1.0 07/21/19
 * @version 2.0 01/20/21
 */
public class CrypWordFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	static final int MAX_WORD_LENGTH = 32;
	static final int QUARTER_WORD_LENGTH = MAX_WORD_LENGTH / 4;
	CrypWordKeyEvent CrypWordKeyActions = new CrypWordKeyEvent(this);
	CrypWordBtnEvent CrypWordBtnActions = new CrypWordBtnEvent(this);
	CrypWordMouseEvent CrypWordMouseActions = new CrypWordMouseEvent(this);

	// Declare all the visual objects here.
	JLabel plaintextLabel = new JLabel("Plaintext: ", JLabel.RIGHT);
	JTextField[] plaintext = new JTextField[MAX_WORD_LENGTH];
	JLabel ciphertextLabel = new JLabel("Ciphertext: ", JLabel.RIGHT);
	JTextField[] ciphertext = new JTextField[MAX_WORD_LENGTH];
	JLabel actualCiphertextWordLabel = new JLabel("Actual Ciphertext Word: ", JLabel.RIGHT);
	JLabel actualCiphertextWord = new JLabel("", JLabel.LEFT);
	JCheckBox likeExclusion = new JCheckBox("Like-Exclusion", false);
	JCheckBox usePuzzle = new JCheckBox("Use Puzzle", false);
	JButton solve = new JButton("Solve");
	JButton clear = new JButton("Clear");
	JLabel instructions = new JLabel("To guess a word, double-click it.", JLabel.LEFT);
	JLabel resultsLabel = new JLabel("Results: ", JLabel.RIGHT);
	JList<String> results = new JList<>();
    JScrollPane scroll = new JScrollPane(results,
    		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
    		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    CrypSwingFrame puzzleFrame;
    
    // The following variables indicate at which row and column to find the input text.
    // -1's indicate that there isn't any.
    int puzzleRowNum = -1;
    int puzzleColNum = -1;

    /**
     * The constructor sets up the frame.
     */
	public CrypWordFrame(CrypSwingFrame in, CrypWordFrameParameters selectedWord) {

		int index;
		puzzleFrame = in;
		Point loc = puzzleFrame.getLocation();
		setLocation(loc.x+100,loc.y+100);

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
		for (constraints.gridx = 1; constraints.gridx <= MAX_WORD_LENGTH; constraints.gridx++) {
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
		for (constraints.gridx = 1; constraints.gridx <= MAX_WORD_LENGTH; constraints.gridx++) {
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
		constraints.gridwidth = MAX_WORD_LENGTH;
		add(actualCiphertextWord, constraints);
		
		// Checkbox and button row
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = QUARTER_WORD_LENGTH;
		add(likeExclusion, constraints);
		constraints.gridx += QUARTER_WORD_LENGTH;
		add(usePuzzle, constraints);
		constraints.gridx += QUARTER_WORD_LENGTH;
		add(solve, constraints);
		JRootPane rootPane = SwingUtilities.getRootPane(solve);
		rootPane.setDefaultButton(solve);
		solve.setActionCommand("SOLVE");
		solve.addActionListener(CrypWordBtnActions);
		constraints.gridx += QUARTER_WORD_LENGTH;
		add(clear, constraints);
		clear.setActionCommand("CLEAR");
		clear.addActionListener(CrypWordBtnActions);

		// (fifth) row with instructions
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = MAX_WORD_LENGTH;
		add(instructions, constraints);

		// results (sixth) row
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		add(resultsLabel, constraints);
		constraints.gridx = 1;
		constraints.gridwidth = MAX_WORD_LENGTH;
		results.setFixedCellHeight(16);
		add(scroll, constraints);
		results.addMouseListener(CrypWordMouseActions);
		
		// Set up the frame.
		pack();
		setVisible(true);
		ciphertext[0].requestFocus();
		
		// If there's valid text to enter...
		if (selectedWord != null) {
			// ...then enter it.
			for (index = 0; index < selectedWord.ciphertextWord.length(); index++)
				ciphertext[index].setText(String.valueOf(selectedWord.ciphertextWord.charAt(index)));
			SetActualCiphertextWord();
			puzzleRowNum = selectedWord.rowNum;
			puzzleColNum = selectedWord.colNum;
			
			// And enter known (or assumed) ciphertext.
			for (index = 0; index < selectedWord.knownPlaintext.keyEntries.size(); index++) {
				plaintext[selectedWord.knownPlaintext.keyEntries.get(index).getIndex()].setText(
						Character.toString(selectedWord.knownPlaintext.keyEntries.get(index).getPlainText()));
			}
			
		}
	}
	
	/**
	 * Set the actual ciphertext word field based on the contents of the entered ciphertext.
	 */
	void SetActualCiphertextWord() {
		char ciphertextChar;
		int index = 0;
		String fieldContent, temp = "";
		
		for (index=0; index < MAX_WORD_LENGTH; index++) {
			fieldContent = ciphertext[index].getText();
			if (fieldContent.isEmpty()) break;
			ciphertextChar = fieldContent.charAt(0);
			if (!CrypSwingFrame.validCiphertextChar(ciphertextChar)) break;
			temp += ciphertextChar;
		}
		actualCiphertextWord.setText(temp);
	}

	/**
	 * Take the user's guess at the plaintext translation of this object's ciphertext.
	 * @param plaintext_guess The user's guess at the plaintext
	 */
	void guess(String plaintext_guess) {
		if (puzzleRowNum >= 0 && puzzleColNum >= 0)
			puzzleFrame.guess(puzzleRowNum, puzzleColNum, plaintext_guess);
	}
}
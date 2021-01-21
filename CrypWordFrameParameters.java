package crypSwing;

/**
 * This class contains parameters for the puzzle frame to pass to the word frame.
 * Yeah, we could just pass each individual variable to the word frame's constructor,
 * but this handy object is easier to return from a method. Plus it can encapsulate
 * some of the logic to find the ciphertext word in the puzzle frame.
 * @author Bitman
 * @version 2.0 01/20/21
 */
public class CrypWordFrameParameters {

	int rowNum, colNum;
	String ciphertextWord = "";
	CrypKey knownPlaintext = new CrypKey();

	/**
	 * This constructor takes the puzzle frame and the starting row and column
	 * and figures out the rest of the info. Note that this constructor assumes
	 * that the row and column arguments point to a legitimate ciphertext letter
	 * in the puzzle, or else you'll want a null object. You'll just have
	 * to do the validation before constructing the object; dem's da berries.
	 * @param puzzleFrame The puzzle frame
	 */
	public CrypWordFrameParameters(CrypSwingFrame puzzleFrame) {
		
		int index;
		String fieldContent;
		CrypKeyEntry plaintextEntry = new CrypKeyEntry();
		
		rowNum = puzzleFrame.rowWithFocus;
		colNum = puzzleFrame.columnWithFocus;
		
		// Because the initial column number might point to the middle of the word,
		// scan for the beginning of the word and change the colNum accordingly.
		while (colNum > 0) {
			fieldContent = puzzleFrame.ciphertext[colNum-1][rowNum].getText();
			if (fieldContent == null) break;
			if (fieldContent.trim().isEmpty()) break;
			if (!puzzleFrame.validCiphertextChar(fieldContent.charAt(0))) break;
			colNum--;
		}
		
		index = colNum;
		while (index < puzzleFrame.LINE_LENGTH) {
			// Read the whole ciphertext word...
			fieldContent = puzzleFrame.ciphertext[index][rowNum].getText();
			if (fieldContent == null) break;
			if (fieldContent.trim().isEmpty()) break;
			if (!puzzleFrame.validCiphertextChar(fieldContent.charAt(0))) break;
			ciphertextWord += fieldContent;
			
			// ...and the known (or assumed) plaintext.
			fieldContent = puzzleFrame.plaintext[index][rowNum].getText();
			if (fieldContent != null)
				if (!fieldContent.trim().isEmpty()) {
					// Presumably the content is valid or else the field would have spat it out.
					plaintextEntry.setCrypKeyEntry(index-colNum, fieldContent.charAt(0));
					knownPlaintext.addEntry(plaintextEntry);
				}
			index++;
		}
	}

}

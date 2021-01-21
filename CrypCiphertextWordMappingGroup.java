package crypSwing;

import java.util.*;

/**
 * This class takes a cryptogram and builds an object with the significant words and their possible translations.
 * @author Bitman
 * @version 2.0 01/20/21
 */
public class CrypCiphertextWordMappingGroup {

	ArrayList<CrypCiphertextWordMapping> wordMapArray = new ArrayList<CrypCiphertextWordMapping>();
	CrypAlphabetMapping alphabetMap;

	/** This constructor takes the cryptogram puzzle and maps (most of) its words to possible translations.
	 * @param puzzle The cryptogram
	 * @param likeExclusion The user's choice of whether to assume like-exclusion
	 */
	CrypCiphertextWordMappingGroup(String puzzle, boolean likeExclusion) {
		int index = 0;
		boolean inParentheses = false;
		String currentWord;
		CrypCiphertextWordMapping newElement;
		
		// Yeah, we could split the puzzle succinctly with a regular expression, like "puzzle.split("[^A-Z']"))",
		// but then, for some reason, removing a word from the list causes a runtime exception.
		// Besides, we get more flexibility by stepping through the process manually.

		while (index < puzzle.length()) {
			if (CrypSwingFrame.validCiphertextChar(puzzle.charAt(index))) {
				currentWord = "";
				do {
					currentWord += puzzle.charAt(index++);
					if (index >= puzzle.length()) break;
				} while (CrypSwingFrame.validCiphertextChar(puzzle.charAt(index)));
				// Since we know that the current character isn't valid, what the heck, let's increment.
				index++;
				// Now that we've got a ciphertext word, let's consider whether to add it.
				// For instance, don't add a one-letter word in parentheses.
				if (inParentheses && currentWord.length() < 2) continue;
				// For another example, don't add a word that's already added.
				if (alreadyStored(currentWord)) continue;
				newElement = new CrypCiphertextWordMapping(currentWord, likeExclusion);
				// For yet another example, don't add any words with no known plaintext translations.
				if (newElement.numberOfCandidates() == 0) continue;
				// After all those qualifications, add the word!
				wordMapArray.add(newElement);
			}
			else if (puzzle.charAt(index) == '(') {
				inParentheses = true;
				index++;
			}
			else if (puzzle.charAt(index) == ')') {
				inParentheses = false;
				index++;
			}
			else index++;
			if (index >= puzzle.length()) break;
		}

		// Now sort the ciphertext word entries.
		wordMapArray.sort(CrypCiphertextWordMapping.CrypCiphertextWordMappingComparator);

		// Map each ciphertext character to every plaintext letter to which it might map.
		alphabetMap = new CrypAlphabetMapping(likeExclusion);

		// Now try to come up with some possible translations; keep paring
		// down the possibilities until it doesn't seem to do any more good.
		while (pareDown());
	}

	/**
	 * Determine whether a ciphertext word is already stored in our mapping.
	 * @param input The ciphertext word
	 * @return true if the ciphertext word is stored, false otherwise
	 */
	boolean alreadyStored(String input) {
		int index;
		for (index=0; index < wordMapArray.size(); index++)
			if (wordMapArray.get(index).getCiphertext().equals(input))
				return true;
		return false;
	}

	/**
	 * Pare down the map by venturing some guesses about what ciphertext
	 * characters map to what plaintext letters (or at least narrowing it down).
	 * @return true if we judge the cryptogram might profit by a further paring down; false if we feel it won't.
	 */
	boolean pareDown() {
		// The basis of this "paring down" is trying the simplest word to guess.
		CrypCiphertextWordMapping wordToTry = wordMapArray.get(0);

		// Incidentally, remember the number of candidates for this word for possible future use.
		int originalNumberOfCandidates = wordToTry.numberOfCandidates();
		// Also remember the size of the alphabet mapping.
		int originalAlphabetMapSize = alphabetMap.size();

		// For each ciphertext letter in that word...
		for (int index = 0; index < wordToTry.getCiphertext().length(); index++) {
			// ...get a list of all plaintext values based on our list of plaintext translations of this word...
			List<Character> translations = translateOneLetter(wordToTry, index);
			// ...and update the alphabet mapping accordingly.
			alphabetMap.narrowDownLetters(wordToTry.getCiphertext().charAt(index), translations);
		}

		// Now that we've narrowed down the alphabet map, we should narrow down
		// all the ciphertext-word-to-plaintext-words translations accordingly.
		int wordIndex = 1;
		while (wordIndex < wordMapArray.size()) {
			wordMapArray.get(wordIndex).pareDown(alphabetMap);
			// If the list of candidate plaintexts for the ciphertext word got pared down to zero...
			if (wordMapArray.get(wordIndex).numberOfCandidates() < 1)
				// ...then delete it.
				wordMapArray.remove(wordIndex);
			else
				// If the word map wasn't eliminated, move the index to point to the next word map.
				wordIndex++;
		}

		// If there was only one candidate plaintext word for our ciphertext word, we've
		// done all the (considerable!) good we can with it; we don't need it anymore.
		if (wordToTry.numberOfCandidates() == 1) wordMapArray.remove(0);

		// Now that the plaintext translations have changed, re-sort the word map array.
		wordMapArray.sort(CrypCiphertextWordMapping.CrypCiphertextWordMappingComparator);

		// Now determine if this paring down seems to have done sufficient good that another
		// iteration might be worth it. How much good is "sufficient good"? Well, that's debatable.

		// First of all, if all the words are solved (or eliminated because we couldn't find a good translation
		// for them), we've done all we can; how much good we did in this last paring is irrelevant!
		if (wordMapArray.size() == 0) return false;

		// Second of all, if the best word to guess is different from
		// the one before the paring down, we probably did some good.
		if (!wordToTry.getCiphertext().equals(wordMapArray.get(0).getCiphertext())) return true;

		// Third of all, even if the word is the same, if we now have fewer
		// candidate plaintext translations for it, we may have done SOME good.
		if (originalNumberOfCandidates > wordMapArray.get(0).numberOfCandidates()) return true;

		// Fourth of all, if the alphabet mapping is getting narrower, that might signify progress.
		 if (originalAlphabetMapSize > alphabetMap.size()) return true;

		 // After all that, I see no sign that we've done any good.
		 return false;
	}

	/**
	 * Given a ciphertext-word-and-its-possible-translations record, return every
	 * given translation of the nth letter.
	 * @param wordRecord The ciphertext-word-and-its-possible-translations record
	 * @param n          The index indicating which letter in the ciphertext word we're translating
	 * @return a List comprising all the possible translations, each as a Character
	 */
	private List<Character> translateOneLetter(CrypCiphertextWordMapping wordRecord, int n) {
		List<Character> temp = new ArrayList<Character>();
		// Get a list of all the possible plaintext letters.
		for (int wordIndex = 0; wordIndex < wordRecord.numberOfCandidates(); wordIndex++) {
			temp.add(wordRecord.candidate(wordIndex).charAt(n));
		}
		// Sort that list.
		Collections.sort(temp);
		// Eliminate duplicates.
		for (int translationsIndex = temp.size()-1; translationsIndex > 0; translationsIndex--)
			if (temp.get(translationsIndex) == temp.get(translationsIndex-1))
				temp.remove(translationsIndex);
		return temp;
	}

}

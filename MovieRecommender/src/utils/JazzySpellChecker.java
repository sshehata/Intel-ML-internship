package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.engine.Word;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import com.swabunga.spell.event.TeXWordFinder;

public class JazzySpellChecker implements SpellCheckListener {

	private SpellChecker spellChecker;
	private List<String> misspelledWords;

	/**
	 * get a list of misspelled words from the text
	 * 
	 * @param text
	 */
	public void getMisspelledWords(String text) {
		StringWordTokenizer texTok = new StringWordTokenizer(text,
				new TeXWordFinder());
		spellChecker.checkSpelling(texTok);
	}

	private static SpellDictionaryHashMap dictionaryHashMap;

	static {

		File dict = new File("resources/dictionary/dictionary.txt");
		try {
			dictionaryHashMap = new SpellDictionaryHashMap(dict);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initialize() {
		spellChecker = new SpellChecker(dictionaryHashMap);
		spellChecker.addSpellCheckListener(this);
	}

	public JazzySpellChecker() {

		initialize();
	}

	/**
	 * correct the misspelled words in the input String and return the result
	 */
	public String getCorrectedLine(String line) {
		misspelledWords = new ArrayList<String>();
		getMisspelledWords(line);

		for (String misSpelledWord : misspelledWords) {
			String suggestion = getSuggestions(misSpelledWord);
			if (suggestion == null)
				continue;
			line = line.replace(misSpelledWord, suggestion);
		}
		return line;
	}

	public String getCorrectedText(String line) {
		StringBuilder builder = new StringBuilder();
		String[] tempWords = line.split(" ");
		for (String tempWord : tempWords) {
			if (!spellChecker.isCorrect(tempWord)) {
				List<Word> suggestions = spellChecker.getSuggestions(tempWord,
						0);
				if (suggestions.size() > 0) {
					builder.append(spellChecker.getSuggestions(tempWord, 0)
							.get(0).toString());
				} else
					builder.append(tempWord);
			} else {
				builder.append(tempWord);
			}
			builder.append(" ");
		}
		return builder.toString().trim();
	}

	public String getSuggestions(String misspelledWord) {

		@SuppressWarnings("unchecked")
		List<Word> su99esti0ns = spellChecker.getSuggestions(misspelledWord, 0);
		try {
			return su99esti0ns.get(0).getWord();
		} catch (IndexOutOfBoundsException e) {
			return null;

		}
	}

	@Override
	public void spellingError(SpellCheckEvent event) {
		event.ignoreWord(true);
		misspelledWords.add(event.getInvalidWord());
	}

	public static void main(String[] args) {
		JazzySpellChecker jazzySpellChecker = new JazzySpellChecker();
		String line = jazzySpellChecker
				.getCorrectedLine("This is a coooooooooool boook :D");
		System.out.println(line);
	}
}
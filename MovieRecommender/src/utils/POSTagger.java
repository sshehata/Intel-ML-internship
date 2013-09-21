package utils;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSTagger {
	
	private MaxentTagger tagger;

	public POSTagger(){
		tagger = new MaxentTagger("resources/taggers/english-left3words-distsim.tagger");
	}
	
	public String tag(String line){
		return tagger.tagString(line);
	}
}
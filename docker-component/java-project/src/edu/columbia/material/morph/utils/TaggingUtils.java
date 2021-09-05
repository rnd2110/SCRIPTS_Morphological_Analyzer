package edu.columbia.material.morph.utils;

import edu.columbia.material.morph.analysis.constants.Num;
import edu.columbia.material.morph.analysis.constants.POS;
import edu.columbia.material.morph.analysis.constants.Tense;

public class TaggingUtils {
	
	public static POS adjustPOS(String word, String pos) {
		if(StringUtils.isSymbol(word)){
			return POS.SYM;
		}else if(StringUtils.isPunc(word)){
			return POS.PUNCT;
		}else if(StringUtils.isNumber(word)){
			return POS.NUM;
		}
		return POS.getByValue(pos);
	}
	
	public static Num adjustNumber(POS pos, String number) {
		if ((pos == POS.NOUN || pos == POS.PROPN)) {
			if(number.equals("NA")) {
				return Num.SG;
			}
		}else {
			return Num.NA;
		}
		return Num.getByValue(number);
	}
	
	public static Tense adjustTense(POS pos, String tense) {
		if ((pos == POS.VERB || pos == POS.AUX)) {
			if(tense.equals("NA")) {
				return Tense.PRES;
			}
		}else {
			return Tense.NA;
		}
		return Tense.getByValue(tense);
	}

}

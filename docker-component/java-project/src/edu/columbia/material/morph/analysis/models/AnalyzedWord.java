package edu.columbia.material.morph.analysis.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import edu.columbia.material.morph.ag.models.SegmentedWord;
import edu.columbia.material.morph.analysis.constants.Num;
import edu.columbia.material.morph.analysis.constants.POS;
import edu.columbia.material.morph.analysis.constants.Tense;

public class AnalyzedWord {
	
	protected String word = "";;
	protected SegmentedWord segmentation = new SegmentedWord();
	protected POS pos = POS.X;
	protected Map<POS, BigDecimal> pos_props = new HashMap<POS, BigDecimal>();
	protected Tense tense = Tense.NA;
	protected Num num = Num.NA;
	protected int index;

	public AnalyzedWord() {
	}
	
	public AnalyzedWord(String word, SegmentedWord segmentation, POS pos, Tense tense, Num num, int index) {
		this.word = word;
		this.segmentation = segmentation;
		this.pos = pos;
		this.tense = tense;
		this.num = num;
		this.index = index;
	}
	
	public AnalyzedWord(String word, SegmentedWord segmentation) {
		this.word = word;
		this.segmentation = segmentation;
		this.pos = POS.X;
		this.tense = Tense.NA;
		this.num = Num.NA;
	}
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	
	@JsonIgnore
	public SegmentedWord getSegmentation() {
		return segmentation;
	}
	public void setSegmentation(SegmentedWord segmentation) {
		this.segmentation = segmentation;
	}
	
	public String getPrefixes() {
		return segmentation.getPrefixesStr("+");
	}
	public String getStem() {
		return segmentation.getStem();
	}
	public String getSuffixes() {
		return segmentation.getSuffixesStr("+");
	}

	public POS getPos() {
		return pos;
	}
	public void setPos(POS pos) {
		this.pos = pos;
	}

	public Map<POS, BigDecimal> getPos_props() {
		return pos_props;
	}

	public void setPos_props(Map<POS, BigDecimal> pos_props) {
		this.pos_props = pos_props;
	}

	public Num getNum() {
		return num;
	}

	public void setNum(Num num) {
		this.num = num;
	}

	public Tense getTense() {
		return tense;
	}
	public void setTense(Tense tense) {
		this.tense = tense;
	}
	
	public Num getNumber() {
		return num;
	}
	public void setNumber(Num num) {
		this.num = num;
	}

	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
}

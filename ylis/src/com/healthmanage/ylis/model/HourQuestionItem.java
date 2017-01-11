package com.healthmanage.ylis.model;

public class HourQuestionItem {
	private String itemId;
	private String ansId;
	private String typeId;
	private String answer;
	private String multiAns;
	private String answVal;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getAnsId() {
		return ansId;
	}

	public void setAnsId(String ansId) {
		this.ansId = ansId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getMultiAns() {
		return multiAns;
	}

	public void setMultiAns(String multiAns) {
		this.multiAns = multiAns;
	}

	public String getAnswVal() {
		return answVal;
	}

	public void setAnswVal(String answVal) {
		this.answVal = answVal;
	}

}

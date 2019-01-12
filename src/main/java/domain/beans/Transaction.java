package domain.beans;

import java.io.Serializable;

public class Transaction implements Serializable {

	private static final long serialVersionUID = -4582287006011515009L;

	public Transaction() {}

	private String transactionID;
	private String title;
	private String type;
	private String category;
	private String date;
	private String store;
	private String amount;
	private String combinedAmount;
	private String description;
	private boolean savings;
	private char credit;
	private char creditPaid;
	private String creditCard;

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCombinedAmount() {
		return combinedAmount;
	}

	public void setCombinedAmount(String combinedAmount) {
		this.combinedAmount = combinedAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSavings() {
		return savings;
	}

	public void setSavings(boolean savings) {
		this.savings = savings;
	}

	public char getCredit() {
		return credit;
	}

	public void setCredit(char credit) {
		this.credit = credit;
	}

	public char getCreditPaid() {
		return creditPaid;
	}

	public void setCreditPaid(char creditPaid) {
		this.creditPaid = creditPaid;
	}
}

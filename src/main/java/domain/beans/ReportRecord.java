package domain.beans;

public class ReportRecord {

	private int ID;
	private String title;
	private String type;
	private String category;
	private String date;
	private double amount;
	private double combinedAmount;
	private String description;
	
	public ReportRecord() {
		
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public double getCombinedAmount() {
		return combinedAmount;
	}

	public void setCombinedAmount(double combinedAmount) {
		this.combinedAmount = combinedAmount;
	}
}

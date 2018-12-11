package domain.dto;

public class CategorySummary {

	private String category;
	private double amount;
	
	public CategorySummary(String category, double amt) {
		this.category = category;
		this.amount = amt;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
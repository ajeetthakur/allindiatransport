package com.tpnagar.wrapper;

public class RatingWrapper{
	private String email;
	private String customerName;
	private String phone;
	private String rating;
	private String review;

	public String getDatetext() {
		return datetext;
	}

	public void setDatetext(String datetext) {
		this.datetext = datetext;
	}

	private String datetext;

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}

	public String getCustomerName(){
		return customerName;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setRating(String rating){
		this.rating = rating;
	}

	public String getRating(){
		return rating;
	}

	public void setReview(String review){
		this.review = review;
	}

	public String getReview(){
		return review;
	}
}

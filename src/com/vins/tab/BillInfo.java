package com.vins.tab;

public class BillInfo {

	public String description;
	public String billAmount;

	public BillInfo(String _description, String _billAmount) {
		super();
		this.description = _description;
		this.billAmount = _billAmount;
	}

	public String getDescription() {
		return description;
	}

	public String getBillAmount() {
		return billAmount;
	}

}

package com.vins.tab;

public class FriendsInfo {

	public String userName;
	public String userEmail;
	public float owesYou;
	public float youOwe;

	public FriendsInfo(String userName, String userEmail) {
		super();
		this.userName = userName;
		this.userEmail = userEmail;
	}
	
	public FriendsInfo(String _userName, float _owesYou, float _youOwe) {
		super();
		this.userName = _userName;
		this.owesYou = _owesYou;
		this.youOwe = _youOwe;
	}

	public String getUserName(){
		return userName;
	}
	
	public String getUserEmail(){
		return userEmail;
	}
	
	public float getHowMuchYouOwe() {
		return youOwe;
	}
	
	public float getHowMuchHeOwesYou() {
		return owesYou;
	}
}

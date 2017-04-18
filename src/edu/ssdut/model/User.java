package edu.ssdut.model;

public class User {

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof User))
			return false;
		User that = (User)o;
		return this.username.equals(that.username);
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", pwd=" + pwd + "]";
	}

	private String username;
	private String pwd;
}

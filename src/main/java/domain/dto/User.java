package domain.dto;

import literals.ApplicationLiterals;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = -7868034600250209563L;

	private String username;
	private String email;
	private String password;
	private char permission;
	private String lastLogin;
	private String status;

	public User() {
		this.permission = ApplicationLiterals.VIEW_ONLY;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public char getPermission() {
		return permission;
	}

	public void setPermission(char permission) {
		this.permission = permission;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

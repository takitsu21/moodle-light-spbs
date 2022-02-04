package fr.uca.springbootstrap.payload.request;

import javax.validation.constraints.NotBlank;

public class AddUserRequest {
	@NotBlank
	private String username;

	@NotBlank
	private Long moduleid;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getModuleid() {
		return moduleid;
	}

	public void setModuleid(Long moduleid) {
		this.moduleid = moduleid;
	}
}

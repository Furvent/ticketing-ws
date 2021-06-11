package fr.eql.ticketing.controller.form;

public class UserForm {
	private String oldPassword;
	private String newPassword;
	private String pseudo;
	private String passwordConfirmation;
	private String errorMessage;

	public UserForm() {
		
	}
	public UserForm(String pseudo, String oldPassword, String newPassword, String passwordConfirmation,
			String errorMessage) {
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		this.pseudo = pseudo;
		this.passwordConfirmation = passwordConfirmation;
		this.errorMessage = errorMessage;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	@Override
	public String toString() {
		return "UserForm [oldPassword=" + oldPassword + ", newPassword=" + newPassword + ", pseudo=" + pseudo
				+ ", passwordConfirmation=" + passwordConfirmation + ", errorMessage=" + errorMessage + "]";
	}
	
}

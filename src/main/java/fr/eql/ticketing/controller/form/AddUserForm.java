package fr.eql.ticketing.controller.form;

import java.util.List;

public class AddUserForm {
	List<Long> idUserToAdd;

	public List<Long> getIdUserToAdd() {
		return idUserToAdd;
	}

	public void setIdUserToAdd(List<Long> idUserToAdd) {
		this.idUserToAdd = idUserToAdd;
	}

	public AddUserForm() {
	}

	public AddUserForm(List<Long> idUserToAdd) {
		this.idUserToAdd = idUserToAdd;
	}

}

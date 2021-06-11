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

	public AddUserForm(List<Long> idUserToAdd) {
		super();
		this.idUserToAdd = idUserToAdd;
	}

	public AddUserForm() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

package fr.eql.ticketing.controller.form;

import java.util.List;

public class TicketForm {
	private Long idStatus;
	private String title;
	private String description;
	private List<Long> idUsers;
	private List<String> radioButtonsOptions;
	private String radioSelected;

	public Long getIdStatus() {
		return idStatus;
	}

	public void setIdStatus(Long idStatus) {
		this.idStatus = idStatus;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Long> getIdUsers() {
		return idUsers;
	}

	public void setIdUsers(List<Long> idUsers) {
		this.idUsers = idUsers;
	}

	public List<String> getRadioButtonsOptions() {
		return radioButtonsOptions;
	}

	public void setRadioButtonsOptions(List<String> radioButtonsOptions) {
		this.radioButtonsOptions = radioButtonsOptions;
	}

	public String getRadioSelected() {
		return radioSelected;
	}

	public void setRadioSelected(String radioSelected) {
		this.radioSelected = radioSelected;
	}

	@Override
	public String toString() {
		return "TicketForm [idStatus=" + idStatus + ", title=" + title + ", description=" + description + ", idUsers="
				+ idUsers + ", radioButtonsOptions=" + radioButtonsOptions + ", radioSelected=" + radioSelected + "]";
	}

}

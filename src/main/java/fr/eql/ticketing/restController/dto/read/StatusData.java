package fr.eql.ticketing.restController.dto.read;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StatusData {
	private String label;
	private LocalDateTime date;

	public StatusData(String label, LocalDateTime date) {
		this.label = label;
		this.date = date;
	}
}

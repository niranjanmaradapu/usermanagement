package com.otsi.retail.authservice.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class States {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long stateId;
	private String stateCode;
	private String stateName;
	private String capital;
}

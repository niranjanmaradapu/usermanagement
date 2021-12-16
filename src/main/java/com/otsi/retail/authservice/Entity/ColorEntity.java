package com.otsi.retail.authservice.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ColorEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;
	private String colorCode;

}

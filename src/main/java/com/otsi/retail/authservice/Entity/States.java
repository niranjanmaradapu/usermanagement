<<<<<<< HEAD
package com.otsi.retail.authservice.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class States extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long stateId;
	private String stateCode;
	private String stateName;
	private String capital;
}
=======
package com.otsi.retail.authservice.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class States extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Long stateId;
	
	private String stateCode;
	
	private String stateName;
	
	private String capital;
}
>>>>>>> alpha-release

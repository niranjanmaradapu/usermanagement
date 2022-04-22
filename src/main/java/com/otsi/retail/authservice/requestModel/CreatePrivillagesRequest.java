package com.otsi.retail.authservice.requestModel;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePrivillagesRequest {

	private ParentPrivilageVo parentPrivillage;
	private List<SubPrivillagesvo> subPrivillages;
}

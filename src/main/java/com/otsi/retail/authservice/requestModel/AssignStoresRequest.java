package com.otsi.retail.authservice.requestModel;

import java.util.List;

import com.otsi.retail.authservice.Entity.Store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignStoresRequest {

	private String userName;
	private List<Store> stores;

	
}

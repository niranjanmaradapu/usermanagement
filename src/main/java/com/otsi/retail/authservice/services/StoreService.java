package com.otsi.retail.authservice.services;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.otsi.retail.authservice.Entity.Store;
import com.otsi.retail.authservice.requestModel.DomianStoresVo;
import com.otsi.retail.authservice.requestModel.GetStoresRequestVo;
import com.otsi.retail.authservice.requestModel.StoreVo;

@Component
public interface StoreService {

	 String createStore(StoreVo vo) throws Exception;
	 List<Store> getStoresForClientDomian(long clientDomianId) throws Exception;
	 List<Store> getStoresForClient(long clientId) throws Exception;
	 String assignStoreToClientDomain(DomianStoresVo vo) throws Exception;
	 List<Store> getStoresOnFilter(GetStoresRequestVo vo);
	 String updateStore(StoreVo vo) throws RuntimeException, Exception;
}
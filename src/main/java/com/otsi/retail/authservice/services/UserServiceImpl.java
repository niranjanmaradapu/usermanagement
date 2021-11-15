package com.otsi.retail.authservice.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.RuntimeCryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesResult;
import com.otsi.retail.authservice.Entity.ClientDomains;
import com.otsi.retail.authservice.Entity.Role;
import com.otsi.retail.authservice.Entity.Store;
import com.otsi.retail.authservice.Entity.UserAv;
import com.otsi.retail.authservice.Entity.UserDeatils;
import com.otsi.retail.authservice.Exceptions.UserNotFoundException;
import com.otsi.retail.authservice.Repository.ClientcDomianRepo;
import com.otsi.retail.authservice.Repository.RoleRepository;
import com.otsi.retail.authservice.Repository.StoreRepo;
import com.otsi.retail.authservice.Repository.UserAvRepo;
import com.otsi.retail.authservice.Repository.UserRepo;
import com.otsi.retail.authservice.requestModel.GetUserRequestModel;
import com.otsi.retail.authservice.requestModel.UpdateUserRequest;
import com.otsi.retail.authservice.responceModel.GetCustomerResponce;
import com.otsi.retail.authservice.utils.CognitoAtributes;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private UserAvRepo userAvRepo;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private ClientcDomianRepo clientcDomianRepo;
	@Autowired
	private StoreRepo storeRepo;
	@Autowired
	private CognitoClient cognitoClient;

	public List<UserDeatils> getUserFromDb(GetUserRequestModel userRequest) throws Exception {

		List<UserDeatils> users = new ArrayList<>();
		if (0l != userRequest.getId()) {
			Optional<UserDeatils> user = userRepo.findById(userRequest.getId());
			if (user.isPresent()) {
				users.add(user.get());

			} else {
				throw new RuntimeException("User not found with this Id : " + userRequest.getId());
			}
		}
		if (null != userRequest.getName()) {
			Optional<UserDeatils> user = userRepo.findByUserName(userRequest.getName());
			if (user.isPresent()) {
				users.add(user.get());

			} else {
				throw new RuntimeException("User not found with this UserName : " + userRequest.getName());
			}
		}
		if (null != userRequest.getPhoneNo()) {
			Optional<UserDeatils> user = userRepo.findByPhoneNumber(userRequest.getPhoneNo());
			if (user.isPresent()) {
				users.add(user.get());
			} else {
				throw new Exception("No user found with this userName: " + userRequest.getPhoneNo());
			}
		}

		if (0L != userRequest.getRoleId()) {
			users = userRepo.findByRoleRoleId(userRequest.getRoleId());
			if (CollectionUtils.isEmpty(users)) {
				throw new RuntimeException("No users found with this Role ID : " + userRequest.getRoleId());
			}
		}

		if (0L != userRequest.getStoreId()) {
			users = userRepo.findByStores_Id(userRequest.getStoreId());
			if (CollectionUtils.isEmpty(users)) {
				throw new RuntimeException("No users found with this Role ID : " + userRequest.getRoleId());
			}
		}

		return users;
	}

	public List<UserDeatils> getUserForClient(int clientId) throws Exception {

		List<UserDeatils> users = userRepo.findByUserAv_NameAndUserAv_IntegerValue(CognitoAtributes.CLIENT_ID,
				clientId);
		if (!CollectionUtils.isEmpty(users)) {
			return users;
		} else {
			throw new Exception("No users found with this client");
		}

	}

	public List<UserDeatils> getUsersForClientDomain(long clientDomianId) {
		List<UserDeatils> users = userRepo.findByClientDomians_ClientDomainaId(clientDomianId);
		if (!CollectionUtils.isEmpty(users)) {
			return users;
		} else {
			throw new UserNotFoundException("User not found with this Domian Id : " + clientDomianId);
		}

	}

	@Override
	public GetCustomerResponce getCustomerbasedOnMobileNumber(String mobileNo) {
		Optional<UserDeatils> user = userRepo.findByPhoneNumberAndIsCustomer(mobileNo, Boolean.TRUE);
		if (user.isPresent()) {
			GetCustomerResponce customer = new GetCustomerResponce();

			customer.setUserId(user.get().getUserId());
			if (null != user.get().getPhoneNumber()) {

				customer.setPhoneNumber(user.get().getPhoneNumber());
			}
			if (null != user.get().getUserName()) {

				customer.setUserName(user.get().getUserName());
			}
			if (null != user.get().getCreatedDate()) {

				customer.setCreatedDate(user.get().getCreatedDate());
			}
			if (null != user.get().getGender()) {

				customer.setGender(user.get().getGender());
			}
			if (null != user.get().getLastModifyedDate()) {

				customer.setLastModifyedDate(user.get().getLastModifyedDate());
			}
			if (true != user.get().isActive()) {

				customer.setActive(user.get().isActive());
			}
			return customer;
		}
		throw new RuntimeException("Customer not found with this mobile Number : " + mobileNo);
	}

	public String updateUser(UpdateUserRequest req) {
		try {
			Optional<UserDeatils> userOptional = userRepo.findById(req.getUserId());
			if (userOptional.isPresent()) {

				UserDeatils userFromDb = userOptional.get();
				userFromDb.setUserId(req.getUserId());
				userFromDb.setUserName(req.getUsername());
				userFromDb.setPhoneNumber(req.getPhoneNumber());
				userFromDb.setGender(req.getGender());
				userFromDb.setLastModifyedDate(LocalDate.now());
				if (null != req.getRole()) {
					Optional<Role> role = roleRepository.findById(req.getRole().getRoleId());
					if (role.isPresent()) {
						userFromDb.setRole(role.get());
					} else {
						throw new RuntimeException(
								"Role not d=found in DB with this Id : " + req.getRole().getRoleId());
					}
				}
				UserDeatils savedUser = userRepo.save(userFromDb);

				userFromDb.getUserAv().stream().forEach(av -> {
					UserAv userAv = av;
					if (userAv.getName().equalsIgnoreCase(CognitoAtributes.EMAIL)) {
						userAv.setStringValue(req.getEmail());
						userAv.setLastModifyedDate(LocalDate.now());
						userAv.setUserData(savedUser);
						userAvRepo.save(userAv);
					}
					if (av.getName().equalsIgnoreCase(CognitoAtributes.PARENTID)) {
						userAv.setIntegerValue(Integer.parseInt(req.getParentId()));
						userAv.setLastModifyedDate(LocalDate.now());
						userAv.setUserData(savedUser);
						userAvRepo.save(userAv);
					}
					if (av.getName().equalsIgnoreCase(CognitoAtributes.ADDRESS)) {
						userAv.setStringValue(req.getAddress());
						userAv.setLastModifyedDate(LocalDate.now());
						userAv.setUserData(savedUser);
						userAvRepo.save(userAv);
					}

					if (av.getName().equalsIgnoreCase(CognitoAtributes.DOMAINID)) {
						userAv.setIntegerValue(Integer.parseInt(req.getDomianId()));
						userAv.setLastModifyedDate(LocalDate.now());
						userAv.setUserData(savedUser);
						userAvRepo.save(userAv);

					}
					if (av.getName().equalsIgnoreCase(CognitoAtributes.CLIENT_ID)) {
						userAv.setIntegerValue(Integer.parseInt(req.getClientId()));
						userAv.setLastModifyedDate(LocalDate.now());
						userAv.setUserData(savedUser);
						userAvRepo.save(userAv);

					}
				});

				if (null != req.getChannelId()) {
					List<ClientDomains> clientDomains = new ArrayList<>();
					Arrays.asList(req.getClientDomain()).stream().forEach(clientDomianId -> {
						Optional<ClientDomains> dbClientDomainRecord = clientcDomianRepo
								.findById(Long.parseLong(clientDomianId.toString()));

						if (dbClientDomainRecord.isPresent()) {
							clientDomains.add(dbClientDomainRecord.get());
						} else {
							throw new RuntimeException("Client Domian not found with this Id : " + clientDomianId);
						}
					});
					savedUser.setClientDomians(clientDomains);
					userRepo.save(savedUser);
				}

				if (!CollectionUtils.isEmpty(req.getStores())) {
					List<Store> stores = new ArrayList<>();
					req.getStores().stream().forEach(storeVo -> {
						Optional<Store> storeOptional = storeRepo.findByName(storeVo.getName());
						if (storeOptional.isPresent()) {
							stores.add(storeOptional.get());
						}
					});
					savedUser.setStores(stores);
					userRepo.save(savedUser);
				}
				AdminUpdateUserAttributesResult result = cognitoClient.updateUserInCognito(req);
				if (result.getSdkHttpMetadata().getHttpStatusCode() == 200) {
					return "SucessFully updated";
				} else {
					throw new RuntimeException("Failed to update");
				}
			} else {
				throw new RuntimeException("User not found with this Id :" + req.getUserId());
			}

		} catch (RuntimeException re) {
			throw new RuntimeException(re.getMessage());
		}
	}
}
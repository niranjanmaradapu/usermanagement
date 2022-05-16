package com.otsi.retail.authservice.requestModel;

import java.time.LocalDate;
import java.util.List;

import com.otsi.retail.authservice.Entity.Role;
import com.otsi.retail.authservice.Entity.UserAv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsVO {

	private Long id;

	private String userName;

	private String phoneNumber;

	private String gender;

	private LocalDate createdDate;

	private LocalDate lastModifyedDate;

	private String createdBy;

	private Role role;

	private List<UserAv> userAv;

	private List<StoreVO> stores;

	private StoreVO ownerOf;

}
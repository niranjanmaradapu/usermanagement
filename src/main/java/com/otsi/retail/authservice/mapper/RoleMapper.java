package com.otsi.retail.authservice.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.otsi.retail.authservice.Entity.Role;
import com.otsi.retail.authservice.Entity.SubPrivillage;
import com.otsi.retail.authservice.Repository.UserRepo;
import com.otsi.retail.authservice.requestModel.ParentPrivilegesVo;
import com.otsi.retail.authservice.requestModel.RoleVo;
import com.otsi.retail.authservice.requestModel.SubPrivillageVo;
@Component
public class RoleMapper {
	@Autowired
	private UserRepo userRepo;

	public RoleVo convertEntityToRoleVo(Role role) {

		RoleVo vo = new RoleVo();

		vo.setRoleName(role.getRoleName());
		vo.setCreatedDate(role.getCreatedDate());
		vo.setModifiedBy(role.getModifiedBy());
		vo.setDiscription(role.getDiscription());
		vo.setRoleId(role.getRoleId());
		vo.setLastModifyedDate(role.getLastModifyedDate());
		vo.setCreatedBy(role.getCreatedBy());
		vo.setActive(role.isActive());
		vo.setUsersCount(userRepo.countByRoleRoleId(role.getRoleId()));
		role.getParentPrivilages().stream().forEach(p -> {
			ParentPrivilegesVo pvo = new ParentPrivilegesVo();
			List<ParentPrivilegesVo> plvo = new ArrayList<>();

			pvo.setId(p.getId());
			pvo.setName(p.getName());
			pvo.setDiscription(p.getDiscription());
			pvo.setPath(p.getPath());
			pvo.setParentImage(p.getParentImage());
			plvo.add(pvo);
			vo.setParentPrivilageVo(plvo);
		});

		role.getSubPrivilages().stream().forEach(s -> {
			SubPrivillageVo svo = new SubPrivillageVo();
			List<SubPrivillageVo> slvo = new ArrayList<>();

			svo.setId(s.getId());
			svo.setName(s.getName());
			svo.setDescription(s.getDescription());
			svo.setChildPath(s.getChildPath());
			svo.setChildImage(s.getChildImage());
			svo.setParentPrivillageId(s.getParentPrivillageId());
			slvo.add(svo);
			vo.setSubPrivilageVo(slvo);

		});

		return vo;

	}

}

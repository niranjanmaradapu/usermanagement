package com.otsi.retail.authservice.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.authservice.Entity.ParentPrivilages;

@Repository
public interface PrivilageRepo extends JpaRepository<ParentPrivilages, Long> {

	List<ParentPrivilages> findByDomian(int domian);

}

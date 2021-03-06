package com.otsi.retail.authservice.controller;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.retail.authservice.Entity.ColorEntity;
import com.otsi.retail.authservice.requestModel.ColorCodeVo;
import com.otsi.retail.authservice.requestModel.ReportVo;
import com.otsi.retail.authservice.services.ReportsService;
import com.otsi.retail.authservice.utils.EndpointConstants;
import com.otsi.retail.authservice.utils.GateWayResponse;


@RestController
@RequestMapping(EndpointConstants.REPORTS)
public class ReportsController {

	private Logger logger = LogManager.getLogger(ReportsController.class);
	@Autowired
	private ReportsService reportsService;

	@GetMapping(path = EndpointConstants.USERS_BY_ROLE)
	public GateWayResponse<?> UsersByRole(@RequestParam Long clientId) {
		try {
			logger.info("usersByRole method starts");
			List<ReportVo> result = reportsService.getUsersByRole(clientId);
			return new GateWayResponse<>(200, result, "", "true");

		} catch (Exception e) {

			return new GateWayResponse<>(400, null, e.getMessage(), "false");
		}

	}

	@GetMapping(path = EndpointConstants.ACTIVE_VS_INACTIVE_USERS)
	public GateWayResponse<?> ActiveUsers(@RequestParam Long clientId) {

		try {

			logger.info("usersByRole method starts");
			List<ReportVo> vo = reportsService.getActiveUsers(clientId);
			return new GateWayResponse<>(200, vo, "", "true");

		} catch (Exception e) {
			return new GateWayResponse<>(400, null, e.getMessage(), "false");

		}

	}

	@GetMapping(path = EndpointConstants.STORES_VS_EMPLOYEES)
	public GateWayResponse<?> StoresVsEmployees(@RequestParam Long clientId) {

		try {

			logger.info("usersByRole method starts");
			List<ReportVo> count = reportsService.StoresVsEmployees(clientId);
			return new GateWayResponse<>(200, count, "", "true");

		} catch (Exception e) {

			return new GateWayResponse<>(400, null, e.getMessage(), "false");
		}

	}
	
	

}

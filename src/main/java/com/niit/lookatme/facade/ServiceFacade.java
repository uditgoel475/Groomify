package com.niit.lookatme.facade;

import java.util.Map;
import java.util.Set;

import com.niit.lookatme.dto.ServiceDTO;
import com.niit.lookatme.dto.ServiceExtDTO;

public interface ServiceFacade {

	Boolean createService(ServiceExtDTO serviceDTO);

	Boolean createNewServiceType(String serviceType);

	Boolean deactivateService(ServiceDTO serviceDTO);

	Boolean activateService(ServiceDTO serviceDTO);

	Boolean updateService(ServiceDTO currentInfo, ServiceExtDTO newInfo);

	Map<String, Set<ServiceExtDTO>> findAllActiveServices();

}

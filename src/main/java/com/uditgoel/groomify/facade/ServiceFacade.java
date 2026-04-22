package com.uditgoel.groomify.facade;

import java.util.Map;
import java.util.Set;

import com.uditgoel.groomify.dto.ServiceDTO;
import com.uditgoel.groomify.dto.ServiceExtDTO;

public interface ServiceFacade {

	Boolean createService(ServiceExtDTO serviceDTO);

	Boolean createNewServiceType(String serviceType);

	Boolean deactivateService(ServiceDTO serviceDTO);

	Boolean activateService(ServiceDTO serviceDTO);

	Boolean updateService(ServiceDTO currentInfo, ServiceExtDTO newInfo);

	Map<String, Set<ServiceExtDTO>> findAllActiveServices();

}

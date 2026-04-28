package com.uditgoel.groomify.facade.impl;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.uditgoel.groomify.dao.repository.ServiceRepository;
import com.uditgoel.groomify.dao.repository.ServiceTypeRepository;
import com.uditgoel.groomify.dao.services.Service;
import com.uditgoel.groomify.dao.services.ServiceType;
import com.uditgoel.groomify.dto.ServiceDTO;
import com.uditgoel.groomify.dto.ServiceExtDTO;
import com.uditgoel.groomify.exception.AlreadyExistsException;
import com.uditgoel.groomify.exception.ResourceNotFoundException;
import com.uditgoel.groomify.facade.ServiceFacade;
import com.uditgoel.groomify.utils.Converter;
import com.uditgoel.groomify.utils.CustomerAndEmployeeUtils;

@org.springframework.stereotype.Service("serviceFacade")
@Transactional
public class ServiceFacadeImpl implements ServiceFacade {

	private final ServiceRepository serviceRepository;

	private final ServiceTypeRepository serviceTypeRepository;

	public ServiceFacadeImpl(ServiceRepository serviceRepository, ServiceTypeRepository serviceTypeRepository) {
		this.serviceRepository = serviceRepository;
		this.serviceTypeRepository = serviceTypeRepository;
	}

	@Override
	public Boolean createService(ServiceExtDTO serviceDTO) {

		if (serviceRepository.existsByNameHsnType(serviceDTO.getName(), serviceDTO.getHsn(),
				serviceDTO.getServiceType())) {
			throw new AlreadyExistsException(String.format(
					"Service with name : '%s' and hsn/sac code : '%s' in Service type '%s' already exists",
					serviceDTO.getName(), serviceDTO.getHsn(), serviceDTO.getServiceType()));
		}

		ServiceType serviceType = findServiceTypeByName(serviceDTO.getServiceType());

		Service service = new Service();
		service.setServiceGroup(serviceType);
		service.setHsn(serviceDTO.getHsn());
		service.setTime(Converter.localTimeToDate(serviceDTO.getServiceTime()));
		service.setPrice(serviceDTO.getPrice());
		service.setIsActive(serviceDTO.getActive());
		service.setName(serviceDTO.getName());

		serviceRepository.save(service);
		return true;
	}

	@Override
	public Map<String, Set<ServiceExtDTO>> findAllActiveServices() {
		return serviceRepository.findAllActiveServices().stream().filter(Objects::nonNull)
				.map(CustomerAndEmployeeUtils::populateServiceExtDTO)
				.collect(Collectors.groupingBy(ServiceExtDTO::getServiceType, TreeMap::new, Collectors.toSet()));
	}

	private ServiceType findServiceTypeByName(String serviceTypeStr) {
		return serviceTypeRepository.findByName(serviceTypeStr)
				.orElseThrow(() -> new ResourceNotFoundException("Service Type", "name", serviceTypeStr));
	}

	@Override
	public Boolean createNewServiceType(String serviceType) {
		if (serviceTypeRepository.existsByName(serviceType)) {
			throw new AlreadyExistsException("Service Type", serviceType);
		}
		serviceTypeRepository.save(new ServiceType(serviceType));
		return true;
	}

	@Override
	public Boolean deactivateService(ServiceDTO serviceDTO) {
		Service service = serviceRepository
				.findActiveByNameHsnType(serviceDTO.getName(), serviceDTO.getHsn(), serviceDTO.getServiceType())
				.orElseThrow(() -> new ResourceNotFoundException("Service is already deactive or not found"));
		service.setIsActive(false);
		serviceRepository.save(service);
		return true;
	}

	@Override
	public Boolean activateService(ServiceDTO serviceDTO) {
		Service service = serviceRepository
				.findInactiveByNameHsnType(serviceDTO.getName(), serviceDTO.getHsn(), serviceDTO.getServiceType())
				.orElseThrow(() -> new ResourceNotFoundException("Service is already active or not found"));
		service.setIsActive(true);
		serviceRepository.save(service);
		return true;
	}

	private Object updateValue(Object currentValue, Object newValue) {
		if (newValue == null)
			return currentValue;
		if (newValue instanceof String) {
			return (StringUtils.isEmpty((String) newValue)) ? currentValue : newValue;
		}
		return newValue;
	}

	@Override
	public Boolean updateService(ServiceDTO currentInfo, ServiceExtDTO newInfo) {
		Service service = serviceRepository
				.findByNameHsnType(currentInfo.getName(), currentInfo.getHsn(), currentInfo.getServiceType())
				.orElseThrow(() -> new ResourceNotFoundException(String.format(
						"Service with given name : '%s' and hsn/sac code : '%s' in Service type '%s' not found",
						currentInfo.getName(), currentInfo.getHsn(), currentInfo.getServiceType())));

		Optional<String> serviceTypeOptional = Optional.ofNullable(newInfo).map(ServiceExtDTO::getServiceType)
				.filter(Objects::nonNull);
		ServiceType newServiceType = null;
		if (serviceTypeOptional.isPresent() && !service.getServiceGroup().getName().equals(serviceTypeOptional.get())) {
			newServiceType = findServiceTypeByName(serviceTypeOptional.get());
		}

		Service updateService = new Service();
		updateService.setServiceGroup((ServiceType) updateValue(service.getServiceGroup(), newServiceType));
		updateService.setHsn((String) updateValue(service.getHsn(), newInfo.getHsn()));
		updateService
				.setTime((Date) updateValue(service.getTime(), Converter.localTimeToDate(newInfo.getServiceTime())));
		updateService.setPrice((Double) updateValue(service.getPrice(), newInfo.getPrice()));
		updateService.setIsActive((Boolean) updateValue(service.getIsActive(), newInfo.getActive()));
		updateService.setName((String) updateValue(service.getName(), newInfo.getName()));

		if (updateService.equals(service)) {
			throw new IllegalArgumentException("No different value is given for the Service update.");
		}

		serviceRepository
				.findByNameHsnType(updateService.getName(), updateService.getHsn(),
						updateService.getServiceGroup().getName())
				.map(Service::getId).filter(x -> x.equals(service.getId())).ifPresent(val -> {
					throw new AlreadyExistsException(String.format(
							"Service with name : '%s' and hsn/sac code : '%s' in Service type '%s' already exists",
							newInfo.getName(), newInfo.getHsn(), newInfo.getServiceType()));
				});

		serviceRepository.save(updateService);
		return true;
	}
}

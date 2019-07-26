package com.niit.lookatme.dao.repository.impl;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.niit.lookatme.dao.repository.CustomerRepository;

public class CustomerRepositoryImpl<Customer, ID extends Serializable> extends SimpleJpaRepository<Customer, ID>
		implements CustomerRepository<Customer, ID> {

	private EntityManager entityManager;

	public CustomerRepositoryImpl(JpaEntityInformation<Customer, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	@Transactional
	@Override
	public List<Customer> findCustomersByBirthdayCurrentWeek() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Customer> cQuery = builder.createQuery(getDomainClass());
		Root<Customer> root = cQuery.from(getDomainClass());
		LocalDate startDate = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
		LocalDate endDate = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
		cQuery.select(root)
				.where(builder.between(root.<Date>get("DOB"),
						Date.from(startDate.atStartOfDay()
							      .atZone(ZoneId.systemDefault())
							      .toInstant()),
						Date.from(endDate.atStartOfDay()
							      .atZone(ZoneId.systemDefault())
							      .toInstant())
						));
		TypedQuery<Customer> query = entityManager.createQuery(cQuery);
		return query.getResultList();
	}

}

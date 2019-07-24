package com.niit.lookatme.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.niit.lookatme.utils.Converter;

@Component
@Aspect
public class Logging {

	private static final Logger LOGGER = LoggerFactory.getLogger(Logging.class);

	@Around("execution(public * com.niit.lookatme.utils..*(..))")
	public Object controllerInfo(ProceedingJoinPoint pjp) throws Throwable {
		Object object = pjp.proceed();
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(pjp.getSignature().toLongString());
			LOGGER.info(Converter.object2Json(object));
		}
		return object;
	}
}
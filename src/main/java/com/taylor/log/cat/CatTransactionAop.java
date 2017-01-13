package com.taylor.log.cat;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;

@Aspect
public class CatTransactionAop {

	private static final Logger LOGGER = Logger.getLogger(CatTransactionAop.class);
	
	/**
	 *@description aop注解到方法的方式记录cat 
	 *@param joinPoint
	 *@param catTransaction
	 *@return
	 *@throws Throwable
	 */
	@Around(value="execution(* *(..)) && @annotation(catTransaction)")
	public Object annotationMethodAround(ProceedingJoinPoint joinPoint, CatTransaction catTransaction) throws Throwable{
		return around(joinPoint);
	}
	
	/**
	 *@description aop注解到类的方式记录cat 
	 *@param joinPoint
	 *@return
	 *@throws Throwable
	 */
	@Around("within(@com.taylor.log.cat.CatTransaction *)")
	public Object annotationClassAround(ProceedingJoinPoint joinPoint) throws Throwable{
		return around(joinPoint);
	}
	
	/**
	 *@description xml aop方式记录cat 
	 *@param joinPoint
	 *@return
	 *@throws Throwable
	 */
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
		//不用获取domain，初始化时设置了domain
		Transaction transaction = null;
		try {
			String className = joinPoint.getTarget().getClass().getSimpleName();
			String methodName = joinPoint.getSignature().getName();
			String name = className + "." + methodName;
			transaction = Cat.newTransaction(className, name);
			Object[] params = joinPoint.getArgs();
			if(params !=null && params.length > 0){
				for(Object param : params ){
					Cat.logEvent(methodName+".Param", JSON.toJSONString(param));
				}
			}
		} catch (Exception e) {
			LOGGER.error("CatTransactionAop create transaction exception", e);
		}
		Object returnObject = null;
		Throwable throwable = null;
		try {
			returnObject = joinPoint.proceed();
		} catch (Throwable t) {
			throwable = t;
			Cat.logError(t);
		}
		try {
			if(transaction != null){
				if (throwable != null) {
					transaction.setStatus(throwable);
				}else {
					transaction.setStatus(Transaction.SUCCESS);
				}
				transaction.complete();
			}
		} catch (Throwable t) {
			LOGGER.error("CatTransactionAop complete transaction exception", t);
		}
		if(throwable != null){
			throw throwable;
		}
		return returnObject;
	}
	
}

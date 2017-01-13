package com.taylor.log.cat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;

public class CatInterceptor implements HandlerInterceptor {

	private ThreadLocal<Transaction> tranLocal = new ThreadLocal<Transaction>();
	private ThreadLocal<Transaction> pageLocal = new ThreadLocal<Transaction>();

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();
		Transaction t = Cat.newTransaction("URL", uri);
		Cat.logEvent("URL.Method", request.getMethod(), Message.SUCCESS,
				request.getRequestURL().toString());
		Cat.logEvent("URL.Host", request.getMethod(), Message.SUCCESS,
				request.getRemoteHost());
		tranLocal.set(t);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String viewName = modelAndView != null ? modelAndView.getViewName()
				: "无";
		Transaction t = Cat.newTransaction("View", viewName);
		pageLocal.set(t);
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		Transaction pt = pageLocal.get();
		if(ex != null) {
			pt.setStatus(ex);
		} else {
			pt.setStatus(Transaction.SUCCESS);
		}
		pt.complete();
		// 总计
		Transaction t = tranLocal.get();
		if (ex != null) {
			t.setStatus(ex);
		} else {
			t.setStatus(Transaction.SUCCESS);
		}
		t.complete();
	}

}

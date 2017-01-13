package com.taylor.log.cat;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.dianping.cat.Cat;
import com.dianping.cat.Cat.Context;
import com.dianping.cat.message.Transaction;

@Activate(group={Constants.PROVIDER,Constants.CONSUMER})
public class CatDubboFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation)
			throws RpcException {
		Result result = null;
		String role = invoker.getUrl().getParameter(Constants.SIDE_KEY);
		String loggerName = invoker.getInterface().getName() + "."
				+ invocation.getMethodName();
		Transaction tr = Cat.newTransaction("services", loggerName);
		CatDubboContext catContext = new CatDubboContext();
		if (Constants.CONSUMER.equals(role)) {// consumer
			Cat.logRemoteCallClient(catContext);
			setRpcAttachment(catContext);
		} else if (Constants.PROVIDER.equals(role)) {// provider
			getRpcAttachment(catContext, invocation);
			Cat.logRemoteCallServer(catContext);
		}
		try {
            result = invoker.invoke(invocation);
            tr.setStatus(Transaction.SUCCESS);
        } catch(Exception e) {
            tr.setStatus("ERROR");
            Cat.logError(e);
			throw new RpcException(e);
        } finally {
            tr.complete();
        }
		return result;
	}

	private void setRpcAttachment(Cat.Context context) {
		RpcContext.getContext().setAttachment(Cat.Context.PARENT,
				context.getProperty(Cat.Context.PARENT));
		RpcContext.getContext().setAttachment(Cat.Context.CHILD,
				context.getProperty(Cat.Context.CHILD));
		RpcContext.getContext().setAttachment(Cat.Context.ROOT,
				context.getProperty(Cat.Context.ROOT));
	}

	private void getRpcAttachment(Cat.Context catContext, Invocation invocation) {
		catContext.addProperty(Context.ROOT,
				invocation.getAttachment(Context.ROOT));
		catContext.addProperty(Context.PARENT,
				invocation.getAttachment(Context.PARENT));
		catContext.addProperty(Context.CHILD,
				invocation.getAttachment(Context.CHILD));
	}
}

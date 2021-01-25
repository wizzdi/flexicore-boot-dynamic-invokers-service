package com.wizzdi.flexicore.boot.dynamic.invokers.service;

import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.Invoker;
import com.wizzdi.flexicore.boot.dynamic.invokers.interfaces.ExecutionContext;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicInvokerFilter;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.ExecuteInvokerRequest;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.ExecuteInvokerResponse;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.ExecuteInvokersResponse;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerInfo;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.pf4j.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Extension
@Service
public class DynamicInvokerService implements Plugin {

	private static final Logger logger= LoggerFactory.getLogger(DynamicInvokerService.class);

	@Autowired
	private List<InvokerInfo> invokerInfos;

	@Autowired
	private PluginManager pluginManager;




	public void validate(DynamicInvokerFilter dynamicInvokerFilter, SecurityContextBase securityContext) {

	}

	public PaginationResponse<InvokerInfo> getAllDynamicInvokers(DynamicInvokerFilter dynamicInvokerFilter, SecurityContextBase securityContext) {
		List<InvokerInfo> list = listAllDynamicInvokers(dynamicInvokerFilter,securityContext);
		long count= countAllDynamicInvokers(dynamicInvokerFilter,securityContext);
		return new PaginationResponse<>(list,dynamicInvokerFilter.getPageSize(),count);
	}

	private long countAllDynamicInvokers(DynamicInvokerFilter dynamicInvokerFilter,SecurityContextBase securityContextBase) {
		return invokerInfos.stream().filter(f -> filter(f, dynamicInvokerFilter)).count();
	}

	public List<InvokerInfo> listAllDynamicInvokers(DynamicInvokerFilter dynamicInvokerFilter,SecurityContextBase securityContextBase) {
		return paginate(invokerInfos.stream().filter(f -> filter(f, dynamicInvokerFilter)), dynamicInvokerFilter).collect(Collectors.toList());
	}

	private Stream<InvokerInfo> paginate(Stream<InvokerInfo> invokerInfoStream,DynamicInvokerFilter dynamicInvokerFilter) {
		if(dynamicInvokerFilter.getCurrentPage()!=null&&dynamicInvokerFilter.getCurrentPage()>-1&&dynamicInvokerFilter.getPageSize()!=null&&dynamicInvokerFilter.getPageSize()>0){
			return invokerInfoStream.skip((long) dynamicInvokerFilter.getPageSize() *dynamicInvokerFilter.getCurrentPage()).limit(dynamicInvokerFilter.getPageSize());
		}
		return invokerInfoStream;
	}

	private boolean filter(InvokerInfo f, DynamicInvokerFilter dynamicInvokerFilter) {
		boolean pred=true;
		if(dynamicInvokerFilter.getNameLike()!=null){
			pred=pred&&(f.getDisplayName().contains(dynamicInvokerFilter.getNameLike()) ||f.getDescription().contains(dynamicInvokerFilter.getNameLike()) );
		}
		if(dynamicInvokerFilter.getMethodNameLike()!=null){
			pred=pred&&f.getMethods().stream().map(e->e.getRelatedMethodNames()).anyMatch(e->e.contains(dynamicInvokerFilter.getMethodNameLike()));
		}
		if(dynamicInvokerFilter.getInvokerTypes()!=null&&!dynamicInvokerFilter.getInvokerTypes().isEmpty()){
			pred=pred&&dynamicInvokerFilter.getInvokerTypes().contains(f.getName().getCanonicalName());
		}


		return pred;
	}

	public ExecuteInvokersResponse executeInvoker(ExecuteInvokerRequest executeInvokerRequest, SecurityContextBase securityContext) {
		Collection<Invoker> plugins = pluginManager.getExtensions(Invoker.class);
		Map<String, Invoker> invokerMap = plugins.parallelStream().collect(Collectors.toMap(f -> f.getClass().getCanonicalName(), f -> f, (a, b) -> a));

		List<ExecuteInvokerResponse<?>> responses = new ArrayList<>();
		Object executionParametersHolder = executeInvokerRequest.getExecutionParametersHolder();
		ExecutionContext executionContext = executeInvokerRequest.getExecutionContext();

		for (String invokerName : executeInvokerRequest.getInvokerNames()) {

			try {
				Invoker invoker = invokerMap.get(invokerName);
				if (invoker == null) {
					String msg = "No Handler " + invokerName;
					logger.error(msg);
					responses.add(new ExecuteInvokerResponse<>(invokerName, false, msg));
					continue;
				}
				Class<? extends Invoker> clazz = invoker.getClass();

				Method[] methods = clazz.getMethods();
				for (Method method : methods) {
					if (method.isBridge()) {
						continue;
					}
					Class<?>[] parameterTypes = method.getParameterTypes();

					if (method.getName().equals(executeInvokerRequest.getInvokerMethodName()) && parameterTypes.length > 0 && parameterTypes[0].isAssignableFrom(executionParametersHolder.getClass())) {

						Object[] parameters = new Object[parameterTypes.length];
						parameters[0] = executionParametersHolder;
						for (int i = 1; i < parameterTypes.length; i++) {
							Class<?> parameterType = parameterTypes[i];
							if (SecurityContextBase.class.isAssignableFrom(parameterType)) {
								parameters[i] = securityContext;
							}
							if (executionContext != null && parameterType.isAssignableFrom(executionContext.getClass())) {
								parameters[i] = executionContext;
							}
						}
						Object ret = method.invoke(invoker, parameters);
						ExecuteInvokerResponse<?> e=new ExecuteInvokerResponse<>(invokerName, true, ret);
						responses.add(e);
						break;

					}
				}
			} catch (Exception e) {
				logger.error( "failed executing " + invokerName, e);
				responses.add(new ExecuteInvokerResponse<>(invokerName, false, e));
			}

		}


		return new ExecuteInvokersResponse(responses);


	}


}

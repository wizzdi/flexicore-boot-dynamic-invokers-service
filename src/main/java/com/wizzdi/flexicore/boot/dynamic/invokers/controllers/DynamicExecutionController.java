package com.wizzdi.flexicore.boot.dynamic.invokers.controllers;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicExecutionCreate;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicExecutionExampleRequest;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicExecutionFilter;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicExecutionUpdate;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.DynamicExecutionService;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@OperationsInside
@RequestMapping("/dynamicExecution")
@Extension
public class DynamicExecutionController implements Plugin {

	@Autowired
	private DynamicExecutionService dynamicExecutionService;

	@IOperation(Name = "creates dynamicExecution",Description = "creates dynamicExecution")
	@PostMapping("/create")
	public DynamicExecution create(@RequestBody DynamicExecutionCreate dynamicExecutionCreate, @RequestAttribute SecurityContextBase securityContext){
		dynamicExecutionService.validateCreate(dynamicExecutionCreate,securityContext);
		return dynamicExecutionService.createDynamicExecution(dynamicExecutionCreate,securityContext);
	}

	@IOperation(Name = "returns dynamicExecution",Description = "returns dynamicExecution")
	@PostMapping("/getAll")
	public PaginationResponse<DynamicExecution> getAll(@RequestBody DynamicExecutionFilter dynamicExecutionFilter, @RequestAttribute SecurityContextBase securityContext){
		dynamicExecutionService.validate(dynamicExecutionFilter,securityContext);
		return dynamicExecutionService.getAllDynamicExecutions(dynamicExecutionFilter,securityContext);
	}

	@IOperation(Name = "returns example for the dynamic execution",Description = "returns example for the dynamic execution")
	@PostMapping("/getDynamicExecutionReturnExample")
	public Object getDynamicExecutionReturnExample(@RequestBody DynamicExecutionExampleRequest dynamicExecutionExampleRequest, @RequestAttribute SecurityContextBase securityContext){
		dynamicExecutionService.validate(dynamicExecutionExampleRequest,securityContext);
		return dynamicExecutionService.getExample(dynamicExecutionExampleRequest.getClazz());
	}

	@IOperation(Name = "updates dynamicExecution",Description = "updates dynamicExecution")
	@PutMapping("/update")
	public DynamicExecution update(@RequestBody DynamicExecutionUpdate dynamicExecutionUpdate, @RequestAttribute SecurityContextBase securityContext){
		String id=dynamicExecutionUpdate.getId();
		DynamicExecution dynamicExecution=id!=null?dynamicExecutionService.getByIdOrNull(id,DynamicExecution.class,securityContext):null;
		if(dynamicExecution==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no Dynamic Execution with id "+id);
		}
		dynamicExecutionUpdate.setDynamicExecution(dynamicExecution);
		dynamicExecutionService.validate(dynamicExecutionUpdate,securityContext);
		return dynamicExecutionService.updateDynamicExecution(dynamicExecutionUpdate,securityContext);
	}
}

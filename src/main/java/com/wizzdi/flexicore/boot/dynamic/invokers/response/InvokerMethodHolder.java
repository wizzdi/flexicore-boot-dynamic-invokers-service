package com.wizzdi.flexicore.boot.dynamic.invokers.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Set;

public class InvokerMethodHolder {

    private final InvokerMethodInfo invokerMethodHolder;
    private String invokerName;

    public InvokerMethodHolder(String invokerName,InvokerMethodInfo invokerMethodHolder) {
        this.invokerMethodHolder = invokerMethodHolder;
        this.invokerName=invokerName;
    }

    public InvokerMethodHolder() {
        this.invokerMethodHolder=new InvokerMethodInfo();
    }


    public void addParameterInfo(ParameterInfo parameterInfo) {
        invokerMethodHolder.addParameterInfo(parameterInfo);
    }

    public String getName() {
        return invokerMethodHolder.getName();
    }

    public InvokerMethodInfo setName(String name) {
        return invokerMethodHolder.setName(name);
    }

    public String getDescription() {
        return invokerMethodHolder.getDescription();
    }

    public InvokerMethodInfo setDescription(String description) {
        return invokerMethodHolder.setDescription(description);
    }

    public String getDisplayName() {
        return invokerMethodHolder.getDisplayName();
    }

    public InvokerMethodInfo setDisplayName(String displayName) {
        return invokerMethodHolder.setDisplayName(displayName);
    }

    public String getReturnType() {
        return invokerMethodHolder.getReturnType();
    }

    @JsonIgnore
    public Class<?> getReturnTypeClass() {
        return invokerMethodHolder.getReturnTypeClass();
    }

    public List<ParameterInfo> getParameters() {
        return invokerMethodHolder.getParameters();
    }

    public InvokerMethodInfo setParameters(List<ParameterInfo> parameters) {
        return invokerMethodHolder.setParameters(parameters);
    }

    public String getParameterHolderType() {
        return invokerMethodHolder.getParameterHolderType();
    }

    public InvokerMethodInfo setParameterHolderType(String parameterHolderType) {
        return invokerMethodHolder.setParameterHolderType(parameterHolderType);
    }

    public String getId() {
        return invokerMethodHolder.getId();
    }

    public InvokerMethodInfo setId(String id) {
        return invokerMethodHolder.setId(id);
    }

    public Set<String> getCategories() {
        return invokerMethodHolder.getCategories();
    }

    public <T extends InvokerMethodInfo> T setCategories(Set<String> categories) {
        return invokerMethodHolder.setCategories(categories);
    }

    public Set<String> getRelatedMethodNames() {
        return invokerMethodHolder.getRelatedMethodNames();
    }

    public <T extends InvokerMethodInfo> T setRelatedMethodNames(Set<String> relatedMethodNames) {
        return invokerMethodHolder.setRelatedMethodNames(relatedMethodNames);
    }

    public String getInvokerName() {
        return invokerName;
    }

    public <T extends InvokerMethodHolder> T setInvokerName(String invokerName) {
        this.invokerName = invokerName;
        return (T) this;
    }

    @Override
    public String toString() {
        return "InvokerMethodHolder{" +
                "name='" + getName() + '\'' +
                ", id='" + getId() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", categories=" + getCategories() +
                ", relatedMethodNames=" + getRelatedMethodNames() +
                ", returnTypeClass=" + getReturnTypeClass() +
                ", parameterHolderType='" + getParameterHolderType() + '\'' +
                ", parameters=" + getParameters() +
                ", invokerName=" + getInvokerName() +

                '}';
    }
}

package com.wizzdi.flexicore.boot.dynamic.invokers.request;

import com.wizzdi.flexicore.security.request.BaseclassFilter;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;

import java.util.Set;

public class DynamicInvokerMethodFilter extends BaseclassFilter {

    private BasicPropertiesFilter basicPropertiesFilter;
    private Set<String> categories;
    private DynamicInvokerFilter dynamicInvokerFilter;


    public Set<String> getCategories() {
        return categories;
    }

    public <T extends DynamicInvokerMethodFilter> T setCategories(Set<String> categories) {
        this.categories = categories;
        return (T) this;
    }

    public DynamicInvokerFilter getDynamicInvokerFilter() {
        return dynamicInvokerFilter;
    }

    public <T extends DynamicInvokerMethodFilter> T setDynamicInvokerFilter(DynamicInvokerFilter dynamicInvokerFilter) {
        this.dynamicInvokerFilter = dynamicInvokerFilter;
        return (T) this;
    }


    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends DynamicInvokerMethodFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }
}

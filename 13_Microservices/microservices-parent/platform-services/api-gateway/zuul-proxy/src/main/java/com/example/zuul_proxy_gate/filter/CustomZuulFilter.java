package com.example.zuul_proxy_gate.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;


@Component
public class CustomZuulFilter extends ZuulFilter {

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader("Test", "TestSample");
        ctx.addZuulResponseHeader("responseHeader", "responseHeader Value");

        return "run from filter for headers";
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return "PreFilter";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

}
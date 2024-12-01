package org.huydd.bus_ticket_Ecommercial_platform.utils;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;


@Service
public class RedisKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(target.getClass().getSimpleName())
                .append("-")
                .append(method.getName());

        if (params != null) {
            for (Object param : params) {
                sb.append("-")
                        .append("params")
                        .append(":").append(param);
            }
        }
        return sb.toString();
    }
}

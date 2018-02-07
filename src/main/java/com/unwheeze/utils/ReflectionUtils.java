package com.unwheeze.utils;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ReflectionUtils {

    public static HashMap<String,Object> getObject(Object obj) throws IllegalAccessException {
        HashMap<String,Object> attributes = new HashMap<>();
        for(Field field : obj.getClass().getDeclaredFields()) {
            attributes.put(field.getName(),field.get(obj));
        }

        return attributes;
    }
}

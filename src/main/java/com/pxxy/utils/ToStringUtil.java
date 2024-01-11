package com.pxxy.utils;

import cn.hutool.extra.spring.SpringUtil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Class name: ToStringUtil
 * Create time: 2024/1/8 10:41
 *
 * @author xw
 * @version 1.0
 */

public class ToStringUtil {

    private static final int maxLength =
            Integer.parseInt(Optional.ofNullable(SpringUtil.getProperty("fgw.log.max-length")).orElse("200"));

    private static final Map<Class<?>, Meta> map = new HashMap<>();

    public static String toString(Object o) {
        if (o == null)
            return "null";

        String s = o.toString();
        return s.length() > maxLength
                ? s.substring(0, maxLength - 4) + "..."
                : s;
    }

    public static String toString(Object[] a) {
        if (a == null)
            return "null";

        if (a.length == 0)
            return "[]";

        Class<?> clazz = a[0].getClass();
        for (Object o : a)
            if (!o.getClass().equals(clazz))
                return generateString(a);

        Meta meta = map.get(clazz);
        if (Objects.isNull(meta))
            map.put(clazz, meta = getMeta(clazz));
        return Meta.UNREACHABLE.equals(meta)
                ? generateString(a)
                : generateString(a, meta);

    }

    private static Meta getMeta(Class<?> clazz) {
        StringJoiner sj = new StringJoiner(",", clazz.getSimpleName() + "(", ")");

        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            return Meta.UNREACHABLE;
        }
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        Method[] readMethods = new Method[propertyDescriptors.length];
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            if (descriptor.getName().equals("class")) continue;
            readMethods[i] = descriptor.getReadMethod();
            sj.add(descriptor.getName());
        }
        if (Objects.isNull(readMethods[readMethods.length - 1])) {
            for (int i = readMethods.length - 2; i >= 0; i--) {
                if (Objects.nonNull(readMethods[i])) {
                    readMethods[readMethods.length - 1] = readMethods[i];
                    readMethods[i] = null;
                    break;
                }
            }
        }
        return new Meta(sj.toString(), readMethods);
    }

    private static String generateString(Object[] a, Meta meta) {
        StringBuilder b = new StringBuilder();
        b.append(meta.properties).append("{size:").append(a.length).append("}[");
        for (int i = 0, iMax = a.length - 1; ; i++) {
            b.append('(');
            Object obj = a[i];
            Method[] readMethods = meta.readMethods;
            for (int j = 0, jMax = readMethods.length - 1; j <= jMax; j++) {
                Method method = readMethods[j];
                if (Objects.isNull(method)) continue;
                try {
                    b.append(method.invoke(obj));
                    if (j == jMax) break;
                    b.append(',');
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            b.append(')');
            if (b.length() > maxLength)
                return b.substring(0, maxLength - 4) + "...";
            if (i == iMax)
                return b.append(']').toString();
            b.append(',');
        }
    }

    private static String generateString(Object[] a) {
        StringBuilder b = new StringBuilder();
        b.append("{size:").append(a.length).append("}[");
        for (int i = 0, iMax = a.length - 1; ; i++) {
            b.append(a[i]);
            if (b.length() > maxLength)
                return b.substring(0, maxLength - 4) + "...";
            if (i == iMax)
                return b.append(']').toString();
            b.append(',');
        }
    }

    private static class Meta {
        String properties;
        Method[] readMethods;

        Meta(String properties, Method[] readMethods) {
            this.properties = properties;
            this.readMethods = readMethods;
        }

        final static Meta UNREACHABLE = new Meta(null, null);
    }

}

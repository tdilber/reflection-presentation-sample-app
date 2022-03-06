package com.beyt.reflection.util;

import com.beyt.reflection.controller.UserController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tdilber at 11/16/2020
 */
@Slf4j
public final class GenericTypeResolverExtendedUtil {
    private GenericTypeResolverExtendedUtil() {
    }


    @Nullable
    public static Class<?> resolveReturnTypeDeepestArgument(Method method, Class<?> genericIfc) {
        Assert.notNull(method, "Method must not be null");
        ResolvableType resolvableType = ResolvableType.forMethodReturnType(method).as(genericIfc);
        if (!resolvableType.hasGenerics() || resolvableType.getType() instanceof WildcardType) {
            return null;
        }
        return getDeepestGeneric(resolvableType);
    }

    @Nullable
    public static Class<?> getDeepestGeneric(ResolvableType resolvableType) {
        Assert.isTrue(resolvableType.getGenerics().length == 1,
                () -> "Expected 1 type argument on generic interface [" + resolvableType +
                        "] but found " + resolvableType.getGenerics().length);
        ResolvableType generic = resolvableType.getGeneric();
        while (!generic.toString().equals("?")) {
            if (!generic.getGeneric().toString().equals("?")) {
                generic = generic.getGeneric();
            } else {
                break;
            }
        }
        return generic.resolve();
    }

    public static Class<?> getMethodDeepestReturnType(Method method, Class<?> genericClass) {
        Class<?> methodClasses = null;
        if (!method.getReturnType().getName().equals("void")) {
            if (genericClass.isAssignableFrom(method.getReturnType())) {
                Class<?> genericDeepestClass = resolveReturnTypeDeepestArgument(method, genericClass);

                if (genericDeepestClass != null) {
                    methodClasses = genericDeepestClass;
                }
            }
        }
        if (methodClasses == null) {
            methodClasses = method.getReturnType();
        }

        return methodClasses;
    }

    public static List<Class<?>> getMethodAllReturnTypes(Method method) {
        List<Class<?>> result = new ArrayList<>();
        result.add(method.getReturnType());
        result.addAll(getMethodAllReturnTypes(method, method.getReturnType()));
        return result;
    }

    public static List<Class<?>> getMethodAllReturnTypes(Method method, Class<?> genericClass) {
        List<Class<?>> methodClasses = Collections.emptyList();
        if (!method.getReturnType().getName().equals("void")) {
            if (genericClass.isAssignableFrom(method.getReturnType())) {
                List<Class<?>> genericDeepestClass = resolveReturnTypeAllArguments(method, genericClass);

                if (!CollectionUtils.isEmpty(genericDeepestClass)) {
                    methodClasses = genericDeepestClass;
                }
            }
        }

        return methodClasses;
    }

    public static List<Class<?>> resolveReturnTypeAllArguments(Method method, Class<?> genericIfc) {
        Assert.notNull(method, "Method must not be null");
        ResolvableType resolvableType = ResolvableType.forMethodReturnType(method).as(genericIfc);
        if (!resolvableType.hasGenerics() || resolvableType.getType() instanceof WildcardType) {
            return null;
        }
        return getAllGenerics(resolvableType);
    }

    public static List<Class<?>> getAllGenerics(ResolvableType resolvableType) {
        Assert.isTrue(resolvableType.getGenerics().length == 1,
                () -> "Expected 1 type argument on generic interface [" + resolvableType +
                        "] but found " + resolvableType.getGenerics().length);
        ResolvableType generic = resolvableType.getGeneric();
        List<Class<?>> result = new ArrayList<>();
        while (!generic.toString().equals("?")) {
            result.add(generic.resolve());
            if (!generic.getGeneric().toString().equals("?")) {
                generic = generic.getGeneric();
            } else {
                break;
            }
        }
        return result;
    }
}

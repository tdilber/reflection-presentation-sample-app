package com.beyt.reflection.util;

import com.beyt.reflection.dto.UserDTO;
import com.beyt.reflection.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericTypeResolverExtendedUtilTest {
    private static Method method1;
    private static Method method2;

    Map<List<UserDTO>, Map<Long, String>> field1;
    Map<List<Integer>, TreeMap<String, UserDTO>> field2;
    Object field3;
    int field4;
    byte[] field5;

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> testMethod() throws UserNotFoundException {
        return null;
    }

    @GetMapping("/")
    public ResponseEntity<Map<List<UserDTO>, Map<Long, String>>> testMethod2(Map<List<UserDTO>, Map<Long, String>> param1, Map<List<Integer>, TreeMap<String, UserDTO>> param2, Object param3, int param4, byte[] param5) throws UserNotFoundException {
        return null;
    }

    @BeforeAll
    static void beforeAll() throws NoSuchMethodException {
        method1 = GenericTypeResolverExtendedUtilTest.class.getDeclaredMethod("testMethod");
        method2 = Arrays.stream(GenericTypeResolverExtendedUtilTest.class.getDeclaredMethods()).filter(m -> m.getName().equals("testMethod2")).findFirst().orElseThrow(IllegalStateException::new);
    }

    @Test
    void resolveReturnTypeGenericsTree() {
        GenericTypeResolverUtil.GenericsTypesTreeResult result1 = GenericTypeResolverUtil.resolveReturnTypeGenericsTree(method1);
        GenericTypeResolverUtil.GenericsTypesTreeResult result2 = GenericTypeResolverUtil.resolveReturnTypeGenericsTree(method2);

        assertEquals("ResponseEntity<List<UserDTO>>", result1.printResult());
        assertEquals("ResponseEntity<Map<List<UserDTO>, Map<Long, String>>>", result2.printResult());
    }

    @Test
    void resolveMethodParameterGenericsTree() {
        GenericTypeResolverUtil.GenericsTypesTreeResult result = GenericTypeResolverUtil.resolveMethodParameterGenericsTree(method2, 0);
        GenericTypeResolverUtil.GenericsTypesTreeResult result2 = GenericTypeResolverUtil.resolveMethodParameterGenericsTree(method2, 1);
        GenericTypeResolverUtil.GenericsTypesTreeResult result3 = GenericTypeResolverUtil.resolveMethodParameterGenericsTree(method2, 2);
        GenericTypeResolverUtil.GenericsTypesTreeResult result4 = GenericTypeResolverUtil.resolveMethodParameterGenericsTree(method2, 3);
        GenericTypeResolverUtil.GenericsTypesTreeResult result5 = GenericTypeResolverUtil.resolveMethodParameterGenericsTree(method2, 4);

        assertEquals("Map<List<UserDTO>, Map<Long, String>>", result.printResult());
        assertEquals("Map<List<Integer>, TreeMap<String, UserDTO>>", result2.printResult());
        assertEquals("Object", result3.printResult());
        assertEquals("int", result4.printResult());
        assertEquals("byte[]", result5.printResult());
    }

    @Test
    void resolveConstructorParameterGenericsTree() {
        Constructor<?> constructor = TestClass.class.getDeclaredConstructors()[0];
        GenericTypeResolverUtil.GenericsTypesTreeResult result = GenericTypeResolverUtil.resolveConstructorParameterGenericsTree(constructor, 0);
        GenericTypeResolverUtil.GenericsTypesTreeResult result2 = GenericTypeResolverUtil.resolveConstructorParameterGenericsTree(constructor, 1);
        GenericTypeResolverUtil.GenericsTypesTreeResult result3 = GenericTypeResolverUtil.resolveConstructorParameterGenericsTree(constructor, 2);
        GenericTypeResolverUtil.GenericsTypesTreeResult result4 = GenericTypeResolverUtil.resolveConstructorParameterGenericsTree(constructor, 3);
        GenericTypeResolverUtil.GenericsTypesTreeResult result5 = GenericTypeResolverUtil.resolveConstructorParameterGenericsTree(constructor, 4);

        assertEquals("Map<List<UserDTO>, Map<Long, String>>", result.printResult());
        assertEquals("Map<List<Integer>, TreeMap<String, UserDTO>>", result2.printResult());
        assertEquals("Object", result3.printResult());
        assertEquals("int", result4.printResult());
        assertEquals("byte[]", result5.printResult());
    }

    @Test
    void resolveFieldGenericsTree() throws NoSuchFieldException {
        Field f1 = GenericTypeResolverExtendedUtilTest.class.getDeclaredField("field1");
        Field f2 = GenericTypeResolverExtendedUtilTest.class.getDeclaredField("field2");
        Field f3 = GenericTypeResolverExtendedUtilTest.class.getDeclaredField("field3");
        Field f4 = GenericTypeResolverExtendedUtilTest.class.getDeclaredField("field4");
        Field f5 = GenericTypeResolverExtendedUtilTest.class.getDeclaredField("field5");

        GenericTypeResolverUtil.GenericsTypesTreeResult result = GenericTypeResolverUtil.resolveFieldGenericsTree(f1);
        GenericTypeResolverUtil.GenericsTypesTreeResult result2 = GenericTypeResolverUtil.resolveFieldGenericsTree(f2);
        GenericTypeResolverUtil.GenericsTypesTreeResult result3 = GenericTypeResolverUtil.resolveFieldGenericsTree(f3);
        GenericTypeResolverUtil.GenericsTypesTreeResult result4 = GenericTypeResolverUtil.resolveFieldGenericsTree(f4);
        GenericTypeResolverUtil.GenericsTypesTreeResult result5 = GenericTypeResolverUtil.resolveFieldGenericsTree(f5);

        assertEquals("Map<List<UserDTO>, Map<Long, String>>", result.printResult());
        assertEquals("Map<List<Integer>, TreeMap<String, UserDTO>>", result2.printResult());
        assertEquals("Object", result3.printResult());
        assertEquals("int", result4.printResult());
        assertEquals("byte[]", result5.printResult());
    }

    @Test
    void resolveReturnTypeDeepestArgument() {
        Class<?> deepestArgument = GenericTypeResolverUtil.resolveReturnTypeDeepestArgument(method1, ResponseEntity.class);

        assertEquals(UserDTO.class, deepestArgument);
    }

    @Test
    void resolveReturnTypeAllArguments() {
        List<Class<?>> argumentList = GenericTypeResolverUtil.getMethodAllReturnTypes(method1, ResponseEntity.class);

        assertEquals(Arrays.asList(List.class, UserDTO.class), argumentList);
    }

    @Test
    void getMethodAllReturnTypes() {
        List<Class<?>> argumentList = GenericTypeResolverUtil.getMethodAllReturnTypes(method1);

        assertEquals(Arrays.asList(ResponseEntity.class, List.class, UserDTO.class), argumentList);
    }

    public static class TestClass {
        public TestClass(Map<List<UserDTO>, Map<Long, String>> param1, Map<List<Integer>, TreeMap<String, UserDTO>> param2, Object param3, int param4, byte[] param5) {
        }
    }
}

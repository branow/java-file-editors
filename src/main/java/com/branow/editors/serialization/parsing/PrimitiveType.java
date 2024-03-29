package com.branow.editors.serialization.parsing;

import com.branow.outfits.checker.ParametersChecker;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public enum PrimitiveType {

    STRING("String"), INTEGER("Integer"), LONG("Long"),
    SHORT("Short"), BYTE("Byte"), BOOLEAN("Boolean"),
    CHARACTER("Character");

    public static PrimitiveType getPrimitiveOfClassName(String fullClassName) {
        PrimitiveType[] types = PrimitiveType.values();
        return Arrays.stream(types).filter(e -> e.getFullName().equals(fullClassName))
                .findAny().orElse(null);
    }

    public static PrimitiveType getPrimitiveOfValue(String fullClassName) {
        PrimitiveType[] types = PrimitiveType.values();
        return Arrays.stream(types).filter(e -> e.getValue().equals(fullClassName))
                .findAny().orElse(null);
    }

    private final String value;

    PrimitiveType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getFullName() {
        return "java.lang." + value;
    }

    public Object create(String value) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (this == CHARACTER) {
            ParametersChecker.isNullOrEmptyThrow(value);
            return ((String) Class.forName(STRING.getFullName()).getConstructor(String.class).newInstance(value)).charAt(0);
        }
        ParametersChecker.isNullThrow(value);
        return Class.forName(getFullName()).getConstructor(String.class).newInstance(value);
    }

    public Object create() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return create("0");
    }

}

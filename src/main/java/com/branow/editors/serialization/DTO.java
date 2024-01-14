package com.branow.editors.serialization;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface DTO<B> {

    DTO<B> form(B base);

    B form();

    default int[] transform(Integer[] array) {
        int[] res = new int[array.length];
        for (int i=0; i<array.length; i++)
            res[i] = array[i];
        return res;
    }

    default Integer[] transform(int[] array) {
        Integer[] res = new Integer[array.length];
        for (int i=0; i<array.length; i++)
            res[i] = array[i];
        return res;
    }

    static<T, R extends DTO<T>> T[] transform(R[] base, Class<T> type) {
        T[] res = (T[]) Array.newInstance(type, base.length);
        for (int i=0; i< base.length; i++)
            res[i] = base[i].form();
        return res;
    }

    static<T, R extends DTO<T>> List<T> transformToList(R[] base, Class<T> type) {
        T[] res = (T[]) Array.newInstance(type, base.length);
        for (int i=0; i< base.length; i++)
            res[i] = base[i].form();
        return new ArrayList<>(Arrays.stream(res).toList());
    }

    static<T, R extends DTO<T>> R[] transform(T[] base, Class<R> type) {
        R[] res = (R[]) Array.newInstance(type, base.length);
        for (int i=0; i< base.length; i++) {
            try {
                res[i] = (R) type.getConstructor().newInstance();
                res[i].form(base[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    static<T, R extends DTO<T>> R[] transformToArray(List<T> base, Class<R> type) {
        R[] res = (R[]) Array.newInstance(type, base.size());
        for (int i=0; i< base.size(); i++) {
            try {
                res[i] = (R) type.getConstructor().newInstance();
                res[i].form(base.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

}

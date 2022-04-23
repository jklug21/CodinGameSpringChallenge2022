package spring2022.io;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DerivedScanner {
    private final Scanner scanner;

    public DerivedScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public <T> T getObjectFromInput(Class<T> type) {
        Field[] fields = type.getDeclaredFields();
        Map<Integer, Field> indexToFieldMap = new HashMap<>();
        int maxIndex = -1;
        for (Field field : fields) {
            ScanIndex index = field.getAnnotation(ScanIndex.class);
            if (index != null) {
                indexToFieldMap.put(index.value(), field);
                maxIndex = Math.max(index.value(), maxIndex);
            }
        }
        try {
            Constructor<T> defaultConstructor = type.getConstructor();
            T instance = defaultConstructor.newInstance();
            for (int i = 0; i <= maxIndex; i++) {
                int v = scanner.nextInt();
                Field field = indexToFieldMap.get(i);
                if (field != null) {
                    field.setAccessible(true);
                    field.set(instance, v);
                }
            }
            return instance;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("whoopsies", e);
        }
    }
}

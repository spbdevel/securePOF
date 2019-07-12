package org.app;

import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collection;

public class Util {

    public static void copyProperties(Object src, Object trg, Collection<String> props) {
        String[] excludedProperties =
                Arrays.stream(BeanUtils.getPropertyDescriptors(src.getClass()))
                        .map(PropertyDescriptor::getName)
                        .filter(name -> !props.contains(name))
                        .toArray(String[]::new);

        BeanUtils.copyProperties(src, trg, excludedProperties);
    }
}

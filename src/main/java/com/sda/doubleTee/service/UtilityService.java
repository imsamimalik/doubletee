package com.sda.doubleTee.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UtilityService {

    public static <T> List<List<T>> cartesianProduct(List<List<T>> lists) {
        // check if not null
        if (lists == null) return null;
        // cartesian product of multiple lists
        return lists.stream()
                // only those lists that are not null and not empty
                .filter(list -> list != null && list.size() > 0)
                // represent each list element as a singleton list
                .map(list -> list.stream().map(Collections::singletonList)
                        // Stream<List<List<T>>>
                        .collect(Collectors.toList()))
                // intermediate output
                .peek(System.out::println)
                // stream of lists into a single list
                .reduce((lst1, lst2) -> lst1.stream()
                        // combinations of inner lists
                        .flatMap(inner1 -> lst2.stream()
                                // concatenate into a single list
                                .map(inner2 -> Stream.of(inner1, inner2)
                                        .flatMap(List::stream)
                                        .collect(Collectors.toList())))
                        // list of combinations
                        .collect(Collectors.toList()))
                // otherwise an empty list
                .orElse(Collections.emptyList());
    }
}

package hoelzel.jonathan.minesweeper;

import java.util.*;
import java.util.stream.Collectors;

public class Util {
    public static int countIterable(Iterable i) {
        int ret = 0;
        for (Object o : i) {
            ret ++;
        }
        return ret;
    }

    public static <T> List<T> toList(Iterable<T> i) {
        List<T> ret = new ArrayList<>();
        i.forEach(ret::add);
        return ret;
    }

    public static <T> Set<T> toSet(Iterable<T> i) {
        Set<T> ret = new HashSet<>();
        i.forEach(ret::add);
        return ret;
    }

    public static <T> Set<T> union(Set<T> set1, Set<T> set2) {
        Set<T> ret = new HashSet<>();
        ret.addAll(set1);
        ret.addAll(set2);
        return ret;
    }

    public static <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        return set1.stream().filter(set2::contains).collect(Collectors.toSet());
    }

    public static <T> Set<T> minus(Set<T> set1, Set<T> set2) {
        return set1.stream().filter(e -> !set2.contains(e)).collect(Collectors.toSet());
    }

    public static <T> boolean intersects(Set<T> set1, Set<T> set2) {
        return set1.stream().anyMatch(set2::contains);
    }
}

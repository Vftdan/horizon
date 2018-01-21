package io.github.vftdan.horizon;

/**
 * Created by Vftdan on 21.01.2018.
 */
public class Utils {
    public static <T> T ifNull(T val, T defa) {
        if(val != null) return val;
        return defa;
    }
    public static <T> T logged(T o) {
        System.out.println(o);
        return o;
    }
    public static class Math {
        public static <T extends Number> T min(T a, T b) {
            return a.doubleValue() > b.doubleValue() ? b : a;
        }
        public static <T extends Number> T max(T a, T b) {
            return a.doubleValue() < b.doubleValue() ? b : a;
        }
        public static <T extends Number> T clamp(T val, T mi, T ma) {
            return min(ma, max(val, mi));
        }
    }
}

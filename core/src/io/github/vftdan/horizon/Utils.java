package io.github.vftdan.horizon;

/**
 * Created by Vftdan on 21.01.2018.
 */
public class Utils {
    public static <T> T ifNull(T val, T defa) {
        if(val != null) return val;
        return defa;
    }
    @SafeVarargs
	public static <T> boolean isOneOf(T val, T... variants) {
    	for(T v: variants) {
    		if(v.equals(val)) return true;
    	}
    	return false;
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
        public static <T extends Number> double pythag(T... vec) {
        	double s = 0;
        	for(T c: vec) {
        		s += c.doubleValue() * c.doubleValue();
        	}
        	return java.lang.Math.sqrt(s);
        }
		public static int signOf(Number n, Number zeroRadius) {
        	double d = n.doubleValue();
        	double zr = zeroRadius.floatValue();
        	if(d <= zr && d >= -zr) return 0;
        	return d < 0 ? -1 : 1;
        }
		public static int signOf(Number n) {
			return signOf(n, 0);
		}
    }
}

package com.certificate.object;

import org.junit.Test;

public class ObjectMockTest {
    @Test
    public void compilationErrorUnknownMethodCallOnObject() {
        class TrickyNum<X extends Object> {
            private X x;
            public TrickyNum(X x) {
                this.x = x;
            }
            private double getDouble() {
                //cause X is Object class and has no method doubleValue() java.lang.Number
//                return x.doubleValue();
                return 0d;
            }
        }
    }

    @Test
    public void integerCannotBeCastDouble() {
        class TrickyNum<X extends Object> {
            private X x;
            public TrickyNum(X x) {
                this.x = x;
            }
            private double getDouble() {
                //Integer can not be cast to Double
//                return ((Double) x).doubleValue();
                return ((Number) x).doubleValue();
            }
        }
        TrickyNum<Integer> a = new TrickyNum<>(1);
        System.out.print(a.getDouble());
    }
}

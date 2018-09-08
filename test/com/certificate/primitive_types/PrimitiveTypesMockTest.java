package com.certificate.primitive_types;

import org.junit.Assert;
import org.junit.Test;

public class PrimitiveTypesMockTest {
    @Test
    public void ignoreFieldVariableIfMethodArgHasSameName() {
        class Staff {
            public int a = 10;
            public int doStuff(int a) {
                a += 1;
                return ++a;
            }
        }

        //cause function has own a variable 3 += 1 = 4; ++4 = 5 and return
        Assert.assertEquals(new Staff().doStuff(3), 5);
    }

    @Test
    public void notUseThisVariableInMethod() {
        class Staff {
            private int value = 1;

            public int getValue() {
                return value;
            }

            public void changeVal(int value) {
                value = value;
            }
        }

        int a = 2;
        Staff staff = new Staff();
        staff.changeVal(a);
        Assert.assertEquals(staff.getValue(), 1);
    }
}

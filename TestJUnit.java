package com.example.LongInt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

import com.sun.deploy.util.StringUtils;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.*;


public class TestJUnit extends Assert {
    private static Map<LongInt, String> testsStorage = new HashMap<LongInt, String>();

    @Before
    public static void FullTestsStorage() throws MyExeption{
        addTest("2", "+", "2", "4");
    }

    @After
    public static void clearTestsStorage() {
        testsStorage.clear();
    }

    @Test
    public static void testAriphmetic() {
        for(Map.Entry<LongInt, String> entry : testsStorage.entrySet()) {
            final LongInt testData = entry.getKey();
            final String result = entry.getValue();
            final String actual = testData.toString();
            assertEquals(actual, result);
        }
    }

    private static void addTest(String a, String op, String b, String res) throws MyExeption {
        LongInt aa = new LongInt(a);
        LongInt bb = new LongInt(b);
        LongInt cc = new LongInt(0);
        if(op == "+")
        {
            cc = aa.Plus(bb);
        }
        else
        {
            if(op == "-")
            {
                cc = aa.Subtraction(bb);
            }
            else
            {
                if(op == "*")
                {
                    cc = aa.Multi(bb);
                }
                else
                {
                    if(op == "/")
                    {
                        cc = aa.Division(bb);
                    }
                }
            }
        }
        testsStorage.put(cc, res);
    }
 }
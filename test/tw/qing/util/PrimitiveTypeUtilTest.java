package tw.qing.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

public class PrimitiveTypeUtilTest extends TestCase
{

    public void testIsInt()
    {
        assertFalse(PrimitiveTypeUtil.isInt(""));
        assertFalse(PrimitiveTypeUtil.isInt(null));
        assertFalse(PrimitiveTypeUtil.isInt(Double.valueOf(1)));
        assertFalse(PrimitiveTypeUtil.isInt(Float.valueOf(1)));

        assertTrue(PrimitiveTypeUtil.isInt(Integer.valueOf(1)));
        assertTrue(PrimitiveTypeUtil.isInt(Long.valueOf(1)));
        assertTrue(PrimitiveTypeUtil.isInt(BigDecimal.valueOf(1)));
        assertTrue(PrimitiveTypeUtil.isInt(BigInteger.valueOf(1)));

    }

    public void testGetInt()
    {

        assertEquals(1, PrimitiveTypeUtil.getInt(Byte.valueOf((byte) 1)));
        assertEquals(1, PrimitiveTypeUtil.getInt(Short.valueOf((short) 1)));
        assertEquals(1, PrimitiveTypeUtil.getInt(Integer.valueOf(1)));
        assertEquals(1, PrimitiveTypeUtil.getInt(Long.valueOf(1)));
        assertEquals(1, PrimitiveTypeUtil.getInt(BigDecimal.valueOf(1)));
        assertEquals(1, PrimitiveTypeUtil.getInt(BigInteger.valueOf(1)));

        try
        {
            PrimitiveTypeUtil.getInt(Float.valueOf(1));
            fail("invoked getInt() with non integer object");
        } catch (Exception ex)
        {
            assertTrue(ClassCastException.class.isInstance(ex));
        }

        try
        {
            PrimitiveTypeUtil.getInt(null);
            fail("invoked getInt() with null object");
        } catch (Exception ex)
        {
            assertTrue(NullPointerException.class.isInstance(ex));
        }

    }

    public void testGetLong()
    {
        assertEquals(Long.valueOf(1), PrimitiveTypeUtil.getLong(Byte.valueOf((byte) 1)));
        assertEquals(Long.valueOf(1), PrimitiveTypeUtil.getLong(Short.valueOf((short) 1)));
        assertEquals(Long.valueOf(1), PrimitiveTypeUtil.getLong(Integer.valueOf(1)));
        assertEquals(Long.valueOf(1), PrimitiveTypeUtil.getLong(Long.valueOf(1)));
        assertEquals(Long.valueOf(1), PrimitiveTypeUtil.getLong(BigInteger.valueOf(1)));
        assertEquals(Long.valueOf(1), PrimitiveTypeUtil.getLong(BigDecimal.valueOf(1)));

        try
        {
            PrimitiveTypeUtil.getLong(Float.valueOf(1));
            fail("invoked getLong() with non integer object");
        } catch (Exception ex)
        {
            assertTrue(ClassCastException.class.isInstance(ex));
        }
    }

    public void testGetInteger()
    {
        assertEquals(Integer.valueOf(1), PrimitiveTypeUtil.getInteger(Byte.valueOf((byte) 1)));
        assertEquals(Integer.valueOf(1), PrimitiveTypeUtil.getInteger(Short.valueOf((short) 1)));
        assertEquals(Integer.valueOf(1), PrimitiveTypeUtil.getInteger(Integer.valueOf(1)));
        assertEquals(Integer.valueOf(1), PrimitiveTypeUtil.getInteger(Long.valueOf(1)));
        assertEquals(Integer.valueOf(1), PrimitiveTypeUtil.getInteger(BigInteger.valueOf(1)));
        assertEquals(Integer.valueOf(1), PrimitiveTypeUtil.getInteger(BigDecimal.valueOf(1)));

        try
        {
            PrimitiveTypeUtil.getInteger(Float.valueOf(1));
            fail("invoked getInteger() with non integer object");
        } catch (Exception ex)
        {
            assertTrue(ClassCastException.class.isInstance(ex));
        }

        assertEquals(null, PrimitiveTypeUtil.getLong(null));
    }

    public void testGetFloat()
    {
        assertEquals(Float.valueOf(1), PrimitiveTypeUtil.getFloat(Float.valueOf(1)));
        assertEquals(Float.valueOf(1), PrimitiveTypeUtil.getFloat(Double.valueOf(1)));
        assertEquals(Float.valueOf(1), PrimitiveTypeUtil.getFloat(BigDecimal.valueOf(1)));
        assertEquals(null, PrimitiveTypeUtil.getFloat(null));

        try
        {
            PrimitiveTypeUtil.getFloat(Integer.valueOf(1));
            fail("invoked getFloat() with non floating number object");
        } catch (Exception ex)
        {
            assertTrue(ClassCastException.class.isInstance(ex));
        }

        try
        {
            PrimitiveTypeUtil.getFloat(Long.valueOf(1));
            fail("invoked getFloat() with non floating number object");
        } catch (Exception ex)
        {
            assertTrue(ClassCastException.class.isInstance(ex));
        }

        try
        {
            PrimitiveTypeUtil.getFloat(BigInteger.valueOf(1));
            fail("invoked getFloat() with non floating number object");
        } catch (Exception ex)
        {
            assertTrue(ClassCastException.class.isInstance(ex));
        }

    }

    public void testGetDouble()
    {
        assertEquals(null, PrimitiveTypeUtil.getDouble(null));
        assertEquals(Double.valueOf(1), PrimitiveTypeUtil.getDouble(Float.valueOf(1)));
        assertEquals(Double.valueOf(1), PrimitiveTypeUtil.getDouble(Double.valueOf(1)));
        assertEquals(BigDecimal.valueOf(1D).doubleValue(), PrimitiveTypeUtil.getDouble(
                BigDecimal.valueOf(1)).doubleValue(), 0.000001);

        try
        {
            PrimitiveTypeUtil.getDouble(Integer.valueOf(1));
            fail("invoked getDouble() with non floating number object");
        } catch (Exception ex)
        {
            assertTrue(ClassCastException.class.isInstance(ex));
        }

        try
        {
            PrimitiveTypeUtil.getDouble(Long.valueOf(1));
            fail("invoked getDouble() with non floating number object");
        } catch (Exception ex)
        {
            assertTrue(ClassCastException.class.isInstance(ex));
        }

        try
        {
            PrimitiveTypeUtil.getDouble(BigInteger.valueOf(1));
            fail("invoked getDouble() with non floating number object");
        } catch (Exception ex)
        {
            assertTrue(ClassCastException.class.isInstance(ex));
        }

    }

    public void testGetWrapClass()
    {
        assertEquals(PrimitiveTypeUtilTest.class, PrimitiveTypeUtil
                .getWrapClass(PrimitiveTypeUtilTest.class));

        assertEquals(null, PrimitiveTypeUtil.getWrapClass(null));

        assertEquals(Character.class, PrimitiveTypeUtil.getWrapClass(char.class));
        assertEquals(Integer.class, PrimitiveTypeUtil.getWrapClass(int.class));
        assertEquals(Long.class, PrimitiveTypeUtil.getWrapClass(long.class));
        assertEquals(Float.class, PrimitiveTypeUtil.getWrapClass(float.class));

        assertEquals(Double.class, PrimitiveTypeUtil.getWrapClass(double.class));
        assertEquals(Boolean.class, PrimitiveTypeUtil.getWrapClass(boolean.class));
    }

    public void testCastValueClassObject()
    {
        assertEquals(null, PrimitiveTypeUtil.castValue(null, (Object) null));
        assertEquals("", PrimitiveTypeUtil.castValue(String.class, (Object) ""));
        assertEquals(Boolean.TRUE, PrimitiveTypeUtil.castValue(boolean.class, (Object) "true"));
    }

    public void testCastValueClassString()
    {
        assertEquals(null, PrimitiveTypeUtil.castValue(null, null));
        assertEquals(null, PrimitiveTypeUtil.castValue(String.class, null));
        assertEquals("str", PrimitiveTypeUtil.castValue(String.class, "str"));
        assertEquals("", PrimitiveTypeUtil.castValue(Character.class, ""));

        // TODO why 0 char !?
        assertEquals(new Character((char) 0), PrimitiveTypeUtil.castValue(char.class, ""));
        assertEquals(new Character("123".charAt(0)), PrimitiveTypeUtil.castValue(char.class, "123"));

        assertEquals(Boolean.FALSE, PrimitiveTypeUtil.castValue(boolean.class, ""));
        assertEquals(Boolean.FALSE, PrimitiveTypeUtil.castValue(boolean.class, "0"));
        assertEquals(Boolean.FALSE, PrimitiveTypeUtil.castValue(boolean.class, "1"));
        assertEquals(Boolean.TRUE, PrimitiveTypeUtil.castValue(boolean.class, "true"));

        assertEquals(new Byte("1"), PrimitiveTypeUtil.castValue(byte.class, "1"));
        assertEquals(new Byte("0"), PrimitiveTypeUtil.castValue(byte.class, ""));

        assertEquals(new Double("123456.789"), PrimitiveTypeUtil.castValue(double.class,
                ",,123,456.789"));

        assertEquals(new Float("123456.789"), PrimitiveTypeUtil.castValue(float.class,
                ",,123,456.789"));

        assertEquals(new Long("123456789"), PrimitiveTypeUtil.castValue(long.class, ",,123,456789"));

        assertEquals(new Integer("123456789"), PrimitiveTypeUtil.castValue(int.class,
                ",,123,456789"));

        assertEquals(new Short("123"), PrimitiveTypeUtil.castValue(short.class, "123"));

        assertEquals(Double.valueOf("1"), PrimitiveTypeUtil.castValue(Double.class, "1"));
        assertEquals(Float.valueOf("1.3"), PrimitiveTypeUtil.castValue(Float.class, "1.3"));
        assertEquals(Long.valueOf("1"), PrimitiveTypeUtil.castValue(Long.class, "1"));
        assertEquals(Integer.valueOf("1"), PrimitiveTypeUtil.castValue(Integer.class, "1"));

        // test cast Date
        Date date = DateUtilTest.makeDate(1999, 12, 22, 0, 0, 0);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        assertEquals(date, PrimitiveTypeUtil.castValue(Date.class, fmt.format(date)));
    }

}

package tw.qing.sys;

import junit.framework.TestCase;

public class StringManagerTest extends TestCase
{

    // final static Logger log = Logger.getLogger(StringManagerTest.class);
    final static String PACKAGE = "pkg_name";
    private StringManager sm;

    protected void setUp() throws Exception
    {
        sm = StringManager.getManager(PACKAGE);
        assertNotNull(sm);
    }

    public void testGetString() throws Exception
    {
        // get the existing key
        assertEquals("value1", sm.getString("key1"));

        // get the non existing key
        assertNull(sm.getString("no_such_key"));

        // get the null key
        try
        {
            sm.getString(null);
            fail("key is null.");
        } catch (Exception ignored)
        {
        }
    }

    public void testGetStringWithOneArg() throws Exception {
        // keyWithOneArg={0}
        assertEquals("null", sm.getString("keyWithOneArg", (Object) null));
        assertEquals("value", sm.getString("keyWithOneArg", "value"));
    }

    public void testGetStringWithTwoArgs() throws Exception {
        // keyWithTwoArgs={0}{1}
        assertEquals("nullvalue", sm.getString("keyWithTwoArgs", null, "value"));
        assertEquals("value1value2", sm.getString("keyWithTwoArgs", "value1", "value2"));
    }

    public void testGetStringWithThreeArgs() throws Exception {
        // keyWithThreeArgs={0}{1}{3}
        assertEquals("nullvalue2value3",
                sm.getString("keyWithThreeArgs", null, "value2", "value3"));

        assertEquals("value1value2value3",
                sm.getString("keyWithThreeArgs", "value1", "value2", "value3"));
    }

    public void testGetStringWithFourArgs() throws Exception {
        // keyWithFourArgs={0}{1}{2}{3}
        assertEquals("nullvalue2value3null",
                sm.getString("keyWithFourArgs", null, "value2", "value3", null));

        assertEquals("value1value2value3value4",
                sm.getString("keyWithFourArgs",
                        "value1", "value2", "value3", "value4"));
    }

    public void testCatchIllegalArgumentException() throws Exception {
        // illegalPattern={1}{0'}'
        assertEquals(
                sm.getString("illegalPattern") + " arg[0]=value1 arg[1]=value2",
                sm.getString("illegalPattern", "value1", "value2"));
    }

    public void testGetStringWithObjectArgs() throws Exception {
        // keyWithObjectArrays={0}{1}{2}

        try {
            assertEquals("1a" + 2 + "b",
                    sm.getString("keyWithObjectArrays", (Object[]) null));
            fail("catches the NPE because args is null.");
        } catch (Exception ignored) {
        }

        // get with object array normally
        assertEquals("1a" + 2 + "b",
                sm.getString("keyWithObjectArrays",
                        new Object[] { "1a", Integer.valueOf(2), "b" }));

        // get with object array with null
        assertEquals("1a" + "null" + "b",
                sm.getString("keyWithObjectArrays",
                        new Object[] { "1a", null, "b" }));

        // TODO does we consider the fewer args?
        // get with the fewer array
        assertEquals("1a" + "2b" + "{2}",
                sm.getString("keyWithObjectArrays",
                        new Object[] { "1a", "2b" }));

        // TODO does we consider the more args?
        // get with the fewer array
        assertEquals("1a" + "2b" + "3c",
                sm.getString("keyWithObjectArrays",
                        new Object[] { "1a", "2b", "3c", "4d" }));

    }

}

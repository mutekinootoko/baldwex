package tw.qing.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class CaseInsensitiveHashMapTest extends TestCase {

    CaseInsensitiveHashMap map;

    protected void setUp() throws Exception {
        super.setUp();
        map = new CaseInsensitiveHashMap();
    }

    public void testContainsKey() {
        map.put("abc", "v");
        assertTrue(map.containsKey("abc"));
        assertTrue(map.containsKey("Abc"));
        assertTrue(map.containsKey("aBc"));
        assertTrue(map.containsKey("abC"));
    }

    public void testPutAll() {
        Map m = new HashMap();
        m.put("abc", "v");
        map.putAll(m);
        assertEquals("v", map.get("abc"));
        assertEquals("v", map.get("abC"));
        assertEquals("v", map.get("aBC"));
    }

    public void testRemove() {
        map.put("abc", "v");
        assertEquals("v", map.remove("aBC"));
        assertFalse(map.containsKey("aBC"));

        map.put("abc", "v");
        assertEquals("v", map.remove("abC"));
        assertFalse(map.containsKey("abC"));
    }

    public void testPut() {
        assertTrue(map.isEmpty());

        map.put("abc", "v1");
        assertEquals(1, map.size());
        assertEquals("v1", map.get("abc"));

        map.put("aBc", "v2");
        assertEquals(1, map.size());
        assertEquals("v2", map.get("abc"));

        map.put("aBC", "v3");
        assertEquals(1, map.size());
        assertEquals("v3", map.get("abc"));
    }

    public void testGet() {
        assertNull(map.get("abc"));
        assertNull(map.get(null));

        map.put("abc", "v");
        assertEquals("v", map.get("abc"));
        assertEquals("v", map.get("abC"));
        assertEquals("v", map.get("aBC"));
    }

    public void testCaseInsensitiveHashMapMap() {
        Map m = new HashMap();
        m.put("abc", "value1");
        m.put("def", "value2");

        CaseInsensitiveHashMap caseInsensitiveMap = new CaseInsensitiveHashMap(m);
        assertTrue(caseInsensitiveMap.containsKey("ABC"));
        assertTrue(caseInsensitiveMap.containsKey("DEF"));
    }

    public void testNonStringKey() throws Exception {
        Date date = new Date();
        map.put(date, "value");
        assertEquals(1, map.size());
        assertTrue(map.containsKey(date));
        assertEquals("value", map.get(date));
        assertEquals("value", map.remove(date));
        assertTrue(map.isEmpty());
    }
}

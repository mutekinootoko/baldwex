package tw.qing.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import junit.framework.TestCase;

public class DateUtilTest extends TestCase {

    static Logger logger = Logger.getLogger(DateUtilTest.class);

    final Date date = new Date();

    public static Date makeDate(int year, int month, int day,
            int hour, int minute, int second) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        return c.getTime();
    }

    public void testDateToString() {
        assertEquals(null, DateUtil.dateToString(null));
        assertEquals(SimpleDateFormat.getInstance().format(date),
                DateUtil.dateToString(new Date()));
    }

    public void testDateToStringDateString() {
        final String pattern = "yyyy/MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        assertEquals(df.format(date), DateUtil.dateToString(date, pattern));
    }

    public void testStringToDate() {

        Date sampleDate = makeDate(2010, 1, 23, 23, 48, 59);

        // null pattern.
        Date now = DateUtil.stringToDate(null);
        assertNotNull(now);
        assertTrue(Math.abs(new Date().getTime() - now.getTime()) <= 100);

        // pattern yyyy-MM-dd
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date result = DateUtil.stringToDate(df.format(sampleDate));
        logConvert(sampleDate, df, result);
        assertDate(result, 2010, 1, 23, 0, 0, 0);

        // pattern yyyy-MM-dd HH:mm:ss
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        result = DateUtil.stringToDate(df.format(sampleDate));
        logConvert(sampleDate, df, result);
        assertDate(result, 2010, 1, 23, 23, 48, 59);

        // pattern yyyy/MM/dd
        df = new SimpleDateFormat("yyyy/MM/dd");
        result = DateUtil.stringToDate(df.format(sampleDate));
        logConvert(sampleDate, df, result);
        assertDate(result, 2010, 1, 23, 0, 0, 0);

        // pattern yyyy/MM/dd HH:mm:ss
        df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        result = DateUtil.stringToDate(df.format(sampleDate));
        logConvert(sampleDate, df, result);
        assertDate(result, 2010, 1, 23, 23, 48, 59);

        // pattern yyyyMMdd
        df = new SimpleDateFormat("yyyyMMdd");
        result = DateUtil.stringToDate(df.format(sampleDate));
        logConvert(sampleDate, df, result);
        assertDate(result, 2010, 1, 23, 0, 0, 0);

        // pattern yyyyMMdd HH:mm:ss
        df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        result = DateUtil.stringToDate(df.format(sampleDate));
        logConvert(sampleDate, df, result);
        assertDate(result, 2010, 1, 23, 23, 48, 59);

        // pattern yyyy-MM
        df = new SimpleDateFormat("yyyy-MM");
        result = DateUtil.stringToDate(df.format(sampleDate));
        logConvert(sampleDate, df, result);
        assertDate(result, 2010, 1, 1, 0, 0, 0);

        // pattern yyyy/MM
        df = new SimpleDateFormat("yyyy/MM");
        result = DateUtil.stringToDate(df.format(sampleDate));
        logConvert(sampleDate, df, result);
        assertDate(result, 2010, 1, 1, 0, 0, 0);

        // pattern yyyyMM
        df = new SimpleDateFormat("yyyyMM");
        result = DateUtil.stringToDate(df.format(sampleDate));
        logConvert(sampleDate, df, result);
        assertDate(result, 2010, 1, 1, 0, 0, 0);

    }

    private void assertDate(Date result, int year, int month,
            int date, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(result);
        assertEquals(year, calendar.get(Calendar.YEAR));
        assertEquals(month, calendar.get(Calendar.MONTH) + 1);
        assertEquals(date, calendar.get(Calendar.DATE));
        assertEquals(hour, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(minute, calendar.get(Calendar.MINUTE));
        assertEquals(second, calendar.get(Calendar.SECOND));
    }

    private void logConvert(Date sampleDate, SimpleDateFormat df, Date result) {
        logger.info("" + sampleDate + "\n => fmt: " + df.format(sampleDate) + "\n => ret: " + result + "\n");
    }

    public void testStringToDateStringString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date result = DateUtil.stringToDate(
                df.format(makeDate(1990, 3, 8, 18, 13, 3)), "yyyy-MM-dd");
        assertDate(result, 1990, 3, 8, 0, 0, 0);
    }

    public void testIllegalArgumentExceptionCase() throws Exception {
        assertNull(DateUtil.stringToDate("", "Zijus"));
        assertNull(DateUtil.dateToString(new Date(), "Zijus"));
    }

}

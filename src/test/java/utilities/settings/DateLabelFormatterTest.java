package utilities.settings;

import org.junit.Before;
import org.junit.Test;
import utilities.DateLabelFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateLabelFormatterTest {

    private DateLabelFormatter dateLabelFormatter;

    private String getDateString(Date date) {
        final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private Date getDateMock() throws ParseException {
        final String dateString = "Sat Jan 12 00:00:00 CST 2019";
        final String desiredFormat = "EEE MMM dd HH:mm:ss z yyyy";
        return new SimpleDateFormat(desiredFormat).parse(dateString);
    }

    private Calendar getCalendarMock() throws ParseException{
        final String dateString = "2019-01-12";
        final String desiredFormat = "yyyy-MM-dd";
        Date date = new SimpleDateFormat(desiredFormat).parse(dateString);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    @Before
    public void setup() {
        dateLabelFormatter = new DateLabelFormatter();
    }

    @Test
    public void testStringToValue_Success() throws ParseException {
        Date dateMock = getDateMock();
        Object actualValue = dateLabelFormatter.stringToValue(getDateString(dateMock));

        assertEquals(dateMock, actualValue);
    }

    @Test(expected = ParseException.class)
    public void testStringToValue_Exception() throws ParseException {
        dateLabelFormatter.stringToValue("some_text");
    }

    @Test
    public void testValueToString_WhenNotNull() throws ParseException {
        String actualValue = dateLabelFormatter.valueToString(getCalendarMock());

        assertEquals("2019-01-12", actualValue);
    }

    @Test
    public void testValueToString_WhenNull() {
        String actualValue = dateLabelFormatter.valueToString(null);

        assertEquals("", actualValue);
    }
}

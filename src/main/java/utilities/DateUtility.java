package utilities;


import domain.beans.SystemSettings;
import literals.ApplicationLiterals;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import utilities.exceptions.AppException;
import utilities.settings.SettingsService;

import java.io.File;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

public class DateUtility {

    public static String getToday() {
        return ApplicationLiterals.YEAR_MONTH_DAY.format(new Date());
    }

    public static int getMonthsSinceJan2016() {
        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(new Date());
        Calendar endCalendar = new GregorianCalendar();
        try {
            endCalendar.setTime(ApplicationLiterals.YEAR_MONTH_DAY.parse("2016-01-01"));
        } catch (ParseException e) {
            throw new AppException(e);
        }

        int diffYear = endCalendar.get(Calendar.YEAR)
                - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH)
                - startCalendar.get(Calendar.MONTH);
        return diffMonth * -1;
    }

    public static String getDeploymentDate() {
        SystemSettings settings = SettingsService.getSystemSettings();

        String productionDirectory = settings.getDeploymentLocation();
        File productionArtifact = new File(productionDirectory +
                ApplicationLiterals.SLASH +
                ApplicationLiterals.APP_ARTIFACT +
                "-" + ApplicationLiterals.VERSION +
                ApplicationLiterals.DOT_JAR);

        long modifiedDate = productionArtifact.lastModified();
        return ApplicationLiterals.MONTH_DAY_YEAR.format(modifiedDate);
    }

    public static JDatePickerImpl getDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        final JDatePickerImpl datePicker = new JDatePickerImpl(datePanel,
                new DateLabelFormatter());
        model.setValue(new Date());
        model.setSelected(true);
        return datePicker;
    }

    private static int getCurrentYear() {
        return Integer.valueOf(ApplicationLiterals.YEAR.format(new Date()));
    }

    private static int getCurrentMonth() {
        return Integer.valueOf(ApplicationLiterals.MONTH.format(new Date()));
    }

    public static boolean isValidReportMonth(int year, int month) {
        LocalDate selectedDate = LocalDate.of(year, month, 1);
        LocalDate currentDate = LocalDate.of(getCurrentYear(), getCurrentMonth(), 1);

        return selectedDate.isBefore(currentDate);
    }
}

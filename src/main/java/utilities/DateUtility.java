package utilities;


import literals.ApplicationLiterals;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import utilities.exceptions.AppException;

import java.io.File;
import java.text.ParseException;
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
        String productionDirectory = ReadConfig.getConfigValue(ApplicationLiterals.DEPLOYMENT_LOCATION);
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
}

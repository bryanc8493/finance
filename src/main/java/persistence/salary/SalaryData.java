package persistence.salary;

import beans.Salary;
import literals.ApplicationLiterals;
import literals.enums.Databases;
import literals.enums.Tables;
import org.apache.log4j.Logger;
import persistence.Connect;
import utilities.exceptions.AppException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Set;

public class SalaryData {

    private static final Logger logger = Logger.getLogger(SalaryData.class);

    public static Set<Salary> getSalaryData() {
        logger.debug("Getting all salary data");
        String SQL_TEXT = "SELECT * FROM " + Databases.FINANCIAL + ApplicationLiterals.DOT
                + Tables.PAY_GRADES;
        Set<Salary> data = new LinkedHashSet<>();

        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(SQL_TEXT);
            while (rs.next()) {
                Salary salary = new Salary();
                salary.setGrade(rs.getInt(1));
                salary.setMinPay(rs.getInt(2));
                salary.setMidPay(rs.getInt(3));
                salary.setMaxPay(rs.getInt(4));
                salary.setStiTarget(rs.getInt(5));
                salary.setStiMax(rs.getInt(6));
                salary.setMtiTarget(rs.getInt(7));
                salary.setMtiMax(rs.getInt(8));
                data.add(salary);
            }
            con.close();
        } catch (Exception e) {
            throw new AppException(e);
        }
        return data;
    }
}

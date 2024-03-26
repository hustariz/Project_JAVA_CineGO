/*
 * DateFormatter.java                                13 déc. 2015
 * CESI RILA 2015/2017
 */
package cineGOv02.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JFormattedTextField.AbstractFormatter;

/**
 * DateFormatter
 * @author Remi
 *
 */
public class DateFormatter extends AbstractFormatter {
    
    /** Pattern de formattage des dates */
    private String datePattern = "yyyy-MM-dd";
    /** Formatteur de date */
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
     
    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }
 
    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }
        return "";
    }
 
}
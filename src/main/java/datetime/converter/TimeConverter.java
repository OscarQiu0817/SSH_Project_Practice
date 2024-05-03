package datetime.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.apache.struts2.util.StrutsTypeConverter;
import com.opensymphony.xwork2.conversion.TypeConversionException;

public class TimeConverter extends StrutsTypeConverter {
	
	public Object convertFromString(Map context, String[] values, Class toClass) {
		DateFormat[] TimeFormat = { 
                new SimpleDateFormat("HH:mm:ssss"),  new SimpleDateFormat("HH:mm"), 
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss") //rfc3399
        };
		for (DateFormat df : TimeFormat)
		  try {
			  System.out.println("●TimeConverter●");
			  java.util.Date date = (java.util.Date) df.parse(values[0]);
			  return new java.sql.Time(date.getTime());
		  } catch (ParseException e) {
			  //e.printStackTrace();
			  //System.out.println("Invalid format - 格示不正確");
			  //throw new TypeConversionException("Invalid format - 格示不正確");
		  }
		return null;
	}

	public String convertToString(Map context, Object obj) {
		System.out.println("●TimeConverter To String●");
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(obj);
	}
	
	public static void main(String args[]) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			java.util.Date date = (java.util.Date) sdf.parse("21:07:31");
			//java.util.Date date = (java.util.Date) sdf.parse("21:07");
			System.out.println("java.sql.Time="+ new java.sql.Time(date.getTime()));

			String str = sdf.format(date);
			System.out.println("str=" + str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}

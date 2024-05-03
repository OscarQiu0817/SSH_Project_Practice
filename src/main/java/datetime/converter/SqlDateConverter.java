package datetime.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.apache.struts2.util.StrutsTypeConverter;
import com.opensymphony.xwork2.conversion.TypeConversionException;

public class SqlDateConverter extends StrutsTypeConverter {

	public Object convertFromString(Map context, String[] values, Class toClass) {
		DateFormat[] SqlDateFormat = { 
				new SimpleDateFormat("yy-MM-dd"),	new SimpleDateFormat("yyyy-MM-dd"),
				new SimpleDateFormat("yy/MM/dd"),	new SimpleDateFormat("yyyy/MM/dd") 
		};
		for (DateFormat df : SqlDateFormat)
			try {
				System.out.println("●SqlDateConverter●");		// 一定會有一個符合，若不符合則拋出例外但不處理。
				java.util.Date date = (java.util.Date) df.parse(values[0]);
				return new java.sql.Date(date.getTime());
			} catch (ParseException e) {
				//e.printStackTrace();
				//System.out.println("Invalid format - 格示不正確");
				//throw new TypeConversionException("Invalid format - 格示不正確");
			}
		return null;
	}

	public String convertToString(Map context, Object obj) {
		System.out.println("●SqlDateConverter To String●");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(obj);
	}

	public static void main(String args[]) {
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		try {
			java.util.Date date = (java.util.Date) sdf.parse("99-07-31");
			//java.util.Date date = (java.util.Date) sdf.parse("1999-07-31");
			//java.util.Date date = (java.util.Date) sdf.parse("99/07/31");
			//java.util.Date date = (java.util.Date) sdf.parse("1999/07/31");
			System.out.println("java.sql.Date="+ new java.sql.Date(date.getTime()));

			String str = sdf.format(date);
			System.out.println("str=" + str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}

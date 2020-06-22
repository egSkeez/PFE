package xtensus.beans.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.richfaces.model.CalendarDataModel;
import org.richfaces.model.CalendarDataModelItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
public class CalendarModel implements CalendarDataModel {
	
	private static final String BUSY_DAY_CLASS = "bdc";
	private static final String BOUNDARY_DAY_CLASS = "rf-ca-boundary-dates";
	@Autowired
	private VariableGlobale vbg;

	private boolean checkWeekend(Calendar calendar) {
		return (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar
				.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY);
	}

	public CalendarDataModelItem[] getData(Date[] dateArray) {
		CalendarDataModelItem[] modelItems = new CalendarModelItem[dateArray.length];
		Calendar current = GregorianCalendar.getInstance();
		Calendar today = GregorianCalendar.getInstance();

		// CalendarDataModelItem[] modelItems = new
		// CalendarDataModelItemImpl[dateArray.length];
		for (int i = 0; i < dateArray.length; i++) {
			CalendarModelItem modelItem = new CalendarModelItem();
			modelItem.setEnabled(true);
			for (int j = 0; j < dateArray.length; j++) {
	            current.setTime(dateArray[j]);
	            
	            if (current.before(today)) {
	                modelItem.setEnabled(false);
	                modelItem.setStyleClass(BOUNDARY_DAY_CLASS);
	            }
	            if (current.after(today)) {
	                modelItem.setEnabled(false);
	                modelItem.setStyleClass(BOUNDARY_DAY_CLASS);
	            }
			}
	            
			for (Date d : getDatesToBeHighlighted()) {
				if (getDatePortion(d).compareTo(getDatePortion(dateArray[i])) == 0) {
					modelItem.setStyleClass(BUSY_DAY_CLASS);
					modelItem.setEnabled(true);
				}
				
			}
			modelItems[i] = modelItem;

		}

		return modelItems;
	}

	public Object getToolTip(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	// This method skips the time part and retuns the date part only
	private Date getDatePortion(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	// This method returns the dates which should be highlighted
	private Date[] getDatesToBeHighlighted() {
		List<Date> dates = new ArrayList<Date>();
		dates = vbg.getDatesSauvgardes();
		Date[] datesArray = dates.toArray(new Date[] {});
		return datesArray;
	}
}
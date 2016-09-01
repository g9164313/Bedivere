package nthu.hpclp.server;

import java.util.Date;

public class UtilsMisc {
	
	public static FmtDate fmtDate = FmtDate.getFormat("yyyy/M/d"); 
	public static FmtDate fmtSql1 = FmtDate.getFormat("yyyy-M-d");
	public static FmtDate fmtSql2 = FmtDate.getFormat("yyyy-M-d HH:mm:ss");
	
	public static FmtDate fmtYear = FmtDate.getFormat("yyyy");
	public static FmtDate fmtMonth = FmtDate.getFormat("M"); 	
	public static FmtDate fmtDayId = FmtDate.getFormat("d"); 
	public static FmtDate fmtDay = FmtDate.getFormat("MM/dd");
	
	public static FmtDate fmtWeek = FmtDate.getFormat("E");
	
	public static FmtDate fmtTime = FmtDate.getFormat("HH:mm");
	
	public static FmtDate fmtMeet = FmtDate.getFormat("yyyy MM/dd H");
	
	public static String date2tw_y(Date stamp){		
		String year = fmtYear.format(stamp);		
		int yy = Integer.valueOf(year);		
		if(yy>1911){
			yy = yy - 1911;
		}else{
			yy = 0;
		}		
		return ""+yy;
	}	
	public static String date2tw_y(){
		return date2tw_y(new Date());
	}
	
	public static String date2tw_d(Date stamp){		
		return date2tw_y(stamp)+"/"+fmtDay.format(stamp);		
	}
	
	public static String twToday(){
		return date2tw_d(new Date());
	}
	
	public static String date2tw_ch(Date stamp){
		String yy = date2tw_y(stamp);
		String mm = fmtMonth.format(stamp);
		String dd = fmtDayId.format(stamp);
		return yy+"年"+mm+"月"+dd+"日";		
	}	
}

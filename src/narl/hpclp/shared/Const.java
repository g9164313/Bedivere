package narl.hpclp.shared;

import java.util.LinkedList;

public final class Const {
	
	public static final String DATABASE_URL = "jdbc:postgresql://localhost:8765/bookkeeping";
	public static final String DATABASE_USER = "qq";
	public static final String DATABASE_PASS = "qq";
		
	public static final String TABLE0 = "param";
	public static final String TABLE1 = "owner";
	public static final String TABLE2 = "tenure";
	public static final String TABLE3 = "account";
	public static final String TABLE4 = "product";
	public static final String TABLE5 = "zipcode";
	
	public static final String CUSTOM_ITEM = "--自訂--";
	
	public static final String SPECIAL_PREFIX = "調整";

	public static final String URL_PATT1 = "report_letter.pdf";
	public static final String URL_PATT2 = "report_notify.pdf";
	public static final String URL_PATT3 = "report_service.pdf";
	public static final String URL_PATT4 = "report_demand.pdf";
	
	public static String insertSlash(String unit1, String unit2){
		unit1 = invertSlash(unit1);
		unit2 = invertSlash(unit2);
		if(unit1.equals(unit2)==true){
			return "";
		}
		return unit1+"/"+unit2;
	}
	
	public static String invertSlash(String txt){		
		String fac = null;
		int pos = txt.indexOf('/');
		if(pos<0){
			return txt;
		}else{
			//emit_unit = emit_unit.replace('/', '·');
			fac = txt.substring(0, pos);
			txt = txt.substring(pos+1);			
		}
		String[] unit = txt.split("·");
		for(pos=0; pos<unit.length; pos++){
			String uu = unit[pos];
			if(uu.indexOf("⁻¹")>=0){
				uu = uu.replace("⁻¹", "");
			}else if(uu.indexOf("⁻²")>=0){
				uu = uu.replace("⁻²", "²");
			}else if(uu.indexOf("⁻³")>=0){
				uu = uu.replace("⁻³", "³");
			}else if(uu.indexOf("²")>=0){
				uu = uu.replace("²", "⁻²");
			}else if(uu.indexOf("³")>=0){
				uu = uu.replace("³", "⁻³");				
			}else{
				uu = uu+"⁻¹";
			}			
			unit[pos] = uu;
		}
		for(String uu:unit){
			fac = fac+'·'+uu;
		}		
		return fac;
	}
}


package nthu.hpclp.shared;

public final class Const {
	
	public static final String DATABASE_URL_MASTER = "jdbc:postgresql://140.114.104.50:8765/bookkeeping";
	public static final String DATABASE_URL_ASSIST = "jdbc:postgresql://localhost:8765/bookkeeping";
	public static final String DATABASE_USER = "qq";
	public static final String DATABASE_PASS = "qq";
		
	public static final String PARAM = "param";
	public static final String OWNER = "owner";
	public static final String TENUR = "tenure";
	public static final String ACCNT = "account";
	public static final String PRODX = "product";

	public static final String CUSTOM_ITEM = "--自訂--";
	
	public static final String SPECIAL_PREFIX = "調整";
	public static final String SPECIAL_PREFIX1 = "調整後";
	
	public static final String PRINT_SCHEDULE= "排定表.pdf";
	public static final String PRINT_LETTER  = "信封.pdf";
	public static final String PRINT_NOTIFY  = "校正通知單.pdf";	
	public static final String REPORT_PRODUCT= "校正報告.pdf";
	public static final String REPORT_DEMAND = "繳費通知單.pdf";
	public static final String REPORT_SERVICE= "費用通知單.pdf";
	public static final String REPORT_TENURE = "儀器報告.xls";
	
	public static String int2note(int val){
		char[] digi=String.valueOf(val).toCharArray();
		String txt="";
		txt = "";
		int cnt = digi.length;
		for(int i=cnt-1; i>=0; --i){
			int idx = i - cnt + 1;
			if(idx%3==0 && idx!=0){
				txt = ","+txt;
			}
			txt = digi[i] + txt;
		}
		return txt;
	}
	
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
	
	private static double TableLength[][] = {
		/*S\D      mm,    cm,     in*/
		/*mm*/{  1f  , 10f  , 0.03937f },
		/*cm*/{ 10f  , 1.f  , 0.393701f},
		/*in*/{ 25.4f, 2.54f, 1f       }
	};
	
	public static double exchange_area(String value, String u1, String u2){
		double v = 1.f;
		try{
			v = Double.valueOf(value);
		}catch(NumberFormatException e){
			v = 1.f;
		}
		return convertArea(v,u1,u2);
	}
	
	public static double convertArea(
		double value, 
		String srcUnit, 
		String dstUnit
	){
		int src=-1, dst=-1;
		
		if(srcUnit.indexOf("mm")>=0){ src = 0; }
		if(srcUnit.indexOf("cm")>=0){ src = 1; }
		if(srcUnit.indexOf("in")>=0){ src = 2; }
		
		if(dstUnit.indexOf("mm")>=0){ dst = 0; }
		if(dstUnit.indexOf("cm")>=0){ dst = 1; }
		if(dstUnit.indexOf("in")>=0){ dst = 2; }
		
		if(src==-1 || dst==-1){
			return value;
		}
		return value * TableLength[src][dst] * TableLength[src][dst];
	}
	
	public static String getValOrUnit(String vals,boolean is_unit){		
		String[] val = vals.split(",");		
		if(val.length==0){
			return "0";
		}		
		if(is_unit){
			val[0] = TrimNumber(val[0]);
		}else{
			val[0] = TrimUnit(val[0]);
		}
		return val[0];
	}
	
	public static String TrimUnit(String src){
		src = src.replaceAll("\\s","");//trim space!!!
		String dst = "";
		char[] list = src.toCharArray();
		for(char cc:list){
			if( 
				('0'<=cc && cc<='9') || cc=='.'
			){
				dst = dst + cc;
			}
		}		
		return dst.trim();
	}
	
	public static boolean isAllDigit(String txt){
		char[] buf = txt.toCharArray();
		for(int i=0; i<buf.length; i++){
			if(buf[i]<'0' || '9'<buf[i]){
				return false;
			}
		}
		return true;
	}
	
	public static String TrimNumber(String src){
		src = src.replaceAll("\\s","");//trim space!!!
		String dst = "";
		char[] list = src.toCharArray();
		for(char cc:list){
			if( 
				('0'<=cc && cc<='9') || cc=='.'
			){
				continue;
			}
			dst = dst + cc;
		}		
		return dst.trim();
	}
}


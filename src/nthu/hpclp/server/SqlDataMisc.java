package nthu.hpclp.server;

import java.sql.ResultSet;
import java.sql.SQLException;

import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemAccnt;
import nthu.hpclp.shared.ItemProdx;

/**
 * Some unclassified functions for data base, like checking serial.....
 * @author qq
 *
 */
public class SqlDataMisc {

	/**
	 * generate a four digital number serial-number
	 * @param arg - a number which user wants to approximate
	 * @return serial number
	 */
	public static String genOKey(String arg) {		
		
		int base = 0;
		if(arg.length()!=0){
			try{
				base = Integer.valueOf(arg);
			}catch(NumberFormatException e){				
				return arg;//not a number, just pass~~~~
			}
		}
		
		int val = 0;
		String cmd = 
			"SELECT generate_series(1,9999,1) AS key " +
			"EXCEPT "+
			"("+
			"SELECT DISTINCT to_number(info[1],'9999') "+ 
			"FROM owner "+
			"WHERE info[1] SIMILAR TO '\\d{4}' "+ 
			") ORDER BY key DESC";
		try {
			ResultSet rs = RpcBridge.getResult(cmd);
			int min = Integer.MAX_VALUE;
			while(rs.next()){
				int _v = rs.getInt(1);
				int diff = Math.abs(base-_v);
				if(diff<min){
					val = _v;
					min = diff;
				}
			}		
		} catch (SQLException e) {			
			e.printStackTrace();
			return "0000";
		}
		return Const.formatDigit(val,4);
	}
	
	public static String genAKey(String arg) {		

		final int pkey = ItemAccnt.INFO_AKEY+1;//SQL is one-base;		
		
		String cmd = 
			"SELECT info["+pkey+"] FROM "+Const.ACCNT+" "+
			"WHERE info["+pkey+"] SIMILAR TO '"+arg+"CL\\d{6}' "+
			"ORDER BY info["+pkey+"] DESC LIMIT 1";
		
		try {
			ResultSet rs = RpcBridge.getResult(cmd);
			rs.next();
			if(rs.getRow()==0){
				arg = arg+"CL"+Pad0(1,6);
			}else{
				String txt = rs.getString(1);
				txt = txt.substring(txt.indexOf("CL")+2);//TODO:check this method~~~~
				int idx = Integer.valueOf(txt) + 1;
				arg = arg+"CL"+Pad0(idx,6);
			}			
		} catch (SQLException e) {			
			e.printStackTrace();
			return "";
		}
		return arg;
	}
	
	public static String genPKey(String[] arg) {		
		if(arg.length!=2){
			return "???";
		}
		
		final int pkey = ItemProdx.INFO_PKEY+1;//SQL is one-base;
		String patx = arg[0]+"-"+arg[1];
		String cmd = 
			"SELECT info["+pkey+"] FROM "+Const.PRODX+" "+
			"WHERE info["+pkey+"] SIMILAR TO '"+patx+"-\\d{3}-\\d{1}' "+
			"ORDER BY info["+pkey+"] DESC LIMIT 1";

		try {
			ResultSet rs = RpcBridge.getResult(cmd);
			rs.next();
			if(rs.getRow()==0){				
				patx = patx + "001-0";				
			}else{				
				String[] val = rs.getString(1).split("-");				
				int idx = Integer.valueOf(val[2]) + 1;				
				int cnt = Integer.valueOf(val[3]);				
				patx = val[0]+"-"+val[1]+"-"+Pad0(idx,3)+"-"+cnt;
			}
		} catch (SQLException e) {			
			e.printStackTrace();
			return "";
		}			
		return patx;
	}
	
	private static String Pad0(int val,int size){
		String txt = "" + val;
		int cnt = size - txt.length();
		for(int i=0; i<cnt; i++){
			txt = "0" + txt;
		}
		return txt;
	}
}

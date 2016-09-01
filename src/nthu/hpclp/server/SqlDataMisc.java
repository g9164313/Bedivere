package nthu.hpclp.server;

import java.sql.ResultSet;
import java.sql.SQLException;

import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemAccnt;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemProdx;

/**
 * Some unclassified functions for data base, like checking serial.....
 * @author qq
 *
 */
public class SqlDataMisc {

	public static String genOKey(String arg) {		
		
		if(Const.isAllDigit(arg)==false){
			return arg;
		}
		
		if(arg.length()<4){
			for(int i=arg.length(); i<4; i++){
				arg = arg+"%";
			}
		}
		
		String rng0 = arg.replace('%', '0');
		String rng9 = arg.replace('%', '9');
		
		final int idx=ItemOwner.INFO_OKEY+1;//SQL is 1-base.....
		
		String cmd = 
			"SELECT generate_series("+rng0+","+rng9+",1) AS key " +
			"EXCEPT "+
			"(SELECT DISTINCT to_number(info["+idx+"],'9999') AS key "+ 
			"FROM "+Const.OWNER+" "+
			"WHERE info["+idx+"] SIMILAR TO '"+arg+"') "+ 
			"ORDER BY key";
		try {
			ResultSet rs = RpcBridge.getResult(cmd);
			rs.next();
			if(rs.getRow()==0){
				return "????";
			}else{
				arg = rs.getString(1);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
			return "";
		}		
		return arg;
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

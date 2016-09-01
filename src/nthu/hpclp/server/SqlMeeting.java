package nthu.hpclp.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemMeeting;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemTenur;

public class SqlMeeting {

	public static HashMap<String,String> listRestday(
		String dayFst, 
		String dayEnd
	) throws SQLException {
		
		long tickFst = UtilsMisc.fmtSql2.parse(dayFst).getTime();
		long tickEnd = UtilsMisc.fmtSql2.parse(dayEnd).getTime();
		
		HashMap<String,String> lst = new HashMap<String,String>();
		
		String cmd = "SELECT * FROM PARAM WHERE key SIMILAR TO '%RESTDAY_%'";
			
		ResultSet rs = RpcBridge.getResult(cmd);
		while(rs.next()){
			String txt = rs.getString("val");
			String[] arg = txt.split("@");
			String _day,memo;
			if(arg.length==1){
				_day = arg[0];
				memo = "假日";//default comment
			}else if(arg.length==2){
				_day = arg[0];
				memo = arg[1];
			}else{
				continue;
			}
			long tickCur = UtilsMisc.fmtDate.parse(_day).getTime();
			if(tickFst<=tickCur && tickCur<=tickEnd){
				if(arg[1].length()==0){
					lst.put(arg[0], "假日");
				}else{
					
				}
				lst.put(_day,memo);
			}
		}
		return lst;
	}
	
	public static ArrayList<ItemMeeting> list(
		ArrayList<ItemMeeting> lst,
		String dayFst, 
		String dayEnd
	) throws SQLException {
		
		//first, prepare restday~~
		HashMap<String,String> restday = listRestday(dayFst,dayEnd);
		ArrayList<String> remnday = new ArrayList<String>();
		
		final int idxOwnerKey = ItemOwner.INFO_OKEY + 1;//1-base;
		final int idxTenurKey = ItemTenur.INFO_TKEY + 1;//1-base;
		
		//second, list all tenures with owner
		String cmd =
			"SELECT "+
			Const.OWNER+".id AS oid, "+
			Const.OWNER+".info AS o_info,"+
			Const.OWNER+".last AS o_last,"+
			Const.TENUR+".id AS tid, "+
			Const.TENUR+".info AS t_info, "+
			Const.TENUR+".stamp AS t_stmp, "+
			Const.TENUR+".last AS t_last, "+  
			Const.TENUR+".meet AS t_meet "+
			" FROM "+Const.OWNER+" JOIN "+Const.TENUR+
			" ON "+Const.TENUR+".oid=owner.id"+
			" WHERE '"+dayFst+"'<="+Const.TENUR+".meet AND "+Const.TENUR+".meet<='"+dayEnd+"' "+
			" ORDER BY "+
			Const.TENUR+".meet ASC, "+
			Const.OWNER+".info["+idxOwnerKey+"] ASC, "+
			Const.TENUR+".info["+idxTenurKey+"] ASC;";
	
		ItemMeeting meet = new ItemMeeting();//the first item~~~
		ResultSet rs = RpcBridge.getResult(cmd);
		while(rs.next()){
			Date t0 = rs.getTimestamp("o_last");
			Date t1 = rs.getTimestamp("t_stmp");
			Date t2 = rs.getTimestamp("t_last");
			Date t3 = rs.getTimestamp("t_meet");
			ItemTenur tenu = new ItemTenur(
				RpcBridge.uuid2flat(rs,"tid"),
				RpcBridge.info2flat(rs,"t_info"),
				t1,t2,t3
			);
						
			String   oid = RpcBridge.uuid2flat(rs,"oid");				
			String[] info = RpcBridge.info2flat(rs,"o_info"); 
			String   t_t3 = UtilsMisc.fmtDate.format(t3);
			if(meet.getKey().equalsIgnoreCase(info[ItemOwner.INFO_OKEY])==false){
				//same owner but different meeting day (different tenure)
				meet = new ItemMeeting(oid,info,t_t3,t3,t0);
				meet.lst.add(tenu);
				tenu.owner= meet;
				lst.add(meet);				
			}else{
				//same owner and meeting day~~~
				meet.lst.add(tenu);
				tenu.owner = meet;
			}
			//always check whether day is holiday~~~
			String rday = restday.get(meet.getSDay());
			if(rday!=null){
				meet.setRestday(rday);
				if(remnday.contains(rday)==false){
					remnday.add(rday);
				}
			}
		}
	
		//third, list owners which are needed to meet
		cmd = "SELECT * FROM "+Const.OWNER+
			" WHERE '"+dayFst+"'<=stamp AND stamp<='"+dayEnd+"'";
		
		rs = RpcBridge.getResult(cmd);
		while(rs.next()){
			Date stmp = rs.getTimestamp("stamp");
			Date last = rs.getTimestamp("stamp");
			meet = new ItemMeeting(
				RpcBridge.uuid2flat(rs,"id"),
				RpcBridge.info2flat(rs,"info"),
				UtilsMisc.fmtDate.format(stmp),
				stmp,last
			);
			lst.add(meet);
			//always check whether day is holiday~~~
			String rday = restday.get(meet.getSDay());
			if(rday!=null){
				meet.setRestday(rday);
				if(remnday.contains(rday)==false){
					remnday.add(rday);
				}
			}			
		}
		
		//finall, add remaind restday~~~~
		for(String rday:restday.keySet()){
			if(remnday.contains(rday)==true){
				continue;
			}
			meet = new ItemMeeting(
				rday,
				restday.get(rday),
				UtilsMisc.fmtDate.parse(rday)
			);
			lst.add(meet);
		}
		return lst;
	}
}

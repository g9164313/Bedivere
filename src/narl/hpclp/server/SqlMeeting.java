package narl.hpclp.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import narl.hpclp.shared.Const;
import narl.hpclp.shared.ItemMeeting;
import narl.hpclp.shared.ItemOwner;
import narl.hpclp.shared.ItemTenur;

public class SqlMeeting {

	public static HashMap<String,String> listRestday(
		String dayFst, 
		String dayEnd
	){
		HashMap<String,String> lst = new HashMap<String,String>();
		
		return lst;
	}
	
	public static ArrayList<ItemMeeting> list(
		ArrayList<ItemMeeting> lst,
		String dayFst, 
		String dayEnd
	) throws SQLException {
		
		final int idxOwnerKey = ItemOwner.INFO_OKEY + 1;//1-base;
		final int idxTenurKey = ItemTenur.INFO_TKEY + 1;//1-base;
		
		//first, list all tenures with owner
		String cmd =
			"SELECT "+
			Const.OWNER+".id AS oid, "+
			Const.OWNER+".info AS o_info,"+
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
			Date t1 = rs.getTimestamp("t_stmp");
			Date t2 = rs.getTimestamp("t_last");
			Date t3 = rs.getTimestamp("t_meet");
			ItemTenur tenu = new ItemTenur(
				RpcBridge.uuid2flat(rs,"tid"),
				RpcBridge.info2flat(rs,"t_info"),
				t1,t2,t3
			);
			
			String   day = RpcBridge.fmtDay.format(t3);
			String   oid = RpcBridge.uuid2flat(rs,"oid");				
			String[] inf = RpcBridge.info2flat(rs,"o_info"); 				
			if(meet.getKey().equalsIgnoreCase(inf[ItemOwner.INFO_OKEY])==false){
				//same owner but different meeting day (different tenure)
				meet = new ItemMeeting(oid,inf,day,rs.getTimestamp("t_meet"));
				meet.lst.add(tenu);
				tenu.owner= meet;
				lst.add(meet);
				continue;
			}				
			meet.lst.add(tenu);
			tenu.owner = meet;
		}
	
		//second, list owners which are needed to meet
		cmd = "SELECT * FROM "+Const.OWNER+
			" WHERE '"+dayFst+"'<=stamp AND stamp<='"+dayEnd+"'";
		
		rs = RpcBridge.getResult(cmd);
		while(rs.next()){
			Date day = rs.getTimestamp("stamp");
			meet = new ItemMeeting(
				RpcBridge.uuid2flat(rs,"id"),
				RpcBridge.info2flat(rs,"info"),
				RpcBridge.fmtDay.format(day),
				day
			);
			lst.add(meet);
		}
		
		//third, list all restday and check list again~~
		HashMap<String,String> restday = listRestday(dayFst,dayEnd);
		if(restday.size()!=0){
			for(ItemMeeting itm:lst){
				String txt = restday.get(itm.day);
				if(txt!=null){
					itm.setRestday(txt);
				}
			}
		}
		return lst;
	}
	
}

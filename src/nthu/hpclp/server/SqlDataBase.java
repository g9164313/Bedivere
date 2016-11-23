package nthu.hpclp.server;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemAccnt;
import nthu.hpclp.shared.ItemMeeting;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemParam;
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ItemTenur;

/**
 * The prefix,'Modify' means 'insert', 'update' and 'delete'.<p>
 * @author qq
 *
 */
public class SqlDataBase {

	public static PreparedStatement insertOwner,insertTenur,insertAccnt,insertProdx,insertParam;	
	public static PreparedStatement updateOwner,updateTenur,updateAccnt,updateProdx,updateParam;	
	public static PreparedStatement deleteOwner,deleteTenue,deleteAccnt,deleteProdx,deleteParam;
	
	public static void prepare(Connection conn) throws SQLException{
		
		insertOwner = conn.prepareStatement(SQL_INSERT_OWNER);
		insertTenur = conn.prepareStatement(SQL_INSERT_TENUR);
		insertAccnt = conn.prepareStatement(SQL_INSERT_ACCNT);
		insertProdx = conn.prepareStatement(SQL_INSERT_PRODX);
		insertParam = conn.prepareStatement(SQL_INSERT_PARAM);
		//---------------------------//
		updateOwner = conn.prepareStatement(SQL_UPDATE_OWNER);	
		updateTenur = conn.prepareStatement(SQL_UPDATE_TENUR);
		updateAccnt = conn.prepareStatement(SQL_UPDATE_ACCNT);
		updateProdx = conn.prepareStatement(SQL_UPDATE_PRODX);
		updateParam = conn.prepareStatement(SQL_UPDATE_PARAM);
		//---------------------------//
		deleteOwner = conn.prepareStatement(SQL_DELETE_OWNER);
		deleteTenue = conn.prepareStatement(SQL_DELETE_TENUR);
		deleteAccnt = conn.prepareStatement(SQL_DELETE_ACCNT);
		deleteProdx = conn.prepareStatement(SQL_DELETE_PRODX);
		deleteParam = conn.prepareStatement(SQL_DELETE_PARAM);
	}
	//--------------------------//
	
	public static ArrayList<ItemOwner> listOwner(String postfix) {
		ArrayList<ItemOwner> lst = new ArrayList<ItemOwner>();
		try {			
			String cmd = "SELECT "+
				Const.OWNER+".id AS id, "+
				Const.OWNER+".info AS info, "+
				Const.OWNER+".stamp AS stamp, "+
				Const.OWNER+".last AS last "+
				"FROM "+Const.OWNER+" "+		
				postfix;		
			ResultSet rs = RpcBridge.getResult(cmd);	
			while(rs.next()){
				lst.add(unpackOwner(rs));
			}
		} catch (SQLException e) {			
			System.err.print(e.getMessage());
		}
		return lst;
	}
	
	public static ArrayList<ItemOwner> listOwnerByRow(int offset, int limit) {
		ArrayList<ItemOwner> lst = new ArrayList<ItemOwner>();		
		try {
			String cmd = "SELECT ROW_NUMBER() OVER (ORDER BY info[1]) AS idx,"+
				Const.OWNER+".id AS id, "+
				Const.OWNER+".info AS info, "+
				Const.OWNER+".stamp AS stamp, "+
				Const.OWNER+".last AS last "+
				"FROM "+Const.OWNER+" "+
				"ORDER BY info[1] "+
				"OFFSET "+offset+" LIMIT "+limit;		
			ResultSet rs = RpcBridge.getResult(cmd);	
			while(rs.next()){
				lst.add(unpackOwner(rs));
			}
			//special, we got a total row number and stuff this value to the first item~~~
			if(lst.size()!=0){			
				rs = RpcBridge.getResult("SELECT COUNT(*) FROM "+Const.OWNER);
				rs.next();
				String[] val = {""+rs.getInt(1)};
				lst.get(0).appx = val;				
			}
		} catch (SQLException e) {			
			System.err.print(e.getMessage());
		}
		return lst;
	}
	
	public static ArrayList<ItemTenur> listTenur(String postfix) {
		ArrayList<ItemTenur> lst = new ArrayList<ItemTenur>();
		try {			
			String cmd = "SELECT "+
				Const.TENUR+".id AS id, "+				
				Const.TENUR+".info AS info, "+
				Const.TENUR+".stamp AS stamp, "+
				Const.TENUR+".last AS last, "+
				Const.TENUR+".meet AS meet, "+
				Const.OWNER+".id AS oid, "+
				Const.OWNER+".info AS oinfo, "+
				Const.OWNER+".stamp AS ostamp, "+
				Const.OWNER+".last AS olast "+
				"FROM "+Const.TENUR+" "+
				"LEFT JOIN "+Const.OWNER+" ON "+Const.OWNER+".id="+Const.TENUR+".oid "+
				postfix;		
			ResultSet rs = RpcBridge.getResult(cmd);
			while(rs.next()){
				lst.add(unpackTenur(rs));
			}			
		} catch (SQLException e) {			
			System.err.print(e.getMessage());
		}
		return lst;
	}
	
	public static ArrayList<ItemTenur> listTenurByRow(int offset, int limit) {
		ArrayList<ItemTenur> lst = new ArrayList<ItemTenur>();		
		try {
			String cmd = "SELECT ROW_NUMBER() OVER (ORDER BY "+
				"tenure.info[3],tenure.info[4],tenure.info[6],tenure.info[7]"+
				") AS idx,"+
				Const.TENUR+".id AS id, "+				
				Const.TENUR+".info AS info, "+
				Const.TENUR+".stamp AS stamp, "+
				Const.TENUR+".last AS last, "+
				Const.TENUR+".meet AS meet, "+
				Const.OWNER+".id AS oid, "+
				Const.OWNER+".info AS oinfo, "+
				Const.OWNER+".stamp AS ostamp, "+
				Const.OWNER+".last AS olast "+
				"FROM "+Const.TENUR+" "+
				"LEFT JOIN "+Const.OWNER+" ON "+Const.OWNER+".id="+Const.TENUR+".oid "+
				"ORDER BY tenure.info[3],tenure.info[4],tenure.info[6],tenure.info[7]"+
				"OFFSET "+offset+" LIMIT "+limit;		
			ResultSet rs = RpcBridge.getResult(cmd);	
			while(rs.next()){
				lst.add(unpackTenur(rs));
			}
			//special, we got a total row number and stuff this value to the first item~~~
			if(lst.size()!=0){			
				rs = RpcBridge.getResult("SELECT COUNT(*) FROM "+Const.TENUR);
				rs.next();
				String[] val = {""+rs.getInt(1)};
				lst.get(0).appx = val;				
			}
		} catch (SQLException e) {			
			System.err.print(e.getMessage());
		}
		return lst;
	}
	
	public static ArrayList<ItemProdx> listProdx(String postfix) {
		ArrayList<ItemProdx> lst = new ArrayList<ItemProdx>();
		try {			
			String cmd = "SELECT "+
				Const.PRODX+".id AS id, "+
				Const.PRODX+".info AS info, "+
				Const.PRODX+".stamp AS stamp, "+
				Const.PRODX+".last AS last, "+
				Const.PRODX+".format AS format, "+
				Const.PRODX+".scribble AS scribble, "+				
				Const.OWNER+".id AS oid, "+
				Const.OWNER+".info AS oinfo, "+
				Const.OWNER+".stamp AS ostamp, "+
				Const.OWNER+".last AS olast, "+
				Const.TENUR+".id AS tid, "+				
				Const.TENUR+".info AS tinfo, "+
				Const.TENUR+".stamp AS tstamp, "+
				Const.TENUR+".last AS tlast, "+
				Const.TENUR+".oid AS towner "+
				"FROM "+Const.PRODX+" "+		
				"LEFT JOIN "+Const.OWNER+" ON "+Const.OWNER+".id="+Const.PRODX+".oid "+
				"LEFT JOIN "+Const.TENUR+" ON "+Const.TENUR+".id="+Const.PRODX+".tid "+
				postfix;		
			ResultSet rs = RpcBridge.getResult(cmd);	
			while(rs.next()){
				ItemProdx itm = unpackProdx(rs);
				itm.purge();
				lst.add(itm);
			}
		} catch (SQLException e) {			
			System.err.print(e.getMessage());
		}
		return lst;
	}	
	//---------------------------------------//
	
	private static ItemOwner unpackOwner(ResultSet rs) throws SQLException {
		ItemOwner item = new ItemOwner(
			getUUID(rs,"id"), 
			getTxtArray(rs,"info"), 
			rs.getTimestamp("stamp"), 
			rs.getTimestamp("last")
		);
		return item;
	}
	
	private static ItemTenur unpackTenur(ResultSet rs) throws SQLException {
		ItemTenur item = new ItemTenur(
			getUUID(rs,"id"), 
			getTxtArray(rs,"info"), 
			rs.getTimestamp("stamp"), 
			rs.getTimestamp("last")
		);
		
		item.setMeet(rs.getTimestamp("meet"));
		
		String uuid = getUUID(rs,"oid");
		if(uuid.length()!=0){
			item.setOwner(new ItemOwner(
				uuid,
				getTxtArray(rs,"oinfo"),
				rs.getTimestamp("ostamp"),
				rs.getTimestamp("olast")
			));
		}
		return item;
	}
	
	private static ItemProdx unpackProdx(ResultSet rs) throws SQLException {

		ItemProdx item = new ItemProdx(
			getUUID(rs,"id"), 
			getTxtArray(rs,"info"), 
			rs.getTimestamp("stamp"), 
			rs.getTimestamp("last")
		);
		
		item.setFormat(rs.getInt("format"));
		
		String[] val = getTxtArray(rs,"scribble");
		if(val!=null){
			for(String v:val){
				item.scribble.add(v);
			}
		}

		String uuid = "";
		uuid = getUUID(rs,"oid");
		ItemOwner owner = null;
		if(uuid.length()!=0){
			owner = new ItemOwner(
				uuid,
				getTxtArray(rs,"oinfo"),
				rs.getTimestamp("ostamp"),
				rs.getTimestamp("olast")
			);
			item.setOwner(owner);
		}
		
		uuid = getUUID(rs,"tid");
		ItemTenur tenur = null;
		if(uuid.length()!=0){
			tenur = new ItemTenur(
				uuid,
				getTxtArray(rs,"tinfo"),
				rs.getTimestamp("tstamp"),
				rs.getTimestamp("tlast")
			);
			item.setTenur(tenur);
			
			uuid = getUUID(rs,"towner");
			if(uuid.length()!=0){
				if(owner!=null){
					if(uuid.equalsIgnoreCase(owner.uuid)==true){
						tenur.setOwner(owner);
						return item;
					}
				}
				//TODO:search owner again!!!
				String cmd = "SELECT "+
					Const.OWNER+".id AS id, "+
					Const.OWNER+".info AS info, "+
					Const.OWNER+".stamp AS stamp, "+
					Const.OWNER+".last AS last "+
					"FROM "+Const.OWNER+" "+		
					"WHERE id='"+uuid+"'";		
				ResultSet tmp = RpcBridge.getResult(cmd);	
				tmp.next();
				tenur.setOwner(unpackOwner(tmp));				
			}
		}
		return item;
	}
	//--------------------------//
	
	private static final String SQL_INSERT_OWNER = "INSERT INTO "+Const.OWNER+
		" (info,stamp,last,id) VALUES(?,?,?,?)";
	
	private static final String SQL_UPDATE_OWNER = "UPDATE "+Const.OWNER+
		" SET info=?, stamp=?, last=? WHERE id=?";
	
	private static final String SQL_DELETE_OWNER = "DELETE FROM "+Const.OWNER+" WHERE id=?";

	public static ItemOwner modifyOwner(ItemOwner obj) throws IllegalArgumentException {
		try {
			if(obj.uuid.length()==0){
				//create a new one~~~
				obj.uuid = UUID.randomUUID().toString();
				setter(insertOwner,obj);
			}else if(obj.isDelete()==true){
				//delete it!!!
				obj.uuid = obj.uuid.substring(1);
				deleteOwner.setObject(1,UUID.fromString(obj.uuid));
				deleteOwner.execute();
			}else if(obj.isModify()==true){
				//modify it~~~
				obj.uuid = obj.uuid.substring(1);
				setter(updateOwner,obj);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
			obj.error = e.getMessage();//update last error message
		}
		return obj;
	}
	private static void setter(PreparedStatement sta, ItemOwner obj) throws SQLException{
		mapInfo(sta,1,obj.info);
		mapDate(sta,2,obj.stmp);
		mapDate(sta,3,obj.last);
		mapUUID(sta,4,obj.uuid);
		sta.execute();
	}
	
	
	private static final String SQL_INSERT_TENUR = "INSERT INTO "+Const.TENUR+
		" (info,stamp,last,meet,dirty,oid,id) VALUES(?,?,?,?,?,?,?)";
	
	private static final String SQL_UPDATE_TENUR = "UPDATE "+Const.TENUR+
		" SET info=?, stamp=?, last=?, meet=?, dirty=?, oid=? WHERE id=?";
	
	private static final String SQL_DELETE_TENUR = "DELETE FROM "+Const.TENUR+" WHERE id=?";
	
	public static ItemTenur modifyTenur(ItemTenur obj) throws IllegalArgumentException {
		try {
			if(obj.uuid.length()==0){
				//create a new one
				obj.uuid = UUID.randomUUID().toString();
				setter(insertTenur,obj);		
			}else if(obj.isDelete()==true){
				//delete it!!!
				obj.uuid = obj.uuid.substring(1);
				deleteTenue.setObject(1,UUID.fromString(obj.uuid));
				deleteTenue.execute();
			}else if(obj.isModify()==true){
				//modify it~~~
				obj.uuid = obj.uuid.substring(1);
				setter(updateTenur,obj);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
			obj.error = e.getMessage();//update last error message
		}
		return obj;
	}
	private static void setter(PreparedStatement sta, ItemTenur obj) throws SQLException{
		mapInfo(sta,1,obj.info);	
		mapDate(sta,2,obj.stmp);	
		mapDate(sta,3,obj.last);
		mapDate(sta,4,obj.meet);
		sta.setBoolean(5,true);
		ItemOwner owner = obj.getOwner();
		if(owner!=null){
			mapUUID(sta,6,owner.uuid);
		}else{
			mapUUID(sta,6,null);
		}
		mapUUID(sta,7,obj.uuid);
		sta.execute();	
	}

	private static final String SQL_INSERT_ACCNT = "INSERT INTO "+Const.ACCNT+
		" (info,stamp,last,fare,final,oid,id) VALUES(?,?,?,?,?,?,?)";
	
	private static final String SQL_UPDATE_ACCNT = "UPDATE "+Const.ACCNT+
		" SET info=?, stamp=?, last=?, fare=?, final=?, oid=? WHERE id=?";
	
	private static final String SQL_DELETE_ACCNT = "DELETE FROM "+Const.ACCNT+" WHERE id=?";

	public static ItemAccnt modifyAccnt(ItemAccnt obj) throws IllegalArgumentException {
		try {
			if(obj.uuid.length()==0){
				obj.uuid = UUID.randomUUID().toString();
				setter(insertAccnt,obj);//create a new one
				//TODO:synchonize relationship			
			}else if(obj.isDelete()==true){
				//delete it!!!
				obj.uuid = obj.uuid.substring(1);
				deleteAccnt.setObject(1,UUID.fromString(obj.uuid));
				deleteAccnt.execute();
			}else if(obj.isModify()==true){
				//modify it~~~
				obj.uuid = obj.uuid.substring(1);
				setter(updateAccnt,obj);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
			obj.error = e.getMessage();//update last error message
		}
		return obj;
	}
	private static void setter(PreparedStatement sta, ItemAccnt obj) throws SQLException{
		mapInfo(sta,1,obj.info);	
		mapDate(sta,2,obj.stmp);	
		mapDate(sta,3,obj.last);
		mapArray(sta,4,obj.fare);
		sta.setBoolean(5,obj.isFinal);
		ItemOwner owner = obj.getOwner();
		if(owner!=null){
			mapUUID(sta,6,owner.uuid);
		}else{
			mapUUID(sta,6,null);
		}
		mapUUID(sta,7,obj.uuid);
		sta.execute();
	}
	
	
	private static final String SQL_INSERT_PRODX = "INSERT INTO "+Const.PRODX+
		" (info,stamp,last,format,scribble,oid,tid,id) VALUES(?,?,?,?,?,?,?,?)";
	
	private static final String SQL_UPDATE_PRODX = "UPDATE "+Const.PRODX+
		" SET info=?, stamp=?, last=?, format=?, scribble=?, oid=?, tid=? WHERE id=?";
	
	private static final String SQL_DELETE_PRODX = "DELETE FROM "+Const.PRODX+" WHERE id=?";
	
	public static ItemProdx modifyProdx(ItemProdx obj) throws IllegalArgumentException {
		try {
			if(obj.uuid.length()==0){
				//create a new one
				obj.uuid = UUID.randomUUID().toString();
				setter(insertProdx,obj);
				cementRelation(obj);
			}else if(obj.isDelete()==true){
				//delete it!!!
				obj.uuid = obj.uuid.substring(1);
				deleteProdx.setObject(1,UUID.fromString(obj.uuid));
				deleteProdx.execute();
			}else if(obj.isModify()==true){
				//modify it~~~
				obj.uuid = obj.uuid.substring(1);
				setter(updateProdx,obj);
				cementRelation(obj);		
			}
		} catch (SQLException e) {			
			e.printStackTrace();
			obj.error = e.getMessage();//update last error message
		}
		return obj;
	}
	private static void setter(PreparedStatement sta, ItemProdx obj) throws SQLException{
		mapInfo(sta,1,obj.info);	
		mapDate(sta,2,obj.stmp);	
		mapDate(sta,3,obj.last);
		sta.setInt(4,obj.format);
		mapArray(sta,5,obj.scribble);
		ItemOwner owner = obj.getOwner();
		if(owner!=null){
			mapUUID(sta,6,owner.uuid);
		}else{
			mapUUID(sta,6,null);
		}
		ItemTenur tenur = obj.getTenur();
		if(tenur!=null){
			mapUUID(sta,7,tenur.uuid);
		}else{
			mapUUID(sta,7,null);
		}
		mapUUID(sta,8,obj.uuid);
		sta.execute();
	}
	
	private static void cementRelation(ItemProdx obj) throws SQLException{
		ItemOwner owner = obj.getOwner();
		ItemTenur tenur = obj.getTenur();
		
		if(tenur==null){
			return;
		}
		String addr = "";
		if(owner!=null){
			addr = owner.getAddress();
		}
		
		Date day = checkPeriod(obj.stmp,addr);
		
		RpcBridge.getResult(
			"UPDATE "+Const.TENUR+
			" SET stamp='"+day.toString()+"',"+
			" meet='"+day.toString()+"'"+			
			" WHERE id='"+tenur.uuid+"'"
		);
	}
	
	public static Date checkPeriod(Date stamp, String addr) throws SQLException {
		
		String day = UtilsMisc.fmtDate.format(stamp);
		String txt = UtilsMisc.fmtYear.format(stamp);	

		txt = String.valueOf(Integer.valueOf(txt)+1)+" "+
			UtilsMisc.fmtMonth.format(stamp)+"/"+
			UtilsMisc.fmtDayId.format(stamp)+" "+
			ItemMeeting.ArriveHour(addr);
		
		Date stmp = UtilsMisc.fmtMeet.parse(txt);//default time stamp!!
		
		for(;;){
			txt = UtilsMisc.fmtWeek.format(stmp).toLowerCase();
			//first, check whether it is weekend
			if(txt.startsWith("sun")==true){
				UtilsCalendar.addDaysToDate(stmp,-2);
				continue;
			}else if(txt.startsWith("sat")==true){
				UtilsCalendar.addDaysToDate(stmp,-1);
				continue;
			}
			//second, check whether it is rest day
			txt = "SELECT * FROM param WHERE val SIMILAR TO '%"+day+"%'";
			ResultSet rs = RpcBridge.getResult(txt);
			rs.next();
			if(rs.getRow()>=1){
				continue;
			}			
			//it is not at all, all right~~~
			break;
		}
		return stmp;
	}
	//--------------------------//
	
	private static final String SQL_INSERT_PARAM = "INSERT INTO "+Const.PARAM+" (key,val) VALUES(?,?)";
	
	private static final String SQL_UPDATE_PARAM = "UPDATE "+Const.PARAM+" SET val=? WHERE key=?";
	
	private static final String SQL_DELETE_PARAM = "DELETE FROM "+Const.PARAM+" WHERE key=?";
		
	public static ItemParam accessParam(final int cmd,ItemParam obj) throws IllegalArgumentException {
		try {
			switch(cmd){
			case Const.CMD_INSERT:
				insertParam.setString(1,obj.getKey());
				insertParam.setString(2,obj.getVal());
				insertParam.execute();
				break;
			
			case Const.CMD_UPDATE:				
				updateParam.setString(1,obj.getVal());
				updateParam.setString(2,obj.getKey());
				updateParam.execute();
				break;
			
			case Const.CMD_DELETE:	
				deleteParam.setString(1,obj.getKey());
				deleteParam.execute();
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			obj.appendError(e.getMessage());
		}
		return obj;
	}	
	//--------------------------//
	
	public static String getUUID(
		ResultSet rs, 
		String col
	) throws SQLException {
		UUID id = (UUID) rs.getObject(col);
		if (id == null) {
			return "";
		}
		return id.toString();
	}
		
	public static String[] getTxtArray(
		ResultSet rs, 
		String col
	) throws SQLException {
		Array tmp = rs.getArray(col);
		if (tmp == null) {
			return null;
		}
		return (String[]) tmp.getArray();
	}

	
	public static boolean mapUUID(
		PreparedStatement stm, 
		int idx, 
		String uuid
	) throws SQLException {
		UUID uid = null;
		try{
			if(uuid==null){
				stm.setObject(idx,uid);//???Can we do this???
				return false;
			}else if(uuid.length()==0){
				uid = UUID.randomUUID();
			}else{
				uid = UUID.fromString(uuid);
			}
		}catch(IllegalArgumentException e){
			return false;
		}
		stm.setObject(idx,uid);
		return true;
	}
	
	public static void mapDate(
		PreparedStatement stm, 
		int idx, 
		Date stamp
	) throws SQLException {
		if(stamp==null){
			stm.setNull(idx,java.sql.Types.TIMESTAMP);
		} else {
			long t = stamp.getTime();
			stm.setTimestamp(
				idx, 
				new Timestamp(t)
			);
		}
	}

	public static void mapInfo(
		PreparedStatement stm, 
		int idx, 
		String[] info
	) throws SQLException {
		if(info==null){
			stm.setNull(idx,java.sql.Types.ARRAY);
		}else{
			Array tmp = RpcBridge.conn.createArrayOf(
				"text",
				info
			);
			stm.setObject(idx, tmp);
		}
	}
	
	public static void mapArray(
		PreparedStatement stm, 
		int idx, 
		ArrayList<String> list
	) throws SQLException {
		if(list.isEmpty()==true){
			stm.setNull(idx,java.sql.Types.ARRAY);//???
		}else{
			Array tmp = RpcBridge.conn.createArrayOf(
				"text", 
				list.toArray()
			);
			stm.setObject(idx, tmp);
		}
	}
}

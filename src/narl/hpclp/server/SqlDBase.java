package narl.hpclp.server;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import narl.hpclp.shared.Const;
import narl.hpclp.shared.ItemAccnt;
import narl.hpclp.shared.ItemOwner;
import narl.hpclp.shared.ItemProdx;
import narl.hpclp.shared.ItemTenur;

/**
 * The prefix,'Modify' means 'insert', 'update' and 'delete'.<p>
 * @author qq
 *
 */
public class SqlDBase {

	public static PreparedStatement insertOwner,insertTenur,insertAccnt,insertProdx;	
	public static PreparedStatement updateOwner,updateTenur,updateAccnt,updateProdx;	
	public static PreparedStatement deleteOwner,deleteTenue,deleteAccnt,deleteProdx;
	
	public static void prepare(Connection conn) throws SQLException{
		
		insertOwner = conn.prepareStatement("INSERT INTO "+Const.OWNER+" (info,stamp,last,id) VALUES(?,?,?,?)");
		insertTenur = conn.prepareStatement("INSERT INTO "+Const.TENUR+" (info,stamp,last,meet,dirty,oid,id) VALUES(?,?,?,?,?,?,?)");
		insertAccnt = conn.prepareStatement("INSERT INTO "+Const.ACCNT+" (info,stamp,last,fare,final,oid,id) VALUES(?,?,?,?,?,?,?)");
		insertProdx = conn.prepareStatement("INSERT INTO "+Const.PRODX+" (info,stamp,last,format,scribble,oid,tid,id) VALUES(?,?,?,?,?,?,?,?)");
		//---------------------------//
		updateOwner = conn.prepareStatement("UPDATE "+Const.OWNER+" SET info=?, stamp=?, last=? WHERE id=?");	
		updateTenur = conn.prepareStatement("UPDATE "+Const.TENUR+" SET info=?, stamp=?, last=?, meet=?, dirty=?, oid=? WHERE id=?");
		updateAccnt = conn.prepareStatement("UPDATE "+Const.ACCNT+" SET info=?, stamp=?, last=?, fare=?, final=?, oid=? WHERE id=?");
		updateProdx = conn.prepareStatement("UPDATE "+Const.PRODX+" SET info=?, stamp=?, last=?, format=?, scribble=?, oid=?, tid=? WHERE id=?");
		//---------------------------//
		deleteOwner = conn.prepareStatement("DELETE FROM "+Const.OWNER+" WHERE id=?");
		deleteTenue = conn.prepareStatement("DELETE FROM "+Const.TENUR+" WHERE id=?");
		deleteAccnt = conn.prepareStatement("DELETE FROM "+Const.ACCNT+" WHERE id=?");
		deleteProdx = conn.prepareStatement("DELETE FROM "+Const.PRODX+" WHERE id=?");
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
	
	public static ArrayList<ItemTenur> listTenur(String postfix) {
		ArrayList<ItemTenur> lst = new ArrayList<ItemTenur>();
		try {			
			String cmd = "SELECT "+
				Const.TENUR+".id AS id, "+				
				Const.TENUR+".info AS info, "+
				Const.TENUR+".stamp AS stamp, "+
				Const.TENUR+".last AS last "+
				Const.TENUR+".meet AS meet "+
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
				Const.TENUR+".last AS tlast "+
				"FROM "+Const.PRODX+" "+		
				"LEFT JOIN "+Const.OWNER+" ON "+Const.OWNER+".id="+Const.PRODX+".oid "+
				"LEFT JOIN "+Const.TENUR+" ON "+Const.TENUR+".id="+Const.PRODX+".tid "+
				postfix;		
			ResultSet rs = RpcBridge.getResult(cmd);	
			while(rs.next()){
				lst.add(unpackProdx(rs));
			}
		} catch (SQLException e) {			
			System.err.print(e.getMessage());
		}
		return lst;
	}
	
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
			item.owner = new ItemOwner(
				uuid,
				getTxtArray(rs,"oinfo"),
				rs.getTimestamp("ostamp"),
				rs.getTimestamp("olast")
			);
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
		if(uuid.length()!=0){
			item.owner = new ItemOwner(
				uuid,
				getTxtArray(rs,"oinfo"),
				rs.getTimestamp("ostamp"),
				rs.getTimestamp("olast")
			);
		}
		uuid = getUUID(rs,"tid");		
		if(uuid.length()!=0){
			item.tenur = new ItemTenur(
				uuid,
				getTxtArray(rs,"tinfo"),
				rs.getTimestamp("tstamp"),
				rs.getTimestamp("tlast")
			);
		}
		return item;
	}
	//--------------------------//
	
	public static ItemOwner modifyOwner(ItemOwner obj) throws IllegalArgumentException {
		try {
			if(obj.uuid.length()==0){
				mapping(insertOwner,obj);//create a new one~~~
			}else if(obj.death==false){
				mapping(updateOwner,obj);//modify it~~~
			}else{
				//delete it!!!
				deleteOwner.setObject(1,UUID.fromString(obj.uuid));
				deleteOwner.execute();
			}
		} catch (SQLException e) {			
			e.printStackTrace();
			obj.error = e.getMessage();//update last error message
		}
		return obj;
	}

	private static void mapping(PreparedStatement sta, ItemOwner obj) throws SQLException{
		mapInfo(sta,1,obj.info);
		mapDate(sta,2,obj.stmp);
		mapDate(sta,3,obj.last);
		mapUUID(sta,4,obj.uuid);
		sta.execute();
	}
	
	public static ItemTenur modifyTenur(ItemTenur obj) throws IllegalArgumentException {
		try {
			if(obj.uuid.length()==0){
				mapping(insertTenur,obj);//create a new one
			}else if(obj.death==false){
				mapping(updateTenur,obj);//modify it~~~		
			}else{
				//delete it!!!
				deleteTenue.setObject(1,UUID.fromString(obj.uuid));
				deleteTenue.execute();
			}
		} catch (SQLException e) {			
			e.printStackTrace();
			obj.error = e.getMessage();//update last error message
		}
		return obj;
	}

	private static void mapping(PreparedStatement sta, ItemTenur obj) throws SQLException{
		mapInfo(sta,1,obj.info);	
		mapDate(sta,2,obj.stmp);	
		mapDate(sta,3,obj.last);
		mapDate(sta,4,obj.meet);
		sta.setBoolean(5,true);
		if(obj.owner!=null){
			mapUUID(sta,6,obj.owner.uuid);
		}else{
			mapUUID(sta,6,null);
		}
		mapUUID(sta,7,obj.uuid);
		sta.execute();	
	}
	
	public static ItemAccnt modifyAccnt(ItemAccnt obj) throws IllegalArgumentException {
		try {
			if(obj.uuid.length()==0){
				mapping(insertAccnt,obj);//create a new one
			}else if(obj.death==false){
				mapping(updateAccnt,obj);//modify it~~~
			}else{
				//delete it!!!
				deleteAccnt.setObject(1,UUID.fromString(obj.uuid));
				deleteAccnt.execute();
			}
		} catch (SQLException e) {			
			e.printStackTrace();
			obj.error = e.getMessage();//update last error message
		}
		return obj;
	}

	private static void mapping(PreparedStatement sta, ItemAccnt obj) throws SQLException{
		mapInfo(sta,1,obj.info);	
		mapDate(sta,2,obj.stmp);	
		mapDate(sta,3,obj.last);
		/*mapList(sta,4,obj.fare);
		updateAccnt.setBoolean(5,obj.isFinal);
		if(obj.owner!=null){
			mapUUID(sta,6,obj.owner.uuid);
		}else{
			mapUUID(sta,6,null);
		}
		mapUUID(sta,7,obj.uuid);
		stmt.execute();*/
	}
	
	public static ItemProdx modifyProdx(ItemProdx obj) throws IllegalArgumentException {
		try {
			if(obj.uuid.length()==0){
				//create a new one
			}else if(obj.death==false){
				//modify it~~~
				/*mapInfo(modifyOwner,1,obj.info);	
				mapDate(modifyOwner,2,obj.stmp);
				mapDate(modifyOwner,3,obj.last);
				mapUUID(modifyOwner,4,obj.uuid);
				modifyOwner.execute();*/		
			}else{
				//delete it!!!
				deleteProdx.setObject(1,UUID.fromString(obj.uuid));
				deleteProdx.execute();
			}
		} catch (SQLException e) {			
			e.printStackTrace();
			obj.error = e.getMessage();//update last error message
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
	
	public static void mapList(
		PreparedStatement stm, 
		int idx, 
		LinkedList<String> list
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

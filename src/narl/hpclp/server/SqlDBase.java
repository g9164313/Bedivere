package narl.hpclp.server;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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
	public static PreparedStatement modifyOwner,modifyTenue,modifyAccnt,modifyProdx;	
	public static PreparedStatement deleteOwner,deleteTenue,deleteAccnt,deleteProdx;
	
	public static void prepare(Connection conn) throws SQLException{
		
		insertOwner = conn.prepareStatement(
			"INSERT INTO "+Const.OWNER+
			"(info,stamp,last,id) "+ 
			"VALUES(?,?,?,?)"
		);
		insertTenur = conn.prepareStatement(
			"INSERT INTO "+Const.TENUR+
			"(info,stamp,last,meet,dirty,oid,id) " + 
			"VALUES(?,?,?,?,?,?,?)"
		);
		insertAccnt = conn.prepareStatement(
			"INSERT INTO "+Const.ACCNT+
			"(info,stamp,last,fare,final,oid,id) " + 
			"VALUES(?,?,?,?,?,?,?)"
		);
		insertProdx = conn.prepareStatement(
			"INSERT INTO "+Const.PRODX+
			"(info,stamp,last,format,scribble,oid,tid,id) " + 
			"VALUES(?,?,?,?,?,?,?,?)"
		);
		//---------------------------//
		modifyOwner = conn.prepareStatement(
			"UPDATE "+Const.OWNER+" SET "+ 
			"info=?, stamp=?, last=? WHERE id=?"
		);	
		modifyTenue = conn.prepareStatement(
			"UPDATE "+Const.TENUR+" SET "+ 
			"info=?, stamp=?, last=?, meet=?, dirty=?, oid=? WHERE id=?"
		);
		modifyAccnt = conn.prepareStatement(
			"UPDATE "+Const.ACCNT+" SET "+ 
			"info=?, stamp=?, last=?, fare=?, final=?, oid=? WHERE id=?"
		);
		modifyProdx = conn.prepareStatement(
			"UPDATE "+Const.PRODX+" SET "+
			"info=?, stamp=?, last=?, format=?, scribble=?, oid=?, tid=? WHERE id=?"
		);
		//---------------------------//
		deleteOwner = conn.prepareStatement("DELETE FROM "+Const.OWNER+" WHERE id=?");
		deleteTenue = conn.prepareStatement("DELETE FROM "+Const.TENUR+" WHERE id=?");
		deleteAccnt = conn.prepareStatement("DELETE FROM "+Const.ACCNT+" WHERE id=?");
		deleteProdx = conn.prepareStatement("DELETE FROM "+Const.PRODX+" WHERE id=?");
	}
	
	public static ItemOwner modifyOwner(ItemOwner obj) throws IllegalArgumentException {
		try {
			if(obj.uuid.length()==0){
				//create a new one
			}else if(obj.deprecated==false){
				//modify it~~~
				mapInfo(modifyOwner,1,obj.info);	
				mapDate(modifyOwner,2,obj.stmp);
				mapDate(modifyOwner,3,obj.last);
				mapUUID(modifyOwner,4,obj.uuid);
				modifyOwner.execute();			
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


	public static ItemTenur modifyTenur(ItemTenur obj) throws IllegalArgumentException {
		try {
			if(obj.uuid.length()==0){
				//create a new one
			}else if(obj.deprecated==false){
				//modify it~~~
				/*mapInfo(modifyOwner,1,obj.info);	
				mapDate(modifyOwner,2,obj.stmp);
				mapDate(modifyOwner,3,obj.last);
				mapUUID(modifyOwner,4,obj.uuid);
				modifyOwner.execute();*/		
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

	public static ItemAccnt modifyAccnt(ItemAccnt obj) throws IllegalArgumentException {
		try {
			if(obj.uuid.length()==0){
				//create a new one
			}else if(obj.deprecated==false){
				//modify it~~~
				/*mapInfo(modifyOwner,1,obj.info);	
				mapDate(modifyOwner,2,obj.stmp);
				mapDate(modifyOwner,3,obj.last);
				mapUUID(modifyOwner,4,obj.uuid);
				modifyOwner.execute();*/		
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

	public static ItemProdx modifyProdx(ItemProdx obj) throws IllegalArgumentException {
		try {
			if(obj.uuid.length()==0){
				//create a new one
			}else if(obj.deprecated==false){
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

	public static boolean mapUUID(
		PreparedStatement stm, 
		int idx, 
		String uuid
	) throws SQLException {
		UUID id = null;
		try{
			if(uuid.length()==0){
				id = UUID.randomUUID();
			}else{
				id = UUID.fromString(uuid);
			}
		}catch(IllegalArgumentException e){
			return false;
		}
		stm.setObject(idx,id);
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

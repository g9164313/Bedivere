package narl.hpclp.server;

import java.io.File;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import narl.hpclp.client.RPC;
import narl.hpclp.shared.Const;
import narl.hpclp.shared.ItemAccnt;
import narl.hpclp.shared.ItemMeeting;
import narl.hpclp.shared.ItemOwner;
import narl.hpclp.shared.ItemParam;
import narl.hpclp.shared.ItemProdx;
import narl.hpclp.shared.ItemTenur;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RpcBridge extends RemoteServiceServlet 
	implements RPC
{
	public static FmtDate fmtDate = FmtDate.getFormat("yyyy/M/d"); 
	public static FmtDate fmtSql1 = FmtDate.getFormat("yyyy-M-d");
	public static FmtDate fmtSql2 = FmtDate.getFormat("yyyy-M-d HH:mm:ss");
	
	public static Connection conn;
	public static PreparedStatement statInsOwner,statModOwner;	
	public static PreparedStatement statInsTenue,statModTenue;	
	public static PreparedStatement statInsAccnt,statModAccnt;
	public static PreparedStatement statInsProdx,statModProdx;
	
	public static ResultSet getResult(String cmd) throws SQLException{
		if(conn==null){
			return null;
		}
		Statement stat = conn.createStatement();
		boolean ret = stat.execute(cmd);			
		if (ret==false) {
			return null;
		}
		return stat.getResultSet();	
	} 
	
	public static String[] info2flat(
		ResultSet rs, 
		String col
	) throws SQLException {
		Array tmp = rs.getArray(col);
		if (tmp == null) {
			return null;
		}
		return (String[]) tmp.getArray();
	}

	public static String uuid2flat(
		ResultSet rs, 
		String col
	) throws SQLException {
		UUID id = (UUID) rs.getObject(col);
		if (id == null) {
			return "";
		}
		return id.toString();
	}
	//---------------------------------//
	
	@Override
	public ItemParam initServer() throws IllegalArgumentException {
		ItemParam res = new ItemParam();
		try {		
			Class.forName("org.postgresql.Driver");					
			conn = DriverManager.getConnection(
				Const.DATABASE_URL, 
				Const.DATABASE_USER,
				Const.DATABASE_PASS
			);
			//---------------------------//
			/*statInsOwner = conn.prepareStatement(
				"INSERT INTO "+
				Const.TABLE1+"(info,stamp,last,id) " + 
				"VALUES(?,?,?,?)"
			);	
			statModOwner = conn.prepareStatement(
				"UPDATE "+Const.TABLE1+" SET "+ 
				"info=?, stamp=?, last=? WHERE id=?"
			);	
			//---------------------------//
			statInsTenue = conn.prepareStatement(
				"INSERT INTO "+Const.TABLE2+
				"(info,stamp,last,meet,dirty,oid,id) " + 
				"VALUES(?,?,?,?,?,?,?)"
			);
			statModTenue = conn.prepareStatement(
				"UPDATE "+Const.TABLE2+" SET "+ 
				"info=?, stamp=?, last=?, meet=?, dirty=?, oid=? WHERE id=?"
			);
			//---------------------------//
			statInsAccnt = conn.prepareStatement(
				"INSERT INTO "+Const.TABLE3+
				"(info,stamp,last,fare,final,oid,id) " + 
				"VALUES(?,?,?,?,?,?,?)"
			);
			statModAccnt = conn.prepareStatement(
				"UPDATE "+Const.TABLE3+" SET "+ 
				"info=?, stamp=?, last=?, fare=?, final=?, oid=? WHERE id=?"
			);
			//---------------------------//
			statInsProdx = conn.prepareStatement(
				"INSERT INTO "+Const.TABLE4+
				"(info,stamp,last,format,scribble,oid,tid,id) " + 
				"VALUES(?,?,?,?,?,?,?,?)"
			);
			statModProdx = conn.prepareStatement(
				"UPDATE "+Const.TABLE4+" SET "+
				"info=?, stamp=?, last=?, format=?, scribble=?, oid=?, tid=? WHERE id=?"
			);*/			
		} catch(ClassNotFoundException e){
			return res.appendError(e.getMessage());
		} catch (SQLException e) {			
			return res.appendError(e.getMessage());
		}

		checkParamData(res);
		
		checkJasperPath(res);

		return res;
	}
	
	private void checkParamData(ItemParam res){
		try {
			ResultSet rs;
			rs = getResult("SELECT val FROM param WHERE key SIMILAR TO '%UNIT%' ORDER BY val");			
			while(rs.next()){ res.gather(rs.getString(1)); }
			res.mapping("prodxUnit");
			
			rs = getResult("SELECT val FROM param WHERE key SIMILAR TO '%DETTYPE%' ORDER BY key");			
			while(rs.next()){ res.gather(rs.getString(1)); }
			res.mapping("detectType");
			
			rs = getResult("SELECT val FROM param WHERE key SIMILAR TO '%SERVICE%' ORDER BY val");			
			while(rs.next()){ res.gather(rs.getString(1)); }
			res.mapping("accntService");
			
			rs = getResult("SELECT val FROM param WHERE key SIMILAR TO '%EMITTER%' ORDER BY val");			
			while(rs.next()){ res.gather(rs.getString(1)); }
			res.mapping("prodxEmitter");
			
		} catch (SQLException e) {			
			res.appendError(e.getMessage());
		}
	}
	
	private void checkJasperPath(ItemParam res){
		final String name = "/narl.hpclp.jasper/";
		String path = new File(".").getAbsolutePath();
		String dstPath;
		//Try every possible path~~~
		if(new File(path+name).exists()==true){
			dstPath = path + name;		
		}else if(new File(path+"/webapps/bedivere"+name).exists()==true){
			//If user deploy package in Apache-Tomcat, the default location may be his root path.
			dstPath = path +"/webapps/bedivere"+name;
		}else if(new File(path+"/bedivere"+name).exists()==true){
			//Is this possible???
			dstPath = path+"/bedivere"+name;
		}else{
			res.appendError("FAIL: path is undetermined."+path);
		}
	}
	//---------------------------------//
	
	@Override
	public ArrayList<ItemMeeting> listMeeting(String dayFst, String dayEnd) {		
		ArrayList<ItemMeeting> res = new ArrayList<ItemMeeting>();		
		try {			
			return SqlMeeting.list(res,dayFst, dayEnd);			
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return res;
	}

	@Override
	public ItemOwner modifyOwner(ItemOwner obj) throws IllegalArgumentException {
		return SqlDBase.modifyOwner(obj);
	}

	@Override
	public ItemTenur modifyTenur(ItemTenur obj) throws IllegalArgumentException {
		return SqlDBase.modifyTenur(obj);
	}

	@Override
	public ItemAccnt modifyAccnt(ItemAccnt obj) throws IllegalArgumentException {
		return SqlDBase.modifyAccnt(obj);
	}

	@Override
	public ItemProdx modifyProdx(ItemProdx obj) throws IllegalArgumentException {
		return SqlDBase.modifyProdx(obj);
	}
}

package narl.hpclp.server;

import java.io.File;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
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
	public static Connection conn;
	
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
		
	@Override
	public String genKey(String args) throws IllegalArgumentException {
		if(args.startsWith(Const.ACCNT)==true){
			args = args.substring(Const.ACCNT.length()+1);
			return SqlDataMisc.genAKey(args);
		}else if(args.startsWith(Const.PRODX)==true){
			args = args.substring(Const.PRODX.length()+1);
			return SqlDataMisc.genPKey(args.split(","));
		}
		return "";
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
			SqlDataBase.prepare(conn);
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
			
			rs = getResult("SELECT val FROM param WHERE key SIMILAR TO '%REST%' ORDER BY val");			
			while(rs.next()){ res.gather(rs.getString(1)); }
						
		} catch (SQLException e) {			
			res.appendError(e.getMessage());
		}
	}
	
	private void checkJasperPath(ItemParam res){
		final String name = "/narl.hpclp.jasper";
		String path = new File(".").getAbsolutePath();		
		//Try every possible path~~~
		if(new File(path+name).exists()==true){
			RpcPrint.DOC_PATH = path + name;			
		}else if(new File(path+"/webapps/bedivere"+name).exists()==true){
			//If user deploy package in Apache-Tomcat, the default location may be his root path.
			RpcPrint.DOC_PATH = path +"/webapps/bedivere"+name;
		}else if(new File(path+"/bedivere"+name).exists()==true){
			//Is this possible???	
			RpcPrint.DOC_PATH = path+"/bedivere"+name;
		}else if(new File(path+"/webapps/Bedivere"+name).exists()==true){
			RpcPrint.DOC_PATH = path +"/webapps/Bedivere"+name;
		}else if(new File(path+"/Bedivere"+name).exists()==true){
			RpcPrint.DOC_PATH = path+"/Bedivere"+name;
		}else{
			res.appendError("FAIL: the unknown paht - \'"+path+"\'");
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
	public ArrayList<ItemOwner> listOwner(String postfix) throws IllegalArgumentException {
		return SqlDataBase.listOwner(postfix);
	}
	
	@Override
	public ArrayList<ItemTenur> listTenure(String postfix) throws IllegalArgumentException {
		return SqlDataBase.listTenur(postfix);
	}
	
	@Override
	public ArrayList<ItemProdx> listProduct(String postfix) throws IllegalArgumentException {		
		return SqlDataBase.listProdx(postfix);
	}
	//---------------------------------//
	
	@Override
	public ItemOwner modifyOwner(ItemOwner obj) throws IllegalArgumentException {
		return SqlDataBase.modifyOwner(obj);
	}

	@Override
	public ItemTenur modifyTenur(ItemTenur obj) throws IllegalArgumentException {
		return SqlDataBase.modifyTenur(obj);
	}

	@Override
	public ItemAccnt modifyAccnt(ItemAccnt obj) throws IllegalArgumentException {
		return SqlDataBase.modifyAccnt(obj);
	}

	@Override
	public ItemProdx modifyProdx(ItemProdx obj) throws IllegalArgumentException {
		return SqlDataBase.modifyProdx(obj);
	}
	
	@Override
	public ArrayList<ItemOwner> cacheOwner(ArrayList<ItemOwner> lst) throws IllegalArgumentException {
		DSrcOwner.lst = new ArrayList<ItemOwner>();
		for(ItemOwner itm:lst){
			itm = SqlDataBase.modifyOwner(itm);
			if(itm.death==false){
				DSrcOwner.lst.add(itm);
			}
		}
		return DSrcOwner.lst;
	}
	
	@Override
	public ArrayList<ItemTenur> cacheTenure(ArrayList<ItemTenur> lst) throws IllegalArgumentException {
		ArrayList<ItemTenur> tmp = new ArrayList<ItemTenur>();
		for(ItemTenur itm:lst){
			itm = SqlDataBase.modifyTenur(itm);
			if(itm.death==false){
				tmp.add(itm);
			}
		}
		return tmp;
	}
	
	@Override
	public ArrayList<ItemAccnt> cacheAccount(ArrayList<ItemAccnt> lst) throws IllegalArgumentException {
		DSrcAccount.lst = new ArrayList<ItemAccnt>();
		for(ItemAccnt itm:lst){
			itm = SqlDataBase.modifyAccnt(itm);
			if(itm.death==false){
				DSrcAccount.lst.add(itm);
			}
		}
		return DSrcAccount.lst;
	}
	
	@Override
	public ArrayList<ItemProdx> cacheProduct(ArrayList<ItemProdx> lst) throws IllegalArgumentException {
		DSrcProduct.lst = new ArrayList<ItemProdx>();
		for(ItemProdx itm:lst){
			itm = SqlDataBase.modifyProdx(itm);
			if(itm.death==false){
				DSrcProduct.lst.add(itm);
			}
		}
		return DSrcProduct.lst;
	}

	@Override	
	public void cacheMeeting(ArrayList<ItemMeeting> lst) throws IllegalArgumentException {
		DSrcMeeting.lst = lst;
	}
}



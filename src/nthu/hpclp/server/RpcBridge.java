package nthu.hpclp.server;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import narl.itrc.server.Utils;
import nthu.hpclp.client.RPC;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemAccnt;
import nthu.hpclp.shared.ItemBase;
import nthu.hpclp.shared.ItemMeeting;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemParam;
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ItemTenur;
import nthu.hpclp.shared.ParamHub;

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
	//---------------------------------//

	@Override
	public ParamHub initServer(ParamHub hub) throws IllegalArgumentException {
		
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(
				Const.DATABASE_URL, 
				Const.DATABASE_USER,
				Const.DATABASE_PASS
			);
			SqlDataBase.prepare(conn);			
		} catch(ClassNotFoundException e1){
			try {
				InitialContext cxt = new InitialContext();
				DataSource ds = (DataSource) cxt.lookup( "java:/comp/env/jdbc/postgres" );
				conn = ds.getConnection();
			} catch (NamingException e2) {
				return hub.appendError(e1.getMessage() + "\n" + e2.getMessage());
			} catch (SQLException e2) {
				return hub.appendError(e1.getMessage() + "\n" + e2.getMessage());
			}
		} catch (SQLException e1) {			
			return hub.appendError(e1.getMessage());
		}

		checkParamValue(hub,hub.prodxDetType,"'DETTYPE%'");
		checkParamValue(hub,hub.prodxRadUnit,"'UNIT%'");
		checkParamValue(hub,hub.prodxEmitter,"'EMITTER%'");
		checkParamValue(hub,hub.accntService,"'SERVICE%'");
		checkParamValue(hub,hub.otherRestDay,"'RESTDAY%'");
		
		checkJasperPath(hub);
		return hub;
	}
	
	private void checkParamValue(
		ParamHub hub,
		ArrayList<ItemParam> lst,
		final String type
	){
		try {
			ResultSet rs;
			rs = getResult(
				"SELECT key,val FROM param "+
				"WHERE key SIMILAR TO "+type+" "+
				"ORDER BY val DESC"
			);			
			while(rs.next()){ 
				hub.gather(
					lst,
					rs.getString(1),
					rs.getString(2)
				);
			}					
		} catch (SQLException e) {			
			hub.appendError(e.getMessage());
		}
	}
	
	private void checkJasperPath(ParamHub res){
		
		final String[] path = {
			"./",
			"./webapps/bedivere/",
			"../webapps/bedivere/",
			"../../webapps/bedivere/"			
		};
		
		final String name = "nthu.hpclp.jasper";		
		
		for(int i=0; i<path.length; i++){
			File fs = new File(path[i]+name);
			if(fs.exists()==true){
				RpcPrint.DOC_PATH = path[i]+name;
				return;
			}
		}
		//we can't locate JASPER files, report it!!!!
		res.appendError(
			"FAIL: the unknown path - "+
			new File(".").getAbsolutePath()
		);
	}
	//---------------------------------//
	
	@Override
	public String genKey(String args) throws IllegalArgumentException {
		
		if(args.startsWith(Const.OWNER)==true){
			
			args = args.substring(Const.OWNER.length()+1);//there is a 'plus' sign
			return SqlDataMisc.genOKey(args);
			
		}if(args.startsWith(Const.ACCNT)==true){
			
			args = args.substring(Const.ACCNT.length()+1);//???
			return SqlDataMisc.genAKey(args);
			
		}else if(args.startsWith(Const.PRODX)==true){
			
			args = args.substring(Const.PRODX.length()+1);//there is a 'star' sign
			return SqlDataMisc.genPKey(args.split(","));
		}
		return "";
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
	public ItemParam modifyParam(ItemParam obj) throws IllegalArgumentException {
		return SqlDataBase.modifyParam(obj);
	}
	//---------------------------------//
	
	@Override
	public ArrayList<ItemOwner> cacheOwner(ArrayList<ItemOwner> lst) throws IllegalArgumentException {
		DSrcOwner.lst = new ArrayList<ItemOwner>();
		for(ItemOwner itm:lst){
			itm = SqlDataBase.modifyOwner(itm);
			if(itm==null){
				continue;
			}
			DSrcOwner.lst.add(itm);
		}
		return DSrcOwner.lst;
	}
	
	@Override
	public ArrayList<ItemTenur> cacheTenure(ArrayList<ItemTenur> lst) throws IllegalArgumentException {
		ArrayList<ItemTenur> tmp = new ArrayList<ItemTenur>();
		for(ItemTenur itm:lst){
			itm = SqlDataBase.modifyTenur(itm);
			if(itm==null){
				continue;
			}
			tmp.add(itm);
		}
		return tmp;
	}
	
	@Override
	public ArrayList<ItemAccnt> cacheAccount(ArrayList<ItemAccnt> lst) throws IllegalArgumentException {
		DSrcAccount.lst = new ArrayList<ItemAccnt>();
		for(ItemAccnt itm:lst){
			itm = SqlDataBase.modifyAccnt(itm);
			if(itm==null){
				continue;
			}
			DSrcAccount.lst.add(itm);
		}
		return DSrcAccount.lst;
	}
	
	@Override
	public ArrayList<ItemProdx> cacheProduct(ArrayList<ItemProdx> lst) throws IllegalArgumentException {
		DSrcProduct.lst = new ArrayList<ItemProdx>();
		for(ItemProdx itm:lst){
			itm = SqlDataBase.modifyProdx(itm);
			if(itm==null){
				continue;
			}
			DSrcProduct.lst.add(itm);
		}
		return DSrcProduct.lst;
	}

	@Override	
	public void cacheMeeting(ArrayList<ItemMeeting> lst) throws IllegalArgumentException {
		DSrcMeeting.lst = lst;
	}
	//-------------------------//

	private static String pathSPoint = ".";
	
	@Override
	public String[] listSPoint() throws IllegalArgumentException {
		final String name="work/SPoint";
		final String[] path = {"./","../","../../"};
		for(int i=0; i<path.length; i++){
			File fs = new File(path[i]+name);
			if(fs.exists()==true){
				pathSPoint = fs.getAbsolutePath()+"/";
				final FilenameFilter flt = new FilenameFilter(){
					@Override
					public boolean accept(File dir, String name) {
						if(name.contains("SPoint")==true){
							return false;
						}
						return true;
					}
				};
				String[] lst = fs.list(flt);
				Arrays.sort(lst,Collections.reverseOrder());
				return lst;
			}
		}		
		return null;
	}
	
	@Override
	public String saveSPoint() throws IllegalArgumentException {		
		return Utils.Exec(pathSPoint+"SPoint-save @ "+pathSPoint);
	}
	
	@Override
	public String loadSPoint(String name) throws IllegalArgumentException {		
		return Utils.Exec(pathSPoint+"SPoint-load @ "+pathSPoint+"/"+name);
	}

	@Override
	public String tearSPoint(String name) throws IllegalArgumentException {		
		return Utils.Exec("rm @ -r @ "+pathSPoint+"/"+name);
	}
	//-------------------------------------//
	
	private static String pathPrint = null;
	
	private void init_path_print(){
		
	}
	
	@Override
	public String printTag(String[] info) throws IllegalArgumentException {
		if(pathPrint==null){
			init_path_print();//just once~~
		}
		if(pathPrint.length()==0){
			return "無法執行程式";
		}
		Utils.Exec("PrintLetter @"+
			info[0]+"@"+
			info[1]+"@"+
			info[2]+"@"+
			info[3]+"@"+
			info[4]+"@"+
			info[5]
		);
		return "";
	}
	//-------------------------------------//
	
	@Override
	public ArrayList<ItemBase> resetReport1() throws IllegalArgumentException {		
		return SqlReport1.reset();
	}

	@Override
	public void groupReport1(int beg,int end) throws IllegalArgumentException {
		SqlReport1.group(beg, end);
	}
}



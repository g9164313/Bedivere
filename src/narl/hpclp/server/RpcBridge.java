package narl.hpclp.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import narl.hpclp.client.RPC;
import narl.hpclp.shared.Const;
import narl.hpclp.shared.ItmMeeting;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RpcBridge extends RemoteServiceServlet 
	implements RPC
{
	public static Connection conn;
	public static PreparedStatement statInsOwner,statModOwner;	
	public static PreparedStatement statInsTenue,statModTenue;	
	public static PreparedStatement statInsAccnt,statModAccnt;
	public static PreparedStatement statInsProdx,statModProdx;
	
	@Override
	public String initServer() throws IllegalArgumentException {
		String txt = null;
		try {		
			Class.forName("org.postgresql.Driver");					
			conn = DriverManager.getConnection(
				Const.DATABASE_URL, 
				Const.DATABASE_USER,
				Const.DATABASE_PASS
			);
			//---------------------------//
			statInsOwner = conn.prepareStatement(
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
			);			
		} catch(ClassNotFoundException e){		
			e.printStackTrace();
			txt = "FAIL: "+e.getMessage();
		} catch (SQLException e) {			
			e.printStackTrace();
			txt = "FAIL: "+e.getMessage();
		}
		if(txt!=null){
			System.err.println(txt);
			return txt;
		}
		
		final String name = "/narl.hpclp.jasper/";
		String path = new File(".").getAbsolutePath();		
		//Try every possible path~~~
		if(new File(path+name).exists()==true){
			txt = path + name;		
		}else if(new File(path+"/webapps/bedivere"+name).exists()==true){
			//If user deploy package in Apache-Tomcat, the default location may be his root path.
			txt = path +"/webapps/bedivere"+name;
		}else if(new File(path+"/bedivere"+name).exists()==true){
			//Is this possible???
			txt = path+"/bedivere"+name;
		}else{
			return "FAIL: path is undetermined."+path;
		}	
		return "";
	}
	//---------------------------------//
	
	@Override
	public ArrayList<ItmMeeting> listMeeting(Date brg, Date end) {
		// TODO Auto-generated method stub
		return null;
	}
}

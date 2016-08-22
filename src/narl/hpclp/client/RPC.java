package narl.hpclp.client;

import java.util.ArrayList;

import narl.hpclp.shared.ItemAccnt;
import narl.hpclp.shared.ItemMeeting;
import narl.hpclp.shared.ItemOwner;
import narl.hpclp.shared.ItemParam;
import narl.hpclp.shared.ItemProdx;
import narl.hpclp.shared.ItemTenur;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("calling")
public interface RPC extends RemoteService {
	
	ItemParam initServer() throws IllegalArgumentException;
	
	String genKey(String args) throws IllegalArgumentException;
	
	String[] getSPoint() throws IllegalArgumentException;
	
	String runScript(String txt) throws IllegalArgumentException;
	
	ArrayList<ItemOwner> listOwner(String postfix);
	ArrayList<ItemTenur> listTenure(String postfix);
	ArrayList<ItemProdx> listProduct(String postfix);
	ArrayList<ItemMeeting> listMeeting(String dayFst, String dayEnd);
	
	ItemOwner modifyOwner(ItemOwner obj) throws IllegalArgumentException;
	ItemTenur modifyTenur(ItemTenur obj) throws IllegalArgumentException;
	ItemAccnt modifyAccnt(ItemAccnt obj) throws IllegalArgumentException;
	ItemProdx modifyProdx(ItemProdx obj) throws IllegalArgumentException;
	
	ArrayList<ItemOwner> cacheOwner(ArrayList<ItemOwner> lst) throws IllegalArgumentException;
	ArrayList<ItemTenur> cacheTenure(ArrayList<ItemTenur> lst) throws IllegalArgumentException;	
	ArrayList<ItemProdx> cacheProduct(ArrayList<ItemProdx> lst) throws IllegalArgumentException;
	ArrayList<ItemAccnt> cacheAccount(ArrayList<ItemAccnt> lst) throws IllegalArgumentException;
	void cacheMeeting(ArrayList<ItemMeeting> lst) throws IllegalArgumentException;

}

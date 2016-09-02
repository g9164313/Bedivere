package nthu.hpclp.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import nthu.hpclp.shared.ItemAccnt;
import nthu.hpclp.shared.ItemMeeting;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemParam;
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ItemTenur;
import nthu.hpclp.shared.ParamHub;

@RemoteServiceRelativePath("calling")
public interface RPC extends RemoteService {
	
	ParamHub initServer(ParamHub hub) throws IllegalArgumentException;
	
	String genKey(String args) throws IllegalArgumentException;
	
	String[] listSPoint() throws IllegalArgumentException;	
	String saveSPoint() throws IllegalArgumentException;	
	String loadSPoint(String name) throws IllegalArgumentException;
	String tearSPoint(String name) throws IllegalArgumentException;
	
	ArrayList<ItemOwner> listOwner(String postfix);
	ArrayList<ItemTenur> listTenure(String postfix);
	ArrayList<ItemProdx> listProduct(String postfix);
	ArrayList<ItemMeeting> listMeeting(String dayFst, String dayEnd);
	
	ItemOwner modifyOwner(ItemOwner obj) throws IllegalArgumentException;
	ItemTenur modifyTenur(ItemTenur obj) throws IllegalArgumentException;
	ItemAccnt modifyAccnt(ItemAccnt obj) throws IllegalArgumentException;
	ItemProdx modifyProdx(ItemProdx obj) throws IllegalArgumentException;
	ItemParam modifyParam(ItemParam obj) throws IllegalArgumentException;
	
	ArrayList<ItemOwner> cacheOwner(ArrayList<ItemOwner> lst) throws IllegalArgumentException;
	ArrayList<ItemTenur> cacheTenure(ArrayList<ItemTenur> lst) throws IllegalArgumentException;	
	ArrayList<ItemProdx> cacheProduct(ArrayList<ItemProdx> lst) throws IllegalArgumentException;
	ArrayList<ItemAccnt> cacheAccount(ArrayList<ItemAccnt> lst) throws IllegalArgumentException;
	void cacheMeeting(ArrayList<ItemMeeting> lst) throws IllegalArgumentException;

}

package nthu.hpclp.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import nthu.hpclp.shared.ItemAccnt;
import nthu.hpclp.shared.ItemBase;
import nthu.hpclp.shared.ItemMeeting;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemParam;
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ItemTenur;
import nthu.hpclp.shared.ParamHub;

public interface RPCAsync {
	
	void initServer(
		ParamHub hub,
		AsyncCallback<ParamHub> res
	) throws IllegalArgumentException;
	
	void genKey(
		String args,
		AsyncCallback<String> res
	) throws IllegalArgumentException;
	
	void listSPoint(
		AsyncCallback<String[]> res
	) throws IllegalArgumentException;
	
	void saveSPoint(
		AsyncCallback<String> res
	) throws IllegalArgumentException;

	void loadSPoint(
		String name,
		AsyncCallback<String> res
	) throws IllegalArgumentException;
	
	void tearSPoint(
		String name,
		AsyncCallback<String> res
	) throws IllegalArgumentException;
	
	void listMeeting(
		String dayFst,
		String dayEnd,
		AsyncCallback<ArrayList<ItemMeeting>> res
	) throws IllegalArgumentException;
	
	void listOwner(
		String postfix,
		AsyncCallback<ArrayList<ItemOwner>> res
	) throws IllegalArgumentException;
	
	void listTenure(
		String postfix,
		AsyncCallback<ArrayList<ItemTenur>> res
	) throws IllegalArgumentException;
	
	void listProduct(
		String postfix,
		AsyncCallback<ArrayList<ItemProdx>> res
	) throws IllegalArgumentException;
	
	
	void modifyOwner(
		ItemOwner obj,
		AsyncCallback<ItemOwner> res
	) throws IllegalArgumentException;
	
	void modifyTenur(
		ItemTenur obj,
		AsyncCallback<ItemTenur> res
	) throws IllegalArgumentException;
	
	void modifyAccnt(
		ItemAccnt obj,
		AsyncCallback<ItemAccnt> res
	) throws IllegalArgumentException;
	
	void modifyProdx(
		ItemProdx obj,
		AsyncCallback<ItemProdx> res
	) throws IllegalArgumentException;
	
	void modifyParam(
		ItemParam obj,
		AsyncCallback<ItemParam> res
	) throws IllegalArgumentException;
	
	
	void cacheOwner(
		ArrayList<ItemOwner> lst,
		AsyncCallback<ArrayList<ItemOwner>> res
	) throws IllegalArgumentException;
	
	void cacheTenure(
		ArrayList<ItemTenur> lst,
		AsyncCallback<ArrayList<ItemTenur>> res
	) throws IllegalArgumentException;
	
	void cacheAccount(
		ArrayList<ItemAccnt> lst,
		AsyncCallback<ArrayList<ItemAccnt>> res
	) throws IllegalArgumentException;
	
	void cacheProduct(
		ArrayList<ItemProdx> lst,
		AsyncCallback<ArrayList<ItemProdx>> res
	) throws IllegalArgumentException;
	
	void cacheMeeting(
		ArrayList<ItemMeeting> lst,
		AsyncCallback<Void> res
	) throws IllegalArgumentException;
	
	void resetReport1(
		AsyncCallback<ArrayList<ItemBase>> res
	) throws IllegalArgumentException;
	
	void groupReport1(
		int beg,
		int end,
		AsyncCallback<Void> res
	) throws IllegalArgumentException;
}

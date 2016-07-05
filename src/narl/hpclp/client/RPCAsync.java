package narl.hpclp.client;

import java.util.ArrayList;

import narl.hpclp.shared.ItemAccnt;
import narl.hpclp.shared.ItemMeeting;
import narl.hpclp.shared.ItemOwner;
import narl.hpclp.shared.ItemProdx;
import narl.hpclp.shared.ItemTenur;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RPCAsync {
	
	void listMeeting(
		String dayFst,
		String dayEnd,
		AsyncCallback<ArrayList<ItemMeeting>> res
	) throws IllegalArgumentException;
	
	void initServer(AsyncCallback<String> res) throws IllegalArgumentException;
	
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
}

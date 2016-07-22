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
	
	ArrayList<ItemMeeting> listMeeting(String dayFst, String dayEnd);

	ArrayList<ItemOwner> listOwner(String postfix);
	
	ArrayList<ItemTenur> listTenure(String postfix);
	
	ArrayList<ItemProdx> listProduct(String postfix);
	
	ItemOwner modifyOwner(ItemOwner obj) throws IllegalArgumentException;
	ItemTenur modifyTenur(ItemTenur obj) throws IllegalArgumentException;
	ItemAccnt modifyAccnt(ItemAccnt obj) throws IllegalArgumentException;
	ItemProdx modifyProdx(ItemProdx obj) throws IllegalArgumentException;
	
	void cacheOwner(ArrayList<ItemOwner> lst) throws IllegalArgumentException;
	void cacheMeeting(ArrayList<ItemMeeting> lst) throws IllegalArgumentException;
}

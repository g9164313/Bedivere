package narl.hpclp.client;

import java.util.ArrayList;

import narl.hpclp.shared.ItemAccnt;
import narl.hpclp.shared.ItemMeeting;
import narl.hpclp.shared.ItemOwner;
import narl.hpclp.shared.ItemProdx;
import narl.hpclp.shared.ItemTenur;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("calling")
public interface RPC extends RemoteService {
		
	ArrayList<ItemMeeting> listMeeting(String dayFst, String dayEnd);
	
	String initServer() throws IllegalArgumentException;
	
	ItemOwner modifyOwner(ItemOwner obj) throws IllegalArgumentException;
	ItemTenur modifyTenur(ItemTenur obj) throws IllegalArgumentException;
	ItemAccnt modifyAccnt(ItemAccnt obj) throws IllegalArgumentException;
	ItemProdx modifyProdx(ItemProdx obj) throws IllegalArgumentException;
}

package narl.hpclp.client;

import java.util.ArrayList;
import java.util.Date;

import narl.hpclp.shared.ItemMeeting;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("calling")
public interface RPC extends RemoteService {
	
	
	ArrayList<ItemMeeting> listMeeting(String dayFst, String dayEnd);
	
	String initServer() throws IllegalArgumentException;	
}

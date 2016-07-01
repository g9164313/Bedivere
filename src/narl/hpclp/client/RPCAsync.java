package narl.hpclp.client;

import java.util.ArrayList;
import java.util.Date;

import narl.hpclp.shared.ItemMeeting;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RPCAsync {
	
	void listMeeting(
		String dayFst,
		String dayEnd,
		AsyncCallback<ArrayList<ItemMeeting>> res
	) throws IllegalArgumentException;
	
	void initServer(AsyncCallback<String> res) throws IllegalArgumentException;
}

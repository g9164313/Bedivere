package narl.hpclp.client;

import java.util.ArrayList;
import java.util.Date;

import narl.hpclp.shared.ItmMeeting;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RPCAsync {
	
	void listMeeting(
		Date brg,
		Date end,
		AsyncCallback<ArrayList<ItmMeeting>> res
	) throws IllegalArgumentException;
	
	void initServer(AsyncCallback<String> res) throws IllegalArgumentException;
}

package narl.hpclp.client;

import java.util.ArrayList;
import java.util.Date;

import narl.hpclp.shared.ItmMeeting;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("calling")
public interface RPC extends RemoteService {
	
	
	ArrayList<ItmMeeting> listMeeting(Date brg, Date end);
	
	String initServer() throws IllegalArgumentException;	
}

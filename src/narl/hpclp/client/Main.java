package narl.hpclp.client;

import narl.hpclp.client.meeting.PanMain;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class Main implements EntryPoint {
	
	public final static DateTimeFormat fmtDate = DateTimeFormat.getFormat("yyyy/M/d"); 
	
	public final static DateTimeFormat fmtYear = DateTimeFormat.getFormat("yyyy"); 	
	public final static DateTimeFormat fmtMonth = DateTimeFormat.getFormat("M"); 	
	public final static DateTimeFormat fmtDay = DateTimeFormat.getFormat("d"); 
	
	public final static DateTimeFormat fmtMonDay = DateTimeFormat.getFormat("MM/dd");	
	public final static DateTimeFormat fmtWeek = DateTimeFormat.getFormat("EEE");	
	public final static DateTimeFormat fmtMeeting = DateTimeFormat.getFormat("MM/dd EEE H");
	
	public final static DateTimeFormat fmtSQLDay = DateTimeFormat.getFormat("yyyy-M-d");
	

	public static RPCAsync rpc = GWT.create(RPC.class);
	
	public static DlgItemOwner dlgOwner = new DlgItemOwner();
	
	public static DlgItemTenur dlgTenur = new DlgItemTenur();
	
	private static PanMain meeting = new PanMain();
	
	private final static int PAN_MEETING=0;
	private final static int PAN_PRODUCT=1;
	private final static int PAN_ACCOUNT=2;
	
	private static void switch_panel(int id){
		RootPanel.get().clear();
		switch(id){
		case PAN_MEETING:
			RootPanel.get().add(meeting);
			break;
		case PAN_PRODUCT:
			break;
		case PAN_ACCOUNT:
			break;
		}
	}
		
	public static void switchPanMeeting(){
		switch_panel(PAN_MEETING);
	}
	
	
	@Override
	public void onModuleLoad() {
		//MaterialLoader.showLoading(true);
		rpc.initServer(new AsyncCallback<String>(){
			@Override
			public void onFailure(Throwable caught) {
				//MaterialLoader.showLoading(false);
				switchPanMeeting();
			}
			@Override
			public void onSuccess(String result) {
				//MaterialLoader.showLoading(false);
				switchPanMeeting();
			}
		});
		
	}
}

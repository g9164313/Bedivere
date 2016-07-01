package narl.hpclp.client;

import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialSplashScreen;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class Main implements EntryPoint {
	
	public static RPCAsync rpc = GWT.create(RPC.class);
	
	private static PanMeeting meeting = new PanMeeting();

	private final static int PAN_MEETING=0;
	private final static int PAN_PRODX=1;
	private final static int PAN_ACCNT=2;
	
	public final static DateTimeFormat fmtDate = DateTimeFormat.getFormat("yyyy/M/d"); 
	
	public final static DateTimeFormat fmtYear = DateTimeFormat.getFormat("yyyy"); 	
	public final static DateTimeFormat fmtMonth = DateTimeFormat.getFormat("M"); 	
	public final static DateTimeFormat fmtDay = DateTimeFormat.getFormat("d"); 
	
	public final static DateTimeFormat fmtMonDay = DateTimeFormat.getFormat("MM/dd");	
	public final static DateTimeFormat fmtWeek = DateTimeFormat.getFormat("EEE");	
	public final static DateTimeFormat fmtMeetDay = DateTimeFormat.getFormat("yyyy MM/dd H");
	
	public final static DateTimeFormat fmtSQLDay = DateTimeFormat.getFormat("yyyy-M-d");
	
	public static void switchPanMeeting(){
		switch_panel(PAN_MEETING);
	}
	
	private static void switch_panel(int id){
		RootPanel.get().clear();
		switch(id){
		case PAN_MEETING:
			RootPanel.get().add(meeting);
			break;
		}
	}
	
	@Override
	public void onModuleLoad() {
		//MaterialLoader.showLoading(true);
		rpc.initServer(new AsyncCallback<String>(){
			@Override
			public void onFailure(Throwable caught) {
				//MaterialLoader.showLoading(false);
				RootPanel.get().add(meeting);
			}
			@Override
			public void onSuccess(String result) {
				//MaterialLoader.showLoading(false);
				RootPanel.get().add(meeting);
			}
		});
		
	}
}

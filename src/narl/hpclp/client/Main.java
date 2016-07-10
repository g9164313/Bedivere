package narl.hpclp.client;

import java.util.ArrayList;
import java.util.Date;

import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import narl.hpclp.shared.Const;
import narl.hpclp.shared.ItemMeeting;
import narl.hpclp.shared.ItemOwner;
import narl.hpclp.shared.ItemParam;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class Main implements EntryPoint {
	
	public final static DateTimeFormat fmtDate = DateTimeFormat.getFormat("yyyy/M/d"); 
	
	public final static DateTimeFormat fmtStmpLast = DateTimeFormat.getFormat("yyyy/M/d H:m");
	
	public final static DateTimeFormat fmtYear = DateTimeFormat.getFormat("yyyy"); 	
	public final static DateTimeFormat fmtMonth = DateTimeFormat.getFormat("M"); 	
	public final static DateTimeFormat fmtDay = DateTimeFormat.getFormat("d"); 
	
	public final static DateTimeFormat fmtMonthDay = DateTimeFormat.getFormat("MM/dd");	
	public final static DateTimeFormat fmtWeekDay = DateTimeFormat.getFormat("EEE");	
	public final static DateTimeFormat fmtMeeting = DateTimeFormat.getFormat("MM/dd EEE H");
	
	public final static DateTimeFormat fmtSQLDay = DateTimeFormat.getFormat("yyyy-M-d");
	
	public static boolean text2stmp(MaterialTextBox box,Date stmp){
		String tmp = fmtStmpLast.format(stmp);
		boolean valid = true;
		try{
			Date dd = fmtStmpLast.parse(box.getText());
			stmp.setTime(dd.getTime());
			tmp = fmtStmpLast.format(stmp);
		}catch(IllegalArgumentException e){
			MaterialToast.fireToast("錯誤的日期格式");
			valid = false;
		}
		box.setText(tmp);
		return valid;
	}
	
	public static RPCAsync rpc = GWT.create(RPC.class);
	
	public static DlgItemOwner dlgOwner = new DlgItemOwner();
	
	public static DlgItemTenur dlgTenur = new DlgItemTenur();
	
	public static ItemParam param = null;
	//----------------------------//
	
	public static void printLetter(ItemOwner itm){
		ArrayList<ItemOwner> lst = new ArrayList<ItemOwner>();
		lst.add(itm);
		printLetter(lst);
	}
	public static void printLetter(ArrayList<ItemOwner> buf){
		rpc.cacheOwner(buf, new AsyncCallback<Void>(){
			@Override
			public void onFailure(Throwable caught) {
				MaterialToast.fireToast("內部錯誤");
			}
			@Override
			public void onSuccess(Void result) {
				String url = GWT.getHostPageBaseURL();
		    	url = url + Const.REPORT_LETTER;
		    	Window.open(url,"_blank","");
			}
		});
	}
	
	public static void printNotify(ItemMeeting itm){
		ArrayList<ItemMeeting> lst = new ArrayList<ItemMeeting>();
		lst.add(itm);
		printNotify(lst);
	}
	public static void printNotify(ArrayList<ItemMeeting> buf){
		rpc.cacheMeeting(buf, new AsyncCallback<Void>(){
			@Override
			public void onFailure(Throwable caught) {
				MaterialToast.fireToast("內部錯誤");
			}
			@Override
			public void onSuccess(Void result) {
				String url = GWT.getHostPageBaseURL();
		    	url = url + Const.REPORT_NOTIFY;
		    	Window.open(url,"_blank","");
			}
		});
	}
	//----------------------------//
	
	private static narl.hpclp.client.meeting.PanMain meeting = new narl.hpclp.client.meeting.PanMain();
	private static narl.hpclp.client.product.PanMain product = new  narl.hpclp.client.product.PanMain();
	
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
			RootPanel.get().add(product);
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
		rpc.initServer(new AsyncCallback<ItemParam>(){
			@Override
			public void onFailure(Throwable caught) {
				switchPanMeeting();
			}
			@Override
			public void onSuccess(ItemParam result) {
				if(result.error!=null){
					//just post a error message~~~~
					RootPanel.get().add(new Label(result.error));
					return;
				}
				param = result;
				dlgTenur.initDetectType();
				switchPanMeeting();
			}
		});
		
	}
}

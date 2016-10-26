package nthu.hpclp.client;

import java.util.ArrayList;
import java.util.Date;

import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemAccnt;
import nthu.hpclp.shared.ItemMeeting;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemParam;
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ParamHub;

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
	
	public final static String date2tw_y(Date stamp){		
		String year = fmtYear.format(stamp);		
		int yy = Integer.valueOf(year);		
		if(yy>1911){
			yy = yy - 1911;
		}else{
			yy = 0;
		}		
		return ""+yy;
	}
	public final static String date2tw_y(){
		return date2tw_y(new Date());
	}
	public final static String date2tw(Date stamp){
		String yyy = date2tw_y(stamp);
		String mm_dd = fmtMonthDay.format(stamp);
		return yyy+"/"+mm_dd;
	}
	public final static String date2tw(){
		return date2tw(new Date());
	}

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
	
	public static DlgEditOwner dlgEditOwner = new DlgEditOwner();	
	public static DlgEditTenur dlgEditTenur = new DlgEditTenur();
	
	public static DlgPickOwner dlgPickOwner = new DlgPickOwner();
	public static DlgPickTenur dlgPickTenur = new DlgPickTenur();
	
	public static ParamHub param = new ParamHub();//enviroment parameters
	//----------------------------//
	
	public static void printLetter(ItemOwner itm){
		ArrayList<ItemOwner> lst = new ArrayList<ItemOwner>();
		lst.add(itm);
		printLetter(lst);
	}
	public static void printLetter(ArrayList<ItemOwner> buf){
		rpc.cacheOwner(buf, new AsyncCallback<ArrayList<ItemOwner>>(){
			@Override
			public void onFailure(Throwable caught) {
				MaterialToast.fireToast("內部錯誤");
			}
			@Override
			public void onSuccess(ArrayList<ItemOwner> result) {
		    	Window.open(
		    		GWT.getHostPageBaseURL()+Const.PRINT_LETTER,
		    		"_blank",""
		    	);
			}
		});
	}

	public static void printProduct(ItemProdx buf){
		ArrayList<ItemProdx> lst = new ArrayList<ItemProdx>();
		lst.add(buf);
		printProduct(lst);
	}
	public static void printProduct(ArrayList<ItemProdx> buf){
		//TODO:how to classify product???
		rpc.cacheProduct(buf, new AsyncCallback<ArrayList<ItemProdx>>(){
			@Override
			public void onFailure(Throwable caught) {
				MaterialToast.fireToast("內部錯誤");
			}
			@Override
			public void onSuccess(ArrayList<ItemProdx> result) {				
		    	Window.open(
		    		GWT.getHostPageBaseURL()+Const.REPORT_PRODUCT,
		    		"_blank",""
		    	);
			}
		});
	}
	
	public static void printDemand(ItemAccnt buf){
		ArrayList<ItemAccnt> lst = new ArrayList<ItemAccnt>();
		lst.add(buf);
		printAccount(lst,Const.REPORT_DEMAND);
	}
	public static void printDemand(ArrayList<ItemAccnt> buf){
		printAccount(buf,Const.REPORT_DEMAND);
	}
	public static void printService(ItemAccnt buf){
		ArrayList<ItemAccnt> lst = new ArrayList<ItemAccnt>();
		lst.add(buf);
		printAccount(lst,Const.REPORT_SERVICE);
	}
	public static void printService(ArrayList<ItemAccnt> buf){
		printAccount(buf,Const.REPORT_SERVICE);
	}
	private static void printAccount(ArrayList<ItemAccnt> buf,final String type){
		rpc.cacheAccount(buf, new AsyncCallback<ArrayList<ItemAccnt>>(){
			@Override
			public void onFailure(Throwable caught) {
				MaterialToast.fireToast("內部錯誤");
			}
			@Override
			public void onSuccess(ArrayList<ItemAccnt> result) {				
		    	Window.open(
		    		GWT.getHostPageBaseURL()+type,
		    		"_blank",""
		    	);
			}
		});
	}
	
	public static void printNotify(ItemMeeting itm){
		ArrayList<ItemMeeting> lst = new ArrayList<ItemMeeting>();
		lst.add(itm);
		printMeeting(lst,Const.PRINT_NOTIFY);
	}
	public static void printNotify(ArrayList<ItemMeeting> buf){
		printMeeting(buf,Const.PRINT_NOTIFY);
	}
	public static void printSchedule(ArrayList<ItemMeeting> buf){
		printMeeting(buf,Const.PRINT_SCHEDULE);
	}
	private static void printMeeting(ArrayList<ItemMeeting> buf,final String type){
		rpc.cacheMeeting(buf, new AsyncCallback<Void>(){
			@Override
			public void onFailure(Throwable caught) {
				MaterialToast.fireToast("內部錯誤");
			}
			@Override
			public void onSuccess(Void result) {
		    	Window.open(
		    		GWT.getHostPageBaseURL()+type,
		    		"_blank",""
		    	);
			}
		});
	}
	//----------------------------//
	
	public static void initComboEmitter(MaterialListBox box){
		box.clear();
		for(ItemParam pair:param.prodxEmitter){
			String val = pair.getVal();
			//select the last emitter, it is special!!!
			combo_add_emitt(box,val);
		}
		box.setSelectedIndex(0);
	}
	
	private static void combo_add_emitt(MaterialListBox box,String arg){
		int pos = arg.indexOf('@');
		if(pos<0){
			box.addItem("???",arg);
		}else{
			String name = arg.substring(0,pos);
			box.addItem(name,arg);
		}
	}
	//----------------------------//
	
	private static nthu.hpclp.client.account.PanMain account = new nthu.hpclp.client.account.PanMain();
	private static nthu.hpclp.client.meeting.PanMain meeting = new nthu.hpclp.client.meeting.PanMain();
	private static nthu.hpclp.client.product.PanMain product = new nthu.hpclp.client.product.PanMain();
	private static nthu.hpclp.client.setting.PanMain setting = new nthu.hpclp.client.setting.PanMain();
	
	private final static int PAN_DEFAULT=1;//Do we need to fix this? No~~~
	private final static int PAN_MEETING=0;
	private final static int PAN_PRODUCT=1;
	private final static int PAN_ACCOUNT=2;
	private final static int PAN_SETTING=3;
	
	private static void switch_panel(int id){
		RootPanel.get().clear();
		switch(id){
		default:
		case PAN_MEETING:
			RootPanel.get().add(meeting);
			break;
		case PAN_PRODUCT:
			RootPanel.get().add(product);			
			break;
		case PAN_ACCOUNT:
			RootPanel.get().add(account);
			break;
		case PAN_SETTING:
			RootPanel.get().add(setting);
			break;
		}
	}
		
	public static void switchToMeeting(){
		switch_panel(PAN_MEETING);		
	}
	
	public static void switchToProduct(){
		switch_panel(PAN_PRODUCT);
	}
	
	public static void switchToAccount(){
		switch_panel(PAN_ACCOUNT);
	}
	
	public static void switchToSetting(){
		switch_panel(PAN_SETTING);
	}
		
	@Override
	public void onModuleLoad() {
		//MaterialLoader.showLoading(true);
		//Event.addNativePreviewHandler(eventHook);
		rpc.initServer(
			param,
			new AsyncCallback<ParamHub>(){
			@Override
			public void onFailure(Throwable caught) {
			}
			@Override
			public void onSuccess(ParamHub result) {
				if(result.error!=null){
					//just post a error message~~~~
					RootPanel.get().add(new Label(result.error));
					return;
				}
				param = result;
				dlgEditTenur.initDetectType();
				switch_panel(PAN_DEFAULT);
			}
		});
	}
}

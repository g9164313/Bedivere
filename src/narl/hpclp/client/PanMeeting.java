package narl.hpclp.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import narl.hpclp.shared.ItmMeeting;
import gwt.material.design.client.ui.MaterialCardContent;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialContainer;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;
import gwt.material.design.client.ui.MaterialSplashScreen;
import gwt.material.design.client.ui.MaterialToast;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.Selection;
import com.googlecode.gwt.charts.client.calendar.Calendar;
import com.googlecode.gwt.charts.client.calendar.CalendarOptions;
import com.googlecode.gwt.charts.client.event.SelectEvent;
import com.googlecode.gwt.charts.client.event.SelectHandler;
import com.googlecode.gwt.charts.client.gauge.Gauge;
import com.googlecode.gwt.charts.client.gauge.GaugeOptions;

public class PanMeeting extends Composite {

	private static PanMeetingUiBinder uiBinder = GWT
			.create(PanMeetingUiBinder.class);

	interface PanMeetingUiBinder extends UiBinder<Widget, PanMeeting> {
	}

    @UiField
    MaterialNavBar appNav, searchNav;

    @UiField
    MaterialSearch search;
    
    @UiField
    MaterialLink lnkPanProdx,lnkPanAccnt;
     
    @UiField
    MaterialPanel panArch1;
    
    @UiField
    MaterialCollapsible lstOwner,lstTenur;

	public PanMeeting() {
		initWidget(uiBinder.createAndBindUi(this));
		search.addCloseHandler(new CloseHandler<String>() {
            @Override
            public void onClose(CloseEvent<String> event) {
                appNav.setVisible(true);
                searchNav.setVisible(false);
            }
        });
		search.addChangeHandler(new ChangeHandler(){
			@Override
			public void onChange(ChangeEvent event) {				
			}
		});
		
		new ChartLoader(ChartPackage.CALENDAR).loadApi(new Runnable(){
			@Override
			public void run() {
				chrMeet = new Calendar();
				chrMeet.addSelectHandler(eventPickDay);				
				chrMeet.setHeight("330px");//??why??
				panArch1.add(chrMeet);				
			}
		});
	}

	@UiHandler("lnkPanProdx")
	void onLnkProdx(ClickEvent e){
		
	}
	
	@UiHandler("lnkPanAccnt")
	void onLnkAccnt(ClickEvent e){
		
	}
	
    @UiHandler("lnkSearch")
    void onSearch(ClickEvent e) {
        appNav.setVisible(false);
        searchNav.setVisible(true);
    }
       
    private Calendar chrMeet;
    
    private ArrayList<ItmMeeting> lstMeet = null;
    
    private HashMap<Integer,Integer> grpStart= new HashMap<Integer,Integer>();
    private HashMap<Integer,Integer> grpTail = new HashMap<Integer,Integer>();
    
    @UiHandler("lnkRenew")
    void onRenew(ClickEvent e) {
    	MaterialLoader.showLoading(true);
    	
    	Date day = new Date();    	
    	CalendarUtil.setToFirstDayOfMonth(day);
    	String beg = Main.fmtSQLDay.format(day)+" 00:00:00";
    	
    	CalendarUtil.addMonthsToDate(day,12);
    	String end = Main.fmtSQLDay.format(day)+" 24:00:00";
    	
    	Main.rpc.listMeeting(beg, end, new AsyncCallback<ArrayList<ItmMeeting>>(){
			@Override
			public void onFailure(Throwable caught) {
				lstMeet = null;
				chrMeet.clearChart();
				MaterialLoader.showLoading(false);
				MaterialToast.fireToast(caught.getMessage());
			}
			@Override
			public void onSuccess(ArrayList<ItmMeeting> result) {
				lstMeet = result;
				chrMeet.clearChart();								
		    	if(lstMeet.isEmpty()==true){
		    		MaterialLoader.showLoading(false);
		    		return;
		    	}
		    	MaterialLoader.showLoading(false);
		    	MaterialToast.fireToast("共"+result.size()+"筆預約項目");
		    	group();
		    	draw();				
			}
    	});
    }
 
    private void group(){
    	grpStart.clear();
    	grpTail.clear();    	
		int idxGroup=0;//mapping to the row of chart~~~		
		String txtDay = lstMeet.get(0).day;
		grpStart.put(idxGroup,0);
    	for(int i=0; i<lstMeet.size(); i++){
    		String day = lstMeet.get(i).day;
			if(day.equalsIgnoreCase(txtDay)==false){				
				grpTail.put(idxGroup,i-1);				
				txtDay = day;//update~~~
				idxGroup++;
				grpStart.put(idxGroup,i);
			}		
    	}
    	grpTail.put(idxGroup,lstMeet.size()-1);//the last one~~~
    }
    
    private void draw(){
    	
		DataTable tab = DataTable.create();		
		tab.addColumn(ColumnType.DATE,"預約日期");
		tab.addColumn(ColumnType.NUMBER,"儀器數量");
		tab.addRows(grpStart.size());
		
		for(int i=0; i<grpStart.size(); i++){
			int beg = grpStart.get(i);
			int end = grpTail.get(i);		
			int cnt = 0;
			for(int j=beg; j<=end; j++){
				ItmMeeting itm = lstMeet.get(i);
				cnt = cnt + itm.lst.size();
			}
			tab.setValue(i, 0, Main.fmtDate.parse(lstMeet.get(beg).day));
			tab.setValue(i, 1, cnt);
		}
		
		CalendarOptions opt = CalendarOptions.create();
		opt.setTitle("儀器數量");
		chrMeet.draw(tab, opt);
    }
    
    private SelectHandler eventPickDay = new SelectHandler(){
		@Override
		public void onSelect(SelectEvent event) {
			lstOwner.clear();
			lstTenur.clear();
			if(lstMeet==null){
				return;
			}
			Integer idx = chrMeet.getSelection().get(0).getRow();
			if(idx==null){
				return;
			}
			//String txt = "";
			int beg = grpStart.get(idx);
			int end = grpTail.get(idx);			
			for(int i=beg; i<=end; i++){
				ItmMeeting itm = lstMeet.get(i);
				//lstOwner.add(new );
			}
			//ItmMeeting itm = lstMeet.get(idx);
			//lstOwner.add(child);
			//System.out.println("row = "+idx+"\n"+txt);
			//MaterialToast.fireToast(txt+"total="+cnt);
			//lstOwner.clear();
			
		}
    };
}

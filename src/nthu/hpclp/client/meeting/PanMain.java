package nthu.hpclp.client.meeting;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialLabel;

import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;
import gwt.material.design.client.ui.MaterialToast;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.ItemMeeting;
import nthu.hpclp.shared.ItemOwner;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.calendar.Calendar;
import com.googlecode.gwt.charts.client.calendar.CalendarOptions;
import com.googlecode.gwt.charts.client.event.SelectEvent;
import com.googlecode.gwt.charts.client.event.SelectHandler;

public class PanMain extends Composite {

	private static PanMainUiBinder uiBinder = GWT.create(PanMainUiBinder.class);

	interface PanMainUiBinder extends UiBinder<Widget, PanMain> {
	}

	@UiField
	MaterialPanel root;
			
    @UiField
    MaterialNavBar appNav, searchNav;

    @UiField
    MaterialSearch search;
 
    @UiField
    MaterialLabel txtPickDay;
    		
    @UiField
    MaterialPanel panArch1;
    
    @UiField
    public MaterialCollapsible colOwner,colTenur;

	public PanMain() {
		initWidget(uiBinder.createAndBindUi(this));		
		initSearch();
		root.add(Main.dlgEditOwner);
		root.add(Main.dlgEditTenur);		
		addAttachHandler(eventShowHide);

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

	private AttachEvent.Handler eventShowHide = new AttachEvent.Handler(){
		@Override
		public void onAttachOrDetach(AttachEvent event) {
			if(event.isAttached()==true){
				refresh();
			}
		}
	};
		
	@UiHandler("lnkPanProdx")
	void onLnkPanProdx(ClickEvent e){
		Main.switchToProduct();
	}
	
	@UiHandler("lnkPanAccnt")
	void onLnkPanAccnt(ClickEvent e){
		Main.switchToAccount();
	}
	
	@UiHandler("lnkPanSetting")
	void onPanStorage(ClickEvent e){
		Main.switchToSetting();
	}
	
    @UiHandler("lnkSearch")
    void onSearch(ClickEvent e) {
        appNav.setVisible(false);
        searchNav.setVisible(true);
    }
    private void initSearch(){
		search.addCloseHandler(new CloseHandler<String>() {
            @Override
            public void onClose(CloseEvent<String> event) {
                appNav.setVisible(true);
                searchNav.setVisible(false);
            }
        });
		search.addChangeHandler(eventSearch);
    }
    private final ChangeHandler eventSearch = new ChangeHandler(){
		@Override
		public void onChange(ChangeEvent event) {
		}
    };
   
    private final String MSG1 = "請指定圖表裡的日期";
    
    @UiHandler("lnkPrintSchedule")
    void onLnkPrintSchedule(ClickEvent e) {
    	ArrayList<ItemMeeting> buf = new ArrayList<ItemMeeting>();
    	gatherMonth(buf);
    	if(buf.size()==0){
    		MaterialToast.fireToast(MSG1);
    	}else{
    		Main.printSchedule(buf);
    	}
    }
    
    @UiHandler("lnkPrintNotify")
    void onLnkPrintNotify(ClickEvent e) {
    	ArrayList<ItemMeeting> buf = new ArrayList<ItemMeeting>();
    	gatherMonth(buf);
    	if(buf.size()==0){
    		MaterialToast.fireToast(MSG1);    		
    	}else{
    		Main.printNotify(buf);
    	}
    }
    
    @UiHandler("lnkPrintLetter")
    void onLnkPrintLetter(ClickEvent e) {
		ArrayList<ItemOwner> buf = new ArrayList<ItemOwner>();
		gatherMonth(buf);
    	if(buf.size()==0){
    		MaterialToast.fireToast(MSG1);    		
    	}else{
    		Main.printLetter(buf);
    	}
    }

    @SuppressWarnings("unchecked")
	private <T> void gatherMonth(ArrayList<T> lst){
    	Integer idx = chrMeet.getSelection().get(0).getRow();
		if(idx==null){
			return;
		}
		int beg = grpHead.get(idx);
		ItemMeeting itm = lstMeet.get(beg);
		String dd = itm.getSDay();
		dd = dd.substring(0,dd.lastIndexOf('/'));
		while(beg>0){
			itm = (ItemMeeting)lstMeet.get(beg);
			String txt = itm.getSDay();
			if(txt.startsWith(dd)==true){
				beg--;
			}else{
				break;
			}			
		}
		int end = grpTail.get(idx);
		while(end<lstMeet.size()){
			itm = (ItemMeeting)lstMeet.get(end);
			String txt = itm.getSDay();
			if(txt.startsWith(dd)==true){
				end++;
			}else{
				break;
			}
		}
		for(int i=beg; i<end; i++){			
			lst.add((T)lstMeet.get(i));
		}
    }
    
    /*@UiHandler("lnkPrintLetter2")
    void onPrintLetter2(ClickEvent e) {
    	ArrayList<ItemOwner> buf = new ArrayList<ItemOwner>();
    	gatherOneDay(buf);
    	if(buf.size()==0){
    		MaterialToast.fireToast(MSG1);
    		return;
    	}
    	Main.printLetter(buf);
    }
    @UiHandler("lnkPrintNotify2")
    void onPrintNotify2(ClickEvent e) {
    	ArrayList<ItemMeeting> buf = new ArrayList<ItemMeeting>();
    	gatherOneDay(buf);
    	if(buf.size()==0){
    		MaterialToast.fireToast(MSG1);
    		return;
    	}
    	Main.printNotify(buf);
    }
    
    @SuppressWarnings("unchecked")
	private <T> void gatherOneDay(ArrayList<T> lst){
    	Integer idx = chrMeet.getSelection().get(0).getRow();
		if(idx==null){
			return;
		}
		int beg = grpHead.get(idx);
		int end = grpTail.get(idx);
		for(int i=beg; i<=end; i++){
			lst.add(((T)lstMeet.get(i)));
		}
    }*/
    
    @UiHandler("lnkRenew")
    void onLnkRenew(ClickEvent e) {
    	refresh();
    }
 
    private SelectHandler eventPickDay = new SelectHandler(){
		@Override
		public void onSelect(SelectEvent event) {
			txtPickDay.setText("");
			colOwner.clear();
			colTenur.clear();
			if(lstMeet==null){
				return;
			}
			Integer idx = chrMeet.getSelection().get(0).getRow();
			if(idx==null){
				return;
			}
			int beg = grpHead.get(idx);
			int end = grpTail.get(idx);
			ItemMeeting itm = null;
			for(int i=beg; i<=end; i++){
				itm = lstMeet.get(i);
				colOwner.add(new CpiOwner(PanMain.this,itm));
			}
			if(itm!=null){
				txtPickDay.setText(itm.getSDay());
			}		
		}
    };

    private Calendar chrMeet;
    
    public ArrayList<ItemMeeting> lstMeet = null;
    private HashMap<Integer,Integer> grpHead = new HashMap<Integer,Integer>();
    private HashMap<Integer,Integer> grpTail = new HashMap<Integer,Integer>();

    public void refresh(){
    	MaterialLoader.showLoading(true);
    	
    	Date day = new Date();    	
    	CalendarUtil.setToFirstDayOfMonth(day);
    	String beg = Main.fmtSQLDay.format(day)+" 00:00:00";
    	
    	CalendarUtil.addMonthsToDate(day,12);
    	String end = Main.fmtSQLDay.format(day)+" 24:00:00";
    	
    	Main.rpc.listMeeting(beg, end, new AsyncCallback<ArrayList<ItemMeeting>>(){
			@Override
			public void onFailure(Throwable caught) {
				lstMeet = null;
				if(chrMeet!=null){ chrMeet.clearChart(); }
				MaterialLoader.showLoading(false);
				MaterialToast.fireToast(caught.getMessage());
			}
			@Override
			public void onSuccess(ArrayList<ItemMeeting> result) {
				lstMeet = result;
		    	if(lstMeet.isEmpty()==true){
		    		MaterialLoader.showLoading(false);
		    		return;
		    	}
		    	MaterialLoader.showLoading(false);
		    	MaterialToast.fireToast("共"+result.size()+"筆預約項目");
		    	redraw();
			}
    	});
    }
    
    public void redraw(){
    	colOwner.clear();
		colTenur.clear();
    	groupItem();
    	drawChart();
    }
    
    private void groupItem(){
    	grpHead.clear();
    	grpTail.clear();    	
		int idxGroup=0;//mapping to the row of chart~~~		
		String txtDay = lstMeet.get(0).getSDay();
		grpHead.put(idxGroup,0);
    	for(int i=0; i<lstMeet.size(); i++){
    		String day = lstMeet.get(i).getSDay();
			if(day.equalsIgnoreCase(txtDay)==false){				
				grpTail.put(idxGroup,i-1);				
				txtDay = day;//update~~~
				idxGroup++;
				grpHead.put(idxGroup,i);
			}		
    	}
    	grpTail.put(idxGroup,lstMeet.size()-1);//the last one~~~
    }
    
    private void drawChart(){
    	chrMeet.clearChart();
		DataTable tab = DataTable.create();		
		tab.addColumn(ColumnType.DATE,"預約日期");
		tab.addColumn(ColumnType.NUMBER,"儀器數量");
		tab.addRows(grpHead.size());
		
		for(int i=0; i<grpHead.size(); i++){
			int beg = grpHead.get(i);
			int end = grpTail.get(i);
			int cnt = 0;
			ItemMeeting itm = lstMeet.get(beg);
			if(itm==null){
				break;//Is it possible???
			}
			for(int j=beg; j<=end; j++){
				itm = lstMeet.get(j);
				cnt = cnt + itm.lst.size();
			}
			tab.setValue(i, 0, Main.fmtDate.parse(itm.getSDay()));
			if(itm.isRestday()==true){
				tab.setValue(i, 1, -30);//treak restday as red color~~~
			}else{
				tab.setValue(i, 1, cnt);
			}
		}

		CalendarOptions opt = CalendarOptions.create();
		opt.setTitle("儀器數量");
		chrMeet.draw(tab, opt);
    }
}

package narl.hpclp.client;

import java.util.Date;

import gwt.material.design.client.ui.MaterialCardContent;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialContainer;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;
import gwt.material.design.client.ui.MaterialSplashScreen;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.calendar.Calendar;
import com.googlecode.gwt.charts.client.calendar.CalendarOptions;
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
    MaterialCollapsible lstMeeting;
    
    private Calendar chart;
    
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
				chart = new Calendar();				
				panArch1.add(chart);
				
				DataTable dataTable = DataTable.create();
				dataTable.addColumn(ColumnType.DATE, "Date");
				dataTable.addColumn(ColumnType.NUMBER, "Won/Loss");
				dataTable.addRows(1);
				dataTable.setValue(0, 0, create(2012, 3, 13));
				dataTable.setValue(0, 1, -1);
				CalendarOptions options = CalendarOptions.create();
				options.setTitle("Red Sox Attendance");
				chart.draw(dataTable, options);
				//chart.clearChart();
				
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
    
    @UiHandler("lnkRenew")
    void onRenew(ClickEvent e) {

    }
    
	@SuppressWarnings("deprecation")
	public static Date create(int year, int month, int day) {
		return new Date(year - 1900, month, day);
	}
}

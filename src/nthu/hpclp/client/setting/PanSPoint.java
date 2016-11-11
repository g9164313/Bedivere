package nthu.hpclp.client.setting;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialToast;
import narl.itrc.client.ExComposite;
import nthu.hpclp.client.Main;

public class PanSPoint extends ExComposite {

	private static PanSPointUiBinder uiBinder = GWT.create(PanSPointUiBinder.class);

	interface PanSPointUiBinder extends UiBinder<Widget, PanSPoint> {
	}

	public PanSPoint() {
		initWidget(uiBinder.createAndBindUi(this));
		initAddins(root);
		init_data_grid();
		arch1.setWidget(grd);	
	}
	
	@UiField
	MaterialPanel root;
	@UiField
	SimplePanel arch1;
	
	@Override
	public void onEventShow() {
		list_spoint();
	}

	@Override
	public void onEventHide() {
	}
	
	private void list_spoint(){
		final AsyncCallback<String[]> event = new AsyncCallback<String[]>(){
			@Override
			public void onFailure(Throwable caught) {
				dlgNotify.show(caught.getMessage());
			}
			@Override
			public void onSuccess(String[] result) {
				if(result==null){
					MaterialToast.fireToast("無法取得內部資料夾",1000);
					return;
				}				
				lst.setList(Arrays.asList(result));
				lst.refresh();
			}
		};
		Main.rpc.listSPoint(event);
	}
		
    @UiHandler("btnBackup")
    void onBackup(ClickEvent e) {
    	MaterialLoader.showLoading(true);	
		Main.rpc.saveSPoint(eventRunDone);
    }
    
    @UiHandler("btnRestore")
    void onRestore(ClickEvent e) {
    	String name = mod.getSelectedObject();
		if(name==null){
			dlgNotify.show("請選擇一個還原點");
			return;
		}
		MaterialLoader.showLoading(true);	
		Main.rpc.loadSPoint(name,eventRunDone);
    }
    
    @UiHandler("btnSyncData")
    void onSyncRemote(ClickEvent e) {
    	//How to do this task???
    }

	private final AsyncCallback<String> eventRunDone = new AsyncCallback<String>(){
		@Override
		public void onFailure(Throwable caught) {			
			dlgNotify.show(caught.getMessage());
		}
		@Override
		public void onSuccess(String result) {
			onEventShow();
			MaterialLoader.showLoading(false);
			if(result.length()==0){
				result = "執行完畢";
			}
			dlgNotify.show(result);				
		}
	};
	
	private DataGrid<String> grd = new DataGrid<String>();
	private ListDataProvider<String> lst = new ListDataProvider<String>();
	private SingleSelectionModel<String> mod = new SingleSelectionModel<String>();
	
	private void init_data_grid(){		
		final TextColumn<String> col = new TextColumn<String>(){
			@Override
			public String getValue(String object) {
				return object;
			}
		};
		grd.setSelectionModel(mod);
		grd.setSize("90%","90%");
		grd.setEmptyTableWidget(new Label("無資料"));
		grd.addColumn(col,"還原點");
		
		//control part
		SimplePager pager = new SimplePager();
		pager.setDisplay(grd);
		//model part
		lst.addDataDisplay(grd);		
	}	
}

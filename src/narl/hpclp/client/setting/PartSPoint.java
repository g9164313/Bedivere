package narl.hpclp.client.setting;

import java.util.Arrays;

import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialToast;
import narl.hpclp.client.Main;
import narl.itrc.client.ExComposite;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

public class PartSPoint extends ExComposite {

	private static PartSPointUiBinder uiBinder = GWT
		.create(PartSPointUiBinder.class);

	interface PartSPointUiBinder extends UiBinder<Widget, PartSPoint> {
	}
	
	@UiField
	MaterialPanel root;
	
	@UiField
	SimplePanel arch1,arch2;
	
	private DataGrid<String> grdSName = new DataGrid<String>();
	
	private ListDataProvider<String> lstSPoint = new ListDataProvider<String>();
	
	private SingleSelectionModel<String> modSName = new SingleSelectionModel<String>();
	
	public PartSPoint() {
		initWidget(uiBinder.createAndBindUi(this));		
		initAddins(root);
		initSelector();
	}

	private void initSelector(){
		//view part
		final TextColumn<String> col = new TextColumn<String>(){
			@Override
			public String getValue(String object) {
				return object;
			}
		};
		grdSName.setSelectionModel(modSName);
		grdSName.setSize("100%","43vh");
		grdSName.addColumn(col,"還原點");
		//control part
		SimplePager pager = new SimplePager();
		pager.setDisplay(grdSName);
		//model part
		lstSPoint.addDataDisplay(grdSName);		
		//finally, attach to panel
		arch1.setWidget(grdSName);		
		arch2.setWidget(pager);
	}
	
	@Override
	public void onEventShow() {
		final AsyncCallback<String[]> event = new AsyncCallback<String[]>(){
			@Override
			public void onFailure(Throwable caught) {
				MaterialToast.fireToast("內部錯誤："+caught.getMessage(),5000);
			}
			@Override
			public void onSuccess(String[] result) {
				if(result==null){
					MaterialToast.fireToast("內部錯誤：無法取得資料夾",5000);
					return;
				}				
				lstSPoint.setList(Arrays.asList(result));
				lstSPoint.refresh();
			}
		};
		Main.rpc.listSPoint(event);
	}

	@Override
	public void onEventHide() {
	}
	
	@UiHandler("btnSynchr")
	void onSynchr(ClickEvent e){
		//check whether data in each database are same 
		MaterialLoader.showLoading(true);	
		/*Main.rpc.runScript(
			"cmd @ arg1 @ arg2",
			eventRunDone
		);*/
	}
	
	@UiHandler("btnBackup")
	void onBackup(ClickEvent e){
		//save current database
		MaterialLoader.showLoading(true);	
		Main.rpc.saveSPoint(eventRunDone);
	}

	@UiHandler("btnRestore")
	void onRestore(ClickEvent e){
		//restore the current file to database		
		String name = modSName.getSelectedObject();
		if(name==null){
			dlgNotify.show("請選擇一個還原點");
			return;
		}
		MaterialLoader.showLoading(true);	
		Main.rpc.loadSPoint(name,eventRunDone);
	}
	
	private final AsyncCallback<String> eventRunDone = new AsyncCallback<String>(){
		@Override
		public void onFailure(Throwable caught) {			
			MaterialLoader.showLoading(false);
			MaterialToast.fireToast("內部錯誤："+caught.getMessage(),5000);			
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
}

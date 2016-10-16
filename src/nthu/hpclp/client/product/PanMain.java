package nthu.hpclp.client.product;

import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;
import nthu.hpclp.client.PartOwne;
import nthu.hpclp.shared.ItemProdx;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class PanMain extends PanCtrl {

	private static PanMainUiBinder uiBinder = GWT.create(PanMainUiBinder.class);

	interface PanMainUiBinder extends UiBinder<Widget, PanMain> {
	}
    
	public PanMain() {
		initWidget(uiBinder.createAndBindUi(this));
		initList();
		root.add(genReport);
		anchorOwner.setWidget(owner);
		anchorTenur.setWidget(tenur);
		anchorInfo.setWidget(inform);
		anchorScriber.setWidget(scriber);
		anchorEmitter.setWidget(emitter);
	}

	@UiField(provided=true) 
	MaterialPanel root = _root;
    @UiField
    MaterialNavBar navApp, navSearch;
    @UiField
    MaterialSearch boxSearch;
	@UiField
	SimplePanel anchorOwner,anchorTenur,anchorInfo;
	@UiField
	SimplePanel anchorScriber,anchorEmitter;
	
    @UiField(provided=true)
    MaterialModal dlgList = _dlgList;
    @UiField(provided=true) 
    SimplePanel anchorList1 = _anchorList1;
    @UiField(provided=true) 
    SimplePanel anchorList2 = _anchorList2;

	private PartOwne owner = new PartOwne();
	private PartTenu tenur = new PartTenu();
	private PartInfo inform = new PartInfo();
	private PartScriber scriber = new PartScriber();
	private PartEmitter emitter = new PartEmitter();
	private DlgGenReport genReport = new DlgGenReport();
	
    @UiHandler("lnkStartSearch")
    void onStartSearch(ClickEvent e) {
        navApp.setVisible(false);
        navSearch.setVisible(true);  
        boxSearch.setFocus(true);
    }
    @UiHandler("boxSearch")
    void onCloseSearch(CloseEvent<String> event){
    	navApp.setVisible(true);
        navSearch.setVisible(false);
        //focus another widget???
    }
    @UiHandler("boxSearch")
    void onSearching(KeyDownEvent event) {
    	int code = event.getNativeKeyCode();
    	if(code!=KeyCodes.KEY_ENTER){
    		return;
    	}
    	//TODO:search something~~~
    	boxSearch.setText("");
    	boxSearch.setFocus(true);
    }
    
    @UiHandler("lnkListShow")
    void onListShow(ClickEvent e){
    	dlgList.openModal();
    }
    @UiHandler("lnkListLast")
    void onListUpdate(ClickEvent e){
    	listLast50();
    }
    @UiHandler("lnkListClear")
    void onShowList(ClickEvent e){
    	//Main.dlgApprove.appear("有未儲存的項目，確認清除?", hook);
    	listClear();
    }
    @UiHandler("lnkListAddItem")
    void onListAddItem(ClickEvent e){
    	listAddItem();
    }
    @UiHandler("lnkListUpload")
    void onUploadList(ClickEvent e){
    	listUpload();
    }
    
    @UiHandler("lnkPrintReport")
    void onPrintReport(ClickEvent e){
    	genReport.appear();
    }
    
	@Override
	public void updateBox(ItemProdx itm) {
		owner.setTarget(itm);
		tenur.setTarget(itm);
		inform.setTarget(itm);
		scriber.setTarget(itm);
		emitter.setTarget(itm);
	}	
}

package nthu.hpclp.client.product;

import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;

import nthu.hpclp.client.Main;
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
		anrchOwner.setWidget(owne);
		anrchTenur.setWidget(tenu);
		anchorInfo.setWidget(info);
	}

	@UiField(provided=true) 
	MaterialPanel root = _root;
	
    @UiField
    MaterialNavBar navApp, navSearch;
    @UiField
    MaterialSearch boxSearch;
	
	@UiField
	SimplePanel anrchOwner,anrchTenur,anchorInfo;
	
	@UiField
	SimplePanel anchorScriber,anchorEmitter;
	
	private PartOwne owne = new PartOwne();
	private PartTenu tenu = new PartTenu();
	private PartInfo info = new PartInfo();
	
    @UiField(provided=true)
    MaterialModal dlgList = _dlgList;
    @UiField(provided=true) 
    SimplePanel anchorList1 = _anchorList1;
    @UiField(provided=true) 
    SimplePanel anchorList2 = _anchorList2;

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
    	//renewSelector(query(boxSearch.getText().trim()));
    	boxSearch.setText("");
    	boxSearch.setFocus(true);
    }
    
    @UiHandler("lnkListShow")
    void onListShow(ClickEvent e){
    	dlgList.openModal();
    }
    @UiHandler("lnkListUpdate")
    void onListUpdate(ClickEvent e){
    	listUpdateLast50();
    }
    @UiHandler("lnkListClear")
    void onShowList(ClickEvent e){
    	//Main.dlgApprove.appear("有未儲存的項目，確認清除?", hook);
    	listClear();
    }
    @UiHandler("lnkListAddItem")
    void onListAddItem(ClickEvent e){
    }
    
	@Override
	public void updateBox(ItemProdx itm) {
		owne.setTarget(itm);
		tenu.setTarget(itm);
		info.setTarget(itm);
	}	
}

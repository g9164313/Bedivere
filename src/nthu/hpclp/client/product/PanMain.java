package nthu.hpclp.client.product;

import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;
import gwt.material.design.client.ui.MaterialToast;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ItemTenur;

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
		root.add(genTenuReport);		
		anchorOwner.setWidget(owner);
		anchorTenur.setWidget(tenur);
		anchorInfo.setWidget(info);
		anchorAppx.setWidget(appx);
		anchorScriber.setWidget(scriber);
		anchorEmitter.setWidget(info.emt);		
		addAltShortcut(
			KeyCodes.KEY_L,KeyCodes.KEY_N,KeyCodes.KEY_I,
			KeyCodes.KEY_S,KeyCodes.KEY_P,KeyCodes.KEY_T,
			KeyCodes.KEY_DELETE
		);
		addShortcut(
			KeyCodes.KEY_F1,
			KeyCodes.KEY_PAGEUP,KeyCodes.KEY_PAGEDOWN
		);
		chainBox(
			owner.boxOKey,
			tenur.boxTKey,
			info.boxStmp,info.boxPKey,
			info.boxTempu,info.boxPress,info.boxHumid,
			scriber.boxInput,
			null
		);
		owner.nextBox = tenur.boxTKey;
		tenur.nextBox = info.boxStmp;
	}

	@UiField(provided=true) 
	MaterialPanel root = _root;
    @UiField
    MaterialNavBar navAppBar,navSearch;
    @UiField
    MaterialSearch boxSearch;
	@UiField
	SimplePanel anchorOwner,anchorTenur,anchorInfo,anchorAppx;
	@UiField
	SimplePanel anchorScriber,anchorEmitter;
	
    @UiField(provided=true)
    MaterialModal dlgList = _dlgList;
    @UiField(provided=true) 
    SimplePanel anchorList1 = _anchorList1;
    @UiField(provided=true)
    SimplePanel anchorList2 = _anchorList2;

	@Override
	public void eventShortcut(Integer keycode,Integer appx){
		switch(keycode){
		case KeyCodes.KEY_L://show list			
			onListShow(null);
			break;
		case KeyCodes.KEY_I://insert the new one
		case KeyCodes.KEY_N://create the new one
			onListAddItem(null); 
			break;
		case KeyCodes.KEY_DELETE://delete the new one
			onListDelete(null);
			break;
		case KeyCodes.KEY_S://save all items
			onListUpload(null); 
			break;
		case KeyCodes.KEY_P://print items
			onPrintProdx(null);
			break;
		case KeyCodes.KEY_F1://print 2D-tag
		case KeyCodes.KEY_T: 
			onPrint2DTag(null); 
			break;
		case KeyCodes.KEY_PAGEUP:
			listNextItem(-1);
			break;
		case KeyCodes.KEY_PAGEDOWN:
			listNextItem(1);
			break;
		}
	}
	
	@Override
	public void onEventShow() {
		navAppBar.add(Main.funcPager);
		updateBox(null);
	}
	//-----------------------------//
	
    @UiHandler("lnkStartSearch")
    void onStartSearch(ClickEvent e) {
    	navAppBar.setVisible(false);
        navSearch.setVisible(true);  
        boxSearch.setFocus(true);
    }
    @UiHandler("boxSearch")
    void onCloseSearch(CloseEvent<String> event){
    	navAppBar.setVisible(true);
        navSearch.setVisible(false);
        //focus another widget???
    }
    @UiHandler("boxSearch")
    void onSearching(KeyDownEvent event) {
    	int code = event.getNativeKeyCode();
    	if(code!=KeyCodes.KEY_ENTER){
    		return;
    	}
    	listQuery(txt2sql(boxSearch.getText().trim()));
    	boxSearch.setText("");
    	boxSearch.setFocus(true);
    }
    //-----------------------------//

    @UiHandler("lnkListShow")
    void onListShow(ClickEvent e){
    	if(isOnList==true){
    		dlgList.closeModal();
    	}else{
    		isOnList = true;
    		lstProvd.refresh();
    		dlgList.openModal();    		
    	}
    }
    @UiHandler("lnkListLast")
    void onListShowLast(ClickEvent e){
    	listShowLast50();
    }
    @UiHandler("lnkListClear")
    void onListClear(ClickEvent e){
    	listClearItem();
    }
    @UiHandler("lnkListAddItem")
    void onListAddItem(ClickEvent e){
    	owner.boxOKey.setFocus(true);
    	listAddItem();
    }
    @UiHandler("lnkListUpload")
    void onListUpload(ClickEvent e){
    	listUploadItem();
    }
    @UiHandler("lnkListDelete")
    void onListDelete(ClickEvent e){
    	ListDeleteItem();
    }
    //-----------------------------//
    
    @UiHandler("lnkPrint2DTag")
    void onPrint2DTag(ClickEvent e){
    	ItemTenur itm = tenur.getTarget(false);
    	if(itm==null){
    		MaterialToast.fireToast("無測試儀器？！");
    		return;
    	}
    	print2DTag(itm,
    		appx.getDate(),
    		appx.getName(),
    		appx.getMemo()
    	);
    }
    @UiHandler("lnkPrintProdx")
    void onPrintProdx(ClickEvent e){
    	printProduct();
    }
    @UiHandler("lnkPrintReport")
    void onPrintReport(ClickEvent e){
    	genTenuReport.appear();
    }
    //-----------------------------//
    
	@Override
	public void updateBox(ItemProdx itm) {
		owner.setTarget(itm);
		tenur.setTarget(itm);
		info.setTarget(itm);
		scriber.setTarget(itm);
	}	
}

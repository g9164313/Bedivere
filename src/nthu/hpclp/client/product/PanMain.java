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
		root.add(genReport);
		//navAppBar.add(Main.funcPager);
		anchorOwner.setWidget(owner);
		anchorTenur.setWidget(tenur);
		anchorInfo.setWidget(inform);
		anchorAppx.setWidget(appx);
		anchorScriber.setWidget(scriber);
		anchorEmitter.setWidget(emitter);		
		addAltShortcut(KeyCodes.KEY_L);
		addAltShortcut(KeyCodes.KEY_N);		
		addAltShortcut(KeyCodes.KEY_S);		
		addAltShortcut(KeyCodes.KEY_P);
		addAltShortcut(KeyCodes.KEY_T);
		addShortcut(KeyCodes.KEY_F1);		
		chainBox(
			owner.boxOKey,
			tenur.boxTKey,
			inform.boxStmp,inform.boxPKey,
			inform.boxTempu,inform.boxPress,inform.boxHumid,
			scriber.boxInput,
			null
		);
		owner.nextBox = tenur.boxTKey;
		tenur.nextBox = inform.boxStmp;
	}

	@UiField(provided=true) 
	MaterialPanel root = _root;
    @UiField
    MaterialNavBar navAppBar, navSearch;
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

	private PartOwner owner = new PartOwner();
	private PartTenur tenur = new PartTenur();
	private PartInfo inform = new PartInfo();
	private PartAppx appx = new PartAppx();
	private PartScriber scriber = new PartScriber();
	private PartEmitter emitter = new PartEmitter();
	private DlgGenReport genReport = new DlgGenReport();

	@Override
	public void eventShortcut(Integer keycode,Integer appx){
		switch(keycode){
		case KeyCodes.KEY_L://show list			
			onListShow(null);
			break;
		case KeyCodes.KEY_N://create the new one
			onListAddItem(null); 
			break;
		case KeyCodes.KEY_S://save all items
			onListSave(null); 
			break;
		case KeyCodes.KEY_P://print items
			onPrintProdx(null);
			break;
		case KeyCodes.KEY_F1://print 2D-tag
		case KeyCodes.KEY_T: 
			onPrint2DTag(null); 
			break;
		}
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
    		dlgList.openModal();    		
    	}
    }
    @UiHandler("lnkListLast")
    void onListShowLast(ClickEvent e){
    	listShowLast50();
    }
    @UiHandler("lnkListClear")
    void onListClear(ClickEvent e){
    	//Main.dlgApprove.appear("有未儲存的項目，確認清除?", hook);
    	listClear();
    }
    @UiHandler("lnkListAddItem")
    void onListAddItem(ClickEvent e){
    	owner.boxOKey.setFocus(true);
    	listAddItem();
    }
    @UiHandler("lnkListSave")
    void onListSave(ClickEvent e){
    	listSaveItem();
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
    	
    }
    @UiHandler("lnkPrintReport")
    void onPrintReport(ClickEvent e){
    	genReport.appear();
    }
    //-----------------------------//
    
	@Override
	public void updateBox(ItemProdx itm) {
		owner.setTarget(itm);
		tenur.setTarget(itm);
		inform.setTarget(itm);
		scriber.setTarget(itm);
		emitter.setTarget(itm);
	}	
}

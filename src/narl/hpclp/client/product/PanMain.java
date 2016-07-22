package narl.hpclp.client.product;

import java.util.ArrayList;
import java.util.Date;

import narl.hpclp.client.Main;
import narl.hpclp.shared.Const;
import narl.hpclp.shared.ItemProdx;
import narl.hpclp.shared.ItemTenur;
import narl.hpclp.shared.ParmEmitter;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialCollection;
import gwt.material.design.client.ui.MaterialFloatBox;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

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
    MaterialLink lnkPanMeet,lnkPanAccnt;
    
    @UiField
    MaterialTextBox boxOKey,boxTKey,boxKey,boxStmp,boxMemo;

    @UiField
    MaterialFloatBox boxTemp,boxPress,boxHumid;
    
    @UiField
    MaterialListBox cmbFormat,cmbUnitRef,cmbUnitMea,cmbEmitter;
    
    @UiField
    MaterialCheckBox chkUseLogo;
    
    @UiField
    MaterialLabel txtInfo1,txtInfo2,
    	txtInfoT1,txtInfoT2,txtInfoT3,
    	txtInfoT4,txtInfoT5,txtInfoT6,
    	txtKindArea,txtStrength,txtSurface,
    	txtFactorK,txtFactorP,txtUncertain,
    	txtSerial,txtCriteron;
        
    @UiField
    MaterialModal dlgSelect;
    @UiField
    MaterialLabel txtSelectorTotal;
    @UiField
    MaterialCollection colSelector;    
    
    @UiField 
    SimplePanel panArch1;

    private GrdScribe grdScrib = new GrdScribe();
    
	public PanMain() {
		initWidget(uiBinder.createAndBindUi(this));
		initSearch();		
		root.add(Main.dlgOwner);
		root.add(Main.dlgTenur);
		addAttachHandler(eventShowHide);
		grdScrib.init(panArch1);
		
		onLnkCreateProdx(null);//create the first item!!!
	}

	private AttachEvent.Handler eventShowHide = new AttachEvent.Handler(){
		@Override
		public void onAttachOrDetach(AttachEvent event) {
			if(event.isAttached()==true){
				//At this time, web just prepare enviroment paramters
				Main.initCombo(cmbFormat, ItemProdx.USED_TXT_FMT);
				Main.initCombo(cmbUnitRef, Main.param.prodxUnit);
				Main.initCombo(cmbUnitMea, Main.param.prodxUnit);
				Main.initComboEmitter(cmbEmitter);
				
				emitt = new ParmEmitter(cmbEmitter.getSelectedValue());
				emitt2box();
			}
		}
	};
	
	@UiHandler("lnkPanMeet")
	void onLnkPanMeet(ClickEvent e){
		Main.switchToMeeting();
	}
	
	@UiHandler("lnkPanAccnt")
	void onLnkPanAccnt(ClickEvent e){
		Main.switchToAccount();
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
    //-------------------//
    
    /**
     * this list keep the user data in local computer~~~
     */
    public ArrayList<ItemProdx> lstProdx = new ArrayList<ItemProdx>();
    
    public static ItemProdx curProdx = null;
    
    public ParmEmitter emitt = null;

    @UiHandler("lnkSelect")
    void onLnkSelect(ClickEvent e){
    	dlgSelect.openModal();
    }
    
    @UiHandler("lnkRenew")
	void onLnkRenew(ClickEvent e){
    	//TODO: ask user whether dropping the current list~~~
    	Main.rpc.listProduct("ORDER BY "+Const.PRODX+".last DESC LIMIT 50",eventList);
	}
    private final AsyncCallback<ArrayList<ItemProdx>> eventList = 
    new AsyncCallback<ArrayList<ItemProdx>>(){
		@Override
		public void onFailure(Throwable caught) {
			MaterialToast.fireToast("內部錯誤!!");
		}
		@Override
		public void onSuccess(ArrayList<ItemProdx> result) {			
			if(result.isEmpty()==true){
				MaterialToast.fireToast("無資料!!");
				return;
			}
			lstProdx = result; 
			curProdx = result.get(0);
			prodx2box();
			refresh_selector();			
			dlgSelect.openModal();
		}
    };

    @UiHandler("lnkPrint1")
    void onLnkPrintProdx(ClickEvent e){
    	
    }
    
    @UiHandler("lnkPrint2")
    void onLnkPrint2DTag(ClickEvent e){
    	
    }
    
    @UiHandler("lnkUpload")
    void onLnkUploadProdx(ClickEvent e){
    	
    }
    
    @UiHandler("lnkCreate")
    void onLnkCreateProdx(ClickEvent e){
    	curProdx = new ItemProdx();
    	lstProdx.add(curProdx);
    	prodx2box();
    	refresh_selector();
    }
    
    @UiHandler("boxOKey")
    void onOwnerKey(ValueChangeEvent<String> event){
    	String txt = event.getValue().trim();
    	//Main.rpc.listOwner(postfix, res);
    }
    
    @UiHandler("boxTKey")
    void onTenurKey(ValueChangeEvent<String> event){
    	
    }

    @UiHandler("boxScribFastAdd")
    void onScribFastAdd(ValueChangeEvent<String> event){
    	
    }    
    
    @UiHandler("cmbFormat")
	void onCmbFormat(ValueChangeEvent<String> event){
    	int fmt = ItemProdx.txt2fmt(event.getValue());
    	switch(fmt){
 		case ItemProdx.FMT_F1V:
 		case ItemProdx.FMT_F1W:
 		case ItemProdx.FMT_F2:
 		case ItemProdx.FMT_F3:
 			chkUseLogo.setValue(true);
 			Main.selectCombo(cmbUnitRef,"μSv·h⁻¹");
 			Main.selectCombo(cmbUnitMea,"μSv·h⁻¹");
 			break;
 		case ItemProdx.FMT_F4:
 		case ItemProdx.FMT_F5:
 			chkUseLogo.setValue(false);
 			Main.selectCombo(cmbUnitRef,"cpm");
 			Main.selectCombo(cmbUnitMea,"cpm");
 			break;				
 		}
	}
    
    @UiHandler("cmbEmitter")
	void onCmbEmitter(ValueChangeEvent<String> event){
    	emitt2box();    	
	}
    
    private void emitt2box(){
    	emitt.setText(cmbEmitter.getSelectedValue());
    	txtKindArea.setText(emitt.getKind()+" - "+emitt.getArea());
    	txtStrength.setText(emitt.getStrength());
    	txtSurface.setText(emitt.getSurface());
    	txtFactorK.setText(emitt.getFactorK());
    	txtFactorP.setText(emitt.getFactorP());
    	txtUncertain.setText(emitt.getUncertain());
    	txtSerial.setText(emitt.getSerial());
    	txtCriteron.setText(emitt.getCriterion());
    }
    
    private void refresh_selector(){
    	int cnt = lstProdx.size();
    	colSelector.clear();    	
		txtSelectorTotal.setText("共"+cnt+"筆資料");		
		for(int idx=0; idx<cnt; idx++){
			colSelector.add(new CitProduct(
				PanMain.this,
				idx+1,
				lstProdx.get(idx)
			));
		}
    }
    private void clearBox(){
    	boxKey.setText("");
    	boxStmp.setText(Main.fmtDate.format(new Date()));
    	boxMemo.setText("");
    	boxTemp.setText("");
    	boxPress.setText("");
    	boxHumid.setText("");
    	//TODO: clear grid~~~~
    	//grdScrib.reload(null);
    }
    
    public void prodx2box(){
    	Main.selectCombo(cmbFormat, curProdx.getFormat());
    	Main.selectCombo(cmbUnitRef, curProdx.getUnitRef());
    	Main.selectCombo(cmbUnitMea, curProdx.getUnitMea());
    	Main.selectCombo(cmbEmitter, curProdx.getEmitterTxt(), null, true);
    	emitt2box();//map information again!!!
    	
    	boxKey.setText(curProdx.getKey());
    	boxStmp.setText(Main.fmtDate.format(curProdx.stmp));
    	boxMemo.setText(curProdx.getMemo());
    	boxTemp.setText(curProdx.getTemperature());
    	boxPress.setText(curProdx.getPressure());
    	boxHumid.setText(curProdx.getHumidity());
    	chkUseLogo.setValue(curProdx.useLogo);
    	
    	boxOKey.setText("");
    	if(curProdx.owner!=null){
    		boxOKey.setText(curProdx.owner.getKey());
    		txtInfo1.setText(curProdx.owner.getName());
    		txtInfo2.setText(curProdx.owner.getAddress());
    	}else{
    		txtInfo1.setText("");
    		txtInfo2.setText("");
    	}
    	
    	boxTKey.setText("");
    	if(curProdx.tenur!=null){
    		ItemTenur itm = curProdx.tenur;
    		txtInfoT1.setText(itm.getDeviceVendor());
    		txtInfoT2.setText(itm.getDeviceSerial());
    		txtInfoT3.setText(itm.getDeviceNumber());
    		txtInfoT4.setText(itm.getDetectType());
    		txtInfoT5.setText(itm.getDetectSerial());
    		txtInfoT6.setText(itm.getDetectNumber());
    	}else{
    		txtInfoT1.setText("");
    		txtInfoT2.setText("");
    	}
    	grdScrib.reload(curProdx.scribble);
    }
    
    public void box2prodx(){
    	curProdx.setFormat(cmbFormat.getSelectedValue());
    	curProdx.setUnitRef(cmbUnitRef.getSelectedValue());
    	curProdx.setUnitMea(cmbUnitMea.getSelectedValue());
    	curProdx.setEmitter(cmbEmitter.getSelectedValue());
    	curProdx.setKey(boxKey.getText());
    	curProdx.setMemo(boxMemo.getText());
    	curProdx.setAmbience(
    		boxTemp.getText(), 
    		boxPress.getText(),
    		boxHumid.getText()
    	);
    	curProdx.useLogo = chkUseLogo.getValue();
    	grdScrib.refresh(curProdx.scribble);
    }
}

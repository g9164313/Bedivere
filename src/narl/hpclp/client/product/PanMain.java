package narl.hpclp.client.product;

import java.util.ArrayList;
import java.util.Date;

import narl.hpclp.client.Main;
import narl.hpclp.shared.Const;
import narl.hpclp.shared.ItemMeeting;
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
    MaterialLabel txtTotal;
    
    @UiField
    MaterialModal dlgSelect;
    
    @UiField
    MaterialCollection colSelect;    
    
	public PanMain() {
		initWidget(uiBinder.createAndBindUi(this));
		initSearch();		
		root.add(Main.dlgOwner);
		root.add(Main.dlgTenur);
		addAttachHandler(eventShowHide);		
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
    
    private ArrayList<ItemProdx> lstProdx = new ArrayList<ItemProdx>();
    
    public ItemProdx curProdx = null;
    
    private ParmEmitter emitt = null;

    @UiHandler("lnkSelect")
    void onLnkSelect(ClickEvent e){
    	dlgSelect.openModal();
    }
    
    @UiHandler("lnkRenew")
	void onLnkRenew(ClickEvent e){
    	//TODO: how to keep old data???
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
			lstProdx = result;//override~~~
			curProdx = null;//reset target~~~
			clearBox();//reset all box~~~
			colSelect.clear();
			if(lstProdx.isEmpty()==true){
				MaterialToast.fireToast("無資料!!");
				return;
			}
			curProdx = lstProdx.get(0);
			prodx2box();
			int cnt = lstProdx.size();
			txtTotal.setText("共"+cnt+"筆資料");
			for(int idx=0; idx<cnt; idx++){
				colSelect.add(new CitProduct(
					PanMain.this,
					idx+1,
					lstProdx.get(idx)
				));
			}
			dlgSelect.openModal();
		}
    };

    @UiHandler("lnkPrint")
    void onLnkPrintProdx(ClickEvent e){
    	
    }
    
    @UiHandler("lnkUpload")
    void onLnkUploadProdx(ClickEvent e){
    	
    }
    
    @UiHandler("lnkCreate")
    void onLnkCreateProdx(ClickEvent e){
    	
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
    
    private void clearBox(){
    	boxKey.setText("");
    	boxStmp.setText(Main.fmtDate.format(new Date()));
    	boxMemo.setText("");
    	boxTemp.setText("");
    	boxPress.setText("");
    	boxHumid.setText("");
    }
    
    public void prodx2box(){
    	Main.selectCombo(cmbFormat, curProdx.getFormat());
    	Main.selectCombo(cmbUnitRef , curProdx.getUnitRef());
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
    }
}

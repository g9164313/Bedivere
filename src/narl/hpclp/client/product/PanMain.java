package narl.hpclp.client.product;

import java.util.ArrayList;

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
import com.google.gwt.event.dom.client.ClickHandler;
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
    MaterialTextBox boxOKey,boxTKey,boxKey,boxStmp,boxMemo,boxScribe;

    @UiField
    MaterialFloatBox boxTemp,boxPress,boxHumid;
    
    @UiField
    MaterialListBox cmbFormat,cmbUnitRef,cmbUnitMea,cmbEmitter;
    
    @UiField
    MaterialCheckBox chkUseLogo;
    
    @UiField
    MaterialLabel 
    	txtInfo1,txtInfo2,
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
		root.add(Main.dlgEditOwner);
		root.add(Main.dlgEditTenur);
		root.add(Main.dlgPickOwner);
		root.add(Main.dlgPickTenur);
		grdScrib.init(panArch1);
		addAttachHandler(eventShowHide);		
		//Don't initialize data here!!!
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
				onLnkCreateProdx(null);//create the first item!!!
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

    @UiHandler("lnkShowSelector")
    void onLnkShowSelector(ClickEvent e){
    	//TODO: refresh title again!!!
    	dlgSelect.openModal();
    }
    
    @UiHandler("lnkRenew")
	void onLnkRenew(ClickEvent e){
    	//TODO: ask user whether dropping the current list~~~
    	Main.rpc.listProduct(
    		"ORDER BY "+Const.PRODX+".last DESC LIMIT 50",
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
    	});
	}

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
    	//Remember to prepare emitter information, This is required
    	curProdx.setEmitter(cmbEmitter.getSelectedValue());
    	lstProdx.add(curProdx);
    	prodx2box();
    	refresh_selector();
    }
    
    @UiHandler("boxOKey")
    void onOwnerKey(ValueChangeEvent<String> event){
    	String txt = event.getValue().trim();
    	String post = "WHERE ";
    	post = post + "(info[1] SIMILAR TO '%"+txt+"%') OR ";
    	post = post + "(info[2] SIMILAR TO '%"+txt+"%') OR ";
    	post = post + "(info[4] SIMILAR TO '%"+txt+"%') OR ";
    	post = post + "(info[6] SIMILAR TO '%"+txt+"%') ORDER BY last DESC ";
    	//Query feature???
    	Main.dlgPickOwner.appear(
    		post,new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				curProdx.owner = Main.dlgPickOwner.getTarget();
				if(curProdx.owner==null){
					return;
				}
				prodx2box();
				boxOKey.setText(curProdx.owner.getKey());
			}
    	});
    }
    
    @UiHandler("boxTKey")
    void onTenurKey(ValueChangeEvent<String> event){
    	String txt = event.getValue().trim().toLowerCase();
    	String post = "WHERE ";
    	post = post + "(tenure.info[1] SIMILAR TO '%"+txt+"%') OR ";
    	post = post + "(tenure.info[2] SIMILAR TO '%"+txt+"%') OR ";
    	post = post + "(tenure.info[3] SIMILAR TO '%"+txt+"%') OR ";
    	post = post + "(tenure.info[4] SIMILAR TO '%"+txt+"%') OR ";
    	post = post + "(tenure.info[6] SIMILAR TO '%"+txt+"%') OR ";
    	post = post + "(tenure.info[7] SIMILAR TO '%"+txt+"%') OR ";
    	post = post + "(tenure.info[11] SIMILAR TO '%"+txt+"%') ORDER BY last DESC ";
    	//Query feature???
    	Main.dlgPickTenur.appear(
        	post,new ClickHandler(){
    		@Override
    		public void onClick(ClickEvent event) {
    			curProdx.tenur = Main.dlgPickTenur.getTarget();
    			if(curProdx.tenur==null){
    				return;
    			}
    			prodx2box();
    			boxTKey.setText("");//just clear it~~~
    			cmbFormat.setFocus(true);
    		}
        });
    }

    @UiHandler("cmbFormat")
	void onChangeFormat(ValueChangeEvent<String> event){
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
    @UiHandler("cmbUnitRef")
    void onChangeUnitRef(ValueChangeEvent<String> event){
    	curProdx.setUnitRef(cmbUnitRef.getSelectedValue());    	
    }
    @UiHandler("cmbUnitMea")
    void onChangeUnitMea(ValueChangeEvent<String> event){    	
    	curProdx.setUnitMea(cmbUnitMea.getSelectedValue());    	
    }

    @UiHandler("boxKey")
    void onChangeKey(ChangeEvent event){
    	curProdx.setKey(boxKey.getText());
    	boxMemo.setFocus(true);
    }
    
    @UiHandler("boxStmp")
    void onChangeStamp(ChangeEvent event){
    	String txt = boxStmp.getText();
    	try{
    		curProdx.setStmp(Main.fmtDate.parse(txt));
    	}catch(IllegalArgumentException e){
    		boxStmp.setText(txt);
    	}
    }
    
    @UiHandler("boxMemo")
    void onChangeMemo(ChangeEvent event){
    	curProdx.setMemo(boxMemo.getText());
    }

    @UiHandler("boxTemp")
    void onChangeTemp(ChangeEvent event){
    	curProdx.setAmbience(boxTemp.getText(),null,null);
    	boxPress.setFocus(true);
    }
    
    @UiHandler("boxPress")
    void onChangePress(ChangeEvent event){
    	curProdx.setAmbience(null, boxPress.getText(),null);
    	boxHumid.setFocus(true);
    }
    
    @UiHandler("boxHumid")
    void onChangeHumid(ChangeEvent event){
    	curProdx.setAmbience(null,null,boxHumid.getText());
    	cmbEmitter.setFocus(true);
    }

    @UiHandler("chkUseLogo")
    void onCheckLogo(ClickEvent e){
    	curProdx.useLogo = chkUseLogo.getValue();
    }
    
    @UiHandler("cmbEmitter")
	void onChangeEmitter(ValueChangeEvent<String> event){
    	emitt2box();
    	boxScribe.setFocus(true);
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
    
    @UiHandler("boxScribe")
    void onChangeScribe(ValueChangeEvent<String> event){    	
    	//replace space and translate format
    	String vals = event.getValue()
    		.trim()
    		.replaceAll("\\s","")
    		.replace('/', '@')
    		.replace(';', '@')
    		.replace('+', ',');
    	if(vals.contains(Const.SPECIAL_PREFIX)==true){
    		//It is the special keyword!!!
    		vals=Const.SPECIAL_PREFIX +"@0@0";
    	}else{
    		String[] val = vals.split("@");
    		if(val.length<=2){
    			//we mist have 3 items
    			MaterialToast.fireToast("格式錯誤");
    			return;
    		}
    		String[] vvv = val[2].split(",");
    		for(int i=0; i<vvv.length; i++){
    			//check format is valid~~~~~
    			try{
    				Double.valueOf(vvv[i]);
    			}catch(NumberFormatException e){
    				MaterialToast.fireToast("格式錯誤 -"+vvv[i]);
    				return;
    			}
    		}
    		val[0] = val[0].replace('x', '×').replace('X', '×');//trick to fix typo!!!
    		vals = val[0];
    		for(int i=1; i<val.length; i++){
    			vals = vals +"@" + val[i];
    		}
    	}
    	curProdx.scribble.add(vals);
    	boxScribe.clear();    	
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
}

package nthu.hpclp.client.product;

import java.util.ArrayList;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialCollection;
import gwt.material.design.client.ui.MaterialFloatBox;
import gwt.material.design.client.ui.MaterialLabel;

import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ItemTenur;
import nthu.hpclp.shared.ParmEmitter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class PanMain extends PanBase {

	private static PanMainUiBinder uiBinder = GWT.create(PanMainUiBinder.class);

	interface PanMainUiBinder extends UiBinder<Widget, PanMain> {
	}
    
	public PanMain() {
		initWidget(uiBinder.createAndBindUi(this));
		root.add(Main.dlgApprove);
		btnScribeEdit.addClickHandler(CitScribe.eventEdit);
		btnScribeCancel.addClickHandler(CitScribe.eventCancel);
		btnScribeDelete.addClickHandler(CitScribe.eventDelete);
		//Don't initialize data here!!!
	}

	@UiField
	MaterialPanel root;
	
    @UiField
    MaterialNavBar navApp, navSearch;
    @UiField
    MaterialSearch boxSearch;
	
    @UiField
    MaterialTextBox boxPKey,boxStmp,boxMemo,boxScribe;
    @UiField
    MaterialFloatBox boxTemp,boxPress,boxHumid;
    @UiField
    MaterialListBox cmbFormat,cmbUnitRef,cmbUnitMea,cmbEmitter;
    @UiField
    MaterialCheckBox chkUseLogo;

    @UiField
    MaterialModal dlgSelector;
    @UiField
    MaterialLabel txtSelectorTotal;
    @UiField
    MaterialCollection colSelector;    
    
    @UiField
    MaterialLabel txtScribe2,txtScribe5;
    @UiField
    MaterialCollection colScribe;
    @UiField
    MaterialModal dlgScribe;
    @UiField
    MaterialTextBox boxScribe0,boxScribe1,boxScribe2;
    @UiField
    MaterialButton btnScribeEdit,btnScribeCancel,btnScribeDelete;

    @UiHandler("lnkSearch")
    void onStartSearch(ClickEvent e) {
        navApp.setVisible(false);
        navSearch.setVisible(true);  
        boxSearch.setFocus(true);
    }
    @UiHandler("boxSearch")
    void onCloseSearch(CloseEvent<String> event){
    	navApp.setVisible(true);
        navSearch.setVisible(false);
    }
    @UiHandler("boxSearch")
    void onSearching(KeyDownEvent event) {
    	int code = event.getNativeKeyCode();
    	if(code!=KeyCodes.KEY_ENTER){
    		return;
    	}
    	renewSelector(query(boxSearch.getText().trim()));
    	boxSearch.setText("");
    	boxSearch.setFocus(true);
    }
    //-------------------//

    @UiHandler("lnkShowSelector")
    void onShowSelector(ClickEvent e){
    	refresh_selector();
    	dlgSelector.openModal();
    }
    
    @UiHandler("lnkRenewSelector")
	void onLnkRenewSelector(ClickEvent e){
    	//TODO: how to remind user that we dropping the current list~~~
    	renewSelector("ORDER BY "+Const.PRODX+".last DESC LIMIT 50");
	}
    
    private void renewSelector(final String postfix){
    	MaterialLoader.showLoading(true);
    	Main.rpc.listProduct(
        	postfix,
        	new AsyncCallback<ArrayList<ItemProdx>>(){
        	@Override
        	public void onFailure(Throwable caught) {
        		MaterialToast.fireToast("內部錯誤!!");
        	}
        	@Override
        	public void onSuccess(ArrayList<ItemProdx> result) {
        		MaterialLoader.showLoading(false);
        		if(result.isEmpty()==true){
        			MaterialToast.fireToast("無資料!!");
        			return;
        		}
        		lstProdx = result; 
        		refresh_selector();	
        		curProdx = result.get(0);
        		prodx2box();
        		if(result.size()>1){
        			dlgSelector.openModal();
        		}        		
        	}
        });
    }
    
    @UiHandler("lnkClearSelector")
	void onClearSelector(ClickEvent e){
    	lstProdx.clear();    	
    	MaterialToast.fireToast("已清除...",100);
    	onCreateProdx(e);
    }
    
    @UiHandler("lnkCreate")
    void onCreateProdx(ClickEvent e){
    	curProdx = new ItemProdx();
    	//Remember to prepare emitter information, This is required!!!
    	curProdx.setEmitter(cmbEmitter.getSelectedValue());
    	lstProdx.add(curProdx);
    	prodx2box();
    	MaterialToast.fireToast("新增報告",100);
    }
   
    @UiHandler("lnkPrint2DTag")
    void onPrint2DTag(ClickEvent e){
    	//TODO:How to print 2D-tag???
    }
    
    @UiHandler("lnkPrintOneProdx")
    void onPrintOneProdx(ClickEvent e){
    	Main.printProduct(curProdx);
    }

    @UiHandler("lnkPrintAllProdx")
    void onPrintAllProdx(ClickEvent e){
    	Main.printProduct(lstProdx);
    }
    
    @UiHandler("lnkUpload")
    void onUploadProdx(ClickEvent e){
    	if(lstProdx.isEmpty()==true){
    		MaterialToast.fireToast("清單內無報告");
    		return;
    	}
    	MaterialLoader.showLoading(true);
    	Main.rpc.cacheProduct(lstProdx,new AsyncCallback<ArrayList<ItemProdx>>(){
			@Override
			public void onFailure(Throwable caught) {
				MaterialLoader.showLoading(false);
				MaterialToast.fireToast("內部錯誤");
			}
			@Override
			public void onSuccess(ArrayList<ItemProdx> result) {
				MaterialLoader.showLoading(false);
				MaterialToast.fireToast("更新清單內容");
				lstProdx = result;
			}
    	});    	
    }
 
    @UiHandler("lnkDeleteProdx")
    void onDeleteProdx(ClickEvent e){
    	if(curProdx.uuid.length()==0){
    		return;
    	}
    	final ClickHandler event = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				curProdx.death = true;//important!!!!
		    	Main.rpc.modifyProdx(curProdx, new AsyncCallback<ItemProdx>(){
					@Override
					public void onFailure(Throwable caught) {
					}
					@Override
					public void onSuccess(ItemProdx result) {
						MaterialToast.fireToast("已刪除資料");
						lstProdx.remove(curProdx);
						refresh_selector();	
						curProdx = lstProdx.get(0);
						prodx2box(); 
					}
		    	});
			}
    	};
    	Main.dlgApprove.appear("確認刪除??",event);    	
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
 			//TODO: how to select emitter???
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

    @UiHandler("boxStmp")
    void onChangeStamp(ChangeEvent event){
    	String txt = boxStmp.getText().trim();
    	try{
    		curProdx.setStmp(Main.fmtDate.parse(txt));
    		boxPKey.setFocus(true);
    	}catch(IllegalArgumentException e){
    		boxStmp.setText(txt);//restore the original value~~~~
    	}
    }
    
    @UiHandler("boxPKey")
    void onChangeKeyProduct(ChangeEvent event){
    	String txt = boxPKey.getText().trim();
    	if(txt.length()==0 || txt.equalsIgnoreCase("+")==true){
    		if(curProdx.owner==null){
    			MaterialToast.fireToast("無委託廠商");
    			return;
    		}
    		String arg1 = Main.date2tw_y(curProdx.stmp);
    		String arg2 = curProdx.owner.getKey();
    		Main.rpc.genKey(
    			Const.PRODX+"@"+arg1+","+arg2,
    			new AsyncCallback<String>(){
    				@Override
    				public void onFailure(Throwable caught) {
    					MaterialToast.fireToast(caught.getMessage());
    				}
    				@Override
    				public void onSuccess(String result) {					
    					curProdx.setKey(result);
    					boxPKey.setText(result);
    				}
    		});    		
    	}else{
    		curProdx.setKey(txt);
    		boxPKey.setText(txt);
    	}
    	boxMemo.setFocus(true);
    }
    
    @UiHandler("boxMemo")
    void onChangeMemo(ChangeEvent event){
    	curProdx.setMemo(boxMemo.getText().trim());
    	boxTemp.setFocus(true);
    }

    @UiHandler("boxTemp")
    void onChangeTemp(ChangeEvent event){
    	curProdx.setAmbience(boxTemp.getText().trim(),null,null);
    	boxPress.setFocus(true);
    }
    
    @UiHandler("boxPress")
    void onChangePress(ChangeEvent event){
    	curProdx.setAmbience(null, boxPress.getText().trim(),null);
    	boxHumid.setFocus(true);
    }
    
    @UiHandler("boxHumid")
    void onChangeHumid(ChangeEvent event){
    	curProdx.setAmbience(null,null,boxHumid.getText().trim());
    	boxScribe.setFocus(true);
    }

    @UiHandler("chkUseLogo")
    void onCheckLogo(ClickEvent e){
    	curProdx.useLogo = chkUseLogo.getValue();
    }
    
    @UiHandler("cmbEmitter")
	void onChangeEmitter(ValueChangeEvent<String> event){
    	//emitt2box();
    	boxScribe.setFocus(true);
	}
    /*private void emitt2box(){
    	emitt.setText(cmbEmitter.getSelectedValue());
    	txtKindArea.setText(emitt.getKind()+" - "+emitt.getArea());
    	txtStrength.setText(emitt.getStrength());
    	txtSurface.setText(emitt.getSurface());
    	txtFactorK.setText(emitt.getFactorK());
    	txtFactorP.setText(emitt.getFactorP());
    	txtUncertain.setText(emitt.getUncertain());
    	txtSerial.setText(emitt.getSerial());
    	txtCriteron.setText(emitt.getCriterion());
    }*/
    
    @UiHandler("boxScribe")
    void onChangeScribe(ValueChangeEvent<String> event){    	
    	//replace space and translate format
    	String val = event.getValue().trim();
    	if(val.length()==0){
    		return;
    	}
    	if(val.charAt(0)=='*'){    		
    		val = "×"+val.substring(1);//star symbol can presents scale one
    	}
    	String abbrev = val.replaceAll("\\s","")
    		.replace('/', '@')
    		.replace(';', '@')
    		.replace('+', ',')
    		.replace('x', '×')
    		.replace('X', '×');//trick to fix all typo!!!
    	
    	if(abbrev.contains(Const.SPECIAL_PREFIX)==true){
    		//It is the special keyword!!!
    		abbrev=Const.SPECIAL_PREFIX +"@0@0";
    	}else{
    		String[] arg = abbrev.split("@");
    		if(arg.length<=2){
    			//we should have 3 items
    			MaterialToast.fireToast("格式錯誤");
    			return;
    		}
    		String[] vvv = arg[2].split(",");
    		for(int i=0; i<vvv.length; i++){
    			//check format is valid~~~~~
    			try{
    				Double.valueOf(vvv[i]);
    			}catch(NumberFormatException e){
    				MaterialToast.fireToast("格式錯誤 -"+vvv[i]);
    				return;
    			}
    		}
    		//combine all arguments~~~~
    		abbrev = arg[0];
    		for(int i=1; i<arg.length; i++){
    			abbrev = abbrev +"@" + arg[i];
    		}
    	}    	
    	curProdx.scribble.add(abbrev);//add item as soon as possible
    	colScribe.add(new CitScribe(curProdx));
    	boxScribe.setText("");//for next turn~~~~
    }    

    public void prodx2box(ItemProdx itm){
    	curProdx = itm;
    	prodx2box();    	
    }
    public void prodx2box(){
    	Main.selectCombo(cmbFormat, curProdx.getFormat());
    	Main.selectCombo(cmbUnitRef, curProdx.getUnitRef());
    	Main.selectCombo(cmbUnitMea, curProdx.getUnitMea());
    	Main.selectCombo(cmbEmitter, curProdx.getEmitterTxt(), null, true);
    	//emitt2box();//map information again!!!
    	
    	boxPKey.setText(curProdx.getKey());
    	boxStmp.setText(Main.fmtDate.format(curProdx.stmp));
    	boxMemo.setText(curProdx.getMemo());
    	boxTemp.setText(curProdx.getTemperature());
    	boxPress.setText(curProdx.getPressure());
    	boxHumid.setText(curProdx.getHumidity());
    	chkUseLogo.setValue(curProdx.useLogo);

    	//refresh_scribe();
    }
    
    /*public static void refresh_scribe(){
    	colScribe.clear();
    	ArrayList<String> lst = curProdx.scribble;
		for(int idx=0; idx<lst.size(); idx++){
			colScribe.add(new CitScribe(curProdx,idx));
		}
		switch(curProdx.format){
		case ItemProdx.FMT_F1V:
		case ItemProdx.FMT_F1W:
		case ItemProdx.FMT_F2:
			txtScribe2.setText("參考值");
			txtScribe5.setText("因子");
			break;
		case ItemProdx.FMT_F3:
			txtScribe2.setText("背景值");
			txtScribe5.setText("反應");
			break;
		case ItemProdx.FMT_F5:
		case ItemProdx.FMT_F4:
			txtScribe2.setText("參考值");
			txtScribe5.setText("效率");
			break;
		}
    }*/
    
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
}

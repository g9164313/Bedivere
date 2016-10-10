package nthu.hpclp.client.product;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialFloatBox;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import narl.itrc.client.ExComposite;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemParam;
import nthu.hpclp.shared.ItemProdx;


public class PartInfo extends ExComposite {

	private static PartInfoUiBinder uiBinder = GWT.create(PartInfoUiBinder.class);

	interface PartInfoUiBinder extends UiBinder<Widget, PartInfo> {
	}

	public PartInfo() {
		initWidget(uiBinder.createAndBindUi(this));
		//format is fixed~~~
		cmbFormatP.clear();
		cmbFormatP.addItem(ItemProdx.TXT_FMT_F2);
		cmbFormatP.addItem(ItemProdx.TXT_FMT_F3);
		cmbFormatP.addItem(ItemProdx.TXT_FMT_F4);
		cmbFormatP.addItem(ItemProdx.TXT_FMT_F5);
		cmbFormatP.addItem(ItemProdx.TXT_FMT_F1V);
		cmbFormatP.addItem(ItemProdx.TXT_FMT_F1W);
	}

	
    @UiField
    MaterialTextBox boxPKey,boxStmp,boxMemo;
    @UiField
    MaterialFloatBox boxTempu,boxPress,boxHumid;
    @UiField
	MaterialListBox cmbFormatP,cmbUnitRef,cmbUnitMea;
    
    @UiField
    MaterialCheckBox chkUseLogo;

	@Override
	public void onEventShow() {
		//unit may be added or deleted~~~
		cmbUnitRef.clear();
		cmbUnitMea.clear();
		for(ItemParam parm:Main.param.prodxRadUnit){
			String unit = parm.getVal();
			cmbUnitRef.addItem(unit);
			cmbUnitMea.addItem(unit);
		}
	}
	@Override
	public void onEventHide() {
	}
	
	private ItemProdx target;
	
	public void setTarget(ItemProdx obj){
		target = obj;
		if(target!=null){
			boxPKey.setText(target.getKey());
			boxStmp.setText(Main.fmtDate.format(target.stmp));
			boxMemo.setText(target.getMemo());
			boxTempu.setText(target.getTemperature());
			boxPress.setText(target.getPressure());
			boxHumid.setText(target.getHumidity());
			cmbFormatP.setEnabled(true);
			cmbUnitRef.setEnabled(true);
			cmbUnitMea.setEnabled(true);
			chkUseLogo.setEnabled(true);
			cmbSelect(cmbFormatP,target.getFormat());
			cmbSelect(cmbUnitRef,target.getUnitRef());
			cmbSelect(cmbUnitMea,target.getUnitMea());
			chkUseLogo.setValue(target.useLogo);
		}else{
			boxPKey.setText("");
			boxStmp.setText("");
			boxMemo.setText("");
			boxTempu.setText("");
			boxPress.setText("");
			boxHumid.setText("");
			cmbFormatP.setEnabled(false);
			cmbUnitRef.setEnabled(false);
			cmbUnitMea.setEnabled(false);
			chkUseLogo.setEnabled(false);
		}
	}
	
    @UiHandler("boxPKey")
    void onChangeKeyProduct(ChangeEvent event){
    	if(target==null){
    		MaterialToast.fireToast("請選取或建立目標");
    		return;
    	}
    	String txt = boxPKey.getText().trim();
    	if(
    		txt.length()==0 || 
    		txt.equalsIgnoreCase("+")==true
    	){
    		if(target.owner==null){
    			MaterialToast.fireToast("無委託廠商");
    			return;
    		}
    		
    		String arg1 = Main.date2tw_y(target.stmp);
    		String arg2 = target.owner.getKey();
    		final AsyncCallback<String> event_gen_key = 
    			new AsyncCallback<String>()
    		{	
				@Override
				public void onFailure(Throwable caught) {
					MaterialToast.fireToast(caught.getMessage());
				}
				@Override
				public void onSuccess(String result) {					
					target.setKey(result);
					boxPKey.setText(result);
				}
    		};
    		Main.rpc.genKey(
    			Const.PRODX+"@"+arg1+","+arg2,
    			event_gen_key
    		);
    	}else{
    		target.setKey(txt);
    	}
    }	
	
	@UiHandler("boxStmp")
    void onChangeStamp(ChangeEvent event){
    	String txt = boxStmp.getText().trim();
    	try{
    		if(target!=null){
    			target.setStmp(Main.fmtDate.parse(txt));
    		}
    	}catch(IllegalArgumentException e){
    		boxStmp.setText(txt);//restore the original value~~~~
    	}
	}
	
    @UiHandler("boxMemo")
    void onChangeMemo(ChangeEvent event){
    	if(target!=null){
    		target.setMemo(boxMemo.getText().trim());
    	}
    }
	
    @UiHandler("boxTempu")
    void onChangeTemp(ChangeEvent event){
    	if(target!=null){
    		target.setAmbience(boxTempu.getText(),null,null);
    	}
    }
    
    @UiHandler("boxPress")
    void onChangePress(ChangeEvent event){
    	if(target!=null){
    		target.setAmbience(null,boxPress.getText(),null);
    	}
    }
    
    @UiHandler("boxHumid")
    void onChangeHumid(ChangeEvent event){
    	if(target!=null){
    		target.setAmbience(null,null,boxHumid.getText());
    	}
    }    
    
	@UiHandler("cmbFormatP")
	void onChangeFormat(ValueChangeEvent<String> event){
    	int fmt = ItemProdx.txt2fmt(cmbFormatP.getValue());
    	String defVal;
    	switch(fmt){
    	default:
 		case ItemProdx.FMT_F1V:
 		case ItemProdx.FMT_F1W:
 		case ItemProdx.FMT_F2:
 		case ItemProdx.FMT_F3:
 			defVal = "μSv·h⁻¹";
 			chkUseLogo.setValue(true);
 			break;
 		case ItemProdx.FMT_F4:
 		case ItemProdx.FMT_F5:
 			defVal = "cpm";
 			chkUseLogo.setValue(false);
 			break;				
 		}
		cmbSelect(cmbUnitRef,defVal);
		cmbSelect(cmbUnitMea,defVal);
		if(target!=null){
			target.setFormat(fmt);
		}
    }
	
    @UiHandler("cmbUnitRef")
    void onChangeUnitRef(ValueChangeEvent<String> event){
    	if(target!=null){
    		target.setUnitRef(cmbUnitRef.getSelectedValue());
    	}
    }
    @UiHandler("cmbUnitMea")
    void onChangeUnitMea(ValueChangeEvent<String> event){
    	if(target!=null){
    		target.setUnitMea(cmbUnitMea.getSelectedValue());
    	}
    }
	
    @UiHandler("chkUseLogo")
    void onCheckLogo(ClickEvent e){
    	if(target!=null){
    		target.useLogo = chkUseLogo.getValue();
    	}
    }
}

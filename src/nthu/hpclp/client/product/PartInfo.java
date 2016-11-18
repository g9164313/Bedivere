package nthu.hpclp.client.product;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.html.Option;
import narl.itrc.client.ExComposite;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemParam;
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ParmEmitter;

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
	
	public PartEmitter emt = new PartEmitter();
	
    @UiField
    public MaterialTextBox boxPKey,boxStmp,boxMemo;
    @UiField
    public MaterialTextBox boxTempu,boxPress,boxHumid;
    @UiField
	public MaterialListBox cmbFormatP,cmbUnitRef,cmbUnitMea;
    
    @UiField
    public MaterialCheckBox chkUseLogo;
    
    @UiField
    public MaterialListBox cmbEmitter;

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
			ParmEmitter emt = target.getEmitter();
			cmbSelect(cmbEmitter,
				emt.getTitle(),
				emt.toString()
			);
			chkUseLogo.setValue(target.useLogo);
		}else{
			boxPKey.setText("");
			boxStmp.setText("");
			boxMemo.setText("");
			boxTempu.setText("");
			boxPress.setText("");
			boxHumid.setText("");
			//cmbFormatP.setEnabled(false);
			//cmbUnitRef.setEnabled(false);
			//cmbUnitMea.setEnabled(false);			
			//cmbEmitter.setEnabled(false);
			//chkUseLogo.setEnabled(false);	
		}
	}
	
	private void init_cmb_unit(){
		cmbUnitRef.clear();
		cmbUnitMea.clear();
		for(ItemParam parm:Main.param.prodxRadUnit){
			String unit = parm.getVal();
			cmbUnitRef.addItem(unit);
			cmbUnitMea.addItem(unit);
		}		
	}
	
	public static String DEFAT_GAMMA_EMITTER = ParmEmitter.DEFAULT_GAMMA_VALUE;
	
	private void init_cmb_emitter(){
		cmbEmitter.clear();		
		//銫-gamma is 'special'
		final String SPECIAL_KIND = "-gamma-";
		int year1=105, year2=104;
		for(ItemParam parm:Main.param.prodxEmitter){
			String val = parm.getVal();
			ParmEmitter e = new ParmEmitter(val);
			String name = e.getTitle();
			
			if(name.contains(SPECIAL_KIND)==false){
				continue;
			}
			//check tail is number!!!
			try{
				int pos = name.indexOf(SPECIAL_KIND);				
				int yr = Integer.valueOf(name.substring(pos+SPECIAL_KIND.length()));
				if(yr>year1){
					year1 = yr;
					year2 = year1 - 1;
				}
			}catch(NumberFormatException err){
				continue;
			}
		}
		int cmbIndex = 0;
		for(ItemParam parm:Main.param.prodxEmitter){
			String txt = parm.getVal();
			ParmEmitter emt = new ParmEmitter(txt);
			String name = emt.getTitle();
			Option opt = new Option();
			opt.setText(name);
			opt.setTitle(name);
			opt.setValue(txt);
			if(name.endsWith("-"+year1)==true){
				DEFAT_GAMMA_EMITTER = txt;
				cmbEmitter.addItem(name,txt);
				cmbEmitter.setItemText(cmbIndex++,name);//workaround~~~~
			}else if(name.endsWith("-"+year2)==true){
				cmbEmitter.addItem(name,txt);
				cmbEmitter.setItemText(cmbIndex++,name);//workaround~~~~
			}else if(name.contains(SPECIAL_KIND)==false){
				cmbEmitter.addItem(name,txt);
				cmbEmitter.setItemText(cmbIndex++,name);//workaround~~~~
			}
		}
		cmbEmitter.addValueChangeHandler(new ValueChangeHandler<String>(){
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				int idx = cmbEmitter.getSelectedIndex();
				String val = cmbEmitter.getValue(idx);
				if(target!=null){
					target.setEmitter(val);
				}
				emt.setTarget(val);
			}
		});
	}	
	//------------------------------------//
    
	@Override
	public void onEventShow() {
		init_cmb_unit();
		init_cmb_emitter();
		emt.setTarget(cmbEmitter.getSelectedValue());
	}
	
	@Override
	public void onEventHide() {
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
    		ItemOwner itm = target.getOwner();
    		if(itm==null){
    			MaterialToast.fireToast("無委託廠商");
    			return;
    		}
    		
    		String arg1 = Main.date2tw_y(target.stmp);
    		String arg2 = itm.getKey();
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
    	String defUnit;
    	switch(fmt){
    	default:
 		case ItemProdx.FMT_F1V:
 		case ItemProdx.FMT_F1W:
 		case ItemProdx.FMT_F2:
 		case ItemProdx.FMT_F3:
 			defUnit = "μSv·h⁻¹";
 			chkUseLogo.setValue(true);
 			break;
 		case ItemProdx.FMT_F4:
 		case ItemProdx.FMT_F5:
 			defUnit = "cpm";
 			chkUseLogo.setValue(false);
 			break;				
 		}
		cmbSelect(cmbUnitRef,defUnit);
		cmbSelect(cmbUnitMea,defUnit);
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

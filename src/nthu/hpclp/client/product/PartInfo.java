package nthu.hpclp.client.product;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialFloatBox;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialTextBox;
import narl.itrc.client.ExComposite;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.ItemProdx;


public class PartInfo extends ExComposite {

	private static PartInfoUiBinder uiBinder = GWT.create(PartInfoUiBinder.class);

	interface PartInfoUiBinder extends UiBinder<Widget, PartInfo> {
	}

	public PartInfo() {
		initWidget(uiBinder.createAndBindUi(this));
	}

    @UiField
    MaterialTextBox boxPKey,boxStmp,boxMemo;
    @UiField
    MaterialFloatBox boxTemp,boxPress,boxHumid;
    @UiField
    MaterialListBox cmbFormat,cmbUnitRef,cmbUnitMea,cmbEmitter;
    
    @UiField
    MaterialCheckBox chkUseLogo;

	@Override
	public void onEventShow() {
	}
	@Override
	public void onEventHide() {
	}
	
	private ItemProdx target;
	
	public void setTarget(ItemProdx obj){
		target = obj;
		updateBox(target);
	}
	
	public ItemProdx getTarget(){
		if(target!=null){ return target; }
		return null;
	}
	
	private void updateBox(ItemProdx itm){
		boxPKey.setText(itm.getKey());
		boxStmp.setText(Main.fmtDate.format(itm.stmp));
		boxMemo.setText(itm.getMemo());
		boxTemp.setText(itm.getTemperature());
		boxPress.setText(itm.getPressure());
		boxHumid.setText(itm.getHumidity());
		chkUseLogo.setValue(itm.useLogo);
	}
}

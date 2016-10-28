package nthu.hpclp.client.product;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;

import nthu.hpclp.client.Main;
import nthu.hpclp.client.PartSelect;
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ItemTenur;

public class PartTenur extends PartSelect<ItemTenur> {

	private static PartTenurUiBinder uiBinder = GWT.create(PartTenurUiBinder.class);

	interface PartTenurUiBinder extends UiBinder<Widget, PartTenur> {
	}
	
	public PartTenur() {
		initWidget(uiBinder.createAndBindUi(this));		
		root.add(Main.dlgEditTenur);
		root.add(Main.dlgPickTenur);
		boxTKey.getIcon().addClickHandler(eventEdit);
	}

	@UiField
	MaterialPanel root;
	
	@UiField(provided=true)
	public MaterialTextBox boxTKey = boxKey;
	
	@UiField MaterialLabel
    	txtInfoT1,txtInfoT2,txtInfoT3,
    	txtInfoT4,txtInfoT5,txtInfoT6;

	private ItemProdx parent;

	public void setTarget(ItemProdx obj){
		parent = obj;
		if(obj!=null){
			target = parent.getTenur();
		}else{
			target = null;
		}		
		updateBox();
	}
	
	@Override
	public ItemTenur getTarget(boolean create) {
		if(create==false){
			return target;
		}
		return new ItemTenur();
	}

	@Override
	public void updateBox() {
    	boxTKey.setText("");
		if(parent!=null){
			parent.setTenur(target);
		}
    	if(target!=null){
    		txtInfoT1.setText(target.getDeviceVendor());
    		txtInfoT2.setText(target.getDeviceSerial());
    		txtInfoT3.setText(target.getDeviceNumber());
    		txtInfoT4.setText(target.getDetectType());
    		txtInfoT5.setText(target.getDetectSerial());
    		txtInfoT6.setText(target.getDetectNumber());
    		//TODO:how to focus the next item??
    	}else{
    		txtInfoT1.setText("");
    		txtInfoT2.setText("");
    		txtInfoT3.setText("");
    		txtInfoT4.setText("");
    		txtInfoT5.setText("");
    		txtInfoT6.setText("");
    	}
	}
}

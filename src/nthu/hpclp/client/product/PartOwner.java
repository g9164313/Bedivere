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
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemProdx;

public class PartOwner extends PartSelect<ItemOwner> {

	private static PartOwnerUiBinder uiBinder = GWT.create(PartOwnerUiBinder.class);

	interface PartOwnerUiBinder extends UiBinder<Widget, PartOwner> {
	}

	public PartOwner() {
		initWidget(uiBinder.createAndBindUi(this));
		root.add(Main.dlgModifyOwner);
		root.add(Main.dlgPickupOwner);
		boxOKey.getIcon().addClickHandler(eventEdit);
	}

	@UiField
	MaterialPanel root;
	
	@UiField(provided=true)
	public MaterialTextBox boxOKey = boxKey;
	
	@UiField
	MaterialLabel txtInfoO1,txtInfoO2,txtInfoO3;
	
	public void setTarget(ItemProdx obj){
		parent = obj;
		if(obj!=null){
			target = parent.getOwner();
		}else{
			target = null;
		}
		updateBox();
	}

	@Override
	public ItemOwner getTarget(boolean create) {
		if(create==false){
			return target;
		}
		return new ItemOwner();
	}

	@Override
	public void updateBox() {
		boxOKey.setText("");
		if(parent!=null){
			parent.setOwner(target);
		}
    	if(target!=null){
    		txtInfoO1.setText(target.getKey());
    		txtInfoO2.setText(target.getName());
    		txtInfoO3.setText(target.getAddress());
    	}else{
    		txtInfoO1.setText("");
    		txtInfoO2.setText("");
    		txtInfoO3.setText("");
    	}
	}
}

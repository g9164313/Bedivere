package nthu.hpclp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import nthu.hpclp.shared.ItemAccnt;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemProdx;

public class PartEditOwner extends Composite {

	private static PartEditOwnerUiBinder uiBinder = GWT.create(PartEditOwnerUiBinder.class);

	interface PartEditOwnerUiBinder extends UiBinder<Widget, PartEditOwner> {
	}

	public PartEditOwner() {
		initWidget(uiBinder.createAndBindUi(this));
		root.add(Main.dlgEditOwner);
		root.add(Main.dlgPickOwner);
	}

	@UiField
	MaterialPanel root;
	@UiField
	MaterialLabel txtInfoO1,txtInfoO2,txtInfoO3;
	@UiField
	MaterialTextBox boxOKey;
	
	private ItemProdx targetProdx;
	private ItemAccnt targetAccnt;
	
	public void setTarget(ItemProdx obj){
		targetProdx = obj;
		updateBox(targetProdx.owner);
	}
	
	public void setTarget(ItemAccnt obj){
		targetAccnt = obj;
		updateBox(targetAccnt.owner);
	}
	
	private ItemOwner getTarget(){
		if(targetProdx!=null){ return targetProdx.owner; }
		if(targetAccnt!=null){ return targetAccnt.owner; }
		return null;
	}

	private void updateTarget(ItemOwner itm){
		if(targetProdx!=null){ targetProdx.owner = itm; }
		if(targetAccnt!=null){ targetAccnt.owner = itm; }
	}

	private void updateBox(ItemOwner itm){
		boxOKey.setText("");
    	if(itm!=null){
    		txtInfoO1.setText(itm.getKey());
    		txtInfoO2.setText(itm.getName());
    		txtInfoO3.setText(itm.getAddress());
    		//TODO:how to focus the next item??
    	}else{
    		txtInfoO1.setText("");
    		txtInfoO2.setText("");
    		txtInfoO3.setText("");
    	}
    	updateTarget(itm);    	
	}

	private final ClickHandler eventUpdate = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			updateBox(Main.dlgPickOwner.getTarget());
		}
	}; 
	
    @UiHandler("boxOKey")
    void onChangeKeyOwner(ValueChangeEvent<String> event){
    	String txt = event.getValue().trim();
    	if(txt.equalsIgnoreCase("+")==true){
    		onEditOwner(null);
    		return;
    	}    	
    	String post = "WHERE "+
    		"(info[1] SIMILAR TO '%"+txt+"%') OR "+
    		"(info[2] SIMILAR TO '%"+txt+"%') OR "+
    		"(info[4] SIMILAR TO '%"+txt+"%') OR "+
    		"(info[6] SIMILAR TO '%"+txt+"%') ORDER BY last DESC ";
    	Main.dlgPickOwner.appear(post,eventUpdate);
    }
    
    @UiHandler("icoEditOwner")
    void onEditOwner(ClickEvent e){
    	ItemOwner itm = getTarget();
    	if(itm==null){
    		MaterialToast.fireToast("新增委託單位");
    		itm = new ItemOwner();
    	}
    	Main.dlgEditOwner.appear(itm,null,eventUpdate);
    }

    @UiHandler("icoClearOwner")
    void onClearOwner(ClickEvent e){
    	updateBox(null);
    	boxOKey.setFocus(true);
    }
}

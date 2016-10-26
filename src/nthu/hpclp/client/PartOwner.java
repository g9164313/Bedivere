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

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import nthu.hpclp.shared.ItemAccnt;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemProdx;

public class PartOwner extends Composite {

	private static PartOwnerUiBinder uiBinder = GWT.create(PartOwnerUiBinder.class);

	interface PartOwnerUiBinder extends UiBinder<Widget, PartOwner> {
	}

	public PartOwner() {
		initWidget(uiBinder.createAndBindUi(this));
		root.add(Main.dlgEditOwner);
		root.add(Main.dlgPickOwner);
		boxOKey.getIcon().addClickHandler(eventEdit);
	}

	@UiField
	MaterialPanel root;
	@UiField
	MaterialLabel txtInfoO1,txtInfoO2,txtInfoO3;
	@UiField
	public MaterialTextBox boxOKey;
	
	private ItemAccnt targetAccnt;
	private ItemProdx targetProdx;
	
	public void setTarget(ItemProdx obj){
		targetAccnt = null;
		targetProdx = obj;
		updateBox(targetProdx.getOwner());
	}
	
	public void setTarget(ItemAccnt obj){
		targetAccnt = obj;
		targetProdx = null;
		updateBox(targetAccnt.getOwner());
	}
	
	public ItemOwner getTarget(){		
		if(targetAccnt!=null){ return targetAccnt.getOwner(); }
		if(targetProdx!=null){ return targetProdx.getOwner(); }
		return null;
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
	}
	
	private void update_item(ItemOwner itm){
		if(targetAccnt!=null){
			targetAccnt.setOwner(itm);
			updateBox(itm);
		}
		if(targetProdx!=null){
			targetProdx.setOwner(itm);
			updateBox(itm);
		}
		boxOKey.setText("");
	}
	
	private final ClickHandler eventEdit = new ClickHandler(){		
		@Override
		public void onClick(ClickEvent event) {
			ItemOwner itm = getTarget();
	    	if(itm==null){
	    		return;
	    	}
	    	if(event==null){	    		
	    		itm = new ItemOwner();
	    		MaterialToast.fireToast("新增委託單位");
	    	}else{
	    		MaterialToast.fireToast("修改委託單位");
	    	}
	    	Main.dlgEditOwner.appear(itm,eventUpdate,null);
		}
		private ClickHandler eventUpdate = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				update_item(Main.dlgEditOwner.getTarget());
			}
		};
	};
	
	public MaterialWidget nextBox = null;
	
    @UiHandler("boxOKey")
    void onChangeKeyOwner(ValueChangeEvent<String> event){
    	String txt = event.getValue().trim();
    	if(txt.equalsIgnoreCase("+")==true){
    		eventEdit.onClick(null);
    		return;
    	}
    	final ClickHandler eventPickup = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				update_item(Main.dlgPickOwner.getTarget());
				//goto next box???
				if(nextBox!=null){
					nextBox.setFocus(true);
				}
			}
    	};
    	String post = "WHERE "+
    		"(info[1] SIMILAR TO '%"+txt+"%') OR "+
    		"(info[2] SIMILAR TO '%"+txt+"%') OR "+
    		"(info[4] SIMILAR TO '%"+txt+"%') OR "+
    		"(info[6] SIMILAR TO '%"+txt+"%') ORDER BY last DESC ";
    	Main.dlgPickOwner.appear(post,eventPickup);
    }
}

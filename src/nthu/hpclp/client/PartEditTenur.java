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
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ItemTenur;

public class PartEditTenur extends Composite {

	private static PartEditTenurUiBinder uiBinder = GWT.create(PartEditTenurUiBinder.class);

	interface PartEditTenurUiBinder extends UiBinder<Widget, PartEditTenur> {
	}

	public PartEditTenur() {
		initWidget(uiBinder.createAndBindUi(this));		
		root.add(Main.dlgEditTenur);
		root.add(Main.dlgPickTenur);
	}

	@UiField
	MaterialPanel root;
	
	@UiField MaterialLabel
    	txtInfoT1,txtInfoT2,txtInfoT3,
    	txtInfoT4,txtInfoT5,txtInfoT6;
	
	@UiField
	MaterialTextBox boxTKey;
	
	private ItemProdx targetProdx;
	
	public void setTarget(ItemProdx obj){
		targetProdx = obj;
		updateBox(targetProdx.tenur);
	}

	private ItemTenur getTarget(){
		if(targetProdx!=null){ return targetProdx.tenur; }
		return null;
	}
	
	private void updateTarget(ItemTenur itm){
		if(targetProdx!=null){ targetProdx.tenur = itm; }
	}
	
	private void updateBox(ItemTenur itm){
    	boxTKey.setText("");
    	if(itm!=null){
    		txtInfoT1.setText(itm.getDeviceVendor());
    		txtInfoT2.setText(itm.getDeviceSerial());
    		txtInfoT3.setText(itm.getDeviceNumber());
    		txtInfoT4.setText(itm.getDetectType());
    		txtInfoT5.setText(itm.getDetectSerial());
    		txtInfoT6.setText(itm.getDetectNumber());
    		//TODO:how to focus the next item??
    	}else{
    		txtInfoT1.setText("");
    		txtInfoT2.setText("");
    		txtInfoT3.setText("");
    		txtInfoT4.setText("");
    		txtInfoT5.setText("");
    		txtInfoT6.setText("");
    	}    	
    	updateTarget(itm);
	}
	
	private final ClickHandler eventUpdate = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			updateBox(Main.dlgPickTenur.getTarget());
		}
	}; 
	
    @UiHandler("boxTKey")
    void onChangeKeyTenure(ValueChangeEvent<String> event){
    	String txt = event.getValue().trim().toLowerCase();
    	if(txt.equalsIgnoreCase("+")==true){
    		onEditTenur(null);
    		return;
    	}    	
    	String post = "WHERE "+
    		"(tenure.info[1] SIMILAR TO '%"+txt+"%') OR "+
    		"(tenure.info[2] SIMILAR TO '%"+txt+"%') OR "+
    		"(tenure.info[3] SIMILAR TO '%"+txt+"%') OR "+
    		"(tenure.info[4] SIMILAR TO '%"+txt+"%') OR "+
    		"(tenure.info[6] SIMILAR TO '%"+txt+"%') OR "+
    		"(tenure.info[7] SIMILAR TO '%"+txt+"%') OR "+
    		"(tenure.info[11] SIMILAR TO '%"+txt+"%') ORDER BY last DESC ";
    	Main.dlgPickTenur.appear(post,eventUpdate);
    }
    
    @UiHandler("icoEditTenur")
    void onEditTenur(ClickEvent e){
    	if(targetProdx.owner==null){
    		MaterialToast.fireToast("無廠商資訊");
    		return;
    	}
    	ItemTenur itm = getTarget();
    	if(itm==null){
    		MaterialToast.fireToast("新增儀器資料");
    		//TODO: how to select a different owner???
    		itm = new ItemTenur(targetProdx.owner);
    	}
    	Main.dlgEditTenur.appear(itm,null,eventUpdate);
    }
    
    @UiHandler("icoClearTenur")
    void onClearTenur(ClickEvent e){
    	updateBox(null);
    	boxTKey.setFocus(true);
    }
}

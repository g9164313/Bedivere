package nthu.hpclp.client.itembase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIntegerBox;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialModal;
import narl.itrc.client.DlgBase;

public class DlgPageIndex extends DlgBase<Integer> {

	private static DlgPageIndexUiBinder uiBinder = GWT.create(DlgPageIndexUiBinder.class);

	interface DlgPageIndexUiBinder extends UiBinder<Widget, DlgPageIndex> {
	}

	public DlgPageIndex() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField(provided=true) 
	MaterialModal root = _dlg_root;	
	@UiField(provided=true)
	MaterialButton btnAction = _btn_action;	
	@UiField(provided=true)
	MaterialButton btnCancel = _btn_cancel;
	
	@UiField
	MaterialIntegerBox boxIndex;
	
	@UiField
	MaterialLabel txtTotal;
	
	public void setTotal(int val){
		txtTotal.setText("/ "+val);
	}
	
	@UiHandler("boxIndex")
    void onChangeIndex(ChangeEvent event){
		eventAction.onClick(null);
	}
	
	@Override
	public void eventAppear(Integer item) {
		boxIndex.setText(""+item);
		boxIndex.setFocus(true);
	}

	@Override
	public void takeAction(ClickEvent event) {
		try{
			target = Integer.valueOf(boxIndex.getText());
		}catch(NumberFormatException e){			
		}		
	}

	@Override
	public void takeCancel(ClickEvent event) {
	}
}

package nthu.hpclp.client.setting;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialTextBox;
import narl.itrc.client.DlgBase;
import nthu.hpclp.shared.ItemParam;

public class DlgModifyValue extends DlgBase<ItemParam> {

	private static DlgModifyValueUiBinder uiBinder = GWT.create(DlgModifyValueUiBinder.class);

	interface DlgModifyValueUiBinder extends UiBinder<Widget, DlgModifyValue> {
	}

	public DlgModifyValue() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField(provided=true) 
	MaterialModal root = _dlg_root;
	@UiField(provided=true)
	MaterialButton btnAction = _btn_action;
	@UiField(provided=true)
	MaterialButton btnCancel = _btn_cancel;
	
	@UiField
	MaterialTextBox boxValue;
	
	@Override
	public String toString(){
		return boxValue.getText();
	}
	
	@Override
	public void eventAppear(ItemParam item) {
		boxValue.setText(item.getVal());
	}

	@Override
	public void takeAction(ClickEvent event) {
	}

	@Override
	public void takeCancel(ClickEvent event) {
	}
}

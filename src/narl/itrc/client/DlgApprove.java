package narl.itrc.client;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialModal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class DlgApprove extends DlgBase<String> {

	private static DlgApproveUiBinder uiBinder = GWT.create(DlgApproveUiBinder.class);

	interface DlgApproveUiBinder extends UiBinder<Widget, DlgApprove> {
	}

	public DlgApprove() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField(provided=true) 
	MaterialModal root = _dlg_root;
	@UiField(provided=true)
	MaterialButton btnAction = _btn_action;
	@UiField(provided=true)
	MaterialButton btnCancel = _btn_cancel;
	
	@UiField
	MaterialIcon icon;
	@UiField
	MaterialLabel text;

	
	public void appear(final String title,final ClickHandler hook){
		text.setText(title);
		appear(null,hook,null);
	}
	
	@Override
	public void eventAppear(String item) {
	}

	@Override
	public void takeAction(ClickEvent event) {
	}

	@Override
	public void takeCancel(ClickEvent event) {
	}
}

package narl.hpclp.client;

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
		refxWidget(root,btnAction,btnCancel);
	}

	@Override
	public void onEventShow() {
	}

	@Override
	public void onEventHide() {
	}
	
	
	@UiField
	MaterialModal root;
	
	@UiField
	MaterialIcon icon;
	
	@UiField
	MaterialLabel text;
	
	@UiField
	MaterialButton btnAction,btnCancel;
	
	public void appear(final String title,final ClickHandler hook){
		text.setText(title);
		appear(null,hook,null);
	}
	
	@Override
	void eventAppear(String item) {
	}

	@Override
	void takeAction(ClickEvent event) {
	}

	@Override
	void takeCancel(ClickEvent event) {
	}
}

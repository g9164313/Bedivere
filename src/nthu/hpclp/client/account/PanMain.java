package nthu.hpclp.client.account;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialPanel;
import narl.itrc.client.ExComposite;

public class PanMain extends ExComposite {

	private static PanMainUiBinder uiBinder = GWT.create(PanMainUiBinder.class);

	interface PanMainUiBinder extends UiBinder<Widget, PanMain> {
	}

	public PanMain() {
		initWidget(uiBinder.createAndBindUi(this));
		initAddins(root);//prepare some basic dialogs
	}

	@UiField
	MaterialPanel root;
	
	@Override
	public void onEventShow() {
	}

	@Override
	public void onEventHide() {
	}
}

package nthu.hpclp.client.account;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialPanel;
import narl.itrc.client.ExComposite;
import nthu.hpclp.client.Main;

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

	@UiHandler("lnkPanMeet")
	void onPanMeet(ClickEvent e){
		Main.switchToMeeting();
	}
	
	@UiHandler("lnkPanProdx")
	void onPanAccnt(ClickEvent e){
		Main.switchToProduct();
	}
	
	@UiHandler("lnkPanSetting")
	void onPanStorage(ClickEvent e){
		Main.switchToSetting();
	}
	
}

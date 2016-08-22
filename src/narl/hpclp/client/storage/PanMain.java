package narl.hpclp.client.storage;

import narl.hpclp.client.Main;
import narl.itrc.client.ExComposite;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class PanMain extends ExComposite {

	private static PanMainUiBinder uiBinder = GWT.create(PanMainUiBinder.class);

	interface PanMainUiBinder extends UiBinder<Widget, PanMain> {
	}

	@UiField
	HTMLPanel arch1,arch2;
	
	public PanMain() {
		initWidget(uiBinder.createAndBindUi(this));
		arch1.add(new PanTimeMachine());
	}

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
	
	@UiHandler("lnkPanAccnt")
	void onPanAccnt(ClickEvent e){
		Main.switchToAccount();
	}
	
	@UiHandler("lnkPanProdx")
	void onPanProdx(ClickEvent e){
		Main.switchToProduct();
	}
}

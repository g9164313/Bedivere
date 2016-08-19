package narl.hpclp.client.storage;

import narl.hpclp.client.Main;
import narl.itrc.client.ExComposite;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

public class PanMain extends ExComposite {

	private static PanMainUiBinder uiBinder = GWT.create(PanMainUiBinder.class);

	interface PanMainUiBinder extends UiBinder<Widget, PanMain> {
	}

	public PanMain() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public PanMain(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
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

package nthu.hpclp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialSideNav;
import narl.itrc.client.ExComposite;

public class PartFuncPager extends Composite {

	private static PartFuncPagerUiBinder uiBinder = GWT.create(PartFuncPagerUiBinder.class);

	interface PartFuncPagerUiBinder extends UiBinder<Widget, PartFuncPager> {
	}
	
	public PartFuncPager() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void initPanel(){		
		pan[PAN_PRODUCT] = new nthu.hpclp.client.product.PanMain();
		switch_panel(PAN_DEFAULT);
	}
	
	@UiHandler("lnkPanProdx")
	void onLnkPan1(ClickEvent e){
		switch_panel(PAN_PRODUCT);
	}
	
	@UiHandler("lnkPanOwner")
	void onLnkPan2(ClickEvent e){
		switch_panel(PAN_OWNER);
	}
	
	@UiHandler("lnkPanTenur")
	void onLnkPan3(ClickEvent e){
		switch_panel(PAN_TENURE);
	}
	
	@UiHandler("lnkPanSPoint")
	void onLnkPan4(ClickEvent e){
		switch_panel(PAN_SPOINT);
	}

	private final static int PAN_MEETING=0;
	private final static int PAN_PRODUCT=1;
	private final static int PAN_OWNER  =2;
	private final static int PAN_TENURE =3;
	private final static int PAN_ACCOUNT=4;
	private final static int PAN_SPOINT =5;
	private final static int PAN_SETTING=6;
	
	private final static int PAN_DEFAULT=PAN_PRODUCT;//Do we need to fix this? No~~~
	
	public ExComposite[] pan = new ExComposite[7];
	
	private void switch_panel(int i){
		RootPanel root = RootPanel.get();
		root.clear();
		root.add(pan[i]);		
	}
}

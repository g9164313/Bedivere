package nthu.hpclp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import nthu.hpclp.client.itembase.PanItemOwner;
import nthu.hpclp.client.itembase.PanItemTenur;
import nthu.hpclp.client.setting.PanSetting;


public class PartFuncPager extends Composite {

	private static PartFuncPagerUiBinder uiBinder = GWT.create(PartFuncPagerUiBinder.class);

	interface PartFuncPagerUiBinder extends UiBinder<Widget, PartFuncPager> {
	}
	
	public PartFuncPager() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void initPanel(){		
		pan[PAN_PRODUCT] = new nthu.hpclp.client.product.PanMain();
		pan[PAN_OWNER] = new PanItemOwner();
		pan[PAN_TENURE] = new PanItemTenur();
		pan[PAN_SETTING] = new PanSetting();	
		switch_panel(PAN_DEFAULT);
	}
	
	@UiHandler("lnkPanProdx")
	void onLnkPanProdx(ClickEvent e){
		switch_panel(PAN_PRODUCT);
	}
	
	@UiHandler("lnkPanOwner")
	void onLnkPanOwner(ClickEvent e){
		switch_panel(PAN_OWNER);
	}
	
	@UiHandler("lnkPanTenur")
	void onLnkPanTenur(ClickEvent e){
		switch_panel(PAN_TENURE);
	}
	
	@UiHandler("lnkPanParam")
	void onLnkPanParam(ClickEvent e){
		switch_panel(PAN_SETTING);
	}
	

	//private final static int PAN_MEETING=0;
	private final static int PAN_PRODUCT=1;
	//private final static int PAN_ACCOUNT=2;
	private final static int PAN_OWNER  =3;
	private final static int PAN_TENURE =4;	
	private final static int PAN_SETTING =5;
	private final static int PAN_MAX_SIZE=6;
	
	//private final static int PAN_DEFAULT = PAN_OWNER;
	//private final static int PAN_DEFAULT = PAN_TENURE;
	private final static int PAN_DEFAULT = PAN_PRODUCT;//Do we need to fix this? No~~~
	
	public Composite[] pan = new Composite[PAN_MAX_SIZE];
	
	private void switch_panel(int i){
		RootPanel root = RootPanel.get();
		root.clear();
		root.add(pan[i]);		
	}
}

package nthu.hpclp.client.setting;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTab;
import narl.itrc.client.ExComposite;
import nthu.hpclp.client.Main;

public class PanSetting extends ExComposite {

	private static PanSettingUiBinder uiBinder = GWT.create(PanSettingUiBinder.class);

	interface PanSettingUiBinder extends UiBinder<Widget, PanSetting> {
	}

	public PanSetting() {
		initWidget(uiBinder.createAndBindUi(this));
		initAddins(root);

		root.add(ColUpdate.dlgValue);
		root.add(ColUpdate.dlgService);
		root.add(ColUpdate.dlgEmitter);
		
		grid[0] = new GrdParam(Main.param.prodxRadUnit);
		grid[1] = new GrdParam(Main.param.prodxDetType);
		grid[2] = new GrdParam(Main.param.accntService);
		grid[3] = new GrdParam(Main.param.prodxEmitter);
	
		archTab1.add(grid[0]);
		archTab2.add(grid[1]);
		archTab3.add(grid[2]);
		archTab4.add(grid[3]);
		archTab5.add(spoint);
	}

	@UiField
	MaterialPanel root;
	@UiField
    MaterialNavBar navAppBar;
	@UiField
	MaterialTab tab;
	@UiField
	MaterialColumn archTab1,archTab2,archTab3,archTab4,archTab5;
	
	private GrdParam[] grid = {null,null,null,null};
	
	private PanSPoint spoint = new PanSPoint(); 
	
    @UiHandler("lnkTab1")
    void onLnkTab1(ClickEvent e) {
    	grid[0].data2grid();
    }
    
    @UiHandler("lnkTab2")
    void onLnkTab2(ClickEvent e) {
    	grid[1].data2grid();
    }
    
    @UiHandler("lnkTab3")
    void onLnkTab3(ClickEvent e) {
    	grid[2].data2grid();
    }
    
    @UiHandler("lnkTab4")
    void onLnkTab4(ClickEvent e) {
    	grid[3].data2grid();
    }
    
    @UiHandler("lnkTab5")
    void onLnkTab5(ClickEvent e) {
    	spoint.list_spoint();
    }
    
	@Override
	public void onEventShow() {
		navAppBar.add(Main.funcPager);
	}

	@Override
	public void onEventHide() {
	}
}

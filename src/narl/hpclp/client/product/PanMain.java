package narl.hpclp.client.product;

import narl.hpclp.client.Main;
import narl.hpclp.shared.ItemProdx;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class PanMain extends Composite {

	private static PanMainUiBinder uiBinder = GWT.create(PanMainUiBinder.class);

	interface PanMainUiBinder extends UiBinder<Widget, PanMain> {
	}

	@UiField
	MaterialPanel root;
			
    @UiField
    MaterialNavBar appNav, searchNav;

    @UiField
    MaterialSearch search;
	
    @UiField
    MaterialLink lnkPanMeet,lnkPanAccnt;
    
    @UiField
    MaterialListBox cmbFormat,cmbUnit1,cmbUnit2;
    
    @UiField
    MaterialLabel txtStrength,txtSurface,
    	txtFactorK,txtFactorP,txtUncertain,
    	txtSerial,txtCriteron,txtArea;
    
	public PanMain() {
		initWidget(uiBinder.createAndBindUi(this));
		initSearch();		
		root.add(Main.dlgOwner);
		root.add(Main.dlgTenur);
		addAttachHandler(eventShowHide);		
	}

	private AttachEvent.Handler eventShowHide = new AttachEvent.Handler(){
		@Override
		public void onAttachOrDetach(AttachEvent event) {
			if(event.isAttached()==true){
				//At this time, web just prepare enviroment paramters
				Main.initCombo(cmbFormat, ItemProdx.USED_TXT_FMT);
				Main.initCombo(cmbUnit1, Main.param.prodxUnit);
				Main.initCombo(cmbUnit2, Main.param.prodxUnit);
			}
		}
	};
	
	@UiHandler("lnkPanMeet")
	void onLnkPanMeet(ClickEvent e){
		Main.switchToMeeting();
	}
	
	@UiHandler("lnkPanAccnt")
	void onLnkPanAccnt(ClickEvent e){
		Main.switchToAccount();
	}
	
    @UiHandler("lnkSearch")
    void onSearch(ClickEvent e) {
        appNav.setVisible(false);
        searchNav.setVisible(true);
    }
    private final ChangeHandler eventSearch = new ChangeHandler(){
		@Override
		public void onChange(ChangeEvent event) {
		}
    };
    private void initSearch(){
		search.addCloseHandler(new CloseHandler<String>() {
            @Override
            public void onClose(CloseEvent<String> event) {
                appNav.setVisible(true);
                searchNav.setVisible(false);
            }
        });
		search.addChangeHandler(eventSearch);
    } 
    
    private void initCombo(){
    	
    }
    
}

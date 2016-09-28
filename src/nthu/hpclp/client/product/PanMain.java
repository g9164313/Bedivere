package nthu.hpclp.client.product;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialCollection;
import gwt.material.design.client.ui.MaterialFloatBox;
import gwt.material.design.client.ui.MaterialLabel;

import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import nthu.hpclp.client.Main;
import nthu.hpclp.client.PartOwne;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemProdx;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class PanMain extends PanCtrl {

	private static PanMainUiBinder uiBinder = GWT.create(PanMainUiBinder.class);

	interface PanMainUiBinder extends UiBinder<Widget, PanMain> {
	}
    
	public PanMain() {
		initWidget(uiBinder.createAndBindUi(this));
		initList();
		root.add(Main.dlgApprove);
		anrchOwner.setWidget(owne);
		anrchTenur.setWidget(tenu);
		anchorInfo.setWidget(info);
	}

	@UiField 
	MaterialPanel root;
	
    //@UiField
    //MaterialNavBar navApp, navSearch;
    //@UiField
    //MaterialSearch boxSearch;
	
	@UiField
	SimplePanel anrchOwner,anrchTenur,anchorInfo;
	
	@UiField
	SimplePanel anchorScriber,anchorEmitter;
	
	private PartOwne owne = new PartOwne();
	private PartTenu tenu = new PartTenu();
	private PartInfo info = new PartInfo();
	
    @UiField(provided=true)
    MaterialModal dlgList = _dlgList;
    @UiField(provided=true) 
    SimplePanel anchorList1 = _anchorList1;
    @UiField(provided=true) 
    SimplePanel anchorList2 = _anchorList2;

	@Override
	public void updateBox(ItemProdx itm) {
	}
	
	
	
}

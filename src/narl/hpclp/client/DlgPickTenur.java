package narl.hpclp.client;

import narl.hpclp.shared.ItemTenur;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialModal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class DlgPickTenur extends DlgBase<ItemTenur> {

	private static DlgPickTenurUiBinder uiBinder = GWT.create(DlgPickTenurUiBinder.class);

	interface DlgPickTenurUiBinder extends UiBinder<Widget, DlgPickTenur> {
	}

	@UiField
	MaterialModal root;
	
	//@UiField
	//MaterialButton btnAction,btnCancel;
	
	public DlgPickTenur() {
		initWidget(uiBinder.createAndBindUi(this));
		//refxWidget(root,btnAction,btnCancel);
	}

	@Override
	void takeAction(ClickEvent event) {
	}

	@Override
	void eventAppear(ItemTenur item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void takeCancel(ClickEvent event) {
		// TODO Auto-generated method stub
		
	}
}

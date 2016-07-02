package narl.hpclp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialModal;
import narl.hpclp.shared.ItemOwner;

public class DlgItemOwner extends DlgItem {

	private static DlgItemOwnerUiBinder uiBinder = GWT.create(DlgItemOwnerUiBinder.class);

	interface DlgItemOwnerUiBinder extends UiBinder<Widget, DlgItemOwner> {
	}

	@UiField
	MaterialModal root;
	
	@UiField
	MaterialButton btnAction,btnCancel;

	public DlgItemOwner() {
		initWidget(uiBinder.createAndBindUi(this));
		refxWidget(root,btnAction,btnCancel);
	}

	private ItemOwner item = null;
	
	public void appear(ItemOwner itm){
		item = itm;
		appear(itm.id);
	}

	@Override
	void takeAction(ClickEvent event) {
		// TODO Auto-generated method stub
		
	}
}

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
import narl.hpclp.shared.ItemTenur;

public class DlgItemTenur extends DlgItem {

	private static DlgItemTenurUiBinder uiBinder = GWT.create(DlgItemTenurUiBinder.class);

	interface DlgItemTenurUiBinder extends UiBinder<Widget, DlgItemTenur> {
	}

	@UiField
	MaterialModal root;
	
	@UiField
	MaterialButton btnAction;
	
	@UiField
	MaterialButton btnCancel;
	
	public DlgItemTenur() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private ItemTenur item = null;
	
	public void appear(ItemTenur itm){
		item = itm;
		appear(itm.id);
	}

	@Override
	void takeAction(ClickEvent event) {
		// TODO Auto-generated method stub
		
	}
}

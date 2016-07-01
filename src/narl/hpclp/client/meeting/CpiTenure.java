package narl.hpclp.client.meeting;

import narl.hpclp.shared.ItemTenur;
import gwt.material.design.client.ui.MaterialCollapsibleItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CpiTenure extends Composite {

	private static CpiTenureUiBinder uiBinder = GWT
			.create(CpiTenureUiBinder.class);

	interface CpiTenureUiBinder extends UiBinder<Widget, CpiTenure> {
	}

	@UiField
	MaterialCollapsibleItem cpiEntry;
	
	public CpiTenure() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private ItemTenur tenur;
	
	private PanMain root;
	
	public CpiTenure(PanMain root,ItemTenur item){
		initWidget(uiBinder.createAndBindUi(this));
		this.root = root;
		this.tenur= item;
		
		
	}
	
}

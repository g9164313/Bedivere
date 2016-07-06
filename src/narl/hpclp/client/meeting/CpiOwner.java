package narl.hpclp.client.meeting;

import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialLabel;
import narl.hpclp.client.Main;
import narl.hpclp.shared.ItemMeeting;
import narl.hpclp.shared.ItemTenur;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CpiOwner extends Composite {

	private static CpiOwnerUiBinder uiBinder = GWT
			.create(CpiOwnerUiBinder.class);

	interface CpiOwnerUiBinder extends UiBinder<Widget, CpiOwner> {
	}

	@UiField
	MaterialLabel txtKey,txtName,txtTotal;
	
	@UiField
	MaterialLabel txtTime,txtAddress,txtInteract,txtPerson,txtMemo;
	
	@UiField
	MaterialCollapsibleItem cpiEntry;
			
	private ItemMeeting meet;
	
	private PanMain root;
	
	public CpiOwner() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public CpiOwner(PanMain root,ItemMeeting item) {
		initWidget(uiBinder.createAndBindUi(this));
		this.root = root;
		this.meet = item;
		mapping();
		//hook event
		cpiEntry.addClickHandler(eventPick);
	}
	
	@UiHandler("icoEdit")
	void onEditItem(ClickEvent e){
		Main.dlgOwner.appear(meet,eventRefresh);
	}
	
	@UiHandler("icoPrint")
	void onPrintItem(ClickEvent e){
		
	}
	
	private Runnable eventRefresh = new Runnable(){
		@Override
		public void run() {
			mapping();
		}
	};
	
	private ClickHandler eventPick = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			root.lstTenur.clear();
			String txt = cpiEntry.getStyleName();
			if(txt.indexOf("active")>=0){
				return;
			}
			if(meet.lst.isEmpty()==true){
				return;
			}
			for(ItemTenur item:meet.lst){
				root.lstTenur.add(new CpiTenure(item));
			}			
		}
	};
	
	private void mapping(){
		//head title
		txtKey.setText(meet.getKey());
		txtName.setText(meet.getName());
		txtTotal.setText(""+meet.lst.size()+"台");		
		//body context
		txtTime.setText(Main.fmtMeeting.format(meet.stmp));
		txtAddress.setText(meet.getZip()+" "+meet.getAddress());
		txtInteract.setText(meet.getPhone()+"   "+meet.getEMail());
		txtPerson.setText(meet.getDepartment()+"   "+meet.getPerson());
		txtMemo.setText(meet.getMemo());
	}
}

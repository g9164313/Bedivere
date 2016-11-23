package nthu.hpclp.client.meeting;

import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialLabel;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.ItemMeeting;
import nthu.hpclp.shared.ItemTenur;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CpiOwner extends Composite {

	private static CpiOwnerUiBinder uiBinder = GWT.create(CpiOwnerUiBinder.class);

	interface CpiOwnerUiBinder extends UiBinder<Widget, CpiOwner> {
	}

	@UiField
	MaterialLabel txtKey,txtName,txtTotal;
	
	@UiField
	MaterialLabel txtStmp,txtAddr,txtPhon,txtPern,txtMemo;
	
	@UiField
	MaterialCollapsibleItem cpiEntry;
	
	private PanMain root;	
	private ItemMeeting meet;
	
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
		if(meet.isRestday()==true){
			return;
		}
		Main.dlgModifyOwner.appear(
			meet,null,new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				//update again!!!
				//because sorting depend on this text~~
				mapping();
				meet.setSDay(Main.fmtDate.format(meet.stmp),meet.stmp);
				root.redraw();
			}
		});
	}
	
	@UiHandler("icoPrint")
	void onPrintItem(ClickEvent e){
		Main.printLetter(meet);		
	}
	
	private ClickHandler eventPick = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			root.colTenur.clear();
			String txt = cpiEntry.getStyleName();
			if(txt.indexOf("active")>=0){
				return;
			}
			if(meet.lst.isEmpty()==true){
				return;
			}
			for(ItemTenur tenu:meet.lst){
				root.colTenur.add(new CpiTenure(root,meet,tenu));
			}			
		}
	};
	
	private void mapping(){		
		//head title
		if(meet.isRestday()==true){
			String msg = meet.memo;
			int cnt = meet.lst.size();
			if(cnt!=0){
				msg = msg+"("+cnt+"台)";
			}
			txtKey.setText("");	
			txtName.setText(msg);
			txtTotal.setText("");
		}else{
			txtKey.setText(meet.getKey());
			txtName.setText(meet.getName());
			txtTotal.setText(""+meet.lst.size()+"台");
		}	
		//body context		
		txtStmp.setText(Main.fmtMeeting.format(meet.stmp));		
		txtAddr.setText(meet.getZip()+" "+meet.getAddress());
		txtPhon.setText(meet.getPhone()+"   "+meet.getEMail());
		txtPern.setText(meet.getDepartment()+"   "+meet.getPerson());
		txtMemo.setText(meet.getMemo());
	}
}

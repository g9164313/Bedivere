package narl.hpclp.client;

import java.util.ArrayList;

import narl.hpclp.shared.ItemOwner;

import gwt.material.design.client.constants.CollectionType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCollection;
import gwt.material.design.client.ui.MaterialCollectionItem;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialToast;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class DlgPickOwner extends DlgBase<ItemOwner> {

	private static DlgPickOwnerUiBinder uiBinder = GWT.create(DlgPickOwnerUiBinder.class);

	interface DlgPickOwnerUiBinder extends UiBinder<Widget, DlgPickOwner> {
	}

	@UiField
	MaterialModal root;
	
	@UiField
	MaterialButton btnAction,btnCancel;
	
	@UiField
	MaterialLabel txtSelTitle;
	
	@UiField
	MaterialCollection colSelector;

	@UiField
	MaterialLabel 
		txtInfo1,txtInfo2,txtInfo3,
		txtInfo4,txtInfo5,txtInfo6,
		txtInfo7;
	
	public DlgPickOwner() {
		initWidget(uiBinder.createAndBindUi(this));
		refxWidget(root,btnAction,btnCancel);
	}

	public void appear(final String postfix,final ClickHandler hook){
		txtInfo1.setText("");
		txtInfo2.setText("");
		txtInfo3.setText("");
		txtInfo4.setText("");
		txtInfo5.setText("");
		txtInfo6.setText("");
		txtInfo7.setText("");
		Main.rpc.listOwner(postfix,new AsyncCallback<ArrayList<ItemOwner>>(){
			@Override
			public void onFailure(Throwable caught) {
				MaterialToast.fireToast("內部錯誤\n"+caught.getMessage());
			}
			@Override
			public void onSuccess(ArrayList<ItemOwner> result) {
				if(result.isEmpty()==true){
					MaterialToast.fireToast("查無資料");
					return;
				}		
				refresh_selector(result);
				appear(null,null,hook);
			}
		});		
	}
		
	class CitOwner extends MaterialCollectionItem implements ClickHandler {
		public ItemOwner itm;
		public CitOwner(int indx,ItemOwner item){
			itm = item;
			setType(CollectionType.CHECKBOX);
			setWaves(WavesType.DEFAULT);
			setTitle(indx);
			addClickHandler(this);
		}
		private void setTitle(int indx){
			MaterialRow row = new MaterialRow();
			add_col(row,"",""+indx+")");
			add_col(row,"",itm.getKey());
			add_col(row,"27em",itm.getName());
			add(row);
		}
		private void add_col(MaterialRow row,String size,String value){
			MaterialColumn col = new MaterialColumn();
			MaterialLabel txt = new MaterialLabel(value);
			if(size.length()!=0){
				txt.setWidth(size);
			}
			col.add(txt);
			row.add(col);
		}
		@Override
		public void onClick(ClickEvent event) {
			txtInfo1.setText(itm.getKey());
			txtInfo2.setText(itm.getName());
			txtInfo3.setText(Main.fmtDate.format(itm.stmp));
			txtInfo4.setText(itm.getZip()+" "+itm.getAddress());
			txtInfo5.setText(itm.getDepartment()+" "+itm.getPerson());
			txtInfo6.setText(itm.getPhone());
			txtInfo7.setText(itm.getEMail());
			target = itm;//remember updating target!!!
		}
	};	
	private void refresh_selector(ArrayList<ItemOwner> result){
		int cnt = result.size();
    	colSelector.clear();    	
    	txtSelTitle.setText("共"+cnt+"筆資料");		
		for(int idx=0; idx<cnt; idx++){
			colSelector.add(new CitOwner(idx+1,result.get(idx)));
		}
	}
	
	@Override
	void eventAppear(ItemOwner item) {
	}

	@Override
	void takeAction(ClickEvent event) {		
	}

	@Override
	void takeCancel(ClickEvent event) {
	}
}

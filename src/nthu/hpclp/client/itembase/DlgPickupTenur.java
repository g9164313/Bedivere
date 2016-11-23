package nthu.hpclp.client.itembase;

import java.util.ArrayList;

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
import narl.itrc.client.DlgBase;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemTenur;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class DlgPickupTenur extends DlgBase<ItemTenur> {

	private static DlgPickupTenurUiBinder uiBinder = GWT.create(DlgPickupTenurUiBinder.class);

	interface DlgPickupTenurUiBinder extends UiBinder<Widget, DlgPickupTenur> {
	}

	public DlgPickupTenur() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField(provided=true) 
	MaterialModal root = _dlg_root;
	@UiField(provided=true)
	MaterialButton btnAction = _btn_action;
	@UiField(provided=true)
	MaterialButton btnCancel = _btn_cancel;
	
	@UiField
	MaterialLabel txtSelTitle;
	
	@UiField
	MaterialCollection colSelector;

	@UiField
	MaterialLabel 
		txtInfo1,txtInfo2,txtInfo3,
		txtInfo4,txtInfo5,txtInfo6,
		txtInfo7,txtInfo8,txtInfo9,
		txtInfo10,txtInfo11;
	
	public void appear(final String postfix,final ClickHandler hook){
		txtInfo1.setText("");
		txtInfo2.setText("");
		txtInfo3.setText("");
		txtInfo4.setText("");
		txtInfo5.setText("");
		txtInfo6.setText("");
		txtInfo7.setText("");
		Main.rpc.listTenur(
			postfix,
			new AsyncCallback<ArrayList<ItemTenur>>(){
			@Override
			public void onFailure(Throwable caught) {
				MaterialToast.fireToast("內部錯誤\n"+caught.getMessage());
			}
			@Override
			public void onSuccess(ArrayList<ItemTenur> result) {
				if(result.isEmpty()==true){
					MaterialToast.fireToast("查無資料");
					return;
				}
				if(result.size()==1){
					//just one result, so pick up this item~~~
					target = result.get(0);
					hook.onClick(null);
					return;
				}
				refresh_selector(result);
				appear(null,hook,null);
			}
		});	
	}
	
	class CitTenur extends MaterialCollectionItem implements ClickHandler {
		public ItemTenur tenur;
		public CitTenur(int indx,ItemTenur item){
			tenur = item;
			setType(CollectionType.CHECKBOX);
			setWaves(WavesType.DEFAULT);
			setTitle(indx);
			addClickHandler(this);
		}
		private void setTitle(int indx){
			MaterialRow row = new MaterialRow();
			add_col(row,"",""+indx+")");
			add_col(row,"",tenur.getName());
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
			txtInfo1.setText(tenur.getDeviceVendor());
			txtInfo2.setText(tenur.getDeviceSerial());
			txtInfo3.setText(tenur.getDeviceNumber());
			txtInfo4.setText(tenur.getDetectType());
			txtInfo5.setText(tenur.getDetectSerial());
			txtInfo6.setText(tenur.getDetectNumber());
			txtInfo7.setText(tenur.getArea());
			txtInfo8.setText(tenur.getFactor());
			txtInfo9.setText(tenur.getSteer());
			txtInfo10.setText(tenur.getMemo()+"/"+Main.fmtDate.format(tenur.meet));
			ItemOwner owner = tenur.getOwner();
			if(owner!=null){
				txtInfo11.setText(owner.getFullName());
			}else{
				txtInfo11.setText("???");
			}
			target = tenur;//remember updating target!!!
		}
	};	
	private void refresh_selector(ArrayList<ItemTenur> result){
		int cnt = result.size();
    	colSelector.clear();    	
    	txtSelTitle.setText("共"+cnt+"筆資料");		
		for(int idx=0; idx<cnt; idx++){
			colSelector.add(new CitTenur(idx+1,result.get(idx)));
		}
	}
	
	@Override
	public void takeAction(ClickEvent event) {
	}

	@Override
	public void eventAppear(ItemTenur item) {
	}

	@Override
	public void takeCancel(ClickEvent event) {
	}
}

package nthu.hpclp.client;

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
import nthu.hpclp.shared.ItemTenur;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class DlgPickTenur extends DlgBase<ItemTenur> {

	private static DlgPickTenurUiBinder uiBinder = GWT.create(DlgPickTenurUiBinder.class);

	interface DlgPickTenurUiBinder extends UiBinder<Widget, DlgPickTenur> {
	}

	public DlgPickTenur() {
		initWidget(uiBinder.createAndBindUi(this));
		refxWidget(root,btnAction,btnCancel);
	}

	@Override
	public void onEventShow() {
	}

	@Override
	public void onEventHide() {
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
		Main.rpc.listTenure(postfix,new AsyncCallback<ArrayList<ItemTenur>>(){
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
				appear(null,null,hook);
			}
		});	
	}
	
	class CitTenur extends MaterialCollectionItem implements ClickHandler {
		public ItemTenur itm;
		public CitTenur(int indx,ItemTenur item){
			itm = item;
			setType(CollectionType.CHECKBOX);
			setWaves(WavesType.DEFAULT);
			setTitle(indx);
			addClickHandler(this);
		}
		private void setTitle(int indx){
			MaterialRow row = new MaterialRow();
			add_col(row,"",""+indx+")");
			add_col(row,"",itm.getName());
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
			txtInfo1.setText(itm.getDeviceVendor());
			txtInfo2.setText(itm.getDeviceSerial());
			txtInfo3.setText(itm.getDeviceNumber());
			txtInfo4.setText(itm.getDetectType());
			txtInfo5.setText(itm.getDetectSerial());
			txtInfo6.setText(itm.getDetectNumber());
			txtInfo7.setText(itm.getArea());
			txtInfo8.setText(itm.getFactor());
			txtInfo9.setText(itm.getSteer());
			txtInfo10.setText(itm.getMemo()+"/"+Main.fmtDate.format(itm.meet));
			if(itm.owner!=null){
				txtInfo11.setText(itm.owner.getFullName());
			}else{
				txtInfo11.setText("???");
			}
			target = itm;//remember updating target!!!
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
	void takeAction(ClickEvent event) {
	}

	@Override
	void eventAppear(ItemTenur item) {
	}

	@Override
	void takeCancel(ClickEvent event) {
	}
}

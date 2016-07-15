package narl.hpclp.client.product;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import narl.hpclp.shared.ItemOwner;
import narl.hpclp.shared.ItemProdx;
import narl.hpclp.shared.ItemTenur;
import gwt.material.design.client.constants.CollectionType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialCollectionItem;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;

public class CitProduct extends MaterialCollectionItem implements ClickHandler {

	private ItemProdx prodx;
	private PanMain root;

	public CitProduct(PanMain root,int serial, ItemProdx item){
		this.root = root;
		this.prodx = item;
		setType(CollectionType.CHECKBOX);
		setWaves(WavesType.DEFAULT);
		setTitle(serial);
		addClickHandler(this);		
	}

	private void setTitle(int serial){
		MaterialRow row = new MaterialRow();
		add_col(row,"5em","第"+serial+"筆");
		add_col(row,"7em",prodx.getKey());
		if(prodx.owner!=null){			
			ItemOwner itm = prodx.owner;
			add_col(row,"18em",itm.getName());
		}
		if(prodx.tenur!=null){
			ItemTenur itm = prodx.tenur;
			add_col(row,"7em",itm.getDeviceVendor());
			add_col(row,"7em",itm.getDeviceSerial());
			add_col(row,"7em",itm.getDeviceNumber());
		}
		this.add(row);
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
		root.curProdx = prodx;
		root.prodx2box();
	}
}

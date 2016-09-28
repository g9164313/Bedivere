package nthu.hpclp.client.product;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import gwt.material.design.client.constants.CollectionType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialCollectionItem;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ItemTenur;

public class CitProduct extends MaterialCollectionItem implements ClickHandler {

	private ItemProdx prodx;
	
	private PanMain root;

	public CitProduct(PanMain root,int serial,ItemProdx item){
		this.root = root;
		this.prodx = item;
		setType(CollectionType.CHECKBOX);
		setWaves(WavesType.DEFAULT);
		initLayout(serial);
		addClickHandler(this);		
	}

	private void initLayout(int serial){
		MaterialRow row = new MaterialRow();
		add_col(row,"5em","第"+serial+"筆");
		
		String key = prodx.getKey();
		if(key.length()!=0){
			add_col(row,"7em",key);			
		}else{
			add_col(row,"7em","???");
		}
		
		if(prodx.owner!=null){			
			ItemOwner itm = prodx.owner;
			add_col(row,"18em",itm.getName());
		}else{
			add_col(row,"18em","???");
		}
		
		if(prodx.tenur!=null){
			ItemTenur itm = prodx.tenur;
			add_col(row,"6em",itm.getDeviceVendor());
			add_col(row,"10em",itm.getDeviceSerial());
			add_col(row,"10em",itm.getDeviceNumber());
		}else{
			add_col(row,"26em","???");
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
		//root.prodx2box(prodx);
	}
}

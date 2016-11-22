package nthu.hpclp.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import narl.itrc.client.DlgBase;
import narl.itrc.client.ExComposite;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ItemTenur;

public abstract class PartSelect<T> extends ExComposite {
	
	public PartSelect(){
		boxKey.addKeyPressHandler(eventSetKey);
	}
	
	public MaterialWidget nextBox = null;
	
	protected MaterialTextBox boxKey = new MaterialTextBox();
	
	public T target;
	
	protected ItemProdx parent;
	
	public abstract T getTarget(boolean create);

	public final ClickHandler eventEdit = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			if(event==null){
				target = getTarget(true);//create a new one
			}else{
				target = getTarget(false);
			}
			if(target instanceof ItemOwner){
				
				Main.dlgEditOwner.appear(
					(ItemOwner)target,
					eventEditDone,
					null
				);
				
			}else if(target instanceof ItemTenur){
				
				Main.dlgEditTenur.appear(
					(ItemTenur)target,
					eventEditDone,
					null
				);
				
			}else{
				MaterialToast.fireToast("no support type",1000);
			}
		}
	};
	
	private final ClickHandler eventEditDone = new ClickHandler(){
		@SuppressWarnings("unchecked")
		@Override
		public void onClick(ClickEvent event) {
			if(target instanceof ItemOwner){			
				target = (T)Main.dlgEditOwner.getTarget();
			}else if(target instanceof ItemTenur){
				target = (T)Main.dlgEditTenur.getTarget();
			}
			updateBox();
		}
	};
	
	
	private final KeyPressHandler eventSetKey = new KeyPressHandler(){
		@SuppressWarnings("unchecked")
		@Override
		public void onKeyPress(KeyPressEvent event) {
			
			int code = event.getNativeEvent().getKeyCode();	
			if(code!=KeyCodes.KEY_ENTER){
				return;
			}
			String txt = boxKey.getText().trim();
			if(txt.equalsIgnoreCase("+")==true){				
				eventEdit.onClick(null);
				return;
			}

			if(target instanceof ItemOwner){
				
				Main.dlgPickOwner.appear(
					"WHERE "+
				    "info[1] SIMILAR TO '%"+txt+"%' OR "+
				    "info[2] SIMILAR TO '%"+txt+"%' OR "+
				    "info[4] SIMILAR TO '%"+txt+"%' OR "+
				    "info[5] SIMILAR TO '%"+txt+"%' OR "+
				    "info[6] SIMILAR TO '%"+txt+"%' OR "+
				    "info[7] SIMILAR TO '%"+txt+"%' OR "+
				    "info[8] SIMILAR TO '%"+txt+"%' ORDER BY last DESC ",
				    eventPickup
				);
				dialog = (DlgBase<T>) Main.dlgPickOwner;
				
			}else if(target instanceof ItemTenur){
				
				Main.dlgPickTenur.appear(
					"WHERE "+
					"lower(tenure.info[2]) SIMILAR TO '%"+txt+"%' OR "+
					"lower(tenure.info[3]) SIMILAR TO '%"+txt+"%' OR "+
					"lower(tenure.info[4]) SIMILAR TO '%"+txt+"%' OR "+
					"tenure.info[5] SIMILAR TO '%"+txt+"%' OR "+
					"lower(tenure.info[6]) SIMILAR TO '%"+txt+"%' OR "+
					"lower(tenure.info[7]) SIMILAR TO '%"+txt+"%' OR "+
					"tenure.info[1] SIMILAR TO '%"+txt+"%' ORDER BY last DESC ",
					eventPickup
				);
				dialog = (DlgBase<T>) Main.dlgPickTenur;
				
			}else{
				MaterialToast.fireToast("no support type",1000);
			}
		}
	};

	private DlgBase<T> dialog;
	
	private final ClickHandler eventPickup = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {			
			target = dialog.getTarget();
			parent.markModify();
			updateBox();			
			boxKey.setText("");
			if(nextBox!=null){
				nextBox.setFocus(true);
			}
		}
	};
	
	/**
	 * children should update parent-item
	 */
	public abstract void updateBox(); 

	@Override
	public void onEventShow() {
	}

	@Override
	public void onEventHide() {
	}
}

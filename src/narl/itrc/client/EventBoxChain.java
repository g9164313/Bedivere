package narl.itrc.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.TextBox;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialTextBox;

public class EventBoxChain implements KeyDownHandler {
	
	public ArrayList<MaterialWidget> list = new ArrayList<MaterialWidget>();
	
	public boolean isLoop = true;

	@Override
	public void onKeyDown(KeyDownEvent event) {
		
		int code = event.getNativeKeyCode();
		
		if(code!=KeyCodes.KEY_ENTER){
			if(code!=KeyCodes.KEY_RIGHT&&code!=KeyCodes.KEY_LEFT){
				return;
			}
			if(event.isControlKeyDown()==false){
				return;
			}
		}
		
		MaterialTextBox cur = (MaterialTextBox)((TextBox)event.getSource()).getParent();
		if(list.contains(cur)==false){
			return;
		}
		
		int idx = list.indexOf(cur);
		switch(code){
		case KeyCodes.KEY_ENTER:
		case KeyCodes.KEY_PAGEDOWN:
			idx++;
			if(idx>=list.size()){
				if(isLoop==true){
					idx = 0;
				}else{
					idx--;
				}
			}
			break;			
		case KeyCodes.KEY_PAGEUP:
			idx--;
			if(idx<0){
				if(isLoop==true){
					idx = list.size() - 1;
				}else{
					idx = 0;
				}
			}
			break;
		}
					
		list.get(idx).setFocus(true);
	}
	
	public static void link(
		EventBoxChain chain,
		MaterialWidget... listBox
	){
		chain.list.clear();
		for(MaterialWidget box:listBox){
			if(box==null){
				chain.isLoop = false;
				break;
			}
			chain.list.add(box);
			if(box instanceof MaterialTextBox){
				((MaterialTextBox)box).addKeyDownHandler(chain);
			}			
		}
	}
}

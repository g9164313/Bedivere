package nthu.hpclp.client.product;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialProgress;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemBase;
import nthu.hpclp.shared.ItemTenur;

public class DlgGenReport extends Composite {

	private static DlgGenReportUiBinder uiBinder = GWT.create(DlgGenReportUiBinder.class);

	interface DlgGenReportUiBinder extends UiBinder<Widget, DlgGenReport> {
	}

	public DlgGenReport() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	MaterialModal root;	
	@UiField
	MaterialProgress barProgress;
	@UiField
	MaterialLabel txtProgress;
	@UiField
	TextArea boxMessage;
	@UiField
	MaterialButton btnCancel,btnRestart;
	
	public void appear(){		
		boxMessage.setText("");
		root.openModal();
		onRestart(null);
	}
	
	private AsyncCallback<Void> eventGroup =
		new AsyncCallback<Void>()
	{
		@Override
		public void onFailure(Throwable caught) {
			print("內部錯誤！！");
			print(caught.getMessage());
		}
		@Override
		public void onSuccess(Void result) {
			if(btnRestart.isEnabled()==true){
				return;//user may break operation!!!
			}
			int max = lst.size();
			end++;
			progress(end,max);
			if(end>=max){
				btnRestart.setEnabled(true);
				//generate report
		    	Window.open(
			    	GWT.getHostPageBaseURL()+Const.REPORT_TENURE,
			    	"_blank",""
			    );
				return;//we complete the task~~~
			}
			beg = end;//reset the index of list~~~ 
			check_group();
			Main.rpc.groupReport1(
				beg,end,
				eventGroup
			);//next turn~~~
		}
	};
	
	private AsyncCallback<ArrayList<ItemBase>> eventReset = 
		new AsyncCallback<ArrayList<ItemBase>>()
	{
		@Override
		public void onFailure(Throwable caught) {
			print("內部錯誤！！");
			print(caught.getMessage());
		}
		@Override
		public void onSuccess(ArrayList<ItemBase> result) {			
			lst = result;
			beg = end = 0;//reset the index of list~~~
			check_group();
			Main.rpc.groupReport1(
				beg,end,
				eventGroup
			);//launch the first task~~~
		}
	};
	
	private int beg,end;
	private ArrayList<ItemBase> lst;
	
	private void check_group(){
		//find prefix
		String ref = ItemTenur.trimTypo(lst.get(beg).info[0].trim());
		//match the prefix (device serial)
		int cnt = lst.size();
		for(;end<cnt;end++){
			String src = ItemTenur.trimTypo(lst.get(end).info[0].trim());
			if(src.equals(ref)==false){
				end--;
				break;
			}
		}
		cnt = end - beg + 1;
		print("儀器型號（"+beg+"，"+end+"）#"+cnt+"："+ref);
	}
	
	private void progress(int stp,int max){
		float val = ((float)stp/(float)max)*100f;
		barProgress.setPercent(val);
		txtProgress.setText(""+Math.round(val)+"％ （"+stp+"/"+max+"）");
	}
	
	private void print(String msg){		
		String txt = boxMessage.getText();
		//if(txt.length()>=3000){
		//	txt = msg;
		//}else{
			txt = txt +"\r\n" + msg;
		//}
		boxMessage.setText(txt);
		//boxMessage.getElement().getFirstChildElement().setScrollTop(
		//	boxMessage.getElement().getFirstChildElement().getScrollHeight()
		//);//very strange method~~~~
	}
	
	@UiHandler("btnRestart")
    void onRestart(ClickEvent e){
		print("初始化...");
		progress(0,1);
		btnRestart.setEnabled(false);		
		Main.rpc.resetReport1(eventReset);
    }
	
    @UiHandler("btnCancel")
    void onCancel(ClickEvent e){
    	btnRestart.setEnabled(true);
    	root.closeModal();
    }
}

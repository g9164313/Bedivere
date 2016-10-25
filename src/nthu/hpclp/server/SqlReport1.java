package nthu.hpclp.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemBase;

public class SqlReport1 {

	private static ArrayList<ItemBase> datLst = new ArrayList<ItemBase>();
	
	public static ArrayList<ItemBase> reset(){
		grpList.clear();
		datLst.clear();
		ArrayList<ItemBase> title = new ArrayList<ItemBase>();
		try {
			String cmd1 = 
				"SELECT "+
				  "ROW_NUMBER() OVER (PARTITION BY tenure ORDER BY tenure.info[1] DESC, product.last DESC) AS rowIndx,"+
				  "product.info[1],"+
				  "tenure.info[1],"+
				  "tenure.info[2],"+
				  "tenure.info[3],"+
				  "tenure.info[4],"+
			 	  "product.last AS last,"+
				  "product.scribble AS scribble "+
				"FROM product,tenure "+
				"WHERE product.tid = tenure.id "+
				"ORDER BY tenure.info[3] ASC, product.last ASC";
			String cmd_out = "SELECT * FROM ("+cmd1+") AS tab1 WHERE tab1.rowIndx = 1";
								
			ResultSet rs = RpcBridge.getResult(cmd_out);
			while(rs.next()){
				
				ItemBase itm = new ItemBase(5);
				itm.info[0] = rs.getString(2);//product key
				itm.info[1] = rs.getString(3);//tenure key
				itm.info[2] = rs.getString(4);//tenure vendor
				itm.info[3] = rs.getString(5);//tenure serial
				itm.info[4] = rs.getString(6);//tenure number
				itm.setDate(rs.getDate(7));
				itm.appx = SqlDataBase.getTxtArray(rs, "scribble");
				datLst.add(itm);
				
				ItemBase name = new ItemBase(2);
				name.info[0] = itm.info[3];//tenure serial
				name.info[1] = itm.info[4];//tenure number
				title.add(name);
			}
		} catch (SQLException e) {			
			System.err.print(e.getMessage());
		}
		return title;
	} 
	
	private static ArrayList<ItemBase> grpList = new ArrayList<ItemBase>();

	public static void group(int beg,int end){
		//reset statistic array~~~		
		for(int i=0; i<staFact.length; i++){ staFact[i]=0; }
		for(int i=0; i<staMeas.length; i++){ staMeas[i]=0; }
		//group one segment
		ItemBase grp = new ItemBase(
			4,
			staFact.length+staMeas.length
		);
		ItemBase fst = datLst.get(beg);
		grp.info[0] = fst.info[2];//tenure vendor
		grp.info[1] = fst.info[3];//tenure serial
		grp.info[2] = String.valueOf(end-beg+1);//total number
		int cnt = 0;
		for(int i=beg; i<=end; i++){
			if(i==datLst.size()){
				break;
			}
			ItemBase itm = datLst.get(i);
			if(itm.appx.length==0){
				System.out.printf("No scribble? strange product - "+itm.info[0]);
				continue;
			}
			cnt = cnt + fill_stat(itm);			
		}
		grp.info[3] = String.valueOf(cnt);
		fill_appx(grp);
	}
	
	
	private static int staFact[] = new int[6];//校正因子(±30%, <-20, -20~-10, -10~0, 0~10, 10~20, 20<), even number
	private static int staMeas[] = new int[6];//實驗數據(0~1%, 1~2%, 2~3%, 3~4%, 4~5%, 5%<)
	
	private static void fill_rest(String[] val){
		
		final int staOff = staFact.length / 2;
		
		SummaryStatistics stat = Formular.getStat(val[1]);
		//double ref_cnt = stat.getN();
		double ref_avg = stat.getMean();
		//double ref_dev = Math.sqrt(stat.getVariance());
		stat = Formular.getStat(val[2]);	
		double mea_cnt = stat.getN();
		double mea_avg = stat.getMean();
		double mea_dev = Math.sqrt(stat.getVariance());
		
		//統計校正因子
		double fact = ((ref_avg-mea_avg)/mea_avg)*100.;
		int idx = (int)Math.floor(fact/10.);//scale it~~~
		idx = idx + staOff;
		if(idx<0){
			idx = 0;
		}else if(idx>=staFact.length){
			idx = staFact.length - 1;
		}
		staFact[idx]++;
		//統計實驗數據
		if(mea_cnt<=1.){
			staMeas[0]++;
		}else{
			double diff = (mea_dev/(mea_cnt-1))*100.;
			//double diff = (mea_dev/mea_avg)*100.;
			idx = (int)Math.floor(diff);
			if(idx>=staMeas.length){
				idx = 5;
			}
			staMeas[idx]++;
		}
	}
	
	private static int fill_stat(ItemBase itm){
		if(itm.appx.length==0){
			return 0;
		}
		int start = 0;
		if(itm.appx[start].contains(Const.SPECIAL_PREFIX)==true){
			//找到"調整後"這個項目~~~
			do{				
				if(itm.appx[start].contains(Const.SPECIAL_PREFIX1)==true){
					start++;
					break;
				}
				start++;
			}while(start<itm.appx.length);
		}
		int cnt = 0;
		for(int j=start; j<itm.appx.length; j++){
			String[] val = itm.appx[j].split("@");
			if(val.length<3){
				//System.out.printf(
				//	"(%s) invalid scribble - %s\n",
				//	itm.info[1],itm.appx[j]
				//);
				continue;
			}
			fill_rest(val);
			cnt++;
		}
		return cnt;
	}
	
	private static void fill_appx(ItemBase grp){
		for(int i=0; i<staFact.length; i++){
			if(staFact[i]==0){
				grp.appx[i] = "";
			}else{
				grp.appx[i] = String.valueOf(staFact[i]);
			}
		}
		int off = staFact.length;
		for(int i=0; i<staMeas.length; i++){
			if(staMeas[i]==0){
				grp.appx[off+i] = "";
			}else{
				grp.appx[off+i] = String.valueOf(staMeas[i]);
			}
		}
		grpList.add(grp);
	}
	
	public static class DSrcTenure implements JRDataSource {	
		private int idx = -1;
		@Override
		public Object getFieldValue(JRField arg0) throws JRException {
			ItemBase itm = grpList.get(idx);			
			String name = arg0.getName();
			if (name.equals("tenur_vendor")) {
				return itm.info[0];
			}else if(name.equals("tenur_serial")){
				return itm.info[1];
			}else if(name.equals("total_number")){
				return itm.info[2];
			}else if(name.equals("total_measure")){
				return itm.info[3];
			}else if(name.equals("factor_0")){
				return itm.appx[0];
			}else if(name.equals("factor_1")){
				return itm.appx[1];
			}else if(name.equals("factor_2")){
				return itm.appx[2];
			}else if(name.equals("factor_3")){
				return itm.appx[3];
			}else if(name.equals("factor_4")){
				return itm.appx[4];
			}else if(name.equals("factor_5")){
				return itm.appx[5];
			}else if(name.equals("stddev_0")){
				return itm.appx[6];
			}else if(name.equals("stddev_1")){
				return itm.appx[7];
			}else if(name.equals("stddev_2")){
				return itm.appx[8];
			}else if(name.equals("stddev_3")){
				return itm.appx[9];
			}else if(name.equals("stddev_4")){
				return itm.appx[10];
			}else if(name.equals("stddev_5")){
				return itm.appx[11];
			}
			return null;
		}
		@Override
		public boolean next() throws JRException {
			if(grpList.isEmpty()==true){
				return false;
			}
			idx++;
			if(idx<grpList.size()){
				return true;
			}
			return false;
		}
	} 
}

package compucon.taxi.app.client;

public class Split1 {

	//String z = "0|0|0|0~2797|10|548|309~2797|10|361|268@";
	
	public String getAllTaxiIDs(String z){
		String [] qqq = z.split("~");
		String [] data = new String[6];

		qqq[qqq.length-1] = qqq[qqq.length-1].substring(0,qqq[qqq.length-1].length()-1);
		for (int i=1; i<=qqq.length-1;i++){
			data[i-1] = qqq[i];
		}
		String tax="";
		
		if ((qqq.length-1)==1){
			String temp[] = data[0].split("\\|");
			tax = temp[2];
			System.out.println(tax);
		}
		if ((qqq.length-1)==2){
			String temp[] = data[0].split("\\|");
			String temp1[] = data[1].split("\\|");
			tax = temp[2] + "," + temp1[2];
			System.out.println(tax);
		}
		if ((qqq.length-1)==3){
			String temp[] = data[0].split("\\|");
			String temp1[] = data[1].split("\\|");
			String temp2[] = data[2].split("\\|");
			tax = temp[2] + "," + temp1[2] + "," + temp2[2];
			System.out.println(tax);
		}
		if ((qqq.length-1)==4){
			String temp[] = data[0].split("\\|");
			String temp1[] = data[1].split("\\|");
			String temp2[] = data[2].split("\\|");
			String temp3[] = data[3].split("\\|");
			tax = temp[2] + "," + temp1[2] + "," + temp2[2] + "," + temp3[2];
			System.out.println(tax);
		}
		if ((qqq.length-1)==5){
			String temp[] = data[0].split("\\|");
			String temp1[] = data[1].split("\\|");
			String temp2[] = data[2].split("\\|");
			String temp3[] = data[3].split("\\|");
			String temp4[] = data[4].split("\\|");
			tax = temp[2] + "," + temp1[2] + "," + temp2[2] + "," + temp3[2] + "," +temp4[2];
			System.out.println(tax);
		}
		return tax;
	}
	
	
}

package compucon.taxi.app.client;

public class Split2 {
	String s;
	public Split2(String s){
		this.s = s;
	}
	
	public int getTaxis(){
		String [] x = s.split("#");
		System.out.println("SPLIT - x- " + s);
		System.out.println("SPLIT - Plithos taxi- " + x.length);
		return x.length;
	}
	
	
	public String getAllTaxiIDs(){
		String [] x = s.split("#");
		if(x.length==1){
			String [] p = x[0].split(";");
			return (p[0]);
		}
		else if(x.length==2){
			String [] p = x[0].split(";");
			String [] p1 = x[1].split(";");
			return (p[0] + "," + p1[0]);
		}
		else if(x.length==3){
			String [] p = x[0].split(";");
			String [] p1 = x[1].split(";");
			String [] p2 = x[2].split(";");
			return (p[0] + "," + p1[0] + "," + p2[0]);
		}
		else if(x.length==4){
			String [] p = x[0].split(";");
			String [] p1 = x[1].split(";");
			String [] p2 = x[2].split(";");
			String [] p3 = x[3].split(";");
			return (p[0] + "," + p1[0] + "," + p2[0] + "," + p3[0]);
		}
		else if(x.length==5){
			String [] p = x[0].split(";");
			String [] p1 = x[1].split(";");
			String [] p2 = x[2].split(";");
			String [] p3 = x[3].split(";");
			String [] p4 = x[4].split(";");
			return (p[0] + "," + p1[0] + "," + p2[0] + "," + p3[0] + "," + p4[0]);
		}
		else{
			return ("NO");
		}
		
	}
	
	public String getAllXOrientations(){
		String [] x = s.split("#");
		if(x.length==1){
			String [] p = x[0].split(";");
			return (p[1]);
		}
		else if(x.length==2){
			String [] p = x[0].split(";");
			String [] p1 = x[1].split(";");
			return (p[1] + "," + p1[1]);
		}
		else if(x.length==3){
			String [] p = x[0].split(";");
			String [] p1 = x[1].split(";");
			String [] p2 = x[2].split(";");
			return (p[1] + "," + p1[1] + "," + p2[1]);
		}
		else if(x.length==4){
			String [] p = x[0].split(";");
			String [] p1 = x[1].split(";");
			String [] p2 = x[2].split(";");
			String [] p3 = x[3].split(";");
			return (p[1] + "," + p1[1] + "," + p2[1] + "," + p3[1]);
		}
		else if(x.length==5){
			String [] p = x[0].split(";");
			String [] p1 = x[1].split(";");
			String [] p2 = x[2].split(";");
			String [] p3 = x[3].split(";");
			String [] p4 = x[4].split(";");
			return (p[1] + "," + p1[1] + "," + p2[1] + "," + p3[1] + "," + p4[1]);
		}
		else{
			return ("NO");
		}
		
	}
	
	public String getAllYOrientations(){
		String [] x = s.split("#");
		if(x.length==1){
			String [] p = x[0].split(";");
			return (p[2]);
		}
		else if(x.length==2){
			String [] p = x[0].split(";");
			String [] p1 = x[1].split(";");
			return (p[2] + "," + p1[2]);
		}
		else if(x.length==3){
			String [] p = x[0].split(";");
			String [] p1 = x[1].split(";");
			String [] p2 = x[2].split(";");
			return (p[2] + "," + p1[2] + "," + p2[2]);
		}
		else if(x.length==4){
			String [] p = x[0].split(";");
			String [] p1 = x[1].split(";");
			String [] p2 = x[2].split(";");
			String [] p3 = x[3].split(";");
			return (p[2] + "," + p1[2] + "," + p2[2] + "," + p3[2]);
		}
		else if(x.length==5){
			String [] p = x[0].split(";");
			String [] p1 = x[1].split(";");
			String [] p2 = x[2].split(";");
			String [] p3 = x[3].split(";");
			String [] p4 = x[4].split(";");
			return (p[2] + "," + p1[2] + "," + p2[2] + "," + p3[2] + "," + p4[2]);
		}
		else{
			return ("NO");
		}
		
	}
	
	public String getAngle(){
		String [] x = s.split("#");
		if(x.length==1){
			String [] p = x[0].split(";");
			return (p[3]);
		}
		else if(x.length==2){
			String [] p = x[0].split(";");
			String [] p1 = x[1].split(";");
			return (p[3] + "," + p1[3]);
		}
		else if(x.length==3){
			String [] p = x[0].split(";");
			String [] p1 = x[1].split(";");
			String [] p2 = x[2].split(";");
			return (p[3] + "," + p1[3] + "," + p2[3]);
		}
		else if(x.length==4){
			String [] p = x[0].split(";");
			String [] p1 = x[1].split(";");
			String [] p2 = x[2].split(";");
			String [] p3 = x[3].split(";");
			return (p[3] + "," + p1[3] + "," + p2[3] + "," + p3[3]);
		}
		else if(x.length==5){
			String [] p = x[0].split(";");
			String [] p1 = x[1].split(";");
			String [] p2 = x[2].split(";");
			String [] p3 = x[3].split(";");
			String [] p4 = x[4].split(";");
			return (p[3] + "," + p1[3] + "," + p2[3] + "," + p3[3] + "," + p4[3]);
		}
		else{
			return ("NO");
		}
		
	}
	
}

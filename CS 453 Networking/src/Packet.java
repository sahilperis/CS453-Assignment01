
public class Packet {

	int id;
	int size;
	float time;
	float headTime;
	float transTime;
	
	//constructor
	public Packet(int id, int size, float time ){
		this.id =id;
		this.size =size;
		this.time = time;
				
	}//end constructor
	
	public void printOutput(){
	    System.out.print(this.id+","+this.size+","+this.time+",");
	    System.out.print(this.headTime+",");
	    System.out.println(this.transTime);
	}
	
	public void dropOutput(){
		System.out.println(this.id+","+this.size+","+this.time+","+"NA,NA");
	}
	
	public int transDelay(int rate){
		return this.size/rate ;
	}
	
	
}//end Packet Class

import java.util.*;
public class Buffer {
  
	ArrayList<Packet> queue;
	int transmission_rate;
	int max_size; 
	int cap;
	String discipline;
	
	public Buffer(int rate, int limit, String type ){
		
	queue = new ArrayList<Packet>(); //the routers queue
    transmission_rate = rate;
    max_size = limit;
    discipline =type;
    cap = limit;
	}
	

	
    

	
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Router {

	public static void main(String[]args){

		Buffer buffer = new Buffer(0, 0, "simple");
		int transRate;
		int bsize;
		String policy;
		

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		boolean simple = false;
		try {
			int itr =0;
			while ((line = reader.readLine()) != null) {

				String[] attr= line.split(","); 
				
				
			
				
               //if first iteration
				if(itr == 0){
					
					itr++;
					transRate = Integer.parseInt(attr[0]);
					bsize = Integer.parseInt(attr[1]);
					policy = attr[2];
					buffer = new Buffer(transRate, bsize, policy);
					System.out.println(attr[0]+","+attr[1]+","+attr[2]);
					
					if(attr[2].equals("simple")){
						
					simple=true;	
				}
				
					
               }//if first iteration 
				
				else{
					int id = Integer.parseInt(attr[0]);
					int size = Integer.parseInt(attr[1]);
					float time = Float.parseFloat(attr[2]);

					//create Packet
					Packet p = new Packet(id, size, time);


                    //if router is following the simple policy  
					if(simple==true){


						//if this is the first incoming packet
						if(buffer.queue.isEmpty()){
							if(p.size <= buffer.max_size){
								buffer.queue.add(p);
								p.headTime=p.time;
								p.transTime = p.headTime+p.transDelay(buffer.transmission_rate);
								buffer.cap -= p.size;
								p.printOutput();
							}
						}


						else{//general cases start

							//re-evaluate queue 

							Packet last = buffer.queue.get(buffer.queue.size()-1); //keep track of the last packet in the buffer 

							for(int i=0; i<buffer.queue.size();i++){
								Packet t = buffer.queue.get(i);
								//System.out.println(temp.id);
								if(t.transTime <= p.time){
									//System.out.println("removing packet" +temp.id);
									buffer.queue.remove(i);
									//System.out.println("removed packet "+ buffer.queue.get(s).id);
									buffer.cap += t.size;

									//i++;
								}//end for loop
							}

							//if there is space for the packet
							if(buffer.cap>=p.size){
								if(p.time>=last.transTime){
									p.headTime=p.time;
								}
								else{
									p.headTime=last.transTime;
								}
								p.transTime = p.headTime+p.transDelay(buffer.transmission_rate);
								buffer.queue.add(p);
								buffer.cap -= p.size;
								p.printOutput();
							}//end if space for packet

							//else there is no space for the packet so drop it. 
							else{
								p.dropOutput();
							}


						}//general case ENDS


					}//if simple ==true

					//if router is following the 'longest' policy and simple=false
					else{
						if(buffer.queue.isEmpty()&&p.size <= buffer.max_size){
							buffer.queue.add(p);
							p.headTime=p.time;
							p.transTime = p.headTime+p.transDelay(buffer.transmission_rate);
							buffer.cap -= p.size;
							//p.printOutput();
						}//end buffer is empty

						//general case for longest policy starts here 
						else{

							//re-evaluate the buffer to print out packets that are being transmitted and remove the ones that have already been transmited.
							Packet last = buffer.queue.get(buffer.queue.size()-1);
							for(int i=0;i<buffer.queue.size();i++){

								Packet current = buffer.queue.get(i);

								//if its already been transmitted
								if(p.time>=current.transTime  ){
									//remove it and increase cap
									current.printOutput();
									buffer.queue.remove(i);
									buffer.cap += current.size;
								}

							}//for loop ends

							//check if there is space in the buffer and if there is then add the incoming packet
							if(buffer.cap>=p.size){

								if(p.time>=last.transTime){
									p.headTime=p.time;
								}
								else{
									p.headTime=last.transTime;
								}
								p.transTime = p.headTime+p.transDelay(buffer.transmission_rate);
								buffer.queue.add(p);
								buffer.cap -= p.size;
							}//end if no space in buffer

							//else find largest packet with latest arrival time and do the needful
							else{

								Packet largest = p;

								for(int i=0; i<buffer.queue.size(); i++){
									Packet current = buffer.queue.get(i);
									//if its not being transmitted
									if(current.headTime>p.time){
										if(current.size>largest.size){
											largest = current;
										}//if there is a larger packet

										else if (current.size==largest.size && current.time>largest.time){
											largest =current;
										}//if there is tie 
									}//end if its not being transmitted

								}//end for 

								//if largest is not the incoming packet and it is in the queue 
								if(largest.id!=p.id /*&& buffer.queue.get(0).id!=largest.id*/){
									buffer.queue.remove(largest);
									largest.dropOutput();
									buffer.cap += largest.size;

									//update the head and trans times of the remaining packets if the queue is not empty

									for(int i=0; i<buffer.queue.size()-1;i++){
										Packet cur = buffer.queue.get(i);
										Packet next = buffer.queue.get(i+1);

										//update packet info
										next.headTime=cur.transTime;
										next.transTime = next.headTime+next.transDelay(buffer.transmission_rate);

									}//end for 


									p.headTime=buffer.queue.get(buffer.queue.size()-1).transTime;
									p.transTime = p.headTime+p.transDelay(buffer.transmission_rate);
									buffer.queue.add(p);
									//System.out.println("added "+p.id);
									buffer.cap -= p.size;
									//System.out.println(buffer.cap);


								}//end if largest is not the incoming packet, it is in the queue and its not at the head of the queue

								//if largest is the incoming packet
								else if(largest.id==p.id){
									p.dropOutput();
								}



							}//end find largest packet with latest arrival time and do the needful



						}//general case ends

					}//longest ends
				}//end packet case
			}//while buffered reader ends
			
			//in the case that the router is following the longest policy, some packets may be left over the queue. Print them out. 
			
			if(simple==false){
			for(Packet l: buffer.queue){
				l.printOutput();
			  }
			}


		}//try block ends 

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}



}






import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class EventGenerator implements Runnable{
	
	//threadul ce genereaza evenimentele
	String fileName;
	ExecutorService tpe;
	int count;
	
	public EventGenerator(String fileName,ExecutorService tpe,int count) {
		this.fileName = fileName;
		this.tpe = tpe;
		this.count = count;
	}


	public void run(){

		BufferedReader sc = null;
		String read = null;
		
		//deschid fisierul
		try {
			sc = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		try {
			//citesc fiecare linie
			while( (read=sc.readLine()) != null){
				String[] ar = read.split(",");
				
				//astept timpul specificat
				Thread.sleep(Long.valueOf(ar[0]));
				
				//creez evenimentul si adaug in coada
				Event a = new Event(Integer.parseInt(ar[2]),ar[1]);
				Main.queue.put(a);
				tpe.submit(new WorkerPool(tpe,count));
				
			}
			
			
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}

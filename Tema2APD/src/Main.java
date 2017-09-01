import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	public static ArrayBlockingQueue<Event> queue ;
	public static List<Integer> PRIME = Collections.synchronizedList(new ArrayList<Integer>());
	public static List<Integer> FACT = Collections.synchronizedList(new ArrayList<Integer>());
	public static List<Integer> SQUARE = Collections.synchronizedList(new ArrayList<Integer>());
	public static List<Integer> FIB = Collections.synchronizedList(new ArrayList<Integer>());
	
	
	public static void main(String[] args) {
		
		//initializez dimensiunea cozii si dimensiunea evenimentelor
		int dimCoada = Integer.parseInt(args[0]);
		int evenimenteFisier = Integer.parseInt(args[1]);
		String []files =new String[args.length -2] ;
		int count = evenimenteFisier * files.length;
		
		//initializez coada cu dimensiunea cozii citita
		queue = new ArrayBlockingQueue<>(dimCoada);
		
		//retin in vector toate fisierele primite ca argument
		for(int i=0;i<files.length;i++){
			files[i] = args[i+2];
		}

		
		ExecutorService tpe = Executors.newFixedThreadPool(4);
		
		//creez threadurile
		Thread t[] = new Thread[files.length];
		for(int k=0;k<files.length;k++){
			t[k] = new Thread(new EventGenerator(files[k],tpe,count));
		}
		
		
		for(int k=0;k<files.length;k++){
			t[k].start();
		}
		
				
		
		for(int k=0;k<files.length;k++){
			try {
				t[k].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	
	}
}

//Oprea Ionut 334CB
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.concurrent.ExecutorService;

public class WorkerPool implements Runnable{

	ExecutorService tpe;
	int count;
	
	
	public WorkerPool(ExecutorService tpe,int count) {

		this.tpe = tpe;
		this.count = count;
	}

	//functiile necesare fiecarei operatii
	public static int prime(int N){
		int check = 1;
		int var = N;
		
		while(var>1){
				check = 1;
			for(int i=2;i<var;i++){
				if(var%i == 0) check = 0;
			}
				if(check == 1) break;
				else var--;
		}
		
		return var;
	}
	
	public static int fact(int N){
		int fact = 1;
		int i;
		
		for(i=1;i<N;i++){
			if(fact*i > N) break;
			fact *= i;
			
		}
		
		return i-1;
	}
	
	public static int square(int N){
		int i;
		
		for(i=1;i<N;i++){
			if(i*i > N) break;
			
		}
		
		return i-1;
	}
	
	public static int fib(int N){
		int f0 = 0;
		int f1 = 1;
		int aux;
		int i=1;
		
		while(true){
			if(f0+f1 > N) break;
			aux = f0;
			f0=f1;
			f1=aux+f1;
			i++;
		}
		
		
		return i;
	}
	
	
	public void run() {
		// TODO Auto-generated method stub
		
		//verific daca am terminat de prelucrat toate evenimentele necesare
		if(Main.FIB.size() + Main.FACT.size() + Main.SQUARE.size() + Main.PRIME.size() == count ) { 
			
			//sortez vectorii
			Collections.sort(Main.FIB); 
			Collections.sort(Main.SQUARE); 
			Collections.sort(Main.FACT); 
			Collections.sort(Main.PRIME); 
			
			
			//creez si completez fiecare fisier de output
			try {
				PrintWriter fibWrite = new PrintWriter("FIB.out","UTF-8");
				for(int i=0;i<Main.FIB.size();i++){
					fibWrite.println(Main.FIB.get(i));
				}
				fibWrite.close();
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				PrintWriter factWrite = new PrintWriter("FACT.out","UTF-8");
				for(int i=0;i<Main.FACT.size();i++){
					factWrite.println(Main.FACT.get(i));
				}
				factWrite.close();
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				PrintWriter primeWrite = new PrintWriter("PRIME.out","UTF-8");
				for(int i=0;i<Main.PRIME.size();i++){
					primeWrite.println(Main.PRIME.get(i));
				}
				primeWrite.close();
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				PrintWriter squareWrite = new PrintWriter("SQUARE.out","UTF-8");
				for(int i=0;i<Main.SQUARE.size();i++){
					squareWrite.println(Main.SQUARE.get(i));
				}
				squareWrite.close();
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			tpe.shutdown();
			return;
		}
		
		else if(Main.queue.isEmpty() == false){
			Event ev;
			try {
				//extrag evenimente din coada si le prelucrez corespunzator
				ev = Main.queue.take();
				if(ev.type.equals("PRIME")) { Main.PRIME.add(prime(ev.number)); }
				else if(ev.type.equals("SQUARE")) { Main.SQUARE.add(square(ev.number)); }
				else if(ev.type.equals("FIB")) { Main.FIB.add(fib(ev.number)); }
				else{ Main.FACT.add(fact(ev.number)); }
				tpe.submit(new WorkerPool(tpe,count));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
	}
	
	
}

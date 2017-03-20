package server;

import util.NetworkUtil;

import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class WriteThreadServer implements Runnable {

	private Thread thr;
	private String name;
	private Hashtable<String, NetworkUtil> table;

	public WriteThreadServer(Hashtable<String, NetworkUtil> table, String name) {
		this.table = table;
		this.name = name;
		this.thr = new Thread(this);
		thr.start();
	}

	public void run() {
		try {
			Scanner input=new Scanner(System.in);
			while(true) {
				String s=input.nextLine();

				StringTokenizer st = new StringTokenizer(s);
				String cName = st.nextToken();
				//StringBuffer sb=new StringBuffer(cName);
				//Set<String> keys = table.keySet();
				if((table.containsKey(cName))){

					//for(String key: keys){
						NetworkUtil nc = table.get(cName);
                        int i = s.indexOf(' ');
                        StringBuffer str = new StringBuffer(s);
                        str=str.delete(0,i);
						nc.write(name+": "+str);

					//}

				}
				else {
					/*NetworkUtil nc = table.get(cName);
					if (nc != null) {
						System.out.println();
						int i = s.indexOf(' ');
						StringBuffer str = new StringBuffer(s);
						str=str.delete(0,i);
						nc.write(name + ":" + str);
					}*/
					Set<String> keys = table.keySet();
					for(String key: keys){
						NetworkUtil nc = table.get(key);
						nc.write(name+": "+s);
					}
				}
			}
		} catch(Exception e) {
			System.out.println (e);
		}			
	}
}




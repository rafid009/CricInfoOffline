package util;

import server.MatchPortThread;
import server.MatchThread;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by ASUS on 3/17/2017.
 */
public class FileUploader {


    private MatchThread matchThread;

    private NetworkUtil nc;

    private int blockSize=512;

    public FileUploader(NetworkUtil nc, MatchThread matchThread) {
        this.matchThread =matchThread;
        this.nc =nc;


    }


    synchronized public void send(String filePath, String fileName, int fileSize) {


            nc.write("2::"+fileName+"::"+fileSize);

            try{

                File file = new File(filePath+fileName);
                FileInputStream fin = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fin);
                byte[] contents;
                int current = 0;
                int size = 256;
                while(current < fileSize){

                    System.out.println("file uploader uploading "+fileName);
                    if(fileSize - current < size)
                    {
                        size = fileSize - current;

                    }

                    contents = new byte[size];
                    bis.read(contents, current, size);
                    nc.write(fileName+"::"+current+"::"+size);
                    nc.write(contents);
                    System.out.println(contents);
                    nc.ncFlush();
                    String response=(String)nc.read();
                    
                    if(!response.equals("Got It")){
                        continue;
                    }

                    current+=size;



                }
                bis.close();
                fin.close();


            }
            catch (Exception e){
                String s = matchThread.getCurrOverTable().get(matchThread.getClintIdMap().get(nc));
                int l = Integer.parseInt(s)-1;
                matchThread.getCurrOverTable().remove(matchThread.getClintIdMap().get(nc));
                matchThread.getCurrOverTable().put(matchThread.getClintIdMap().get(nc), Integer.toString(l));
                matchThread.getMatchNcFUMap().remove(nc);
                matchThread.getMatchSubscriberPortMap().remove(matchThread.getClintIdMap().get(nc));
                matchThread.getClintIdMap().remove(nc);
                nc.closeConnection();
                System.out.println("Removes NC executed");
                e.printStackTrace();
            }





    }
}

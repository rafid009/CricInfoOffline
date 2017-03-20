package util;

import client.ClientMain;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.StringTokenizer;

/**
 * Created by ASUS on 3/17/2017.
 */
public class FileDownloader {
    private String fileName;
    private int fileSize;
    private NetworkUtil nc;

    private ClientMain clientMain;
    private String filePath="./src/clientFiles/";
    public FileDownloader(ClientMain clientMain, String fileName, int fileSize, NetworkUtil nc) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.nc = nc;
        this.clientMain = clientMain;

    }


    public void receive() {
        try{
            File folder = new File(filePath+Integer.toString(clientMain.getClientId())+"/");
            folder.mkdir();
            filePath = filePath + Integer.toString(clientMain.getClientId())+"/";
            File file = new File(filePath+fileName);
            FileOutputStream fout = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            int current=0;
            int numBlocks = fileSize/256;
            byte[] contents = new byte[256];
            while(current<fileSize){
                String msg = (String)nc.read();
                StringTokenizer tok = new StringTokenizer(msg, "::");

                    String fName = tok.nextToken();
                    int curr = Integer.parseInt(tok.nextToken());
                System.out.println(fName+" "+curr);
                    int size = Integer.parseInt(tok.nextToken());
                    try {
                        contents = (byte[]) nc.read();
                        bos.write(contents, curr, size);
                        current = curr+size;
                        nc.write("Got It");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        nc.write("no");
                        continue;
                    }

            }
            bos.flush();
            bos.close();
            fout.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }



    }
}

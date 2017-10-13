package jp.co.docomo.sdk.hitoe.sample;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tomoki on 2017/08/30.
 */

public class MyLogger{
    private static final MyLogger myLogger = new MyLogger();

    private String OUTPUT_DIR="hitoe_output";
    private String outputDir;
    private String outputFileTime;
    private SimpleDateFormat sdmYMD,sdmHMS;
    private long startTime=0;

    private MyLogger(){
        sdmYMD=new SimpleDateFormat("yyyy.MM.dd");
        sdmHMS=new SimpleDateFormat("HH:mm:ss:SSS");
        outputFileTime=sdmYMD.format(new Date())+"_"+sdmHMS.format(new Date());

        outputDir = Environment.getExternalStorageDirectory().getPath() + "/"+OUTPUT_DIR;
        File outputFile = new File(outputDir);
        if (!outputFile.exists()) {
            outputFile.mkdir();
        }
    }

    public static MyLogger getInstance(){
        return myLogger;
    }
    public void writeFile(String rawData,String dataKey){
        if (rawData==null){
            return;
        }
        File file = new File(outputDir+"/"+outputFileTime+dataKey+".csv");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            if(dataKey.equals("raw.ecg")){
                String[] list = rawData.split(CommonConsts.COMMA, -1);
                String[] ecgList = list[1].split(CommonConsts.COLON, -1);
                long time_stamp=Long.parseLong(list[0]);
                String output_str=sdmYMD.format(new Date(time_stamp))+","+sdmHMS.format(new Date(time_stamp));
                for(int i = 0; i < ecgList.length; i++) {
                    bw.write(output_str+","+String.valueOf(time_stamp+5*i)+","+ecgList[i]+"\n");
                    bw.flush();
                }
            }else if (dataKey.equals("raw.rri")){
                String[] list = rawData.split(CommonConsts.COMMA, -1);
                long time_stamp=Long.parseLong(list[0]);
                bw.write(sdmYMD.format(new Date(time_stamp)) + "," + sdmHMS.format(new Date(time_stamp)) + "," + list[0] + "," + list[1] + "\n");
                bw.flush();
                bw.close();
            }else if (dataKey.equals("ba.interpolated_rri")){
                String[] list = rawData.split(CommonConsts.COMMA, -1);
                long time_stamp=Long.parseLong(list[0]);
                if (startTime==0){
                    startTime=Long.parseLong(list[0]);
                }
                long time=time_stamp-startTime;
                bw.write(sdmYMD.format(new Date(time_stamp)) + "," + sdmHMS.format(new Date(time_stamp)) + "," + list[0] + "," + String.valueOf(time) + "," + list[1] + "\n");
                bw.flush();
                bw.close();
            }else if (dataKey.equals("ba.cleaned_rri")){
            String[] list = rawData.split(CommonConsts.COMMA, -1);
            long time_stamp=Long.parseLong(list[0]);
            bw.write(sdmYMD.format(new Date(time_stamp)) + "," + sdmHMS.format(new Date(time_stamp)) + "," + list[0] + "," + list[1] + "\n");
            bw.flush();
            bw.close();
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//public class MyLogger {
//    public String OUTPUT_DIR="hitoe_output";
//    public String output_filename="out.csv";
//    String outputDir;
//    SimpleDateFormat sdmYMD,sdmHMS;
//
//    MyLogger(String type){
//        sdmYMD=new SimpleDateFormat("yyyy.MM.dd");
//        sdmHMS=new SimpleDateFormat("HH:mm:ss:SSS");
//        output_filename=sdmYMD.format(new Date())+"_"+sdmHMS.format(new Date())+"_"+type+".csv";
//
//        outputDir = Environment.getExternalStorageDirectory().getPath() + "/"+OUTPUT_DIR;
//        File outputFile = new File(outputDir);
//        if (!outputFile.exists()) {
//            outputFile.mkdir();
//        }
//    }
//    public void writeFile(String rawData,String dataKey){
//        if (rawData==null){
//            return;
//        }
//        File file = new File(outputDir+"/"+output_filename);
//        FileOutputStream fos;
//        try {
//            fos = new FileOutputStream(file, true);
//            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
//            BufferedWriter bw = new BufferedWriter(osw);
//            if(dataKey.equals("raw.ecg")){
//                String[] list = rawData.split(CommonConsts.COMMA, -1);
//                String[] ecgList = list[1].split(CommonConsts.COLON, -1);
//                long time_stamp=Long.parseLong(list[0]);
//                String output_str=sdmYMD.format(new Date())+","+sdmHMS.format(new Date());
//                for(int i = 0; i < ecgList.length; i++) {
//                    bw.write(output_str+","+String.valueOf(time_stamp+5*i)+","+ecgList[i]+"\n");
//                    bw.flush();
//                }
//            }else if (dataKey.equals("raw.rri")){
//                String[] list = rawData.split(CommonConsts.COMMA, -1);
//                bw.write(sdmYMD.format(new Date()) + "," + sdmHMS.format(new Date()) + "," + list[0] + "," + list[1] + "\n");
//                bw.flush();
//                bw.close();
//            }else if (dataKey.equals("ba.interpolated_rri")){
//                String[] list = rawData.split(CommonConsts.COMMA, -1);
//                bw.write(sdmYMD.format(new Date()) + "," + sdmHMS.format(new Date()) + "," + list[0] + "," + list[1] + "\n");
//                bw.flush();
//                bw.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}

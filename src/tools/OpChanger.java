package tools;

import constants.ServerConstants;

import java.io.*;

/**
 * Created by Tim on 12/3/2016.
 */
public class OpChanger {

    public static void main(String[] args){

        String fromChange = "RESULT_CHECK_NAME";
        String toChange = "NULL";
        int change = 7;
        boolean isAfterFromChange = false;
        boolean send = true;
        String opType = send ? "send" : "recv";

        File input = new File("D:\\MapleDev\\LucidMS\\"+opType+"opsNew.properties");
        File output = new File("D:\\MapleDev\\LucidMS\\"+opType+"opsNew2.properties");
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            br = new BufferedReader(new FileReader(input));
            pw = new PrintWriter(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String newLine = "";
            while (newLine != null) {
                newLine = br.readLine();
                if(newLine == null){
                    break;
                }
                if (newLine.length() <= 0 || newLine.charAt(0) == '#') {
                    pw.write(newLine + "\n");
                    continue;
                }
                String[] lineSplit = newLine.split("#");
                String actualInfo = lineSplit[0].replace(" ", "");
                String comment = null;
                if(lineSplit.length > 1) {
                    comment = lineSplit[1];
                }
                String[] infoArray = actualInfo.split("=");
                String op = infoArray[0];
                if(op.equalsIgnoreCase(fromChange)){
                    isAfterFromChange = true;
                }else if(op.equalsIgnoreCase(toChange)){
                    return;
                }
                int val = Integer.valueOf(infoArray[1].replace("0x",""),16);
                if(isAfterFromChange && val < 0x999) {
                    val += change;
                    if (pw != null) {
                        String added = "";
                        if (comment != null) {
                            if (comment.contains("| Old comment")) {
                                added = " |" + comment.split("|")[1];
                            } else if(!comment.contains("BOT:")){
                                added = " | Old comment: #" + comment;
                            }
                        }
                        pw.print(op + " = 0x" + Integer.toHexString(val).toUpperCase() + " # BOT: Old = 0x" +
                                Integer.toHexString(val - change).toUpperCase() + "(+" + change +" for v" +
                                ServerConstants.MAPLE_VERSION + "." + ServerConstants.MAPLE_PATCH + ")"+ added + "\n");
                    }
                }else{
                    pw.write(newLine + "\n");
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            pw.close();
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


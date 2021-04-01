package com.csw.demo.service;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Locale;

@Service
public class JSONToXMLService {
    public void readFile(String jsonPath, String xmlPath)  {
        try {
            File file = new File(jsonPath);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st ;
            StringBuffer strBuffer = new StringBuffer();
            while ((st = br.readLine()) != null) {
                strBuffer.append(st);
            }
            try {
                File file1 = new File(xmlPath);
                file1.createNewFile();
                FileWriter output = new FileWriter(file1);
                output.write(checkDataType(strBuffer.toString()).toString());
                output.close();
                System.out.println("Your xml is ready in this path: " + xmlPath);
            } catch(Exception e){
                System.out.println("Oops!..Please provide valid file path");
            }
        } catch(Exception e) {
            System.out.println("Invalid file input . Please check once again.");
            e.printStackTrace();
        }
    }

    public StringBuffer checkDataType(String st){
        StringBuffer arrBuffer = new StringBuffer();
        if(st.charAt(0) == '-') {
            arrBuffer.append(singleValueConversion("Integer", st.toString()));
        } else if((int) st.charAt(0) >=48 && (int) st.charAt(0) <=57) {
            arrBuffer.append(singleValueConversion("Integer", st.toString()));
        } else if(st.equalsIgnoreCase("null")){
            arrBuffer.append(singleValueConversion("Null", st.toString()));
        } else if(st.equalsIgnoreCase("true") || st.equalsIgnoreCase("false")) {
            arrBuffer.append(singleValueConversion("Boolean", st.toString()));
        } else if(((char) st.charAt(0)>=65 && (char) st.charAt(0)<=90) ||
                ((char) st.charAt(0)>=97 && (char) st.charAt(0)<=122)){
            arrBuffer.append(singleValueConversion("String", st.toString()));
        }  else if(st.charAt(0) == '['){
            JSONArray arr = new JSONArray(st);
            System.out.println(arr.length());
            arrBuffer.append("<array>");
            for(int i = 0;i<arr.length();i++){
                arrBuffer.append(checkDataType(arr.get(i).toString()));
            }
            arrBuffer.append("</array>");
        } else {
            JSONObject obj = new JSONObject(st);
            return constructXML(obj, arrBuffer);
        }
        return arrBuffer;
    }

    public StringBuffer constructXML(JSONObject obj, StringBuffer strBuff){
        Iterator<?> keys = obj.keys();
        strBuff.append("<object>");
            while(keys.hasNext()){
            String key = (String) keys.next();
            if(obj.get(key) instanceof JSONObject){
                strBuff.append("<object name=" + key + ">");
                constructXML((JSONObject) obj.get(key), strBuff);
                strBuff.append("</object>");
            }
            if(obj.get(key) instanceof Integer) {
                strBuff.append(constructDataTypeTags("Integer", obj.get(key).toString(),key));
            } else if(obj.get(key) instanceof String) {
                strBuff.append(constructDataTypeTags("String", obj.get(key).toString(), key));
            } else if(obj.get(key) instanceof Double) {
                strBuff.append(constructDataTypeTags("Float", obj.get(key).toString(), key));
            } else if(obj.get(key) instanceof Boolean) {
                strBuff.append(constructDataTypeTags("Boolean", obj.get(key).toString(),key));
            } else if(obj.get(key) instanceof JSONArray) {
                JSONArray arr = (JSONArray) obj.get(key);
                strBuff.append("<array name=" + key + ">");
                for(int i = 0;i<arr.length();i++){
                    strBuff.append(checkDataType(arr.get(i).toString()));
                }
                strBuff.append("</array>");
            } else {
                strBuff.append(constructDataTypeTags("Null", null, key));
            }
        }
        strBuff.append("</object>");
        return strBuff;
    }
    public StringBuffer constructDataTypeTags(String dataType, String value, String key){
            if(dataType.equalsIgnoreCase("Integer")) {
                return new StringBuffer("<number name=" + key +">" + value + " </number>");
            } else if(dataType.equalsIgnoreCase("String")) {
                return new StringBuffer("<string name=" + key +">" + value + "</string>");
            } else if(dataType.equalsIgnoreCase("Float")) {
                return new StringBuffer("<float name=" + key +">" + value + "</float>");
            } else if(dataType.equalsIgnoreCase("Boolean")) {
                return new StringBuffer("<boolean name=" + key +">" + value + "</boolean>");
            } else {
                return new StringBuffer("<null name=" + key + "/>");
            }
    }
    public StringBuffer singleValueConversion(String dataType, String value) {
        if(dataType.equalsIgnoreCase("Integer")) {
            return new StringBuffer("<number>" + value + "</number>");
        } else if(dataType.equalsIgnoreCase("String")) {
            return new StringBuffer("<string>" + value + "</string>");
        } else if(dataType.equalsIgnoreCase("Float")) {
            return new StringBuffer("<float>" + value + "</float>");
        } else if(dataType.equalsIgnoreCase("Boolean")) {
            return new StringBuffer("<boolean>" + value + "</boolean>");
        } else {
            return new StringBuffer("</null>");
        }
    }
}

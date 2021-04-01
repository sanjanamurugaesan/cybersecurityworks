package com.csw.demo.factory;


import com.csw.demo.converter.XMLJSONConverter;
import com.csw.demo.service.JSONToXMLService;

public class ConverterFactory implements XMLJSONConverter{

    @Override
    public void convertJSONToXML(String jsonPath, String xmlPath) {
        JSONToXMLService service = new JSONToXMLService();
        service.readFile(jsonPath, xmlPath);
    }
}

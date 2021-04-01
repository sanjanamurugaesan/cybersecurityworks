package com.csw.demo;

import com.csw.demo.factory.ConverterFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("Enter your json path: ") ;
        Scanner in1 = new Scanner(System.in);
        System.out.println("Enter your xml path: ") ;
        Scanner in2 = new Scanner(System.in);
        String jsonPath = in1.nextLine();
        String xmlPath = in2.nextLine();
        ConverterFactory converter = new ConverterFactory();
        converter.convertJSONToXML(jsonPath, xmlPath);
    }

}

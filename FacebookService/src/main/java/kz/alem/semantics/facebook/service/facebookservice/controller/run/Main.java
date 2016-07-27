package kz.alem.semantics.facebook.service.facebookservice.controller.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Zhasan
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {kz.alem.semantics.facebook.service.facebookservice.controller.FeedController.class})
public class Main {

    public static void main(String args[]) {
          SpringApplication.run(Main.class, args);
    }

}

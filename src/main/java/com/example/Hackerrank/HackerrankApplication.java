package com.example.Hackerrank;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import services.CalculateGoalService;

@SpringBootApplication
public class HackerrankApplication {


    public static void main(String[] args) {

        SpringApplication.run(HackerrankApplication.class, args);
        final String COMPETITION = "UEFA Champions League";
        int YEAR = 2011;

        System.out.println("RTA FINAL: " + CalculateGoalService.getWinnerTotalGoals(COMPETITION, YEAR));

    }

}

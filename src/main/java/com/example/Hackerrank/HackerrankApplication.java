package com.example.Hackerrank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.Hackerrank.services.CalculateGoalService;

@SpringBootApplication
public class HackerrankApplication {


    public static void main(String[] args) {

        SpringApplication.run(HackerrankApplication.class, args);
        final String COMPETITION = "UEFA Champions League";
        int YEAR = 2011;

        System.out.println("RTA FINAL: " + CalculateGoalService.getWinnerTotalGoals(COMPETITION, YEAR));

    }

}

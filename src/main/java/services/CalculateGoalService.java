package services;

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

public class CalculateGoalService {

    public static int getWinnerTotalGoals(String competition, int year) {
        final String COMPETITION = getChangedString(competition);
        String winner = getWinnerTeam(COMPETITION, year);
        int team1Goals = getTotalGoals(winner, COMPETITION, year, "team1");
        int team2Goals = getTotalGoals(winner, COMPETITION, year, "team2");

        return team1Goals + team2Goals;
    }

    public static JsonObject getRequest(String url) {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                return JsonParser.parseString(responseBody).getAsJsonObject();
            } else {
                System.err.println("[getRequest] Error, api response status is: {}" + statusCode + "but expect: {}" + HttpStatus.SC_OK);
            }

        } catch (Exception e) {
            System.out.println("[getRequest] Problem get and process data" + e);
        }
        return null;
    }

    public static String getWinnerTeam(String competitionName, int competitionYear) {

        String url = "https://jsonmock.hackerrank.com/api/football_competitions?name=" + competitionName + "&year=" + competitionYear;
        JsonObject jsonObject = getRequest(url);
        if (jsonObject != null) {
            JsonArray dataArray = jsonObject.getAsJsonArray("data");
            JsonObject dataObject = dataArray.get(0).getAsJsonObject();
            return dataObject.get("winner").getAsString();

        } else {
            System.out.println("[getWinnerTeam] Error getting winners. The request body is null");
        }
        return null;
    }

    public static int getTotalGoals(String winner, String competition, int year, String team) {
        int totalGoals = 0;
        String url = "https://jsonmock.hackerrank.com/api/football_matches?competition=" + competition + "&year=" + year + "&" + team + "=" + winner + "&page=" + 1;

        JsonObject request = getRequest(url);
        if (request != null) {
            int totalPages = request.get("total_pages").getAsInt();
            JsonArray goalsData = request.getAsJsonArray("data");
            totalGoals = getGoalsInArray(goalsData, team + "goals");

            for (int i = 2; i <= totalPages; i++) {
                String urlOtherPage = "https://jsonmock.hackerrank.com/api/football_matches?competition=" + competition + "&year=" + year + "&" + team + "=" + winner + "&page=" + i;
                JsonObject requestBody = getRequest(urlOtherPage);
                if (requestBody != null) {
                    JsonArray goalsList = requestBody.getAsJsonArray("data");
                    totalGoals += getGoalsInArray(goalsList, team + "goals");
                } else {
                    System.out.println("[getWinnerTeam] Error getting goals. The request body is null");
                }
            }
            System.out.println("team" + team + "goals" + totalGoals);
            return totalGoals;

        } else {
            System.out.println("[getWinnerTeam] Error getting goals. The request body is null");
        }
        return 0;
    }

    public static int getGoalsInArray(JsonArray goalsList, String team) {
        int teamGoals = 0;
        for (JsonElement goal : goalsList) {
            JsonObject matchObject = goal.getAsJsonObject();
            teamGoals += matchObject.get(team).getAsInt();
        }
        return teamGoals;
    }

    public static String getChangedString(String competition) {
        return competition.replace(" ", "%20");
    }

}

package ohtu;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.util.List;
import org.apache.http.client.fluent.Request;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {

    public static void main(String[] args) throws IOException {
        // vaihda oma opiskelijanumerosi seuraavaan, ÄLÄ kuitenkaan laita githubiin omaa opiskelijanumeroasi
        String studentNr = "011120775";
        if (args.length > 0) {
            studentNr = args[0];
        }

        String url = "https://studies.cs.helsinki.fi/ohtustats/students/" + studentNr + "/submissions";
        String kurssiUrl = "https://studies.cs.helsinki.fi/ohtustats/courseinfo";
        String kurssiStatsUrl = "https://studies.cs.helsinki.fi/ohtustats/stats";

        String bodyText = Request.Get(url).execute().returnContent().asString();
        String courseText = Request.Get(kurssiUrl).execute().returnContent().asString();

        System.out.println("json-muotoinen data:");
        System.out.println(courseText);
        System.out.println(bodyText);

        Gson mapper = new Gson();
        Submission[] subs = mapper.fromJson(bodyText, Submission[].class);
        Course course = mapper.fromJson(courseText, Course.class);

        String statsResponse = Request.Get(kurssiStatsUrl).execute().returnContent().asString();
        JsonParser parser = new JsonParser();
        JsonObject parsittuData = parser.parse(statsResponse).getAsJsonObject();
        JsonObject parsittu1 = parsittuData.getAsJsonObject("1");
        JsonObject parsittu2 = parsittuData.getAsJsonObject("2");
        JsonObject parsittu3 = parsittuData.getAsJsonObject("3");
        int students = parsittu1.get("students").getAsInt()
                + parsittu2.get("students").getAsInt()
                + parsittu3.get("students").getAsInt();
        int exercises = parsittu1.get("exercise_total").getAsInt() +
                parsittu2.get("exercise_total").getAsInt() +
                parsittu3.get("exercise_total").getAsInt();

        System.out.println(
                "Kurssi: " + course.getName() + ", " + course.getTerm() + "\n\n"
                + "Opiskelijanumero: " + studentNr + "\n"
        );
        for (Submission submission : subs) {
            System.out.print(
                    "viikko " + submission.getWeek() + ":\n"
                    + "   " + "tehtyjä tehtäviä yhteensä: "
                    + submission.getExercises().length
                    + " (maksimi "
                    + course.getExercises()[submission.getWeek() - 1]
                    + ") ," + "aikaa kului " + submission.getHours()
                    + " tuntia, tehdyt tehtävät: "
            );
            for (Integer number : submission.getExercises()) {
                System.out.print(number + " ");
            }
            System.out.println();

        }

        System.out.println("kurssilla yhteensä " + students + " palautusta,"
                + " palautettuja tehtäviä " + exercises + " kpl");

    }
}

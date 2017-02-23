import com.opencsv.CSVWriter;
import com.robrua.orianna.api.core.RiotAPI;
import com.robrua.orianna.type.core.common.Region;
import com.robrua.orianna.type.core.game.Game;
import com.robrua.orianna.type.core.game.RawStats;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import joinery.DataFrame;

public class DAO 
{
    // Creating the teams
    public Collection<Team> getTeams()
    {
        Collection<Team> teams = new LinkedList<>();
        
        Collection<Player> H2Kplayers = new LinkedList<>();
        Collection<Player> G2players = new LinkedList<>();
        Collection<Player> UOLplayers = new LinkedList<>();
        
        H2Kplayers.add(new Player("Odoamne", "Odoamne", "top", "Romania"));
        H2Kplayers.add(new Player("Jankos", "H2K Jankos", "jun", "Poland"));
        H2Kplayers.add(new Player("Febiven", "Cant Change It", "mid", "Netherlands"));
        H2Kplayers.add(new Player("Nuclear", "NuclearIsBadBoy", "adc", "Korea"));
        H2Kplayers.add(new Player("Chei", "mataofeurope", "sup", "Korea"));
        
        G2players.add(new Player("Expect", "ExDpect", "top", "Korea"));
        G2players.add(new Player("Trick", "Healthy Life", "jun", "Korea"));
        G2players.add(new Player("Perkz", "goodplayer98", "mid", "Croatia"));
        G2players.add(new Player("Zven", "G2 ZV3N", "adc", "Denmark"));
        G2players.add(new Player("Mithy", "mithypote", "sup", "Spain"));
        
        UOLplayers.add(new Player("Vizicsacsi", "UOL Vizicsacsi", "top", "Hungary"));
        UOLplayers.add(new Player("Xerxe", "Xerxe", "jun", "Romania"));
        UOLplayers.add(new Player("Exileh", "Exileh", "mid", "Germany"));
        UOLplayers.add(new Player("Samux", "xumäS", "adc", "Spain"));
        UOLplayers.add(new Player("Hylissang", "UOL Hylissang", "sup", "Bulgaria"));

        teams.add(new Team("H2K", "H2K", "United Kingdom", "euw", H2Kplayers));
        teams.add(new Team("G2", "G2 Esports", "Spain", "euw", G2players));
        teams.add(new Team("UOL", "Unicorns of Love", "Germany", "euw", UOLplayers));
        
        return teams;
    }
    
    // Remember to change the path of the output file
    // The data here is being retrieved directly from the Riot Games API.
    // Orianna API is used because it's way more clear to read and to understand than raw API calls with JSON responses.
    // It is also used Open CSV to create the CSV files in an easy way
    public void getRecentGamesGamesCSV(Collection<Team> teams) throws IOException
    {        
        CSVWriter writer = new CSVWriter(new FileWriter("/Output/Path/Here"), ';', Character.MIN_VALUE);
        String[] header = "player#team#country#lane#ngame#kda#kills#deaths#assists#gold#cs#wards#ttldmgchamps".split("#");
        writer.writeNext(header);
        for(Team team: teams)
        {
            for(Player player: team.getPlayers())
            {
                RiotAPI.setRegion(Region.valueOf(team.getRegion().toUpperCase()));
                List<Game> recentGames = RiotAPI.getRecentGames(player.getSumName());
                int count = 0;
                for(Game game: recentGames)
                {
                	// Using a counter here because the matches do not have to have the same date
                    count++;
                    RawStats stats = game.getStats();
                    float kills = stats.getKills();
                    float assists = stats.getAssists();
                    float deaths = stats.getDeaths();
                    float kda = (kills + assists) / (deaths == 0 ? 1: deaths);
                    
                    String row = "";
                    row = row + player.getName() + "#" + team.getName() + "#" + 
                          team.getCountry() + "#" + player.getLane() + "#" + count + "#" + String.format("%.2f", kda) + "#" +
                          stats.getKills() + "#" + stats.getDeaths()  + "#" + stats.getAssists()  + "#" + 
                          stats.getGoldEarned() + "#" + stats.getMinionsKilled() + "#" + stats.getWardsPlaced() + "#" + stats.getTotalDamageDealtToChampions();
                    writer.writeNext(row.split("#"));                    
                }
            }
        }
        writer.close();
    }
    
    // Remember to change the path of the output file
    // The data that is being read here is data that can't be retrieved directly from the API.
    // You need to know the match history link because there is the information that will allow for calls to be answered
    // In the CSV files that we read as dataframes are the links to the API call that will return the match data in JSON
    // Remember, these are not standard API calls and because of that there is no API Key needed and no developer involved.
    // Here as well Open CSV is used to create the CSV files.
    // Joinery is a library that allows developers to work with dataframes in Java. It is used to read de CSVs containing the URLs of the API calls and to transform columns into lists.
    //It has been used for future purposes too because that CSV files contain more info that could result interesting sooner or later and reading that from a dataframe is way easier.
    public void getLCSGamesCSV(Collection<Team> teams) throws IOException
    {
        // Creating the writer and writing the header first
        CSVWriter writer = new CSVWriter(new FileWriter("/Output/Path/Here"), ';', Character.MIN_VALUE);
        String[] header = "player#team#country#lane#date#kda#kills#deaths#assists#gold#cs#wards#ttldmgchamps".split("#");
        writer.writeNext(header);
        
        // Read as a dataframe the CSVs containing the link to the data of the matches in JSON and more info
        DataFrame dfH2K = DataFrame.readCsv("../csv/H2K.csv");
        DataFrame dfG2 = DataFrame.readCsv("../csv/G2.csv");
        DataFrame dfUOL = DataFrame.readCsv("../csv/UOL.csv");
        
        // Getting all the links to the data into a collection we will iterate later
        List<String> gameLinksH2K = dfH2K.col("Match data");
        List<String> gameLinksG2 = dfG2.col("Match data");
        List<String> gameLinksUOL = dfUOL.col("Match data");
        List<List<String>> allGameLinks = new ArrayList<>();
        allGameLinks.add(gameLinksH2K);
        allGameLinks.add(gameLinksG2);
        allGameLinks.add(gameLinksUOL);
        
        BufferedReader rd = null;
        Collection<Long> gameIdsRead = new ArrayList<>();
        
        // For every link read the JSON that it returns
        for(List<String> links: allGameLinks)
        {
            for(String link: links)
            {
                URL url = new URL(link);
                rd = new BufferedReader(new InputStreamReader(url.openStream()));

                //  Reading the response
                StringBuilder result = new StringBuilder();
                String line = "";
                while ((line = rd.readLine()) != null) 
                {
                        result.append(line);
                }

                JsonReader reader = Json.createReader( new StringReader(result.toString()) );
                JsonObject object = reader.readObject();
                reader.close();

                // Getting the participants and participants IDs arrays
                JsonArray participants = object.getJsonArray("participants");
                JsonArray participantsIDs = object.getJsonArray("participantIdentities");
                List<JsonObject> partIds = participantsIDs.getValuesAs(JsonObject.class);
                
                // Whenever a game ID is repeated it means we already stored data from one team so we have to store the data from the other one now.
                //The games can only be repeated once.
                boolean repeated = false;
                Long gameId = Long.parseLong(object.getJsonNumber("gameId").toString());
                if(!gameIdsRead.contains(gameId))
                    gameIdsRead.add( gameId );
                else
                    repeated = true;
                
                // Getting the game creation date because its easier than counting the game number (also the teams haven't played the same amount of matches) and it also gives more information
                JsonNumber gameCreation = object.getJsonNumber("gameCreation");
                Date gameDate = new Date(gameCreation.longValue());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                
                // That counter is used when a match isn't repeated. It counts the number of participants and when it hits 5 then we are done with reading data, we already have the team
                int partCounter = 1;

                // Iterating trhough the participants of the game
                for(JsonObject participant: participants.getValuesAs(JsonObject.class))
                {
                    JsonNumber partId = participant.getJsonNumber("participantId");
                    JsonObject partIdInfo = partIds.get(partId.intValue() - 1);
                    JsonObject player = partIdInfo.getJsonObject("player");
                    String name = player.getString("summonerName");

                    // Quickly know whats the team's name by lookin into the summoner name
                    String team = name.contains("H2K") ? "H2K": name.contains("UOL") ? "UOL" : "G2";

                    // Quickly know whats the country by lookin into the summoner name
                    String country = name.contains("H2K") ? "United Kingdom": name.contains("UOL") ? "Germany" : "Spain";
                    
                    // Check if the game is a repeated one (it can be repeated because the teams can compete against each other)
                    if(!repeated)
                    {
                        // Check if the participant is from one of our teams and if so just get
                        if(name.contains("H2K") || name.contains("UOL") || name.contains("G2") && partCounter <= 5)
                        {
                            partCounter++;
                            JsonObject stats = participant.getJsonObject("stats");
                            JsonObject timeline = participant.getJsonObject("timeline");
                            float kills = Float.parseFloat(stats.getJsonNumber("kills").toString());
                            float deaths = Float.parseFloat(stats.getJsonNumber("deaths").toString());
                            float assists = Float.parseFloat(stats.getJsonNumber("assists").toString());
                            float kda = (kills + assists) / (deaths == 0 ? 1: deaths);

                            // Put all the data into a String separated with whatever character you want
                            String row = name + "#" + team + "#" + country + "#" + timeline.getString("lane") + "#" + dateFormat.format(gameDate) + "#" +
                                         String.format("%.2f#%.0f#%.0f#%.0f", kda, kills, deaths, assists) + "#" +
                                         stats.getJsonNumber("goldEarned") + "#" + stats.getJsonNumber("totalMinionsKilled") + "#" + 
                                         stats.getJsonNumber("wardsPlaced") + "#" + stats.getJsonNumber("totalDamageDealtToChampions");
                            
                            // Add that string as a new row to the file
                            writer.writeNext(row.split("#"));
                        }
                    }else
                    {
                        // If the game is repeated check if the participant ID is greater than 5. If so that would mean that it is one of the participants of the team we want
                        if(name.contains("H2K") || name.contains("UOL") || name.contains("G2") && partId.intValue() > 5)
                        {
                            JsonObject stats = participant.getJsonObject("stats");
                            JsonObject timeline = participant.getJsonObject("timeline");
                            float kills = Float.parseFloat(stats.getJsonNumber("kills").toString());
                            float deaths = Float.parseFloat(stats.getJsonNumber("deaths").toString());
                            float assists = Float.parseFloat(stats.getJsonNumber("assists").toString());
                            float kda = (kills + assists) / (deaths == 0 ? 1: deaths);

                            // Put all the data into a String separated with whatever character you want
                            String row = name + "#" + team + "#" + country + "#" + timeline.getString("lane") + "#" + dateFormat.format(gameDate) + "#" +
                                         String.format("%.2f#%.0f#%.0f#%.0f", kda, kills, deaths, assists) + "#" +
                                         stats.getJsonNumber("goldEarned") + "#" + stats.getJsonNumber("totalMinionsKilled") + "#" + 
                                         stats.getJsonNumber("wardsPlaced") + "#" + stats.getJsonNumber("totalDamageDealtToChampions");
                            
                            // Add that string as a new row to the file
                            writer.writeNext(row.split("#"));
                        }
                    }
                }
            }
        }
        
        writer.close();
    }
}

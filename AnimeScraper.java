package anime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.*;

public class AnimeScraper {
	private final static String API_URL = "https://api.amvstr.ml/api/v2";
	private static final Charset UTF_8 = Charset.forName("UTF-8");

	private static int getAnimeId(String query) {
		try {
			URL url = new URL(API_URL + "/search?q=" + query + "&limit=1");
			StringBuilder builder = new StringBuilder();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), UTF_8));
			String str;
			while ((str = bufferedReader.readLine()) != null) {
				builder.append(str);
			}
			JSONObject jsonData = new JSONObject(builder.toString());
			JSONObject results = jsonData.getJSONArray("results").getJSONObject(0);
			return (results.getInt("id"));
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	private static String getEpisodeId(int animeId, int episode) {
		try {
			URL url = new URL(API_URL + "/episode/" + animeId);
			StringBuilder builder = new StringBuilder();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), UTF_8));
			String str;
			while ((str = bufferedReader.readLine()) != null) {
				builder.append(str);
			}
			JSONArray episodesArr = new JSONArray(builder.toString());
			for (int i = 0; i < episodesArr.length(); i++) {
				if (episodesArr.getJSONObject(i).getInt("number") == episode) {
					return (episodesArr.getJSONObject(i).getString("id"));
				}
			}
			return (null);
		} catch (IOException e) {
			e.printStackTrace();
			return (null);
		}
	}
	
	public static String getStreamUrl(String episodeId) {
		try {
			URL url = new URL(API_URL + "/stream?id=" + episodeId);
			StringBuilder builder = new StringBuilder();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), UTF_8));
			String str;
			while ((str = bufferedReader.readLine()) != null) {
				builder.append(str);
			}
			JSONObject jsonData = new JSONObject(builder.toString());
			return (jsonData.getJSONObject("stream").getJSONObject("multi").getJSONObject("main").getString("url"));
		} catch (IOException e) {
			e.printStackTrace();
			return ("An error occured.");
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getStreamUrl(getEpisodeId(getAnimeId("demon"), 1)));
	}
}

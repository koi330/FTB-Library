package latmod.lib.github;

import latmod.lib.net.*;

import com.google.gson.JsonElement;

/**
 * Created by LatvianModder on 16.03.2016.
 */
public class GitHubAPI {

    public static final String RAW_CONTENT = "https://raw.githubusercontent.com/";
    public static final String API_PATH = "https://api.github.com/";

    public static JsonElement getAPI(String api) throws Exception {
        return new LMURLConnection(RequestMethod.GET, API_PATH + api).connect().asJson();
    }
}

package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    // Task: pick appropriate instance variables for this class
    private Map<String, Integer> countryToIndex = new HashMap<String, Integer>();
    private JSONArray jsonArray;
    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */

    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            this.jsonArray = new JSONArray(jsonString);

            // Task: use the data in the jsonArray to populate your instance variables
            //            Note: this will likely be one of the most substantial pieces of code you write in this lab.
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String country = jsonObject.getString("alpha3");
                countryToIndex.put(country.toUpperCase(), i);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        // Task: return an appropriate list of language codes,
        //            but make sure there is no aliasing to a mutable object
        JSONObject jsonObject = jsonArray.getJSONObject(countryToIndex.get(country.toUpperCase()));
        ArrayList<String> languages = new ArrayList<>();
        for (String lanCode: jsonObject.keySet()) {
            if (!"alpha3".equals(lanCode) && !"alpha2".equals(lanCode) && !"id".equals(lanCode)) {
                languages.add(lanCode);
            }
        }
        return languages;
    }

    @Override
    public List<String> getCountries() {
        // Task: return an appropriate list of country codes,
        //            but make sure there is no aliasing to a mutable object
        List<String> countries = new ArrayList<>(countryToIndex.keySet());
        return countries;
    }

    @Override
    public String translate(String country, String language) {
        // Task: complete this method using your instance variables as needed
        JSONObject jsonObject = jsonArray.getJSONObject(countryToIndex.get(country.toUpperCase()));
        return jsonObject.getString(language);
    }
}

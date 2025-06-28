#!/usr/bin/groovy

/*
 * This groovy script checks for the latest truthy and lexemes download from Wikidata.
 * Outputs the result as JSON using Google's Gson library.
 */

@Grab(group='org.jsoup', module='jsoup', version='1.16.1')
@Grab(group='com.google.code.gson', module='gson', version='2.10.1')

import java.lang.Long

import org.jsoup.Jsoup
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonArray
import com.google.gson.JsonNull

/**
 * Finds the latest Wikidata dump URL for a given base name (e.g., "truthy-BETA", "lexemes-BETA")
 * @param baseName The base file type to look for
 * @return A map with 'date' and 'url' keys, or null if not found
 */
def findLatestDump(String baseName) {
    def baseUrl = 'https://dumps.wikimedia.org/wikidatawiki/entities/'
    def indexPage = Jsoup.connect(baseUrl).get()
    def result = JsonNull.INSTANCE

    // Get all date directories, reverse chronological order
    def dateDirs = indexPage.select("a[href~=[0-9]{8}/]")
        .collect { it.text().replace("/", "") }
        .findAll { it ==~ /\d{8}/ }
        .sort().reverse()

    for (date in dateDirs) {
        def subUrl = "${baseUrl}${date}/"
        def subPage = Jsoup.connect(subUrl).get()

        def expectedFile = "wikidata-${date}-${baseName}.nt.bz2"
        def found = subPage.select("a[href=${expectedFile}]")

        if (!found.isEmpty()) {
            JsonObject r = new JsonObject()
	    r.addProperty("date", Long.parseLong(date))
            r.addProperty("url", "${subUrl}${expectedFile}")
	    result = r
	    break
        }
    }

    return result
}

// Base dump types to check
def baseNames = ["truthy-BETA", "lexemes-BETA"]

// Collect results in a json object
def jsonObject = new JsonObject()
baseNames.each { baseName ->
    def entry = findLatestDump(baseName)
    def releases = new JsonArray()
    releases.add(entry);
    jsonObject.add(baseName, releases)
}

// Output as pretty-printed JSON
def gson = new GsonBuilder().setPrettyPrinting().create()
println gson.toJson(jsonObject)


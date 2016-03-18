package com.nitt.cse.selectdictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rb on 21/2/16.
 */
public class DictionaryItem {
    private String description;
    private List<String> synonyms;
    private List<String> type;
    private List<String> typeof;
    private String word;

    public DictionaryItem() {
        description="";
        word="";
        synonyms = new ArrayList<>();
        type = new ArrayList<>();
        typeof = new ArrayList<>();
    }
    public DictionaryItem(String word,String description) {
        this.word = word;
        this.description = description;
        this.synonyms = new ArrayList<>();
        this.type = new ArrayList<>();
        this.typeof = new ArrayList<>();
    }
    public DictionaryItem(String word,String description,List<String> synonyms) {
        this.word = word;
        this.description = description;
        this.synonyms = synonyms;
        this.type = new ArrayList<>();
        this.typeof = new ArrayList<>();
    }
    public DictionaryItem(String word,String description
                            , List<String> synonyms, List<String> type) {
        this.word = word;
        this.description = description;
        this.synonyms = synonyms;
        this.type = type;
        this.typeof = new ArrayList<>();
    }
    public DictionaryItem(String word,String description
                    , List<String> synonyms, List<String> type, List<String> typeof) {
        this.word = word;
        this.description = description;
        this.synonyms = synonyms;
        this.type = type;
        this.typeof = typeof;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<String> getTypeof() {
        return typeof;
    }

    public void setTypeof(List<String> typeof) {
        this.typeof = typeof;
    }

    @Override
    public String toString() {
        super.toString();
        String returnString = "Word : "+getWord()+"\n"
                              +"Description : "+getDescription()+"\n"
                              +"Synonyms : "+getSynonyms()+"\n"
                              +"Types : "+getType()+"\n"
                              +"Type of : "+getTypeof()+"\n";
        return returnString;
    }
}

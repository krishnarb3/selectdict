package com.nitt.cse.selectdictionary;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rb on 21/2/16.
 */
public class DictionaryLookup {
    public static List<DictionaryItem> getMeaning(List<String> words) {
        List<DictionaryItem> items = new ArrayList<>();
        for(String word:words) {
            String dictionaryURL = Utils.dictionaryURL;
            DictionaryItem item=new DictionaryItem();
            try {
                String encodedURL = URLEncoder.encode(word,"UTF-8");
                Document document = Jsoup.connect(dictionaryURL+encodedURL).get();
                Elements elementsDescription = document.getElementsByTag("meta");
                for(Element elementDescription : elementsDescription) {
                    try {
                        String property = elementDescription.attr("property");
                        if(property.equals("og:description")) {
                            String description = elementDescription.attr("content");
                            Log.d("Dict",description);
                            item = new DictionaryItem(word,description);
                        }
                    } catch (Exception e) {
                        Log.d(Utils.LOGGING,e.toString());
                    }
                }
                Elements elementsType = document.getElementsByTag("dl");
                List<String> typeofs = new ArrayList<>();
                List<String> types = new ArrayList<>();
                List<String> synonyms = new ArrayList<>();
                for(Element elementType : elementsType) {
                    if(elementType.attr("class").equals("instances")) {
                        Element tag = elementType.child(0);
                        Element dd = elementType.child(1);
                        Elements links = dd.getElementsByTag("a");
                        for(Element element : links) {
                            Log.d(Utils.LOGGING,element.text()+" - "+tag.text());
                            if(tag.text().equals("Type of:"))
                                typeofs.add(element.text());
                            else if(tag.text().equals("Types:"))
                                types.add(element.text());
                            else if(tag.text().equals("Synonyms:"))
                                synonyms.add(element.text());
                        }
                        item.setTypeof(typeofs);
                        item.setType(types);
                        item.setSynonyms(synonyms);
                    }
                }

            }catch(Exception e) {
                Log.d(Utils.LOGGING,"Error getting meaning : "+e.toString());
            }
            items.add(item);
        }
        Log.d(Utils.LOGGING,items.toString());
        return items;
    }
}

package com.nexters.paperfume.content.fragrance;

import android.content.res.AssetManager;
import android.icu.text.MessagePattern;
import android.util.Log;

import com.nexters.paperfume.enums.Feeling;
import com.nexters.paperfume.enums.PartOfDay;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

/**
 * Created by sangyeonK on 2016-08-13.
 */
public class FragranceManager {
    private static FragranceManager ourInstance = new FragranceManager();

    public static FragranceManager getInstance() {
        return ourInstance;
    }

    private FragranceManager() {

    }

    private AssetManager mAssetManager;
    private HashMap<Feeling, HashMap< PartOfDay, HashMap< String, Vector<FragranceInfo>>>> mMapFragranceInfo = new HashMap<Feeling, HashMap<PartOfDay, HashMap< String, Vector<FragranceInfo>>>>();

    public void initFragrance(AssetManager assetManager) {
        mAssetManager = assetManager;
        loadFragrance(Feeling.HAPPY, "feelings/happy.xml");
        loadFragrance(Feeling.GROOMY, "feelings/groomy.xml");
        loadFragrance(Feeling.MISS, "feelings/miss.xml");
        loadFragrance(Feeling.STIFLED, "feelings/stifled.xml");
    }

    public void loadFragrance(Feeling feeling, String feelingAssetPath) {
        try {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlFactoryObject.newPullParser();

            String[] names = mAssetManager.list("feelings");
            InputStream is = mAssetManager.open(feelingAssetPath);

            parser.setInput(is, null);
            int event = parser.getEventType();
            Feeling currentFeeling;
            PartOfDay currentPartOfDay = PartOfDay.DAY;
            while (event != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equals("day")) {
                            currentPartOfDay = PartOfDay.DAY;
                        }
                        if (tagName.equals("night")) {
                            currentPartOfDay = PartOfDay.NIGHT;
                        }
                        if (tagName.equals("fragrance")) {
                            String name = parser.getAttributeValue(null, "name");
                            String imageAssetPath = parser.getAttributeValue(null, "image_asset_path");

                            HashMap<PartOfDay,HashMap< String, Vector<FragranceInfo>>> hmap_P_SvF = mMapFragranceInfo.get(feeling);
                            if(hmap_P_SvF == null) {
                                hmap_P_SvF = new HashMap<PartOfDay,HashMap< String, Vector<FragranceInfo>>>();
                                mMapFragranceInfo.put(feeling, hmap_P_SvF);
                            }

                            HashMap< String, Vector<FragranceInfo>> hmap_S_vF = hmap_P_SvF.get(currentPartOfDay);
                            if(hmap_S_vF == null) {
                                hmap_S_vF = new HashMap< String, Vector<FragranceInfo>>();
                                hmap_P_SvF.put(currentPartOfDay, hmap_S_vF);
                            }

                            Vector<FragranceInfo> vecFragranceInfo = hmap_S_vF.get(name);
                            if(vecFragranceInfo == null) {
                                vecFragranceInfo = new Vector<FragranceInfo>();
                                hmap_S_vF.put(name,vecFragranceInfo);
                            }

                            String[] imageAssetList = mAssetManager.list(imageAssetPath);
                            for(String imageAsset : imageAssetList) {
                                FragranceInfo info = new FragranceInfo();
                                info.setName(name);
                                info.setPartOfDay(currentPartOfDay);
                                info.setImageAsset(imageAssetPath + "/" +imageAsset);
                                vecFragranceInfo.add(info);
                            }
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();
            }
        }
        catch( IOException ioe) {
        }
        catch ( XmlPullParserException xppe ) {
        }
    }

    public FragranceInfo getFragrance(Feeling feeling) {

        long timestamp = System.currentTimeMillis() / 1000;
        Calendar cal = Calendar.getInstance();
        int Hour24=cal.get(Calendar.HOUR_OF_DAY);

        PartOfDay partOfDay = PartOfDay.DAY;

        if(0 <= Hour24 && Hour24 < 6)
            partOfDay = PartOfDay.NIGHT;
        else if(6<= Hour24 && Hour24 < 18)
            partOfDay = PartOfDay.DAY;
        else
            partOfDay = PartOfDay.NIGHT;

        HashMap<PartOfDay,HashMap< String, Vector<FragranceInfo>>> hmap_P_SvF = mMapFragranceInfo.get(feeling);
        if(hmap_P_SvF == null)
            return null;

        HashMap< String, Vector<FragranceInfo>> hmap_S_vF = hmap_P_SvF.get(partOfDay);
        if(hmap_S_vF == null)
            return null;

        Random generator = new Random();
        //기분 - 시간 대에 해당하는 향기 ( Hashmap<시간,vector<향기>> ) 랜덤 선택
        Object[] values = hmap_S_vF.values().toArray();
        int randFragranceNameIndex = generator.nextInt(values.length);
        Vector<FragranceInfo> vecFragranceInfo = (Vector<FragranceInfo>)values[randFragranceNameIndex];

        //향기 에 등록되어 있는 정보(vector)들 중 랜덤 선택 ( 배경 이미지 랜덤선택 )
        int randFragranceInfoIndex = generator.nextInt(vecFragranceInfo.size());
        FragranceInfo selectedFragranceInfo = (FragranceInfo)vecFragranceInfo.get(randFragranceInfoIndex);

        return selectedFragranceInfo;
    }
}

package com.nexters.paperfume.content.fragrance;

import android.content.res.AssetManager;
import android.content.res.Resources;

import com.nexters.paperfume.R;
import com.nexters.paperfume.enums.Feeling;
import com.nexters.paperfume.enums.PartOfDay;
import com.nexters.paperfume.util.SharedPreferenceManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
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

    public static final String KEY_LAST_FRAGRANCE_TIMESTAMP = "LAST_FRAGRANCE_TIMESTAMP";       //마지막 향 정보 획득 시간
    public static final String KEY_LAST_FRAGRANCE_FEELING = "LAST_FRAGRANCE_FEELING" ;          //SharedFragrance에서 향 구분을 위해 사용할 키 값
    public static final String KEY_LAST_FRAGRANCE_NAME_INDEX = "LAST_FRAGRANCE_NAME_INDEX" ;    //향 이름 인덱스
    public static final String KEY_LAST_FRAGRANCE_INFO_INDEX = "LAST_FRAGRANCE_INFO_INDEX" ;    //향 - 리스트 인덱스
    public static final String KEY_LAST_FRAGRANCE_ADJECTIVE_INDEX = "LAST_FRAGRANCE_ADJECTIVE_INDEX" ;    //형용사 인덱스

    private AssetManager mAssetManager;
    private Resources mResources;
    private HashMap<Feeling, HashMap< PartOfDay, HashMap< String, Vector<FragranceInfo>>>> mMapFragranceInfo = new HashMap<Feeling, HashMap<PartOfDay, HashMap< String, Vector<FragranceInfo>>>>();

    public void initFragrance(Resources resources, AssetManager assetManager) {
        mResources = resources;
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

        int currtimestamp = (int)System.currentTimeMillis() / 1000;
        int prevTimestamp =  SharedPreferenceManager.getInstance().getInt(KEY_LAST_FRAGRANCE_TIMESTAMP);
        SharedPreferenceManager.getInstance().setInt(KEY_LAST_FRAGRANCE_TIMESTAMP, currtimestamp);

        Boolean refreshFragrancePreference = false;
        int randFragranceNameIndex = -1;
        int randFragranceInfoIndex = -1;
        int randFragranceAdjectiveIndex = -1;

        int curr3HourUnit =  currtimestamp / ( 60 * 60 * 3);
        int prev3HourUnit =  prevTimestamp / ( 60 * 60 * 3);

        //3시간 단위로 선택한 향기 리셋
        if(curr3HourUnit != prev3HourUnit) {
            resetFragrancePreference();
            refreshFragrancePreference = true;
        }
        else {
            randFragranceNameIndex = SharedPreferenceManager.getInstance().getInt(KEY_LAST_FRAGRANCE_NAME_INDEX + "_" + feeling.toString());
            randFragranceInfoIndex = SharedPreferenceManager.getInstance().getInt(KEY_LAST_FRAGRANCE_INFO_INDEX + "_" + feeling.toString());
            randFragranceAdjectiveIndex = SharedPreferenceManager.getInstance().getInt(KEY_LAST_FRAGRANCE_ADJECTIVE_INDEX + "_" + feeling.toString());

            //하나라도 초기화 된 값이라면 FragrancePreference 갱신
            if(randFragranceNameIndex == -1 || randFragranceInfoIndex == -1 || randFragranceAdjectiveIndex == -1 )
                refreshFragrancePreference = true;
        }

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

        Random r = new Random();
        //기분 - 시간 대에 해당하는 향기 ( Hashmap<시간,vector<향기>> ) 랜덤 선택
        Object[] values = hmap_S_vF.values().toArray();
        if(randFragranceNameIndex == -1 )  randFragranceNameIndex = r.nextInt(values.length);
        Vector<FragranceInfo> vecFragranceInfo = (Vector<FragranceInfo>)values[randFragranceNameIndex];

        //향기 에 등록되어 있는 정보(vector)들 중 랜덤 선택 ( 배경 이미지 랜덤선택 )
        if(randFragranceInfoIndex == -1 )  randFragranceInfoIndex = r.nextInt(vecFragranceInfo.size());
        FragranceInfo selectedFragranceInfo = (FragranceInfo)vecFragranceInfo.get(randFragranceInfoIndex);

        //랜덤 형용사 반영
        String[] fragranceAdjectives = mResources.getStringArray(R.array.fragrance_adjective);
        if(randFragranceAdjectiveIndex == -1 )  randFragranceAdjectiveIndex = r.nextInt(fragranceAdjectives.length);
        selectedFragranceInfo.setAdjective( fragranceAdjectives[ randFragranceAdjectiveIndex ] );

        //SharedPreference에 기록
        if( refreshFragrancePreference == true ) {
            SharedPreferenceManager.getInstance().setBoolean(KEY_LAST_FRAGRANCE_FEELING + "_" + feeling.toString(), true);
            SharedPreferenceManager.getInstance().setInt(KEY_LAST_FRAGRANCE_NAME_INDEX + "_" + feeling.toString(), randFragranceNameIndex);
            SharedPreferenceManager.getInstance().setInt(KEY_LAST_FRAGRANCE_INFO_INDEX + "_" + feeling.toString(), randFragranceInfoIndex);
            SharedPreferenceManager.getInstance().setInt(KEY_LAST_FRAGRANCE_ADJECTIVE_INDEX + "_" + feeling.toString(), randFragranceAdjectiveIndex);
        }

        return selectedFragranceInfo;
    }

    private void resetFragrancePreference(){
        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE_FEELING + "_" + Feeling.HAPPY.toString());
        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE_NAME_INDEX + "_" + Feeling.HAPPY.toString());
        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE_INFO_INDEX + "_" + Feeling.HAPPY.toString());
        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE_ADJECTIVE_INDEX + "_" + Feeling.HAPPY.toString());

        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE_FEELING + "_" + Feeling.MISS.toString());
        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE_NAME_INDEX + "_" + Feeling.MISS.toString());
        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE_INFO_INDEX + "_" + Feeling.MISS.toString());
        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE_ADJECTIVE_INDEX + "_" + Feeling.MISS.toString());

        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE_FEELING + "_" + Feeling.GROOMY.toString());
        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE_NAME_INDEX + "_" + Feeling.GROOMY.toString());
        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE_INFO_INDEX + "_" + Feeling.GROOMY.toString());
        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE_ADJECTIVE_INDEX + "_" + Feeling.GROOMY.toString());

        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE_FEELING + "_" + Feeling.STIFLED.toString());
        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE_NAME_INDEX + "_" + Feeling.STIFLED.toString());
        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE_INFO_INDEX + "_" + Feeling.STIFLED.toString());
        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE_ADJECTIVE_INDEX + "_" + Feeling.STIFLED.toString());
    }
}

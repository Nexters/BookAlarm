package com.nexters.paperfume.content.fragrance;

import android.content.res.AssetManager;
import android.content.res.Resources;

import com.google.gson.Gson;
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

    public static final String KEY_LAST_FRAGRANCE_TIMESTAMP = "LAST_FRAGRANCE_TIME";       //마지막 향 정보 획득 시간
    public static final String KEY_LAST_FRAGRANCE = "LAST_FRAGRANCE";                           //마지막으로 획득한 향 정보

    private AssetManager mAssetManager;
    private Resources mResources;
    private HashMap<Feeling, HashMap< PartOfDay, HashMap< String, Vector<FragranceInfo>>>> mMapFragranceInfo = new HashMap<Feeling, HashMap<PartOfDay, HashMap< String, Vector<FragranceInfo>>>>();

    public void init(Resources resources, AssetManager assetManager) {
        mResources = resources;
        mAssetManager = assetManager;

        loadFragrance(Feeling.HAPPY, "feelings/happy.xml");
        loadFragrance(Feeling.GROOMY, "feelings/groomy.xml");
        loadFragrance(Feeling.MISS, "feelings/miss.xml");
        loadFragrance(Feeling.STIFLED, "feelings/stifled.xml");
    }

    /**
     * assets 로부터 향기 정보를 읽어온다
     * @param feeling 기분 ( HAPPY, GROOMY ... )
     * @param feelingAssetPath 입력한 기분에 해당하는 xml의 assets상 위치
     */
    public void loadFragrance(Feeling feeling, String feelingAssetPath) {
        try {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlFactoryObject.newPullParser();

            InputStream is = mAssetManager.open(feelingAssetPath);

            parser.setInput(is, null);
            int event = parser.getEventType();
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
        catch ( XmlPullParserException xppe ) {
            xppe.printStackTrace();
        }
        catch( IOException ioe) {
            ioe.printStackTrace();
        }

    }

    /**
     * 현재 기분의 향기자료 를 반환한다.
     * @param feeling 현재 기분
     * @return 현재 기분에 해당하는 향기자료
     */
    public FragranceInfo getFragrance(Feeling feeling) {

        Calendar cal = Calendar.getInstance();
        int Hour24=cal.get(Calendar.HOUR_OF_DAY);

        PartOfDay partOfDay;

        if(0 <= Hour24 && Hour24 < 6)
            partOfDay = PartOfDay.NIGHT;
        else if(6<= Hour24 && Hour24 < 18)
            partOfDay = PartOfDay.DAY;
        else
            partOfDay = PartOfDay.NIGHT;

        Gson gson = new Gson();
        MetaFragranceInfo metaFragranceInfo;

        String s = SharedPreferenceManager.getInstance().getString(KEY_LAST_FRAGRANCE + "_" + feeling.toString());

        if( s == null ) {
            metaFragranceInfo = new MetaFragranceInfo();
        }
        else {
            metaFragranceInfo = gson.fromJson(s,MetaFragranceInfo.class);
        }

        metaFragranceInfo.setFeeling(feeling);
        metaFragranceInfo.setPartOfDay(partOfDay);

        FragranceInfo selectedFragranceInfo = findFragranceInfo(metaFragranceInfo);

        //SharedPreference에 기록
        if( metaFragranceInfo.isSetToPreference() ) {
            s = gson.toJson(metaFragranceInfo);
            SharedPreferenceManager.getInstance().setString(KEY_LAST_FRAGRANCE + "_" + feeling.toString(), s);
        }

        return selectedFragranceInfo;
    }

    private FragranceInfo findFragranceInfo(MetaFragranceInfo metaFragranceInfo) {

        Feeling feeling = metaFragranceInfo.getFeeling();
        PartOfDay partOfDay = metaFragranceInfo.getPartOfDay();
        int idxFragranceName = metaFragranceInfo.getIdxFracranceName();
        int idxFragranceInfo = metaFragranceInfo.getIdxFracranceInfo();
        int idxFragranceAdjective = metaFragranceInfo.getIdxFragranceAdjective();

        HashMap<PartOfDay,HashMap< String, Vector<FragranceInfo>>> hmap_P_SvF = mMapFragranceInfo.get(feeling);
        if(hmap_P_SvF == null)
            return null;

        HashMap< String, Vector<FragranceInfo>> hmap_S_vF = hmap_P_SvF.get(partOfDay);
        if(hmap_S_vF == null)
            return null;

        Random r = new Random();
        //기분 - 시간 대에 해당하는 향기 랜덤 선택
        String[] names = new String[hmap_S_vF.size()];
        int index = 0;
        for(String name : hmap_S_vF.keySet()) {
            names[index] = name;
            index++;
        }
        if(idxFragranceName == -1 ) {
            idxFragranceName = r.nextInt(names.length);
            metaFragranceInfo.setIdxFracranceName(idxFragranceName);
        }
        if(idxFragranceName >= names.length ) {
            idxFragranceName = names.length - 1;
            metaFragranceInfo.setIdxFracranceName(idxFragranceName);
        }
        Vector<FragranceInfo> vecFragranceInfo = hmap_S_vF.get(names[idxFragranceName]);

        //향기 에 등록되어 있는 정보(vector)들 중 랜덤 선택 ( 배경 이미지 랜덤선택 )
        if(idxFragranceInfo == -1 ) {
            idxFragranceInfo = r.nextInt(vecFragranceInfo.size());
            metaFragranceInfo.setIdxFracranceInfo(idxFragranceInfo);
        }
        if(idxFragranceInfo >= vecFragranceInfo.size() ) {
            idxFragranceInfo = vecFragranceInfo.size() - 1;
            metaFragranceInfo.setIdxFracranceInfo(idxFragranceInfo);
        }

        FragranceInfo selectedFragranceInfo = vecFragranceInfo.get(idxFragranceInfo);

        //랜덤 형용사 반영
        String[] fragranceAdjectives = mResources.getStringArray(R.array.fragrance_adjective);
        if(idxFragranceAdjective == -1 ) {
            idxFragranceAdjective = r.nextInt(fragranceAdjectives.length);
            metaFragranceInfo.setIdxFragranceAdjective(idxFragranceAdjective);
        }
        if(idxFragranceAdjective >= fragranceAdjectives.length ) {
            idxFragranceAdjective = fragranceAdjectives.length - 1;
            metaFragranceInfo.setIdxFragranceAdjective(idxFragranceAdjective);
        }
        selectedFragranceInfo.setAdjective( fragranceAdjectives[ idxFragranceAdjective ] );

        return selectedFragranceInfo;
    }

    public boolean checkResetFragrance(){

        long currentTimestamp = System.currentTimeMillis() / 1000;
        long previousTimestamp =  SharedPreferenceManager.getInstance().getLong(KEY_LAST_FRAGRANCE_TIMESTAMP);


        long current3HourUnit =  currentTimestamp / ( 60 * 60 * 3);
        long previous3HourUnit =  previousTimestamp / ( 60 * 60 * 3);

        //3시간 단위로 리셋
        if(current3HourUnit != previous3HourUnit) {
            return true;
        }
        else {
            return false;
        }

    }

    public void resetFragrance() {

        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE + "_" + Feeling.HAPPY.toString());
        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE + "_" + Feeling.MISS.toString());
        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE + "_" + Feeling.GROOMY.toString());
        SharedPreferenceManager.getInstance().remove( KEY_LAST_FRAGRANCE + "_" + Feeling.STIFLED.toString());

        long currentTimestamp = System.currentTimeMillis() / 1000;
        SharedPreferenceManager.getInstance().setLong(KEY_LAST_FRAGRANCE_TIMESTAMP, currentTimestamp);
    }

    /**
     * FragranceInfo 를 찾을때 사용하게 되는 클래스
     */
    class MetaFragranceInfo {

        transient Feeling mFeeling;
        transient PartOfDay mPartOfDay;
        int mIdxFracranceName = -1;
        int mIdxFracranceInfo = -1;
        int mIdxFragranceAdjective = -1;

        /**
         * SharedPreference에 갱신이 필요한 경우, 이를 나타내주는 변수
         */
        transient boolean mSetToPreference = false;

        public Feeling getFeeling() {
            return mFeeling;
        }

        public void setFeeling(Feeling feeling) {
            mFeeling = feeling;
        }

        public PartOfDay getPartOfDay() {
            return mPartOfDay;
        }

        public void setPartOfDay(PartOfDay partOfDay) {
            mPartOfDay = partOfDay;
        }

        public int getIdxFracranceName() {
            return mIdxFracranceName;
        }

        public void setIdxFracranceName(int idxFracranceName) {
            mIdxFracranceName = idxFracranceName;
            mSetToPreference = true;
        }

        public int getIdxFracranceInfo() {
            return mIdxFracranceInfo;
        }

        public void setIdxFracranceInfo(int idxFracranceInfo) {
            mIdxFracranceInfo = idxFracranceInfo;
            mSetToPreference = true;
        }

        public int getIdxFragranceAdjective() {
            return mIdxFragranceAdjective;
        }

        public void setIdxFragranceAdjective(int idxFragranceAdjective) {
            mIdxFragranceAdjective = idxFragranceAdjective;
            mSetToPreference = true;
        }

        public boolean isSetToPreference() {
            return mSetToPreference;
        }

        public void setSetToPreference(boolean setToPreference) {
            mSetToPreference = setToPreference;
        }

        public void reset() {
            mIdxFracranceName = -1;
            mIdxFracranceInfo = -1;
            mIdxFragranceAdjective = -1;
        }
    }
}

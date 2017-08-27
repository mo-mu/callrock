package com.momu.callrock.constant;

/**
 * Created by knulps on 2017. 7. 16..
 */
public class CConstants {
    /**
     * 카카오 지도 좌표정보 변환 REST API url
     */
    public static final String URL_KAKAO_GEO_TRANSCOORD = "https://dapi.kakao.com/v2/local/geo/transcoord.json?x=%1$s&y=%2$s&input_coord=WGS84&output_coord=TM";

    /**
     * 카카오 지도 GM좌표로 주소 불러오는 REST API url
     */
    public static final String URL_KAKAO_GEO_COORD2ADDRESS = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x=%1$s&y=%2$s&input_coord=WGS84";

    /**
     * TM 좌표로 가까운 측정소 불러오는 url
     */
    public static final String URL_STATION_LIST_BY_GEO = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?ServiceKey=NCPIDFE%2F7buZ0eIVTd6x6iqFLtZRkGcVW%2FZuKO1g%2BM9cCN8YBQBmPIKzaP%2B9MTfyyNMhXDS3SkK8%2FjiyINYe0A%3D%3D&_returnType=json&ver=1.3&";
    /**
     * 전체 측정소 정보 불러오는 url
     */
    public static final String URL_STATION_INFO = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getMsrstnList?pageNo=1&numOfRows=400&ServiceKey=NCPIDFE%2F7buZ0eIVTd6x6iqFLtZRkGcVW%2FZuKO1g%2BM9cCN8YBQBmPIKzaP%2B9MTfyyNMhXDS3SkK8%2FjiyINYe0A%3D%3D&_returnType=json";

    public static final String URL_STATION_DETAIL = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?_returnType=json&dataTerm=month&pageNo=1&numOfRows=10&ServiceKey=NCPIDFE%2F7buZ0eIVTd6x6iqFLtZRkGcVW%2FZuKO1g%2BM9cCN8YBQBmPIKzaP%2B9MTfyyNMhXDS3SkK8%2FjiyINYe0A%3D%3D&ver=1.3&stationName=";


    /**
     * 미세먼지 기준, 좋음
     */
    public static final int GRADE_GOOD = 0;

    /**
     * 미세먼지 기준, 보통
     */
    public static final int GRADE_NORMAL = 1;

    /**
     * 미세먼지 기준, 나쁨
     */
    public static final int GRADE_BAD = 2;

    /**
     * 미세먼지 기준, 매우 나쁨
     */
    public static final int GRADE_WORST = 3;

    /**
     * main에서 search페이지로 request 할 때 값
     */
    public static final int SEARCH_LOCATION = 1001;

    /**
     * search에서 main으로 result 값을 던져줄 때 값
     */
    public static final int SELECT_ITEM = 2001;


    /**
     * 통합 문구
     */
    public static final String [] wholeTexts = {"마음껏 외출하세요~!","그냥~그냥 쏘쏘한 날이에요","무리한 야외활동 금지!","매우 나쁜 상태! 마스크 꼭 챙겨요!"};
    /**
     * 마스크 문구
     */
    public static final String [] maskTexts = {"마스크가 필요없는 날이에요","호흡기가 민감하면, 마스크를 쓰세요","마스크를 껴주세요","마스크를 꼭! 껴주세요"};

    /**
     * 활동 문구
     */
    public static final String [] activityTexts = {"야외활동 하기 좋은 날!","민감군은 장시간 또는 무리한 실외 활동을 자제하세요","실외활동 및 외출을 자제하세요","실외활동 및 외출을 자제하세요"};

    /**
     * 생활 문구
     */
    public static final String [] lifeTexts = {"저기압엔 고기앞으로...","물이나 비타민 C가 많은 과일/야채를 섭취해보세요","혹시 외출 했다면, 깨끗이 씻어주세요","혹시 외출 했다면, 깨끗이 씻어주세요"};

    /**
     * 환기 문구
     */
    public static final String [] windowTexts = {"창문을 활짝 열어 환기해요","환기를 되도록 자제해주세요","환기는 최대 1분 내외로 해주세요","창문을 꽁꽁 닫아주세요"};

}
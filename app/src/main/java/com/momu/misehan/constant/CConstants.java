package com.momu.misehan.constant;

/**
 * 상수 모음
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
    public static final String URL_STATION_LIST_BY_GEO = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?ServiceKey=NCPIDFE%2F7buZ0eIVTd6x6iqFLtZRkGcVW%2FZuKO1g%2BM9cCN8YBQBmPIKzaP%2B9MTfyyNMhXDS3SkK8%2FjiyINYe0A%3D%3D&_returnType=json&";
    /**
     * 전체 측정소 정보 불러오는 url
     */
    public static final String URL_STATION_INFO = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getMsrstnList?pageNo=1&numOfRows=400&ServiceKey=NCPIDFE%2F7buZ0eIVTd6x6iqFLtZRkGcVW%2FZuKO1g%2BM9cCN8YBQBmPIKzaP%2B9MTfyyNMhXDS3SkK8%2FjiyINYe0A%3D%3D&_returnType=json";

    public static final String URL_STATION_DETAIL = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?_returnType=json&dataTerm=month&pageNo=1&numOfRows=10&ServiceKey=NCPIDFE%2F7buZ0eIVTd6x6iqFLtZRkGcVW%2FZuKO1g%2BM9cCN8YBQBmPIKzaP%2B9MTfyyNMhXDS3SkK8%2FjiyINYe0A%3D%3D&stationName=";


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
    public static final int RESULT_SEARCH_LOCATION = 1001;

    /**
     * main에서 permission setting페이지로 request 할 때 값
     */
    public static final int RESULT_PERMISSION_SETTINGS = 1002;

    /**
     * search에서 main으로 result 값을 던져줄 때 값
     */
    public static final int RESULT_SELECT_ITEM = 2001;


    /**
     * 위젯 업데이트 시 쓰는 값
     */
    public static final String UPDATE_WIDGET = "UPDATE CLICKED";

    /**
     * 나눔명조 폰트
     */
    public static final String FONT_NANUM_MYEONGJO = "fonts/NanumMyeongjoExtraBold.otf";

    /**
     * 나눔Squarer(?) 폰트
     */
    public static final String FONT_NANUM_SQUAR = "fonts/NANUMSQUARER.TTF";

    /**
     * 노티 channel ID
     */
    public static String default_notification_channel_id = "misehan_noti";
}
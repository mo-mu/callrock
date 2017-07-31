package com.momu.callrock.constant;

/**
 * Created by knulps on 2017. 7. 16..
 */
public class CConstants {
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
}
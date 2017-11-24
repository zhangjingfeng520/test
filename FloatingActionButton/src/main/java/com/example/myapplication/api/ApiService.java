package com.example.myapplication.api;

import com.example.myapplication.bean.jsonbean.BooksByCats;


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/11/9.
 */

public interface ApiService {
    String BASE_URL="http://api.zhuishushenqi.com";//书籍
    String IMG_BASE_URL = "http://statics.zhuishushenqi.com";//图片

    /**
     * 按分类获取书籍列表
     *
     * @param gender male、female
     * @param type   hot(热门)、new(新书)、reputation(好评)、over(完结)
     * @param major  玄幻
     * @param minor  东方玄幻、异界大陆、异界争霸、远古神话
     * @param limit  50
     * @return
     */
    @GET("/book/by-categories")
    Observable<BooksByCats> getBooksByCats(@Query("gender") String gender, @Query("type") String type, @Query("major") String major, @Query("minor") String minor, @Query("start") int start, @Query("limit") int limit);
}

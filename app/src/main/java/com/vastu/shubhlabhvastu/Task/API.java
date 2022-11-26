package com.vastu.shubhlabhvastu.Task;

public interface API {

    String Authorization = "Authorization";
    String Accept = "Accept";
    String ApplicationJSON = "application/json";

    String SITE_URL = "https://devindia.in/demo/subhlabh/";
    String BASE_URL = "https://devindia.in/demo/subhlabh/api/v1/";
    String COMMON_APP_VERSION = BASE_URL + "common?GET-TYPE=APPVER";
    String COMMON_INTRO_DATA = BASE_URL + "common?GET-TYPE=INTRO";

}

package com.vastu.shubhlabhvastu.Task;

public interface API {

    String Authorization = "Authorization";
    String Accept = "Accept";
    String ApplicationJSON = "application/json";

    String SITE_URL = "https://devindia.in/demo/subhlabh/";
    String BASE_URL = "https://devindia.in/demo/subhlabh/api/v1/";

    String IMAGE_URL_INTRO = SITE_URL+"upload/intro_data/";
    String IMAGE_URL_BANNER = SITE_URL+"upload/banner_data/";
    String IMG_EXPERT = SITE_URL + "upload/tl_data/image/";
    String IMG_URL_PAGE = SITE_URL + "upload/page_data/";

    String COMMON_APP_VERSION = BASE_URL + "common?GET-TYPE=APPVER";
    String COMMON_INTRO_DATA = BASE_URL + "common?GET-TYPE=INTRO";
    String CUSTOMER_ACCESS = BASE_URL + "customer";
    String DASHBOARD = BASE_URL + "dashboard";
    String PAGE = BASE_URL + "page";
    String EXPERT = BASE_URL + "expert";
    String PACKAGELIST = BASE_URL + "query?GET-TYPE=PACKAGE";
    String SAVEQUERY = BASE_URL + "query";

}

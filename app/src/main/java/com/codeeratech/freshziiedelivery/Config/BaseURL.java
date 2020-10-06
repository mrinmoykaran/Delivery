package com.codeeratech.freshziiedelivery.Config;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class BaseURL {
    public static final String PREFS_NAME = "GroceryLoginPrefs";
    public static final String PREFS_NAME2 = "GroceryLoginPrefs2";
    public static final String IS_LOGIN = "isLogin";
    public static final String KEY_NAME = "user_fullname";
    public static final String KEY_ID = "user_id";
    public static final String KEY_PASSWORD = "user_password";
    public static final String USER_CURRENCY_CNTRY = "user_currency_country";
    public static final String USER_CURRENCY = "user_currency";
    public static final String KEY_ORDER_ID = "ORDER_ID";
    static final String APP_NAME = "Gofreshdelivery";
    //    public static String BASE_URL = "https://thecodecafe.in/gogrocer-ver2.0/api/driver/";
//    public static String BASE_URL = "https://gogrocer.tecmanic.com/api/driver/";
    public static String BASE_URL = "http://www.freshziie.com/admin/api/driver/";
//    public static String currencyApi = "https://gogrocer.tecmanic.com/api/currency";
    public static String currencyApi = "http://www.freshziie.com/admin/api/currency";

    public static String dLogin = BASE_URL + "driver_login";
    public static String dRegister = BASE_URL + "driver_register";
    public static String dProfile = BASE_URL + "driver_profile";
    public static String todayOrder = BASE_URL + "ordersfortoday";
    public static String nextOrder = BASE_URL + "ordersfornextday";
    public static String dOutforDelivery = BASE_URL + "out_for_delivery";
    public static String dCompleteDelievry = BASE_URL + "delivery_completed";
    public static String signatureUrl = BASE_URL + "delivery_completed";
    public static String historyOrders = BASE_URL + "completed_orders";
    public static String Loction = BASE_URL + "update_location";
    public static String update_status = BASE_URL + "update_status";

}


/*

    public static String GET_ORDER_URL=  BASE_URL + "index.php/api/delivery_boy_order";

    public static String check_Status=  BASE_URL + "index.php/api/change_boy_status";
    public static String pickup=  BASE_URL + "index.php/api/pickup";
    public static String GET_DELIVERD_ORDER_URL = BASE_URL + "index.php/api/delivered_complete";

    public static String LOGIN = BASE_URL+"index.php/api/delivery_boy_login";

    public static String OrderDetail = BASE_URL + "index.php/api/order_details";

    public static String IMG_PRODUCT_URL = BASE_URL + "uploads/products/";
    public static String JSON_RIGISTER_FCM = BASE_URL +"index.php/api/get_deliveryboy_deviceid";

    public static final String urlUpload = "mark_deliver";
    public static final int REQCODE = 100;
    public static final String image = "signature";
    public static final String imageName = "id";

}
*/

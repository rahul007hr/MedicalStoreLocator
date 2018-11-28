package com.medicalstorefinder.mychemists.Constants;


public final class Constants {

    public static final String PREF_IS_FIRST_TIME = "PrefIsFirstTime";
    public static final String PREF_USER_ROLE = "customer";
    public static final String PREF_IS_USER = "PrefISUser";
    public static final String PREF_FIREBASE_USER_TOKEN = "firebaseUserToken";
    public static final String PREF_KEY_USER_ID = "PrefUserID";
    public static final String PREF_KEY_MEDICAL_ID = "PrefMedicalID";
    public static final String PREF_KEY_ORDER_ID = "orderid";
    public static final String PREF_KEY_ORDER_ID_LIST = "orderidList";
    public static final String PREF_KEY_USER_PHONE = "PrefPhone";
    public static final String PREF_KEY_USER_PASS = "PrefUserPass";
    public static final String PREF_KEY_USER_SHOP_NAME = "PrefShopName";
    public static final String PREF_KEY_USER_FIRST_NAME = "PrefFirstName";
    public static final String PREF_KEY_USER_LAST_NAME = "PrefLastName";
    public static final String PREF_KEY_USER_Address = "PrefAddress";
    public static final String PREF_SERVICE_PROVIDER_IDS = "serviceproviderIds";
    public static final String DISTANCE = "km";
    public static final String PREF_USER_ORDER_ImagePath = "userOrderImagePath";
    public static final String PREF_USER_ORDER_Description = "userOrderDescription";
    public static final String PREF_USER_ORDER_Latitude = "userOrderLatitude";
    public static final String PREF_USER_ORDER_Longitude = "userOrderLongitude";
    public static final String PREF_USER_ORDER_getAddress = "userOrdergetAddress";
    public static final String PREF_PROFILE_ImagePath = "profileImagePath";

    public static final String PREF_KEY_USER_LATITUDE = "PrefLatitude";
    public static final String PREF_KEY_USER_LONGITUDE = "PrefLongitude";
    public static final String PREF_KEY_USER_Email = "PrefEmail";
    public static final String PREF_KEY_USER_ProfilePic = "PrefProfilePic";

    public static final String DOMAIN_NAME = "https://www.mychemist.net.in/";
    public static final String IMAGE_PATH = "";
    public static final String PROFILE_IMAGE_PATH = "https://www.mychemist.net.in/admin/images/";
    public static final String NO_AVATAR_IMAGE_PATH = "https://www.mychemist.net.in/public_html/admin/images/";

    public static final String API_CHECK_STABLE_VERSION = DOMAIN_NAME + "admin/api/checkversion";
    public static final String API_SIGN_UP = DOMAIN_NAME + "admin/api/medicalregister";
    public static final String API_SIGN_UP_CUSTOMER = DOMAIN_NAME + "admin/api/customerregister";
    public static final String API_MEDICAL_LOGIN = DOMAIN_NAME + "admin/api/medicallogin";
    public static final String API_MEDICAL_RECEIVED_ORDER = DOMAIN_NAME + "admin/api/medicalreplytoorder";
    public static final String API_Account_Logout = DOMAIN_NAME + "admin/api/logout";
    public static final String API_CUSTOMER_LOGIN = DOMAIN_NAME + "admin/api/customerlogin";
    public static final String API_VERIFY_OTP = DOMAIN_NAME + "admin/api/verifyotp";
    public static final String API_MEDICAL_FORGOT_PASSWORD = DOMAIN_NAME + "admin/api/medicalforgotpass";
    public static final String API_GET_NEARBY = DOMAIN_NAME + "admin/api/getnearby";
    public static final String API_POST_ORDER = DOMAIN_NAME + "admin/api/createorder";
    public static final String API_SERVICE_PROVIDER_LIST_USING_ORDER_STATUS = DOMAIN_NAME + "admin/api/getcustomerorders";
    public static final String API_RECEIVED_ORDER_STATUS = DOMAIN_NAME + "admin/api/getmedicalorders";
    public static final String API_MEDICAL_COST_RESPONCE_STATUS = DOMAIN_NAME + "admin/api/customerconfirmation";
    public static final String API_MEDICAL_FINAL_CONFIRMATION = DOMAIN_NAME + "admin/api/medicalconfirmation";
    public static final String API_MEDICAL_PROFILE_UPDATE = DOMAIN_NAME + "admin/api/updatemedical";
    public static final String API_MEDICAL_PROFILE_GET = DOMAIN_NAME + "admin/api/getprofile";
    public static final String API_UPDATE_ORDER_STATUS = DOMAIN_NAME + "admin/api/updateorderstatus";
    public static final String API_RATINGS = DOMAIN_NAME + "admin/api/addrating";
    public static final String API_SINGLE_NOTIFICATION = DOMAIN_NAME + "admin/api/getsingleordercustomer";
    public static final String API_SINGLE_NOTIFICATION_MEDICAL = DOMAIN_NAME + "admin/api/getsingleordermedical";
    public static final String API_ALL_NOTIFICATIONS = DOMAIN_NAME + "admin/api/getallnotifications";

}

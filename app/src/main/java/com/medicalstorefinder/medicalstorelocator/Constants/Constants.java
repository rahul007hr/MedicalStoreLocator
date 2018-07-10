package com.medicalstorefinder.medicalstorelocator.Constants;


public final class Constants {

    public static final String PREF_ISAD = "PrefISAD";
    public static final String PREF_KEY_USER_ID = "PrefUserID";
    public static final String PREF_KEY_USER_NAME = "PrefUserName";
    public static final String PREF_KEY_USER_PASS = "PrefUserPass";
    public static final String PREF_KEY_USER_RegMobile = "PrefRegMobile";
    public static final String PREF_KEY_USER_Email = "PrefEmail";
    public static final String PREF_KEY_USER_ProfilePic = "PrefProfilePic";

    //    public static final String DOMAIN_NAME = "http://192.168.0.102/";
    public static final String DOMAIN_NAME = "http://www.paanwalapro.com/";

    public static final String API_Account_IsAuthorised = DOMAIN_NAME + "api/Users/IsAuthorised";//.....
    public static final String API_SetUserInfo = DOMAIN_NAME + "api/Users/PostUser";//.........
    public static final String API_Account_Logout = DOMAIN_NAME + "api/Users/Logout";//.....
    public static final String API_Account_Activation = DOMAIN_NAME + "api/Users/Activation";//.....
    public static final String API_ChangePassword = DOMAIN_NAME + "api/Users/ChangePassword";//.....
    public static final String API_Account_ForgotPassword = DOMAIN_NAME + "api/Users/ForgotPassword";//.....

    public static final String API_GetServiceProviders = DOMAIN_NAME + "api/ServiceProvider/GetServiceProviders";//....
    public static final String API_SubmitServiceProvidersList = DOMAIN_NAME + "api/ServiceProvider/PostServiceProvidersList";//....
    public static final String API_GetServiceProvidersList = DOMAIN_NAME + "api/ServiceProvider/GetServiceProvidersList";//.....

    public static final String API_SubmitUserOrder = DOMAIN_NAME + "api/UsersOrder/PostUserOrder";//.......
    public static final String API_GetServiceProvidersDetails = DOMAIN_NAME + "api/Users/GetServiceProvidersDetails";

    public static final String API_GetUserHistry = DOMAIN_NAME + "api/UsersOrder/GetUserHistry";
    public static final String API_GetServiceProviderHistry = DOMAIN_NAME + "api/UsersOrder/GetServiceProviderHistry";
    public static final String API_GetAdminHistry = DOMAIN_NAME + "api/Users/GetAdminHistry";
    public static final String API_Account_OrderStatus = DOMAIN_NAME + "api/UsersOrder/OrderStatus";//.....





}

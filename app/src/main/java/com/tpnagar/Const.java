package com.tpnagar;

public class Const {

    ///Production Environment/////////////////////////
    public static final String URL_MainSearch="http://tpnagar.com:5001/api/search/";
   public static final String URL_GetToken= "http://tpnagar.com:5001/api/gettoken";
   public static final String URL_MainFORAll="http://api.tpnagar.com/api/V1/";



    ///Development Environment/////////////////////////
  /*  public static final String URL_MainFORAll = "http://api.tpnagar.co.in/api/V1/";
    public static final String URL_MainSearch = "http://tpnagar.co.in:5004/api/search/";
   public static final String URL_GetToken = "http://tpnagar.co.in:5004/api/gettoken";
*/

    public static final String URL_Mainservice = URL_MainFORAll + "service/";

    public static final String URL_Main = URL_MainFORAll + "company/";

    public static final String URL_Maincity = URL_MainFORAll + "city/";


    public static final String URL_validatelogin = URL_Main + "validatelogin";

    public static final String URL_LoggedInUserInfo = URL_MainFORAll + "company/LoggedInUserInfo/";
    public static final String URL_signup = URL_Main + "signup";

    public static final String URL_RandomCityList = URL_Maincity + "RandomCityList";
    public static final String URL_ServiceListOnCityAndCategory = URL_Mainservice + "ServiceListOnCityAndCategory";


    public static final String URL_ValidateOTP = URL_MainFORAll + "sms/ValidateOTP";
    public static final String URL_StatesOfCountry = URL_MainFORAll + "state/StatesOfCountry/1";

    public static final String URL_CitiesOfState = URL_MainFORAll + "city/CitiesOfState/";


    public static final String URL_AreasOfCity = URL_MainFORAll + "area/AreasOfCity/";

    public static final String URL_CompanyType = URL_MainFORAll + "company/CompanyType";

    public static final String URL_GetLoginDetail = URL_MainFORAll + "company/GetLoginDetail/";

    public static final String URL_UpdateCompanyProfile = URL_MainFORAll + "company/UpdateCompanyProfile";
    public static final String URL_GetDBTabs = URL_MainFORAll + "dashboard/GetDBTabs/";
    public static final String URL_addpostedvehicle = URL_MainFORAll + "postvehicle/addpostedvehicle";
    public static final String URL_addpostedgoods = URL_MainFORAll + "postgoods/addpostedgoods";
    public static final String URL_GenerateOTP = URL_MainFORAll + "sms/GenerateOTP";

    public static final String URL_addcompanyservice_multi_state_city = URL_MainFORAll + "service/addcompanyservice_multi_state_city";

    public static final String URL_GetCompanyService = URL_MainFORAll + "service/GetCompanyService/";
    public static final String URL_AddCompanyServiceKeyword = URL_MainFORAll + "keyword/AddCompanyServiceKeyword";
    public static final String URL_GetKeyword = URL_MainFORAll + "keyword/GetKeyword/";
    public static final String URL_addbranch = URL_MainFORAll + "branch/addbranch";


    public static final String URL_GetCompanyServiceList = URL_MainFORAll + "service/GetCompanyServiceList/";
    public static final String URL_GetCompanyServiceKeyword = URL_MainFORAll + "keyword/GetCompanyServiceKeyword/";

    public static final String URL_Get_Disposition_List = URL_MainFORAll + "disposition/Get_Disposition_List/";

    public static final String URL_deletepostedvehicle = URL_MainFORAll + "postvehicle/deletepostedvehicle";
    public static final String URL_postedgoodslist = URL_MainFORAll + "postgoods/postedgoodslist/";
    public static final String URL_deletepostedgoods = URL_MainFORAll + "postgoods/deletepostedgoods";
    public static final String URL_branchlist = URL_MainFORAll + "branch/branchlist/";
    public static final String URL_deletebranch = URL_MainFORAll + "branch/deletebranch";

    public static final String URL_managebranchadmin = URL_MainFORAll + "company/managebranchadmin";


    public static final String URL_postedvehiclelist = URL_MainFORAll + "postvehicle/postedvehiclelist/";

    public static final String URL_deletecompanyservice = URL_MainFORAll + "service/deletecompanyservice";

    public static final String URL_DeleteCompanyKeyword = URL_MainFORAll + "keyword/DeleteCompanyKeyword";


    public static final String URL_GetSearchResult = URL_MainFORAll + "search/GetSearchResult";
    public static final String URL_SearchData = URL_MainFORAll + "Search/SearchMobileData";

    public static final String URL_updatebranch = URL_MainFORAll + "branch/updatebranch";

    public static final String URL_changepwd = URL_MainFORAll + "pwd/changepwd";
    public static final String URL_changeloginmobile = URL_MainFORAll + "company/changeloginmobile";

    public static final String URL_addrating = URL_MainFORAll + "rating/addrating";

    public static final String URL_createbranchlogin = URL_MainFORAll + "branch/createbranchlogin";

    public static final String URL_forgetpwd = URL_MainFORAll + "pwd/forgetpwd";

    public static final String URL_addenquiry = URL_MainFORAll + "contactus/addenquiry";
    public static final String URL_addcontactus = URL_MainFORAll + "contactus/addcontactus";
    public static final String URL_getpostedgoods = URL_MainFORAll + "postgoods/getpostedgoods";

    public static final String URL_getpostedvehicles = URL_MainFORAll + "postvehicle/getpostedvehicles";

    public static final String URL_GetCitiesOfMultiStates = URL_MainFORAll + "city/GetCitiesOfMultiStates/";

    public static final String URL_BrokerServiceWithoutSuppliers = URL_MainFORAll + "service/BrokerServiceWithoutSuppliers";

    public static final String URL_BrokerServices = URL_MainFORAll + "service/BrokerServices";
    public static final String URL_GetCompanyDetails = URL_MainFORAll + "company/GetCompanyDetails/";

    public static final String URL_updateloginmobile = URL_MainFORAll + "company/updateloginmobile";
    public static final String URL_InnerSearchCity = URL_MainFORAll + "city/InnerSearchCity/";
    public static final String URL_AddSearchEnquiry = URL_MainFORAll + "company/AddSearchEnquiry/";
    public static final String URL_SearchMobileData = URL_MainFORAll + "Search/SearchMobileData";
    public static final String URL_ServiceListOnCityAndCategoryNew = URL_MainFORAll + "service/ServiceListOnCityAndCategory/";
    public static final String URL_ContractorServices = URL_MainFORAll + "Search/SearchMobileData";
    public static final String URL_GSearchMobileData = URL_MainFORAll + "company/GetCompanyProfilePics";
    public static final String URL_GetCompanyProfilePics = URL_MainFORAll + "company/GetCompanyProfilePics";
    public static final String URL_UpdateCompanyProfilePics = URL_MainFORAll + "company/UpdateCompanyProfilePics";



    public static final String URL_ADDVENDOR = URL_MainFORAll + "vendor/addvendor";

    /***
     * {
     "Vendor_Id":"236",
     "Company_Id":"40010",
     "Login_Id":"24",
     "App_Source":"1"
     }
     */

    public static final String URL_DELETEVENDOR = URL_MainFORAll + "vendor/deletevendor";
    /**
     * {
     * "id":"1",
     * "Login_Id":"24",
     * "App_Source":"1"
     * }
     */
    public static final String URL_SHOWCOMPANYVENDORS = URL_MainFORAll + "vendor/showcompanyvendors";


  //public static final String URL_SHOWCOMPANYVENDORS = URL_MainFORAll + "vendor/showcompanyvendors";


  /**
     * {
     * "Company_Id":"35091"
     * }
     */

    public static final String URL_SHOWVENDOROFCOMPANY = URL_MainFORAll + "vendor/showvendorofcompany";

    /**
     * {
     * "Company_Id":"40009"
     * }
     */


    public static final String URL_HIDENSHOWCOMPANY = URL_MainFORAll + "vendor/hidenshowcompany";
    /**
     * {
     * "id":"2",
     * "Action":"0",
     * "Login_Id":"24",
     * "App_Source":"1"
     * }
     */

    public static final String URL_HIDENSHOWVENDOR = URL_MainFORAll + "vendor/hidenshowvendor";

    /**
     * {
     * "id":"2",
     * "Action":"0",
     * "Login_Id":"24",
     * "App_Source":"1"
     * }
     */

    public static final String URL_SHOWALLVENDORS = URL_MainFORAll + "vendor/showallvendors";
    /***
     * {
     "Company_Id":"40010"
     }
     */

    public static final String URL_ROLLBACKVENDOR = URL_MainFORAll + "vendor/rollbackvendor";
    /***
     * {
     "id":"1",
     "Login_Id":"24",
     "App_Source":"1"
     }
     */

    public static final String URL_SHOWACTIVEVENDOROFCOMPANY = URL_MainFORAll + "vendor/showactivevendorofcompany";

  public static final String URL_getallowedbranch = URL_MainFORAll + "vendor/GetAllowedVendorBranchListForBranch";

    public static final String URL_addallowedvendorbranch = URL_MainFORAll + "vendor/addallowedvendorbranch";

  public static final String URL_FilterVendorsFromSearchResult = URL_MainFORAll + "search/FilterVendorsFromSearchResult";

    public static final String URL_getnotificationslist = URL_MainFORAll + "notification/getnotificationslist/";

    public static final String URL_postedvehicledetail = URL_MainFORAll + "postvehicle/postedvehicledetail/";

    public static final String URL_postedgoodsdetail = URL_MainFORAll + "postgoods/postedgoodsdetail/";

    public static final String URL_markasread = URL_MainFORAll + "notification/markasread";

    public static final String URL_deletebyid = URL_MainFORAll + "notification/deletebyid";

    public static final String URL_onoff = URL_MainFORAll + "notification/onoff";

    public static final String URL_GetCompanyRatingDetail = URL_MainFORAll + "company/GetCompanyRatingDetail/";

    //http://api.tpnagar.co.in/api/V1/company/GetCompanyRatingDetail/<company Id>

    /*{
        "CompanyId":"236",
            "RequestType":"1"
    }*/
    public static final String URL_blocknotification = URL_MainFORAll + "notification/blocknotification";

    /*{*/
    /*    "CompanyId":"236",*/
    /*        "BlockedCompanyId":"39768",*/
    /*        "BlockingNote":""*/
    //}                                              /**/


    public static final String URL_unblocknotification= URL_MainFORAll + "notification/unblocknotification";


  /*  {
        "CompanyId":"236",
            "BlockedCompanyId":"39768",
            "UnBlockingNote":""
    }*/

    public static final String URL_notificationlistbycompany= URL_MainFORAll + "notification/notificationlistbycompany/";

    public static final String URL_getblockedcompanylist= URL_MainFORAll + "notification/getblockedcompanylist/";

    public static final String URL_notificationsofcompany= URL_MainFORAll + "notification/notificationsofcompany/";

    public static final String URL_deletebycompanyid= URL_MainFORAll + "notification/deletebycompanyid";

    public static final String URL_markasreadcompany= URL_MainFORAll + "notification/markasreadcompany";
    public static final String URL_getcompanyvendor= URL_MainFORAll + "vendor/getcompanyvendor";



  //  http://api.tpnagar.co.in/api/V1/notification/markasreadcompany


    //http://api.tpnagar.co.in/api/V1/notification/deletebycompanyid

    //http://api.tpnagar.co.in/api/V1/notification/notificationsofcompany/24/28937
    //http://api.tpnagar.co.in/api/V1/notification/notificationlistbycompany/24
}

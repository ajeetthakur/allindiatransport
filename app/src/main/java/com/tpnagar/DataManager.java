package com.tpnagar;

import com.tpnagar.wrapper.BranchWrapper;
import com.tpnagar.wrapper.CitiesOfStateWrapper;
import com.tpnagar.wrapper.CompanyTypeWrapper;
import com.tpnagar.wrapper.GetComapnyServiceWrapper;
import com.tpnagar.wrapper.GetKeywordWrapper;
import com.tpnagar.wrapper.SearchCompanyWrapper;
import com.tpnagar.wrapper.StateWrapper;

import java.util.ArrayList;

/**
 * Created by admin on 1/9/2017.
 */

public class DataManager {

    public ArrayList<BranchWrapper> getBranchWrapperlist() {
        return branchWrapperlist;
    }

    public void setBranchWrapperlist(ArrayList<BranchWrapper> branchWrapperlist) {
        this.branchWrapperlist = branchWrapperlist;
    }

    public CharSequence[] getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(CharSequence[] serviceItem) {
        this.serviceItem = serviceItem;
    }

    CharSequence[]  serviceItem;

    ArrayList<BranchWrapper> branchWrapperlist = new ArrayList<BranchWrapper>();

    public ArrayList<String> getCityIds() {
        return cityIds;
    }

    public void setCityIds(ArrayList<String> cityIds) {
        this.cityIds = cityIds;
    }

    ArrayList<String> cityIds = new ArrayList<String>();


    public ArrayList<String> getCity() {
        return city;
    }

    public void setCity(ArrayList<String> city) {
        this.city = city;
    }

    ArrayList<String> city = new ArrayList<String>();

    public ArrayList<String> getCityBroker() {
        return cityBroker;
    }

    public void setCityBroker(ArrayList<String> cityBroker) {
        this.cityBroker = cityBroker;
    }

    ArrayList<String> cityBroker = new ArrayList<String>();



    public ArrayList<SearchCompanyWrapper> getSearchCompanyWrapper() {
        return searchCompanyWrapper;
    }

    public void setSearchCompanyWrapper(ArrayList<SearchCompanyWrapper> searchCompanyWrapper) {
        this.searchCompanyWrapper = searchCompanyWrapper;
    }

    public ArrayList<SearchCompanyWrapper> getSearchCompanyBrokerWrapper() {
        return searchCompanyBrokerWrapper;
    }

    public void setSearchCompanyBrokerWrapper(ArrayList<SearchCompanyWrapper> searchCompanyBrokerWrapper) {
        this.searchCompanyBrokerWrapper = searchCompanyBrokerWrapper;
    }

    ArrayList<SearchCompanyWrapper> searchCompanyBrokerWrapper = new ArrayList<SearchCompanyWrapper>();

    ArrayList<SearchCompanyWrapper> searchCompanyWrapper = new ArrayList<SearchCompanyWrapper>();
    public ArrayList<GetKeywordWrapper> getGetKeywordWrappers() {
        return getKeywordWrappers;
    }

    public void setGetKeywordWrappers(ArrayList<GetKeywordWrapper> getKeywordWrappers) {
        this.getKeywordWrappers = getKeywordWrappers;
    }

    ArrayList<GetKeywordWrapper> getKeywordWrappers=new ArrayList<GetKeywordWrapper>();
    public ArrayList<GetComapnyServiceWrapper> getGetComapnyServiceWrapperlist() {
        return getComapnyServiceWrapperlist;
    }

    public void setGetComapnyServiceWrapperlist(ArrayList<GetComapnyServiceWrapper> getComapnyServiceWrapperlist) {
        this.getComapnyServiceWrapperlist = getComapnyServiceWrapperlist;
    }

    ArrayList<GetComapnyServiceWrapper> getComapnyServiceWrapperlist = new ArrayList<GetComapnyServiceWrapper>();

    ArrayList<StateWrapper> stateWrapper = new ArrayList<StateWrapper>();
    ArrayList<CitiesOfStateWrapper> citiesOfStateWrapper = new ArrayList<CitiesOfStateWrapper>();

    public ArrayList<CitiesOfStateWrapper> getCitiesOfStateWrapperTO() {
        return citiesOfStateWrapperTO;
    }

    public void setCitiesOfStateWrapperTO(ArrayList<CitiesOfStateWrapper> citiesOfStateWrapperTO) {
        this.citiesOfStateWrapperTO = citiesOfStateWrapperTO;
    }

    ArrayList<CitiesOfStateWrapper> citiesOfStateWrapperTO = new ArrayList<CitiesOfStateWrapper>();
    ArrayList<CompanyTypeWrapper> CcompanyTypeWrapper = new ArrayList<CompanyTypeWrapper>();
    private static DataManager dManager;

    public static DataManager getInstance() {

        if (dManager != null)
            return dManager;

        else {
            dManager = new DataManager();
            return dManager;
        }
    }

    public static void getInstance_1() {

        if (dManager != null)
            dManager = null;


    }

    public ArrayList<StateWrapper> getStateWrapper() {
        return stateWrapper;
    }

    public void setStateWrapper(ArrayList<StateWrapper> stateWrapper) {
        this.stateWrapper = stateWrapper;
    }

    public ArrayList<CitiesOfStateWrapper> getCitiesOfStateWrapper() {
        return citiesOfStateWrapper;
    }

    public void setCitiesOfStateWrapper(ArrayList<CitiesOfStateWrapper> citiesOfStateWrapper) {
        this.citiesOfStateWrapper = citiesOfStateWrapper;
    }

    public ArrayList<CompanyTypeWrapper> getCcompanyTypeWrapper() {
        return CcompanyTypeWrapper;
    }

    public void setCcompanyTypeWrapper(ArrayList<CompanyTypeWrapper> ccompanyTypeWrapper) {
        CcompanyTypeWrapper = ccompanyTypeWrapper;
    }

}
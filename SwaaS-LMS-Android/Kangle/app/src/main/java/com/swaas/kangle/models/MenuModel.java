package com.swaas.kangle.models;

import java.io.Serializable;

/**
 * Created by Sayellessh on 28-11-2018.
 */

public class MenuModel implements Serializable {

    public int Menu_id;
    public String Menu_Name;
    public String URL;
    public String default_label;
    public String default_thumbnail_URL;
    public String updated_URL;
    public int language_id;
    public String role_id;
    public int Company_id;
    public String updated_label;
    public int user_id;
    public String language_culture;
    public int menu_type;

    public int getMenu_id() {
        return Menu_id;
    }

    public void setMenu_id(int menu_id) {
        Menu_id = menu_id;
    }

    public String getMenu_Name() {
        return Menu_Name;
    }

    public void setMenu_Name(String menu_Name) {
        Menu_Name = menu_Name;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getDefault_label() {
        return default_label;
    }

    public void setDefault_label(String default_label) {
        this.default_label = default_label;
    }

    public String getDefault_thumbnail_URL() {
        return default_thumbnail_URL;
    }

    public void setDefault_thumbnail_URL(String default_thumbnail_URL) {
        this.default_thumbnail_URL = default_thumbnail_URL;
    }

    public String getUpdated_URL() {
        return updated_URL;
    }

    public void setUpdated_URL(String updated_URL) {
        this.updated_URL = updated_URL;
    }

    public int getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(int language_id) {
        this.language_id = language_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public int getCompany_id() {
        return Company_id;
    }

    public void setCompany_id(int company_id) {
        Company_id = company_id;
    }

    public String getUpdated_label() {
        return updated_label;
    }

    public void setUpdated_label(String updated_label) {
        this.updated_label = updated_label;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getLanguage_culture() {
        return language_culture;
    }

    public void setLanguage_culture(String language_culture) {
        this.language_culture = language_culture;
    }

    public int getMenu_type() {
        return menu_type;
    }

    public void setMenu_type(int menu_type) {
        this.menu_type = menu_type;
    }
}

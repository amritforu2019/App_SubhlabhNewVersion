package com.vastu.shubhlabhvastu.Model;

public class ModelAllExpert {

    private String id;
    private String t_name;
    private String c_typ;
    private String image;
    private String edu_year;
    private String rating;

    public ModelAllExpert(String id, String t_name, String c_typ, String image, String edu_year, String rating) {
        this.id = id;
        this.t_name = t_name;
        this.c_typ = c_typ;
        this.image = image;
        this.edu_year = edu_year;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getT_name() {
        return t_name;
    }

    public void setT_name(String t_name) {
        this.t_name = t_name;
    }

    public String getC_typ() {
        return c_typ;
    }

    public void setC_typ(String c_typ) {
        this.c_typ = c_typ;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEdu_year() {
        return edu_year;
    }

    public void setEdu_year(String edu_year) {
        this.edu_year = edu_year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}

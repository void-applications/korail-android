package org.personal.korail_android.item;

public class FacilityReviewItem {
    String id;
    String review;
    String date;
    int star;

    public FacilityReviewItem(String id, String review, String date, int star) {
        this.id = id;
        this.review = review;
        this.date = date;
        this.star = star;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}

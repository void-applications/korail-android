package org.personal.korail_android.item;

public class FacilityReviewItem {
    String review;
    String date;
    int star;

    public FacilityReviewItem(String review, String date, int star) {
        this.review = review;
        this.date = date;
        this.star = star;
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

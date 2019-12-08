package com.udacity.course3.reviews.entityMongo;

import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.Review;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;


@Document("reviews")
public class ReviewMongo {

    @Id
    private String reviewId;

    private String reviewText;

    private int productId;


    private List<String> comments;

    public  ReviewMongo() {

    }

    public ReviewMongo(String reviewId, String reviewText, List<String> comments){
        this.reviewId = reviewId;
        this.reviewText = reviewText;
        this.comments = comments;
    }

    //create document from review object
    public  ReviewMongo(Review review){
        this.reviewId = String.valueOf(review.getReviewId());
        this.productId = review.getProduct().getProductId();
        this.reviewText = review.getReviewText();
        if (review.getComments() != null) {
            for (Comment comment : review.getComments()) {
                this.setComments(Arrays.asList(comment.getCommentText()));
            }
        }
    }


    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public int getProductd() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public void setReviews(List<String> comments) {
        this.comments = comments;
    }

}


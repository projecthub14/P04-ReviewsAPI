package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.entityMongo.ReviewMongo;
import com.udacity.course3.reviews.mongodbRepo.ReviewMongoRepository;
import com.udacity.course3.reviews.repository.CommentRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Spring REST controller for working with comment entity.
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {


    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ReviewMongoRepository reviewMongoRepository;

    /**
     * Creates a comment for a review.
     *
     * 1. Add argument for comment entity. Use {@link RequestBody} annotation.
     * 2. Check for existence of review.
     * 3. If review not found, return NOT_FOUND.
     * 4. If found, save comment.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.POST)
    public ResponseEntity<String> createCommentForReview( @PathVariable("reviewId") Integer reviewId, @Valid @RequestBody Comment comment) {
        Optional<Review> review = reviewRepository.findById(reviewId);


        Review theReview = null;

        if(review.isPresent()) {
            theReview = review.get();
            comment.setReview(theReview);
            commentRepository.save(comment);

            if(theReview.getComments() == null){
                theReview.setComments(Arrays.asList(comment));
            }
            else{
                List<Comment> comments = theReview.getComments();
                comments.add(comment);
                theReview.setComments(comments);

            }
            reviewMongoRepository.save(new ReviewMongo(theReview));
            return new ResponseEntity<>(HttpStatus.CREATED);

        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }

    /**
     * List comments for a review.
     *
     * 2. Check for existence of review.
     * 3. If review not found, return NOT_FOUND.
     * 4. If found, return list of comments.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.GET)
    public ResponseEntity<?> listCommentsForReview(@PathVariable("reviewId") Integer reviewId) {
        Optional<ReviewMongo> review = reviewMongoRepository.findById(String.valueOf(reviewId));

        ReviewMongo theReview = null;

        if(review.isPresent()) {
            theReview = review.get();
            List<String> comments = theReview.getComments();
            return new ResponseEntity<>(comments,HttpStatus.FOUND);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
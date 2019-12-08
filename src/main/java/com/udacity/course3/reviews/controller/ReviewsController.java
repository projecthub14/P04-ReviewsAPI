package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.entityMongo.ReviewMongo;
import com.udacity.course3.reviews.mongodbRepo.ReviewMongoRepository;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Spring REST controller for working with review entity.
 */
@RestController
public class ReviewsController {


    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ReviewMongoRepository reviewMongoRepository;

    /**
     * Creates a review for a product.
     * <p>
     * 1. Add argument for review entity. Use {@link RequestBody} annotation.
     * 2. Check for existence of product.
     * 3. If product not found, return NOT_FOUND.
     * 4. If found, save review.
     *
     * @param productId The id of the product.
     * @return The created review or 404 if product id is not found.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.POST)
    public ResponseEntity<?> createReviewForProduct(@PathVariable("productId") Integer productId, @RequestBody Review review) {
        Optional<Product> product = productRepository.findById(productId);

        Product theProduct = null;

        if(product.isPresent()) {
            theProduct = product.get();
                review.setProduct(theProduct);

            Review reviewSql = reviewRepository.save(review);
            reviewMongoRepository.save(new ReviewMongo(reviewSql));

            return  new ResponseEntity<>(reviewSql,HttpStatus.CREATED);

        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Lists reviews by product.
     *
     * @param productId The id of the product.
     * @return The list of reviews.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.GET)
    public ResponseEntity<List<ReviewMongo>> listReviewsForProduct(@PathVariable("productId") Integer productId) {

        Optional<Product> product = productRepository.findById(productId);
        Product theProduct = null;

        if(product.isPresent()) {
            theProduct = product.get();
            List<Review> reviews = theProduct.getReviews();
            List<Integer> reviewIds = reviews.stream().map(review -> review.getReviewId())
                                          .collect(Collectors.toList());
            List<ReviewMongo> reviewMongoList = reviewIds.stream().
                    map( id -> {
                        Optional<ReviewMongo> reviewMongo =  reviewMongoRepository.findById(String.valueOf(id));
                        if(reviewMongo.isPresent())
                        {
                            return reviewMongo.get();
                        }
                        return null;
                    }).collect(Collectors.toList());
            return new ResponseEntity<>(reviewMongoList,HttpStatus.FOUND);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
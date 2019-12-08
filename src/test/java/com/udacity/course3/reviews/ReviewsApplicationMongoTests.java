package com.udacity.course3.reviews;

import com.udacity.course3.reviews.entityMongo.ReviewMongo;
import com.udacity.course3.reviews.mongodbRepo.ReviewMongoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class ReviewsApplicationMongoTests {


	@Autowired
	ReviewMongoRepository reviewMongoRepository;


	@Test
	public void create_fetch_review_comment(){

		//Create review
		ReviewMongo review = new ReviewMongo();

		review.setProductId(1);
		review.setReviewText("I like the color");
		review.setComments(Arrays.asList("ok great","great"));


		//save the review
		reviewMongoRepository.save(review);

		//get the review by id
		Optional<ReviewMongo> reviewTest = reviewMongoRepository.findById(review.getReviewId());
		if(reviewTest.isPresent()){
			assertThat(reviewTest).isNotNull();
		}

		//test to get review by id
		assertEquals(review.getReviewId(),reviewTest.get().getReviewId());

		//test to check reviewText
		assertEquals(reviewTest.get().getReviewText(),"I like the color");

		//test to check comments size
		assertEquals(reviewTest.get().getComments().size(),2);
	}




}
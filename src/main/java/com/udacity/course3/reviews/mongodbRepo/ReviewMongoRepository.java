package com.udacity.course3.reviews.mongodbRepo;


import com.udacity.course3.reviews.entityMongo.ReviewMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewMongoRepository extends MongoRepository<ReviewMongo,String> {


}

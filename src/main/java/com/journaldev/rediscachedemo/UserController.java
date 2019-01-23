package com.journaldev.rediscachedemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepository userRepository;

	/*
	 * In the above mapping, getUser method will put a person into a cache named as
	 * ‘users’, identifies that person by the key as ‘userId’ and will only store a
	 * user with followers greater than 12000. This makes sure that cache is
	 * populated with users who are very popular and are often queried for.
	 */
	//@Cacheable(value = "users", key = "#userId", unless = "#result.followers<1200")
	@Cacheable("soumyadeep")
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public User getUser(@PathVariable String userId) {
		LOG.info("Inside method {}.", Thread.currentThread().getStackTrace()[1].getMethodName());
		return userRepository.findOne(Long.valueOf(userId));
	}
	
	@CachePut(value = "users", key = "#user.id")
	@PutMapping("/update")
	public User updatePersonByID(@RequestBody User user) {
		LOG.info("Inside method {}.", Thread.currentThread().getStackTrace()[1].getMethodName());
		userRepository.save(user);
		return user;
	}

	//@CacheEvict(value="users", allEntries=true)
	@CacheEvict("soumyadeep")
	@DeleteMapping("/{userId}")
	public void deleteUserByID(@PathVariable Long userId) {
		LOG.info("Inside method {}.", Thread.currentThread().getStackTrace()[1].getMethodName());
		LOG.info("deleting person with id {}", userId);
		userRepository.delete(userId);
	}
}

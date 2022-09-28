package com.intern.restwebservices.restfulwebservices.user.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.intern.restwebservices.restfulwebservices.user.dao.UserDaoService;
import com.intern.restwebservices.restfulwebservices.user.exception.UserNotFoundException;
import com.intern.restwebservices.restfulwebservices.user.model.User;

@RestController
public class UserResource {
	
	@Autowired
	private UserDaoService service;
	
	public UserResource(UserDaoService service) {
		this.service = service;
	}

	@GetMapping("/users")
	public List<User> retrieveAllUsers(){
		return service.findAll();
	}
	
	
	  @GetMapping("/users/{id}") 
	  public EntityModel<User> retrieveUser(@PathVariable int id){ 
		  User user = service.findOne(id);
		  if(user==null) {
			  throw new UserNotFoundException("id-" + id);
		  }
		  EntityModel<User> entityModel = EntityModel.of(user);
		  WebMvcLinkBuilder linkToUsers = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		  entityModel.add(linkToUsers.withRel("all-users"));
		return entityModel;
		}
	  
	 @PostMapping("/users")
	 public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User savedUser = service.save(user);
		// After creating the user, want to return a status CREATED and the new User id
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	 }
	 
	 @DeleteMapping("/users/{id}") 
	  public void deleteUser(@PathVariable int id){ 
		  User user = service.deleteById(id);
		  
		  if(user==null) {
			  throw new UserNotFoundException("id-" + id);
		  }
		  
		}
}

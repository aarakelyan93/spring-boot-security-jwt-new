package com.example.repository;

import com.example.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  List<User> findAllByDeletedDateNull();

  Optional<User> findByIdAndDeletedDateNull(Long id);

  Optional<User> findByUsernameAndDeletedDateNull(String username);

}

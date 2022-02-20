package com.example.repository;

import com.example.entity.Role;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

  Optional<Role> findByAuthority(String authority);

}

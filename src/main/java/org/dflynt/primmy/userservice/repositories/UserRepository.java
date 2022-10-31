package org.dflynt.primmy.userservice.repositories;

import org.dflynt.primmy.userservice.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByuserid(String userid);
    User findByemail(String email);
    User findByuseridAndVerificationCode(String userid, String verificationCode);
    boolean existsByemail(String email);

    @Modifying
    @Query(value = "UPDATE user " +
                    "SET enabled = 1, enableddate = CURRENT_TIMESTAMP() " +
                    "WHERE userid = :userId and verificationCode = :verificationCode" , nativeQuery = true)
    int enableUser(@Param("userId") String userid, @Param("verificationCode") String verificationCode);

    int deleteByuserid(String userid);

}

package com.lyq.eca.dao;

import com.lyq.eca.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UserDao extends JpaRepository<User, Integer> {
    List<User> findAll();

    User findByUsername(String username);

    User getByUsernameAndPassword(String username, String password);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    int getlast();

    @Query(value = "select u from User u where u.username like %:name%")
    List<User> search(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("update User set username=:name where id=:id")
    void updateUser(@Param("name") String name,@Param("id") int id);

    @Transactional
    @Modifying
    void deleteById(int id);

    @Query(value = "select username from user",nativeQuery = true)
    List<Object> getUsernames();
}

package com.Yana.Buddy.repository;

import com.Yana.Buddy.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(User user) {
        // TODO: 데이터베이스에 user의 정보가 들어가야함!
        entityManager.persist(user);
        entityManager.flush();
        entityManager.close();
    }

    public Optional<User> findByEmail(String email) {
        List<User> user = entityManager.createQuery("SELECT user FROM User As user WHERE user.email= :email",User.class)
                .setParameter("email",email).getResultList();
        return user.stream().findAny();

    }

    public Optional<User> findByNickname(String nickname) {
        List<User> user = entityManager.createQuery("SELECT user FRom User As user WHERE user.nickname= :nickname",User.class)
                .setParameter("nickname",nickname)
                .getResultList();
        return user.stream().findAny();
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class,id));
    }

    public void deleteById(Long id) {
        // 지우고자하는 객체를 찾음
        User user = entityManager.find(User.class,id);
        // 지우고자하는 객체 삭제
        entityManager.remove(user);
        entityManager.flush();
        entityManager.close();
    }

}

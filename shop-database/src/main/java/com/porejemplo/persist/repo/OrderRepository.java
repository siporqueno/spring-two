package com.porejemplo.persist.repo;

import com.porejemplo.persist.model.Order;
import com.porejemplo.persist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUser(User user);

    @Query("select distinct o " +
            "from Order o " +
            "left join fetch o.orderItems")
    List<Order> findAllByUserWithOrderItemsFetch(User user);
}

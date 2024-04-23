package com.badis.orderservice.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String OrderNumber;
    @OneToMany(cascade = CascadeType.ALL) // mappedBy = "order" : order is the name of the field in
                                                             // OrderLineItems class (private Order order;)
                                                             // cascade = CascadeType.ALL : if we delete an order, all
                                                             // the orderLineItems will be deleted too (if we don't
                                                             // specify this, we will get an error
    private List<OrderLineItems> orderLineList;

}

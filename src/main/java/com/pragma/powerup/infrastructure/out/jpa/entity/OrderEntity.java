package com.pragma.powerup.infrastructure.out.jpa.entity;

import com.pragma.powerup.domain.model.OrderStatus;
import com.pragma.powerup.infrastructure.out.jpa.converter.OrderStatusConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "PEDIDOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_cliente")
    private Long idClient;

    @Column(name = "fecha")
    private LocalDate date;

    @Column(name = "estado")
    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus status ;

    @Column(name = "id_chef")
    private Long idChef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_restaurante")
    private RestaurantEntity restaurant;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderPlateEntity> plates;
}

package com.pragma.powerup.infrastructure.out.jpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "EMPLEADOS_RESTAURANTES")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantEmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usuario", nullable = false)
    private Long idUser;

    @Column(name = "id_restaurante", nullable = false)
    private Long idRestaurant;
}

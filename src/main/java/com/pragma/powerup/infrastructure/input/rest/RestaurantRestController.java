package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.handler.impl.RestaurantHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantRestController {
    private final RestaurantHandler restaurantHandler;
}

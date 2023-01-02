package com.example.demo.domain;

import com.github.javafaker.Faker;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Client {
    private static final Faker FAKER = Faker.instance();
    public final String name;
    public final String address;

    public static Client random()
    {
        return Client.builder()
        .name(FAKER.name().name())
        .address(FAKER.address().fullAddress())
        .build();
    }
}

package com.company.web.springdemo;

import com.company.web.springdemo.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Helpers {

    public static User createMockAdmin() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        return mockUser;
    }

    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("MockUsername");
        mockUser.setPassword("MockPassword");
        mockUser.setLastName("MockLastName");
        mockUser.setFirstName("MockFirstName");
        mockUser.setEmail("mock@user.com");
        return mockUser;
    }

    public static Beer createMockBeer() {
        var mockBeer = new Beer();
        mockBeer.setId(1);
        mockBeer.setName("MockBeer");
        mockBeer.setCreatedBy(createMockUser());
        mockBeer.setStyle(createMockStyle());
        return mockBeer;
    }

    public static Style createMockStyle() {
        var mockStyle = new Style();
        mockStyle.setId(1);
        mockStyle.setName("MockStyle");
        return mockStyle;
    }

    public static FilterOptions createMockFilterOptions() {
        return new FilterOptions(
                "name",
                0.0,
                10.0,
                1,
                "sort",
                "order");
    }

    public static BeerDto createBeerDto() {
        BeerDto dto = new BeerDto();
        dto.setStyleId(1);
        dto.setName("MockBeer");
        dto.setAbv(4.5);
        return dto;
    }

    /**
     * Accepts an object and returns the stringified object.
     * Useful when you need to pass a body to a HTTP request.
     */
    public static String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

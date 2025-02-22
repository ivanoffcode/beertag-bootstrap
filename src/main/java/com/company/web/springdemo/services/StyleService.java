package com.company.web.springdemo.services;

import com.company.web.springdemo.models.Style;

import java.util.List;

public interface StyleService {

    List<Style> get();

    Style get(int id);

}

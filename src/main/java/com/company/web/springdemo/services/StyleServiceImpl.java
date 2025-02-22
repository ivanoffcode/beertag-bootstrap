package com.company.web.springdemo.services;

import com.company.web.springdemo.models.Style;
import com.company.web.springdemo.repositories.StyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StyleServiceImpl implements StyleService {

    private final StyleRepository repository;

    @Autowired
    public StyleServiceImpl(StyleRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Style> get() {
        return repository.get();
    }

    @Override
    public Style get(int id) {
        return repository.get(id);
    }

}

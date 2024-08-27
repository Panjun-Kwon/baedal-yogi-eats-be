package com.fstuckint.baedalyogieats.client.example;

import com.fstuckint.baedalyogieats.client.example.model.ExampleClientResult;

import org.springframework.stereotype.Component;

@Component
public class ExampleClient {

    private final ExampleApi exampleApi;

    public ExampleClient(ExampleApi exampleApi) {
        this.exampleApi = exampleApi;
    }

    public ExampleClientResult example(String exampleParameter) {
        ExampleRequestDto request = new ExampleRequestDto(exampleParameter);
        return exampleApi.example(request).toResult();
    }

}

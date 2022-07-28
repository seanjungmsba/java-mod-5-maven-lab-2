package org.example.singleton;

import lombok.*;

@Getter
@Setter
public class ExampleLombok {

    private String name;
    private int age;

    public ExampleLombok() {this.getAge();}
    
}

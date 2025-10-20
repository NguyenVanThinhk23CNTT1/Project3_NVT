package com.example.lession01_spring_boot.pkg_default_method;

public interface Shape {
     void draw();
    default void setcolor (String color ){
    System.out.println("Vẽ hình với màu:"+color);
    }
}

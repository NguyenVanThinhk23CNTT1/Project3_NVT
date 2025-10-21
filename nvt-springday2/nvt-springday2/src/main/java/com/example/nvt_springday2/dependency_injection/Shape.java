package com.example.nvt_springday2.dependency_injection;

public interface Shape {
    void draw();
}

// Concrete Class 1
class CircleShape implements Shape {
    @Override
    public void draw() {
        System.out.println("CircleShape draw");
    }
}

// Concrete Class 2
class RectangleShape implements Shape {
    @Override
    public void draw() {
        System.out.println("RectangleShape draw");
    }
}
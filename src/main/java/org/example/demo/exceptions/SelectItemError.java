package org.example.demo.exceptions;

public class SelectItemError extends Throwable {
    public SelectItemError() {
        super ("No item is selected.");
    }
}

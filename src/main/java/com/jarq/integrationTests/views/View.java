package com.jarq.integrationTests.views;

import java.util.Scanner;

public abstract class View {

    public void displayMessage(String msg) {
        System.out.println(msg);
    }

    public String getUserInput(String msg) {
        Scanner scanner = new Scanner(System.in);
        displayMessage(msg);
        return scanner.nextLine();
    }
}

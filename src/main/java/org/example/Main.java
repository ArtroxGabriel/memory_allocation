package org.example;

import org.example.shell.Shell;

public class Main {
    public static void main(String[] args) {
        int exitCode;

        try (Shell shell = new Shell()) {
            exitCode = shell.start();
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
            exitCode = 1;
        }

        System.exit(exitCode);
    }
}
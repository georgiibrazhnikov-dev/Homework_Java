package ru.vsu.atm.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.vsu.atm.controller.AtmConsole;

@Component
public class AtmRunner implements CommandLineRunner {
    private final AtmConsole atmConsole;

    public AtmRunner(AtmConsole atmConsole) {
        this.atmConsole = atmConsole;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== AtmRunner started ===");
        atmConsole.start();
    }
}
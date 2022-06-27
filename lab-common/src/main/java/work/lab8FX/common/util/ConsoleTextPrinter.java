package work.lab8FX.common.util;

import work.lab8FX.common.abstractions.AbstractTextPrinter;

public class ConsoleTextPrinter extends AbstractTextPrinter {

    public void printText(String text) {
        System.out.print(text);
    }

    public void printlnText(String text) {
        System.out.println(text);
    }
}

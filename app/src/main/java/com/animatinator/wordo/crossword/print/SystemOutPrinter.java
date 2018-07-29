package com.animatinator.wordo.crossword.print;

public class SystemOutPrinter implements StringOutput {
    @Override
    public void outputString(String string) {
        System.out.println(string);
    }
}

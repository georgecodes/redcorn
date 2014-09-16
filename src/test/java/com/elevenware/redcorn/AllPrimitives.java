package com.elevenware.redcorn;

public class AllPrimitives {

    private Long id;
    private boolean theBoolean;
    private short theShort;
    private int theInt;
    private long theLong;
    private double theDouble;
    private float theFloat;
    private char theChar;
    private String theString;

    public AllPrimitives() {}

    public AllPrimitives(boolean theBoolean, short theShort, int theInt, long theLong, double theDouble, float theFloat, char theChar, String theString) {
        this.theBoolean = theBoolean;
        this.theShort = theShort;
        this.theInt = theInt;
        this.theLong = theLong;
        this.theDouble = theDouble;
        this.theFloat = theFloat;
        this.theChar = theChar;
        this.theString = theString;
    }

    public boolean isTheBoolean() {
        return theBoolean;
    }

    public void setTheBoolean(boolean theBoolean) {
        this.theBoolean = theBoolean;
    }

    public short getTheShort() {
        return theShort;
    }

    public void setTheShort(short theShort) {
        this.theShort = theShort;
    }

    public int getTheInt() {
        return theInt;
    }

    public void setTheInt(int theInt) {
        this.theInt = theInt;
    }

    public long getTheLong() {
        return theLong;
    }

    public void setTheLong(long theLong) {
        this.theLong = theLong;
    }

    public double getTheDouble() {
        return theDouble;
    }

    public void setTheDouble(double theDouble) {
        this.theDouble = theDouble;
    }

    public float getTheFloat() {
        return theFloat;
    }

    public void setTheFloat(float theFloat) {
        this.theFloat = theFloat;
    }

    public char getTheChar() {
        return theChar;
    }

    public void setTheChar(char theChar) {
        this.theChar = theChar;
    }

    public String getTheString() {
        return theString;
    }

    public void setTheString(String theString) {
        this.theString = theString;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



}

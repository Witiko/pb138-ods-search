/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.odsSearch.common;

/**
 * This class represents an exception encountered while opening an Open Document
 * Spreadsheet file.
 * @author Vít Novotný <witiko@mail.muni.cz>
 */
public class SpreadsheetImplException extends Exception {
    public SpreadsheetImplException() { super(); }
    public SpreadsheetImplException(String message) { super(message); }
    public SpreadsheetImplException(String message, Throwable cause) { super(message, cause); }
    public SpreadsheetImplException(Throwable cause) { super(cause); }
}

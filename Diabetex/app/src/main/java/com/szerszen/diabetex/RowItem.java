package com.szerszen.diabetex;

import android.inputmethodservice.Keyboard;
import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Klasa rzędu w tablicy produktów pokazująca co zawiera jeden rząd w tablicy,
 * Klasa zawiera również gettery i settery dla wszystkich wartości
 *
 * Created by Monika Ryczko on 1/8/18.
 * @author Monika Ryczko
 */

public class RowItem {

    /** Cechy którymi charakteryzuje się każdy rząd w tablicy czyli każdy produkt*/
    private String product_name;
    private String product_IG;
    private String product_kcal;
    private int product_pic_id;

    /**
     * Tworzy obiekt rzędu w tablicy ListProduct
     * @param product_name
     *          Nazwa produktu
     * @param product_pic_id
     *          Identyfikator obrazka produktu
     * @param product_kcal
     *          Wartosc kaloryczna produktu
     * @param product_IG
     *          Wartość IG produktu
     */
    public RowItem(String product_name, int product_pic_id, String product_kcal, String product_IG) {
        this.product_name = product_name;
        this.product_pic_id = product_pic_id;
        this.product_kcal = product_kcal;
        this.product_IG = product_IG;
    }

    /**
     * @return Nazwe produktu
     */
    public String getProduct_name() {
        return product_name;
    }

    /**
     * Przypisuje nową nazwe produktowi
     * @param product_name
     */
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    /**
     * @return Identyfikator obrazka produktu
     */
    public int getProduct_pic_id() {
        return product_pic_id;
    }

    /**
     * Przypisuje nowy identyfikator obrazkowi
     * @param product_pic_id
     */
    public void setProduct_pic_id(int product_pic_id) {
        this.product_pic_id = product_pic_id;
    }

    /**
     * @return Wartość IG produktu
     */
    public String getProduct_IG() {
        return product_IG;
    }

    /**
     * Przypisuje nową wartość IG produktowi
     * @param product_ig
     */
    public void setProduct_IG(String product_ig) {
        this.product_IG = product_ig;
    }

    /**
     * @return Wartość kaloryczna produktu
     */
    public String getProduct_kcal(){
        return product_kcal;
    }

    /**
     * Przypisuje nową wartość kaloryczną produktowi
     * @param product_kcal
     */
    public void setProduct_kcal(String product_kcal) {
        this.product_kcal = product_kcal;
    }

}
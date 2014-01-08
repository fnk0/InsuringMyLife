package com.gabilheri.insuringmylife;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcus on 1/5/14.
 */
public class ListRowGroup {

    public String string;
    public String brand;
    public final List<String> children = new ArrayList<String>();

    public ListRowGroup(String string, String brand) {
        this.string = string;
        this.brand = brand;
    }
}

package com.rafaelsf80.d4w.retail;
import java.util.ArrayList;
/**
 * Created by wilfrid on 11/25/14.
 */
public class ConfigStore {

public ArrayList<Config> list;

    public ConfigStore() {
        list = new ArrayList<Config>();
    }

    public void setData(Config config)
    {
        list.add(0,config);
    }

    public Config getData() {
        return list.get(0);
    }
}

package info.trilok.finagletest.Requests;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by trilok on 8/23/2014.
 */
public class Request {
    public String command;

    public HashMap<String,String> params = new HashMap<String, String>();
}

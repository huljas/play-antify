package controllers;

import play.*;
import play.i18n.Messages;
import play.mvc.*;

import java.util.*;

public class Application extends Controller {

    public static void index() {
        String s1 = "String s1";
        String s2 = Messages.get("s2");
        render(s1, s2);
    }

}
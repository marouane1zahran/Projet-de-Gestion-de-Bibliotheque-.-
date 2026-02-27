
package login;

import java.util.HashMap;


public class IdAndPasswords {
    private HashMap<String, String> logininfo = new HashMap<>();

    public IdAndPasswords() {
       
        logininfo.put("marouane@email.com", SecurityUtils.hashPassword("admin123"));
        logininfo.put("abderrahmane@email.com", SecurityUtils.hashPassword("prof123"));
        
    }
    protected HashMap<String, String> getLoginInfo() {
        return logininfo;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.RMI.shared;

import java.io.Serializable;

/**
 *
 * @author Jeroen Roovers
 */
public class Account implements Serializable {

    private String username;
    private String password;

    public Account() {
    }

    public Account(String Username, String Password) {
        this.username = Username;
        this.password = Password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

package DomumViri.User;

import java.io.Serializable;

public class Account implements Serializable{

    private String userName;
    private Team team;
    /**
     *
     * @param userName
     */
    public Account(String userName) {
        this.userName = userName;
    }

    public Team getTeam() {
        return team;
    }

    public String getUserName() {
        return userName;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}

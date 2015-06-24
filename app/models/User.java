package models;

import javax.persistence.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

import java.util.*;
import play.*;

@Entity
public class User extends Model {
    @Id
    private Long id;
    @Required
    private String username;
    @Required
    public String password;
    public String nickname;
    public String groupname;

    @OneToOne(mappedBy="user")
    @Transient
    @Required
    public String  votekey;

    @OneToOne(mappedBy="user")
    public Vote vote;

    @OneToOne(mappedBy="user")
    public Production production;

    public User() {
    }

    public User(final String userName,
                final String passWord,
                final String nickName,
                final String groupName) {
        setUsername(userName);
        password = passWord;
        nickname = nickName;
        groupname = groupName;
    }

    public User(final String userName,
                final String passWord,
                final String nickName,
                final String groupName,
                final String voteKey) {
        this(userName, passWord,
             nickName, groupName);
        votekey = voteKey;
    }

    public static Map<String,String> options() {
        @SuppressWarnings("unchecked")
        List<User> users = find.where().orderBy("username ASC").findList();
        Logger.debug("--liste des users--");
        Logger.debug(users.toString());
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(User u: users) {
            options.put(u.getId().toString(), u.getUsername());
        }
        return options;
    }

    public static User authenticate(final String userName,
                                    final String passWord) {
        return find.where()
                .eq("username", userName)
                    .eq("password", passWord)
                        .findUnique();
    }

    public String toString() {
        return "obj User: " + getUsername() + " cle vote saisie=" + votekey;
    }

    public static Model.Finder<Long, User> find =
                    new Model.Finder<Long, User>(Long.class, User.class);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

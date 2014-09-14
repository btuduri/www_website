package models;

import javax.persistence.*;

import javax.validation.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;

import com.avaje.ebean.*;

@Entity
public class Votekey extends Model {
    @Id
    public Long id;

    @OneToOne()
    public User user;
    @Required
    @Column(unique=true)
    public String votekey;


    public Votekey() {
    }

    public Votekey(final User aUser,
                   final String voteKey) {
        user = aUser;
        votekey = voteKey;
    }

    public String toString() {
        return "obj Votekey: " + user + "votekey: " + votekey;
    }

    public static Finder<Long, Votekey> find =
                    new Finder<Long, Votekey>(Long.class, Votekey.class);
}

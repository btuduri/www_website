package models;

import javax.persistence.*;

import javax.validation.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;

import com.avaje.ebean.*;
import play.*;

@Entity
public class Vote extends Model {
    @Id
    public Long id;

    @OneToOne()
    public User user;

    @OneToOne()
    public Compo compo;

    @OneToOne()
    public Production production;

    @Required
    public Long note;

    public Vote() {
    }

    public Vote(final User user,
                final Compo compo,
                final Production production,
                final Long note) {
        this.user = user;
        this.compo = compo;
        this.production = production;
        this.note = note;
    }

    public String toString() {
        return "obj Vote: User obj :" +
                user + " Compo obj: " +
                compo + " Production obj : " +
                production + " note obj : " +
                note;
    }

    public static Finder<Long, Vote> find =
                    new Finder<Long, Vote>(Long.class, Vote.class);

}

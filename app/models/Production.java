package models;

import org.joda.time.*;

import java.util.*;
import javax.persistence.*;

import javax.validation.*;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.Constraints.*;

import com.avaje.ebean.*;

@Entity
public class Production extends Model {
    @Id
    public Long id;

    @Required
    public String name;

    @Required
    public String comment;

    @ManyToOne()
    public String user;

    @ManyToOne
    public String compo;

    public String filename;

    @Formats.DateTime(pattern="dd-MM-yyyy HH:mm:ss")
    public Date uploadDate;

    @OneToOne(mappedBy="production")
    public Vote vote;

    public Production() {
    }

    public Production(final String aName,
                      final String aComment,
                      final String aUser,
                      final String aCompo,
                      final String aFilename,
                      final Date aUploadDate) {
        name = aName;
        comment = aComment;
        user = aUser;
        compo = aCompo;
        filename = aFilename;
        uploadDate = aUploadDate;
    }

    public String toString() {
        return "name prod: " + name + " comment: " + comment + " user obj: " + user + "compo: " + compo + " filename: " + filename + "upload date: " + uploadDate;
    }

    public static Model.Finder<Long, Production> find =
                    new Model.Finder<Long, Production>(Long.class, Production.class);
}

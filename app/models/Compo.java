package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.Constraints.*;

import play.*;

@Entity
public class Compo extends Model {
    @Id
    public Long id;

    @Required
    private String name;

    @Formats.DateTime(pattern="dd-MM-yyyy HH:mm:ss")
    public Date startDate;

    @Formats.DateTime(pattern="dd-MM-yyyy HH:mm:ss")
    public Date endDate;

    @Required
    public String directoryPath;

    @Required
    public boolean voteOpen;

    @Required
    public boolean uploadOpen;

    @OneToOne(mappedBy="compo")
    public Vote vote;

    @OneToOne(mappedBy="compo")
    public Production production;

    public Compo() {
    }

    public Compo(final String aName,
                 final Date aStartDate,
                 final Date aEndDate,
                 final String aDirectoryPath,
                 final boolean aVoteOpen,
                 final boolean aUploadOpen) {
        setName(aName);
        startDate = aStartDate;
        endDate = aEndDate;
        directoryPath = aDirectoryPath;
        voteOpen = aVoteOpen;
        uploadOpen = aUploadOpen;
    }



    public static Map<String,String> options() {
        @SuppressWarnings("unchecked")
        List<Compo> compos = find.where().eq("uploadOpen",true).orderBy("name ASC").findList();
        Logger.debug("--liste des compos--");
        Logger.debug(compos.toString());
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Compo c: compos) {
            options.put(c.id.toString(), c.getName());
        }
        return options;
    }

    public String toString() {
        return "compo name: " + getName() +
               " startDate: " + startDate +
               "endDate: " + endDate +
               " directoryPath: " + directoryPath +
               " voteOpen: " + voteOpen +
               " uploadOpen: " + uploadOpen;
    }

    public static Model.Finder<Long, Compo> find =
                    new Model.Finder<Long, Compo>(Long.class, Compo.class);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

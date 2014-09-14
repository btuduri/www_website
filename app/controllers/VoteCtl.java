package controllers;

import play.*;
import play.mvc.*;
import static play.mvc.Http.*;
import play.data.*;
import static play.data.Form.*;

import views.html.voteview.*;
import models.*;

import java.util.*;


public class VoteCtl extends Controller {
    public static Result GO_HOME = redirect(
           routes.VoteCtl.show());

    static Form<Production> formulaire = form(Production.class);

    public static Result index() {
        List<Compo> l = Compo.find.where().eq("uploadOpen", true).findList();
        if (l != null && l.size() > 0) {
            return redirect("/");
        }

        l = Compo.find.where().eq("voteOpen", false).findList();
         if (l != null && l.size() > 0) {
            return redirect("/");
        }

        List<Production> p = Production.find
                                .where()
                                .orderBy("compo ASC").findList();
        List<Compo> lc = new ArrayList<Compo>();
        for (Production e : p) {
            Compo c = Compo.find.where().eq("id",e.compo).findUnique();
            User u = User.find.where().eq("id", e.user).findUnique();
            e.compo = c.name;
            e.user = u.username;
        }
        Logger.debug("----");
        Logger.debug(p.toString());
        Logger.debug("----");

        return ok(creationVote.render(p));
    }

    public static Result show() {
        return index();
    }

    public static Result vote() {
/*
        Form<Production> filledForm = formulaire.bindFromRequest();
        Logger.info(" filledForm created ");
        Logger.info("compo = " + filledForm.field("compo").value());
        Logger.info("name = " + filledForm.field("name").value());
        Logger.info("comment = " + filledForm.field("comment").value());
        Logger.info("user = " + filledForm.field("user").value());

        if (filledForm.hasErrors()) {
           Logger.info("I ve some errors");
            Logger.error(filledForm.error("compo").message());
           return badRequest(
                creationProductionUploadView.render(filledForm)
            );
       }

        Production p = Production.find.where().eq("user",
            filledForm.field("user").value())
                .eq("compo",
                    filledForm.field("compo").value()).findUnique();

        if (p != null) {
            try {
                Logger.debug("---> suppression de l'ancien fichier " + Paths.get("/tmp", p.filename));
                Files.delete(Paths.get("/tmp", p.filename));
            } catch (IOException ioe) {
                Logger.debug(ioe.getMessage());
            }
        }

        MultipartFormData body = request().body().asMultipartFormData();
        FilePart prod = body.getFile("filename");
        Logger.info("filename uploaded = " + prod.getFilename());

        if (prod == null) {
                Logger.debug("probleme upload fichier");
                filledForm.reject("filename", "fichier invalide ou non specifié");
                return badRequest(
                    creationProductionUploadView.render(filledForm)
                );
        }

        try {
            Files.move(prod.getFile().toPath(), Paths.get("/tmp", prod.getFilename()), StandardCopyOption.REPLACE_EXISTING);
       } catch (IOException ioe) {
            Logger.debug(ioe.getMessage());
        }


        if (p == null) 
            p =filledForm.get();
            p.filename = prod.getFilename();
        } else {
            p.name = filledForm.field("name").value();
            p.comment = filledForm.field("comment").value();
            p.filename = prod.getFilename();
        }

        p.save();
*/        return GO_HOME;
    }
}

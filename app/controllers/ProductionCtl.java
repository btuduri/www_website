package controllers;

import play.*;
import play.mvc.*;
import static play.mvc.Http.*;
import play.data.*;
import static play.data.Form.*;

import views.html.productionuploadview.*;
import models.*;

import play.mvc.Http.MultipartFormData.FilePart;
import java.nio.file.*;
import java.io.*;
import java.util.*;

public class ProductionCtl extends Controller {

    public static Result GO_HOME = redirect(
           routes.ProductionCtl.index()); 

    static Form<Production> formulaire = form(Production.class);

    @Security.Authenticated(Secured.class)
    public static Result index() {
        List<Compo> l = Compo.find.where().eq("uploadOpen", true).findList();
        if (l != null && l.size() > 0) {
        return ok(
            listeProductionUploadView.render(
                Production.find.where()
                    .eq("user", Http.Context.current().session().get("userid"))
                        .orderBy("name ASC").findList()));
        } else {
            l = Compo.find.where().eq("voteOpen", true).findList();
            if (l != null && l.size() > 0) {
                return ok(gotoVoteProductionUploadView.render());
            }
            return redirect("/");
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result show() {
        return ok(creationProductionUploadView.render(formulaire));
    }

    @Security.Authenticated(Secured.class)
    public static Result upload() {
        Form<Production> filledForm = formulaire.bindFromRequest();
        Logger.info(" filledForm created ");
        Logger.info("compo = " + filledForm.field("compo").value());
        Logger.info("name = " + filledForm.field("name").value());
        Logger.info("comment = " + filledForm.field("comment").value());
        Logger.info("user = " + filledForm.field("user").value());

        if (filledForm.hasErrors()) {
           Logger.info("I ve some errors");
           return badRequest(
                creationProductionUploadView.render(filledForm)
            );
       }


        Logger.error(Http.Context.current().session().get("userid"));
        Production p = Production.find.where().eq("user",
            Http.Context.current().session().get("userid"))
                .eq("compo",
                    filledForm.field("compo").value()).findUnique();


        Compo comp = Compo.find.where().eq("id",
            filledForm.field("compo").value())
                .findUnique();

        if (comp == null) {
            filledForm.reject("compo", "la compo est invalide !!!");
            return badRequest(
                creationProductionUploadView.render(filledForm)
            );
        }

        String uploadDirectory = comp.directoryPath;
        Logger.info("directoryPath: " + uploadDirectory);

        if (p != null) {
            try {
                Logger.debug("---> suppression de l'ancien fichier " +
                    Paths.get(uploadDirectory, p.filename));
                Files.delete(Paths.get(uploadDirectory, p.filename));
            } catch (IOException ioe) {
                Logger.debug(ioe.getMessage());
            }
        }

        MultipartFormData body = request().body().asMultipartFormData();
        FilePart prod = body.getFile("filename");

        if (prod == null) {
                Logger.debug("probleme upload fichier");
                filledForm.reject("filename", "fichier invalide ou non specifié");
                return badRequest(
                    creationProductionUploadView.render(filledForm)
                );
        }
        Logger.info("filename uploaded = " + prod.getFilename());

        try {
            Files.move(prod.getFile().toPath(),
                Paths.get(uploadDirectory, prod.getFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
       } catch (IOException ioe) {
            Logger.debug(ioe.getMessage());
        }

        if (p == null) {
            p =filledForm.get();
            p.filename = prod.getFilename();
            //p.user = Http.Context.current().session().get("userid");
            //p.save();
        } else {
            p.name = filledForm.field("name").value();
            p.comment = filledForm.field("comment").value();
            p.filename = prod.getFilename();
            //p.user = Http.Context.current().session().get("userid");
            //p.update();
        }

        try {
        p.user.setId( Long.parseLong(Http.Context.current().session().get("userid").toString()));
        } catch (NumberFormatException ex){}
        p.save();

        return GO_HOME;
    }
}

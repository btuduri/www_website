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

    public static Result index() {
        List<Compo> l = Compo.find.where().eq("uploadOpen", true).findList();
        if (l != null && l.size() > 0) {
        return ok(
            listeProductionUploadView.render(
                Production.find.where()
                    .orderBy("name ASC").findList()));
        } else {
            l = Compo.find.where().eq("voteOpen", true).findList();
            if (l != null && l.size() > 0) {
                return ok(gotoVoteProductionUploadView.render());
            }
            return redirect("/");
        }
    }

    public static Result show() {
        return ok(creationProductionUploadView.render(formulaire));
    }

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

        Production p = Production.find.where().eq("user",
            filledForm.field("user").value())
                .eq("compo",
                    filledForm.field("compo").value()).findUnique();

        Compo comp = Compo.find.where().eq("compo",
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
        } else {
            p.name = filledForm.field("name").value();
            p.comment = filledForm.field("comment").value();
            p.filename = prod.getFilename();
        }

        p.save();
        return GO_HOME;
    }
}

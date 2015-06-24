package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import views.html.userview.*;
import views.html.login;
import models.*;

public class UserCtl extends Controller {

    public static Result GO_HOME = redirect(
           routes.Application.index());

    static Form<User> formulaire = form(User.class);

/*    public static Result index() {
        return ok(
            listeUserView.render(
                User.find.where()
                    .orderBy("username ASC").findList()));
    }*/

    public static Result show() {
        return ok(creationUserView.render(formulaire));
    }

    public static Result create() {
        Form<User> filledForm = formulaire.bindFromRequest();
        if (filledForm.hasErrors()) {
            if (filledForm.field("username").valueOr("").isEmpty()) {
                filledForm.reject("username", "le nom est nécéssaire");
            }

            if (filledForm.field("password").valueOr("").isEmpty()) {
                filledForm.reject("password", "le mot de passe est necessaire");
            }

            if (filledForm.field("votekey").valueOr("").isEmpty()) {
                filledForm.reject("votekey", "la cle de vote est necessaire");
            }
            return badRequest(
                creationUserView.render(filledForm)
            );
        }


        User u = User.find.where().eq("username", filledForm.field("username").value()).findUnique();

        if (u != null) {
            filledForm.reject("username", "l'utilisateur est enregistré");
            return badRequest(
                    creationUserView.render(filledForm)
            );
        }

        Logger.debug("votekey. = " + filledForm.field("votekey").value());

        Votekey votekey = Votekey.find.where().eq("votekey",
                filledForm.field("votekey").value()).findUnique();

        if (votekey == null) {
            filledForm.reject("votekey","la cle est inconnue ou invalide");
            return badRequest(
                    creationUserView.render(filledForm)
            );
        }

        if (votekey.user != null) {
            filledForm.reject("votekey", "la cle a deja ete assignée");

            return badRequest(
                creationUserView.render(filledForm)
            );
        }


        filledForm.get().save();
        Logger.debug("filledform = " + filledForm.get());
        votekey.user = filledForm.get();
        Logger.debug("votekey= " + votekey);
        votekey.votekey = filledForm.field("votekey").value();
        votekey.save();

        return GO_HOME;
    }

    public static Result login() {
        return ok(
            login.render(Form.form(Login.class))
        );
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
            routes.Application.index()
        );
    }

    public static Result authenticate() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("username", loginForm.get().username);
            session("userid", loginForm.get().userid.toString());
            return redirect(routes.Application.index());
        }
    }

    public static class Login {
        public String username;
        public String password;
        public Long userid;

        public String validate() {
            User u = User.authenticate(username, password);
            if (u == null)
                return "Invalid user or password";
            userid = u.getId();
            return null;
        }
    }

}

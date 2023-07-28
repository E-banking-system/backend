package adria.sid.ebanckingbackend.utils.codeGenerators;

import adria.sid.ebanckingbackend.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class HtmlCodeGeneratorImpl implements HtmlCodeGenerator {

    private String getCommonStyles() {
        return "<style>"
                + "/* Styles pour rendre l'email attrayant */"
                + "body {"
                + "    font-family: Arial, sans-serif;"
                + "    background-color: #f5f5f5;"
                + "    color: #333;"
                + "    margin: 0;"
                + "    padding: 0;"
                + "}"
                + ".container {"
                + "    max-width: 600px;"
                + "    margin: 0 auto;"
                + "    padding: 20px;"
                + "    background-color: #fff;"
                + "    border-radius: 8px;"
                + "    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);"
                + "}"
                + "h1 {"
                + "    color: #007bff;"
                + "}"
                + "p {"
                + "    margin-bottom: 15px;"
                + "}"
                + "a {"
                + "    display: inline-block;"
                + "    padding: 10px 20px;"
                + "    background-color: #007bff;"
                + "    color: #fff;"
                + "    text-decoration: none;"
                + "    border-radius: 5px;"
                + "    font-weight: bold;"
                + "}"
                + ".text-link {"
                + "    color: white;"
                + "}"
                + "a:hover {"
                + "    background-color: #0056b3;"
                + "}"
                + "b {"
                + "    font-weight: bold;"
                + "}"
                + ".activate-link {"
                + "    color: #fff;"
                + "}"
                + "</style>";
    }

    @Override
    public String generateVerifiedEmailHTML(String message) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Email Verification</title>"
                + "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\">"
                + getCommonStyles()
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<h1>Welcome</h1>"
                + "<p>" + message + "</p>"
                + "<p>Merci<br><b>BANK XXX</b></p>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    @Override
    public String generateActivatedEmailHTML(String url, UserEntity userEntity) {
        String nom = userEntity.getNom();
        String prenom = userEntity.getPrenom();
        String raisonSociale = userEntity.getRaisonSociale();
        String displayName = (nom != null && prenom != null) ? (nom + " " + prenom) : raisonSociale;

        String mailContent = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Email Verification</title>"
                + "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\">"
                + getCommonStyles()
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<h1>Bienvenue, " + displayName + "!</h1>"
                + "<p>Merci pour votre souscription, veuillez suivre les instructions pour finaliser votre inscription.</p>"
                + "<a class=\"activate-link\" href=\"" + url + "\"><div class=\"text-link\">Cliquez ici pour activer votre compte utilisateur</div></a>"
                + "<p>Merci <br> <b>BANQUE XXX</b></p>"
                + "</div>"
                + "</body>"
                + "</html>";
        return mailContent;
    }

    @Override
    public String generateActivatedAccountInfoEmail(String pin, UserEntity userEntity) {
        String nom = userEntity.getNom();
        String prenom = userEntity.getPrenom();
        String raisonSociale = userEntity.getRaisonSociale();
        String displayName = (nom != null && prenom != null) ? (nom + " " + prenom) : raisonSociale;

        String mailContent = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Email Verification</title>"
                + "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\">"
                + getCommonStyles()
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<h1>Bienvenue, " + displayName + "!</h1>"
                + "<p>Merci pour votre fidélité.</p>"
                + "<h4>Voici le code PIN de votre compte bancaire : " + pin + "</h4>"
                + "<p>Merci <br> <b>BANQUE XXX</b></p>"
                + "</div>"
                + "</body>"
                + "</html>";
        return mailContent;
    }
    @Override
    public String generateResetPasswordEmailHTML(UserEntity userEntity,String url) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Reset Password</title>"
                + "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\">"
                + getCommonStyles()
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<h1>Bienvenue, " + userEntity.getNom() + "!</h1>"
                + "<h4>Consulter ce lien pour réinitialiser votre mot de passe: " + url + "</h4>"
                + "<p>Merci <br> <b>BANQUE XXX</b></p>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
}
